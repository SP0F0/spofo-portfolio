package spofo.holdingstock.controller.port;

import java.util.List;
import spofo.holdingstock.controller.response.AddStockResponse;
import spofo.holdingstock.controller.response.HoldingStockResponse;
import spofo.holdingstock.domain.AddStockRequest;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.portfolio.domain.Portfolio;

public interface HoldingStockService {

    List<HoldingStock> getStocks(Long portfolioId);

    AddStockResponse addStock(AddStockRequest addStockRequest, Long portfolioId);

    AddStockResponse addMoreStock(AddStockRequest addStockRequest, Long portfolioId, Long stockId);

    void deleteStock(Long stockId);

    List<HoldingStockResponse> getStocksByCode(Long portfolioId, String stockCode);

    HoldingStock addStock(HoldingStockCreate request, Portfolio portfolio);

    void deleteByPortfolioId(Long id);
}
