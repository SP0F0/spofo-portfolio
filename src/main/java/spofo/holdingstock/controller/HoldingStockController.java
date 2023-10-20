package spofo.holdingstock.controller;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spofo.holdingstock.controller.port.HoldingStockService;
import spofo.holdingstock.controller.request.HoldingStockRequest;
import spofo.holdingstock.controller.response.AddStockResponse;
import spofo.holdingstock.controller.response.HoldingStockResponse;
import spofo.holdingstock.domain.AddStockRequest;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.holdingstock.domain.HoldingStockStatistic;
import spofo.portfolio.controller.port.PortfolioService;
import spofo.portfolio.domain.Portfolio;
import spofo.tradelog.domain.TradeLogCreate;

@RestController
@RequiredArgsConstructor
public class HoldingStockController {

    private final HoldingStockService holdingStockService;
    private final PortfolioService portfolioService;

    @GetMapping("/portfolios/{portfolioId}/stocks")
    public ResponseEntity<List<HoldingStockResponse>> getHoldingStocks(
            @PathVariable("portfolioId") Long portfolioId) {
        List<HoldingStock> holdingStocks = holdingStockService.getByPortfolioId(portfolioId);
        List<HoldingStockResponse> result = null;

        return ok().body(result);
    }

    @PutMapping("/portfolios/{portfolioId}/stocks")
    public ResponseEntity<AddStockResponse> create(
            @RequestBody @Validated HoldingStockRequest request,
            @PathVariable("portfolioId") Long portfolioId) {
        HoldingStockCreate holdingStockCreate = request.toHoldingStockCreate();
        TradeLogCreate tradeLogCreate = request.toTradeLogCreate();
        Portfolio portfolio = portfolioService.getPortfolio(portfolioId);

        HoldingStock holdingStock =
                holdingStockService.create(holdingStockCreate, tradeLogCreate, portfolio);

        // 흠

        return ok(null);
    }

//    @PutMapping("/portfolios/{portfolioId}/stocks/{stockId}")
//    public ResponseEntity<AddStockResponse> addMoreStock(
//            @RequestBody @Validated HoldingStockRequest request,
//            @PathVariable("portfolioId") Long portfolioId,
//            @PathVariable("stockId") Long stockId) {
//        HoldingStockCreate holdingStockCreate = request.toHoldingStockCreate();
//        TradeLogCreate tradeLogCreate = request.toTradeLogCreate();
//        Portfolio portfolio = portfolioService.getPortfolio(portfolioId);
//
//        HoldingStock holdingStock =
//                holdingStockService.create(holdingStockCreate, tradeLogCreate, portfolio);
//
//        return ok(null);
//    }

    @DeleteMapping("/portfolios/{portfolioId}/stocks/{stockId}")
    public ResponseEntity<Void> deleteStock(
            @PathVariable("portfolioId") Long portfolioId,
            @PathVariable("stockId") Long stockId) {
        holdingStockService.delete(stockId);
        return ok().build();
    }

    @GetMapping("/portfolios/{portfolioId}/stocks/{stockCode}")
    public ResponseEntity<HoldingStockResponse> getStocksByCode(
            @PathVariable("portfolioId") Long portfolioId,
            @PathVariable("stockCode") String stockCode) {
        // TODO : 전체 보유 종목 조회
        //List<HoldingStockResponse> result = holdingStockService.getStocksByCode(portfolioId,stockCode);
        return ok().body(null);
    }
}
