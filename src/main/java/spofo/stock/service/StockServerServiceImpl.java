package spofo.stock.service;

import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import spofo.global.domain.enums.Server;
import spofo.stock.domain.Stock;

@Service
@RequiredArgsConstructor
public class StockServerServiceImpl implements StockServerService {

    private final RestClient restClient;

    @Override
    public Stock getStock(String stockCode) {
        return restClient.get()
                .uri(Server.STOCKSERVER.getUri("/stocks/" + stockCode))
                .retrieve()
                .body(Stock.class);
    }

    @Override
    public Map<String, Stock> getStocks(List<String> stockCodes) {
        // todo stock 정보를 리스트로 조회
        List<Stock> stocks = Collections.emptyList();
        return stocks.stream()
                .collect(toMap(Stock::getCode, stock -> stock, (s1, s2) -> s1));
    }
}
