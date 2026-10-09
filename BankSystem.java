import java.util.*;

public class BankSystem {
    static class Account {
        String id, owner;
        double balance;
        List<String> history = new ArrayList<>();

        Account(String id, String owner, double initial) {
            this.id = id; this.owner = owner; this.balance = initial;
            history.add(String.format("Account opened with $%.2f", initial));
        }

        void deposit(double amt) {
            balance += amt;
            history.add(String.format("Deposited $%.2f | Balance: $%.2f", amt, balance));
        }

        boolean withdraw(double amt) {
            if (amt > balance) return false;
            balance -= amt;
            history.add(String.format("Withdrew $%.2f | Balance: $%.2f", amt, balance));
            return true;
        }

        public String toString() {
            return String.format("ID: %s | Owner: %-12s | Balance: $%.2f", id, owner, balance);
        }
    }

    static Map<String, Account> accounts = new HashMap<>();
    static Scanner sc = new Scanner(System.in);
    static int nextId = 1001;

    static void createAccount() {
        System.out.print("Your name: "); String name = sc.nextLine().trim();
        System.out.print("Initial deposit ($): ");
        try {
            double dep = Double.parseDouble(sc.nextLine().trim());
            String id = "ACC" + nextId++;
            accounts.put(id, new Account(id, name, dep));
            System.out.println("✅ Account created! Your ID: " + id);
        } catch (NumberFormatException e) { System.out.println("Invalid amount."); }
    }

    static Account getAccount() {
        System.out.print("Account ID: "); String id = sc.nextLine().trim();
        Account a = accounts.get(id);
        if (a == null) System.out.println("Account not found.");
        return a;
    }

    static void deposit() {
        Account a = getAccount(); if (a == null) return;
        System.out.print("Amount to deposit: ");
        try { double amt = Double.parseDouble(sc.nextLine().trim()); a.deposit(amt); System.out.printf("✅ Deposited $%.2f%n", amt); }
        catch (NumberFormatException e) { System.out.println("Invalid amount."); }
    }

    static void withdraw() {
        Account a = getAccount(); if (a == null) return;
        System.out.print("Amount to withdraw: ");
        try {
            double amt = Double.parseDouble(sc.nextLine().trim());
            if (a.withdraw(amt)) System.out.printf("✅ Withdrew $%.2f%n", amt);
            else System.out.println("❌ Insufficient funds.");
        } catch (NumberFormatException e) { System.out.println("Invalid amount."); }
    }

    static void transfer() {
        System.out.print("From Account ID: "); String fromId = sc.nextLine().trim();
        System.out.print("To Account ID: "); String toId = sc.nextLine().trim();
        Account from = accounts.get(fromId), to = accounts.get(toId);
        if (from == null || to == null) { System.out.println("Invalid account(s)."); return; }
        System.out.print("Amount to transfer: ");
        try {
            double amt = Double.parseDouble(sc.nextLine().trim());
            if (from.withdraw(amt)) { to.deposit(amt); System.out.printf("✅ Transferred $%.2f from %s to %s%n", amt, from.owner, to.owner); }
            else System.out.println("❌ Insufficient funds.");
        } catch (NumberFormatException e) { System.out.println("Invalid amount."); }
    }

    static void history() {
        Account a = getAccount(); if (a == null) return;
        System.out.println("
Transaction History for " + a.owner + ":");
        for (int i = 0; i < a.history.size(); i++)
            System.out.println("  " + (i+1) + ". " + a.history.get(i));
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Java Bank System ===");
        System.out.println("Commands: create | deposit | withdraw | transfer | history | list | quit
");
        while (true) {
            System.out.print("> ");
            String cmd = sc.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "create": createAccount(); break;
                case "deposit": deposit(); break;
                case "withdraw": withdraw(); break;
                case "transfer": transfer(); break;
                case "history": history(); break;
                case "list": accounts.values().forEach(a -> System.out.println("  " + a)); break;
                case "quit": System.out.println("Goodbye!"); return;
                default: System.out.println("Unknown command.");
            }
        }
    }
}
