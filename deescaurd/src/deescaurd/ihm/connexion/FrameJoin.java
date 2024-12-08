package deescaurd.ihm.connexion;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import deescaurd.Controleur;
import deescaurd.ihm.pseudo.FramePseudo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameJoin extends JFrame implements ActionListener
{
    private Controleur ctrl;
    private JPanel panel;
    private JPanel panelBas;
    private JTextField portField;
    private JTextField hostnameField;
    private JButton validButton;

    public FrameJoin(Controleur ctrl)
    {
        this.ctrl = ctrl;

        this.setSize(400, 140);
        this.setTitle("Deescaurd");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.panel = new JPanel();

        this.panel.setLayout(new GridLayout(2, 2));

        this.hostnameField = new JTextField();
        this.portField = new JTextField();

        this.panel.add(new JLabel("Hostname : ", JLabel.RIGHT));
        this.panel.add(this.hostnameField);
        this.panel.add(new JLabel("Port : ", JLabel.RIGHT));
        this.panel.add(this.portField);

        this.panelBas = new JPanel();

        this.validButton = new JButton("Entrer");

        this.panelBas.setLayout(new GridLayout(1, 3));
        this.panelBas.add(new JLabel("  "));
        this.panelBas.add(this.validButton);
        this.panelBas.add(new JLabel("  "));

        this.validButton.addActionListener(this);

        this.add(this.panel, BorderLayout.CENTER);
        this.add(this.panelBas, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == this.validButton)
        {
            if(!(this.hostnameField.getText().isEmpty()) && 
               !(this.portField.getText().isEmpty()))
            {
                this.ctrl.setHostname(this.hostnameField.getText());
                this.ctrl.setPort(Integer.parseInt(this.portField.getText()));
                this.setVisible(false);
                new FramePseudo(this.ctrl);
            }
        }    
    }
}
