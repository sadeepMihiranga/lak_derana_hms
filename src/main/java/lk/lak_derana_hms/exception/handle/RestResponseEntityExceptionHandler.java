package lk.lak_derana_hms.exception.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lak_derana_hms.exception.BaseException;
import lk.lak_derana_hms.exception.DataNotFoundException;
import lk.lak_derana_hms.exception.InvalidDataException;
import lk.lak_derana_hms.exception.OperationException;
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

/**
 * Class to generate corresponding HTTP responses based on exception.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    ObjectMapper mapper=new ObjectMapper();
    HttpHeaders httpHeaders =  new HttpHeaders();
    {
        httpHeaders.set("Content-Type","application/json");
    }

    private ResponseEntity handleException(BaseException ex) throws IOException {
        return handleExceptionInternal(ex,mapper.readTree(ex.toString()).toString(),httpHeaders, ex.getStatus(),null);
    }
    
    @ExceptionHandler(InvalidDataException.class)
    public final ResponseEntity handleInvalidDataException(InvalidDataException ex) throws IOException {
        return handleException(ex);
    }
    
    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity handleDataNotFoundException(DataNotFoundException ex) throws IOException {
        return handleException(ex);
    }
    
    @ExceptionHandler(OperationException.class)
    public final ResponseEntity handleOperationException(OperationException ex) throws IOException {
        return handleException(ex);
    }

    @Override
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DataNotFoundException notFoundException = new DataNotFoundException(ex.getMessage());
        try {
            return handleException(notFoundException);
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
