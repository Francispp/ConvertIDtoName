package org.example.convertidtoname.controller;

import org.example.convertidtoname.service.TradeEnrichmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class TradeEnrichmentController {
    private final TradeEnrichmentService tradeEnrichmentService;

    public TradeEnrichmentController(TradeEnrichmentService service) {
        this.tradeEnrichmentService = service;
    }

    @PostMapping("/enrich")
    public ResponseEntity<Resource> enrichTrades(@RequestParam("file") MultipartFile file) {
        String enrichedCsv = tradeEnrichmentService.processTrades(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new ByteArrayResource(enrichedCsv.getBytes()));
    }
}

