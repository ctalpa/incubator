package ct.app.apms.util.exception;

import ct.app.apms.config.error.ErrorDTO;

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

    private final String[] params;

    private String objectName;

    private String errorDescription = null;

    public CustomParametrizedException(String message, String... params) {
        super(message);
        this.params = params;
    }

    public CustomParametrizedException(String message, Throwable cause, String... params) {
        super(message, cause);
        this.params = params;
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

    public ParameterizedErrorDTO getParameterizedErrorDTO() {
        return new ParameterizedErrorDTO(getMessage(), params);
    }

    public String getDescription() {
        return this.errorDescription;
    }

    public ErrorDTO getErrorDTO() {
        final ErrorDTO errorDTO = new ErrorDTO(getMessage(), getDescription());
        if (params != null) {
            for (String param : params) {
                errorDTO.add(objectName, param, null);
            }
        }
        return errorDTO;
    }
}
