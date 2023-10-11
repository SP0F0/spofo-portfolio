package spofo.medium.portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static spofo.global.domain.exception.ErrorCode.PORTFOLIO_NOT_FOUND;
import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.PortfolioType.REAL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.global.domain.exception.PortfolioNotFound;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.PortfoliosStatistic;
import spofo.support.service.ServiceTestSupport;

public class PortfolioServiceImplTest extends ServiceTestSupport {

    private static final String PORTFOLIO_CREATE_NAME = "포트폴리오 생성";
    private static final String PORTFOLIO_CREATE_DESC = "포트폴리오 생성입니다.";
    private static final String PORTFOLIO_UPDATE_NAME = "포트폴리오 수정";
    private static final String PORTFOLIO_UPDATE_DESC = "포트폴리오 수정입니다.";
    private static final Long MEMBER_ID = 1L;

    @Test
    @DisplayName("포트폴리오를 등록한다.")
    void createPortfolio() {
        // given
        PortfolioCreate createPortfolio = getCreatePortfolio();

        // when
        Portfolio savedPortfolio = portfolioService.create(createPortfolio, MEMBER_ID);

        // then
        assertThat(savedPortfolio.getId()).isNotNull();
        assertThat(savedPortfolio.getName()).isEqualTo(PORTFOLIO_CREATE_NAME);
        assertThat(savedPortfolio.getDescription()).isEqualTo(PORTFOLIO_CREATE_DESC);
        assertThat(savedPortfolio.getType()).isEqualTo(REAL);
        assertThat(savedPortfolio.getCurrency()).isEqualTo(KRW);
        assertThat(savedPortfolio.getMemberId()).isEqualTo(MEMBER_ID);
    }

    @Test
    @DisplayName("포트폴리오를 수정한다.")
    void updatePortfolio() {
        // given
        Portfolio savedPortfolio = portfolioService.create(getCreatePortfolio(), MEMBER_ID);

        PortfolioUpdate updatePortfolio = getUpdatePortfolio(savedPortfolio.getId());

        // when
        Portfolio updatedPortfolio = portfolioService
                .update(updatePortfolio, savedPortfolio.getId(), MEMBER_ID);

        // then
        assertThat(updatedPortfolio.getId()).isEqualTo(savedPortfolio.getId());
        assertThat(updatedPortfolio.getName()).isEqualTo(PORTFOLIO_UPDATE_NAME);
        assertThat(updatedPortfolio.getDescription()).isEqualTo(PORTFOLIO_UPDATE_DESC);
    }

    @Test
    @DisplayName("포트폴리오 한 개를 조회한다.")
    void getPortfolio() {
        // given
        Portfolio savedPortfolio = portfolioService.create(getCreatePortfolio(), MEMBER_ID);

        // when
        Portfolio findPortfolio = portfolioService.getPortfolio(savedPortfolio.getId());

        // then
        assertThat(findPortfolio.getId()).isEqualTo(savedPortfolio.getId());
        assertThat(findPortfolio.getName()).isEqualTo(findPortfolio.getName());
    }

    @Test
    @DisplayName("포트폴리오를 삭제한다.")
    void deletePortfolio() {
        // given
        Portfolio savedPortfolio = portfolioService.create(getCreatePortfolio(), MEMBER_ID);

        // when
        portfolioService.delete(savedPortfolio.getId());

        // then
        assertThatThrownBy(() -> portfolioService.getPortfolio(savedPortfolio.getId()))
                .isInstanceOf(PortfolioNotFound.class)
                .hasMessage(PORTFOLIO_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("포트폴리오 한 개의 자산 통계를 조회한다.")
    void getPortfolioStatisticById() {
        // given
        Portfolio savedPortfolio = portfolioService.create(getCreatePortfolio(), MEMBER_ID);

        // when
        PortfoliosStatistic portfoliosStatistic = portfolioService.getPortfoliosStatistic(1L);

        // then
        assertThat(portfoliosStatistic).isNotNull();
    }

    private PortfolioCreate getCreatePortfolio() {
        return PortfolioCreate.builder()
                .name(PORTFOLIO_CREATE_NAME)
                .description(PORTFOLIO_CREATE_DESC)
                .currency(KRW)
                .type(REAL)
                .build();
    }

    private PortfolioUpdate getUpdatePortfolio(Long id) {
        return PortfolioUpdate.builder()
                .id(id)
                .name(PORTFOLIO_UPDATE_NAME)
                .description(PORTFOLIO_UPDATE_DESC)
                .currency(KRW)
                .type(REAL)
                .build();
    }
}
