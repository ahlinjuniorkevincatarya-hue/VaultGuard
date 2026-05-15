package banking.exceptions;

public class ClientInexistantException extends Exception {
    private final String cin;

    public ClientInexistantException(String cin) {
        super("Aucun client trouvé avec le CIN : " + cin);
        this.cin = cin;
    }

    public String getCin() { return cin; }
}