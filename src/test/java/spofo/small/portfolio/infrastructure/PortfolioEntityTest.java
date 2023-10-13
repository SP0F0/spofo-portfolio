package spofo.small.portfolio.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.IncludeType.Y;
import static spofo.portfolio.domain.enums.PortfolioType.FAKE;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.infrastructure.StockHaveEntity;

public class PortfolioEntityTest {

    @Test
    @DisplayName("포트폴리오로 포트폴리오 엔티티를 만든다.")
    void PortfolioToPortfolioEntity() {
        //given
        Portfolio portfolio = createPortfolio();

        //when
        PortfolioEntity entity = PortfolioEntity.from(portfolio);

        //then
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getMemberId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("모의 포트폴리오");
        assertThat(entity.getDescription()).isEqualTo("모의 포트폴리오입니다.");
        assertThat(entity.getCurrency()).isEqualTo(KRW);
        assertThat(entity.getIncludeType()).isEqualTo(Y);
        assertThat(entity.getType()).isEqualTo(FAKE);
    }

    @Test
    @DisplayName("포트폴리오 엔티티로 포트폴리오를 만든다.")
    void PortfolioEntityToPortfolioWithStockHaves() {
        // given
        Portfolio portfolio = createPortfolio();
        PortfolioEntity portfolioEntity = PortfolioEntity.from(portfolio);

        StockHave stockHave = StockHave.builder()
                .portfolio(portfolio)
                .build();

        setField(portfolioEntity, "stockHaveEntities", Set.of(StockHaveEntity.from(stockHave)));

        // when
        Portfolio model = portfolioEntity.toModel();

        // then
        assertThat(model.getId()).isEqualTo(1L);
        assertThat(model.getMemberId()).isEqualTo(1L);
        assertThat(model.getName()).isEqualTo("모의 포트폴리오");
        assertThat(model.getDescription()).isEqualTo("모의 포트폴리오입니다.");
        assertThat(model.getCurrency()).isEqualTo(KRW);
        assertThat(model.getIncludeType()).isEqualTo(Y);
        assertThat(model.getType()).isEqualTo(FAKE);
        assertThat(model.getStockHaves()).hasSize(1);
    }

    @Test
    @DisplayName("StockHave가 없을 때 포트폴리오 엔티티로 포트폴리오를 만든다.")
    void PortfolioEntityToPortfolioWithoutStockHaves() {
        // given
        Portfolio portfolio = createPortfolio();
        PortfolioEntity portfolioEntity = PortfolioEntity.from(portfolio);

        // when
        Portfolio model = portfolioEntity.toModel();

        // then
        assertThat(model.getId()).isEqualTo(1L);
        assertThat(model.getMemberId()).isEqualTo(1L);
        assertThat(model.getName()).isEqualTo("모의 포트폴리오");
        assertThat(model.getDescription()).isEqualTo("모의 포트폴리오입니다.");
        assertThat(model.getCurrency()).isEqualTo(KRW);
        assertThat(model.getIncludeType()).isEqualTo(Y);
        assertThat(model.getType()).isEqualTo(FAKE);
        assertThat(model.getStockHaves()).hasSize(0);
    }

    private Portfolio createPortfolio() {
        return Portfolio.builder()
                .id(1L)
                .memberId(1L)
                .name("모의 포트폴리오")
                .description("모의 포트폴리오입니다.")
                .currency(KRW)
                .includeType(Y)
                .type(FAKE)
                .build();
    }
}
