package deescaurd;
import java.util.ArrayList;

import deescaurd.ihm.chat.FrameChat;
import deescaurd.ihm.connexion.FrameJoin;
import deescaurd.metier.Message;
import deescaurd.reseau.ClientChat;

public class Controleur 
{
    private FrameJoin ihm;
    private FrameChat trueIhm;
    private ClientChat cli;
    private String pseudo;
    private String hostname;
    private int port;

    private String message;
    private ArrayList<Message> messageRct;

    public static final int NB_MAX_MSG = 10;

    public Controleur()
    {
        this.messageRct = new ArrayList<Message>();
        this.ihm = new FrameJoin(this);
    }

    public Exception joinChannel()
    {
        try {

            this.cli = new ClientChat(this.hostname, this.port, this);

            this.trueIhm = new FrameChat(this);
            Thread th1 = new Thread(this.trueIhm);
            th1.start();

        } catch (Exception e) {
            return e;
        }
        return null;
    }

    public void ajouterMessage(String msg)
    {
        Message msg2 = new Message(msg, this.pseudo);
        if (this.messageRct.size() < Controleur.NB_MAX_MSG)
        {
            this.messageRct.add(msg2);
        }
        else
        {
            this.messageRct.remove(0);
            this.messageRct.add(msg2);
        }
    }

    public String getPseudo() 
    {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<Message> getMessageRct() {
        return messageRct;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void main(String[] args) {
        new Controleur();
    }
}
