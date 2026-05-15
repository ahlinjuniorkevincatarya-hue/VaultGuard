package banking.service;

import banking.exceptions.ClientInexistantException;
import banking.exceptions.CompteDejaExistantException;
import banking.exceptions.CompteInexistantException;
import banking.model.Client;
import banking.model.abstracts.Compte;

import java.util.*;
import java.util.stream.Collectors;

public class GestionnaireClients {

    private final Map<String, Client> clients;
    private final List<String>        ordreCreation;
    private       int                 compteurComptes;

    public GestionnaireClients() {
        this.clients         = new HashMap<>();
        this.ordreCreation   = new ArrayList<>();
        this.compteurComptes = 0;
    }

    public void ajouterClient(Client client) {
        if (client == null) throw new IllegalArgumentException("Client null.");
        if (clients.containsKey(client.getCin()))
            throw new IllegalStateException("CIN déjà existant : " + client.getCin());
        clients.put(client.getCin(), client);
        ordreCreation.add(client.getCin());
        System.out.println("✔ Client enregistré : " + client.getPrenom() + " " + client.getNom());
    }

    public Client getClient(String cin) throws ClientInexistantException {
        if (cin == null || cin.isBlank()) throw new IllegalArgumentException("CIN vide.");
        Client c = clients.get(cin.toUpperCase().trim());
        if (c == null) throw new ClientInexistantException(cin);
        return c;
    }

    public void modifierClient(String cin, String nom, String prenom, String email)
            throws ClientInexistantException {
        Client c = getClient(cin);
        if (nom    != null && !nom.isBlank())    c.setNom(nom);
        if (prenom != null && !prenom.isBlank()) c.setPrenom(prenom);
        if (email  != null && !email.isBlank()) {
            if (!FormateurString.estEmailValide(email))
                throw new IllegalArgumentException("Email invalide : " + email);
            c.setEmail(email);
        }
        System.out.println("✔ Client " + cin + " mis à jour.");
    }

    public void supprimerClient(String cin) throws ClientInexistantException {
        getClient(cin);
        clients.remove(cin.toUpperCase().trim());
        ordreCreation.remove(cin.toUpperCase().trim());
        System.out.println("✔ Client " + cin + " supprimé.");
    }

    public void afficherTousLesClients() {
        if (clients.isEmpty()) { System.out.println("Aucun client."); return; }
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  LISTE DES CLIENTS (" + clients.size() + ")");
        System.out.println("═══════════════════════════════════════════");
        for (String cin : ordreCreation) {
            Client c = clients.get(cin);
            System.out.printf("  %-12s │ %-22s │ %d compte(s)%n",
                c.getCin(),
                FormateurString.tronquer(c.getPrenom() + " " + c.getNom(), 22),
                c.getNombreComptes());
        }
        System.out.println("═══════════════════════════════════════════");
    }

    public List<Client> getTousLesClients() {
        List<Client> liste = new ArrayList<>();
        for (String cin : ordreCreation) liste.add(clients.get(cin));
        return Collections.unmodifiableList(liste);
    }

    public List<Client> rechercherParNom(String terme) {
        if (terme == null || terme.isBlank()) return getTousLesClients();
        String t = terme.toLowerCase().trim();
        return clients.values().stream()
            .filter(c -> c.getNom().toLowerCase().contains(t)
                      || c.getPrenom().toLowerCase().contains(t))
            .collect(Collectors.toList());
    }

    public void ajouterCompteAuClient(String cin, Compte compte)
            throws ClientInexistantException, CompteDejaExistantException {
        Client client = getClient(cin);
        if (compteExiste(compte.getNumeroCompte()))
            throw new CompteDejaExistantException(compte.getNumeroCompte());
        client.ajouterCompte(compte);
        compteurComptes++;
        System.out.println("✔ Compte " + compte.getNumeroCompte() + " ajouté à " + cin);
    }

    public void supprimerCompteClient(String cin, String numeroCompte)
            throws ClientInexistantException, CompteInexistantException {
        Client client = getClient(cin);
        if (!client.supprimerCompte(numeroCompte))
            throw new CompteInexistantException(numeroCompte);
        System.out.println("✔ Compte " + numeroCompte + " supprimé.");
    }

    public Compte getCompte(String numeroCompte) throws CompteInexistantException {
        for (Client c : clients.values())
            for (Compte compte : c.getComptes())
                if (compte.getNumeroCompte().equals(numeroCompte)) return compte;
        throw new CompteInexistantException(numeroCompte);
    }

    public boolean compteExiste(String numeroCompte) {
        for (Client c : clients.values())
            for (Compte compte : c.getComptes())
                if (compte.getNumeroCompte().equals(numeroCompte)) return true;
        return false;
    }

    public String genererNumeroCompteUnique(String cin) {
        String numero;
        int t = compteurComptes;
        do { numero = FormateurString.genererNumeroCompte(cin, t++); }
        while (compteExiste(numero));
        return numero;
    }

    public int getNombreClients()      { return clients.size(); }
    public int getNombreTotalComptes() {
        return clients.values().stream().mapToInt(Client::getNombreComptes).sum();
    }
}