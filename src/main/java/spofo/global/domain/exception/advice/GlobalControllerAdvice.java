package spofo.global.domain.exception.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spofo.global.domain.exception.ErrorCode;
import spofo.global.domain.exception.PortfolioException;
import spofo.global.domain.exception.dto.ErrorResult;

@Slf4j
@RestControllerAdvice
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
    public ResponseEntity<ErrorResult> portfolioExHandler(PortfolioException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(errorCode.getStatus())
                .errorMessage(errorCode.getMessage())
                .build();

        return status(errorCode.getStatus()).body(errorResult);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> bindExHandler(BindException e) {
        String errorMessage = e.hasGlobalErrors() ?
                e.getBindingResult().getGlobalError().getDefaultMessage()
                : "잘못된 요청입니다.";

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(BAD_REQUEST)
                .errorMessage(errorMessage)
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            errorResult.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return badRequest().body(errorResult);
    }

    // 종목 추가 에러 핸들러
    @ExceptionHandler
    public ResponseEntity DataIntegrityViolationException(DataIntegrityViolationException e) {
        return status(HttpStatus.BAD_REQUEST)
                .body("찾을 수 없는 포트폴리오 아이디입니다. \n" + e.getMessage());
    }

}
