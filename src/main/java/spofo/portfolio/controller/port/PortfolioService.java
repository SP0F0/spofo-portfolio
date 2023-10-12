package spofo.portfolio.controller.port;

import java.util.List;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.PortfoliosStatistic;

public interface PortfolioService {

    PortfoliosStatistic getPortfoliosStatistic(Long memberId);

    List<PortfolioStatistic> getPortfolios(Long memberId);

    Portfolio getPortfolio(Long id);

    PortfolioStatistic getPortfolioStatistic(Long id);

    Portfolio create(PortfolioCreate request, Long memberId);

    Portfolio update(PortfolioUpdate request, Long id, Long memberId);

    void delete(Long id);
}