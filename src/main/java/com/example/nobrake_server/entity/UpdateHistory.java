package com.example.nobrake_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_ecu_id")
    private VehicleEcu ecuVehicle; // 매핑 테이블 참조

    @ManyToOne
    @JoinColumn(name = "firmware_id")
    private Firmware firmware;

    @Enumerated(EnumType.STRING)
    private UpdateStatus status; // COMPLETED, FAILED, ROLLED_BACK

    private LocalDateTime updateDate; // Sig_UpdateDate 매핑

    private String failureReason; // Sig_RejectReason 매핑

    public static UpdateHistory createHistory(VehicleEcu ecuVehicle, Firmware firmware) {
        return UpdateHistory.builder()
                .ecuVehicle(ecuVehicle)
                .firmware(firmware)
                .status(UpdateStatus.DOWNLOADING)
                .updateDate(LocalDateTime.now())
                .build();
    }
}
