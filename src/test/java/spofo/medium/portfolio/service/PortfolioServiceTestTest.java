package spofo.medium.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.PortfolioType.REAL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.support.service.ServiceTestSupport;

public class PortfolioServiceTestTest extends ServiceTestSupport {

    @Test
    @DisplayName("포트폴리오를 등록한다.")
    void createPortfolio() {
        // given
        Long memberId = 1L;
        String name = "portfolio name";
        String description = "portfolio description";

        PortfolioCreate portfolioCreate = PortfolioCreate.builder()
                .name(name)
                .description(description)
                .currency(KRW)
                .type(REAL)
                .build();

        // when
        Portfolio savedPortfolio = portfolioService.create(portfolioCreate, memberId);

        // then
        assertThat(savedPortfolio.getId()).isNotNull();
        assertThat(savedPortfolio.getName()).isEqualTo(name);
        assertThat(savedPortfolio.getDescription()).isEqualTo(description);
        assertThat(savedPortfolio.getType()).isEqualTo(REAL);
        assertThat(savedPortfolio.getCurrency()).isEqualTo(KRW);
        assertThat(savedPortfolio.getMemberId()).isEqualTo(memberId);
    }
}
