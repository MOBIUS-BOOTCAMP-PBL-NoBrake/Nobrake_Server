package com.example.nobrake_server.controller.ota;

import com.example.nobrake_server.dto.UpdateHistoryRequest;
import com.example.nobrake_server.service.ota.OtaHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ota")
@RequiredArgsConstructor
public class OtaController {

    private final OtaHistoryService otaHistoryService;

    @PostMapping("/history")
    public ResponseEntity<?> saveHistory(@RequestBody UpdateHistoryRequest request) {
        System.out.println("ecu ID : " + request.getTargetEcuId());

        UUID historyId = otaHistoryService.saveUpdateLog(request);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "업데이트 기록이 성공적으로 저장되었습니다.",
                "historyId", historyId
        ));
    }
}