package fi.academy.Error;

import java.util.Date;

public class ErrorPost {
    private final Date timestamp = new Date();
    private String message;
    private int status;

    public ErrorPost(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
