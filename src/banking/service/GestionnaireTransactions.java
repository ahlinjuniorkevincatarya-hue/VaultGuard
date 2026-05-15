package banking.service;

import banking.exceptions.MontantInvalideException;
import banking.exceptions.SoldeInsuffisantException;
import banking.exceptions.TransactionIllegaleException;
import banking.model.Client;
import banking.model.Pret;
import banking.model.abstracts.Compte;
import banking.model.abstracts.Transaction;
import banking.model.transactions.Depot;
import banking.model.transactions.Retrait;
import banking.model.transactions.Virement;

import java.util.*;

public class GestionnaireTransactions {

    private final Map<String, List<Transaction>> historique;
    private final List<Pret>                     prets;
    private       int                            compteurTx;

    public GestionnaireTransactions() {
        this.historique  = new HashMap<>();
        this.prets       = new ArrayList<>();
        this.compteurTx  = 0;
    }

    private String genererIdTransaction() {
        return "TX" + String.format("%05d", ++compteurTx);
    }

    private void enregistrer(String numeroCompte, Transaction tx) {
        historique.computeIfAbsent(numeroCompte, k -> new ArrayList<>()).add(tx);
    }

    // ── DÉPÔT ────────────────────────────────────────────────────
    public void effectuerDepot(Compte compte, double montant) {
        if (montant <= 0)
            throw new MontantInvalideException("Montant invalide : " + montant);
        compte.deposer(montant);
        Depot tx = new Depot(genererIdTransaction(), montant, compte.getNumeroCompte());
        enregistrer(compte.getNumeroCompte(), tx);
        System.out.printf("✔ Dépôt de %.2f MAD sur %s%n", montant, compte.getNumeroCompte());
    }

    // ── RETRAIT ───────────────────────────────────────────────────
    public void effectuerRetrait(Compte compte, double montant)
            throws SoldeInsuffisantException {
        if (montant <= 0)
            throw new MontantInvalideException("Montant invalide : " + montant);
        compte.retirer(montant);
        Retrait tx = new Retrait(genererIdTransaction(), montant, compte.getNumeroCompte());
        enregistrer(compte.getNumeroCompte(), tx);
        System.out.printf("✔ Retrait de %.2f MAD depuis %s%n", montant, compte.getNumeroCompte());
    }

    // ── VIREMENT ──────────────────────────────────────────────────
    public void effectuerVirement(Compte source, Compte destination, double montant)
            throws SoldeInsuffisantException, TransactionIllegaleException {
        if (montant <= 0)
            throw new MontantInvalideException("Montant invalide : " + montant);
        if (source.getNumeroCompte().equals(destination.getNumeroCompte()))
            throw new TransactionIllegaleException("Impossible de virer vers le même compte.");
        source.retirer(montant);
        destination.deposer(montant);
        String id = genererIdTransaction();
        Virement tx = new Virement(id, montant,
            source.getNumeroCompte(), destination.getNumeroCompte());
        enregistrer(source.getNumeroCompte(), tx);
        enregistrer(destination.getNumeroCompte(), tx);
        System.out.printf("✔ Virement %.2f MAD : %s → %s%n",
            montant, source.getNumeroCompte(), destination.getNumeroCompte());
    }

    // ── HISTORIQUE ────────────────────────────────────────────────
    public List<Transaction> getHistorique(String numeroCompte) {
        return Collections.unmodifiableList(
            historique.getOrDefault(numeroCompte, new ArrayList<>()));
    }

    public void afficherHistorique(String numeroCompte) {
        List<Transaction> liste = getHistorique(numeroCompte);
        System.out.println("\n══ Historique : " + numeroCompte + " ══");
        if (liste.isEmpty()) {
            System.out.println("  Aucune transaction.");
        } else {
            for (Transaction tx : liste)
                System.out.println("  " + tx.afficher());
        }
        System.out.println("═══════════════════════════════════════");
    }

    // ── PRÊTS ─────────────────────────────────────────────────────
    public Pret creerPret(Client client, double montant,
                          double tauxAnnuel, int dureeEnMois) {
        if (montant <= 0)
            throw new MontantInvalideException("Montant invalide : " + montant);
        String id = "PR" + String.format("%04d", prets.size() + 1);
        Pret pret = new Pret(id, client, montant, tauxAnnuel, dureeEnMois);
        prets.add(pret);
        System.out.printf("✔ Prêt %s : %.2f MAD sur %d mois à %.1f%%%n",
            id, montant, dureeEnMois, tauxAnnuel * 100);
        return pret;
    }

    public List<Pret> getPretsClient(String cin) {
        List<Pret> result = new ArrayList<>();
        for (Pret p : prets)
            if (p.getClient().getCin().equals(cin)) result.add(p);
        return result;
    }
}