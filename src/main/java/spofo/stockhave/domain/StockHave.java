package spofo.stockhave.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import spofo.portfolio.domain.Portfolio;
import spofo.tradelog.domain.TradeLog;

@Getter
@Builder
public class StockHave {

    private Long id;
    private String stockCode;
    private Portfolio portfolio;
    private List<TradeLog> tradeLogs;

    public static StockHave of(StockHaveCreate stockHaveCreate, Portfolio portfolio) {
        return StockHave.builder()
                .stockCode(stockHaveCreate.getStockCode())
                .portfolio(portfolio)
                .build();
    }
}
