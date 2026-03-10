package com.example.nobrake_server.controller.firmware;

import com.example.nobrake_server.dto.FirmwareDownloadRequest;
import com.example.nobrake_server.service.firmware.FirmwareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/firmware")
@RequiredArgsConstructor
public class FirmwareController {

    private final FirmwareService firmwareService;

    @PostMapping("/download")
    public ResponseEntity<?> downloadFirmware(@RequestBody FirmwareDownloadRequest request) {
        return ResponseEntity.ok(firmwareService.getFirmwareData(request));
    }
}
