package spofo.holdingstock.controller.port;

import java.util.List;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.holdingstock.domain.HoldingStockStatistic;
import spofo.tradelog.domain.TradeLogCreate;

public interface HoldingStockService {

    List<HoldingStock> getByPortfolioId(Long portfolioId);

    HoldingStock get(Long id);

    HoldingStock create(HoldingStockCreate holdingStockCreate, TradeLogCreate tradeLogCreate,
            Long portfolioId);

    void delete(Long id);

    void deleteByPortfolioId(Long id);

    List<HoldingStockStatistic> getHoldingStockStatistics(Long portfolioId);
}
