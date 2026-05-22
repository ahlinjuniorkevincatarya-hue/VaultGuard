package banking.model.accounts;

import banking.exceptions.SoldeInsuffisantException;
import banking.model.Client;
import banking.model.abstracts.Compte;

public class CompteCourant extends Compte {

    private final String numeroCompte;
    private       double solde;
    private final Client proprietaire;
    private final double decouvertAutorise;

    public CompteCourant(String numeroCompte, double soldeInitial,
                         Client proprietaire, double decouvertAutorise) {
        this.numeroCompte      = numeroCompte;
        this.solde             = soldeInitial;
        this.proprietaire      = proprietaire;
        this.decouvertAutorise = decouvertAutorise;
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
        if (solde - montant < -decouvertAutorise)
            throw new SoldeInsuffisantException("Solde insuffisant (découvert max : " + decouvertAutorise + ")");
        solde -= montant;
    }

    @Override
    public String toFichier() {
        return "COURANT;" + numeroCompte + ";" + solde + ";" + decouvertAutorise + ";" + proprietaire.getCin();
    }

    @Override
    public String afficher() {
        return String.format("Compte Courant [%s] | Solde : %.2f MAD | Découvert autorisé : %.2f MAD",
            numeroCompte, solde, decouvertAutorise);
    }
}