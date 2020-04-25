package br.edu.infnet.abcbroker.service;

import br.edu.infnet.abcbroker.model.StockInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockInfoService {

    @Value("classpath:MGLU3.SA.csv")
    private Resource stockInfoResource;

    public List<StockInfo> getStockInfoFromFile() {
        try {
            File stockInfoFile = stockInfoResource.getFile();
            List<String> lines = Files.readAllLines(stockInfoFile.toPath(), StandardCharsets.UTF_8);
            lines.remove(0);
            return lines.stream().map(s -> {
                try {
                    String[] fields = s.split(",");
                    return StockInfo.builder()
                            .date(LocalDate.parse(fields[0]))
                            .open(Double.parseDouble(fields[1]))
                            .high(Double.parseDouble(fields[2]))
                            .low(Double.parseDouble(fields[3]))
                            .close(Double.parseDouble(fields[4]))
                            .adjClose(Double.parseDouble(fields[5]))
                            .volume(Double.parseDouble(fields[6]))
                            .build();
                } catch (NumberFormatException e) {
                    return null;
                }
            })
                    .filter(s -> s != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
