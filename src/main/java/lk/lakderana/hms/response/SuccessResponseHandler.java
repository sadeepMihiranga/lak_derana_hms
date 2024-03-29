package lk.lakderana.hms.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Class to handle success response
 */
public class SuccessResponseHandler {

    /**
     * Generate response with data
     *
     * @param data response data
     *
     * @return Response for success operation.
     */
    public static ResponseEntity<SuccessResponse> generateResponse(Object data) {
        return new ResponseEntity<>(new SuccessResponse(data), HttpStatus.OK);
    }

    /**
     * Generate response with data and message
     *
     * @param data response data
     * @param message response message
     *
     * @return Response for success operation
     */
    public static ResponseEntity<SuccessResponse> generateResponse(Object data, String message) {
        return new ResponseEntity<>(new SuccessResponse(data,message), HttpStatus.OK);
    }

    /**
     * Generate response with data and message
     *
     * @param data response data
     * @param message response message
     *
     * @return Response for success operation
     */
    public static SuccessResponse generateResponse(Object data, String message, Boolean success, Integer code) {
        return new SuccessResponse(data, message, success, code);
    }
}
