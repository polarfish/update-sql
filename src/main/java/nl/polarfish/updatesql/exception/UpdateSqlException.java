package nl.polarfish.updatesql.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UpdateSqlException extends RuntimeException {

    @Getter
    private String reason;

    public UpdateSqlException(String message) {
        super(message);
    }

    public UpdateSqlException(String message, Throwable cause) {
        super(message, cause);
        this.reason = cause.getMessage();
    }

}
