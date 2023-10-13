package spofo.tradelog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static spofo.global.component.utils.CommonUtils.getBD;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import spofo.portfolio.infrastructure.PortfolioEntity;
import spofo.stockhave.infrastructure.StockHaveEntity;
import spofo.stockhave.infrastructure.StockHaveJpaRepository;
import spofo.tradelog.controller.response.TradeLogResponse;
import spofo.tradelog.domain.CreateTradeLogRequest;
import spofo.tradelog.domain.enums.TradeType;
import spofo.tradelog.infrastructure.TradeLogEntity;
import spofo.tradelog.infrastructure.TradeLogJpaRepository;

@SpringBootTest(classes = {TradeLogServiceImpl.class})
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("종목 이력 서비스")
class TradeLogServiceImplTest {

    @Autowired
    private TradeLogServiceImpl tradeLogServiceImpl;

    @MockBean
    private TradeLogJpaRepository tradeLogJpaRepository;

    @MockBean
    private StockHaveJpaRepository stockHaveJpaRepository;

    private static Long STOCK_ID = 1L;
    private static Long PORTFOLIO_ID = 1L;

    @DisplayName("보유 종목에 대한 이력을 생성한다.")
    void createTradeLog() {

        // given
        PortfolioEntity mockPortfolioEntity = getMockPortfolio();
        StockHaveEntity mockStockHaveEntity = getMockStockHave(mockPortfolioEntity);
        CreateTradeLogRequest mockCreateTradeLogRequest = CreateTradeLogRequest.builder()
                .stockHaveEntity(mockStockHaveEntity)
                .type(TradeType.BUY)
                .price(getBD(1000))
                .tradeDate(LocalDateTime.now())
                .quantity(getBD(2))
                .marketPrice(getBD(1000))
                .build();

        // then
//        tradeLogServiceImpl.createTradeLog(mockCreateTradeLogRequest);

        // when
        then(tradeLogJpaRepository).should().save(any(TradeLogEntity.class));
    }

    @DisplayName("보유 종목에 대한 이력을 조회한다.")
    void getTradeLogs() {

        // given
        PortfolioEntity mockPortfolioEntity = getMockPortfolio();
        StockHaveEntity mockStockHaveEntity = getMockStockHave(mockPortfolioEntity);
        TradeLogEntity mockTradeLogEntity = getMockTradeLog(mockStockHaveEntity);

        List<TradeLogEntity> mockTradeLogEntityList = List.of(mockTradeLogEntity);

        given(stockHaveJpaRepository.getReferenceById(STOCK_ID)).willReturn(mockStockHaveEntity);
        given(tradeLogJpaRepository.findByStockHaveEntity(mockStockHaveEntity)).willReturn(
                mockTradeLogEntityList);
        TradeLogResponse tradeLogResponse = TradeLogResponse.from(mockTradeLogEntity,
                BigDecimal.ZERO,
                getBD(2000));

        // when
        List<TradeLogResponse> findTradeLogsList = tradeLogServiceImpl.getTradeLogs(STOCK_ID);

        // then
        assertThat(findTradeLogsList.get(0)).isEqualTo(tradeLogResponse);
    }

    private PortfolioEntity getMockPortfolio() {
        return null;
    }

    private StockHaveEntity getMockStockHave(PortfolioEntity portfolioEntity) {
        return null;
    }

    private TradeLogEntity getMockTradeLog(StockHaveEntity stockHaveEntity) {
        return null;
    }
}