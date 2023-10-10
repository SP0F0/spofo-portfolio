package spofo.global.component.utils;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

public class CommonUtils {

    public static boolean isZero(BigDecimal value) {
        return value.compareTo(ZERO) == 0;
    }

    public static BigDecimal toPercent(BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(100));
    }

    public static int getCommonScale() {
        return 2;
    }
}
