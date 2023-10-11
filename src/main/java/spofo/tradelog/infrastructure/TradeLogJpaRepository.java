package spofo.tradelog.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import spofo.stockhave.infrastructure.StockHaveEntity;

public interface TradeLogJpaRepository extends JpaRepository<TradeLogEntity, Long> {

//    @Query("select t from TradeLog t where t.stockHave.id = :stockId")
//    List<TradeLog> findByStockId(@Param("stockId") Long stockId);

    List<TradeLogEntity> findByStockHaveEntity(StockHaveEntity stock);
}
