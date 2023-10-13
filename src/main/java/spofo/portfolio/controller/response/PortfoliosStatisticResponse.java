package spofo.portfolio.controller.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import spofo.portfolio.domain.TotalPortofoliosStatistic;

@Data
@Builder
public class PortfoliosStatisticResponse {

    private BigDecimal totalAsset;
    private BigDecimal gain;
    private BigDecimal gainRate;
    private BigDecimal dailyGainRate;

    public static PortfoliosStatisticResponse from(TotalPortofoliosStatistic statistic) {
        return PortfoliosStatisticResponse.builder()
                .totalAsset(statistic.getTotalAsset())
                .gain(statistic.getGain())
                .gainRate(statistic.getGainRate())
                .dailyGainRate(statistic.getDailyGainRate())
                .build();
    }
}
