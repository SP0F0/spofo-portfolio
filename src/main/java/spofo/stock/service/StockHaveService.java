package spofo.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import spofo.portfolio.entity.Portfolio;
import spofo.portfolio.repository.PortfolioRepository;
import spofo.stock.dto.request.AddStockRequest;
import spofo.stock.dto.response.AddStockResponse;
import spofo.stock.dto.response.StockHaveResponse;
import spofo.stock.entity.StockHave;
import spofo.stock.repository.StockHaveRepository;
import spofo.tradelog.entity.TradeLog;
import spofo.tradelog.repository.TradeLogRepository;

@Service
@RequiredArgsConstructor
public class StockHaveService {

    private final StockHaveRepository stockHaveRepository;
    private final TradeLogRepository tradeLogRepository;
    private final PortfolioRepository portfolioRepository;
    private final RestClient restClient = RestClient.builder().build();

    // API - 008
    // 모든 보유 종목 불러오기
    public List<StockHaveResponse> getStocks(Long portfolioId) {
        return stockHaveRepository
                .findByPortfolioId(portfolioId)
                .stream()
                .map(this::stockHaveResponse)
                .toList();
    }

    private StockHaveResponse stockHaveResponse(StockHave stockHave) {
        String stockCode = stockHave.getStockCode();
        Long stockId = stockHave.getId();

        return StockHaveResponse.from(
                stockHave,
                getStockName(stockCode),
                getSector(stockCode),
                getStockAsset(stockCode, stockId),
                getGain(stockCode, stockId),
                getGainRate(stockCode, stockId),
                getAvgPrice(stockId),
                getCurrentPrice(stockCode),
                getQuantity(stockId),
                getImageUrl(stockCode)
        );
    }

    // API - 009
    // 종목 추가하기
    public AddStockResponse addStock(AddStockRequest addStockRequest, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.getReferenceById(portfolioId);
        StockHave stockHave = addStockRequest.toEntity(portfolio);
        stockHaveRepository.save(stockHave);

        return AddStockResponse.from(stockHave);
    }

    // API - 010
    // 종목 추가 매수하기
//    public AddStockResponse addMoreStock(AddStockRequest addStockRequest, Long portfolioId,
//            Long stockId) {
//        Portfolio portfolio = portfolioRepository.getReferenceById(portfolioId);
//        StockHave stockHave = stockHaveRepository.getReferenceById(stockId);
//        stockHaveRepository.save(stockHave);
//
//        return AddStockResponse.from(stockHave);
//    }

    // API - 011
    // 종목 삭제하기
    public StockHaveResponse deleteStock(Long stockId) {
        return null;
    }

    // TODO : 종목명 불러오기
    // From Stock
    private String getStockName(String stockCode) {
        String json = restClient.get()
                .uri("http://stock.spofo.net:8080/stocks/{stockCode}", stockCode)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonToMap;
        try {
            jsonToMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonToMap.get("name");
    }

    // TODO : 섹터 (산업명) 불러오기
    // From Stock
    private String getSector(String stockCode) {
        String json = restClient.get()
                .uri("http://stock.spofo.net:8080/stocks/{stockCode}", stockCode)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonToMap;
        try {
            jsonToMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonToMap.get("sector");
    }

    // TODO : 보유 종목의 자산 가치
    // 현재가 * 수량
    // From CurrentPrice
    private BigDecimal getStockAsset(String stockCode, Long stockId) {
        return getCurrentPrice(stockCode).multiply(getQuantity(stockId));
    }

    // TODO : 보유 종목의 수익금
    // (현재가 - 평균 단가) * 수량
    // From CurrentPrice
    private BigDecimal getGain(String stockCode, Long stockId) {
        return (getCurrentPrice(stockCode).subtract(getAvgPrice(stockId))).multiply(
                getQuantity(stockId));
    }

    // TODO : 보유 종목의 수익률
    // (보유 종목의 가치 / 매수가) * 100 - 100
    // From CurrentPrice
    private BigDecimal getGainRate(String stockCode, Long stockId) {
        BigDecimal gainRate = BigDecimal.ZERO;

        try {
            (getGain(stockCode, stockId).divide(getAvgPrice(stockId)))
                    .multiply(BigDecimal.valueOf(100)).subtract(BigDecimal.valueOf(100));
        } catch (ArithmeticException ae) {
            System.out.println("ArithmeticException occurs!");
        }

        return gainRate;
    }

    // TODO : 보유 종목의 평균 단가(매수가)
    // TradeLog 매수가의 합 / 수량의 합
    // From TradeLog
    private BigDecimal getAvgPrice(Long stockId) {
        StockHave stockHave = stockHaveRepository.getReferenceById(stockId);
        BigDecimal totalPrice;
        BigDecimal totalQuantity = getQuantity(stockId);
        BigDecimal avgPrice = BigDecimal.ZERO;

        totalPrice = tradeLogRepository.findByStockHave(stockHave)
                .stream()
                .map(TradeLog::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // Division by 0 Error Handler
        try {
            avgPrice = totalPrice.divide(totalQuantity, 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException ae) {
            System.out.println("ArithmeticException occurs!");
        }

        return avgPrice;
    }

    // TODO : 보유 종목의 현재가
    // From Stock
    private BigDecimal getCurrentPrice(String stockCode) {
        String json = restClient.get()
                .uri("http://stock.spofo.net:8080/stocks/{stockCode}", stockCode)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonToMap;
        try {
            jsonToMap = mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new BigDecimal(jsonToMap.get("price"));
    }

    // TODO : 보유 종목의 수량
    // From TradeLog
    private BigDecimal getQuantity(Long stockId) {
        StockHave stockHave = stockHaveRepository.getReferenceById(stockId);

        return tradeLogRepository.findByStockHave(stockHave)
                .stream()
                .map(TradeLog::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // TODO : 아이콘 이미지 URL
    // From Stock
    private String getImageUrl(String stockCode) {
        String imageUrl = "";
        String json = restClient.get()
                .uri("http://stock.spofo.net:8080/stocks/search?keyword={stockCode}", stockCode)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(json);
            for (JsonNode jn : jsonNode) {
                String jsonStockCode = jn.get("stockCode").asText();
                String jsonImageUrl = jn.get("imageUrl").asText();

                if (stockCode.equals(jsonStockCode)) {
                    imageUrl = jsonImageUrl;
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return imageUrl;
    }
}
