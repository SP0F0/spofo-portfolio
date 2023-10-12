package spofo.tradelog.domain.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static spofo.tradelog.domain.enums.TradeType.BUY;
import static spofo.tradelog.domain.enums.TradeType.SELL;
import static spofo.tradelog.domain.enums.TradeType.findByDbValue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TradeTypeTest {

    @Test
    @DisplayName("db value를 입력받아 Buy 값을 가지는 Trade Type을 찾는다.")
    void toFindBuyByDbValue() {
        // given
        int dbData = 1;

        // when
        TradeType findedTradeType = findByDbValue(dbData);

        // then
        assertThat(findedTradeType).isEqualTo(BUY);
    }

    @Test
    @DisplayName("db value를 입력받아 Sell 값을 가지는 Trade Type을 찾는다.")
    void toFindSellByDbValue() {
        // given
        int dbData = 2;

        // when
        TradeType findedTradeType = findByDbValue(dbData);

        // then
        assertThat(findedTradeType).isEqualTo(SELL);
    }

    @Test
    @DisplayName("유효하지 않은 dbValue이면 기본값인 BUY를 반환한다.")
    void toFindWithInvalid() {
        // given
        int dbData = 3;

        // when
        TradeType findedTradeType = findByDbValue(dbData);

        // then
        assertThat(findedTradeType).isEqualTo(BUY);
    }
}