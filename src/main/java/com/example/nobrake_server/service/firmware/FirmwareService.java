package com.example.nobrake_server.service.firmware;

import com.example.nobrake_server.dto.FirmwareDownloadRequest;
import com.example.nobrake_server.entity.Firmware;
import com.example.nobrake_server.entity.UpdateHistory;
import com.example.nobrake_server.entity.Vehicle;
import com.example.nobrake_server.entity.VehicleEcu;
import com.example.nobrake_server.repository.FirmwareRepository;
import com.example.nobrake_server.repository.UpdateHistoryRepository;
import com.example.nobrake_server.repository.VehicleEcuRepository;
import com.example.nobrake_server.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirmwareService {

    private final FirmwareRepository firmwareRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleEcuRepository vehicleEcuRepository;
    private final UpdateHistoryRepository updateHistoryRepository;

    public Map<String, Object> getFirmwareData(FirmwareDownloadRequest req) {
// 1. 차량 유효성 검사
        Vehicle vehicle = vehicleRepository.findById(UUID.fromString(req.getVehicleId()))
                .orElseThrow(() -> new RuntimeException("등록되지 않은 차량 ID입니다."));

        // 2. 차량-ECU 매핑 정보 조회 (UpdateHistory의 외래키를 위함)
        VehicleEcu vehicleEcu = vehicleEcuRepository.findByVehicle_IdAndEcu_Id(UUID.fromString(req.getVehicleId()), UUID.fromString(req.getTargetEcuId()))
                .orElseThrow(() -> new RuntimeException("해당 차량에 연결된 ECU 정보를 찾을 수 없습니다."));

        // 3. 펌웨어 조회
        Firmware firmware = firmwareRepository.findByEcu_IdAndVersion(UUID.fromString(req.getTargetEcuId()), req.getRequestedVersion())
                .orElseThrow(() -> new RuntimeException("해당 조건의 펌웨어가 존재하지 않습니다."));

        // 4. UpdateHistory 생성 및 저장 (상태: DOWNLOADING)
        UpdateHistory history = UpdateHistory.createHistory(vehicleEcu, firmware);
        updateHistoryRepository.save(history);

        // 5. 응답 데이터 구성
        String base64Binary = Base64.getEncoder().encodeToString(firmware.getBinaryData());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("firmwareId", firmware.getFirmwareId());
        responseData.put("version", firmware.getVersion());
        responseData.put("fileSize", firmware.getFileSize());
        responseData.put("checksum", "a1b2c3d4...");
        responseData.put("binaryData", base64Binary);

        return Map.of("status", "SUCCESS", "data", responseData);
    }
}
