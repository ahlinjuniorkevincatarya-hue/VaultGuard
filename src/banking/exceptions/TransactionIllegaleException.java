package banking.exceptions;

public class TransactionIllegaleException extends Exception {
    public TransactionIllegaleException(String message) {
        super("Transaction illégale : " + message);
    }
}