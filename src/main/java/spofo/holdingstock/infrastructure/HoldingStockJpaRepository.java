package spofo.holdingstock.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingStockJpaRepository extends JpaRepository<HoldingStockEntity, Long> {

    List<HoldingStockEntity> findByPortfolioEntityId(Long portfolioId);

    void deleteByPortfolioEntityId(Long id);
}