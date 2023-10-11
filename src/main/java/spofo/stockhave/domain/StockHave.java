package spofo.stockhave.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import spofo.portfolio.domain.Portfolio;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.infrastructure.TradeLogEntity;

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

    public StockHave addTradeLog(TradeLog tradeLog) {
        StockHaveEntity entity = StockHaveEntity.from(this);
        entity.addStockHaveEntity(TradeLogEntity.from(tradeLog));
        return entity.toModel();
    }
}
