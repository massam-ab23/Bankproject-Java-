/**
 * Savings account implementation.
 *
 * This account type provides interest on the balance.
 * The first withdrawal is free, and subsequent withdrawals
 * incur a fee based on a percentage of the withdrawn amount.
 *
 * Features:
 * - Interest calculation
 * - Withdrawal tracking
 * - Fee applied after first withdrawal
 */
package xxxxxxx;

// Import tools for precise money calculations and rounding
import java.math.BigDecimal;
import java.math.RoundingMode;



public class SavingsAccount extends Account {


    // Interest rate (2.4%)
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.024");

    // Fee rate (2%) after the first withdrawal
    private static final BigDecimal FEE_RATE = new BigDecimal("0.02");

    // Count how many withdrawals have been done
    private int withdrawals;

    // Constructor (creates a new savings account with 0 kr)
    public SavingsAccount(int accountId) {
        super(accountId, BigDecimal.ZERO); // Call parent constructor
        this.withdrawals = 0; // No withdrawals at start
    }

    // Return account type
    @Override
    public String getAccountType() {
        return "Sparkonto";
    }

    // Withdraw money (with possible fee after first withdrawal)
    @Override
    public boolean withdraw(int amount) {

        if (amount <= 0) return false; // Cannot withdraw 0 or negative

        BigDecimal amountBd = BigDecimal.valueOf(amount); // Convert to BigDecimal

        BigDecimal fee = BigDecimal.ZERO;

        // If already withdrawn before, add fee
        if (withdrawals >= 1) {
            fee = amountBd.multiply(FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        }

        // Total amount to remove = amount + fee
        BigDecimal total = amountBd.add(fee);

        // If not enough money in account -> fail
        if (getBalance().compareTo(total) < 0) return false;

        // Subtract total from balance
        setBalance(getBalance().subtract(total));

        withdrawals++; // Increase withdrawal count

        // Save transaction (negative because money is taken out)
        addTransaction(total.negate());

        return true;
    }

    // Return account info as a string
    @Override
    public String toAccountString() {
        return getAccountId() + " " + formatCurrency(getBalance()) + " " +
                getAccountType() + " " + formatPercent(INTEREST_RATE);
    }

    // Return account info when closing account
    @Override
    public String closeAccountString() {

        // Calculate interest money in kronor
        BigDecimal interestSek = getBalance().multiply(INTEREST_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        return getAccountId() + " " + formatCurrency(getBalance()) + " " +
                getAccountType() + " " + formatCurrency(interestSek);
    }
}
