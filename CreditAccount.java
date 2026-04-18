/**
 * Credit account implementation.
 *
 * This account type allows negative balances up to a defined credit limit.
 * It applies different interest rates depending on whether the balance is
 * positive or negative.
 *
 * Features:
 * - Credit limit enforcement
 * - Separate interest rates for positive and negative balances
 * - Transaction tracking
 */
package xxxxxxx;

// Import tools for precise money calculations and rounding
import java.math.BigDecimal;
import java.math.RoundingMode;


public class CreditAccount extends Account {


    // Interest rate when balance is positive: 1.1%
    private static final BigDecimal POS_RATE = new BigDecimal("0.011");

    // Interest rate when balance is negative: 5%
    private static final BigDecimal NEG_RATE = new BigDecimal("0.05");

    // Lowest allowed balance (credit limit): -5000 kr
    private static final BigDecimal CREDIT_LIMIT = new BigDecimal("-5000");

    // Constructor (creates a new credit account with 0 kr)
    public CreditAccount(int accountId){
        super(accountId, BigDecimal.ZERO); // Call parent class constructor
    }

    // Return the type of account
    @Override
    public String getAccountType(){
        return "Kreditkonto";
    }

    // Withdraw money from the credit account
    @Override
    public boolean withdraw(int amount){
        if (amount <= 0) return false; // Cannot withdraw 0 or negative amount

        BigDecimal amountBd = BigDecimal.valueOf(amount); // Convert amount to BigDecimal
        BigDecimal newBalance = getBalance().subtract(amountBd); // Calculate new balance after withdrawal

        if (newBalance.compareTo(CREDIT_LIMIT) < 0) return false; // Stop if it goes below credit limit

        setBalance(newBalance); // Update balance
        addTransaction(amountBd.negate()); // Save transaction as negative value
        return true; // Withdrawal worked
    }

    // Return account info as a text string
    @Override
    public String toAccountString() {
        BigDecimal rate = (getBalance().compareTo(BigDecimal.ZERO) < 0) ? NEG_RATE : POS_RATE; // Choose interest rate depending on balance

        return getAccountId() + " " + formatCurrency(getBalance()) + " " +
                getAccountType() + " " + formatPercent(rate);
    }

    // Return account info when account is closed
    @Override
    public String closeAccountString(){
        BigDecimal rate = (getBalance().compareTo(BigDecimal.ZERO) < 0) ? NEG_RATE : POS_RATE; // Choose correct interest rate
        BigDecimal interestSek = getBalance().multiply(rate).setScale(2, RoundingMode.HALF_UP); // Calculate interest in kronor

        return getAccountId() + " " + formatCurrency(getBalance()) + " " +
                getAccountType() + " " + formatCurrency(interestSek);

    }
}
