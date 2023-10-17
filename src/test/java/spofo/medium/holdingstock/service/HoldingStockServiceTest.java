package spofo.medium.holdingstock.service;

import static spofo.portfolio.domain.enums.Currency.KRW;
import static spofo.portfolio.domain.enums.PortfolioType.REAL;

import org.junit.jupiter.api.Test;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.support.service.ServiceTestSupport;

public class HoldingStockServiceTest extends ServiceTestSupport {

    private static final String PORTFOLIO_CREATE_NAME = "포트폴리오 생성";
    private static final String PORTFOLIO_CREATE_DESC = "포트폴리오 생성입니다.";
    private static final String PORTFOLIO_UPDATE_NAME = "포트폴리오 수정";
    private static final String PORTFOLIO_UPDATE_DESC = "포트폴리오 수정입니다.";
    private static final String TEST_STOCK_CODE = "000660";
    private static final Long MEMBER_ID = 1L;

    @Test
    void addStock() {
        // given
        PortfolioCreate createPortfolio = getCreatePortfolio();
        Portfolio savedPortfolio = portfolioService.create(createPortfolio, MEMBER_ID);
/*
        HoldingStockCreate holdingStockCreate = HoldingStockCreate.builder()
                .stockCode(TEST_STOCK_CODE)
                .build();
        // when
        HoldingStock savedHoldingStock = holdingStockService.addStock(holdingStockCreate,
                savedPortfolio);
*/

        // then
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
