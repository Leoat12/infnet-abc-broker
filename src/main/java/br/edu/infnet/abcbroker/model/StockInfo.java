package br.edu.infnet.abcbroker.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class StockInfo {

    private LocalDate date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double adjClose;
    private Double volume;
    private Map<Integer, Double> sma;
    private Map<Integer, Double> ema;

}
