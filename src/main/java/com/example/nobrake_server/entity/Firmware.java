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
public class Firmware {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID firmwareId;

    @ManyToOne
    @JoinColumn(name = "ecu_id")
    private Ecu ecu;

    private Integer version; // Sig_SwVersion 매핑
    private Long fileSize;

    @Lob
    private byte[] binaryData; // 펌웨어 파일 데이터

    public static Firmware createFirmware(Ecu ecu, Integer version, Long fileSize, byte[] binaryData){
        return Firmware.builder()
                .ecu(ecu)
                .version(version)
                .fileSize(fileSize)
                .binaryData(binaryData)
                .build();
    }
}