package br.edu.infnet.abcbroker.controller;

import br.edu.infnet.abcbroker.model.StockInfo;
import br.edu.infnet.abcbroker.service.StockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
public class StockInfoController {

    private final StockInfoService stockInfoService;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public List<StockInfo> getStockInfo() {
        return stockInfoService.getStockInfoFromFile();
    }
}
