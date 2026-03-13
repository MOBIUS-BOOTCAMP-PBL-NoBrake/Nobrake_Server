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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Resource> getFirmwareBinary(FirmwareDownloadRequest req) {
        // 1. 차량 유효성 검사
        Vehicle vehicle = vehicleRepository.findByVin(req.getVehicleVIN())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 차량 VIN입니다."));

        // 2. 차량-ECU 매핑 정보 조회
        VehicleEcu vehicleEcu = vehicleEcuRepository.findByVehicle_VinAndEcu_TargetEcuId(
                        req.getVehicleVIN(), req.getTargetEcuId())
                .orElseThrow(() -> new RuntimeException("해당 차량에 연결된 ECU 정보를 찾을 수 없습니다."));

        // 3. 펌웨어 조회
        Firmware firmware = firmwareRepository.findByEcu_TargetEcuIdAndVersion(
                        req.getTargetEcuId(), req.getRequestedVersion())
                .orElseThrow(() -> new RuntimeException("해당 조건의 펌웨어가 존재하지 않습니다."));

        // 4. UpdateHistory 생성 및 저장 (상태: DOWNLOADING)
        UpdateHistory history = UpdateHistory.createHistory(vehicleEcu, firmware);
        updateHistoryRepository.save(history);

        // 5. 응답 데이터 구성 (Base64 변환 없이 바이너리 그대로 사용)
        byte[] binaryData = firmware.getBinaryData();
        ByteArrayResource resource = new ByteArrayResource(binaryData);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(binaryData.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"firmware_" + firmware.getVersion() + ".bin\"")
                // CAPL에서 버전 확인용으로 쓸 헤더
                .header("X-OTA-Version", String.valueOf(firmware.getVersion()))
                .body(resource);
    }
}
