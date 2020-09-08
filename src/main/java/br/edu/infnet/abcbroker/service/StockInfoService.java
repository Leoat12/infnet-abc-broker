package br.edu.infnet.abcbroker.service;

import br.edu.infnet.abcbroker.model.MACD;
import br.edu.infnet.abcbroker.model.StockInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockInfoService {

    private final ResourceLoader resourceLoader;

    public List<StockInfo> getStockInfoFromFile() {
        try {
            Resource stockInfoResource = resourceLoader.getResource("classpath:MGLU3.SA.csv");
            List<String> lines = new BufferedReader(
                    new InputStreamReader(stockInfoResource.getInputStream(), StandardCharsets.UTF_8)
            )
                    .lines().collect(Collectors.toList());

            lines.remove(0);
            List<StockInfo> stockInfos = lines.stream()
                    .map(s -> {
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
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            calculateEMA(stockInfos, 9);
            calculateEMA(stockInfos, 12);
            calculateEMA(stockInfos, 26);
            calculateMACD(stockInfos);

            return stockInfos;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private void calculateSMA(List<StockInfo> stockInfos, int period) {

        int start = 0;
        int end = period;

        while (end < stockInfos.size()) {
            StockInfo endStock = stockInfos.get(end);
            if (Objects.isNull(endStock.getSma())) {
                endStock.setSma(new HashMap<>());
            }
            endStock.getSma().put(period, stockInfos.subList(start, end)
                    .stream().mapToDouble(StockInfo::getClose).sum() / period);
            start++;
            end++;
        }

    }

    /**
     * Initial SMA: 10-period sum / 10
     * <p>
     * Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
     * <p>
     * EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).
     *
     * @param stockInfos Information of the stock
     * @param period     The period desires for the EMA
     */
    private void calculateEMA(List<StockInfo> stockInfos, int period) {
        calculateSMA(stockInfos, period);
        int end = period;

        Double initialSma = stockInfos.get(end).getSma().get(period);
        double multiplier = (2.0 / (period + 1.0));

        Double previousEma = 0.0;
        while (end < stockInfos.size()) {
            StockInfo endStock = stockInfos.get(end);
            if (Objects.isNull(endStock.getEma())) {
                endStock.setEma(new HashMap<>());
            }
            double ema;
            if (end == period) {
                ema = (endStock.getClose() - initialSma) * multiplier + initialSma;
            } else {
                ema = (endStock.getClose() - previousEma) * multiplier + previousEma;
            }
            endStock.getEma().put(period, ema);
            previousEma = ema;
            end++;
        }
    }

    private void calculateMACD(List<StockInfo> stockInfos) {

        for (StockInfo si : stockInfos) {
            if (Objects.nonNull(si.getEma()) && si.getEma().containsKey(12) && si.getEma().containsKey(26)) {
                MACD macd = new MACD();
                macd.setDate(si.getDate());
                macd.setMacd(si.getEma().get(12) - si.getEma().get(26));
                si.setMacd(macd);
            }
        }

        calculateSignalLine(stockInfos);

        for (StockInfo si : stockInfos) {
            if (Objects.nonNull(si.getMacd()) && Objects.nonNull(si.getMacd().getMacd()) && Objects.nonNull(si.getMacd().getSignal())) {
                si.getMacd().setHistogram(si.getMacd().getMacd() - si.getMacd().getSignal());
            }
        }

    }

    private void calculateSignalLine(List<StockInfo> stockInfos) {
        List<StockInfo> macdLine = stockInfos.stream()
                .filter(stockInfo -> Objects.nonNull(stockInfo.getMacd()))
                .map(si ->
                        StockInfo.builder().date(si.getMacd().getDate()).close(si.getMacd().getMacd()).build()
                ).collect(Collectors.toList());

        calculateEMA(macdLine, 9);

        for (StockInfo stockInfo : stockInfos) {
            macdLine.stream().filter(m -> m.getDate().isEqual(stockInfo.getDate()) && Objects.nonNull(m.getEma()))
                    .findFirst().ifPresent(m ->
                    stockInfo.getMacd().setSignal(m.getEma().get(9))
            );
        }
    }
}
