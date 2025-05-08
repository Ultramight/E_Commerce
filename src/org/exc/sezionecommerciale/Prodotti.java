package org.exc.sezionecommerciale;

import java.io.IOException;
import java.io.PrintWriter;

public class Prodotti {
	
	public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter("prodotti.txt")) {
        	
            // Intestazione (facoltativa ma utile per letture successive)
            writer.println("Prodotto;Tipo;Prezzo;Marca");

            // Prodotto 1
            writer.println("Prodotto 1;Jeans;29.99;Carrera");

            // Prodotto 2
            writer.println("Prodotto 2;Jeans;39.99;Levis");

            // Prodotto 3
            writer.println("Prodotto 3;Pantaloncino;9.99;Diesel");
            
            // Prodotto 4
            writer.println("Prodotto 4;Pantaloncino;13.99;Levis");

            System.out.println("Dati dei prodotti salvati in prodotti.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}
