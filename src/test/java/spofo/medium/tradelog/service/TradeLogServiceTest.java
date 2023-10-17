package spofo.medium.tradelog.service;

import static spofo.global.component.utils.CommonUtils.getBD;
import static spofo.tradelog.domain.enums.TradeType.BUY;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spofo.support.service.ServiceTestSupport;
import spofo.tradelog.domain.CreateTradeLogRequest;

public class TradeLogServiceTest extends ServiceTestSupport {

    @Test
    @DisplayName("Trade Log를 생성한다.")
    void createTradeLog() {
        // given

        CreateTradeLogRequest.builder()
                .quantity(getBD(100))
                .type(BUY)
                .price(getBD(10000))
                .holdingStockEntity(null)
                .build();
        // when

        // then
    }

}
