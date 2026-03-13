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
        // 1. 차량-ECU 매핑 정보 조회
        VehicleEcu mapping = vehicleEcuRepository.findByVehicle_VinAndEcu_TargetEcuId(req.getVehicleVIN(), req.getTargetEcuId())
                .orElseThrow(() -> new RuntimeException("잘못된 차량-ECU 매핑 정보입니다."));

        // 2. 해당 버전의 펌웨어 정보 조회 (이력 연결용)
        Firmware firmware = firmwareRepository.findByEcu_TargetEcuIdAndVersion(req.getTargetEcuId(), req.getSwVersion())
                .orElseThrow(() -> new RuntimeException("해당 펌웨어 버전을 찾을 수 없습니다."));

        // 3. UpdateHistory 엔티티 생성 및 저장 (Builder 활용)
        UpdateHistory history = historyRepository.findFirstByEcuVehicleAndFirmware_VersionAndStatusOrderByUpdateDateDesc(
                        mapping, req.getSwVersion(), UpdateStatus.DOWNLOADING)
                .orElseThrow(() -> new RuntimeException("진행 중인 업데이트 내역을 찾을 수 없습니다."));

        // 4. 성공 시 차량의 현재 SW 버전 업데이트
        if (req.getIsDone() == 1) {
            history.updateStatus(UpdateStatus.COMPLETED);
        }else{
            history.updateStatus(UpdateStatus.FAILED);
            history.updateFailureReason(req.getFailureReason());
        }

        history.updateUpdateDate(LocalDateTime.now());
        historyRepository.save(history);

        return history.getId();
    }
}
