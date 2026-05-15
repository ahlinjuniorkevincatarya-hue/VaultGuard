package banking.model.transactions;

import banking.model.abstracts.Transaction;

public class Virement extends Transaction {

    private final String compteSource;
    private final String compteDestination;

    public Virement(String id, double montant,
                    String compteSource, String compteDestination) {
        super(id, montant,
            "Virement de " + compteSource + " → " + compteDestination);
        this.compteSource      = compteSource;
        this.compteDestination = compteDestination;
    }

    @Override
    public String getType() { return "VIREMENT"; }

    public String getCompteSource()      { return compteSource; }
    public String getCompteDestination() { return compteDestination; }
}
