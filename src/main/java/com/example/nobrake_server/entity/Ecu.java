package com.example.nobrake_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    public static Ecu createEcu(){
        return Ecu.builder()
                .build();
    }
}