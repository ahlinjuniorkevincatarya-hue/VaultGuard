package banking.exceptions;

public class CompteDejaExistantException extends Exception {
    private final String numeroCompte;

    public CompteDejaExistantException(String numeroCompte) {
        super("Un compte avec le numéro " + numeroCompte + " existe déjà.");
        this.numeroCompte = numeroCompte;
    }

    public String getNumeroCompte() { return numeroCompte; }
}