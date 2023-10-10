package spofo.stockhave.infrastructure;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.service.port.StockHaveRepository;

@Repository
@RequiredArgsConstructor
public class StockHaveRepositoryImpl implements StockHaveRepository {

    private final StockJpaHaveRepository stockJpaHaveRepository;

    @Override
    public List<StockHave> findByPortfolioId(Long id) {
        return stockJpaHaveRepository.findByPortfolioId(id).stream()
                .map(StockHaveEntity::toModel)
                .toList();
    }

    @Override
    public Optional<StockHave> findById(Long id) {
        return stockJpaHaveRepository.findById(id).map(StockHaveEntity::toModel);
    }

    @Override
    public StockHave save(StockHave stockHave) {
        return stockJpaHaveRepository.save(StockHaveEntity.from(stockHave)).toModel();
    }

    @Override
    public void deleteById(Long id) {
        stockJpaHaveRepository.deleteById(id);
    }
}
