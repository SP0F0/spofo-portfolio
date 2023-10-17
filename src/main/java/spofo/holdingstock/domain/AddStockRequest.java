package spofo.holdingstock.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spofo.holdingstock.infrastructure.HoldingStockEntity;
import spofo.portfolio.infrastructure.PortfolioEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddStockRequest {

    private String stockCode;
    private String tradeDate;
    private BigDecimal quantity;
    private BigDecimal avgPrice;

    public HoldingStockEntity toEntity(PortfolioEntity portfolioEntity) {

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
