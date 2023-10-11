package spofo.stockhave.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockHaveJpaRepository extends JpaRepository<StockHaveEntity, Long> {

    @Query(value = "SELECT * FROM stock_have sh WHERE sh.portfolio_id = :portfolioId", nativeQuery = true)
    List<StockHaveEntity> findByPortfolioId(@Param("portfolioId") Long portfolioId);

}