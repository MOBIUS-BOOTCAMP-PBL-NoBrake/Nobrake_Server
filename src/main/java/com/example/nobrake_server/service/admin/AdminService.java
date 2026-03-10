package com.example.nobrake_server.service.admin;

import com.example.nobrake_server.entity.Ecu;
import com.example.nobrake_server.entity.Firmware;
import com.example.nobrake_server.entity.Vehicle;
import com.example.nobrake_server.entity.VehicleEcu;
import com.example.nobrake_server.repository.EcuRepository;
import com.example.nobrake_server.repository.FirmwareRepository;
import com.example.nobrake_server.repository.VehicleEcuRepository;
import com.example.nobrake_server.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final VehicleRepository vehicleRepository;
    private final EcuRepository ecuRepository;
    private final VehicleEcuRepository vehicleEcuRepository;
    private final FirmwareRepository firmwareRepository;

    @Transactional
    public void initMockData() {
        // 1. 기존 데이터 삭제 (중복 생성 방지)
        vehicleEcuRepository.deleteAll();
        firmwareRepository.deleteAll();
        vehicleRepository.deleteAll();
        ecuRepository.deleteAll();

        // 2. 차량 등록 (Sig_VehicleID: 1234)
        Vehicle vehicle = Vehicle.createVehicle();
        vehicleRepository.save(vehicle);

        // 3. ECU 등록 (Engine, Brake)
        Ecu rightECU = Ecu.createEcu();
        ecuRepository.save(rightECU);

        // 4. 차량-ECU 매핑 (EcuVehicle)
        VehicleEcu mapping = VehicleEcu.of(vehicle, rightECU);
        vehicleEcuRepository.save(mapping);

        // 5. 시연용 타겟 펌웨어 등록 (Version: 200)
        long fileSize = 524288L; // 512KB 설정
        byte[] dummyBinary = new byte[(int) fileSize];

        new java.util.Random().nextBytes(dummyBinary);
        Firmware firmware = Firmware.createFirmware(rightECU, 2,fileSize, dummyBinary);
        firmwareRepository.save(firmware);
    }
}
