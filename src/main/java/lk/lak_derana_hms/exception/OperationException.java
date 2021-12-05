package lk.lak_derana_hms.exception;

import lk.lak_derana_hms.exception.code.CustomErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception class for internal server errors
 */
public class OperationException extends BaseException{

    private static String message = "Error Processing the Operation";
    private static HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private static Integer code = CustomErrorCode.ERROR_OPERATION;

    public OperationException(String description) {
        super(message, code, status, description);
    }

    public OperationException(String message,String description) {
        super(message, code, status, description);
    }
}
