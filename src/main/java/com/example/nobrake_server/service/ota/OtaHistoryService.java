package com.example.nobrake_server.service.ota;

import com.example.nobrake_server.dto.UpdateHistoryRequest;
import com.example.nobrake_server.entity.Firmware;
import com.example.nobrake_server.entity.UpdateHistory;
import com.example.nobrake_server.entity.UpdateStatus;
import com.example.nobrake_server.entity.VehicleEcu;
import com.example.nobrake_server.repository.FirmwareRepository;
import com.example.nobrake_server.repository.UpdateHistoryRepository;
import com.example.nobrake_server.repository.VehicleEcuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtaHistoryService {

    private final UpdateHistoryRepository historyRepository;
    private final VehicleEcuRepository vehicleEcuRepository;
    private final FirmwareRepository firmwareRepository;

    @Transactional
    public UUID saveUpdateLog(UpdateHistoryRequest req) {
        // 1. 공통 정보 조회 (차량-ECU 매핑)
        VehicleEcu mapping = vehicleEcuRepository.findByVehicle_VinAndEcu_TargetEcuId(req.getVehicleVIN(), req.getTargetEcuId())
                .orElseThrow(() -> new RuntimeException("잘못된 차량-ECU 매핑 정보입니다."));

        // 2. 상태값에 따른 로직 분기 (상태 2: 롤백, 그 외: 일반 업데이트)
        if (req.getUpdateStatus() == 2) {
            return handleRollback(req, mapping);
        }

        return handleNormalUpdate(req, mapping);
    }

    /**
     * [롤백 처리 핸들러]
     * 이미 완료된 이력을 롤백으로 마감하고, 이전 버전의 성공 이력을 새로 생성합니다.
     */
    private UUID handleRollback(UpdateHistoryRequest req, VehicleEcu mapping) {
        // [A] 현재 설치된 버전(예: 2)의 내역을 롤백 마감
        UpdateHistory failedHistory = historyRepository
                .findFirstByEcuVehicleAndStatusOrderByUpdateDateDesc(mapping, UpdateStatus.COMPLETED)
                .orElseThrow(() -> new RuntimeException("롤백할 대상(완료된 내역)이 없습니다."));

        failedHistory.updateStatus(UpdateStatus.ROLLED_BACK);
        failedHistory.updateUpdateDate(LocalDateTime.now());
        historyRepository.save(failedHistory);

        // [B] 버전 연산을 통해 이전 버전(예: 2 - 1 = 1)을 직접 조회
        // 굳이 LessThan 정렬 쿼리를 쓰지 않고, 정확한 버전을 찍어서 가져옵니다.

        Firmware previousFirmware = firmwareRepository
                .findByEcu_TargetEcuIdAndVersion(req.getTargetEcuId(), req.getSwVersion())
                .orElseThrow(() -> new RuntimeException("해당하는 펌웨어가 존재하지 않습니다."));

        // [C] 롤백 성공 이력 생성
        UpdateHistory rollbackSuccessEntry = UpdateHistory.builder()
                .ecuVehicle(mapping)
                .firmware(previousFirmware)
                .status(UpdateStatus.COMPLETED)
                .updateDate(LocalDateTime.now())
                .build();

        return historyRepository.save(rollbackSuccessEntry).getId();
    }

    /**
     * [일반 업데이트 핸들러]
     * 진행 중(DOWNLOADING)인 이력을 찾아 성공(COMPLETED) 또는 실패(FAILED)로 마감합니다.
     */
    private UUID handleNormalUpdate(UpdateHistoryRequest req, VehicleEcu mapping) {
        UpdateHistory history = historyRepository
                .findFirstByEcuVehicleAndFirmware_VersionAndStatusOrderByUpdateDateDesc(
                        mapping, req.getSwVersion(), UpdateStatus.DOWNLOADING)
                .orElseThrow(() -> new RuntimeException("진행 중인 업데이트 내역을 찾을 수 없습니다."));

        if (req.getUpdateStatus() == 1) { // 성공
            history.updateStatus(UpdateStatus.COMPLETED);
        } else { // 실패 (0 또는 기타)
            history.updateStatus(UpdateStatus.FAILED);
            history.updateFailureReason(req.getFailureReason());
        }

        history.updateUpdateDate(LocalDateTime.now());
        return historyRepository.save(history).getId();
    }
}
