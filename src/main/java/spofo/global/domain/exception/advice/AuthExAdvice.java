package spofo.global.domain.exception.advice;

import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spofo.global.domain.exception.ErrorCode;
import spofo.global.domain.exception.TokenNotValidException;
import spofo.global.domain.exception.dto.ErrorResult;

@RestControllerAdvice
public class AuthExAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> authExHandler(TokenNotValidException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResult errorResult = ErrorResult.builder()
                .errorCode(errorCode.getStatus())
                .errorMessage(errorCode.getMessage())
                .build();

        return status(errorCode.getStatus())
                .body(errorResult);

    }
}
