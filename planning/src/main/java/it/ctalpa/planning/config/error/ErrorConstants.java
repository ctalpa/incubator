package it.ctalpa.planning.config.error;

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
    public static final String ERR_UNIQUENESS_VIOLATION_FLIGHT_TYPE_VALUE = "Combination of Flight Item Type and Flight Value already exists";
    public static final String ERR_UNIQUENESS_VIOLATION_DESC = "The record cannot be inserted because it already exists";
    public static final String ERR_FILE_VALIDATION = "File not valid";
    public static final String ERR_IO = "I/O Internal error";
    public static final String ERR_ID_MAY_BE_NULL = "Item ID should be null";
    public static final String ERR_CONTENT_UNACCEPTABLE = "Unacceptable content type";
    public static final String ERR_PASSWORD_NULL = "The password cannot be null";
    public static final String ERR_ADMINISTRATOR = "Cannot modify the Administrator role";
    public static final String ERR_CURRENCY_RATE = "Cannot find an applicable currency rate";
    public static final String ERR_OVERLAPPED_EXC_RATE = "An exchange rate in the period may be overlapped";
    public static final String ERR_START_END_DATE = "The start date is not before the end";
    public static final String ERR_AIRCRAFT_REGISTRATION_VALIDATION = "The aircraft registration number prefix is not valid";
    public static final String ERR_MISSING_BILLING_CENTER_OF_CURRENT_USER = "Current user account doesn't have a billing center assigned";
    public static final String ERR_BILLING_CENTER_HAS_NO_AERODROMES = "Billing center has no aerodromes associated with it";
    public static final String ERR_HQ_BILLING_CENTER_NOT_FOUND = "HQ Billing center is not defined";
    public static final String ERR_UNSPECIFIED_LOCATION_ID_DUPLICATED = "Unspecified Location text identifier or/and aerodrome identifier is duplicated";

    private ErrorConstants() {
    }

}
