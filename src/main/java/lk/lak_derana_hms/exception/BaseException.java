package lk.lak_derana_hms.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * Base class for Exceptions
 *
 * @author BuddhimaN
 * @since 11/19
 */
public class BaseException extends RuntimeException{


    private String message;
    private Integer code;
    @JsonIgnore
    private HttpStatus status;
    private String description;

    public BaseException() {
    }

    public BaseException(String message, Integer code, HttpStatus status, String description) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.description = description;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseException that = (BaseException) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(code, that.code) &&
                status == that.status &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, code, status, description);
    }

    @Override
    public String toString() {
        return "{" +
                    "\"error\":{" +
                        "\"message\":\"" + message  +"\""+
                        ",\"code\":" + code +
                        ",\"description\":\"" + description + "\""+
                    "}" +
                "}";
    }
}
