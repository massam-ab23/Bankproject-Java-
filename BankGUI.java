/**
 * Main GUI window for the banking application.
 *
 * This class creates the main application frame, including
 * the menu bar, header, status bar, and screen switching
 * between customer and account management panels.
 */

package xxxxxxx;

// Import Swing tools for windows, menus, labels, dialogs, and layouts
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BankGUI extends JFrame {


    // Reference to the bank logic (the program brain)
    private final BankLogic bankLogic;

    // CardLayout lets us switch between different screens/panels
    private final CardLayout cardLayout;

    // Panel that holds the different screens
    private final JPanel cardPanel;

    // Panel for customer handling
    private final CustomerPanel customerPanel;

    // Panel for account handling
    private final AccountPanel accountPanel;

    // Label at the bottom that shows status messages
    private final JLabel statusLabel;

    // Constructor (builds the whole main window)
    public BankGUI(BankLogic bankLogic) {
        this.bankLogic = bankLogic; // Save bank logic reference

        setTitle("Bankapplikation - Muiugeta Abrham Sbhat"); // Window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close program when window closes
        setSize(1000, 650); // Set window size
        setLocationRelativeTo(null); // Put window in center of screen

        // Try to use the system's normal look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Try to load a window icon image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("images/bank.png"));
            setIconImage(icon.getImage());
        } catch (Exception ignored) {
        }

        cardLayout = new CardLayout(); // Create screen-switching layout
        cardPanel = new JPanel(cardLayout); // Create panel that uses CardLayout

        statusLabel = new JLabel("Redo"); // Status text at bottom
        statusLabel.setBorder(new EmptyBorder(6, 10, 6, 10)); // Add padding

        customerPanel = new CustomerPanel(this, bankLogic); // Create customer page
        accountPanel = new AccountPanel(this, bankLogic); // Create account page

        // Add pages to card panel with names
        cardPanel.add(customerPanel, "CUSTOMERS");
        cardPanel.add(accountPanel, "ACCOUNTS");

        setLayout(new BorderLayout()); // Main window layout
        setJMenuBar(createMenuBar()); // Add top menu bar
        add(createHeaderPanel(), BorderLayout.NORTH); // Add top header
        add(cardPanel, BorderLayout.CENTER); // Add main changing area
        add(statusLabel, BorderLayout.SOUTH); // Add status at bottom

        showCustomerPanel(); // Start by showing customer page
        setVisible(true); // Show window on screen
    }

    // Create the top header panel with title and subtitle
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15)); // Add spacing around header

        JLabel titleLabel = new JLabel("Bankhantering");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24)); // Big bold title

        JLabel subtitleLabel = new JLabel("Hantera kunder, konton och transaktioner");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Smaller subtitle

        // Panel that stacks title and subtitle vertically
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.WEST); // Put text on left side

        return headerPanel;
    }

    // Create the menu bar at the top
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // First menu: navigation
        JMenu navigateMenu = new JMenu("Navigera");
        JMenuItem customersItem = new JMenuItem("Kunder");
        JMenuItem helpItem = new JMenuItem("Om programmet");
        JMenuItem exitItem = new JMenuItem("Avsluta");

        // When user clicks "Kunder", show customer page
        customersItem.addActionListener(e -> showCustomerPanel());

        // When user clicks "Om programmet", show info dialog
        helpItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Bankapplikation byggd i Java Swing.\n" +
                                "Projektet använder BankLogic från uppgift 2.",
                        "Om programmet",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        // When user clicks "Avsluta", close the window
        exitItem.addActionListener(e -> dispose());

        navigateMenu.add(customersItem);
        navigateMenu.addSeparator(); // Add a line separator
        navigateMenu.add(exitItem);

        // Second menu: help
        JMenu helpMenu = new JMenu("Hjälp");
        helpMenu.add(helpItem);

        menuBar.add(navigateMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    // Show the customer panel/page
    public void showCustomerPanel() {
        customerPanel.refreshCustomerList(); // Update customer list first
        cardLayout.show(cardPanel, "CUSTOMERS"); // Switch to customer page
        setStatus("Visar kundpanelen"); // Update status text
    }

    // Show the account panel/page for a specific customer
    public void showAccountPanel(String personalNumber) {
        accountPanel.setSelectedCustomer(personalNumber); // Tell account panel which customer to show
        cardLayout.show(cardPanel, "ACCOUNTS"); // Switch to account page
        setStatus("Visar kontopanel för kund: " + personalNumber); // Update status
    }

    // Change the text in the status bar
    public void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }
}
