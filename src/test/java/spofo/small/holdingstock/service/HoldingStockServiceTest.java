package spofo.small.holdingstock.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static spofo.global.domain.exception.ErrorCode.HOLDING_STOCK_NOT_FOUND;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.global.domain.exception.HoldingStockNotFound;
import spofo.holdingstock.controller.port.HoldingStockService;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.holdingstock.service.HoldingStockServiceImpl;
import spofo.mock.FakeHoldingStockRepository;
import spofo.mock.FakeTradeLogService;
import spofo.portfolio.domain.Portfolio;
import spofo.tradelog.controller.port.TradeLogService;
import spofo.tradelog.domain.TradeLogCreate;

public class HoldingStockServiceTest {

    private HoldingStockService holdingStockService;
    private TradeLogService fakeTradeLogService;
    private FakeHoldingStockRepository fakeHoldingStockRepository;

    private static final Long PORTFOLIO_ID = 1L;
    private static final String STOCK_CODE = "101010";

    @BeforeEach
    void setup() {
        fakeTradeLogService = new FakeTradeLogService();
        fakeHoldingStockRepository = new FakeHoldingStockRepository();
        holdingStockService =
                new HoldingStockServiceImpl(fakeTradeLogService, fakeHoldingStockRepository);
    }

    @Test
    @DisplayName("포트폴리오 1개에 속한 보유종목을 조회한다.")
    void getByPortfolioId() {
        // given
        Portfolio portfolio = createPortfolio(PORTFOLIO_ID);

        HoldingStock holdingStock = createHoldingStock(STOCK_CODE, portfolio);

        fakeHoldingStockRepository.save(holdingStock);

        // when
        List<HoldingStock> holdingStocks = holdingStockService.getByPortfolioId(PORTFOLIO_ID);

        // then
        assertThat(holdingStocks)
                .extracting("id", "stockCode", "portfolio.id")
                .contains(
                        tuple(1L, STOCK_CODE, PORTFOLIO_ID)
                );
    }

    @Test
    @DisplayName("포트폴리오 1개에 속한 보유종목이 없으면 비어있는 리스트를 반환한다.")
    void getByPortfolioIdWithNoHoldingStock() {
        // given & when
        List<HoldingStock> holdingStocks = holdingStockService.getByPortfolioId(PORTFOLIO_ID);

        // then
        assertThat(holdingStocks).isEmpty();
    }

    @Test
    @DisplayName("포트폴리오 id가 유효하지 않으면 비어있는 리스트를 반환한다.")
    void getByPortfolioIdWithNoNotValidPortfolioId() {
        // given
        Long portfolioId = null;

        // when
        List<HoldingStock> holdingStocks = holdingStockService.getByPortfolioId(portfolioId);

        // then
        assertThat(holdingStocks).isEmpty();
    }

    @Test
    @DisplayName("보유종목 1건을 조회한다.")
    void getHoldingStock() {
        // given
        Long holdingStockId = 1L;
        Portfolio portfolio = createPortfolio(PORTFOLIO_ID);
        HoldingStock holdingStock = createHoldingStock(STOCK_CODE, portfolio);

        fakeHoldingStockRepository.save(holdingStock);

        // when
        HoldingStock savedHoldingStock = holdingStockService.get(holdingStockId);

        // then
        assertThat(savedHoldingStock.getId()).isEqualTo(holdingStockId);
        assertThat(savedHoldingStock.getStockCode()).isEqualTo(STOCK_CODE);
    }

    @Test
    @DisplayName("존재하지 않는 보유종목은 조회할 수 없다.")
    void getHoldingStockWithNoResult() {
        // given
        Long holdingStockId = 1L;

        // expected
        assertThatThrownBy(() -> holdingStockService.get(holdingStockId))
                .isInstanceOf(HoldingStockNotFound.class)
                .hasMessage(HOLDING_STOCK_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("보유 종목 1건을 생성한다.")
    void holdingStockCreate() {
        // given
        Portfolio portfolio = createPortfolio(PORTFOLIO_ID);
        TradeLogCreate tradeLogCreate = TradeLogCreate.builder().build();

        HoldingStockCreate holdingStockCreate = HoldingStockCreate.builder()
                .stockCode(STOCK_CODE)
                .build();

        // when
        HoldingStock savedHoldingStock =
                holdingStockService.create(holdingStockCreate, tradeLogCreate, portfolio);

        // then
        assertThat(savedHoldingStock.getId()).isEqualTo(1L);
        assertThat(savedHoldingStock.getStockCode()).isEqualTo(STOCK_CODE);
    }

    @Test
    @DisplayName("보유종목 1건을 삭제한다.")
    void deleteHoldingStock() {
        // given
        Long holdingStockId = 1L;
        Portfolio portfolio = createPortfolio(PORTFOLIO_ID);
        HoldingStock holdingStock = createHoldingStock(STOCK_CODE, portfolio);

        fakeHoldingStockRepository.save(holdingStock);

        // when
        holdingStockService.delete(holdingStockId);

        // then
        assertThatThrownBy(() -> holdingStockService.get(holdingStockId))
                .isInstanceOf(HoldingStockNotFound.class)
                .hasMessage(HOLDING_STOCK_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 보유종목을 삭제할 수 없다.")
    void deleteHoldingStockWithNoResult() {
        // given
        Long holdingStockId = 1L;

        // expected
        assertThatThrownBy(() -> holdingStockService.delete(holdingStockId))
                .isInstanceOf(HoldingStockNotFound.class)
                .hasMessage(HOLDING_STOCK_NOT_FOUND.getMessage());
    }

    // 포폴, 보유종목 2개 -> 성공

    @Test
    @DisplayName("포트폴리오 아이디로 보유종목을 삭제한다.")
    void deleteHoldingStockByPortfolioId() {
        // given
        Portfolio portfolio = createPortfolio(PORTFOLIO_ID);
        HoldingStockCreate create = HoldingStockCreate.builder()
                .stockCode(STOCK_CODE)
                .build();

        HoldingStock holdingStock = HoldingStock.of(create, portfolio);

        fakeHoldingStockRepository.save(holdingStock);

        // when
        holdingStockService.deleteByPortfolioId(PORTFOLIO_ID);

        // then
        List<HoldingStock> holdingStocks = holdingStockService.getByPortfolioId(PORTFOLIO_ID);

        assertThat(holdingStocks).isEmpty();
    }

    private Portfolio createPortfolio(Long id) {
        return Portfolio.builder()
                .id(id)
                .build();
    }

    private HoldingStock createHoldingStock(String stockCode, Portfolio portfolio) {
        return HoldingStock.builder()
                .id(1L)
                .stockCode(stockCode)
                .portfolio(portfolio)
                .build();
    }
}
