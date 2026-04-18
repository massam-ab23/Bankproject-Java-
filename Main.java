/**
 * Entry point of the banking application.
 *
 * This class initializes the core logic and starts the GUI.
 * The application is launched on the Swing Event Dispatch Thread.
 */
package xxxxxxx;

// Import Swing utility for starting GUI safely
import javax.swing.SwingUtilities;

public class Main {


    public static void main(String[] args) {

        // Run GUI code in the correct Swing thread
        SwingUtilities.invokeLater(() -> {

            // Create the bank logic (handles all data and operations)
            BankLogic bankLogic = new BankLogic();

            // Create and show the main GUI window
            new BankGUI(bankLogic);
        });
    }
}
