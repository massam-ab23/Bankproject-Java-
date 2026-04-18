package xxxxxxx;
// Import tools for money, rounding, date/time, text formatting, and lists
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Abstract base class for bank accounts.
 *
 * This class defines common properties and behavior shared by all account types,
 * including account ID, balance handling, transaction history, and money formatting.
 *
 * Features:
 * - Stores account ID and current balance
 * - Handles deposits
 * - Saves transaction history
 * - Formats amounts and percentages
 * - Provides abstract methods for account-specific behavior
 */
public abstract class Account {


    // Unique number for the account
    private final int accountId;

    // Current money in the account
    private BigDecimal balance;

    // List of all transactions made on this account
    private final List<Transaction> transactions;

    // Format for showing date and time of transactions
    private static final DateTimeFormatter TX_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructor (runs when a new account is created)
    protected Account(int accountID, BigDecimal startBalance){
        this.accountId = accountID; // Set account number
        this.balance = startBalance; // Set starting balance
        this.transactions = new ArrayList<>(); // Start with empty transaction list
    }

    // Get account number
    public int getAccountId(){
        return accountId;
    }

    // Get current balance
    public BigDecimal getBalance(){
        return balance;
    }

    // Every child account class must explain how to print account info
    public abstract String toAccountString();

    // Every child account class must explain how to print closing info
    public abstract String closeAccountString();

    // Every child account class must say what type of account it is
    public abstract  String getAccountType();

    // Deposit money into account
    public boolean deposit(int amount){
        if(amount <= 0) return false; // Cannot deposit 0 or negative

        BigDecimal a = BigDecimal.valueOf(amount); // Convert int to BigDecimal
        balance = balance.add(a); // Add amount to balance
        addTransaction(a); // Save deposit as transaction
        return true; // Success
    }

    // Every child account class must make its own withdraw rules
    public abstract boolean withdraw(int amount);

    // Change balance manually (used by child classes)
    protected void setBalance(BigDecimal newBalance){

        this.balance = newBalance;
    }

    // Save one transaction in the history list
    protected void addTransaction(BigDecimal amountSigned){
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        transactions.add(new Transaction(now, amountSigned, balance)); // Save transaction
    }

    // Return all transactions as readable text strings
    public List<String> getTransactionStrings(){
        List<String> out = new ArrayList<>();

        // Go through every transaction
        for (Transaction t : transactions){
            out.add(formatTransaction(t)); // Turn it into text
        }

        return out;
    }

    // Convert one transaction into a nice readable string
    private String formatTransaction(Transaction t){
        String dateTime = t.getDateTime().format(TX_FORMAT);

        return dateTime + " " + formatCurrency(t.getAmount()) +
                " Saldo: " + formatCurrency(t.getBalanceAfter());
    }

    // Format money in Swedish currency style
    protected String formatCurrency(BigDecimal value){
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        return nf.format(value.setScale(2, RoundingMode.HALF_UP));
    }

    // Format decimal number as percent
    protected String formatPercent(BigDecimal rate) {
        NumberFormat pf = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
        pf.setMaximumFractionDigits(1); // At most 1 decimal
        pf.setMinimumFractionDigits(0); // At least 0 decimals
        return pf.format(rate);
    }


}
