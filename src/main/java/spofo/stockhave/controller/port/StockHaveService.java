package spofo.stockhave.controller.port;

import java.util.List;
import spofo.stockhave.controller.response.AddStockResponse;
import spofo.stockhave.controller.response.StockHaveResponse;
import spofo.stockhave.domain.AddStockRequest;
import spofo.stockhave.domain.StockHave;

public interface StockHaveService {

    List<StockHave> getStocks(Long portfolioId);

    AddStockResponse addStock(AddStockRequest addStockRequest, Long portfolioId);

    AddStockResponse addMoreStock(AddStockRequest addStockRequest, Long portfolioId, Long stockId);

    void deleteStock(Long stockId);

    List<StockHaveResponse> getStocksByCode(Long portfolioId, String stockCode);

    StockHave addStock(StockHave stockHave);

    void deleteByPortfolioId(Long id);
}
