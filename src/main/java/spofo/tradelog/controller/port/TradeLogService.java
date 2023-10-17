package spofo.tradelog.controller.port;

import java.util.List;
import spofo.holdingstock.domain.HoldingStock;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.TradeLogCreate;

public interface TradeLogService {

    void createTradeLog(TradeLogCreate request, HoldingStock holdingStock);

    List<TradeLogResponse> getTradeLogs(Long stockId);

    void deleteByHoldingStockId(Long id);
}
