package spofo.holdingstock.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spofo.global.infrastructure.BaseEntity;
import spofo.holdingstock.domain.HoldingStock;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.infrastructure.TradeLogEntity;

@Entity
@Getter
@Table(name = "holding_stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoldingStockEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode; // 종목 코드 (FK)

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolioEntity;

    @OneToMany(mappedBy = "holdingStockEntity")
    private Set<TradeLogEntity> tradeLogEntities = new LinkedHashSet<>();

    public static HoldingStockEntity from(HoldingStock holdingStock) {
        HoldingStockEntity entity = new HoldingStockEntity();

        entity.id = holdingStock.getId();
        entity.stockCode = holdingStock.getStockCode();
        entity.portfolioEntity = PortfolioEntity.from(holdingStock.getPortfolio());

        return entity;
    }

    public HoldingStock toModel() {
        List<TradeLog> tradeLogs = tradeLogEntities.stream()
                .map(TradeLogEntity::toModel)
                .toList();

        return HoldingStock.builder()
                .id(id)
                .stockCode(stockCode)
                //.portfolio(portfolioEntity.toModel())
                .tradeLogs(tradeLogs)
                .build();
    }
}
