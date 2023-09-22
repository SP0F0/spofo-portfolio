import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spofo.portfolio.domain.portfolio.service.PortfolioService;

@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/portfolios/total")
    public String PorfoliosTotal(){
        return ""; // Todo : 전체 포트폴리오 자산 조회
    }
    @GetMapping("/portfolios")
    public String PortfoliosGet(){
        return ""; // Todo : 포트폴리오 목록 조회
    }

    @PostMapping("/portfolios")
    public String PortfoliosPost(CreatePortfolio createDto){
        return ""; // Todo : 포트폴리오 추가
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    String getStatus() {
        return "포트폴리오 서버입니다.";
    }

    @GetMapping("/test/callStock")
    @ResponseStatus(HttpStatus.OK)
    String getStock() {
        return portfolioService.getStock();
    }

    @GetMapping("/test/callAuth")
    @ResponseStatus(HttpStatus.OK)
    String getAuth() {
        return portfolioService.getAuth();
    }


}
