package banking.model;

import java.time.LocalDate;

public class Pret {

    private final String    id;
    private final Client    client;
    private final double    montantInitial;
    private final double    tauxAnnuel;
    private final int       dureeEnMois;
    private final LocalDate dateDebut;
    private       double    soldeRestant;
    private       int       mensualitesPaye;

    public Pret(String id, Client client, double montant,
                double tauxAnnuel, int dureeEnMois) {
        this.id              = id;
        this.client          = client;
        this.montantInitial  = montant;
        this.tauxAnnuel      = tauxAnnuel;
        this.dureeEnMois     = dureeEnMois;
        this.dateDebut       = LocalDate.now();
        this.soldeRestant    = montant;
        this.mensualitesPaye = 0;
    }

    public double calculerMensualite() {
        double tauxMensuel = tauxAnnuel / 12;
        if (tauxMensuel == 0) return montantInitial / dureeEnMois;
        return montantInitial * tauxMensuel
            / (1 - Math.pow(1 + tauxMensuel, -dureeEnMois));
    }

    public void rembourser() {
        if (estSolde()) {
            System.out.println(" Prêt déjà soldé.");
            return;
        }
        double mensualite = calculerMensualite();
        soldeRestant    -= mensualite;
        mensualitesPaye += 1;
        if (soldeRestant < 0) soldeRestant = 0;
        System.out.printf(" Remboursement : %.2f MAD | Reste : %.2f MAD%n",
            mensualite, soldeRestant);
    }

    public boolean estSolde()               { return soldeRestant <= 0; }
    public String  getId()                  { return id; }
    public Client  getClient()              { return client; }
    public double  getMontantInitial()      { return montantInitial; }
    public double  getSoldeRestant()        { return soldeRestant; }
    public int     getMensualitesPaye()     { return mensualitesPaye; }
    public int     getMensualitesRestantes(){ return dureeEnMois - mensualitesPaye; }

    public String afficher() {
        return String.format(
            "Prêt [%s] | %s %s | Initial : %.2f MAD | Reste : %.2f MAD | Mensualité : %.2f MAD | %d/%d",
            id, client.getPrenom(), client.getNom(),
            montantInitial, soldeRestant,
            calculerMensualite(), mensualitesPaye, dureeEnMois);
    }
}   