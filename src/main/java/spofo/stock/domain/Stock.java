package spofo.stock.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Stock {

    private String code;
    private String name;
    private BigDecimal price;
    private String sector;

}
