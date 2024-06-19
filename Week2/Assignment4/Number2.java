class BankAccount {
    private int balance;

    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited " + amount + ", new balance is " + balance);
    }

    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrew " + amount + ", new balance is " + balance);
        } else {
            System.out.println("Failed to withdraw " + amount + ", balance is " + balance);
        }
    }

    public int getBalance() {
        return balance;
    }
}

public class Number2 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();

        Runnable depositTask = () -> {
            for (int i = 0; i < 10; i++) {
                account.deposit(100);
            }
        };

        Runnable withdrawTask = () -> {
            for (int i = 0; i < 10; i++) {
                account.withdraw(50);
            }
        };

        Thread depositThread1 = new Thread(depositTask);
        Thread depositThread2 = new Thread(depositTask);
        Thread withdrawThread1 = new Thread(withdrawTask);
        Thread withdrawThread2 = new Thread(withdrawTask);

        depositThread1.start();
        depositThread2.start();
        withdrawThread1.start();
        withdrawThread2.start();

        try {
            depositThread1.join();
            depositThread2.join();
            withdrawThread1.join();
            withdrawThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final balance is " + account.getBalance());
    }
}
