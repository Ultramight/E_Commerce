package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Carrello {

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter("carrello.txt")) {

            writer.println("Carrello utenti:\n");
         // Intestazione (facoltativa ma utile per letture successive)
            writer.println("Email;Tipo;Marca;Prezzo");

            // Carrello di Mario Rossi -
            writer.println("mario.rossi@example.com;Jeans;Carrera;29.99");
            writer.println("mario.rossi@example.com;Pantaloncino;Levis;13.99");
          

            // Carrello di Laura Bianchi -          
            writer.println("laura.bianchi@example.com;Jeans;Levis;39.99");
            writer.println("laura.bianchi@example.com;Pantaloncino;Diesel;9.99");
            

            // Carrello di Luca Verdi -
            writer.println("luca.verdi@example.com;Pantaloncino;Diesel;9.99");

            System.out.println("Carrelli salvati in carrello.txt.");
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}