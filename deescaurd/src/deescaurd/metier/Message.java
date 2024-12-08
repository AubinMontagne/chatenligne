package deescaurd.metier;

import java.util.Calendar;
import java.util.Date;

public class Message 
{
    private final Date dateEnvoi;
    private String message;
    private String sender;

    public Message(String message, String sender)
    {
        this.dateEnvoi = Calendar.getInstance().getTime();
        this.message = message;
        this.sender = sender;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
