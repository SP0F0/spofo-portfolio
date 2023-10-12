package spofo.tradelog.service;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.stockhave.infrastructure.StockHaveJpaRepository;
import spofo.tradelog.controller.port.TradeLogService;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.CreateTradeLogRequest;
import spofo.tradelog.domain.enums.TradeType;
import spofo.tradelog.infrastructure.TradeLogEntity;
import spofo.tradelog.infrastructure.TradeLogJpaRepository;
import spofo.tradelog.service.port.TradeLogRepository;

@Service
@RequiredArgsConstructor
public class TradeLogServiceImpl implements TradeLogService {

    private final TradeLogRepository tradeLogRepository;
    private final TradeLogJpaRepository tradeLogJpaRepository;
    private final StockHaveJpaRepository stockHaveJpaRepository;

    @Override
    public void createTradeLog(CreateTradeLogRequest createTradeLogRequest) {
        TradeLogEntity tradeLogEntity = createTradeLogRequest.toEntity();
        tradeLogJpaRepository.save(tradeLogEntity);
    }

    /**
     * 종목 이력 목록 리스트 전달
     **/
    @Override
    public List<TradeLogResponse> getTradeLogs(Long stockId) {
        StockHaveEntity stockHaveEntity = stockHaveJpaRepository.getReferenceById(stockId);
        List<TradeLogEntity> tradeLogEntities = tradeLogJpaRepository.findByStockHaveEntity(
                stockHaveEntity);
        return tradeLogEntities.stream()
                .map(tradeLog -> TradeLogResponse.from(tradeLog, getProfit(tradeLog),
                        getTotalPrice(tradeLog)))
                .toList();
    }

    @Override
    public void deleteByStockHaveId(Long id) {
        tradeLogRepository.deleteByStockHaveId(id);
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
        if (tradeLogEntity.getType().equals(TradeType.BUY)) {
            return ZERO;
        }
        // TODO : 매도 시 계산 로직 작성했지만 불확실함
        return getTotalPrice(tradeLogEntity).subtract(
                (tradeLogEntity.getMarketPrice().multiply(tradeLogEntity.getQuantity())));
    }
}
