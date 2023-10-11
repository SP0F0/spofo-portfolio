package spofo.portfolio.controller;

import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spofo.auth.domain.MemberInfo;
import spofo.auth.domain.annotation.LoginMember;
import spofo.portfolio.controller.response.CreatePortfolioResponse;
import spofo.portfolio.controller.response.PortfolioResponse;
import spofo.portfolio.controller.response.PortfolioStatisticResponse;
import spofo.portfolio.controller.response.PortfoliosStatisticResponse;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioCreate;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.PortfolioUpdate;
import spofo.portfolio.domain.PortfoliosStatistic;
import spofo.portfolio.service.PortfolioServiceImpl;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioServiceImpl portfolioServiceImpl;

    @GetMapping("/portfolios/total")
    public ResponseEntity<PortfoliosStatisticResponse> getPortfoliosStatistic(
            @LoginMember MemberInfo memberInfo) {
        PortfoliosStatistic statistic =
                portfolioServiceImpl.getPortfoliosStatistic(memberInfo.getId());

        return ok(PortfoliosStatisticResponse.from(statistic));
    }

    @GetMapping("/portfolios")
    public ResponseEntity<List<PortfolioStatisticResponse>> getPortfolioSimple(
            @LoginMember MemberInfo memberInfo) {
        List<PortfolioStatisticResponse> portfolios
                = portfolioServiceImpl.getPortfolios(memberInfo.getId())
                .stream()
                .map(PortfolioStatisticResponse::from)
                .toList();

        return ok(portfolios);
    }

    @GetMapping("/portfolios/{portfolioId}")
    public ResponseEntity<PortfolioResponse> getPortfolio(
            @PathVariable(name = "portfolioId") Long portfolioId) {
        Portfolio portfolio = portfolioServiceImpl.getPortfolio(portfolioId);

        return ok(PortfolioResponse.from(portfolio));
    }

    @GetMapping("/portfolios/{portfolioId}/total")
    public ResponseEntity<PortfolioStatisticResponse> getPortfolioStatistic(
            @PathVariable(name = "portfolioId") Long portfolioId) {
        PortfolioStatistic portfolio = portfolioServiceImpl.getPortfolioStatistic(portfolioId);

        return ok(PortfolioStatisticResponse.from(portfolio));
    }

    @PostMapping("/portfolios")
    public ResponseEntity<CreatePortfolioResponse> create(
            @RequestBody @Valid PortfolioCreate portfolioCreate,
            @LoginMember MemberInfo memberInfo) {
        Portfolio portfolio = portfolioServiceImpl.create(portfolioCreate, memberInfo.getId());

        return ok(CreatePortfolioResponse.from(portfolio));
    }

    @PutMapping("/portfolios/{portfolioId}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "portfolioId") Long portfolioId,
            @RequestBody @Valid PortfolioUpdate request,
            @LoginMember MemberInfo memberInfo) {
        portfolioServiceImpl.update(request, portfolioId, memberInfo.getId());
        return ok().build();
    }

    @DeleteMapping("/portfolios/{portfolioId}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "portfolioId") Long portfolioId) {
        portfolioServiceImpl.delete(portfolioId);
        return ok().build();
    }
}
