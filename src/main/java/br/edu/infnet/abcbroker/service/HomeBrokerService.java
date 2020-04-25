package br.edu.infnet.abcbroker.service;

import br.edu.infnet.abcbroker.model.StockInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HomeBrokerService {

    @Value("classpath:MGLU3.SA.csv")
    private Resource stockInfoFile;

    public List<StockInfo> getStockInfoFromFile() {
        return Collections.emptyList();
    }

}
