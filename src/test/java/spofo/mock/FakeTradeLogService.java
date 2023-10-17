package spofo.mock;

import java.util.ArrayList;
import java.util.List;
import spofo.holdingstock.domain.HoldingStock;
import spofo.tradelog.controller.port.TradeLogService;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.domain.TradeLogCreate;

public class FakeTradeLogService implements TradeLogService {

    private List<TradeLog> data = new ArrayList<>();

    @Override
    public TradeLog create(TradeLogCreate request, HoldingStock holdingStock) {
        TradeLog tradeLog = TradeLog.of(request, holdingStock);
        data.add(tradeLog);
        return tradeLog;
    }

    @Override
    public void createTradeLog(TradeLogCreate request, HoldingStock holdingStock) {

    }

    @Override
    public List<TradeLogResponse> getTradeLogs(Long stockId) {
        return null;
    }

    @Override
    public void deleteByHoldingStockId(Long id) {

    }
}
