package com.example.nobrake_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHistoryRequest {
    private String vehicleVIN;      // 로그를 기록할 차량 식별자
    private Integer targetEcuId;   // Sig_TargetECU_ID
    private Integer swVersion;     // Sig_SwVersion
    private Integer updateStatus;        // Sig_UpdateDone (0: 실패, 1: 완료, 2: 롤백)
    private String failureReason;  // 실패 시 사유 (Optional)
}
