package banking.service;

import banking.interfaces.Analysable;
import banking.model.AlerteFraude;
import banking.model.abstracts.Transaction;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetecteurFraude implements Analysable {

    // Seuils de détection
    private static final double SEUIL_MONTANT_ANORMAL = 10000.0;
    private static final int SEUIL_FREQUENCE_ELEVEE = 3;
    private static final long INTERVALLE_MINUTES_FREQUENCE = 5;
    private static final String[] COMMERCES_SUSPECTS = {"CASINO", "CRYPTO", "JEU", "BITCOIN", "PARIS"};

    private Map<String, List<Transaction>> historiqueParMerchant = new HashMap<>();

    @Override
    public AlerteFraude analyser(Transaction transaction) {
        int score = 0;
        String raison = "";
        String type = "";

        // Règle 1: Montant anormal
        if (transaction.getMontant() > SEUIL_MONTANT_ANORMAL) {
            score += 50;
            raison = "Montant anormal: " + transaction.getMontant() + " MAD";
            type = "MONTANT_ELEVE";
        }

        // Règle 2: Commerce suspect
        String description = transaction.getDescription().toUpperCase();
        for (String commerce : COMMERCES_SUSPECTS) {
            if (description.contains(commerce)) {
                score += 20;
                raison = (raison.isEmpty() ? "" : raison + " | ") + "Commerce suspect: " + commerce;
                type = "COMMERCE_SUSPECT";
                break;
            }
        }

        // Règle 3: Fréquence élevée
        String merchantKey = extractMerchantKey(transaction.getDescription());
        List<Transaction> recentes = historiqueParMerchant.computeIfAbsent(merchantKey, k -> new ArrayList<>());
        
        // Nettoyer les anciennes transactions (garder seulement les dernières minutes)
        LocalDateTime now = LocalDateTime.now();
        recentes.removeIf(t -> Duration.between(t.getDate(), now).toMinutes() > INTERVALLE_MINUTES_FREQUENCE);
        
        recentes.add(transaction);
        
        if (recentes.size() >= SEUIL_FREQUENCE_ELEVEE) {
            score += 30;
            raison = (raison.isEmpty() ? "" : raison + " | ") + "Fréquence élevée: " + recentes.size() + " transactions en " + INTERVALLE_MINUTES_FREQUENCE + " min";
            type = "FREQUENCE_ELEVEE";
        }

        // Déterminer sévérité
        String severite;
        if (score >= 70) {
            severite = "ELEVEE";
        } else if (score >= 40) {
            severite = "MOYENNE";
        } else if (score >= 20) {
            severite = "FAIBLE";
        } else {
            return null;
        }

        return new AlerteFraude(type, severite, transaction, score, raison);
    }

    private String extractMerchantKey(String description) {
        if (description == null || description.isEmpty()) {
            return "UNKNOWN";
        }
        return description.substring(0, Math.min(20, description.length())).toUpperCase();
    }

    public void analyserEtAfficher(Transaction transaction) {
        AlerteFraude alerte = analyser(transaction);
        if (alerte != null) {
            System.out.println("[ALERTE FRAUDE] " + alerte.afficher());
        }
    }
}