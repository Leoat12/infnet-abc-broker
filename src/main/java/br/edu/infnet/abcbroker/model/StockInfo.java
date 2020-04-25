package br.edu.infnet.abcbroker.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StockInfo {

    private LocalDate date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double adjClose;
    private Double volume;

}
