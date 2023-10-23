package spofo.tradelog.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spofo.holdingstock.infrastructure.HoldingStockEntity;

public interface TradeLogJpaRepository extends JpaRepository<TradeLogEntity, Long> {

    List<TradeLogEntity> findByHoldingStockEntity(HoldingStockEntity stock);

    List<TradeLogEntity> findByHoldingStockEntityId(Long id);

    void deleteByHoldingStockEntityId(Long id);
}
