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
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 실물 차량 식별자: 17자리 표준 (ISO 3779)
    /*
    WMI (1~3자리): 제조국 및 제조사 정보 (예: 현대자동차 한국 생산분은 KMH)
    VDS (4~9자리): 차량의 특성 (모델, 바디 타입, 엔진 유형, 제동 장치 등)
    VIS (10~17자리): 제작 연도, 생산 공장, 그리고 해당 공장의 생산 일련번호
     */
    @Column(unique = true, nullable = false, length = 17)
    private String vin;

    // 차량 모델명 (예: IONIQ6, GV80 등)
    private String modelName;

    public static Vehicle createVehicle(String vin, String modelName) {
        return Vehicle.builder()
                .vin(vin)
                .modelName(modelName)
                .build();
    }
}