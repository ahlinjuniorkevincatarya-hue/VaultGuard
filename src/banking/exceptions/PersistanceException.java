package banking.exceptions;

public class PersistanceException extends RuntimeException {
    public PersistanceException(String message) {
        super(message);
    }
}
