package lk.lakderana.hms.exception;

import lk.lakderana.hms.response.ResponseCode;
import org.springframework.http.HttpStatus;

public class TransactionConflictException extends BaseException {

    private static String message = "Conflict";
    private static HttpStatus status = HttpStatus.CONFLICT;
    private static Integer code = ResponseCode.CONFLICT_DATA;

    public TransactionConflictException(String description) {
        super(message, code, status, description);
    }

    public TransactionConflictException(String message, String description) {
        super(message, code, status, description);
    }
}
