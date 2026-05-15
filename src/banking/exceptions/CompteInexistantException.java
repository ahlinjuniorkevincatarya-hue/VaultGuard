package banking.exceptions;

public class CompteInexistantException extends Exception {
    private final String numeroCompte;

    public CompteInexistantException(String numeroCompte) {
        super("Aucun compte trouvé avec le numéro : " + numeroCompte);
        this.numeroCompte = numeroCompte;
    }

    public String getNumeroCompte() { return numeroCompte; }
}