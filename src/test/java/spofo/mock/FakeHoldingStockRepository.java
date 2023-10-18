package spofo.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.infrastructure.HoldingStockEntity;
import spofo.holdingstock.service.port.HoldingStockRepository;

public class FakeHoldingStockRepository implements HoldingStockRepository {

    private long autoIncrement = 0;
    List<HoldingStockEntity> data = new ArrayList<>();

    @Override
    public List<HoldingStock> findByPortfolioId(Long id) {
        return data.stream()
                .filter(item -> item.toModel().getPortfolio().getId().equals(id))
                .map(HoldingStockEntity::toModel)
                .toList();
    }

    @Override
    public Optional<HoldingStock> findById(Long id) {
        return data.stream()
                .filter(item -> item.getId().equals(id))
                .map(HoldingStockEntity::toModel)
                .findAny();
    }

    @Override
    public HoldingStock save(HoldingStock holdingStock) {
        if (holdingStock.getId() == null || holdingStock.getId() == 0) {
            HoldingStock newHoldingStock = holdingStock.builder()
                    .id(++autoIncrement)
                    .stockCode(holdingStock.getStockCode())
                    .portfolio(holdingStock.getPortfolio())
                    .build();
            data.add(HoldingStockEntity.from(newHoldingStock));
            return newHoldingStock;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), holdingStock.getId()));
            data.add(HoldingStockEntity.from(holdingStock));
            return holdingStock;
        }
    }

    @Override
    public void delete(HoldingStock holdingStock) {
        data.removeIf(item -> Objects.equals(item.getId(), holdingStock.getId()));
    }

    @Override
    public void deleteByPortfolioId(Long id) {
        data.removeIf(item -> Objects.equals(item.toModel().getPortfolio().getId(), id));
    }
}
