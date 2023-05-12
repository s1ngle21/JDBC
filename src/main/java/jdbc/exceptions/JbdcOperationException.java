package jdbc.exceptions;

public class JbdcOperationException extends RuntimeException {
    public JbdcOperationException(String message) {
        super(message);
    }

    public JbdcOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
