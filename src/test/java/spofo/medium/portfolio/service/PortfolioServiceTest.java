package spofo.medium.portfolio.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static spofo.global.component.utils.CommonUtils.*;
import static spofo.global.domain.exception.ErrorCode.PORTFOLIO_NOT_FOUND;
import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.PortfolioType.REAL;
import static spofo.tradelog.domain.enums.TradeType.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import spofo.global.component.utils.CommonUtils;
import spofo.global.domain.exception.PortfolioNotFound;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.PortfoliosStatistic;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.domain.StockHaveCreate;
import spofo.support.service.ServiceTestSupport;
import spofo.tradelog.domain.TradeLogCreate;
import spofo.tradelog.domain.enums.TradeType;

public class PortfolioServiceTest extends ServiceTestSupport {

    private static final String PORTFOLIO_CREATE_NAME = "포트폴리오 생성";
    private static final String PORTFOLIO_CREATE_DESC = "포트폴리오 생성입니다.";
    private static final String PORTFOLIO_UPDATE_NAME = "포트폴리오 수정";
    private static final String PORTFOLIO_UPDATE_DESC = "포트폴리오 수정입니다.";
    private static final Long MEMBER_ID = 1L;

    @Test
    @DisplayName("회원 1명이 등록한 전체 포트폴리오의 개요를 조회한다.")
    void getPortfoliosStatistic() {
        fail("테스트 작성 필요");
    }

    @Test
    @DisplayName("포트폴리오 포함여부가 N인 포트폴리오는 전체 통계 계산 시 제외된다.")
    void getPortfoliosStatisticWithNoInclude() {
        fail("테스트 작성 필요");
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

    @Test
    @DisplayName("회원 1명이 등록한 포트폴리오가 존재하지 않으면 통계 결과는 모두 0이다.")
    void getPortfoliosStatisticWithNoResult() {
        fail("테스트 작성 필요");
    }

    @Test
    @DisplayName("회원 1명이 가진 여러 개의 포트폴리오를 조회한다.")
    void getPortfolios() {
        fail("테스트 작성 필요");
    }

    @Test
    @DisplayName("회원 1명이 등록한 포트폴리오가 존재하지 않으면 비어있는 리스트를 반환한다.")
    void getPortfoliosWithNoResult() {
        fail("테스트 작성 필요");
    }

    @Test
    @DisplayName("포트폴리오 1건을 조회한다.")
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
    @DisplayName("존재하지 않는 포트폴리오를 조회할 수 없다.")
    void getPortfolioWithNoResult() {
        // given
        Long portfolioId = 1L;

        // expected
        assertThatThrownBy(() -> portfolioService.getPortfolio(portfolioId))
                .isInstanceOf(PortfolioNotFound.class)
                .hasMessage(PORTFOLIO_NOT_FOUND.getMessage());
    }

    @Test
    @Rollback(false)
    @DisplayName("포트폴리오 1건을 통계와 함께 조회한다.") // api-004
    void getPortfolioStatistic() {
        // given
        PortfolioCreate createPortfolio = getCreatePortfolio();
        Portfolio savedPortfolio = portfolioService.create(createPortfolio, MEMBER_ID);

        StockHaveCreate stockHaveCreate = StockHaveCreate.builder()
                .stockCode("000660")
                .build();

        StockHave savedStockHave = stockHaveService.addStock(stockHaveCreate, savedPortfolio);

        TradeLogCreate tradeLogCreate = TradeLogCreate.builder()
                .type(BUY)
                .price(getBD(10000))
                .tradeDate(LocalDateTime.now())
                .marketPrice(getBD(20000))
//                .stockHave(savedStockHave)
                .quantity(getBD(10))
                .type(BUY)
                .build();

        tradeLogService.createTradeLog(tradeLogCreate, savedStockHave);

        // when
        PortfolioStatistic statistic = portfolioService.getPortfolioStatistic(MEMBER_ID);

        // then
//        assertThat(statistic.getTotalGain()).isEqualTo(getBD(100000));
        assertThat(statistic.getPortfolio().getId()).isEqualTo(savedPortfolio.getId());
//        assertThat(statistic.getTotalBuy()).isEqualTo()
    }

    @Test
    @DisplayName("포트폴리오 1건을 생성한다.")
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
    @DisplayName("포트폴리오 1건을 수정한다.")
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
    @DisplayName("존재하지 않는 포트폴리오를 수정할 수 없다.")
    void updatePortfolioWithNoResult() {
        // given
        Long memberId = 1L;
        Long portfolioId = 1L;
        PortfolioUpdate portfolioUpdate = PortfolioUpdate.builder().build();

        // expected
        assertThatThrownBy(() -> portfolioService.update(portfolioUpdate, portfolioId, memberId))
                .isInstanceOf(PortfolioNotFound.class)
                .hasMessage(PORTFOLIO_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("포트폴리오 1건을 삭제한다.")
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
    @DisplayName("존재하지 않는 포트폴리오를 삭제할 수 없다.")
    void deletePortfolioWithNoResult() {
        // given
        Long portfolioId = 1L;

        // expected
        assertThatThrownBy(() -> portfolioService.delete(portfolioId))
                .isInstanceOf(PortfolioNotFound.class)
                .hasMessage(PORTFOLIO_NOT_FOUND.getMessage());
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
