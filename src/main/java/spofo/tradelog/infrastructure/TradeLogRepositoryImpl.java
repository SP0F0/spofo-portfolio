package spofo.tradelog.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.service.port.TradeLogRepository;

@Repository
@RequiredArgsConstructor
public class TradeLogRepositoryImpl implements TradeLogRepository {

    private final TradeLogJpaRepository tradeLogJpaRepository;

    @Override
    public TradeLog save(TradeLog tradeLog) {
        return tradeLogJpaRepository.save(TradeLogEntity.from(tradeLog)).toModel();
    }

    @Override
    public void deleteByStockHaveId(Long id) {
        tradeLogJpaRepository.findByStockHaveEntityId(id);
    }
}
