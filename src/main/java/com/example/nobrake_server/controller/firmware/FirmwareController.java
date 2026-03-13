package com.example.nobrake_server.controller.firmware;

import com.example.nobrake_server.dto.FirmwareDownloadRequest;
import com.example.nobrake_server.service.firmware.FirmwareService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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

    // 요청 형식(@RequestBody)은 유지, 응답만 Resource(바이너리)로 변경
    @PostMapping("/download")
    public ResponseEntity<Resource> downloadFirmware(@RequestBody FirmwareDownloadRequest request) {
        System.out.println("=============================Downloading firmware==================================");
        return firmwareService.getFirmwareBinary(request);
    }
}
