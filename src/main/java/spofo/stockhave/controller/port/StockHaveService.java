package spofo.stockhave.controller.port;

import java.util.List;
import spofo.portfolio.domain.Portfolio;
import spofo.stockhave.controller.response.AddStockResponse;
import spofo.stockhave.controller.response.StockHaveResponse;
import spofo.stockhave.domain.AddStockRequest;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.domain.StockHaveCreate;

public interface StockHaveService {

    List<StockHave> getStocks(Long portfolioId);

    AddStockResponse addStock(AddStockRequest addStockRequest, Long portfolioId);

    AddStockResponse addMoreStock(AddStockRequest addStockRequest, Long portfolioId, Long stockId);

    void deleteStock(Long stockId);

    List<StockHaveResponse> getStocksByCode(Long portfolioId, String stockCode);

    StockHave addStock(StockHaveCreate request, Portfolio portfolio);

    void deleteByPortfolioId(Long id);
}
