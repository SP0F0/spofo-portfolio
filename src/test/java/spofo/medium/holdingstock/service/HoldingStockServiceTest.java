package spofo.medium.holdingstock.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.holdingstock.domain.HoldingStock;
import spofo.portfolio.domain.Portfolio;
import spofo.support.service.ServiceTestSupport;

public class HoldingStockServiceTest extends ServiceTestSupport {

    @Test
    @DisplayName("포트폴리오 1개에 속한 보유종목을 조회한다.")
    void getByPortfolioId() {
        // given
        Portfolio savedPortfolio = portfolioRepository.save(createPortfolio());
        HoldingStock holdingStock = createHoldingStock(savedPortfolio);

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
        Portfolio savedPortfolio = portfolioRepository.save(createPortfolio());

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
        Portfolio savedPortfolio = portfolioRepository.save(createPortfolio());
        HoldingStock holdingStock = createHoldingStock(savedPortfolio);

        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

        // when
        HoldingStock foundHoldingStock = holdingStockService.get(savedHoldingStock.getId());

        // then
        assertThat(foundHoldingStock.getId()).isEqualTo(1L);
        assertThat(foundHoldingStock.getStockCode()).isEqualTo(holdingStock.getStockCode());
    }

    @Test
    @DisplayName("존재하지 않는 보유종목은 조회할 수 없다.")
    void getHoldingStockWithNoResult() {
        // given

        // when

        // then
        Assertions.fail("테스트 필요!");
    }

    @Test
    @DisplayName("보유 종목 1건을 생성한다.")
    void holdingStockCreate() {
        // given

        // when

        // then
        Assertions.fail("테스트 필요!");
    }

    @Test
    @DisplayName("보유종목 1건을 삭제한다.")
    void deleteHoldingStock() {
        // given

        // when

        // then
        Assertions.fail("테스트 필요!");
    }

    @Test
    @DisplayName("존재하지 않는 보유종목을 삭제할 수 없다.")
    void deleteHoldingStockWithNoResult() {
        // given

        // when

        // then
        Assertions.fail("테스트 필요!");
    }

    @Test
    @DisplayName("포트폴리오 아이디로 보유종목을 삭제한다.")
    void deleteHoldingStockByPortfolioId() {
        // given

        // when

        // then
        Assertions.fail("테스트 필요!");
    }

    private Portfolio createPortfolio() {
        return Portfolio.builder()
                .build();
    }

    private HoldingStock createHoldingStock(Portfolio portfolio) {
        return HoldingStock.builder()
                .id(1L)
                .stockCode("101010")
                .portfolio(portfolio)
                .build();
    }
}
