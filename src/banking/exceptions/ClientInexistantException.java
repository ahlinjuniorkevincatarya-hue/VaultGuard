package banking.exceptions;

public class ClientInexistantException extends RuntimeException {
    public ClientInexistantException(String message) {
        super(message);
    }
}
