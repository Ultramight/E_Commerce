package org.exc.sezionecommerciale;

import java.io.PrintWriter;
import java.io.IOException;

public class Carrello {

    public static void scriviCarrelliSuFile() {
        try (PrintWriter writer = new PrintWriter("carrello.txt")) {

            writer.println("Carrello utenti:\n");
            writer.println("Email;Tipo;Marca;Prezzo");

            writer.println("mario.rossi@example.com;Jeans;Carrera;29.99");
            writer.println("mario.rossi@example.com;Pantaloncino;Levis;13.99");

            writer.println("laura.bianchi@example.com;Jeans;Levis;39.99");
            writer.println("laura.bianchi@example.com;Pantaloncino;Diesel;9.99");

            writer.println("luca.verdi@example.com;Pantaloncino;Diesel;9.99");

        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file");
        }
    }
}