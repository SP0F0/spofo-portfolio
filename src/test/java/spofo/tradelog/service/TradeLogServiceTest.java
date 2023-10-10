package spofo.tradelog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import spofo.portfolio.domain.enums.IncludeType;
import spofo.portfolio.domain.enums.PortfolioType;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.stockhave.infrastructure.StockJpaHaveRepository;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.CreateTradeLogRequest;
import spofo.tradelog.domain.enums.TradeType;
import spofo.tradelog.infrastructure.TradeJpaLogRepository;
import spofo.tradelog.infrastructure.TradeLogEntity;

@SpringBootTest(classes = {TradeLogService.class})
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("종목 이력 서비스")
class TradeLogServiceTest {

    @Autowired
    private TradeLogService tradeLogService;

    @MockBean
    private TradeJpaLogRepository tradeJpaLogRepository;

    @MockBean
    private StockJpaHaveRepository stockJpaHaveRepository;

    private static Long STOCK_ID = 1L;
    private static Long PORTFOLIO_ID = 1L;

    @Test
    @DisplayName("보유 종목에 대한 이력을 생성한다.")
    void createTradeLog() {

        // given
        PortfolioEntity mockPortfolioEntity = getMockPortfolio();
        StockHaveEntity mockStockHaveEntity = getMockStockHave(mockPortfolioEntity);
        CreateTradeLogRequest mockCreateTradeLogRequest = CreateTradeLogRequest.builder()
                .stockHaveEntity(mockStockHaveEntity)
                .type(TradeType.B)
                .price(BigDecimal.valueOf(1000))
                .tradeDate(LocalDateTime.now())
                .quantity(BigDecimal.valueOf(2))
                .marketPrice(BigDecimal.valueOf(1000))
                .build();

        // then
        tradeLogService.createTradeLog(mockCreateTradeLogRequest);

        // when
        then(tradeJpaLogRepository).should().save(any(TradeLogEntity.class));
    }

    @Test
    @DisplayName("보유 종목에 대한 이력을 조회한다.")
    void getTradeLogs() {

        // given
        PortfolioEntity mockPortfolioEntity = getMockPortfolio();
        StockHaveEntity mockStockHaveEntity = getMockStockHave(mockPortfolioEntity);
        TradeLogEntity mockTradeLogEntity = getMockTradeLog(mockStockHaveEntity);

        List<TradeLogEntity> mockTradeLogEntityList = List.of(mockTradeLogEntity);

        given(stockJpaHaveRepository.getReferenceById(STOCK_ID)).willReturn(mockStockHaveEntity);
        given(tradeJpaLogRepository.findByStockHave(mockStockHaveEntity)).willReturn(
                mockTradeLogEntityList);
        TradeLogResponse tradeLogResponse = TradeLogResponse.from(mockTradeLogEntity,
                BigDecimal.ZERO,
                BigDecimal.valueOf(2000));

        // when
        List<TradeLogResponse> findTradeLogsList = tradeLogService.getTradeLogs(STOCK_ID);

        // then
        assertThat(findTradeLogsList.get(0)).isEqualTo(tradeLogResponse);
    }

    private PortfolioEntity getMockPortfolio() {
        return PortfolioEntity.builder()
                .id(1L)
                .memberId(1L)
                .name("name-test")
                .description("detail-test")
                //.currency("한국")
                .includeYn(IncludeType.Y)
                .type(PortfolioType.FAKE)
                .build();
    }

    private StockHaveEntity getMockStockHave(PortfolioEntity portfolioEntity) {
        return StockHaveEntity.builder()
                .id(1L)
                .stockCode("test")
                .portfolioEntity(portfolioEntity)
                .build();
    }

    private TradeLogEntity getMockTradeLog(StockHaveEntity stockHaveEntity) {
        return null;
        /*
        return TradeLogEntity.builder()
                //.stockHave(stockHaveEntity)
                .tradeType(TradeType.B)
                .price(BigDecimal.valueOf(1000))
                .tradeDate(LocalDateTime.now())
                .quantity(BigDecimal.valueOf(2))
                .marketPrice(BigDecimal.valueOf(1000))
                .build();

         */
    }
}