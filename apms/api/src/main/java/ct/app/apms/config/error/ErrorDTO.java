package ct.app.apms.config.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String error;
    private final String errorDescription;

    private List<FieldErrorDTO> fieldErrors;

    public ErrorDTO(String error) {
        this(error, null);
    }

    public ErrorDTO(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public ErrorDTO(String error, String errorDescription, List<FieldErrorDTO> fieldErrors) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.fieldErrors = fieldErrors;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorDTO(objectName, field, message));
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
