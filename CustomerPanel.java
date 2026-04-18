/**
 * GUI panel for customer management in the banking system.
 *
 * This panel allows the user to create, update, delete,
 * and inspect customers, as well as open the account view
 * for the selected customer.
 */
package xxxxxxxx;

// Import Swing tools for GUI parts like buttons, lists, text fields, and dialogs
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {

   
    // Reference to the main GUI window
    private final BankGUI gui;

    // Reference to the bank logic (the program brain)
    private final BankLogic bankLogic;

    // Model that stores customer strings for the list
    private final DefaultListModel<String> customerListModel;

    // Visual list that shows customers on screen
    private final JList<String> customerList;

    // Text fields for first name, last name, and personal number
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField pNoField;

    // Constructor (builds the whole customer panel)
    public CustomerPanel(BankGUI gui, BankLogic bankLogic) {
        this.gui = gui; // Save GUI reference
        this.bankLogic = bankLogic; // Save bank logic reference

        setLayout(new BorderLayout(15, 15)); // Use BorderLayout with spacing
        setBorder(new EmptyBorder(15, 15, 15, 15)); // Add space around panel

        customerListModel = new DefaultListModel<>();
        customerList = new JList<>(customerListModel);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one customer at a time
        customerList.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Nice readable font

        // When the selected customer changes, fill the text fields with that customer's data
        customerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelection();
            }
        });

        // Left panel with customer list
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Kunder"));
        leftPanel.add(new JScrollPane(customerList), BorderLayout.CENTER); // Add scrolling

        // Form panel with text fields
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Kunduppgifter"));

        formPanel.add(new JLabel("Förnamn:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Efternamn:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Personnummer:"));
        pNoField = new JTextField();
        formPanel.add(pNoField);

        // Right panel with form and buttons
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(formPanel, BorderLayout.NORTH);
        rightPanel.add(createButtonPanel(), BorderLayout.CENTER);

        // Add left and right parts to main panel
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        refreshCustomerList(); // Load customers into the list when panel starts
    }

    // Create panel with all customer action buttons
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Åtgärder"));

        // Create buttons
        JButton createButton = new JButton("Skapa kund");
        JButton changeButton = new JButton("Ändra kund");
        JButton deleteButton = new JButton("Ta bort kund");
        JButton showButton = new JButton("Visa kundinfo");
        JButton accountsButton = new JButton("Öppna konton");
        JButton clearButton = new JButton("Rensa fält");

        // Connect buttons to methods
        createButton.addActionListener(e -> createCustomer());
        changeButton.addActionListener(e -> changeCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        showButton.addActionListener(e -> showCustomerInfo());
        accountsButton.addActionListener(e -> openAccounts());
        clearButton.addActionListener(e -> clearFields());

        // Add buttons to panel
        panel.add(createButton);
        panel.add(changeButton);
        panel.add(deleteButton);
        panel.add(showButton);
        panel.add(accountsButton);
        panel.add(clearButton);

        return panel;
    }

    // Reload the customer list from BankLogic
    public void refreshCustomerList() {
        customerListModel.clear(); // Remove old list data

        List<String> customers = bankLogic.getAllCustomers();

        // Add each customer string to the list model
        for (String customer : customers) {
            customerListModel.addElement(customer);
        }

        gui.setStatus("Kundlistan uppdaterad"); // Show status text
    }

    // Fill text fields using the selected customer from the list
    private void fillFieldsFromSelection() {
        String selected = customerList.getSelectedValue();
        if (selected == null) return; // Nothing selected

        // Split into 3 parts: personal number, first name, last name
        String[] parts = selected.split(" ", 3);
        if (parts.length >= 3) {
            pNoField.setText(parts[0]);
            firstNameField.setText(parts[1]);
            lastNameField.setText(parts[2]);
        }
    }

    // Create a new customer
    private void createCustomer() {
        String firstName = firstNameField.getText().trim(); // Read first name
        String lastName = lastNameField.getText().trim(); // Read last name
        String pNo = pNoField.getText().trim(); // Read personal number

        // Stop if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || pNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fyll i alla fält.");
            return;
        }

        boolean created = bankLogic.createCustomer(firstName, lastName, pNo);
        if (created) {
            JOptionPane.showMessageDialog(this, "Kunden skapades.");
            gui.setStatus("Ny kund skapad: " + pNo);
            clearFields(); // Clear fields after success
            refreshCustomerList(); // Update list
        } else {
            JOptionPane.showMessageDialog(this, "Kunde inte skapa kund. Personnummer finns kanske redan.");
        }
    }

    // Change the selected customer
    private void changeCustomer() {
        String selected = customerList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Välj en kund i listan.");
            return;
        }

        String pNo = extractPersonalNumber(selected); // Get personal number from selected row
        String firstName = firstNameField.getText().trim(); // Read updated first name
        String lastName = lastNameField.getText().trim(); // Read updated last name

        boolean changed = bankLogic.changeCustomerName(firstName, lastName, pNo);
        if (changed) {
            JOptionPane.showMessageDialog(this, "Kundens namn uppdaterades.");
            gui.setStatus("Kund uppdaterad: " + pNo);
            refreshCustomerList(); // Refresh list after change
        } else {
            JOptionPane.showMessageDialog(this, "Ingen ändring kunde göras.");
        }
    }

    // Delete the selected customer
    private void deleteCustomer() {
        String selected = customerList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Välj en kund i listan.");
            return;
        }

        String pNo = extractPersonalNumber(selected); // Get personal number

        // Ask user to confirm deletion
        int choice = JOptionPane.showConfirmDialog(this,
                "Vill du verkligen ta bort kunden?",
                "Bekräfta borttagning",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return; // Stop if user says no
        }

        List<String> deletedInfo = bankLogic.deleteCustomer(pNo);
        if (deletedInfo != null) {
            JOptionPane.showMessageDialog(this,
                    "Kund borttagen:\n" + String.join("\n", deletedInfo));
            gui.setStatus("Kund borttagen: " + pNo);
            clearFields(); // Clear fields after deletion
            refreshCustomerList(); // Update list
        } else {
            JOptionPane.showMessageDialog(this, "Kunde inte ta bort kunden.");
        }
    }

    // Show full info about selected customer
    private void showCustomerInfo() {
        String selected = customerList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Välj en kund i listan.");
            return;
        }

        String pNo = extractPersonalNumber(selected); // Get personal number
        List<String> info = bankLogic.getCustomer(pNo); // Ask BankLogic for customer info

        if (info != null) {
            JTextArea textArea = new JTextArea(10, 35);
            textArea.setEditable(false); // User cannot type in it

            // Add each row of info to text area
            for (String row : info) {
                textArea.append(row + "\n");
            }

            JOptionPane.showMessageDialog(this,
                    new JScrollPane(textArea),
                    "Kundinformation",
                    JOptionPane.INFORMATION_MESSAGE);

            gui.setStatus("Visade kundinformation för: " + pNo);
        } else {
            JOptionPane.showMessageDialog(this, "Ingen kundinformation kunde hämtas.");
        }
    }

    // Open the account page for the selected customer
    private void openAccounts() {
        String selected = customerList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Välj en kund i listan.");
            return;
        }

        String pNo = extractPersonalNumber(selected); // Get personal number
        gui.showAccountPanel(pNo); // Switch to account panel
    }

    // Extract personal number from one row in the list
    private String extractPersonalNumber(String row) {
        int firstSpace = row.indexOf(' '); // Find first space
        return firstSpace == -1 ? row : row.substring(0, firstSpace); // Return text before first space
    }

    // Clear all text fields and unselect customer
    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        pNoField.setText("");
        customerList.clearSelection();
        gui.setStatus("Fälten rensades");
    }
}
