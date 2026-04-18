/**
 * Core business logic for the banking system.
 *
 * This class manages customers, accounts, deposits, withdrawals,
 * account creation/closure, customer deletion, and transaction retrieval.
 * It acts as the main service layer between the GUI and the domain objects.
 */
package xxxxxxx;

// Import list tools (to store many customers)
import java.util.ArrayList;
import java.util.List;

public class BankLogic {

    // List that stores all customers in the bank
    private final List<Customer> customers;

    // Keeps track of the next account number to use
    private int nextAccountId;

    // Constructor (runs when the bank system starts)
    public BankLogic() {
        customers = new ArrayList<>(); // Start with no customers
        nextAccountId = 1001; // First account number
    }

    // ------------------- REQUIRED METHODS (TestBank calls these) -------------------

    // Return all customers as text strings
    public List<String> getAllCustomers() {
        List<String> result = new ArrayList<>();

        // Go through each customer in the bank
        for (Customer c : customers) {
            result.add(c.customerInfoString()); // Add customer info to result list
        }

        return result;
    }

    // Create a new customer
    public boolean createCustomer(String name, String surname, String pNo) {

        // If a customer with this personal number already exists -> fail
        if (findCustomer(pNo) != null) return false;

        // Otherwise create and add the customer
        customers.add(new Customer(name, surname, pNo));

        return true;
    }

    // Get one customer and that customer's accounts
    public List<String> getCustomer(String pNo) {
        Customer c = findCustomer(pNo); // Find the customer

        if (c == null) return null; // Customer not found

        return c.customerWithAccountsStrings(); // Return customer info + account info
    }

    // Change a customer's name
    public boolean changeCustomerName(String name, String surname, String pNo) {
        Customer c = findCustomer(pNo); // Find the customer

        if (c == null) return false; // Customer not found

        // If both new name and new surname are empty -> fail
        if (name != null && surname != null && name.isEmpty() && surname.isEmpty()) {
            return false;
        }

        // Let the customer object update its own name
        return c.changeName(name, surname);
    }

    // Create a savings account for a customer
    public int createSavingsAccount(String pNo) {
        Customer c = findCustomer(pNo); // Find the customer

        if (c == null) return -1; // Customer not found

        int id = nextAccountId++; // Use current account number, then increase it

        c.addAccount(new SavingsAccount(id)); // Add new savings account to customer
        return id; // Return new account number
    }

    // Create a credit account for a customer
    public int createCreditAccount(String pNo){
        Customer c = findCustomer(pNo); // Find the customer

        if (c == null) return -1; // Customer not found

        int id = nextAccountId++; // Use current account number, then increase it
        c.addAccount(new CreditAccount(id)); // Add new credit account to customer
        return id; // Return new account number
    }

    // Get information about one specific account
    public String getAccount(String pNo, int accountId) {
        Customer c = findCustomer(pNo); // Find customer
        if (c == null) return null; // Customer not found

        Account a = c.findAccount(accountId); // Find account
        if (a == null) return null; // Account not found

        return a.toAccountString(); // Return account information
    }

    // Deposit money into an account
    public boolean deposit(String pNo, int accountId, int amount) {
        if (amount <= 0) return false; // Invalid amount

        Customer c = findCustomer(pNo); // Find customer
        if (c == null) return false; // Customer not found

        return c.depositTo(accountId, amount); // Let customer/account handle deposit
    }

    // Withdraw money from an account
    public boolean withdraw(String pNo, int accountId, int amount) {
        if (amount <= 0) return false; // Invalid amount

        Customer c = findCustomer(pNo); // Find customer
        if (c == null) return false; // Customer not found

        return c.withdrawFrom(accountId, amount); // Let customer/account handle withdrawal
    }

    // Close one account
    public String closeAccount(String pNo, int accountId) {
        Customer c = findCustomer(pNo); // Find customer
        if (c == null) return null; // Customer not found

        Account removed = c.removeAccount(accountId); // Remove the account
        if (removed == null) return null; // Account not found

        return removed.closeAccountString(); // Return final account info
    }

    // Delete a customer and return their info + all account closing info
    public List<String> deleteCustomer(String pNo) {
        Customer c = findCustomer(pNo); // Find customer
        if (c == null) return null; // Customer not found

        List<String> info = new ArrayList<>();
        info.add(c.customerInfoString()); // Save customer info first

        // Go through all accounts and save closing info
        for (Account a : c.getAccounts()) {
            info.add(a.closeAccountString());
        }

        customers.remove(c); // Remove customer from bank
        return info; // Return saved info
    }

    // Get all transactions for one account
    public List<String> getTransactions(String pNo, int accountId){
        Customer c = findCustomer(pNo); // Find customer
        if (c == null) return null; // Customer not found

        Account a = c.findAccount(accountId); // Find account
        if (a == null) return null; // Account not found

        return a.getTransactionStrings(); // Return transaction history
    }

    // ------------------- PRIVATE HELP METHOD -------------------

    // Find a customer using the personal number
    private Customer findCustomer(String pNo) {

        // Go through all customers
        for (Customer c : customers) {

            // If personal number matches, return that customer
            if (c.getPersonalNumber().equals(pNo)) {
                return c;
            }
        }

        return null; // No matching customer found
    }
}
