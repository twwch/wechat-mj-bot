package com.chtw.midjourney.controller;

import com.chtw.midjourney.result.ResponseData;
import com.chtw.midjourney.dto.RequestTrigger;
import com.chtw.midjourney.service.MidjourneyService;
import com.chtw.midjourney.storage.mj.document.ImagineLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/midjourney")
public class MidjourneyController {

    private final MidjourneyService midjourneyService;

    @Autowired
    public MidjourneyController(MidjourneyService midjourneyService) {
        this.midjourneyService = midjourneyService;
    }

    @PostMapping("/imagine")
    public ResponseEntity<ResponseData> midjourneyBot(@RequestBody RequestTrigger requestTrigger) {
        try {
            midjourneyService.executeAction(requestTrigger);
            return ResponseEntity.ok(ResponseData.builder()
                    .message("success")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/imagine/save")
    public ResponseEntity<ResponseData> imagineBot(@RequestBody ImagineLogs data) {
        try {
            midjourneyService.saveAndExecuteAction(data);
            return ResponseEntity.ok(ResponseData.builder()
                    .message("success")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseData.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}