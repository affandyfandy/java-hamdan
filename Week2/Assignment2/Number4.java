import java.util.HashMap;
import java.util.Map;

public class Number4 {

    // ATM Interface
    public interface ATM {
        boolean withdraw(int accountId, double amount);
        boolean deposit(int accountId, double amount);
        double queryBalance(int accountId);
        boolean login(String password);
        boolean logout();

        default void displayMessage(String message) {
            System.out.println(message);
        }
    }

    // ATMImpl Class
    public static class ATMImpl implements ATM {
        private int idATM;
        private int accountId;
        private boolean isLoggedIn = false;
        private static final String CORRECT_PASSWORD = "123456";
        private static final Map<Integer, Double> accounts = new HashMap<>();

        public ATMImpl(int idATM, int accountId) {
            this.idATM = idATM;
            this.accountId = accountId;
            accounts.putIfAbsent(accountId, 0.0); // Initialize account balance if not present
        }

        @Override
        public boolean withdraw(int accountId, double amount) {
            if (!isLoggedIn) {
                displayMessage("Please login first.");
                return false;
            }
            if (amount <= 0) {
                displayMessage("Invalid amount.");
                return false;
            }
            double currentBalance = accounts.get(accountId);
            if (currentBalance >= amount) {
                accounts.put(accountId, currentBalance - amount);
                displayMessage("Withdraw successful. New balance: " + accounts.get(accountId));
                return true;
            } else {
                displayMessage("Insufficient balance.");
                return false;
            }
        }

        @Override
        public boolean deposit(int accountId, double amount) {
            if (!isLoggedIn) {
                displayMessage("Please login first.");
                return false;
            }
            if (amount <= 0) {
                displayMessage("Invalid amount.");
                return false;
            }
            double currentBalance = accounts.get(accountId);
            accounts.put(accountId, currentBalance + amount);
            displayMessage("Deposit successful. New balance: " + accounts.get(accountId));
            return true;
        }

        @Override
        public double queryBalance(int accountId) {
            if (!isLoggedIn) {
                displayMessage("Please login first.");
                return 0.0;
            }
            return accounts.get(accountId);
        }

        @Override
        public boolean login(String password) {
            if (CORRECT_PASSWORD.equals(password)) {
                isLoggedIn = true;
                displayMessage("Login successful.");
                return true;
            } else {
                displayMessage("Login failed.");
                return false;
            }
        }

        @Override
        public boolean logout() {
            isLoggedIn = false;
            displayMessage("Logged out successfully.");
            return true;
        }
    }

    // Account Interface
    public interface Account {
        void checkAccountDetails();

        default void accountStatement() {
            System.out.println("Displaying account statement...");
        }
    }

    // SavingAccount Class
    public static class SavingAccount implements Account {
        private int accountId;
        private double interestRate;

        public SavingAccount(int accountId, double interestRate) {
            this.accountId = accountId;
            this.interestRate = interestRate;
        }

        @Override
        public void checkAccountDetails() {
            System.out.println("Saving Account Details:");
            System.out.println("Account ID: " + accountId);
            System.out.println("Interest Rate: " + interestRate);
        }

        public void calculateInterest() {
            System.out.println("Calculating interest...");
        }
    }

    // CurrentAccount Class
    public static class CurrentAccount implements Account {
        private int accountId;
        private double overdraftLimit;

        public CurrentAccount(int accountId, double overdraftLimit) {
            this.accountId = accountId;
            this.overdraftLimit = overdraftLimit;
        }

        @Override
        public void checkAccountDetails() {
            System.out.println("Current Account Details:");
            System.out.println("Account ID: " + accountId);
            System.out.println("Overdraft Limit: " + overdraftLimit);
        }

        public void checkOverdraft() {
            System.out.println("Checking overdraft...");
        }
    }

    // Main Method to Test the Implementation
    public static void main(String[] args) {
        ATMImpl atm = new ATMImpl(1, 1001);
        atm.login("123456");
        atm.deposit(1001, 500.0);
        System.out.println("Balance: " + atm.queryBalance(1001));
        atm.withdraw(1001, 200.0);
        System.out.println("Balance: " + atm.queryBalance(1001));
        atm.logout();

        SavingAccount savingAccount = new SavingAccount(2001, 0.03);
        savingAccount.checkAccountDetails();
        savingAccount.calculateInterest();
        savingAccount.accountStatement();

        CurrentAccount currentAccount = new CurrentAccount(3001, 1000.0);
        currentAccount.checkAccountDetails();
        currentAccount.checkOverdraft();
        currentAccount.accountStatement();
    }
}
