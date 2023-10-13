package spofo.stockhave.infrastructure;

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
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.stockhave.domain.StockHave;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.infrastructure.TradeLogEntity;

@Entity
@Getter
@Table(name = "stock_have")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockHaveEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode; // 종목 코드 (FK)

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolioEntity;

    @OneToMany(mappedBy = "stockHaveEntity")
    private Set<TradeLogEntity> tradeLogEntities = new LinkedHashSet<>();

    public static StockHaveEntity from(StockHave stockHave) {
        StockHaveEntity entity = new StockHaveEntity();

        entity.id = stockHave.getId();
        entity.stockCode = stockHave.getStockCode();
        entity.portfolioEntity = PortfolioEntity.from(stockHave.getPortfolio());

        return entity;
    }

    public StockHave toModel() {
        List<TradeLog> tradeLogs = tradeLogEntities.stream()
                .map(TradeLogEntity::toModel)
                .toList();

        return StockHave.builder()
                .id(id)
                .stockCode(stockCode)
                .portfolio(portfolioEntity.toModel())
                .tradeLogs(tradeLogs)
                .build();
    }
}
