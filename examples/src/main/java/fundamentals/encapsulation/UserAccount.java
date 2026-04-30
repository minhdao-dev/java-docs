package fundamentals.encapsulation;

import java.util.Objects;

public final class UserAccount {

    private final String username;
    private String       email;
    private int          loginCount;
    private boolean      active;

    public UserAccount(String username, String email) {
        this.username   = validateUsername(username);
        this.email      = validateEmail(email);
        this.loginCount = 0;
        this.active     = true;
    }

    private static String validateUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username must not be blank");
        if (username.length() < 3)
            throw new IllegalArgumentException("Username too short: " + username);
        return username.toLowerCase().trim();
    }

    private static String validateEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email: " + email);
        return email.toLowerCase().trim();
    }

    public String  getUsername()   { return username; }
    public String  getEmail()      { return email; }
    public int     getLoginCount() { return loginCount; }
    public boolean isActive()      { return active; }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public void recordLogin() {
        if (!active) throw new IllegalStateException("Account is deactivated");
        loginCount++;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "UserAccount{username='" + username + "', email='" + email
               + "', logins=" + loginCount + ", active=" + active + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount u)) return false;
        return username.equals(u.username);
    }

    @Override
    public int hashCode() { return Objects.hash(username); }

    public static void main(String[] args) {
        UserAccount alice = new UserAccount("Alice_Dev", "alice@example.com");
        System.out.println(alice);

        alice.recordLogin();
        alice.recordLogin();
        System.out.println(alice.getLoginCount()); // 2

        alice.setEmail("alice@newdomain.com");

        alice.deactivate();
        try {
            alice.recordLogin();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage()); // Account is deactivated
        }
    }
}
