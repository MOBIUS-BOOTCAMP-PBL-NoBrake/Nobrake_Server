package com.example.nobrake_server.controller.admin;

import com.example.nobrake_server.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/init")
    public ResponseEntity<?> initializeData() {
        adminService.initMockData();
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "시연용 기초 데이터가 성공적으로 생성되었습니다."
        ));
    }
}
