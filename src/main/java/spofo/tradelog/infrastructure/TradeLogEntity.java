package spofo.tradelog.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.domain.enums.TradeType;

@Entity
@Table(name = "TRADE_LOG_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(columnDefinition = "VARCHAR(100) DEFAULT 'BUY'", nullable = false)
    @Column
    @Enumerated(EnumType.STRING)
    private TradeType type;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(updatable = false, nullable = false)
    private LocalDateTime tradeDate;

    @Column(precision = 30, scale = 15, nullable = false)
    private BigDecimal quantity;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal marketPrice;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    @Setter
    private StockHaveEntity stockHaveEntity;

    public static TradeLogEntity from(TradeLog tradeLog) {
        TradeLogEntity entity = new TradeLogEntity();

        entity.id = tradeLog.getId();
        entity.type = tradeLog.getType();
        entity.price = tradeLog.getPrice();
        entity.tradeDate = tradeLog.getTradeDate();
        entity.quantity = tradeLog.getQuantity();
        entity.marketPrice = tradeLog.getMarketPrice();
        entity.createdAt = tradeLog.getCreatedAt();

        return entity;
    }

    public TradeLog toModel() {
        return TradeLog.builder()
                .id(id)
                .type(type)
                .price(price)
                .tradeDate(tradeDate)
                .quantity(quantity)
                .marketPrice(marketPrice)
                .createdAt(createdAt)
                .build();
    }

}
