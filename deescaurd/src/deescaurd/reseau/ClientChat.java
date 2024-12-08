package deescaurd.reseau;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import deescaurd.Controleur;
import deescaurd.ihm.chat.FrameChat;
import deescaurd.ihm.connexion.FrameJoin;

public class ClientChat
{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String pseudo;
	private Controleur ctrl;

	private FrameChat ihm;

	public ClientChat(String adresseServeur, int port, Controleur ctrl) 
	{
		this.ctrl = ctrl;
		System.out.println("test 1");

		try 
		{
			// Connexion au serveur
			socket = new Socket(adresseServeur, port);
			JOptionPane.showMessageDialog(new JFrame(), "Sucess", "success", JOptionPane.INFORMATION_MESSAGE);

			
			// Initialisation des flux
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


			// Gérer l'authentification (choix du pseudonyme)
			this.authentification();

			// Boucle pour envoyer des messages
			System.out.println("hhh");
			new Thread(new MessageRecu()).start();
			new Thread(new MessageEnvoyee()).start();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(new JFrame(), e, "Erreur", JOptionPane.ERROR_MESSAGE);
			new FrameJoin(this.ctrl);
		} 
		/*finally 
		{
			close();
		}*/
	}

	private void authentification() throws IOException 
	{
		//Scanner scanner = new Scanner(System.in);
		System.out.println(in.readLine()); // Message de bienvenue et demande de pseudonyme
		while (true) 
		{
			pseudo = this.ctrl.getPseudo();
			out.println(pseudo);
			String reponse = in.readLine(); // Réponse du serveur
			System.out.println(reponse);

			if (reponse.substring(0, 9).equals("Bienvenue")) 
			{
				break; 
			}
		}
	}
	
	// s'occupe de l'envois des messages
	/*private void boucle() 
	{
		//Scanner scanner = new Scanner(System.in);
		//System.out.println("Vous pouvez maintenant envoyer des messages (tapez 'quit' pour quitter) : \n /quiestenligne pour voir la liste des connectés");
		//Thread.dumpStack();
	}*/

	private void close() 
	{     
		try 
		{
			if (socket != null) 
			{
				socket.close();
			}
			System.out.println("Connexion fermée.");
		} 
		catch (IOException e) 
		{
			System.err.println("Erreur lors de la fermeture : " + e.getMessage());
		}
	}

	private class MessageRecu implements Runnable
	{
		@Override
		public void run() 
		{
			System.out.println("Reception lancé...");
			try 
			{
				String message;
				while ((message = in.readLine()) != null) 
				{
					ClientChat.this.ctrl.ajouterMessage(message);
				}

				try {new Thread().sleep(200);
				} catch (InterruptedException e) {}
			} 
			catch (IOException e) 
			{
				System.err.println("Connexion au serveur perdue.");
				e.printStackTrace();
			} 
			finally 
			{
				close();
			}
		}
	}

	private class MessageEnvoyee implements Runnable
	{
		@Override
		public void run() 
		{
			System.out.println("Envoi lancé...");
			while (true) 
			{
				//String message = scanner.nextLine();
				String message = ClientChat.this.ctrl.getMessage();
				if(message != null)
				{
					if (message.equalsIgnoreCase("quit")) 
					{
						System.out.println("Déconnexion...");
						System.exit(0);
						break;
					}
					
					out.println(message);
					try {new Thread().sleep(300);
					} catch (InterruptedException e) {}

					ClientChat.this.ctrl.setMessage(null);
				}
				try {new Thread().sleep(200);
				} catch (InterruptedException e) {}
				
				//System.out.println("test 1 : boucle");
			}
		}
	}
}
