package spofo.holdingstock.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingStockRequest {

    @NotBlank(message = "종목코드는 필수 입력입니다.")
    private String code;

    @NotNull(message = "매수날짜 필수 입력입니다.")
    private LocalDateTime tradeDate;

    @Positive
    @NotNull(message = "수량은 필수 입력입니다.")
    private BigDecimal quantity;

    @Positive
    @NotNull(message = "평균단가는 필수 입력입니다.")
    private BigDecimal avgPrice;
}
