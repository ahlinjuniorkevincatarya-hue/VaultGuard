package banking.service.persistence;

import banking.interfaces.Exportable;
import banking.model.Client;
import banking.model.abstracts.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classe représentant un rapport de dépenses mensuel pour un client.
 */
public class RapportDepenses implements Exportable {
    private int mois;
    private int annee;
    private Client client;
    private Map<String, Double> depensesParCategorie;
    private double totalDepenses;
    private List<Transaction> transactions;

    /**
     * Constructeur pour initialiser le rapport.
     *
     * @param mois         Le mois du rapport
     * @param annee        L'année du rapport
     * @param client       Le client concerné
     * @param transactions La liste des transactions du client
     */
    public RapportDepenses(int mois, int annee, Client client, List<Transaction> transactions) {
        this.mois = mois;
        this.annee = annee;
        this.client = client;
        this.transactions = transactions;
        this.depensesParCategorie = new LinkedHashMap<>();
        this.totalDepenses = 0.0;

        // Initialisation avec l'ordre demandé
        depensesParCategorie.put("Nourriture", 0.0);
        depensesParCategorie.put("Transport", 0.0);
        depensesParCategorie.put("Loisirs", 0.0);
        depensesParCategorie.put("Factures", 0.0);
        depensesParCategorie.put("Santé", 0.0);
        depensesParCategorie.put("Autres", 0.0);
    }

    /**
     * Analyse l'historique des transactions et catégorise les dépenses.
     */
    public void generer() {
        totalDepenses = 0.0;
        for (String key : depensesParCategorie.keySet()) {
            depensesParCategorie.put(key, 0.0);
        }

        if (transactions == null) {
            return;
        }

        for (Transaction t : transactions) {
            // On s'assure que la transaction correspond au mois et à l'année
            if (t.getDate().getMonthValue() == mois && t.getDate().getYear() == annee) {
                String desc = t.getDescription() != null ? t.getDescription().toLowerCase() : "";
                String categorie = "Autres";

                if (desc.contains("uber") || desc.contains("taxi") || desc.contains("bus")) {
                    categorie = "Transport";
                } else if (desc.contains("restaurant") || desc.contains("food") || desc.contains("pizza") || desc.contains("cafe")) {
                    categorie = "Nourriture";
                } else if (desc.contains("netflix") || desc.contains("cinema") || desc.contains("game")) {
                    categorie = "Loisirs";
                } else if (desc.contains("electricite") || desc.contains("internet") || desc.contains("eau")) {
                    categorie = "Factures";
                } else if (desc.contains("pharmacie") || desc.contains("hopital")) {
                    categorie = "Santé";
                }

                double montant = t.getMontant();
                depensesParCategorie.put(categorie, depensesParCategorie.get(categorie) + montant);
                totalDepenses += montant;
            }
        }
    }

    /**
     * Affiche le rapport formaté dans la console.
     */
    public void afficher() {
        System.out.println("===== RAPPORT MENSUEL =====");
        System.out.printf("Client: %s %s%n", client.getPrenom(), client.getNom());
        System.out.printf("Mois: %d/%d%n%n", mois, annee);

        for (Map.Entry<String, Double> entry : depensesParCategorie.entrySet()) {
            if (entry.getValue() > 0) {
                String formatMontant = (entry.getValue() % 1 == 0) ? "%.0f" : "%.2f";
                System.out.printf(Locale.US, "%s : " + formatMontant + " DH%n", entry.getKey(), entry.getValue());
            }
        }

        System.out.println();
        String formatTotal = (totalDepenses % 1 == 0) ? "%.0f" : "%.2f";
        System.out.printf(Locale.US, "Total: " + formatTotal + " DH%n", totalDepenses);
    }

    /**
     * Exporte le rapport dans un fichier texte.
     */
    public void exporter() {
        String filename = String.format("rapport_depenses_%s_%d_%d.txt", client.getCin(), mois, annee);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("===== RAPPORT MENSUEL =====");
            writer.newLine();
            writer.write(String.format("Client: %s %s", client.getPrenom(), client.getNom()));
            writer.newLine();
            writer.write(String.format("Mois: %d/%d", mois, annee));
            writer.newLine();
            writer.newLine();

            for (Map.Entry<String, Double> entry : depensesParCategorie.entrySet()) {
                if (entry.getValue() > 0) {
                    String formatMontant = (entry.getValue() % 1 == 0) ? "%.0f" : "%.2f";
                    writer.write(String.format(Locale.US, "%s : " + formatMontant + " DH", entry.getKey(), entry.getValue()));
                    writer.newLine();
                }
            }

            writer.newLine();
            String formatTotal = (totalDepenses % 1 == 0) ? "%.0f" : "%.2f";
            writer.write(String.format(Locale.US, "Total: " + formatTotal + " DH", totalDepenses));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exportation du rapport : " + e.getMessage());
        }
    }
}
