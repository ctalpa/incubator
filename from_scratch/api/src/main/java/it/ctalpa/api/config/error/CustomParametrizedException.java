package it.ctalpa.planning.config.error;

import java.util.List;

/**
 * Custom, parameterized exception, which can be translated on the client side.
 * For example:
 *
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 *
 * Can be translated with:
 *
 * <pre>
 * "error.myCustomError" :  "The server says {{params[0]}} to {{params[1]}}"
 * </pre>
 */
public class CustomParametrizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String[] params;

    private String objectName;

    private String errorDescription = null;

    private List<FieldErrorDTO> fields = null;

    public CustomParametrizedException(String message, String... params) {
        super(message);
        this.params = params;
    }

    public CustomParametrizedException(ErrorDTO errorDTO) {
        super(errorDTO.getError());
        this.errorDescription = errorDTO.getErrorDescription();
        this.fields = errorDTO.getFieldErrors();
    }

    public CustomParametrizedException(String message, Throwable cause, String... params) {
        super(message, cause);
        this.params = params;
        this.errorDescription = cause.getMessage();
    }

    public CustomParametrizedException(String message, Class<?> origin, String... params) {
        super(message);
        this.objectName = origin.getSimpleName();
        this.params = params;
    }

    public CustomParametrizedException(String message, Throwable cause, Class<?> origin, String... params) {
        super(message, cause);
        this.objectName = origin.getSimpleName();
        this.params = params;
        this.errorDescription = cause.getMessage();
    }

    public CustomParametrizedException(String message, String descrtiption, Class<?> origin, String... params) {
        super(message);
        this.objectName = origin.getSimpleName();
        this.params = params;
        this.errorDescription = descrtiption;
    }


    public String getDescription() {
        return this.errorDescription;
    }

    public ErrorDTO getErrorDTO() {
        final ErrorDTO errorDTO = new ErrorDTO(getMessage(), getDescription());
        if (fields != null) {
            for (FieldErrorDTO field : fields) {
                errorDTO.add(field);
            }
        }
        if (params != null) {
            for (String param : params) {
                errorDTO.add(objectName, param, null);
            }
        }
        return errorDTO;
    }
}
