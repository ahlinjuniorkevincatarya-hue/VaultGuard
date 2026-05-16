package banking.interfaces;

import banking.model.AlerteFraude;
import banking.model.abstracts.Transaction;

public interface Analysable {
    AlerteFraude analyser(Transaction transaction);
}