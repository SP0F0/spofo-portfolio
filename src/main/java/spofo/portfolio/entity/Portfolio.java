package spofo.portfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import spofo.global.entity.Date;
import spofo.portfolio.enums.IncludeType;
import spofo.portfolio.enums.PortfolioType;

@Entity
@Table(name = "Portfolio")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@DynamicInsert
public class Portfolio extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String name;

    private String description;

    private String currency;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'Y'")
    private IncludeType includeYn;

    @Enumerated(EnumType.STRING)
    private PortfolioType type;

    public void toUpdate(String name, String description, IncludeType includeYn) {
        this.name = name;
        this.description = description;
        this.includeYn = includeYn;
    }
}
