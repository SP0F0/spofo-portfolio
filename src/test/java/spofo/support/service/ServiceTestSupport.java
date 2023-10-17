package spofo.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import spofo.holdingstock.controller.port.HoldingStockService;
import spofo.portfolio.controller.port.PortfolioService;
import spofo.portfolio.service.port.PortfolioRepository;
import spofo.stock.service.StockServerService;
import spofo.support.annotation.CustomServiceTest;
import spofo.tradelog.controller.port.TradeLogService;

@CustomServiceTest
public abstract class ServiceTestSupport {

    @Autowired
    protected PortfolioService portfolioService;

    @Autowired
    protected TradeLogService tradeLogService;

    @Autowired
    protected HoldingStockService holdingStockService;

    @SpyBean
    protected PortfolioRepository portfolioRepository;

    @MockBean
    protected StockServerService mockStockServerService;
}
