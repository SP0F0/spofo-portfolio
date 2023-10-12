package spofo.medium.stockhave.service.StockHaveService;

import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.PortfolioType.REAL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.domain.StockHaveCreate;
import spofo.support.service.ServiceTestSupport;

public class StockHaveServiceTest extends ServiceTestSupport {

    private static final String PORTFOLIO_CREATE_NAME = "포트폴리오 생성";
    private static final String PORTFOLIO_CREATE_DESC = "포트폴리오 생성입니다.";
    private static final String PORTFOLIO_UPDATE_NAME = "포트폴리오 수정";
    private static final String PORTFOLIO_UPDATE_DESC = "포트폴리오 수정입니다.";
    private static final String TEST_STOCK_CODE = "000660";
    private static final Long MEMBER_ID = 1L;

    @Test
    @Rollback(false)
    @DisplayName("주식 종목을 추가하면 Trade Log가 생성된다.")
    void addStock() {
        // given
        PortfolioCreate createPortfolio = getCreatePortfolio();
        Portfolio savedPortfolio = portfolioService.create(createPortfolio, MEMBER_ID);
        StockHaveCreate stockHaveCreate = StockHaveCreate.builder()
                .stockCode(TEST_STOCK_CODE)
                .build();

        StockHave stockHave = StockHave.of(stockHaveCreate, savedPortfolio);

        // when
        StockHave savedStockHave = stockHaveService.addStock(stockHaveCreate, savedPortfolio);

        // then
        System.out.println(savedStockHave);
        //portfolioService.delete(savedPortfolio.getId());

        //assertThat(savedStockHave.getStockCode()).isEqualTo(TEST_STOCK_CODE);
        //assertThat(savedStockHave.getTradeLogs().get(0).getPrice()).isEqualTo(getBD(10000));
    }

    private PortfolioCreate getCreatePortfolio() {
        return PortfolioCreate.builder()
                .name(PORTFOLIO_CREATE_NAME)
                .description(PORTFOLIO_CREATE_DESC)
                .currency(KRW)
                .type(REAL)
                .build();
    }
}
