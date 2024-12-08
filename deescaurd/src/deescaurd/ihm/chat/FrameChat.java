package deescaurd.ihm.chat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import deescaurd.Controleur;
import deescaurd.metier.Message;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrameChat extends JFrame implements Runnable, ActionListener
{
    private JTextField zoneMessage;
    private JLabel[] lstLabel;
    private JPanel panel;
    private Controleur ctrl;

    public FrameChat(Controleur ctrl)
    {
        this.ctrl = ctrl;

        this.setTitle("main page");
        this.setSize(720, 540);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        this.panel = new JPanel(new GridLayout(Controleur.NB_MAX_MSG, 1, 0, 3));
        this.lstLabel = new JLabel[Controleur.NB_MAX_MSG];
        this.zoneMessage = new JTextField();

        for (int i = 0; i < Controleur.NB_MAX_MSG; i++) 
        {
            this.lstLabel[i] = new JLabel();
            this.lstLabel[i].setOpaque(true);
        
            this.lstLabel[i].setBackground(new Color(128, 128, 128));

            this.panel.add(lstLabel[i]);    
        }

        this.add(this.panel, BorderLayout.CENTER);
        this.add(this.zoneMessage, BorderLayout.SOUTH);

        this.zoneMessage.addActionListener(this);

        this.setVisible(true);
    }

    @Override
    public void run() 
    {
        ArrayList<Message> temp = new ArrayList<Message>();
        while (true) 
        {
            temp = this.ctrl.getMessageRct();

            for (int i = 0; i < temp.size(); i++) 
            {
                if(temp.get(i).getMessage() != null)
                {
                    this.lstLabel[i].setText("[" + temp.get(i).getDateEnvoi() + "] "  + temp.get(i).getMessage());
                }
            }
            
            try {new Thread().sleep(200);
            } catch (Exception e) {}
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.zoneMessage)
        {
            System.out.println(this.zoneMessage.getText());
            this.ctrl.setMessage(this.zoneMessage.getText());
            this.ctrl.ajouterMessage(this.zoneMessage.getText());

            this.zoneMessage.setText("");
            
        }
    }

    public String getMsg()
    {
        return this.zoneMessage.getText();
    }
    
}
