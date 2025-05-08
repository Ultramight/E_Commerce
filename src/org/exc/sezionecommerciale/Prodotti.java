package org.exc.sezionecommerciale;

import java.io.IOException;
import java.io.PrintWriter;

public class Prodotti {
    
    // Metodo per scrivere i dati dei prodotti nel file
    public void salvaDatiProdotti() {
        try (PrintWriter writer = new PrintWriter("prodotti.txt")) {  // Creazione e apertura del file
            // Intestazione del file
            writer.println("Prodotto;Tipo;Prezzo;Marca");

            // Scrittura dei dati per ciascun prodotto
            writer.println("Prodotto 1;Jeans;29.99;Carrera");
            writer.println("Prodotto 2;Jeans;39.99;Levis");
            writer.println("Prodotto 3;Pantaloncino;9.99;Diesel");
            writer.println("Prodotto 4;Pantaloncino;13.99;Levis");

            // Messaggio di conferma
            System.out.println("Dati dei prodotti salvati in prodotti.txt.");
        } catch (IOException e) {  // Gestione delle eccezioni di I/O
            System.out.println("Errore nella scrittura del file");
        }
    }
}
