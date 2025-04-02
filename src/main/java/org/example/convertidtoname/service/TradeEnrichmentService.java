package org.example.convertidtoname.service;

import org.example.convertidtoname.model.Trade;
import org.example.convertidtoname.util.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class TradeEnrichmentService {
    @Value("classpath:static/product.csv")
    private File productFile;

    private final Logger logger = LoggerFactory.getLogger(TradeEnrichmentService.class);

    public String processTrades(MultipartFile file) {
        Map<String, String> productMap = CsvParser.loadProductData(productFile.getPath());
        List<Trade> trades = CsvParser.parseTradeCsv(file, productMap, logger);
        return CsvParser.convertToCsv(trades);
    }
}
