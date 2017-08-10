package it.ctalpa.planning.config.error;

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

    public void add(FieldErrorDTO field) {
        if (field != null) {
            if (fieldErrors == null) {
                fieldErrors = new ArrayList<>();
            }
            fieldErrors.add(field);
        }
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

    public String toString() {
        final StringBuilder messageError = new StringBuilder();
        if (error != null) {
            messageError.append(error);
            if (fieldErrors.size() > 0) {
                messageError.append(" > ");
                for (final FieldErrorDTO fieldErrorDTO : fieldErrors) {
                    messageError.append(fieldErrorDTO.getObjectName())
                        .append('.')
                        .append(fieldErrorDTO.getField())
                        .append(": ")
                        .append(fieldErrorDTO.getMessage())
                        .append("; ");
                }
            }
        } else {
            messageError.append("no errors");
        }
        return messageError.toString();
    }
}
