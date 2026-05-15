package banking.model.accounts;

import banking.exceptions.SoldeInsuffisantException;
import banking.model.Client;
import banking.model.abstracts.Compte;

public class ComptePro extends Compte {

    private final String numeroCompte;
    private       double solde;
    private final Client proprietaire;
    private final String secteurActivite;
    private final double plafondTransaction;

    public ComptePro(String numeroCompte, double soldeInitial, Client proprietaire,
                     String secteurActivite, double plafondTransaction) {
        this.numeroCompte       = numeroCompte;
        this.solde              = soldeInitial;
        this.proprietaire       = proprietaire;
        this.secteurActivite    = secteurActivite;
        this.plafondTransaction = plafondTransaction;
    }

    @Override public String getNumeroCompte() { return numeroCompte; }
    @Override public double getSolde()         { return solde; }
    @Override public Client getProprietaire()  { return proprietaire; }

    @Override
    public void deposer(double montant) {
        if (montant <= 0) throw new IllegalArgumentException("Montant invalide.");
        if (montant > plafondTransaction)
            throw new IllegalArgumentException("Dépôt dépasse le plafond : " + plafondTransaction);
        solde += montant;
    }

    @Override
    public void retirer(double montant) throws SoldeInsuffisantException {
        if (montant <= 0) throw new IllegalArgumentException("Montant invalide.");
        if (montant > plafondTransaction)
            throw new IllegalArgumentException("Retrait dépasse le plafond : " + plafondTransaction);
        if (montant > solde)
            throw new SoldeInsuffisantException("Solde insuffisant : " + solde + " MAD disponible.");
        solde -= montant;
    }

    @Override
    public String toFichier() {
        return "PRO;" + numeroCompte + ";" + solde + ";" + secteurActivite + ";" + plafondTransaction + ";" + proprietaire.getCin();
    }

    @Override
    public String afficher() {
        return String.format("Compte Pro [%s] | Solde : %.2f MAD | Secteur : %s | Plafond : %.2f MAD",
            numeroCompte, solde, secteurActivite, plafondTransaction);
    }
}