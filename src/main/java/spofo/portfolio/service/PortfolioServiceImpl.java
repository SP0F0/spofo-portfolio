package spofo.portfolio.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spofo.global.domain.exception.PortfolioNotFound;
import spofo.portfolio.controller.port.PortfolioService;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.PortfoliosStatistic;
import spofo.portfolio.service.port.PortfolioRepository;
import spofo.stock.domain.Stock;
import spofo.stock.service.StockServerService;
import spofo.stockhave.domain.StockHave;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockServerService stockServerService;

    // 전체 포트폴리오 자산 조회 api-001
    @Override
    public PortfoliosStatistic getPortfoliosStatistic(Long memberId) {
        List<Portfolio> portfolios = portfolioRepository.findByMemberIdWithTradeLogs(memberId);
        List<PortfolioStatistic> portfolioStatistics = getPortfolioStatistics(portfolios);
        return PortfoliosStatistic.of(portfolioStatistics);
    }

    // 포트폴리오 목록 조회 api-002
    @Override
    public List<PortfolioStatistic> getPortfolios(Long memberId) {
        List<Portfolio> portfolios = portfolioRepository.findByMemberIdWithTradeLogs(memberId);
        return getPortfolioStatistics(portfolios);
    }

    //포트폴리오 단건 조회 api-013
    @Override
    public Portfolio getPortfolio(Long id) {
        return findById(id);
    }


    // 포트폴리오 자산 조회 api-004
    @Override
    public PortfolioStatistic getPortfolioStatistic(Long id) {
        Portfolio portfolio = getPortfolioFrom(portfolioRepository.findByIdWithTradeLogs(id));
        return getPortfolioStatistics(List.of(portfolio)).get(0);
    }


    // 포트폴리오 생성 api-005
    @Transactional
    @Override
    public Portfolio create(PortfolioCreate request, Long memberId) {
        Portfolio portfolio = Portfolio.of(request, memberId);
        return portfolioRepository.save(portfolio);
    }

    // 포트폴리오 수정 api-006
    @Transactional
    @Override
    public Portfolio update(PortfolioUpdate request, Long id, Long memberId) {
        Portfolio savedPortfolio = findById(id);
        Portfolio updatedPortfolio = savedPortfolio.update(request, memberId);
        return portfolioRepository.save(updatedPortfolio);
    }

    // 포트폴리오 삭제 api-007
    @Transactional
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
                .flatMap(portfolio -> portfolio.getStockHaves().stream()
                        .map(StockHave::getStockCode))
                .distinct()
                .toList();
    }
}
