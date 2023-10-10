package spofo.portfolio.controller.response;

import lombok.Builder;
import lombok.Data;
import spofo.portfolio.domain.Portfolio;
import spofo.portfolio.domain.enums.Currency;
import spofo.portfolio.domain.enums.IncludeType;
import spofo.portfolio.domain.enums.PortfolioType;

@Data
@Builder
public class PortfolioResponse {

    private final Long id;
    private final String name; // 포트폴리오 이름
    private final String detail; // 포트폴리오 설명
    private final Currency currency;
    private final IncludeType includeYn;
    private final PortfolioType type; // 태그

    public static PortfolioResponse from(Portfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .detail(portfolio.getDescription())
                .currency(portfolio.getCurrency())
                .includeYn(portfolio.getIncludeYn())
                .type(portfolio.getType())
                .build();
    }

    /*
    public static PortfolioResponse from(Portfolio portfolio, BigDecimal totalAsset,
            BigDecimal totalBuy, BigDecimal gain, BigDecimal gainRate) {
        return PortfolioResponse.builder()
                //.portfolio(portfolio)
                .totalAsset(totalAsset)
                .totalBuy(totalBuy)
                .gain(gain)
                .gainRate(gainRate)
                .build();
    }
     */

}
