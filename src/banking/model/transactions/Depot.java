package banking.model.transactions;

import banking.model.abstracts.Transaction;

public class Depot extends Transaction {

    private final String numeroCompte;

    public Depot(String id, double montant, String numeroCompte) {
        super(id, montant, "Dépôt sur compte " + numeroCompte);
        this.numeroCompte = numeroCompte;
    }

    @Override
    public String getType() { return "DEPOT"; }

    public String getNumeroCompte() { return numeroCompte; }
}
