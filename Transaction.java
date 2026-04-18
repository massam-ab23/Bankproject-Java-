/**
 * Represents a single account transaction.
 *
 * A transaction stores:
 * - the date and time it occurred
 * - the amount (positive for deposits, negative for withdrawals)
 * - the account balance after the transaction
 */
package xxxxxxxxx;

// Import tools for date/time and precise money values
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Transaction {


    // Date and time when the transaction happened
    private final LocalDateTime dateTime;

    // Amount of money in the transaction
    // (positive = deposit, negative = withdrawal)
    private final BigDecimal amount;

    // The account balance after this transaction
    private final BigDecimal balanceAfter;

    // Constructor (creates a new transaction)
    public Transaction(LocalDateTime dateTime, BigDecimal amount, BigDecimal balanceAfter){
        this.dateTime = dateTime; // Set date and time
        this.amount = amount; // Set amount changed
        this.balanceAfter = balanceAfter; // Set balance after transaction
    }

    // Get date and time of transaction
    public  LocalDateTime getDateTime(){
        return dateTime;
    }

    // Get amount of transaction
    public BigDecimal getAmount(){
        return amount;
    }

    // Get balance after transaction
    public BigDecimal getBalanceAfter(){
        return balanceAfter;
    }
}
