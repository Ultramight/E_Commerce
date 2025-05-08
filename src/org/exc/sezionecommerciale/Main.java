package org.exc.sezionecommerciale;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Avvia l'interfaccia in modo sicuro nel thread EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            new Interfaccia();
        });
    }
}