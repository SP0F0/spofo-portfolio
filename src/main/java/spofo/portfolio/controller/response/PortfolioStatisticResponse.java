package spofo.portfolio.controller.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.PortfolioStatistic;
import spofo.portfolio.domain.enums.PortfolioType;

@Data
@Builder
public class PortfolioStatisticResponse {

    private Long id;
    private String name;
    private String detail;
    private BigDecimal totalAsset;
    private BigDecimal totalBuy;
    private BigDecimal gain;
    private BigDecimal gainRate;
    private PortfolioType type;

    public static PortfolioStatisticResponse from(PortfolioStatistic statistic) {
        Portfolio portfolio = statistic.getPortfolio();

        return PortfolioStatisticResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .detail(portfolio.getDescription())
                .totalAsset(statistic.getTotalAsset())
                .totalBuy(statistic.getTotalBuy())
                .gain(statistic.getTotalGain())
                .gainRate(statistic.getGainRate())
                .type(portfolio.getType())
                .build();
    }
}
