package ct.app.apms.util.exception;

/**
 * This exception is thrown when an operation threatens the system integrity or configuration.
 */
public class OperationNotAllowedException extends RuntimeException {
    /**
     * Constructs a new instance of OperationNotAllowedException.
     * All fields default to null.
     */
    public OperationNotAllowedException() {
        super();
    }

    /**
     * Constructs a new instance of OperationNotAllowedException using an
     * explanation. All other fields default to null.
     *
     * @param  explanation     Possibly null additional detail about this exception
     * @see Throwable#getMessage
     */
    public OperationNotAllowedException(String explanation) {
        super(explanation);
    }

}
