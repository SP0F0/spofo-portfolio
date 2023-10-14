package spofo.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

	/*
	// 스프링 컨텍스트와 restDoc을 연결해준다.
	// 그러나 spring rest doc 작성을 위해서 스프링 컨테이너를 실행해야 하는 단점이 있다.
	@BeforeEach
	void setup(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider provider) {

		// 스프링의 컨텍스트를 넣는데 restDoc 설정을 연결해준다.
		this.mockMvc = webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(provider))
			.build();
	}
	 */

    // 스프링 컨테이너 없이 Spring rest doc 설정하기
    @BeforeEach
    void setup(RestDocumentationContextProvider provider) {
        this.mockMvc = standaloneSetup(initController())
                .apply(documentationConfiguration(provider))
                .build();
    }

    protected abstract Object initController();

}