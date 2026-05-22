package banking;

import banking.model.Client;
import banking.model.abstracts.Compte;
import banking.model.accounts.CompteCourant;
import banking.model.accounts.CompteEpargne;
import banking.model.accounts.ComptePro;
import banking.model.abstracts.Transaction;
import banking.model.transactions.Depot;
import banking.model.transactions.Retrait;
import banking.model.transactions.Virement;
import banking.service.GestionnaireClients;
import banking.service.GestionnaireTransactions;
import banking.service.DetecteurFraude;
import banking.service.persistence.PersistanceService;
import banking.exceptions.*;

import banking.service.persistence.RapportDepenses;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale de l'application VaultGuard.
 * Gère le menu console et les interactions utilisateur.
 */
public class Main {

    // Scanner partagé pour toute l'application
    private static final Scanner scanner = new Scanner(System.in);

    // Services principaux
    private static final GestionnaireClients gestionnaireClients = new GestionnaireClients();
    private static final GestionnaireTransactions gestionnaireTx = new GestionnaireTransactions();
    private static final DetecteurFraude detecteurFraude = new DetecteurFraude();
    private static final PersistanceService persistanceService = new PersistanceService();

    public static void main(String[] args) {

        afficherBanniere();

        // Chargement des données au démarrage
        chargerDonnees();

        boolean running = true;

        while (running) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> menuClients();
                case 2 -> menuComptes();
                case 3 -> menuTransactions();
                case 4 -> menuFraude();
                case 5 -> menuRapports();
                case 0 -> {
                    sauvegarderDonnees();
                    System.out.println("\n  Au revoir !\n");
                    running = false;
                }
                default -> System.out.println("\n  [!] Choix invalide. Réessayez.\n");
            }
        }

        scanner.close();
    }

    // ─────────────────────────────────────────────
    // AFFICHAGE
    // ─────────────────────────────────────────────

    /**
     * Affiche la bannière de démarrage de l'application.
     */
    private static void afficherBanniere() {
        System.out.println("""
                
                ╔══════════════════════════════════════╗
                ║          V A U L T G U A R D          ║
                ║   Système bancaire intelligent        ║
                ╚══════════════════════════════════════╝
                """);
    }

    /**
     * Affiche le menu principal.
     */
    private static void afficherMenuPrincipal() {
        System.out.println("""
                ┌─────────────────────────────────────┐
                │           MENU PRINCIPAL            │
                ├─────────────────────────────────────┤
                │  1. Gestion des clients             │
                │  2. Gestion des comptes             │
                │  3. Transactions                    │
                │  4. Détection de fraude             │
                │  5. Rapports & Analyses             │
                │  0. Quitter                         │
                └─────────────────────────────────────┘
                """);
    }

    // ─────────────────────────────────────────────
    // MENU CLIENTS
    // ─────────────────────────────────────────────

    /**
     * Menu de gestion des clients.
     */
    private static void menuClients() {
        boolean retour = false;

        while (!retour) {
            System.out.println("""
                    
                    ── Gestion des clients ──
                      1. Créer un client
                      2. Modifier un client
                      3. Supprimer un client
                      4. Afficher un client
                      5. Afficher tous les clients
                      0. Retour
                    """);

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> creerClient();
                case 2 -> modifierClient();
                case 3 -> supprimerClient();
                case 4 -> afficherClient();
                case 5 -> gestionnaireClients.afficherTousLesClients();
                case 0 -> retour = true;
                default -> System.out.println("[!] Choix invalide.");
            }
        }
    }

    /**
     * Crée un nouveau client.
     */
    private static void creerClient() {
        System.out.println("\n-- Créer un client --");
        System.out.print("CIN (ex: AB123456) : ");
        String cin = scanner.nextLine().trim();
        System.out.print("Nom               : ");
        String nom = scanner.nextLine().trim();
        System.out.print("Prénom            : ");
        String prenom = scanner.nextLine().trim();
        System.out.print("Email             : ");
        String email = scanner.nextLine().trim();
        LocalDate naissance = lireDate("Date de naissance (YYYY-MM-DD) : ");

        try {
            Client client = new Client(cin, nom, prenom, email, naissance);
            gestionnaireClients.ajouterClient(client);
            System.out.println("\n  [✓] Client créé avec succès. CIN : " + cin);
        } catch (RuntimeException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Modifie les informations d'un client existant.
     */
    private static void modifierClient() {
        System.out.println("\n-- Modifier un client --");
        System.out.print("ID du client : ");
        String cin = scanner.nextLine().trim();

        try {
            Client client = gestionnaireClients.getClient(cin);
            System.out.println("Client trouvé : " + client.getPrenom() + " " + client.getNom());

            System.out.print("Nouveau nom (laisser vide pour garder)   : ");
            String nom = scanner.nextLine().trim();
            System.out.print("Nouveau prénom (laisser vide pour garder): ");
            String prenom = scanner.nextLine().trim();
            System.out.print("Nouvel email (laisser vide pour garder)  : ");
            String email = scanner.nextLine().trim();

            gestionnaireClients.modifierClient(cin,
                nom.isEmpty() ? null : nom,
                prenom.isEmpty() ? null : prenom,
                email.isEmpty() ? null : email);

            System.out.println("  [✓] Client mis à jour.");

        } catch (ClientInexistantException | IllegalArgumentException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Supprime un client du système.
     */
    private static void supprimerClient() {
        System.out.println("\n-- Supprimer un client --");
        System.out.print("ID du client : ");
        String id = scanner.nextLine().trim();

        try {
            gestionnaireClients.supprimerClient(id);
            System.out.println("\n  [✓] Client supprimé.");
        } catch (ClientInexistantException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Affiche les informations d'un client.
     */
    private static void afficherClient() {
        System.out.println("\n-- Afficher un client --");
        System.out.print("ID du client : ");
        String id = scanner.nextLine().trim();

        try {
            Client client = gestionnaireClients.getClient(id);
            System.out.println(client);
        } catch (ClientInexistantException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // MENU COMPTES
    // ─────────────────────────────────────────────

    /**
     * Menu de gestion des comptes bancaires.
     */
    private static void menuComptes() {
        boolean retour = false;

        while (!retour) {
            System.out.println("""
                    
                    ── Gestion des comptes ──
                      1. Ouvrir un compte
                      2. Fermer un compte
                      3. Afficher un compte
                      0. Retour
                    """);

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> ouvrirCompte();
                case 2 -> fermerCompte();
                case 3 -> afficherCompte();
                case 0 -> retour = true;
                default -> System.out.println("[!] Choix invalide.");
            }
        }
    }

    /**
     * Ouvre un nouveau compte pour un client.
     */
    private static void ouvrirCompte() {
        System.out.println("\n-- Ouvrir un compte --");
        System.out.print("CIN du client : ");
        String cin = scanner.nextLine().trim();

        try {
            Client client = gestionnaireClients.getClient(cin);

            System.out.println("""
                    Type de compte :
                      1. Courant
                      2. Épargne
                      3. Professionnel
                    """);
            int type = lireEntier("Votre choix : ");

            double soldeInit = lireDouble("Solde initial : ");
            String numero = gestionnaireClients.genererNumeroCompteUnique(cin);

            Compte compte;
            switch (type) {
                case 1 -> {
                    double decouvert = lireDouble("Découvert autorisé : ");
                    compte = new CompteCourant(numero, soldeInit, client, decouvert);
                }
                case 2 -> {
                    double taux = lireDouble("Taux intérêt (en %) : ");
                    compte = new CompteEpargne(numero, soldeInit, client, taux);
                }
                case 3 -> {
                    System.out.print("Secteur d'activité : ");
                    String secteur = scanner.nextLine().trim();
                    double plafond = lireDouble("Plafond transaction : ");
                    compte = new ComptePro(numero, soldeInit, client, secteur, plafond);
                }
                default -> throw new IllegalArgumentException("Type de compte invalide.");
            }

            gestionnaireClients.ajouterCompteAuClient(cin, compte);
            System.out.println("\n  [✓] Compte créé. Numéro : " + numero);

        } catch (ClientInexistantException | CompteDejaExistantException | IllegalArgumentException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Ferme un compte bancaire.
     */
    private static void fermerCompte() {
        System.out.println("\n-- Fermer un compte --");
        System.out.print("ID du client    : ");
        String idClient = scanner.nextLine().trim();
        System.out.print("Numéro de compte : ");
        String numeroCompte = scanner.nextLine().trim();

        try {
            gestionnaireClients.supprimerCompteClient(idClient, numeroCompte);
            System.out.println("\n  [✓] Compte fermé.");
        } catch (ClientInexistantException | CompteInexistantException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Affiche les informations d'un compte.
     */
    private static void afficherCompte() {
        System.out.println("\n-- Afficher un compte --");
        System.out.print("ID du client    : ");
        String idClient = scanner.nextLine().trim();
        System.out.print("Numéro de compte : ");
        String numeroCompte = scanner.nextLine().trim();

        try {
            Compte compte = gestionnaireClients.getCompte(numeroCompte);
            System.out.println(compte.afficher());
        } catch (CompteInexistantException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // MENU TRANSACTIONS
    // ─────────────────────────────────────────────

    /**
     * Menu des transactions bancaires.
     */
    private static void menuTransactions() {
        boolean retour = false;

        while (!retour) {
            System.out.println("""
                    
                    ── Transactions ──
                      1. Dépôt
                      2. Retrait
                      3. Virement
                      4. Historique des transactions
                      0. Retour
                    """);

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> effectuerDepot();
                case 2 -> effectuerRetrait();
                case 3 -> effectuerVirement();
                case 4 -> afficherHistorique();
                case 0 -> retour = true;
                default -> System.out.println("[!] Choix invalide.");
            }
        }
    }

    /**
     * Effectue un dépôt sur un compte.
     */
    private static void effectuerDepot() {
        System.out.println("\n-- Dépôt --");
        System.out.print("ID du client    : ");
        String idClient = scanner.nextLine().trim();
        System.out.print("Numéro de compte : ");
        String numeroCompte = scanner.nextLine().trim();
        double montant = lireDouble("Montant        : ");

        try {
            Compte compte = gestionnaireClients.getCompte(numeroCompte);
            gestionnaireTx.effectuerDepot(compte, montant);
        } catch (CompteInexistantException | MontantInvalideException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Effectue un retrait sur un compte.
     */
    private static void effectuerRetrait() {
        System.out.println("\n-- Retrait --");
        System.out.print("ID du client    : ");
        String idClient = scanner.nextLine().trim();
        System.out.print("Numéro de compte : ");
        String numeroCompte = scanner.nextLine().trim();
        double montant = lireDouble("Montant        : ");

        try {
            Compte compte = gestionnaireClients.getCompte(numeroCompte);
            gestionnaireTx.effectuerRetrait(compte, montant);
            // construire une transaction et l'analyser
            Retrait tx = new Retrait("TXN-" + System.currentTimeMillis(), montant, "Retrait");
            detecteurFraude.analyserEtAfficher(tx);
        } catch (SoldeInsuffisantException | MontantInvalideException | CompteInexistantException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Effectue un virement entre deux comptes.
     */
    private static void effectuerVirement() {
        System.out.println("\n-- Virement --");
        System.out.print("ID client source         : ");
        String idSource = scanner.nextLine().trim();
        System.out.print("Numéro compte source     : ");
        String numSource = scanner.nextLine().trim();
        System.out.print("Numéro compte destination : ");
        String numDest = scanner.nextLine().trim();
        double montant = lireDouble("Montant                  : ");

        try {
            Compte compteSource = gestionnaireClients.getCompte(numSource);
            Compte compteDest = gestionnaireClients.getCompte(numDest);
            gestionnaireTx.effectuerVirement(compteSource, compteDest, montant);
            Virement v = new Virement("TXN-" + System.currentTimeMillis(), montant, numSource, numDest);
            detecteurFraude.analyserEtAfficher(v);

            System.out.println("\n  [✓] Virement effectué.");
            System.out.println("      Solde source      : " + compteSource.getSolde() + " €");
            System.out.println("      Solde destination : " + compteDest.getSolde() + " €");

        } catch (SoldeInsuffisantException | MontantInvalideException | CompteInexistantException | TransactionIllegaleException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    /**
     * Affiche l'historique des transactions d'un compte.
     */
    private static void afficherHistorique() {
        System.out.println("\n-- Historique des transactions --");
        System.out.print("ID du client    : ");
        String idClient = scanner.nextLine().trim();
        System.out.print("Numéro de compte : ");
        String numeroCompte = scanner.nextLine().trim();

        try {
            gestionnaireTx.afficherHistorique(numeroCompte);
        } catch (Exception e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // MENU FRAUDE
    // ─────────────────────────────────────────────

    /**
     * Menu de gestion des alertes fraude.
     */
    private static void menuFraude() {
        boolean retour = false;

        while (!retour) {
            System.out.println("""
                    
                    ── Détection de fraude ──
                      1. Afficher toutes les alertes
                      2. Afficher les alertes par sévérité
                      0. Retour
                    """);

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> {
                    System.out.println("-- Analyser une transaction manuelle --");
                    double montant = lireDouble("Montant : ");
                    System.out.print("Description : ");
                    String desc = scanner.nextLine().trim();
                    Transaction tx = new Retrait("TXF-" + System.currentTimeMillis(), montant, desc);
                    detecteurFraude.analyserEtAfficher(tx);
                }
                case 0 -> retour = true;
                default -> System.out.println("[!] Choix invalide.");
            }
        }
    }

    // ─────────────────────────────────────────────
    // MENU RAPPORTS
    // ─────────────────────────────────────────────

    /**
     * Menu des rapports et analyses.
     */
    private static void menuRapports() {
        boolean retour = false;

        while (!retour) {
            System.out.println("""
                    
                    ── Rapports & Analyses ──
                      1. Générer un rapport mensuel
                      2. Classement des clients
                      0. Retour
                    """);

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> genererRapportMensuel();
                case 2 -> gestionnaireClients.afficherTousLesClients();
                case 0 -> retour = true;
                default -> System.out.println("[!] Choix invalide.");
            }
        }
    }

    /**
     * Génère un rapport mensuel pour un client.
     */
    private static void genererRapportMensuel() {
        System.out.println("\n-- Rapport mensuel --");
        System.out.print("ID du client : ");
        String idClient = scanner.nextLine().trim();
        int mois = lireEntier("Mois (1-12)  : ");
        int annee = lireEntier("Année        : ");

        try {
            Client client = gestionnaireClients.getClient(idClient);

            // Collecter toutes les transactions du client
            List<Transaction> all = new java.util.ArrayList<>();
            for (Compte c : client.getComptes()) {
                all.addAll(gestionnaireTx.getHistorique(c.getNumeroCompte()));
            }

            RapportDepenses r = new RapportDepenses(mois, annee, client, all);
            r.generer();
            r.afficher();

        } catch (ClientInexistantException e) {
            System.out.println("\n  [!] Erreur : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // PERSISTANCE
    // ─────────────────────────────────────────────

    /**
     * Charge les données depuis les fichiers CSV au démarrage.
     */
    private static void chargerDonnees() {
        try {
            HashMap<String, Client> loaded = persistanceService.chargerClients();
            for (Client c : loaded.values()) {
                try { gestionnaireClients.ajouterClient(c); } catch (Exception ignored) {}
            }
            System.out.println("  [✓] Données chargées (si disponibles).\n");
        } catch (PersistanceException e) {
            System.out.println("  [!] Impossible de charger les données : " + e.getMessage());
            System.out.println("      Démarrage avec une base vide.\n");
        }
    }

    /**
     * Sauvegarde les données dans les fichiers CSV à la fermeture.
     */
    private static void sauvegarderDonnees() {
        try {
            // Préparer une map CIN -> Client pour la persistance
            HashMap<String, Client> map = new HashMap<>();
            for (Client c : gestionnaireClients.getTousLesClients()) map.put(c.getCin(), c);
            persistanceService.sauvegarderClients(map);
            System.out.println("\n  [✓] Données sauvegardées.");
        } catch (PersistanceException e) {
            System.out.println("\n  [!] Erreur de sauvegarde : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // UTILITAIRES
    // ─────────────────────────────────────────────

    /**
     * Lit un entier saisi par l'utilisateur avec gestion d'erreur.
     * @param message Le message à afficher avant la saisie.
     * @return L'entier saisi.
     */
    private static int lireEntier(String message) {
        while (true) {
            System.out.print(message);
            try {
                int valeur = Integer.parseInt(scanner.nextLine().trim());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Veuillez saisir un nombre entier.");
            }
        }
    }

    /**
     * Lit un double saisi par l'utilisateur avec gestion d'erreur.
     * @param message Le message à afficher avant la saisie.
     * @return Le double saisi.
     */
    private static double lireDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double valeur = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (valeur <= 0) {
                    System.out.println("  [!] Le montant doit être positif.");
                    continue;
                }
                return valeur;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Veuillez saisir un montant valide.");
            }
        }
    }

    private static LocalDate lireDate(String message) {
        while (true) {
            System.out.print(message);
            try {
                String s = scanner.nextLine().trim();
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("  [!] Format invalide. Utilisez YYYY-MM-DD.");
            }
        }
    }
}