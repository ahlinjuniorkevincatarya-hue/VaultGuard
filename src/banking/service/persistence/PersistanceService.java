package banking.service.persistence;

import banking.model.Client;
import banking.model.abstracts.Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersistanceService {

    private static final String FICHIER_CLIENTS = "clients.csv";
    private static final String FICHIER_TRANSACTIONS = "transactions.csv";

    public void sauvegarderClients(HashMap<String, Client> clients) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FICHIER_CLIENTS))) {

            for (Client c : clients.values()) {

                writer.write(
                        c.getCin() + "," +
                        c.getNom() + "," +
                        c.getPrenom() + "," +
                        c.getEmail()
                );

                writer.newLine();
            }

        } catch(IOException e){

            System.out.println(
                    "Erreur sauvegarde clients: "
                            + e.getMessage()
            );
        }
    }

    public HashMap<String, Client> chargerClients() {

        return new HashMap<>();
    }

    public void sauvegarderTransactions(
            List<Transaction> transactions){

        try(
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(
                                        FICHIER_TRANSACTIONS))
        ){

            for(Transaction t : transactions){

                writer.write(
                        t.getId()+","+
                        t.getDate()+","+
                        t.getMontant()+","+
                        t.getDescription()
                );

                writer.newLine();
            }

        }catch(IOException e){

            System.out.println(
                    "Erreur sauvegarde transactions"
            );
        }
    }

    public List<Transaction> chargerTransactions(){

        return new ArrayList<>();
    }
}