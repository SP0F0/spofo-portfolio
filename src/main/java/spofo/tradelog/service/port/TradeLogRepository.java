package spofo.tradelog.service.port;

import spofo.tradelog.domain.TradeLog;

public interface TradeLogRepository {

    TradeLog save(TradeLog tradeLog);

    void deleteByHoldingStockId(Long id);
}