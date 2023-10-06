package spofo.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    /*
    작성 예시입니다
    @ExceptionHandler(PortfolioException.class)
    public String commonExHandler(PortfolioException ex, HttpServletResponse response, Model model) {

        model.addAttribute("error", ex.getMessage());
        response.setStatus(ex.getErrorCode().getStatus().value());

        return "portfolio_error";
    }
    */

    // 종합 에러 핸들러
    @ExceptionHandler
    public ResponseEntity exception(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);

    }

    // 종목 추가 에러 핸들러
    @ExceptionHandler
    public ResponseEntity DataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("찾을 수 없는 포트폴리오 아이디입니다. \n" + e.getMessage());
    }

}
