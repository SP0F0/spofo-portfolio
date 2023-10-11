package spofo.stockhave.infrastructure;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spofo.global.infrastructure.BaseEntity;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.stockhave.domain.StockHave;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.infrastructure.TradeLogEntity;

@Entity
@Table(name = "stock_have")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockHaveEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode; // 종목 코드 (FK)

    @JoinColumn(name = "portfolio_id")
    @ManyToOne(fetch = FetchType.LAZY) // Fecth 타입 Lazy 설정, Default: Eager
    @Setter
    private PortfolioEntity portfolioEntity;

    @OneToMany(mappedBy = "stockHaveEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TradeLogEntity> tradeLogEntities = new ArrayList<>();

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

    public void addStockHaveEntity(TradeLogEntity entity) {
        if (tradeLogEntities.size() > 0) {
            tradeLogEntities.remove(entity);
        }
        entity.setStockHaveEntity(this);
        tradeLogEntities.add(entity);
    }
}
