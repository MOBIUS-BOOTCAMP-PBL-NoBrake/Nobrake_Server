package com.example.nobrake_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ecu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Sig_TargetECU_ID (0~255) 매핑

    // CAN 메시지 매핑용 ID: Sig_TargetECU_ID (0~255)
    // 실제 통신 시 1바이트 할당량을 고려한 식별값
    @Column(nullable = false)
    private Integer targetEcuId;

    // ECU 부품 번호 (HW/SW 식별용)
    private String partNumber;

    public static Ecu createEcu(Integer targetEcuId, String partNumber) {
        return Ecu.builder()
                .targetEcuId(targetEcuId)
                .partNumber(partNumber)
                .build();
    }
}