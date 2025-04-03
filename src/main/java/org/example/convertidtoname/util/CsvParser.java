package org.example.convertidtoname.util;

import org.example.convertidtoname.model.Trade;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvParser {
    public static Map<String, String> loadProductData(String filePath) {
        Map<String, String> productMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines().skip(1).forEach(line -> {
                String[] parts = line.split(",");
                productMap.put(parts[0], parts[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productMap;
    }

    public static List<Trade> parseTradeCsv(MultipartFile file, Map<String, String> productMap, Logger logger) {
        List<Trade> trades = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine();
            if (!isValidHeader(headerLine)) {
                throw new IllegalArgumentException("Invalid CSV format: Header is missing or incorrect, please try again. Expected header format: date,productId,currency,price");
            }
            reader.lines().skip(1).forEach(line -> {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    logger.error("Invalid CSV format: " + line);
                    parts = Arrays.copyOf(parts, 4);
                    for (int i = 0; i < 4; i++) {
                        if (parts[i] == null) {
                            parts[i] = "";
                        }
                    }
                }
                if (!isValidDate(parts[0])) {
                    logger.error("Invalid date format: " + parts[0]);
                    return;
                }
                try {
                    String productId = parts[1].toString();
                    String productName = productMap.getOrDefault(productId, "Missing Product Name");
                    String price = parts[3].toString();
                    trades.add(new Trade(parts[0], productName, parts[2], price));
                } catch (NumberFormatException e) {
                    logger.error("Invalid format in line: " + line, e);
                }
            });
        } catch (IOException e) {
            logger.error("Error reading CSV file", e);
            throw new IllegalArgumentException("Error processing CSV file", e);
        }
        return trades;
    }

    private static boolean isValidHeader(String headerLine) {
        if (headerLine == null || headerLine.trim().isEmpty()) {
            return false;
        }
        String[] headers = headerLine.split(",");
        return headers.length == 4 &&
                headers[0].trim().equalsIgnoreCase("date") &&
                headers[1].trim().equalsIgnoreCase("productId") &&
                headers[2].trim().equalsIgnoreCase("currency") &&
                headers[3].trim().equalsIgnoreCase("price");
    }

    public static String convertToCsv(List<Trade> trades) {
        StringBuilder sb = new StringBuilder("date,productName,currency,price\n");
        for (Trade trade : trades) {
            sb.append(String.format("%s,%s,%s,%s\n",
                    trade.getDate(), trade.getProductName(), trade.getCurrency(), trade.getPrice()));
        }
        return sb.toString();
    }

    private static boolean isValidDate(String date) {
        return date.matches("\\d{8}");
    }

    public static Map<String, String> loadProductDataFromStream(InputStream inputStream) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load product data", e);
        }
        return map;
    }

}
