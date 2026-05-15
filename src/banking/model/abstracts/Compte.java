package banking.model.abstracts;

import banking.exceptions.SoldeInsuffisantException;
import banking.model.Client;

public abstract class Compte {

    public abstract String getNumeroCompte();
    public abstract double getSolde();
    public abstract Client getProprietaire();
    public abstract void deposer(double montant);
    public abstract void retirer(double montant) throws SoldeInsuffisantException;
    public abstract String toFichier();
    public abstract String afficher();
}