package spofo.portfolio.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import spofo.global.infrastructure.BaseEntity;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.enums.Currency;
import spofo.portfolio.domain.enums.IncludeType;
import spofo.portfolio.domain.enums.PortfolioType;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.infrastructure.StockHaveEntity;

@Entity
@Table(name = "portfolio")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PortfolioEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'Y'")
    private IncludeType includeYn;

    @Enumerated(EnumType.STRING)
    private PortfolioType type;

    @OneToMany(mappedBy = "portfolioEntity")
    private List<StockHaveEntity> stockHaveEntities = new ArrayList<>();

    public static PortfolioEntity from(Portfolio portfolio) {
        PortfolioEntity entity = new PortfolioEntity();

        entity.id = portfolio.getId();
        entity.memberId = portfolio.getMemberId();
        entity.name = portfolio.getName();
        entity.description = portfolio.getDescription();
        entity.currency = portfolio.getCurrency();
        entity.includeYn = portfolio.getIncludeYn();
        entity.type = portfolio.getType();

        return entity;
    }

    public Portfolio toModel() {
        List<StockHave> stockHaves = stockHaveEntities.stream()
                .map(StockHaveEntity::toModel)
                .toList();

        return Portfolio.builder()
                .id(id)
                .memberId(memberId)
                .name(name)
                .description(description)
                .currency(currency)
                .includeYn(includeYn)
                .type(type)
                .stockHaves(stockHaves)
                .build();
    }
}
