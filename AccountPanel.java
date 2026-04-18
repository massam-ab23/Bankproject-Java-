package xxxxxxx;

// Import Swing tools for GUI windows, buttons, labels, lists, and dialogs
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AccountPanel extends JPanel {

 /**
 * GUI panel for managing a selected customer's bank accounts.
 *
 * This panel allows the user to:
 * - create savings and credit accounts
 * - view account information
 * - deposit and withdraw money
 * - view transaction history
 * - close accounts
 *
 * It communicates with the BankLogic layer and updates the main BankGUI interface.
 */

    // Reference to the main GUI window
    private final BankGUI gui;

    // Reference to the bank logic (the program brain)
    private final BankLogic bankLogic;

    // Stores the personal number of the currently selected customer
    private String selectedPersonalNumber;

    // Label that shows which customer is selected
    private final JLabel customerLabel;

    // Model that stores account strings for the list
    private final DefaultListModel<String> accountListModel;

    // Visual list that shows accounts on screen
    private final JList<String> accountList;

    // Text field where user writes amount for deposit/withdraw
    private JTextField amountField;

    // Constructor (builds the whole account panel)
    public AccountPanel(BankGUI gui, BankLogic bankLogic) {
        this.gui = gui; // Save GUI reference
        this.bankLogic = bankLogic; // Save bank logic reference

        setLayout(new BorderLayout(15, 15)); // Use BorderLayout with spacing
        setBorder(new EmptyBorder(15, 15, 15, 15)); // Add padding around panel

        customerLabel = new JLabel("Ingen kund vald"); // No customer selected yet
        customerLabel.setFont(new Font("SansSerif", Font.BOLD, 18)); // Bigger bold text

        // Top panel that shows selected customer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Vald kund"));
        topPanel.add(customerLabel, BorderLayout.CENTER);

        // Create list model and account list
        accountListModel = new DefaultListModel<>();
        accountList = new JList<>(accountListModel);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one account at a time
        accountList.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Nice aligned font

        // Panel that holds the account list
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Konton"));
        listPanel.add(new JScrollPane(accountList), BorderLayout.CENTER); // Add scrolling

        // Right side panel with amount field and buttons
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(createAmountPanel(), BorderLayout.NORTH);
        rightPanel.add(createButtonPanel(), BorderLayout.CENTER);

        // Put all parts into the main panel
        add(topPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // Create panel where user writes amount
    private JPanel createAmountPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Belopp"));

        panel.add(new JLabel("Ange belopp:")); // Instruction text
        amountField = new JTextField(); // Input field for amount

        panel.add(amountField);
        return panel;
    }

    // Create panel with all action buttons
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Kontohantering"));

        // Create buttons
        JButton createSavingsButton = new JButton("Skapa sparkonto");
        JButton createCreditButton = new JButton("Skapa kreditkonto");
        JButton showAccountButton = new JButton("Visa konto");
        JButton depositButton = new JButton("Sätt in");
        JButton withdrawButton = new JButton("Ta ut");
        JButton transactionsButton = new JButton("Visa transaktioner");
        JButton closeAccountButton = new JButton("Stäng konto");
        JButton backButton = new JButton("Till kunder");

        // Connect buttons to methods
        createSavingsButton.addActionListener(e -> createSavingsAccount());
        createCreditButton.addActionListener(e -> createCreditAccount());
        showAccountButton.addActionListener(e -> showAccountInfo());
        depositButton.addActionListener(e -> depositToAccount());
        withdrawButton.addActionListener(e -> withdrawFromAccount());
        transactionsButton.addActionListener(e -> showTransactions());
        closeAccountButton.addActionListener(e -> closeAccount());
        backButton.addActionListener(e -> gui.showCustomerPanel());

        // Add buttons to panel
        panel.add(createSavingsButton);
        panel.add(createCreditButton);
        panel.add(showAccountButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transactionsButton);
        panel.add(closeAccountButton);
        panel.add(backButton);

        return panel;
    }

    // Set which customer is selected in this panel
    public void setSelectedCustomer(String personalNumber) {
        selectedPersonalNumber = personalNumber; // Save selected customer

        // If no customer is selected
        if (personalNumber == null) {
            customerLabel.setText("Ingen kund vald");
            accountListModel.clear(); // Clear account list
            return;
        }

        // Get customer info from bank logic
        List<String> customerInfo = bankLogic.getCustomer(personalNumber);

        // If customer exists, show first line (customer info)
        if (customerInfo != null && !customerInfo.isEmpty()) {
            customerLabel.setText(customerInfo.get(0));
        } else {
            customerLabel.setText(personalNumber); // Fallback: just show personal number
        }

        refreshAccountList(); // Update accounts shown in list
    }

    // Reload account list from bank logic
    public void refreshAccountList() {
        accountListModel.clear(); // Remove old items

        if (selectedPersonalNumber == null) return; // No customer selected

        List<String> customerInfo = bankLogic.getCustomer(selectedPersonalNumber);
        if (customerInfo == null || customerInfo.isEmpty()) return; // Nothing to show

        // Start at index 1 because index 0 is customer info, not account info
        for (int i = 1; i < customerInfo.size(); i++) {
            accountListModel.addElement(customerInfo.get(i));
        }

        gui.setStatus("Kontolistan uppdaterad"); // Show status text
    }

    // Create a savings account for selected customer
    private void createSavingsAccount() {
        if (!hasSelectedCustomer()) return; // Stop if no customer selected

        int accountId = bankLogic.createSavingsAccount(selectedPersonalNumber);
        if (accountId != -1) {
            JOptionPane.showMessageDialog(this, "Sparkonto skapat.\nKontonummer: " + accountId);
            refreshAccountList(); // Update list
            gui.setStatus("Sparkonto skapat: " + accountId);
        } else {
            JOptionPane.showMessageDialog(this, "Kunde inte skapa sparkonto.");
        }
    }

    // Create a credit account for selected customer
    private void createCreditAccount() {
        if (!hasSelectedCustomer()) return; // Stop if no customer selected

        int accountId = bankLogic.createCreditAccount(selectedPersonalNumber);
        if (accountId != -1) {
            JOptionPane.showMessageDialog(this, "Kreditkonto skapat.\nKontonummer: " + accountId);
            refreshAccountList(); // Update list
            gui.setStatus("Kreditkonto skapat: " + accountId);
        } else {
            JOptionPane.showMessageDialog(this, "Kunde inte skapa kreditkonto.");
        }
    }

    // Show information about selected account
    private void showAccountInfo() {
        Integer accountId = getSelectedAccountId(); // Read selected account number
        if (accountId == null) return;

        String info = bankLogic.getAccount(selectedPersonalNumber, accountId);
        if (info != null) {
            JOptionPane.showMessageDialog(this, info, "Kontoinformation", JOptionPane.INFORMATION_MESSAGE);
            gui.setStatus("Visade konto: " + accountId);
        } else {
            JOptionPane.showMessageDialog(this, "Kunde inte hämta kontoinformation.");
        }
    }

    // Deposit money into selected account
    private void depositToAccount() {
        Integer accountId = getSelectedAccountId(); // Read selected account number
        if (accountId == null) return;

        Integer amount = parseAmount(); // Read amount from text field
        if (amount == null) return;

        boolean success = bankLogic.deposit(selectedPersonalNumber, accountId, amount);
        if (success) {
            JOptionPane.showMessageDialog(this, "Insättning lyckades.");
            amountField.setText(""); // Clear input field
            refreshAccountList(); // Update account list
            gui.setStatus("Insättning genomförd på konto " + accountId);
        } else {
            JOptionPane.showMessageDialog(this, "Insättning misslyckades.");
        }
    }

    // Withdraw money from selected account
    private void withdrawFromAccount() {
        Integer accountId = getSelectedAccountId(); // Read selected account number
        if (accountId == null) return;

        Integer amount = parseAmount(); // Read amount from text field
        if (amount == null) return;

        boolean success = bankLogic.withdraw(selectedPersonalNumber, accountId, amount);
        if (success) {
            JOptionPane.showMessageDialog(this, "Uttag lyckades.");
            amountField.setText(""); // Clear input field
            refreshAccountList(); // Update account list
            gui.setStatus("Uttag genomfört från konto " + accountId);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Uttag misslyckades.\nKontrollera saldo, avgift eller kreditgräns.");
        }
    }

    // Show all transactions for selected account
    private void showTransactions() {
        Integer accountId = getSelectedAccountId(); // Read selected account number
        if (accountId == null) return;

        List<String> transactions = bankLogic.getTransactions(selectedPersonalNumber, accountId);
        if (transactions == null) {
            JOptionPane.showMessageDialog(this, "Kunde inte hämta transaktioner.");
            return;
        }

        // Create a text area to show transaction lines
        JTextArea area = new JTextArea(15, 45);
        area.setEditable(false); // User cannot type in it
        area.setFont(new Font("Monospaced", Font.PLAIN, 13)); // Nice readable font

        // If there are no transactions
        if (transactions.isEmpty()) {
            area.setText("Inga transaktioner finns för detta konto.");
        } else {
            // Add each transaction line to the text area
            for (String tx : transactions) {
                area.append(tx + "\n");
            }
        }

        // Show text area inside a scroll pane dialog
        JOptionPane.showMessageDialog(this,
                new JScrollPane(area),
                "Transaktioner för konto " + accountId,
                JOptionPane.INFORMATION_MESSAGE);

        gui.setStatus("Visade transaktioner för konto " + accountId);
    }

    // Close the selected account
    private void closeAccount() {
        Integer accountId = getSelectedAccountId(); // Read selected account number
        if (accountId == null) return;

        // Ask user for confirmation before closing
        int choice = JOptionPane.showConfirmDialog(this,
                "Vill du verkligen stänga konto " + accountId + "?",
                "Bekräfta stängning",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) return; // Stop if user says no

        String result = bankLogic.closeAccount(selectedPersonalNumber, accountId);
        if (result != null) {
            JOptionPane.showMessageDialog(this, "Kontot stängdes:\n" + result);
            refreshAccountList(); // Update list after closing account
            gui.setStatus("Konto stängt: " + accountId);
        } else {
            JOptionPane.showMessageDialog(this, "Kunde inte stänga kontot.");
        }
    }

    // Check that a customer is selected
    private boolean hasSelectedCustomer() {
        if (selectedPersonalNumber == null) {
            JOptionPane.showMessageDialog(this, "Ingen kund vald.");
            return false;
        }
        return true;
    }

    // Read account ID from the selected list item
    private Integer getSelectedAccountId() {
        if (!hasSelectedCustomer()) return null;

        String selected = accountList.getSelectedValue(); // Get selected row text
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Välj ett konto i listan.");
            return null;
        }

        try {
            // Account number is the first text before the first space
            return Integer.parseInt(selected.split(" ")[0]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kunde inte läsa kontonumret.");
            return null;
        }
    }

    // Read and validate amount from text field
    private Integer parseAmount() {
        String text = amountField.getText().trim(); // Read text and remove extra spaces

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ange ett belopp.");
            return null;
        }

        try {
            int amount = Integer.parseInt(text); // Convert text to integer

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Beloppet måste vara större än 0.");
                return null;
            }

            return amount; // Valid amount
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Belopp måste vara ett heltal.");
            return null;
        }
    }
}
