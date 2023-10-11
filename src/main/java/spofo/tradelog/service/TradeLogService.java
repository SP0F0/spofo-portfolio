package spofo.tradelog.service;

import java.util.List;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.CreateTradeLogRequest;

public interface TradeLogService {

    void createTradeLog(CreateTradeLogRequest createTradeLogRequest);
    
    List<TradeLogResponse> getTradeLogs(Long stockId);
}
