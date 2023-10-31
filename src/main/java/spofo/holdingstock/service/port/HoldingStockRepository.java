package spofo.holdingstock.service.port;

import java.util.List;
import java.util.Optional;
import spofo.holdingstock.domain.HoldingStock;
import spofo.portfolio.domain.Portfolio;

public interface HoldingStockRepository {

    List<HoldingStock> findByPortfolioId(Long id);

    Optional<HoldingStock> findById(Long id);

    HoldingStock save(HoldingStock holdingStock);

    void delete(HoldingStock holdingStock);

    void deleteByPortfolioId(Long id);

    void deleteAll();

    boolean exists(Portfolio portfolio, String stockCode);
}
