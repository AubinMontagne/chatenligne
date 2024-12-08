package deescaurd.ihm.pseudo;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import deescaurd.Controleur;

public class FramePseudo extends JFrame implements ActionListener
{
    private JTextField pseudoField;
    private JButton button;
    private Controleur ctrl;

    public FramePseudo(Controleur ctrl)
    {
        this.ctrl = ctrl;

        this.setSize(400, 150);
        this.setTitle("deescaurd");
        this.setLayout(new GridLayout(2, 2));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pseudoField = new JTextField();
        this.button = new JButton("valider");

        this.add(new JLabel("Pseudo : ", JLabel.CENTER));
        this.add(this.pseudoField);
        this.add(new JLabel("  "));
        this.add(this.button);

        this.button.addActionListener(this);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == this.button)
        {
            if(!this.pseudoField.getText().isEmpty())
            {
                this.ctrl.setPseudo(this.pseudoField.getText());
                this.setVisible(false);
                this.ctrl.joinChannel();
            }
        }
    }

    public static void main(String[] args) {
        new FramePseudo(null);
    }
}
