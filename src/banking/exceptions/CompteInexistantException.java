package banking.exceptions;

public class CompteInexistantException extends RuntimeException {
    public CompteInexistantException(String message) {
        super(message);
    }
}
