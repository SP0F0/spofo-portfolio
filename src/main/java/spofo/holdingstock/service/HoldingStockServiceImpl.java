package spofo.holdingstock.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spofo.global.domain.exception.HoldingStockNotFound;
import spofo.holdingstock.controller.port.HoldingStockService;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.holdingstock.domain.HoldingStockStatistic;
import spofo.holdingstock.service.port.HoldingStockRepository;
import spofo.portfolio.domain.Portfolio;
import spofo.stock.domain.Stock;
import spofo.stock.service.StockServerService;
import spofo.tradelog.controller.port.TradeLogService;
import spofo.tradelog.domain.TradeLogCreate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HoldingStockServiceImpl implements HoldingStockService {

    private final TradeLogService tradeLogService;
    private final HoldingStockRepository holdingStockRepository;
    private final StockServerService stockServerService;

    @Override
    public List<HoldingStock> getByPortfolioId(Long portfolioId) {
        return holdingStockRepository.findByPortfolioId(portfolioId);
    }

    @Override
    public HoldingStockStatistic getStatistic(Long id) {
        HoldingStock holdingStock = findById(id);
        String stockCode = holdingStock.getStockCode();
        Stock stock = stockServerService.getStock(stockCode);

        return HoldingStockStatistic.of(holdingStock, stock);
    }

    @Override
    public HoldingStock get(Long id) {
        return findById(id);
    }

    @Override
    public HoldingStock get(Portfolio portfolio, String stockCode) {
        return holdingStockRepository.findByStockCode(portfolio, stockCode)
                .orElse(null);
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
        tradeLogService.deleteByHoldingStockId(id);
        holdingStockRepository.delete(savedHoldingStock);
    }

    @Override
    @Transactional
    public void deleteByPortfolioId(Long portfolioId) {
        List<HoldingStock> holdingStocks = holdingStockRepository.findByPortfolioId(portfolioId);
        List<Long> holdingStockIds = getHoldingStockIds(holdingStocks);

        tradeLogService.deleteByHoldingStockIds(holdingStockIds);
        holdingStockRepository.deleteByPortfolioId(portfolioId);
    }

    @Override
    public List<HoldingStockStatistic> getHoldingStockStatistics(Long portfolioId) {
        List<HoldingStock> holdingStocks = getByPortfolioId(portfolioId);
        List<String> stockCodes = getStockCodes(holdingStocks);
        Map<String, Stock> stocks = stockServerService.getStocks(stockCodes);
        return holdingStocks.stream()
                .map(holdingStock -> HoldingStockStatistic.of(holdingStock,
                        stocks.get(holdingStock.getStockCode())))
                .toList();
    }

    private HoldingStock findById(Long id) {
        return getFrom(holdingStockRepository.findById(id));
    }

    private HoldingStock getFrom(Optional<HoldingStock> holdingStockOptional) {
        return holdingStockOptional.orElseThrow(HoldingStockNotFound::new);
    }

    private List<String> getStockCodes(List<HoldingStock> holdingStocks) {
        return holdingStocks.stream()
                .map(HoldingStock::getStockCode)
                .distinct()
                .toList();
    }

    private List<Long> getHoldingStockIds(List<HoldingStock> holdingStocks) {
        return holdingStocks.stream()
                .map(HoldingStock::getId)
                .toList();
    }
}
