package spofo.tradelog.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spofo.holdingstock.infrastructure.HoldingStockEntity;

public interface TradeLogJpaRepository extends JpaRepository<TradeLogEntity, Long> {

//    @Query("select t from TradeLog t where t.stockHave.id = :stockId")
//    List<TradeLog> findByStockId(@Param("stockId") Long stockId);

    List<TradeLogEntity> findByHoldingStockEntity(HoldingStockEntity stock);
//
//    @Query("select distinct t "
//            + "from TradeLogEntity t "
//            + "where t.holdingStockEntity.id = :id")
    void deleteByHoldingStockEntityId(Long id);
}
