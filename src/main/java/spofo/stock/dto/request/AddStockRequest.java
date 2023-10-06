package spofo.stock.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spofo.portfolio.entity.Portfolio;
import spofo.stock.entity.StockHave;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddStockRequest {

    private String stockCode;
    private String tradeDate;
    private BigDecimal quantity;
    private BigDecimal avgPrice;

    public StockHave toEntity(Portfolio portfolio) {
        return StockHave.builder()
                .stockCode(stockCode)
                .portfolio(portfolio)
                .build();
    }

//    public StockHave toEntity(Portfolio portfolio, StockHave stockHave) {
//        return StockHave.builder()
//                .id(stockHave.getId())
//                .stockCode(stockCode)
//                .portfolio(portfolio)
//                .build();
//    }

}
