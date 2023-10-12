package spofo.support.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import spofo.global.config.restclient.RestClientConfig;
import spofo.mock.FakeAuthServerServiceImpl;
import spofo.portfolio.controller.PortfolioController;

/**
 * @see ControllerTestMockBeanSupport
 */
@WebMvcTest(controllers = {PortfolioController.class})
@Import({FakeAuthServerServiceImpl.class, RestClientConfig.class})
public abstract class ControllerTestSupport extends ControllerTestMockBeanSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
