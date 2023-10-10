package spofo.global.domain.exception;

import static spofo.global.domain.exception.ErrorCode.MEMBER_NOT_VALID;

public class TokenNotValidException extends PortfolioException {

    public TokenNotValidException() {
        super(MEMBER_NOT_VALID);
    }
}
