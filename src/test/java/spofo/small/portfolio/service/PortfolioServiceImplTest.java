package spofo.small.portfolio.service;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static spofo.global.domain.exception.ErrorCode.PORTFOLIO_NOT_FOUND;
import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.IncludeType.N;
import static spofo.portfolio.domain.enums.IncludeType.Y;
import static spofo.portfolio.domain.enums.PortfolioType.FAKE;
import static spofo.portfolio.domain.enums.PortfolioType.REAL;
import static spofo.tradelog.domain.enums.TradeType.BUY;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.global.domain.exception.PortfolioNotFound;
import spofo.mock.FakePortfolioRepository;
import spofo.mock.FakeStockServerService;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.PortfoliosStatistic;
import spofo.portfolio.domain.enums.Currency;
import spofo.portfolio.domain.enums.IncludeType;
import spofo.portfolio.domain.enums.PortfolioType;
import spofo.portfolio.service.PortfolioService;
import spofo.portfolio.service.PortfolioServiceImpl;
import spofo.stock.domain.Stock;
import spofo.stockhave.domain.StockHave;
import spofo.tradelog.domain.TradeLog;

public class PortfolioServiceImplTest {

    private PortfolioService portfolioService;
    private FakeStockServerService fakeStockServerService;
    private FakePortfolioRepository fakePortfolioRepository;
    private final String TEST_STOCK_CODE = "101001";

    @BeforeEach
    void setup() {
        fakePortfolioRepository = new FakePortfolioRepository();
        fakeStockServerService = new FakeStockServerService();
        portfolioService = new PortfolioServiceImpl(fakePortfolioRepository,
                fakeStockServerService);

        Stock stock = Stock.builder()
                .code(TEST_STOCK_CODE)
                .name("삼성전자")
                .price(BigDecimal.valueOf(66000))
                .sector("반도체")
                .build();

        fakeStockServerService.save(stock);
    }

    @Test
    @DisplayName("회원 1명이 등록한 전체 포트폴리오의 개요를 조회한다.")
    void getPortfoliosStatistic() {
        // given
        Long memberId = 1L;
        TradeLog tradeLog = getTradeLog();
        StockHave stockHave = getStockHave(tradeLog);

        Portfolio portfolio = Portfolio.builder()
                .memberId(memberId)
                .includeYn(Y)
                .stockHaves(List.of(stockHave))
                .build();

        fakePortfolioRepository.save(portfolio);

        // when
        PortfoliosStatistic portfoliosStatistic = portfolioService.getPortfoliosStatistic(memberId);

        // then
        assertThat(portfoliosStatistic.getTotalAsset()).isEqualTo(BigDecimal.valueOf(66000));
        assertThat(portfoliosStatistic.getGain()).isEqualTo(BigDecimal.valueOf(33000));
        assertThat(portfoliosStatistic.getGainRate()).isEqualTo(
                BigDecimal.valueOf(100).setScale(2));
    }

    @Test
    @DisplayName("포트폴리오 포함여부가 N인 포트폴리오는 전체 통계 계산 시 제외된다.")
    void getPortfoliosStatisticWithNoInclude() {
        // given
        Long memberId = 1L;
        TradeLog tradeLog = getTradeLog();
        StockHave stockHave = getStockHave(tradeLog);

        Portfolio portfolio = Portfolio.builder()
                .memberId(memberId)
                .includeYn(N)
                .stockHaves(List.of(stockHave))
                .build();

        fakePortfolioRepository.save(portfolio);

        // when
        PortfoliosStatistic portfoliosStatistic = portfolioService.getPortfoliosStatistic(memberId);

        // then
        assertThat(portfoliosStatistic.getTotalAsset()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(portfoliosStatistic.getGain()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(portfoliosStatistic.getGainRate()).isEqualTo(ZERO);
    }

    @Test
    @DisplayName("회원 1명이 등록한 포트폴리오가 존재하지 않으면 통계 결과는 모두 0이다.")
    void getPortfoliosStatisticWithNoResult() {
        // given
        Long memberId = 1L;

        // when
        PortfoliosStatistic portfoliosStatistic = portfolioService.getPortfoliosStatistic(memberId);

        // then
        assertThat(portfoliosStatistic.getTotalAsset()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(portfoliosStatistic.getGain()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(portfoliosStatistic.getGainRate()).isEqualTo(ZERO);
    }

    @Test
    @DisplayName("회원 1명이 가진 여러 개의 포트폴리오를 조회한다.")
    void getPortfolios() {
        // given
        Long memberId = 1L;
        TradeLog tradeLog = getTradeLog();
        StockHave stockHave = getStockHave(tradeLog);

        Portfolio portfolio1 = Portfolio.builder()
                .memberId(memberId)
                .stockHaves(List.of(stockHave))
                .build();

        Portfolio portfolio2 = Portfolio.builder()
                .memberId(memberId)
                .stockHaves(List.of(stockHave, stockHave))
                .build();

        fakePortfolioRepository.save(portfolio1);
        fakePortfolioRepository.save(portfolio2);

        // when
        List<PortfolioStatistic> portfolios = portfolioService.getPortfolios(memberId);

        // then
        assertThat(portfolios).hasSize(2)
                .extracting("portfolio.id", "totalAsset", "totalBuy", "totalGain", "gainRate")
                .containsExactlyInAnyOrder(
                        tuple(1L, BigDecimal.valueOf(66000), BigDecimal.valueOf(33000),
                                BigDecimal.valueOf(33000), BigDecimal.valueOf(100).setScale(2)),

                        tuple(2L, BigDecimal.valueOf(132000), BigDecimal.valueOf(66000),
                                BigDecimal.valueOf(66000), BigDecimal.valueOf(100).setScale(2))
                );
    }

    @Test
    @DisplayName("회원 1명이 등록한 포트폴리오가 존재하지 않으면 비어있는 리스트를 반환한다.")
    void getPortfoliosWithNoResult() {
        // given
        Long memberId = 1L;

        // when
        List<PortfolioStatistic> portfolios = portfolioService.getPortfolios(memberId);

        // then
        assertThat(portfolios).isEmpty();
    }

    @Test
    @DisplayName("포트폴리오 1건을 조회한다.")
    void getPortfolio() {
        // given
        Long portfolioId = 1L;

        Portfolio portfolio = Portfolio.builder()
                .memberId(1L)
                .name("포트폴리오 이름")
                .description("포트폴리오 설명")
                .currency(KRW)
                .includeYn(Y)
                .type(REAL)
                .build();

        fakePortfolioRepository.save(portfolio);

        // when
        Portfolio savedPortfolio = portfolioService.getPortfolio(portfolioId);

        // then
        assertThat(savedPortfolio.getId()).isEqualTo(portfolioId);
        assertThat(savedPortfolio.getMemberId()).isEqualTo(1L);
        assertThat(savedPortfolio.getName()).isEqualTo("포트폴리오 이름");
        assertThat(savedPortfolio.getDescription()).isEqualTo("포트폴리오 설명");
        assertThat(savedPortfolio.getCurrency()).isEqualTo(KRW);
        assertThat(savedPortfolio.getIncludeYn()).isEqualTo(Y);
        assertThat(savedPortfolio.getType()).isEqualTo(REAL);
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
    @DisplayName("포트폴리오 1건을 통계외 함께 조회한다.")
    void getPortfolioStatistic() {
        // given
        Long portfolioId = 1L;
        String name = "포트폴리오 이름";
        String description = "포트폴리오 설명";
        TradeLog tradeLog = getTradeLog();
        StockHave stockHave = getStockHave(tradeLog);

        Portfolio portfolio = Portfolio.builder()
                .memberId(1L)
                .name(name)
                .description(description)
                .currency(KRW)
                .includeYn(Y)
                .type(REAL)
                .stockHaves(List.of(stockHave))
                .build();

        fakePortfolioRepository.save(portfolio);

        // when
        PortfolioStatistic statistic = portfolioService.getPortfolioStatistic(1L);

        // then
        Portfolio savedPortfolio = statistic.getPortfolio();

        assertThat(savedPortfolio.getId()).isEqualTo(portfolioId);
        assertThat(savedPortfolio.getMemberId()).isEqualTo(1L);
        assertThat(savedPortfolio.getName()).isEqualTo(name);
        assertThat(savedPortfolio.getDescription()).isEqualTo(description);
        assertThat(savedPortfolio.getCurrency()).isEqualTo(KRW);
        assertThat(savedPortfolio.getIncludeYn()).isEqualTo(Y);
        assertThat(savedPortfolio.getType()).isEqualTo(REAL);
        assertThat(statistic.getTotalAsset()).isEqualTo(BigDecimal.valueOf(66000));
        assertThat(statistic.getTotalBuy()).isEqualTo(BigDecimal.valueOf(33000));
        assertThat(statistic.getTotalGain()).isEqualTo(BigDecimal.valueOf(33000));
        assertThat(statistic.getGainRate()).isEqualTo(BigDecimal.valueOf(100).setScale(2));
    }

    @Test
    @DisplayName("포트폴리오 1건을 생성한다.")
    void createPortfolio() {
        // given
        Long memberId = 1L;
        String name = "portfolio name";
        String description = "portfolio description";
        Currency currency = KRW;
        PortfolioType type = REAL;

        PortfolioCreate portfolioCreate = PortfolioCreate.builder()
                .name(name)
                .description(description)
                .currency(currency)
                .type(type)
                .build();

        // when
        Portfolio savedPortfolio = portfolioService.create(portfolioCreate, memberId);

        // then
        assertThat(savedPortfolio.getId()).isEqualTo(1L);
        assertThat(savedPortfolio.getMemberId()).isEqualTo(memberId);
        assertThat(savedPortfolio.getName()).isEqualTo(name);
        assertThat(savedPortfolio.getDescription()).isEqualTo(description);
        assertThat(savedPortfolio.getCurrency()).isEqualTo(currency);
        assertThat(savedPortfolio.getIncludeYn()).isEqualTo(Y);
        assertThat(savedPortfolio.getType()).isEqualTo(REAL);
    }

    @Test
    @DisplayName("포트폴리오 1건을 수정한다.")
    void updatePortfolio() {
        // given
        Long memberId = 1L;
        String updateName = "변경된 포트폴리오 이름";
        String updatedDescription = "변경된 포트폴리오 설명";
        Currency currency = KRW;
        IncludeType updatedIncludeYn = N;
        PortfolioType updatedType = FAKE;

        Portfolio portfolio = Portfolio.builder()
                .memberId(memberId)
                .name("포트폴리오 이름")
                .description("포트폴리오 설명")
                .currency(currency)
                .includeYn(Y)
                .type(REAL)
                .build();

        Portfolio savedPortfolio = fakePortfolioRepository.save(portfolio);

        PortfolioUpdate portfolioUpdate = PortfolioUpdate.builder()
                .id(savedPortfolio.getId())
                .name(updateName)
                .description(updatedDescription)
                .currency(savedPortfolio.getCurrency())
                .includeYn(updatedIncludeYn)
                .type(updatedType)
                .build();

        // when
        Portfolio updatedPortfolio =
                portfolioService.update(portfolioUpdate, savedPortfolio.getId(), memberId);

        // then
        assertThat(updatedPortfolio.getId()).isEqualTo(1L);
        assertThat(updatedPortfolio.getMemberId()).isEqualTo(memberId);
        assertThat(updatedPortfolio.getName()).isEqualTo(updateName);
        assertThat(updatedPortfolio.getDescription()).isEqualTo(updatedDescription);
        assertThat(updatedPortfolio.getCurrency()).isEqualTo(KRW);
        assertThat(updatedPortfolio.getIncludeYn()).isEqualTo(updatedIncludeYn);
        assertThat(updatedPortfolio.getType()).isEqualTo(updatedType);
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
        Long portfolioId = 1L;
        Portfolio portfolio = Portfolio.builder().build();

        fakePortfolioRepository.save(portfolio);

        // when
        portfolioService.delete(portfolioId);

        // then
        assertThatThrownBy(() -> portfolioService.getPortfolio(portfolioId))
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

    private StockHave getStockHave(TradeLog tradeLog) {
        return StockHave.builder()
                .stockCode(TEST_STOCK_CODE)
                .tradeLogs(List.of(tradeLog))
                .build();
    }

    private TradeLog getTradeLog() {
        return TradeLog.builder()
                .type(BUY)
                .price(valueOf(33000))
                .quantity(valueOf(1))
                .build();
    }
}
