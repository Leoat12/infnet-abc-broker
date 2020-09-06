package br.edu.infnet.abcbroker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MACD {

    private LocalDate date;
    private Double macd;
    private Double signal;
    private Double histogram;

}
