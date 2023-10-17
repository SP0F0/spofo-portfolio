package spofo.holdingstock.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spofo.global.domain.exception.HoldingStockNotFound;
import spofo.holdingstock.controller.port.HoldingStockService;
import spofo.holdingstock.domain.HoldingStock;
import spofo.holdingstock.domain.HoldingStockCreate;
import spofo.holdingstock.service.port.HoldingStockRepository;
import spofo.portfolio.domain.Portfolio;
import spofo.tradelog.controller.port.TradeLogService;
import spofo.tradelog.domain.TradeLogCreate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HoldingStockServiceImpl implements HoldingStockService {

    private final TradeLogService tradeLogService;
    private final HoldingStockRepository holdingStockRepository;

    @Override
    public List<HoldingStock> getByPortfolioId(Long portfolioId) {
        return holdingStockRepository.findByPortfolioId(portfolioId);
    }

    @Override
    public HoldingStock get(Long id) {
        return findById(id);
    }

    @Override
    @Transactional
    public HoldingStock create(HoldingStockCreate holdingStockCreate, TradeLogCreate tradeLogCreate,
            Portfolio portfolio) {
        HoldingStock holdingStock = HoldingStock.of(holdingStockCreate, portfolio);
        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

        tradeLogService.create(tradeLogCreate, savedHoldingStock);

        return savedHoldingStock;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HoldingStock savedHoldingStock = findById(id);
        holdingStockRepository.delete(savedHoldingStock);
    }

    @Override
    public void deleteByPortfolioId(Long portfolioId) {
        holdingStockRepository.deleteByPortfolioId(portfolioId);
    }

    private HoldingStock findById(Long id) {
        return getFrom(holdingStockRepository.findById(id));
    }

    private HoldingStock getFrom(Optional<HoldingStock> holdingStockOptional) {
        return holdingStockOptional.orElseThrow(() -> new HoldingStockNotFound());
    }
    /*

    private final HoldingStockRepository holdingStockRepository;
    private final HoldingStockJpaRepository holdingStockJpaRepository;
    private final TradeLogRepository tradeLogRepository;
    private final TradeLogJpaRepository tradeLogJpaRepository;
    private final PortfolioJpaRepository portfolioJpaRepository;
    private final TradeLogServiceImpl tradeLogServiceImpl;
    private final RestClient restClient = RestClient.builder().build();


    // API - 008
    // 모든 보유 종목 불러오기
    @Override
    public List<HoldingStock> getStocks(Long portfolioId) {
        return holdingStockRepository.findByPortfolioId(portfolioId);
    }

    private HoldingStockResponse holdingStockResponse(HoldingStockEntity holdingStockEntity) {
        String stockCode = holdingStockEntity.getStockCode();
        Long stockId = holdingStockEntity.getId();

        return HoldingStockResponse.from(
                holdingStockEntity,
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
        HoldingStockEntity holdingStockEntity = addStockRequest.toEntity(portfolioEntity);
        HoldingStockEntity sh = holdingStockJpaRepository.save(holdingStockEntity);
        CreateTradeLogRequest createTradeLogRequest =
                CreateTradeLogRequest.builder()
                        .holdingStockEntity(sh)
                        .type(BUY) // 매도 추가 시 수정
                        .price(addStockRequest.getAvgPrice())
                        .tradeDate(LocalDateTime.parse(addStockRequest.getTradeDate()))
                        .quantity(addStockRequest.getQuantity())
                        .marketPrice(getCurrentPrice(sh.getStockCode()))
                        .build();

//        tradeLogServiceImpl.createTradeLog(new TradeLogCreate());

        return AddStockResponse.from(sh);
    }

    @Override
    @Transactional
    public HoldingStock addStock(HoldingStockRequest request, Portfolio portfolio) {
        // stockhave 만들기
        // tradeLog 만들기

        // 쌍방 연관관계 만들기

        HoldingStock holdingStock = null;
        HoldingStock savedHoldingStock = holdingStockRepository.save(holdingStock);

//        TradeLog tradeLog = TradeLog.builder()
//                .marketPrice(getBD(10000))
//                .quantity(getBD(100))
//                .price(getBD(10000))
//                .tradeDate(LocalDateTime.now())
//                .type(BUY)
//                .stockHave(savedStockHave)
//                .build();
//        // savedStockHave.addTradeLog(tradeLog);
//        //TradeLog tradeLog1 = tradeLog.addStockHave(savedStockHave);
//        TradeLog savedTradeLogs = tradeLogRepository.save(tradeLog);
        return savedHoldingStock;
    }

    // API - 010
    // 종목 추가 매수하기
    @Override
    public AddStockResponse addMoreStock(AddStockRequest addStockRequest,
            Long portfolioId, Long stockId) {
        PortfolioEntity portfolioEntity = portfolioJpaRepository.getReferenceById(portfolioId);
        HoldingStockEntity holdingStockEntity = addStockRequest.toEntity(portfolioEntity);
        holdingStockJpaRepository.save(holdingStockEntity);

        return AddStockResponse.from(holdingStockEntity);
    }

    // API - 011
    // 종목 삭제하기
    @Transactional
    @Override
    public void delete(Long stockId) {
        HoldingStockEntity holdingStockEntity = holdingStockJpaRepository.getReferenceById(stockId);
        holdingStockJpaRepository.delete(holdingStockEntity);
    }

    // API - 014
    // 종목 단건 조회하기
    @Override
    public List<HoldingStockResponse> getStocksByCode(Long portfolioId, String stockCode) {
        return holdingStockJpaRepository
                .findByPortfolioId(portfolioId)
                .stream()
                .map(this::holdingStockResponse)
                .filter(stock -> stockCode.equals(stock.getStockCode()))
                .toList();
    }

    @Override
    public void deleteByPortfolioId(Long id) {
        holdingStockRepository.deleteByPortfolioId(id);
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
                    .multiply(getBD(100))
                    .subtract(getBD(100));
        } catch (ArithmeticException ae) {
            throw new RuntimeException(ae);
        }

        return gainRate;
    }

    // TODO : 보유 종목의 평균 단가(매수가)
    // TradeLog 매수가의 합 / 수량의 합
    // From TradeLog
    private BigDecimal getAvgPrice(Long stockId) {
        HoldingStockEntity holdingStockEntity = holdingStockJpaRepository.getReferenceById(stockId);
        BigDecimal totalPrice;
        BigDecimal totalQuantity = getQuantity(stockId);
        BigDecimal avgPrice = BigDecimal.ZERO;

        totalPrice = tradeLogJpaRepository.findByHoldingStockEntity(holdingStockEntity)
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
        HoldingStockEntity holdingStockEntity = holdingStockJpaRepository.getReferenceById(stockId);

        return tradeLogJpaRepository.findByHoldingStockEntity(holdingStockEntity)
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
     */
}
