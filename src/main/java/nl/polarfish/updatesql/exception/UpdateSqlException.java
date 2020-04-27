package nl.polarfish.updatesql.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UpdateSqlException extends RuntimeException {

    public UpdateSqlException(String message, Throwable cause) {
        super(message, cause);
    }

}
