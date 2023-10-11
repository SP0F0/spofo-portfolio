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

    private final StockHaveJpaRepository stockHaveJpaRepository;

    @Override
    public List<StockHave> findByPortfolioId(Long id) {
        return stockHaveJpaRepository.findByPortfolioId(id).stream()
                .map(StockHaveEntity::toModel)
                .toList();
    }

    @Override
    public Optional<StockHave> findById(Long id) {
        return stockHaveJpaRepository.findById(id).map(StockHaveEntity::toModel);
    }

    @Override
    public StockHave save(StockHave stockHave) {
        return stockHaveJpaRepository.save(StockHaveEntity.from(stockHave)).toModel();
    }

    @Override
    public void deleteById(Long id) {
        stockHaveJpaRepository.deleteById(id);
    }
}
