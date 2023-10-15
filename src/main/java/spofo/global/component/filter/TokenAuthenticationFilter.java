package spofo.global.component.filter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spofo.auth.domain.MemberInfoHolder;
import spofo.auth.service.AuthServerService;
import spofo.global.domain.exception.ErrorCode;
import spofo.global.domain.exception.TokenNotValid;
import spofo.global.domain.exception.dto.ErrorResult;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServerService authServerService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String idToken = request.getHeader(AUTHORIZATION);
            Long memberId = authServerService.verify(idToken)
                    .orElseThrow(() -> new TokenNotValid());

            MemberInfoHolder.set(memberId);

            filterChain.doFilter(request, response);
        } catch (TokenNotValid ex) {
            handleTokenNotValidException(response, ex);
        } finally {
            MemberInfoHolder.clear();
        }
    }

    private void handleTokenNotValidException(HttpServletResponse response,
            TokenNotValid ex) throws IOException {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(errorCode.getStatus())
                .errorMessage(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }

}
