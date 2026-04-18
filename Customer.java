/**
 * Represents a bank customer.
 *
 * This class stores customer identity information and manages
 * the accounts that belong to the customer. It provides methods
 * for adding, removing, locating, and operating on accounts.
 */
package xxxxxxxx;

// Import list tools (to store multiple accounts)
import java.util.ArrayList;
import java.util.List;

public class Customer {

    // Customer's first name
    private String name;

    // Customer's last name
    private String surname;

    // Personal number (cannot change)
    private final String personalNumber;

    // List of all accounts that belong to this customer
    private final List<Account> accounts;

    // Constructor (runs when creating a new customer)
    public Customer(String name, String surname, String personalNumber){
        this.name = name; // Set name
        this.surname = surname; // Set surname
        this.personalNumber = personalNumber; // Set personal number
        this.accounts = new ArrayList<>(); // Start with empty account list
    }

    // Get personal number
    public String getPersonalNumber(){
        return personalNumber;
    }

    // Get first name
    public String getName(){
        return name;
    }

    // Get surname
    public String getSurname(){
        return surname;
    }

    // Get all accounts (returns a copy so original list is protected)
    public List<Account> getAccounts(){
        return new ArrayList<>(accounts);
    }

    // Change customer's name and/or surname
    public boolean changeName(String newName, String newSurname){
        boolean changed = false; // Track if anything changed

        // Change name if:
        // - not null
        // - not empty
        // - different from current name
        if (newName != null && !newName.isEmpty() && !newName.equals(this.name)){
            this.name = newName;
            changed = true;
        }

        // Change surname with same rules
        if (newSurname != null && !newSurname.isEmpty() && !newSurname.equals(this.surname)){
            this.surname = newSurname;
            changed = true;
        }

        return changed; // true if something changed
    }

    // Add a new account to this customer
    public void addAccount(Account account){
        accounts.add(account);
    }

    // Find an account by account ID
    public Account findAccount(int accountId){

        // Go through all accounts
        for (Account a : accounts){
            if (a.getAccountId() == accountId) return a; // Found
        }

        return null; // Not found
    }

    // Remove an account by account ID
    public Account removeAccount(int accountId){

        // Loop using index so we can remove safely
        for (int i = 0; i < accounts.size(); i++){
            if (accounts.get(i).getAccountId() == accountId){
                return accounts.remove(i); // Remove and return account
            }
        }

        return null; // Not found
    }

    // Deposit money into a specific account
    public boolean depositTo(int accountId, int amount){
        Account a = findAccount(accountId); // Find account

        // Only deposit if account exists
        return a != null && a.deposit(amount);
    }

    // Withdraw money from a specific account
    public boolean withdrawFrom(int accountId, int amount){
        Account a = findAccount(accountId); // Find account

        // Only withdraw if account exists
        return a != null && a.withdraw(amount);
    }

    // Return basic customer info as string
    public String customerInfoString(){
        return personalNumber + " " + name + " " + surname;
    }

    // Return customer info + all account info
    public List<String> customerWithAccountsStrings(){
        List<String> info = new ArrayList<>();

        // Add customer info first
        info.add(customerInfoString());

        // Add all account info
        for (Account a : accounts){
            info.add(a.toAccountString());
        }

        return info;
    }
}
