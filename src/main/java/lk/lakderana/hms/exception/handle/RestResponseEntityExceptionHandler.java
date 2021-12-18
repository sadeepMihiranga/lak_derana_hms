package lk.lakderana.hms.exception.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lakderana.hms.exception.BaseException;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.exception.OperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static lk.lakderana.hms.util.ResponseMessageKeys.*;
import static lk.lakderana.hms.util.ResponseMessageKeys.KEY_CODE;

/**
 * Class to generate corresponding HTTP responses based on exception.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    ObjectMapper mapper = new ObjectMapper();
    HttpHeaders httpHeaders =  new HttpHeaders();
    {
        httpHeaders.set("Content-Type","application/json");
    }

    private ResponseEntity handleException(BaseException ex, HttpStatus httpStatus) throws IOException {
        Map<String ,Object> errorAttributes = new LinkedHashMap<>();

        errorAttributes.put(KEY_DATA, null);
        errorAttributes.put(KEY_MESSAGE, ex.getDescription());
        errorAttributes.put(KEY_SUCCESS, false);
        errorAttributes.put(KEY_CODE, ex.getCode());

        return new ResponseEntity<Object>(errorAttributes, httpStatus);
    }
    
    @ExceptionHandler(InvalidDataException.class)
    public final ResponseEntity handleInvalidDataException(InvalidDataException ex) throws IOException {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity handleDataNotFoundException(DataNotFoundException ex) throws IOException {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(OperationException.class)
    public final ResponseEntity handleOperationException(OperationException ex) throws IOException {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DataNotFoundException notFoundException = new DataNotFoundException(ex.getMessage());
        try {
            return handleException(notFoundException, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            logger.debug("Auth -> handleMissingServletRequestParameter -> RestResponseEntityExceptionHandler : " + e.getMessage());
        }
        return new ResponseEntity(status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleTypeMismatch(ex, headers, status, request);
    }
}
