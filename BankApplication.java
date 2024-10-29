import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Account {
    protected double balance;
    protected String accountNumber;
    protected List<String> transactionHistory;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        recordTransaction("Initial deposit: " + initialBalance);
    }

    public abstract void withdraw(double amount) throws Exception;

    public void deposit(double amount) {
        balance += amount;
        recordTransaction("Deposited: " + amount);
    }

    public double getBalance() {
        return balance;
    }

    public void displayBalance() {
        System.out.printf("Account Balance: %.2f%n", balance);
    }

    protected void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public void printTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, double initialBalance) {
        super(accountNumber, initialBalance);
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Withdrawal denied: Insufficient funds in savings.");
        }
        balance -= amount;
        recordTransaction("Withdrawn: " + amount);
    }
}

class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, double initialBalance, double overdraftLimit) {
        super(accountNumber, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount > balance + overdraftLimit) {
            throw new Exception("Withdrawal denied: Overdraft limit exceeded.");
        }
        balance -= amount;
        recordTransaction("Withdrawn: " + amount);
    }
}

public class BankApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SavingsAccount savings = new SavingsAccount("SA123", 1000.0);
        CurrentAccount current = new CurrentAccount("CA123", 800.0, 200.0);

        while (true) {
            System.out.println("Choose an account:");
            System.out.println("1. Savings Account");
            System.out.println("2. Current Account");
            System.out.println("3. Exit");

            int choice = -1;
            while (choice < 1 || choice > 3) {
                System.out.print("Enter your choice: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    scanner.next(); // clear invalid input
                }
            }

            if (choice == 3) {
                break;
            }

            Account selectedAccount = (choice == 1) ? savings : current;

            System.out.println("Choose an action:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Transaction History");
            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    selectedAccount.deposit(depositAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    try {
                        selectedAccount.withdraw(withdrawAmount);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    selectedAccount.displayBalance();
                    break;
                case 4:
                    selectedAccount.printTransactionHistory();
                    break;
                default:
                    System.out.println("Invalid action.");
            }
        }
        scanner.close();
    }
}
