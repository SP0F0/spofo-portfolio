package spofo.portfolio.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spofo.portfolio.domain.enums.PortfolioType;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortfolioFilterRequest {

    private PortfolioType filter;

    public boolean isEmpty() {
        return filter == null;
    }
}
