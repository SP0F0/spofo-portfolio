package spofo.portfolio.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spofo.portfolio.domain.enums.PortfolioType;
import spofo.portfolio.infrastructure.PortfolioEntity;

@Data
@NoArgsConstructor
public class CreatePortfolioRequest {

    private String name;
    private String description;
    private String currency;
    private PortfolioType type;

    public PortfolioEntity toEntity() {
        return null;
    }

    @Builder
    public CreatePortfolioRequest(String name, String description, String currency,
            PortfolioType type) {
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.type = type;
    }

}
