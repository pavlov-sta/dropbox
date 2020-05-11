package common;

public class AuthMessage extends AbstractMessage {
    private  boolean status;

    public boolean isStatus() {
        return status;
    }

    public AuthMessage(boolean status) {
        this.status = status;
    }
}
