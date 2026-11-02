import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String location = "test.txt";
        String txnLocation = "transactions.txt";
        double balance = 0;
        boolean running = true;
        int choice;

        balance = checkBalance(location);

        while(running){
            System.out.println("\nBanking help");
            System.out.println("1. Show Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.println("6. Reset balance");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer newline

            switch(choice){
                case 1 -> showBalance(balance);
                case 2 -> balance = handleDeposit(scanner, balance, txnLocation);
                case 3 -> balance = handleWithdraw(scanner, balance, txnLocation);
                case 4 -> readTransactions(txnLocation);
                case 5 -> running = exit(balance, location);
                case 6 -> balance = 0;
                default -> System.out.println("Invalid choice");
            }
        }
        System.out.println("Have a nice day!");
        scanner.close();
    }

    static void showBalance(double balance){
        System.out.printf("\nBalance: $%.2f\n", balance);
    }

    static double handleDeposit(Scanner scanner, double balance, String txnLocation) {
        System.out.print("Enter an amount to be deposited: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer

        if (amount < 0) {
            System.out.println("Amount can't be negative");
            return balance;
        }

        System.out.print("Enter a title/label for this deposit (e.g., Paycheck): ");
        String title = scanner.nextLine();

        double newBalance = balance + amount;
        logTransaction(txnLocation, "Deposit", title, amount, newBalance);
        return newBalance;
    }

    static double handleWithdraw(Scanner scanner, double balance, String txnLocation) {
        System.out.print("Enter amount to be withdrawn: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer

        if (amount > balance) {
            System.out.println("Sorry, insufficient funds.");
            return balance;
        } else if (amount < 0) {
            System.out.println("Amount can't be negative");
            return balance;
        }

        System.out.print("Enter a title/label for this withdrawal (e.g., Groceries): ");
        String title = scanner.nextLine();

        double newBalance = balance - amount;
        logTransaction(txnLocation, "Withdrawal", title, amount, newBalance);
        return newBalance;
    }

    public static void logTransaction(String location, String type, String title, double amount, double postBalance) {
        // Using true in FileWriter enables appending to the file instead of overwriting it
        try (FileWriter writer = new FileWriter(location, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            out.printf("%s | Title: %s | Amount: $%.2f | Balance After: $%.2f\n", type, title, amount, postBalance);
        } catch (IOException e) {
            System.out.println("Could not log transaction.");
        }
    }

    public static void readTransactions(String location) {
        System.out.println("\nTransaction History");
        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            String line;
            boolean hasHistory = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hasHistory = true;
            }
            if (!hasHistory) {
                System.out.println("No transactions found.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No transaction history file found yet.");
        } catch (IOException e) {
            System.out.println("Something went wrong reading history.");
        }
    }

    public static double checkBalance(String loc){
        String line;
        double balance = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(loc))){
            while((line = reader.readLine()) != null){
                balance = Double.valueOf(line);
                return balance;
            }
        }
        catch(FileNotFoundException e){
            System.out.println("Couldn't locate balance file, creating a new one");
            writeBalance(balance, loc);
            return 0;
        }
        catch(IOException e) {
            System.out.println("Something went wrong");
            return -1;
        }
        return balance;
    }

    public static void writeBalance(double info, String location){
        try(FileWriter writer = new FileWriter(location)){
            writer.write(String.valueOf(info));
        }
        catch(IOException e){
            System.out.println("Something went wrong");
        }
    }

    public static boolean exit(double balance, String location){
        writeBalance(balance, location);
        return false;
    }
}
