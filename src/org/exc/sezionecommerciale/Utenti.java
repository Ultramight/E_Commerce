package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Utenti {
    
    // Metodo per scrivere i dati degli utenti nel file
    public void salvaDatiUtenti() {
        try (PrintWriter writer = new PrintWriter("utenti.txt")) {  // Creazione e apertura del file
            // Intestazione del file
            writer.println("Utente;Nome;Cognome;Email;Indirizzo");

            // Scrittura dei dati per ciascun utente
            writer.println("Utente 1;Mario;Rossi;mario.rossi@example.com;Via Roma 1");
            writer.println("Utente 2;Laura;Bianchi;laura.bianchi@example.com;Via Lazio 3");
            writer.println("Utente 3;Luca;Verdi;luca.verdi@example.com;Via Pisa 109");

            // Messaggio di conferma
            System.out.println("Dati degli utenti salvati in utenti.txt.");
        } catch (IOException e) {  // Gestione delle eccezioni di I/O
            System.out.println("Errore nella scrittura del file");
        }
    }
}
