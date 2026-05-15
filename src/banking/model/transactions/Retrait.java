package banking.model.transactions;

import banking.model.abstracts.Transaction;

public class Retrait extends Transaction {

    private final String numeroCompte;

    public Retrait(String id, double montant, String numeroCompte) {
        super(id, montant, "Retrait du compte " + numeroCompte);
        this.numeroCompte = numeroCompte;
    }

    @Override
    public String getType() { return "RETRAIT"; }

    public String getNumeroCompte() { return numeroCompte; }
}