package org.exc.sezionecommerciale;

//FUNZIONALITA' PRINCIPALI
//L'utente può effettuare il login inserendo la propria email e accedere al proprio carrello.
//L'utente può aggiungere e rimuovere prodotti dal carrello.
//L'interfaccia aggiorna il totale del carrello in tempo reale e salva i cambiamenti nel file carrello.txt.
//Il sistema è in grado di gestire il login, la visualizzazione delle informazioni utente e il logout.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Interfaccia extends JFrame {
    private JTextField emailField;
    private JButton loginButton;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel userInfoPanel;
    private JPanel cartPanel;
    private JLabel statusLabel;
    private JButton logoutButton;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    
    private String loggedUserEmail = null;
    private String loggedUserName = null;
    private String loggedUserSurname = null;
    private String loggedUserAddress = null;
    
    private List<Product> availableProducts;
    private List<CartItem> userCart;

    public Interfaccia() {
        setTitle("Sistema E-commerce");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inizializzazione delle liste
        availableProducts = new ArrayList<>();
        userCart = new ArrayList<>();
        
        // Caricamento dei prodotti disponibili
        loadProducts();
        
        // Configurazione del layout principale
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Pannello di login
        setupLoginPanel();
        
        // Pannello per le informazioni utente (inizialmente nascosto)
        setupUserInfoPanel();
        
        // Pannello per il carrello (inizialmente nascosto)
        setupCartPanel();
        
        // Pannello di stato in basso
        setupStatusPanel();
        
        // Inizialmente mostra solo il pannello di login
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    // Creazione del pannello di Login
    private void setupLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        //gbc.gridx definisce la colonna in cui verrà posizionato il componente.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        loginPanel.add(titleLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        loginPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 10);
        loginPanel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        loginPanel.add(loginButton, gbc);
    }
     //Questa riga crea un nuovo oggetto JPanel, che è un contenitore (panel) per i componenti grafici
    private void setupUserInfoPanel() {
        userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni Utente"));
    }
    
    private JLabel totalPriceLabel;
    
    private void setupCartPanel() {
        cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Carrello"));
        
        // Creazione della tabella del carrello
        //Questi nomi saranno utilizzati come intestazioni delle colonne quando la tabella verrà visualizzata.
        String[] columnNames = {"Tipo", "Marca", "Prezzo"};
        cartTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; //Questo metodo è sovrascritto per disabilitare la possibilità di modificare le celle della tabella.
            }
        };
        cartTable = new JTable(cartTableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        
        // Pannello per il totale
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPriceLabel = new JLabel("Totale: 0.00 €");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(totalPriceLabel);
        
        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        addProductButton = new JButton("Aggiungi Prodotto");
        removeProductButton = new JButton("Rimuovi Prodotto");
        
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductToCart();
            }
        });
         //Aggiunge l'evento di rimorzione prodotto dal carello
        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProductFromCart();
            }
        });
        
        buttonPanel.add(addProductButton);
        buttonPanel.add(removeProductButton);
        
        // Pannello con totale e pulsanti
        // Scelta personalmente la posizione
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        cartPanel.add(southPanel, BorderLayout.SOUTH);
    }
    
    private void setupStatusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        //Ogni area può contenere un componente, e il BorderLayout si adatta 
        //in modo da distribuire lo spazio in base alla posizione di ciascun componente.
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        statusLabel = new JLabel("Non connesso");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        logoutButton = new JButton("Logout");
        logoutButton.setEnabled(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(logoutButton, BorderLayout.EAST);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void login() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci un'email", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("utenti.txt"));
            String line;
            boolean found = false;
            
            // Salta la prima riga (intestazione)
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5 && parts[3].equalsIgnoreCase(email)) {
                    loggedUserEmail = parts[3];
                    loggedUserName = parts[1];
                    loggedUserSurname = parts[2];
                    loggedUserAddress = parts[4];
                    found = true;
                    break;
                }
            }
            reader.close();
            
            if (found) {
                // Utente trovato, carica il carrello
                loadUserCart();
                
                // Aggiorna stato
                statusLabel.setText("Online: " + loggedUserName + " " + loggedUserSurname);
                logoutButton.setEnabled(true);
                
                // Aggiorna pannello informazioni utente
                updateUserInfoPanel();
                
                // Cambia visualizzazione
                mainPanel.remove(loginPanel);
                
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new BorderLayout());
                contentPanel.add(userInfoPanel, BorderLayout.NORTH);
                contentPanel.add(cartPanel, BorderLayout.CENTER);
                
                mainPanel.add(contentPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Utente non trovato", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore nella lettura del file utenti", "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void logout() {
        // Resetta le informazioni utente
        loggedUserEmail = null;
        loggedUserName = null;
        loggedUserSurname = null;
        loggedUserAddress = null;
        
        // Svuota il carrello visualizzato
        userCart.clear();
        cartTableModel.setRowCount(0);
        
        // Aggiorna stato
        statusLabel.setText("Non connesso");
        logoutButton.setEnabled(false);
        
        // Torna alla schermata di login
        mainPanel.removeAll();
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
        
        // Resetta il campo email
        emailField.setText("");
    }
    
    private void loadProducts() {
        availableProducts.clear();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("prodotti.txt"));
            String line;
            
            // Salta la prima riga (intestazione)
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String type = parts[1];
                    String brand = parts[3];
                    double price = Double.parseDouble(parts[2].replace(',', '.'));
                    
                    availableProducts.add(new Product(id, type, brand, price));
                }
            }
            reader.close();
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Errore nella lettura del file prodotti", "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void loadUserCart() {
        userCart.clear();
        cartTableModel.setRowCount(0);
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("carrello.txt"));
            String line;
            int lineCount = 0;
            
            // Leggi le righe una ad una e gestisci il formato del file in modo più flessibile
            while ((line = reader.readLine()) != null) {
                lineCount++;
                
                // Ignora le righe di intestazione
                if (lineCount <= 3 || line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(";");
                if (parts.length >= 4 && parts[0].equalsIgnoreCase(loggedUserEmail)) {
                    String type = parts[1];
                    String brand = parts[2];
                    
                    // Gestione più robusta della conversione del prezzo
                    double price = 0.0;
                    try {
                        price = Double.parseDouble(parts[3].replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.out.println("Errore nella conversione del prezzo: " + parts[3]);
                        continue; // Salta questo prodotto ma continua a leggere gli altri
                    }
                    
                    CartItem item = new CartItem(type, brand, price);
                    userCart.add(item);
                    
                    // Aggiungi alla tabella
                    cartTableModel.addRow(new Object[]{type, brand, String.format("%.2f €", price)});
                }
            }
            reader.close();
            
            // Aggiorna il totale
            updateCartTotal();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore nella lettura del file carrello", "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateCartTotal() {
        double total = 0.0;
        for (CartItem item : userCart) {
            total += item.getPrice();
        }
        totalPriceLabel.setText("Totale: " + String.format("%.2f €", total));
    }
    
    private void updateUserInfoPanel() {
        userInfoPanel.removeAll();
        
        JLabel nameLabel = new JLabel("Nome: " + loggedUserName + " " + loggedUserSurname);
        JLabel emailLabel = new JLabel("Email: " + loggedUserEmail);
        JLabel addressLabel = new JLabel("Indirizzo: " + loggedUserAddress);
        
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        emailLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addressLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(addressLabel);
        
        userInfoPanel.revalidate();
        userInfoPanel.repaint();
    }
    
    private void addProductToCart() {
        if (loggedUserEmail == null) {
            return;
        }
        
        // Se non ci sono prodotti disponibili
        if (availableProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun prodotto disponibile", "Informazione", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Prepara la lista di prodotti da mostrare
        Vector<String> productOptions = new Vector<>();
        for (Product product : availableProducts) {
            productOptions.add(product.getType() + " - " + product.getBrand() + " - " + String.format("%.2f €", product.getPrice()));
        }
        
        // Mostra un dialogo per selezionare il prodotto
        JComboBox<String> productComboBox = new JComboBox<>(productOptions);
        
        int result = JOptionPane.showConfirmDialog(this, productComboBox, 
                "Seleziona un prodotto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = productComboBox.getSelectedIndex();
            Product selectedProduct = availableProducts.get(selectedIndex);
            
            // Aggiungi al modello della tabella
            cartTableModel.addRow(new Object[]{
                selectedProduct.getType(),
                selectedProduct.getBrand(),
                String.format("%.2f €", selectedProduct.getPrice())
            });
            
            // Aggiungi alla lista del carrello
            userCart.add(new CartItem(selectedProduct.getType(), selectedProduct.getBrand(), selectedProduct.getPrice()));
            
            // Aggiorna il totale
            updateCartTotal();
            
            // Salva nel file
            saveCartToFile();
        }
    }
    
    private void removeProductFromCart() {
        if (loggedUserEmail == null) {
            return;
        }
        
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un prodotto da rimuovere", "Avviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Rimuovi dalla lista
        userCart.remove(selectedRow);
        
        // Rimuovi dalla tabella
        cartTableModel.removeRow(selectedRow);
        
        // Aggiorna il totale
        updateCartTotal();
        
        // Salva nel file
        saveCartToFile();
    }
    
    private void saveCartToFile() {
        try {
            // Prima leggiamo tutto il file per mantenere i carrelli degli altri utenti
            List<String> allLines = new ArrayList<>();
            List<String> headerLines = new ArrayList<>();
            boolean headerProcessed = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader("carrello.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!headerProcessed && (line.equals("Carrello utenti:") || line.isEmpty() || line.startsWith("Email;"))) {
                        headerLines.add(line);
                        if (line.startsWith("Email;")) {
                            headerProcessed = true;
                        }
                    } else if (!line.split(";")[0].equalsIgnoreCase(loggedUserEmail)) {
                        allLines.add(line);
                    }
                }
            }
            
            // Ora scriviamo tutto di nuovo con il carrello aggiornato
            try (PrintWriter writer = new PrintWriter(new FileWriter("carrello.txt"))) {
                // Scriviamo le intestazioni
                for (String header : headerLines) {
                    writer.println(header);
                }
                
                // Scriviamo i carrelli degli altri utenti
                for (String line : allLines) {
                    writer.println(line);
                }
                
                // Scriviamo il carrello dell'utente corrente
                for (CartItem item : userCart) {
                    writer.println(loggedUserEmail + ";" + item.getType() + ";" + item.getBrand() + ";" + 
                                   String.format("%.2f", item.getPrice()).replace(',', '.'));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio del carrello", "Errore", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Classe per rappresentare un prodotto
    private static class Product {
        private String id;
        private String type;
        private String brand;
        private double price;
        
        public Product(String id, String type, String brand, double price) {
            this.id = id;
            this.type = type;
            this.brand = brand;
            this.price = price;
        }
        
        public String getId() {
            return id;
        }
        
        public String getType() {
            return type;
        }
        
        public String getBrand() {
            return brand;
        }
        
        public double getPrice() {
            return price;
        }
    }
    
    // Classe per rappresentare un elemento nel carrello
    private static class CartItem {
        private String type;
        private String brand;
        private double price;
        
        public CartItem(String type, String brand, double price) {
            this.type = type;
            this.brand = brand;
            this.price = price;
        }
        
        public String getType() {
            return type;
        }
        
        public String getBrand() {
            return brand;
        }
        
        public double getPrice() {
            return price;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interfaccia();
            }
        });
    }
}