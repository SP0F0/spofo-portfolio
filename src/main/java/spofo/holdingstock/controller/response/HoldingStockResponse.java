package spofo.holdingstock.controller.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import spofo.holdingstock.domain.HoldingStockStatistic;
import spofo.holdingstock.infrastructure.HoldingStockEntity;
import spofo.stock.domain.Stock;

@Data
@Builder
public class HoldingStockResponse {

    private Long id;
    private String stockCode;
    private String stockName;
    private String sector;
    private BigDecimal totalAsset;
    private BigDecimal gain;
    private BigDecimal gainRate;
    private BigDecimal avgPrice;
    private BigDecimal currentPrice;
    private BigDecimal quantity;
    private String imageUrl;

    public static HoldingStockResponse from(HoldingStockStatistic holdingStockStatistic, Stock stock) {
        return HoldingStockResponse.builder()
                .id(holdingStockStatistic.getHoldingStock().getId())
                .stockCode(stock.getCode())
                .stockName(stock.getName())
                .sector(stock.getSector())
                .totalAsset(holdingStockStatistic.getTotalAsset())
                .gain(holdingStockStatistic.getGain())
                .gainRate(holdingStockStatistic.getGainRate())
                .avgPrice(holdingStockStatistic.getAvgPrice())
                .currentPrice(holdingStockStatistic.getCurrentPrice())
                .quantity(holdingStockStatistic.getQuantity())
                .imageUrl(stock.getImageUrl())
                .build();
    }
}
