package spofo.stockhave.service.port;

import java.util.List;
import java.util.Optional;
import spofo.stockhave.domain.StockHave;

public interface StockHaveRepository {

    List<StockHave> findByPortfolioId(Long id);

    Optional<StockHave> findById(Long id);

    StockHave save(StockHave stockHave);

    void deleteById(Long id);

}
