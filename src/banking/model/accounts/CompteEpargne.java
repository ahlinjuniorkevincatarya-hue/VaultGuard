package banking.model.accounts;

import banking.exceptions.SoldeInsuffisantException;
import banking.model.Client;
import banking.model.abstracts.Compte;

public class CompteEpargne extends Compte {

    private final String numeroCompte;
    private       double solde;
    private final Client proprietaire;
    private final double tauxInteret;

    public CompteEpargne(String numeroCompte, double soldeInitial,
                         Client proprietaire, double tauxInteret) {
        this.numeroCompte = numeroCompte;
        this.solde        = soldeInitial;
        this.proprietaire = proprietaire;
        this.tauxInteret  = tauxInteret;
    }

    @Override public String getNumeroCompte() { return numeroCompte; }
    @Override public double getSolde()         { return solde; }
    @Override public Client getProprietaire()  { return proprietaire; }

    @Override
    public void deposer(double montant) {
        if (montant <= 0) throw new IllegalArgumentException("Montant invalide.");
        solde += montant;
    }

    @Override
    public void retirer(double montant) throws SoldeInsuffisantException {
        if (montant <= 0) throw new IllegalArgumentException("Montant invalide.");
        if (montant > solde)
            throw new SoldeInsuffisantException("Solde insuffisant : " + solde + " MAD disponible.");
        solde -= montant;
    }

    public void appliquerInteret() {
        solde += solde * tauxInteret / 100;
    }

    @Override
    public String toFichier() {
        return "EPARGNE;" + numeroCompte + ";" + solde + ";" + tauxInteret + ";" + proprietaire.getCin();
    }

    @Override
    public String afficher() {
        return String.format("Compte Épargne [%s] | Solde : %.2f MAD | Taux : %.2f%%",
            numeroCompte, solde, tauxInteret);
    }
}