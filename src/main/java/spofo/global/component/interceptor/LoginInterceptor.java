package spofo.global.component.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import spofo.auth.domain.MemberInfoHolder;
import spofo.auth.service.AuthServerService;
import spofo.global.domain.exception.TokenNotValidException;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final AuthServerService authServerService;

    private static final String AUTHORIZATION = "authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        String idToken = request.getHeader(AUTHORIZATION);

        // todo 추후에 인증 안되었을 때 401에러를 발생시켜야 됨
        Long memberId = authServerService.verify(idToken)
                .orElseThrow(() -> new TokenNotValidException());

        MemberInfoHolder.set(memberId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        MemberInfoHolder.clear();
    }
}
