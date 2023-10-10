package spofo.tradelog.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import spofo.tradelog.domain.enums.TradeType;

@Getter
@Builder
public class TradeLog {

    private Long id;
    private TradeType type;
    private BigDecimal price;
    private LocalDateTime tradeDate;
    private BigDecimal quantity;
    private BigDecimal marketPrice;
    private LocalDateTime createdAt;

    public static TradeLog of() {
        // 얘는 컨트롤러가 아닌 서비스에서 주입받아 만드는 것이므로 뭐가 필요한지 보고 처리!
        return null;
        /*
        return TradeLog.builder()
                .type(tradeLogCreate.getType())
                .price(tradeLogCreate.getPrice())
                .tradeDate(tradeLogCreate.getTradeDate())
                .quantity(tradeLogCreate.getQuantity())
                .marketPrice(tradeLogCreate.getMarketPrice())
                .createdAt(tradeLogCreate.getCreatedAt())
                .build();

         */
    }
}