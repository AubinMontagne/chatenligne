import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientChat 
{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String pseudo;

	public ClientChat(String adresseServeur, int port) 
	{
		try 
		{
			// Connexion au serveur
			socket = new Socket(adresseServeur, port);
			System.out.println("Connecté au serveur de chat à " + adresseServeur + ":" + port);

			// Initialisation des flux
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Gérer l'authentification (choix du pseudonyme)
			this.authentification();
 
			// Démarrer un thread pour lire les messages du serveur
			new Thread(new MessageRecu()).start();

			// Boucle pour envoyer des messages
			this.boucle();
		} 
		catch (IOException e) 
		{
			System.err.println("Erreur de connexion : " + e.getMessage());
		} 
		finally 
		{
			close();
		}
	}

	private void authentification() throws IOException 
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println(in.readLine()); // Message de bienvenue et demande de pseudonyme
		while (true) 
		{
			pseudo = scanner.nextLine();
			out.println(pseudo);
			String reponse = in.readLine(); // Réponse du serveur
			System.out.println(reponse);
			if (reponse.startsWith("Bienvenue")) 
			{
				break; 
			}
		}
	}
	
	private void boucle() 
	{
		Scanner scanner = new Scanner(System.in); // La classe fait par l'iut et utilisable
		System.out.println("Vous pouvez maintenant envoyer des messages (tapez 'quit' pour quitter) : \n /quiestenligne pour voir la liste des connectés");
		while (true) 
		{
			String message = scanner.nextLine();
			if (message.equalsIgnoreCase("quit")) 
			{
				System.out.println("Déconnexion...");
				break;
			}
			out.println(message);
		}
	}

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

	// Classe interne pour lire les messages du serveur
	private class MessageRecu implements Runnable 
	{
		@Override
		public void run() 
		{
			try 
			{
				String message;
				while ((message = in.readLine()) != null) 
				{
					System.out.println(message);
				}
			} 
			catch (IOException e) 
			{
				System.err.println("Connexion au serveur perdue.");
			} 
			finally 
			{
				close();
			}
		}
	}

	public static void main(String[] args) 
	{
		if (args.length != 2)
		{
			System.out.println("Usage : java ClientChat <adresse_serveur> <port_serveur>");
			return;
		}
		String serveurAdresse = args[0];
		int port = Integer.parseInt(args[1]);
		new ClientChat(serveurAdresse, port);
	}
}
