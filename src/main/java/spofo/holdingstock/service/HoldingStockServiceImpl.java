package spofo.holdingstock.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spofo.global.domain.exception.HoldingStockNotFound;
import spofo.holdingstock.controller.port.HoldingStockService;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.holdingstock.service.port.HoldingStockRepository;
import spofo.portfolio.domain.Portfolio;
import spofo.tradelog.controller.port.TradeLogService;
import spofo.tradelog.domain.TradeLogCreate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HoldingStockServiceImpl implements HoldingStockService {

    private final TradeLogService tradeLogService;
    private final HoldingStockRepository holdingStockRepository;

    @Override
    public List<HoldingStock> getByPortfolioId(Long portfolioId) {
        return holdingStockRepository.findByPortfolioId(portfolioId);
    }

    @Override
    public HoldingStock get(Long id) {
        return findById(id);
    }

    @Override
    @Transactional
    public HoldingStock create(HoldingStockCreate holdingStockCreate, TradeLogCreate tradeLogCreate,
            Portfolio portfolio) {
        HoldingStock holdingStock = HoldingStock.of(holdingStockCreate, portfolio);
        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

        tradeLogService.create(tradeLogCreate, savedHoldingStock);

        return savedHoldingStock;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HoldingStock savedHoldingStock = findById(id);
        holdingStockRepository.delete(savedHoldingStock);
    }

    @Override
    @Transactional
    public void deleteByPortfolioId(Long portfolioId) {
        holdingStockRepository.deleteByPortfolioId(portfolioId);
    }

    private HoldingStock findById(Long id) {
        return getFrom(holdingStockRepository.findById(id));
    }

    private HoldingStock getFrom(Optional<HoldingStock> holdingStockOptional) {
        return holdingStockOptional.orElseThrow(() -> new HoldingStockNotFound());
    }
}
