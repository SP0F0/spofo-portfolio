package spofo.tradelog.domain;

import static spofo.portfolio.domain.enums.IncludeType.Y;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.stockhave.domain.StockHave;
import spofo.tradelog.domain.enums.TradeType;

@Getter
@Builder
public class TradeLog {

    private final Long id;
    private final TradeType type;
    private final BigDecimal price;
    private final LocalDateTime tradeDate;
    private final BigDecimal quantity;
    private final BigDecimal marketPrice;
    private final LocalDateTime createdAt;
    private final StockHave stockHave;

    public static TradeLog of(TradeLogCreate create, StockHave stockHave) {
        return TradeLog.builder()
                .type(create.getType())
                .price(create.getPrice())
                .tradeDate(create.getTradeDate())
                .quantity(create.getQuantity())
                .marketPrice(create.getMarketPrice())
                .stockHave(stockHave)
                .build();
    }

//    public TradeLog update(TradeLogUpdate update, Long memberId) {
//        return Portfolio.builder()
//                .id(update.getId())
//                .name(update.getName())
//                .description(update.getDescription())
//                .currency(update.getCurrency())
//                .includeYn(update.getIncludeYn())
//                .type(update.getType())
//                .memberId(memberId)
//                .build();
//    }
}