package banking;

import banking.exceptions.*;
import banking.model.Client;
import banking.model.abstracts.Compte;
import banking.service.FormateurString;
import banking.service.GestionnaireClients;
import java.time.LocalDate;
import banking.service.persistence.RapportDepenses;
import banking.service.persistence.PersistanceService;
import banking.model.abstracts.Transaction;
import banking.model.transactions.Retrait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    static GestionnaireClients gestionnaire = new GestionnaireClients();
    static final String SEP = "\n" + "─".repeat(50);

    public static void main(String[] args) {
        testS1();
        testE2();
        testE5();
        testE4();
        testModification();
        testRecherche();
        testFormateurString();
        testPart5();

        System.out.println(SEP);
        System.out.println("✅ Tous les tests Membre 2 sont terminés.");
    }

    static void testS1() {
        System.out.println(SEP);
        System.out.println("TEST S1 — Créer un client");
        try {
            Client alice = new Client("AB123456", "BENALI", "alice",
                "alice@email.com", LocalDate.of(1995, 3, 14));
            gestionnaire.ajouterClient(alice);
            System.out.println(alice.toStringDetaille());
            System.out.println(" S1 réussi");
        } catch (Exception e) {
            System.err.println(" S1 échoué : " + e.getMessage());
        }
    }

    static void testE2() {
        System.out.println(SEP);
        System.out.println("TEST E2 — Client inexistant");
        try {
            gestionnaire.getClient("CINQUINEXISTE");
            System.err.println(" E2 échoué : exception non levée !");
        } catch (ClientInexistantException e) {
            System.out.println(" E2 réussi : " + e.getMessage());
        }
    }

    static void testE5() {
        System.out.println(SEP);
        System.out.println("TEST E5 — Compte déjà existant");
        try {
            Client alice = gestionnaire.getClient("AB123456");
            // Simuler un compte déjà existant
            gestionnaire.ajouterCompteAuClient("AB123456", new Compte() {
                public String getNumeroCompte() { return "MA1230001"; }
                public double getSolde()         { return 1000; }
                public Client getProprietaire()  { return alice; }
                public void deposer(double m)    {}
                public void retirer(double m) throws SoldeInsuffisantException {}
                public String toFichier()        { return ""; }
                public String afficher()         { return ""; }
            });
            // Tenter de l'ajouter une deuxième fois
            gestionnaire.ajouterCompteAuClient("AB123456", new Compte() {
                public String getNumeroCompte() { return "MA1230001"; }
                public double getSolde()         { return 500; }
                public Client getProprietaire()  { return alice; }
                public void deposer(double m)    {}
                public void retirer(double m) throws SoldeInsuffisantException {}
                public String toFichier()        { return ""; }
                public String afficher()         { return ""; }
            });
            System.err.println(" E5 échoué : exception non levée !");
        } catch (CompteDejaExistantException e) {
            System.out.println(" E5 réussi : " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" E5 échoué : " + e.getMessage());
        }
    }

    static void testE4() {
        System.out.println(SEP);
        System.out.println("TEST E4 — Compte inexistant");
        try {
            gestionnaire.getCompte("COMPTE_FANTOME");
            System.err.println(" E4 échoué : exception non levée !");
        } catch (CompteInexistantException e) {
            System.out.println(" E4 réussi : " + e.getMessage());
        }
    }

    static void testModification() {
        System.out.println(SEP);
        System.out.println("TEST — Modifier un client");
        try {
            gestionnaire.modifierClient("AB123456", "Alaoui", null, "alice.new@email.com");
            Client alice = gestionnaire.getClient("AB123456");
            System.out.println("   Nouveau nom  : " + alice.getNom());
            System.out.println("   Nouvel email : " + alice.getEmail());
            System.out.println(" Modification réussie");
        } catch (ClientInexistantException e) {
            System.err.println(" Modification échouée : " + e.getMessage());
        }
    }

    static void testRecherche() {
        System.out.println(SEP);
        System.out.println("TEST — Recherche + affichage");
        try {
            Client bob = new Client("CD789012", "Alami", "Mohammed",
                "m.alami@email.com", LocalDate.of(1988, 7, 22));
            gestionnaire.ajouterClient(bob);
            gestionnaire.afficherTousLesClients();
            var res = gestionnaire.rechercherParNom("ala");
            System.out.println("  Recherche 'ala' → " + res.size() + " résultat(s)");
            res.forEach(c -> System.out.println("    - " + c.getPrenom() + " " + c.getNom()));
            System.out.println(" Recherche réussie");
        } catch (Exception e) {
            System.err.println(" Recherche échouée : " + e.getMessage());
        }
    }

    static void testFormateurString() {
        System.out.println(SEP);
        System.out.println("TEST — FormateurString");
        System.out.println("  capitaliser('jean-pierre') → " + FormateurString.capitaliser("jean-pierre"));
        System.out.println("  formaterMontant(12500.5)   → " + FormateurString.formaterMontant(12500.5));
        System.out.println("  estEmailValide(ok)         → " + FormateurString.estEmailValide("alice@gmail.com"));
        System.out.println("  estEmailValide(ko)         → " + FormateurString.estEmailValide("pas-un-email"));
        System.out.println("  estCinValide('AB123456')   → " + FormateurString.estCinValide("AB123456"));
        System.out.println("  masquerEmail               → " + FormateurString.masquerEmail("alice@gmail.com"));
        System.out.println(" FormateurString OK");
    }
    static void testPart5() {

    System.out.println(SEP);
    System.out.println("TEST PARTIE 5");

    try {

        Client c = new Client(
                "AA123",
                "Smith",
                "John",
                "john@gmail.com",
                LocalDate.of(2000,1,1)
        );

        List<Transaction> list =
                new ArrayList<>();

        list.add(
                new Retrait(
                        "T1",
                        200,
                        "restaurant"
                )
        );

        list.add(
                new Retrait(
                        "T2",
                        50,
                        "uber"
                )
        );

        RapportDepenses r =
                new RapportDepenses(
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear(),
                        c,
                        list
                );

        r.generer();

        r.afficher();

        r.exporter();

        HashMap<String,Client> clients =
                new HashMap<>();

        clients.put(
                c.getCin(),
                c
        );

        PersistanceService p =
                new PersistanceService();

        p.sauvegarderClients(clients);

        p.sauvegarderTransactions(list);

        System.out.println("Partie 5 OK");

    } catch(Exception e){

        System.out.println(
                "Erreur Partie 5 : "
                        + e.getMessage()
        );

        e.printStackTrace();
    }
}
}