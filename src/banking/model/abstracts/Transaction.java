package banking.model.abstracts;

import banking.interfaces.Auditable;
import java.time.LocalDateTime;

public abstract class Transaction implements Auditable {

    private final String        id;
    private final double        montant;
    private final LocalDateTime date;
    private final String        description;

    public Transaction(String id, double montant, String description) {
        this.id          = id;
        this.montant     = montant;
        this.date        = LocalDateTime.now();
        this.description = description;
    }

    public String        getId()          { return id; }
    public double        getMontant()     { return montant; }
    public LocalDateTime getDate()        { return date; }
    public String        getDescription() { return description; }

    /** Chaque sous-classe dit quel type elle est */
    public abstract String getType();

    /** Pour l'affichage dans l'historique */
    public String afficher() {
        return String.format("[%s] %s | %.2f MAD | %s | %s",
            date.toLocalDate(), getType(), montant, description, id);
    }
}
