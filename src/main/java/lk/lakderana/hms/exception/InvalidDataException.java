package lk.lakderana.hms.exception;

import lk.lakderana.hms.exception.code.CustomErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Exception class for invalid user inputs
 */
public class InvalidDataException extends BaseException{

    private static String message = "Invalid data found in the request.";
    private static HttpStatus status = HttpStatus.BAD_REQUEST;
    private static Integer code = CustomErrorCode.INVALID_INPUT;

    public InvalidDataException(String description) {
        super(message, code, status, description);
    }

    public InvalidDataException(String message,String description) {
        super(message, code, status, description);
    }

    public InvalidDataException() {

    }
}
