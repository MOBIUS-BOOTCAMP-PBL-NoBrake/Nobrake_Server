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
        Vehicle vehicle = Vehicle.createVehicle("KMHCN51C6RU123456", "IONIQ5");
        vehicleRepository.save(vehicle);

        // 3. ECU 등록 (Engine, Brake)
        Ecu rightECU = Ecu.createEcu(52, "92190-L1000");
        ecuRepository.save(rightECU);

        // 4. 차량-ECU 매핑 (EcuVehicle)
        VehicleEcu mapping = VehicleEcu.of(vehicle, rightECU);
        vehicleEcuRepository.save(mapping);

        // 5. 시연용 타겟 펌웨어 등록 (10KB로 축소)
        long fileSize1 = 10240L; // 10KB 설정
        byte[] dummyBinary1 = new byte[(int) fileSize1];
        new java.util.Random().nextBytes(dummyBinary1);

        // 버전 1 등록
        Firmware firmware1 = Firmware.createFirmware(rightECU, 1, fileSize1, dummyBinary1);
        firmwareRepository.save(firmware1);

        // 버전 2 등록
        long fileSize2 = 10240L;
        byte[] dummyBinary2 = new byte[(int) fileSize2];
        new java.util.Random().nextBytes(dummyBinary2);

        Firmware firmware2 = Firmware.createFirmware(rightECU, 2, fileSize2, dummyBinary2);
        firmwareRepository.save(firmware2);
    }
}
