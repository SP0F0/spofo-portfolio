package spofo.tradelog.service;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.stockhave.infrastructure.StockJpaHaveRepository;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.CreateTradeLogRequest;
import spofo.tradelog.domain.enums.TradeType;
import spofo.tradelog.infrastructure.TradeJpaLogRepository;
import spofo.tradelog.infrastructure.TradeLogEntity;

@Service
@RequiredArgsConstructor
public class TradeLogService {

    private final TradeJpaLogRepository tradeJpaLogRepository;
    private final StockJpaHaveRepository stockJpaHaveRepository;

    public void createTradeLog(CreateTradeLogRequest createTradeLogRequest) {
        TradeLogEntity tradeLogEntity = createTradeLogRequest.toEntity();
        tradeJpaLogRepository.save(tradeLogEntity);
    }

    /**
     * 종목 이력 목록 리스트 전달
     **/
    public List<TradeLogResponse> getTradeLogs(Long stockId) {
        StockHaveEntity stockHaveEntity = stockJpaHaveRepository.getReferenceById(stockId);
        List<TradeLogEntity> tradeLogEntities = tradeJpaLogRepository.findByStockHave(
                stockHaveEntity);
        return tradeLogEntities.stream()
                .map(tradeLog -> TradeLogResponse.from(tradeLog, getProfit(tradeLog),
                        getTotalPrice(tradeLog)))
                .toList();
    }

    /**
     * 금액 계산
     **/
    private BigDecimal getTotalPrice(TradeLogEntity tradeLogEntity) {
        return tradeLogEntity.getPrice().multiply(tradeLogEntity.getQuantity());
    }

    /**
     * 실현 수익 계산
     **/
    private BigDecimal getProfit(TradeLogEntity tradeLogEntity) {
        if (tradeLogEntity.getType().equals(TradeType.B)) {
            return ZERO;
        }
        // TODO : 매도 시 계산 로직 작성했지만 불확실함
        return getTotalPrice(tradeLogEntity).subtract(
                (tradeLogEntity.getMarketPrice().multiply(tradeLogEntity.getQuantity())));
    }
}
