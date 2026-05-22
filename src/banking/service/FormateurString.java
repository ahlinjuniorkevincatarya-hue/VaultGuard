package banking.service;

import java.util.regex.Pattern;

public class FormateurString {

    private static final Pattern PATTERN_EMAIL =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private FormateurString() {}

    public static String capitaliser(String texte) {
        if (texte == null || texte.isBlank()) return "";
        String[] parties = texte.trim().toLowerCase().split("-");
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < parties.length; i++) {
            if (parties[i].isEmpty()) continue;
            if (i > 0) res.append("-");
            res.append(Character.toUpperCase(parties[i].charAt(0)));
            res.append(parties[i].substring(1));
        }
        return res.toString();
    }

    public static String formaterMontant(double montant) {
        return String.format("%,.2f MAD", montant).replace(",", " ").replace(".", ",");
    }

    public static String formaterMontantAvecSigne(double montant) {
        return (montant >= 0 ? "+" : "") + formaterMontant(montant);
    }

    public static String genererNumeroCompte(String cin, int index) {
        String chiffres = cin.replaceAll("[^0-9]", "");
        String base = chiffres.length() >= 3 ? chiffres.substring(0, 3) : chiffres;
        return String.format("MA%s%04d", base, index + 1);
    }

    public static boolean estEmailValide(String email) {
        if (email == null || email.isBlank()) return false;
        return PATTERN_EMAIL.matcher(email.trim()).matches();
    }

    public static boolean estCinValide(String cin) {
        if (cin == null || cin.isBlank()) return false;
        return cin.trim().matches("^[A-Za-z]{1,2}\\d{5,6}$");
    }

    public static String masquerEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        int at = email.indexOf('@');
        if (at <= 1) return email;
        return email.charAt(0) + "***" + email.substring(at);
    }

    public static String tronquer(String texte, int max) {
        if (texte == null) return "";
        return texte.length() <= max ? texte : texte.substring(0, max - 3) + "...";
    }
}