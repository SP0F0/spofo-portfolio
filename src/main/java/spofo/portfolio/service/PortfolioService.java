package spofo.portfolio.service;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import spofo.global.exception.ErrorCode;
import spofo.global.exception.PortfolioException;
import spofo.global.utils.CalculateUtils;
import spofo.portfolio.dto.request.UpdatePortfolioRequest;
import spofo.portfolio.dto.response.CreatePortfolioResponse;
import spofo.portfolio.dto.response.OnePortfolioResponse;
import spofo.portfolio.dto.response.PortfolioResponse;
import spofo.portfolio.dto.response.PortfolioSimpleResponse;
import spofo.portfolio.dto.response.TotalPortfolioResponse;
import spofo.portfolio.entity.Portfolio;
import spofo.portfolio.repository.PortfolioRepository;
import spofo.stock.dto.response.StockHaveResponse;
import spofo.stock.service.StockHaveService;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final StockHaveService stockHaveService;

    private final PortfolioRepository portfolioRepository;
    private final RestClient restClient = RestClient.builder().build();
    //private final StockHave stockHave;

    public Long getMemberId() {
        return Long.valueOf(Objects.requireNonNull(restClient.get()
                .uri("https://www.auth.spofo.net:8080/auth/members/search")
                .retrieve()
                .body(String.class)));
    }


    // 전체 포트폴리오 자산 조회 api-001
    //개요 여부에 따라 포함해서 계산
    public TotalPortfolioResponse getTotalPortfolio(Long memberId) {
        List<Portfolio> portfolios = portfolioRepository.findByMemberId(memberId);
        BigDecimal totalAsset = getAllTotalAsset();
        BigDecimal gain = getAllGain();
        BigDecimal gainRate = getAllGainRate(getAllTotalAsset(), getAllBuy());
        BigDecimal dailyGainRate = getAllDailyGainRate();
        return TotalPortfolioResponse.from(totalAsset, gain, gainRate, dailyGainRate);
    }

    // todo: 전체 포폴 총 자산 계산 [목록 조회 총 자산의 합]
    private BigDecimal getAllTotalAsset() {
        return BigDecimal.ZERO;
    }

    // todo: 전체 포폴 평가 수익 [목록 조회 평가 수익금의 합]
    private BigDecimal getAllGain() {
        return BigDecimal.ZERO;
    }

    // todo: 전체 포폴 총 매수 금액 [목록 조회 총 매수 금액의 합]
    private BigDecimal getAllBuy() {
        return BigDecimal.ZERO;
    }

    // todo: 전체 포폴 수익률 [((총자산/총매수금액)*100)-100]
    private BigDecimal getAllGainRate(BigDecimal totalAsset, BigDecimal allBuy) {
        if (CalculateUtils.isZERO(totalAsset) || CalculateUtils.isZERO(allBuy)) {
            return ZERO;
        }
        return totalAsset.divide(allBuy, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).subtract(
                        BigDecimal.valueOf(100));
    }

    // todo: 전체 포폴 일간 수익률(전날 대비 일간 수익률) [((오늘 총자산 - 어제 총자산)*100)-100]
    private BigDecimal getAllDailyGainRate() {
        return BigDecimal.ZERO;
    }

    // 포트폴리오 목록 조회 api-002
    public List<PortfolioSimpleResponse> getPortfolioSimple(Long memberId) {
        return portfolioRepository.findByMemberId(memberId).stream()
                .map(portfolio -> PortfolioSimpleResponse.from(portfolio,
                        getGain(getTotalAsset(portfolio.getId()), getTotalBuy(portfolio.getId())),
                        getGainRate(getTotalAsset(portfolio.getId()),
                                getTotalBuy(portfolio.getId())))
                )
                .collect(toList());
    }

    // 포트폴리오 생성 api-005
    public CreatePortfolioResponse createPortfolio(Portfolio createPortfolioRequest) {
        Portfolio portfolio = portfolioRepository.save(
                Portfolio.builder()
                        //.memberId(getMemberId())
                        .memberId(portfolioRepository.findmemberId())
                        .type(createPortfolioRequest.getType())
                        .name(createPortfolioRequest.getName())
                        .description(createPortfolioRequest.getDescription())
                        .currency(createPortfolioRequest.getCurrency())
                        .build()
        );
        return CreatePortfolioResponse.from(portfolio);
    }

    //포트폴리오 단건 조회 api-013
    public OnePortfolioResponse getOnePortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);

        return OnePortfolioResponse.from(portfolio);
    }

    // 포트폴리오 자산 조회 api-004
    public PortfolioResponse getPortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);

        BigDecimal totalAsset = getTotalAsset(portfolioId);
        BigDecimal totalBuy = getTotalBuy(portfolioId);
        BigDecimal gain = getGain(totalAsset, totalBuy);
        BigDecimal gainRate = getGainRate(totalAsset, totalBuy);
        return PortfolioResponse.from(portfolio, totalAsset, totalBuy, gain, gainRate);
    }

    // 포트폴리오 수정 api-006
    @Transactional
    public void updatePortfolio(Long portfolioId, UpdatePortfolioRequest updatePortfolioRequest) {
        Portfolio portfolio = findById(portfolioId);
        portfolio.toUpdate(updatePortfolioRequest.getName(), updatePortfolioRequest.getDetail(),
                updatePortfolioRequest.getIncludeYn());
    }

    // 포트폴리오 삭제 api-007
    @Transactional
    public void deletePortfolio(Long portfolioId) {
        Portfolio portfolio = findById(portfolioId);
        portfolioRepository.delete(portfolio);
    }

    private Portfolio findById(Long id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() ->
                        new PortfolioException(ErrorCode.PORTFOLIO_NOT_FOUND));
    }

    /**
     * 각 종목 현재가 * 수량
     **/
    private BigDecimal getTotalAsset(Long portfolioId) {
        BigDecimal totalAsset = ZERO;
        List<StockHaveResponse> stockHaveResponses = stockHaveService.getStocks(portfolioId);
        for (StockHaveResponse stockHaveResponse : stockHaveResponses) {
            totalAsset = totalAsset.add(stockHaveResponse.getTotalAsset());
        }
        return totalAsset;
    }

    /**
     * 각 종목의 구매가 * 수량
     **/
    private BigDecimal getTotalBuy(Long portfolioId) {
        BigDecimal totalBuy = ZERO;
        List<StockHaveResponse> stockHaveResponses = stockHaveService.getStocks(portfolioId);
        for (StockHaveResponse stockHaveResponse : stockHaveResponses) {
            totalBuy = totalBuy.add(stockHaveResponse.getAvgPrice());
        }
        return totalBuy;
    }

    /**
     * 평가 손익 계산 총 자산 - 총 매수 금액
     **/
    private BigDecimal getGain(BigDecimal totalAsset, BigDecimal totalBuy) {
        return totalAsset.subtract(totalBuy);
    }

    /**
     * 수익률 계산 ((총 자산/총 매수 금액)*100)-100
     **/
    private BigDecimal getGainRate(BigDecimal totalAsset, BigDecimal totalBuy) {
        if (CalculateUtils.isZERO(totalAsset) || CalculateUtils.isZERO(totalBuy)) {
            return ZERO;
        }
        return totalAsset.divide(totalBuy, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).subtract(
                        BigDecimal.valueOf(100));
    }

    /**
     * TODO : 일간 수익률 계산
     * private BigDecimal getReturnPerDay() {
     * <p>
     * }
     **/


}
