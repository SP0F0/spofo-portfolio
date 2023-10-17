package spofo.holdingstock.controller.port;

import java.util.List;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.portfolio.domain.Portfolio;
import spofo.tradelog.domain.TradeLogCreate;

public interface HoldingStockService {

    List<HoldingStock> getByPortfolioId(Long portfolioId);

    HoldingStock get(Long id);

    HoldingStock create(HoldingStockCreate holdingStockCreate, TradeLogCreate tradeLogCreate,
            Portfolio portfolio);

    void delete(Long id);

    void deleteByPortfolioId(Long id);
}
