package fundamentals.oop;

import java.util.Objects;

/**
 * Demonstrates core OOP concepts: class anatomy (fields, methods, static vs instance),
 * the {@code this} keyword, multiple references to the same object, and the contract
 * between {@code toString()}, {@code equals()}, and {@code hashCode()}.
 *
 * <p>Lesson 11 — OOP: Class and Object
 * (<a href="https://github.com/minhdao-dev/java-docs/blob/main/docs/java/fundamentals/11-oop-class-object.en.md">docs/java/fundamentals/11-oop-class-object.en.md</a>)
 */
public class OopDemo {

    // ── BankAccount — main demo class ────────────────────────────────────────

    static class BankAccount {

        private static int totalAccounts = 0; // static: shared, lives in Metaspace

        private final String accountId;
        private final String owner;
        private double balance;

        BankAccount(String owner, double initialBalance) {
            this.owner     = owner;
            this.balance   = initialBalance;
            this.accountId = "ACC-" + (++totalAccounts);
        }

        void deposit(double amount) {
            if (amount <= 0) return;
            balance += amount;
        }

        boolean withdraw(double amount) {
            if (amount <= 0 || amount > balance) return false;
            balance -= amount;
            return true;
        }

        double getBalance() { return balance; }

        static int getTotalAccounts() { return totalAccounts; }

        @Override
        public String toString() {
            return accountId + " [" + owner + "] $" + String.format("%.2f", balance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BankAccount other)) return false;
            return accountId.equals(other.accountId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountId);
        }
    }

    // ── Counter — shows static vs instance ───────────────────────────────────

    static class Counter {
        static int totalCreated = 0; // shared across all instances
        int count = 0;               // each object has its own copy

        Counter() { totalCreated++; }

        void increment() { count++; }
    }

    // ── main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {

        // ── 1. Class anatomy & toString() ────────────────────────────────────
        System.out.println("=== 1. BankAccount — toString() ===");
        BankAccount alice = new BankAccount("Alice", 1000);
        BankAccount bob   = new BankAccount("Bob",   500);

        alice.deposit(200);
        bob.withdraw(100);

        System.out.println(alice);                           // ACC-1 [Alice] $1200.00
        System.out.println(bob);                             // ACC-2 [Bob] $400.00

        // ── 2. Static vs instance ─────────────────────────────────────────────
        System.out.println("\n=== 2. Static vs Instance ===");
        System.out.println("BankAccount.getTotalAccounts() = " + BankAccount.getTotalAccounts()); // 2

        Counter c1 = new Counter();
        Counter c2 = new Counter();
        c1.increment();
        c1.increment();
        System.out.println("c1.count = " + c1.count);              // 2 — own copy
        System.out.println("c2.count = " + c2.count);              // 0 — own copy, unaffected
        System.out.println("Counter.totalCreated = " + Counter.totalCreated); // 2 — shared

        // ── 3. Multiple references, one object ───────────────────────────────
        System.out.println("\n=== 3. Multiple References ===");
        BankAccount ref = alice;                    // ref points to SAME object, not a copy
        ref.deposit(500);
        System.out.println("alice.getBalance() = " + alice.getBalance()); // 1700.0 — same object
        System.out.println("alice == ref: " + (alice == ref));            // true — same address

        // ── 4. equals() and hashCode() ───────────────────────────────────────
        System.out.println("\n=== 4. equals() and hashCode() ===");
        BankAccount alice2 = alice;                 // same reference
        BankAccount other  = new BankAccount("Alice", 9999); // different object, different accountId

        System.out.println("alice == alice2:      " + (alice == alice2));       // true
        System.out.println("alice.equals(alice2): " + alice.equals(alice2));    // true
        System.out.println("alice == other:       " + (alice == other));        // false
        System.out.println("alice.equals(other):  " + alice.equals(other));     // false — different accountId
        System.out.println("hashCode consistent:  " + (alice.hashCode() == alice2.hashCode())); // true
    }
}
