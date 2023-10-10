package spofo.tradelog.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spofo.tradelog.service.port.TradeLogRepository;

@Repository
@RequiredArgsConstructor
public class TradeLogRepositoryImpl implements TradeLogRepository {

    private final TradeJpaLogRepository tradeJpaLogRepository;
}
