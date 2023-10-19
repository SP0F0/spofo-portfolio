package spofo.medium.holdingstock.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static spofo.global.domain.exception.ErrorCode.HOLDING_STOCK_NOT_FOUND;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.global.domain.exception.HoldingStockNotFound;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.portfolio.domain.Portfolio;
import spofo.support.service.ServiceTestSupport;
import spofo.tradelog.domain.TradeLogCreate;

public class HoldingStockServiceTest extends ServiceTestSupport {

    @Test
    @DisplayName("포트폴리오 1개에 속한 보유종목을 조회한다.")
    void getByPortfolioId() {
        // given
        Portfolio savedPortfolio = portfolioRepository.save(getPortfolio());
        HoldingStock holdingStock = getHoldingStock(savedPortfolio);

        holdingStockRepository.save(holdingStock);

        // when
        List<HoldingStock> holdingStocks =
                holdingStockService.getByPortfolioId(savedPortfolio.getId());

        // then
        assertThat(holdingStocks)
                .extracting("id", "stockCode", "portfolio")
                .contains(
                        tuple(1L, holdingStock.getStockCode(), null)
                );
    }

    @Test
    @DisplayName("포트폴리오 1개에 속한 보유종목이 없으면 비어있는 리스트를 반환한다.")
    void getByPortfolioIdWithNoHoldingStock() {
        // given
        Portfolio savedPortfolio = portfolioRepository.save(getPortfolio());

        // when
        List<HoldingStock> holdingStocks =
                holdingStockService.getByPortfolioId(savedPortfolio.getId());

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
        Portfolio savedPortfolio = portfolioRepository.save(getPortfolio());
        HoldingStock holdingStock = getHoldingStock(savedPortfolio);

        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

        // when
        HoldingStock foundHoldingStock = holdingStockService.get(savedHoldingStock.getId());

        // then
        assertThat(foundHoldingStock.getId()).isEqualTo(savedHoldingStock.getId());
        assertThat(foundHoldingStock.getStockCode()).isEqualTo(holdingStock.getStockCode());
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
        Portfolio portfolio = getPortfolio();
        TradeLogCreate tradeLogCreate = TradeLogCreate.builder().build();

        HoldingStockCreate holdingStockCreate = getHoldingStockCreate();

        // when
        HoldingStock savedHoldingStock =
                holdingStockService.create(holdingStockCreate, tradeLogCreate, portfolio);

        // then
        assertThat(savedHoldingStock.getId()).isNotNull();
        assertThat(savedHoldingStock.getStockCode()).isEqualTo(holdingStockCreate.getStockCode());
    }

    @Test
    @DisplayName("보유종목 1건을 삭제한다.")
    void deleteHoldingStock() {
        // given
        Portfolio portfolio = getPortfolio();
        HoldingStock holdingStock = getHoldingStock(portfolio);

        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

        // when
        holdingStockService.delete(savedHoldingStock.getId());

        // then
        assertThatThrownBy(() -> holdingStockService.get(savedHoldingStock.getId()))
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

    @Test
    @DisplayName("포트폴리오 아이디로 보유종목을 삭제한다.")
    void deleteHoldingStockByPortfolioId() {
        // given
        Portfolio savedPortfolio = portfolioRepository.save(getPortfolio());

        HoldingStock holdingStock = HoldingStock.of(getHoldingStockCreate(), savedPortfolio);
        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

        // when
        holdingStockService.deleteByPortfolioId(savedHoldingStock.getId());

        // then
        List<HoldingStock> holdingStocks =
                holdingStockService.getByPortfolioId(savedHoldingStock.getId());

        assertThat(holdingStocks).isEmpty();
    }

    private Portfolio getPortfolio() {
        return Portfolio.builder()
                .build();
    }

    private HoldingStock getHoldingStock(Portfolio portfolio) {
        return HoldingStock.builder()
                .id(1L)
                .stockCode("101010")
                .portfolio(portfolio)
                .build();
    }

    private HoldingStockCreate getHoldingStockCreate() {
        return HoldingStockCreate.builder()
                .stockCode("101010")
                .build();
    }

}
