package com.example.nobrake_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirmwareDownloadRequest {
    private String vehicleId;      // Sig_VehicleID 매핑
    private String targetEcuId;   // Sig_TargetECU_ID 매핑
    private Integer requestedVersion; // Sig_RequestedVersion 매핑
}
