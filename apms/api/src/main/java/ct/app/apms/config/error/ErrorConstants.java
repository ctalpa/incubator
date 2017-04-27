package ct.app.apms.config.error;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "Concurrency failure";
    public static final String ERR_ACCESS_DENIED = "Access denied";
    public static final String ERR_VALIDATION = "Data not valid";
    public static final String ERR_METHOD_NOT_SUPPORTED = "Method not supported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String ERR_NOT_FOUND = "Not found";
    public static final String ERR_CONSTRAINT_VIOLATION = "Constraint violation";
    public static final String ERR_DEPENDENCY_VIOLATION = "Some relationship exists";
    public static final String ERR_UNIQUENESS_VIOLATION = "Already exists";
    public static final String ERR_UNIQUENESS_VIOLATION_DESC = "The record cannot be inserted because it already exists";
    public static final String ERR_FILE_VALIDATION = "File not valid";
    public static final String ERR_IO = "I/O Internal error";
    public static final String ERR_ID_MAY_BE_NULL = "Item ID should be null";
    public static final String ERR_CONTENT_UNACCEPTABLE = "Unacceptable content type";
    public static final String ERR_PASSWORD_NULL = "The password cannot be null";

    private ErrorConstants() {
    }

}
