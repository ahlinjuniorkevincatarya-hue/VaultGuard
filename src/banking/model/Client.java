package banking.model;

import banking.model.abstracts.Compte;
import banking.service.FormateurString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {

    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private List<Compte> comptes;
    private LocalDate dateNaissance;

    public Client(String cin, String nom, String prenom, String email, LocalDate dateNaissance) {
        if (cin == null || cin.isBlank())
            throw new IllegalArgumentException("Le CIN ne peut pas être vide.");
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        if (prenom == null || prenom.isBlank())
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("L'email est invalide.");

        this.cin           = cin.toUpperCase().trim();
        this.nom           = FormateurString.capitaliser(nom);
        this.prenom        = FormateurString.capitaliser(prenom);
        this.email         = email.trim().toLowerCase();
        this.dateNaissance = dateNaissance;
        this.comptes       = new ArrayList<>();
    }

    public void ajouterCompte(Compte compte) {
        if (compte == null) throw new IllegalArgumentException("Compte null.");
        comptes.add(compte);
    }

    public boolean supprimerCompte(String numeroCompte) {
        return comptes.removeIf(c -> c.getNumeroCompte().equals(numeroCompte));
    }

    public List<Compte> getComptes()       { return Collections.unmodifiableList(comptes); }
    public int          getNombreComptes() { return comptes.size(); }
    public String       getCin()           { return cin; }
    public String       getNom()           { return nom; }
    public String       getPrenom()        { return prenom; }
    public String       getEmail()         { return email; }
    public LocalDate    getDateNaissance() { return dateNaissance; }

    public void setNom(String nom)             { this.nom    = FormateurString.capitaliser(nom); }
    public void setPrenom(String prenom)       { this.prenom = FormateurString.capitaliser(prenom); }
    public void setEmail(String email)         { this.email  = email.trim().toLowerCase(); }
    public void setDateNaissance(LocalDate d)  { this.dateNaissance = d; }

    @Override
    public String toString() {
        return String.format("Client{CIN='%s', Nom='%s %s', Email='%s', Comptes=%d}",
            cin, prenom, nom, email, comptes.size());
    }

    public String toStringDetaille() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────────────────────────────────────┐\n");
        sb.append(String.format("│  CIN      : %-28s│%n", cin));
        sb.append(String.format("│  Nom      : %-28s│%n", prenom + " " + nom));
        sb.append(String.format("│  Email    : %-28s│%n", email));
        sb.append(String.format("│  Né(e) le : %-28s│%n", dateNaissance != null ? dateNaissance : "N/A"));
        sb.append(String.format("│  Comptes  : %-28s│%n", comptes.size()));
        sb.append("├─────────────────────────────────────────┤\n");
        if (comptes.isEmpty()) {
            sb.append("│  Aucun compte associé                   │\n");
        } else {
            for (Compte c : comptes)
                sb.append(String.format("│  → %-37s│%n",
                    c.getNumeroCompte() + "  " + FormateurString.formaterMontant(c.getSolde())));
        }
        sb.append("└─────────────────────────────────────────┘");
        return sb.toString();
    }
}