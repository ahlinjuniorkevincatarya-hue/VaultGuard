package banking.model;

import banking.model.abstracts.Transaction;
import java.time.LocalDateTime;

public class AlerteFraude {
    private String type;
    private String severite;  // FAIBLE, MOYENNE, ELEVEE
    private LocalDateTime dateTime;
    private Transaction transaction;
    private int score;
    private String raison;

    public AlerteFraude(String type, String severite, Transaction transaction, int score, String raison) {
        this.type = type;
        this.severite = severite;
        this.dateTime = LocalDateTime.now();
        this.transaction = transaction;
        this.score = score;
        this.raison = raison;
    }

    public String getType() { return type; }
    public String getSeverite() { return severite; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Transaction getTransaction() { return transaction; }
    public int getScore() { return score; }
    public String getRaison() { return raison; }

    public String afficher() {
        return String.format("[%s] %s | Score: %d | %s | %.2f MAD | %s",
            dateTime.toLocalDate(), severite, score, type, transaction.getMontant(), raison);
    }

    @Override
    public String toString() {
        return afficher();
    }
}