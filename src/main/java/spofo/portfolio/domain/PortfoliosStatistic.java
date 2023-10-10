package spofo.portfolio.domain;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static spofo.global.component.utils.CommonUtils.getCommonScale;
import static spofo.global.component.utils.CommonUtils.isZero;
import static spofo.global.component.utils.CommonUtils.toPercent;
import static spofo.portfolio.domain.enums.IncludeType.Y;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortfoliosStatistic {

    private BigDecimal totalAsset; // 포트폴리오 자산들의 합계
    private BigDecimal gain; // 포트폴리오 수익들의 합계
    private BigDecimal gainRate; // 총 수익 / 총 자산
    private BigDecimal dailyGainRate;

    public static PortfoliosStatistic of(List<PortfolioStatistic> portfolioStatistics) {
        BigDecimal totalAsset = ZERO;
        BigDecimal totalBuy = ZERO;
        BigDecimal totalGain = ZERO;
        BigDecimal gainRate = ZERO;

        for (PortfolioStatistic statistic : portfolioStatistics) {
            if (statistic.getPortfolio().getIncludeYn() == Y) {
                totalAsset = totalAsset.add(statistic.getTotalAsset());
                totalBuy = totalBuy.add(statistic.getTotalBuy());
                totalGain = totalGain.add(statistic.getTotalGain());
            }
        }

        if (!isZero(totalBuy)) {
            gainRate = toPercent(totalGain.divide(totalBuy, getCommonScale(), HALF_UP));
        }

        return PortfoliosStatistic.builder()
                .totalAsset(totalAsset)
                .gain(totalGain)
                .gainRate(gainRate)
                //.dailyGainRate(portfolio)
                .build();
    }

}
