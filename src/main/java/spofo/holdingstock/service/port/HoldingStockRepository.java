package spofo.holdingstock.service.port;

import java.util.List;
import java.util.Optional;
import spofo.holdingstock.domain.HoldingStock;

public interface HoldingStockRepository {

    List<HoldingStock> findByPortfolioId(Long id);

    Optional<HoldingStock> findById(Long id);

    HoldingStock save(HoldingStock holdingStock);

    void deleteById(Long id);

    void deleteByPortfolioId(Long id);
}
