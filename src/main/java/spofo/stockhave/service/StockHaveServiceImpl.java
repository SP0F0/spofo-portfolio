package spofo.stockhave.service;

import static spofo.global.component.utils.CommonUtils.getBD;
import static spofo.tradelog.domain.enums.TradeType.BUY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.portfolio.infrastructure.PortfolioJpaRepository;
import spofo.stockhave.controller.response.AddStockResponse;
import spofo.stockhave.controller.response.StockHaveResponse;
import spofo.stockhave.domain.AddStockRequest;
import spofo.stockhave.domain.StockHave;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.stockhave.infrastructure.StockHaveJpaRepository;
import spofo.stockhave.service.port.StockHaveRepository;
import spofo.tradelog.domain.CreateTradeLogRequest;
import spofo.tradelog.domain.TradeLog;
import spofo.tradelog.infrastructure.TradeLogEntity;
import spofo.tradelog.infrastructure.TradeLogJpaRepository;
import spofo.tradelog.service.TradeLogServiceImpl;
import spofo.tradelog.service.port.TradeLogRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StockHaveServiceImpl implements StockHaveService {

    private final StockHaveRepository stockHaveRepository;
    private final StockHaveJpaRepository stockHaveJpaRepository;
    private final TradeLogRepository tradeLogRepository;
    private final TradeLogJpaRepository tradeLogJpaRepository;
    private final PortfolioJpaRepository portfolioJpaRepository;
    private final TradeLogServiceImpl tradeLogServiceImpl;
    private final RestClient restClient = RestClient.builder().build();

    // API - 008
    // 모든 보유 종목 불러오기
    @Override
    public List<StockHave> getStocks(Long portfolioId) {
        return stockHaveRepository.findByPortfolioId(portfolioId);
    }

    private StockHaveResponse stockHaveResponse(StockHaveEntity stockHaveEntity) {
        String stockCode = stockHaveEntity.getStockCode();
        Long stockId = stockHaveEntity.getId();

        return StockHaveResponse.from(
                stockHaveEntity,
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
    @Override
    public AddStockResponse addStock(AddStockRequest addStockRequest, Long portfolioId) {
        PortfolioEntity portfolioEntity = portfolioJpaRepository.getReferenceById(portfolioId);
        StockHaveEntity stockHaveEntity = addStockRequest.toEntity(portfolioEntity);
        StockHaveEntity sh = stockHaveJpaRepository.save(stockHaveEntity);
        CreateTradeLogRequest createTradeLogRequest =
                CreateTradeLogRequest.builder()
                        .stockHaveEntity(sh)
                        .type(BUY) // 매도 추가 시 수정
                        .price(addStockRequest.getAvgPrice())
                        .tradeDate(LocalDateTime.parse(addStockRequest.getTradeDate()))
                        .quantity(addStockRequest.getQuantity())
                        .marketPrice(getCurrentPrice(sh.getStockCode()))
                        .build();

        tradeLogServiceImpl.createTradeLog(createTradeLogRequest);

        return AddStockResponse.from(sh);
    }

    @Override
    public StockHave addStock(StockHave stockHave) {
        TradeLog tradeLog = TradeLog.builder()
                .marketPrice(getBD(10000))
                .quantity(getBD(100))
                .price(getBD(10000))
                .tradeDate(LocalDateTime.now())
                .type(BUY)
                .build();

        TradeLog savedTradeLogs = tradeLogRepository.save(tradeLog);
        StockHave savedStockHave = stockHaveRepository.save(stockHave).addTradeLog(savedTradeLogs);
        savedTradeLogs = tradeLogRepository.save(savedTradeLogs);
        return stockHaveRepository.save(savedStockHave);
    }

    // API - 010
    // 종목 추가 매수하기
    @Override
    public AddStockResponse addMoreStock(AddStockRequest addStockRequest,
            Long portfolioId, Long stockId) {
        PortfolioEntity portfolioEntity = portfolioJpaRepository.getReferenceById(portfolioId);
        StockHaveEntity stockHaveEntity = addStockRequest.toEntity(portfolioEntity);
        stockHaveJpaRepository.save(stockHaveEntity);

        return AddStockResponse.from(stockHaveEntity);
    }

    // API - 011
    // 종목 삭제하기
    @Transactional
    @Override
    public void deleteStock(Long stockId) {
        StockHaveEntity stockHaveEntity = stockHaveJpaRepository.getReferenceById(stockId);
        stockHaveJpaRepository.delete(stockHaveEntity);
    }

    // API - 014
    // 종목 단건 조회하기
    @Override
    public List<StockHaveResponse> getStocksByCode(Long portfolioId, String stockCode) {
        return stockHaveJpaRepository
                .findByPortfolioId(portfolioId)
                .stream()
                .map(this::stockHaveResponse)
                .filter(stock -> stockCode.equals(stock.getStockCode()))
                .toList();
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
    // (현재 자산 가치 / 매수가) * 100 - 100
    // From CurrentPrice
    private BigDecimal getGainRate(String stockCode, Long stockId) {
        BigDecimal gainRate = BigDecimal.ZERO;

        try {
            ((getCurrentPrice(stockCode).multiply(getQuantity(stockId)))
                    .divide(getAvgPrice(stockId)))
                    .multiply(BigDecimal.valueOf(100))
                    .subtract(BigDecimal.valueOf(100));
        } catch (ArithmeticException ae) {
            throw new RuntimeException(ae);
        }

        return gainRate;
    }

    // TODO : 보유 종목의 평균 단가(매수가)
    // TradeLog 매수가의 합 / 수량의 합
    // From TradeLog
    private BigDecimal getAvgPrice(Long stockId) {
        StockHaveEntity stockHaveEntity = stockHaveJpaRepository.getReferenceById(stockId);
        BigDecimal totalPrice;
        BigDecimal totalQuantity = getQuantity(stockId);
        BigDecimal avgPrice = BigDecimal.ZERO;

        totalPrice = tradeLogJpaRepository.findByStockHaveEntity(stockHaveEntity)
                .stream()
                .map(TradeLogEntity::getPrice)
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
        StockHaveEntity stockHaveEntity = stockHaveJpaRepository.getReferenceById(stockId);

        return tradeLogJpaRepository.findByStockHaveEntity(stockHaveEntity)
                .stream()
                .map(TradeLogEntity::getQuantity)
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
