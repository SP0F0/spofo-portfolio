package spofo.holdingstock.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingStockJpaRepository extends JpaRepository<HoldingStockEntity, Long> {

    @Query(value = "SELECT * FROM stock_have sh WHERE sh.portfolio_id = :portfolioId", nativeQuery = true)
    List<HoldingStockEntity> findByPortfolioId(@Param("portfolioId") Long portfolioId);

    void deleteByPortfolioEntityId(Long id);
}