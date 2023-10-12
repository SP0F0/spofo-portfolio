package spofo.stockhave.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spofo.stockhave.infrastructure.StockHaveEntity;

@Data
@AllArgsConstructor
@Builder
public class AddStockResponse {

    private Long portfolioId;

    public static AddStockResponse from(StockHaveEntity stockHaveEntity) {
        return AddStockResponse.builder()
                .portfolioId(stockHaveEntity.getPortfolioEntity().getId())
                .build();
    }

}
