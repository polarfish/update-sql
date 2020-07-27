package nl.polarfish.updatesql.exception;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class UpdateSqlErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        Throwable error = getError(webRequest);
        if (error instanceof UpdateSqlException) {
            UpdateSqlException updateSqlException = (UpdateSqlException) error;

            Throwable cause = updateSqlException.getCause();
            if (cause != null) {
                errorAttributes.put("cause", Map.of(
                    "exception", cause.getClass().getName(),
                    "message", cause.getMessage()));
            }
        }

        return errorAttributes;
    }

}
