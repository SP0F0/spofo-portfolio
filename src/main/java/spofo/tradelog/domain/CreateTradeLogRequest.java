package spofo.tradelog.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import spofo.holdingstock.infrastructure.HoldingStockEntity;
import spofo.tradelog.domain.enums.TradeType;
import spofo.tradelog.infrastructure.TradeLogEntity;

@Data
@Builder
public class CreateTradeLogRequest {

    private HoldingStockEntity holdingStockEntity;
    private TradeType type;
    private BigDecimal price;
    private LocalDateTime tradeDate;
    private BigDecimal quantity;
    private BigDecimal marketPrice;

    public TradeLogEntity toEntity() {
        return null;
    }
}
