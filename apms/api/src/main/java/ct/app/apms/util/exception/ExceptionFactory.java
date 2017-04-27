package ct.app.apms.util.exception;

import ct.app.apms.config.error.ErrorConstants;

public class ExceptionFactory {

    private ExceptionFactory() {
    }

    public static CustomParametrizedException getDepencencyViolationException(final Class<?> origin,
            final Class<?> reference) {
        return new CustomParametrizedException(ErrorConstants.ERR_DEPENDENCY_VIOLATION, origin,
                reference.getSimpleName());
    }

    public static CustomParametrizedException getInvalidFileException(final Class<?> origin,
            final String... parameters) {
        return new CustomParametrizedException(ErrorConstants.ERR_FILE_VALIDATION, origin, parameters);
    }

    public static CustomParametrizedException getInvalidFileException(final Class<?> origin, final Throwable cause,
            final String... parameters) {
        return new CustomParametrizedException(ErrorConstants.ERR_FILE_VALIDATION, cause, origin, parameters);
    }

    public static CustomParametrizedException getInvalidDataException(final Class<?> origin, final Throwable cause,
            final String... parameters) {
        return new CustomParametrizedException(ErrorConstants.ERR_VALIDATION, cause, origin, parameters);
    }

    public static CustomParametrizedException getNullablePasswordException(final Class<?> origin) {
        return new CustomParametrizedException(ErrorConstants.ERR_PASSWORD_NULL, origin);
    }
}
