package spofo.tradelog.controller.port;

import java.util.List;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.domain.StockHaveCreate;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.CreateTradeLogRequest;
import spofo.tradelog.domain.TradeLogCreate;

public interface TradeLogService {

    void createTradeLog(TradeLogCreate request, StockHave stockHave);

    List<TradeLogResponse> getTradeLogs(Long stockId);

    void deleteByStockHaveId(Long id);
}
