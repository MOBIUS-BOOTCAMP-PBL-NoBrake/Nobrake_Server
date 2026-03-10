package com.example.nobrake_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHistoryRequest {
    private String vehicleId;      // 로그를 기록할 차량 식별자
    private String targetEcuId;   // Sig_TargetECU_ID
    private Integer swVersion;     // Sig_SwVersion
    private Integer isDone;        // Sig_UpdateDone (0: 실패, 1: 완료)
    private String failureReason;  // 실패 시 사유 (Optional)
}
