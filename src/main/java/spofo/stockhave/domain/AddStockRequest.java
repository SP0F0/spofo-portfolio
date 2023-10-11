package spofo.stockhave.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.stockhave.infrastructure.StockHaveEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddStockRequest {

    private String stockCode;
    private String tradeDate;
    private BigDecimal quantity;
    private BigDecimal avgPrice;

    public StockHaveEntity toEntity(PortfolioEntity portfolioEntity) {

        return null;
    }

//    public StockHave toEntity(Portfolio portfolio, StockHave stockHave) {
//        return StockHave.builder()
//                .id(stockHave.getId())
//                .stockCode(stockCode)
//                .portfolio(portfolio)
//                .build();
//    }

}
