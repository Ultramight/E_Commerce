package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Utenti {
    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter("utenti.txt")) {

            // Intestazione
            writer.println("Utente;Nome;Cognome;Email;Indirizzo");

            // Utente 1
            writer.println("Utente 1;Mario;Rossi;mario.rossi@example.com;Via Roma 1");

            // Utente 2
            writer.println("Utente 2;Laura;Bianchi;laura.bianchi@example.com;Via Lazio 3");

            // Utente 3
            writer.println("Utente 3;Luca;Verdi;luca.verdi@example.com;Via Pisa 109");

            System.out.println("Dati degli utenti salvati in utenti.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}
