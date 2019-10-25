package common;

public class UserConnect extends AbstractMessage {
    private String username;
    private String passwords;

    public String getUsername() {
        return username;
    }

    public String getPasswords() {
        return passwords;
    }

    public UserConnect(String username, String passwords) {
        this.username = username;
        this.passwords = passwords;
    }
}
