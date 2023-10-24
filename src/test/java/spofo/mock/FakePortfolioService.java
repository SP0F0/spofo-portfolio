package spofo.mock;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import spofo.global.domain.exception.PortfolioNotFound;
import spofo.holdingstock.domain.HoldingStock;
import spofo.portfolio.controller.port.PortfolioService;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.TotalPortfoliosStatistic;
import spofo.portfolio.service.port.PortfolioRepository;
import spofo.stock.domain.Stock;
import spofo.stock.service.StockServerService;

@RequiredArgsConstructor
public class FakePortfolioService implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockServerService stockServerService;

    @Override
    public TotalPortfoliosStatistic getPortfoliosStatistic(Long memberId) {
        List<Portfolio> portfolios = portfolioRepository.findByMemberIdWithTradeLogs(memberId);
        List<PortfolioStatistic> portfolioStatistics = getPortfolioStatistics(portfolios);
        return TotalPortfoliosStatistic.of(portfolioStatistics);
    }

    @Override
    public List<PortfolioStatistic> getPortfolios(Long memberId) {
        List<Portfolio> portfolios = portfolioRepository.findByMemberIdWithTradeLogs(memberId);
        return getPortfolioStatistics(portfolios);
    }

    @Override
    public Portfolio getPortfolio(Long id) {
        return findById(id);
    }

    @Override
    public PortfolioStatistic getPortfolioStatistic(Long id) {
        Portfolio portfolio = getPortfolioFrom(portfolioRepository.findByIdWithTradeLogs(id));
        return getPortfolioStatistics(List.of(portfolio)).get(0);
    }

    @Override
    public Portfolio create(PortfolioCreate request, Long memberId) {
        Portfolio portfolio = Portfolio.of(request, memberId);
        return portfolioRepository.save(portfolio);
    }

    @Override
    public Portfolio update(PortfolioUpdate request, Long id, Long memberId) {
        Portfolio savedPortfolio = findById(id);
        Portfolio updatedPortfolio = savedPortfolio.update(request, memberId);
        return portfolioRepository.save(updatedPortfolio);
    }

    @Override
    public void delete(Long id) {
        Portfolio portfolio = findById(id);
        portfolioRepository.delete(portfolio);
    }

    private Portfolio findById(Long id) {
        return getPortfolioFrom(portfolioRepository.findById(id));
    }

    private Portfolio getPortfolioFrom(Optional<Portfolio> portfolioOptional) {
        return portfolioOptional.orElseThrow(() -> new PortfolioNotFound());
    }

    private List<PortfolioStatistic> getPortfolioStatistics(List<Portfolio> portfolios) {
        List<String> stockCodes = getStockCodes(portfolios);
        Map<String, Stock> stocks = stockServerService.getStocks(stockCodes);
        return portfolios.stream()
                .map(portfolio -> PortfolioStatistic.of(portfolio, stocks))
                .toList();
    }

    private List<String> getStockCodes(List<Portfolio> portfolios) {
        return portfolios.stream()
                .flatMap(portfolio -> portfolio.getHoldingStocks().stream()
                        .map(HoldingStock::getStockCode))
                .distinct()
                .toList();
    }
}
