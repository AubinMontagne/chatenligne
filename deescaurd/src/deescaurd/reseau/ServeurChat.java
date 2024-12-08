package deescaurd.reseau;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServeurChat {
	private static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

	public static void main(String[] args) throws IOException
	{
		// On lance le serveur
		ServerSocket serveur = new ServerSocket(12345); 
		System.out.println("Serveur de chat démarré sur le port 12345...");

		// Le while maintient notre serveur en vie, il attend les clients
		try 
		{
			while (true) 
			{
				Socket socket = serveur.accept();
				System.out.println("Nouvelle connexion détectée : " + socket.getInetAddress());

				// Créer un thread pour gérer ce client
				new Thread(new GerantDeClient(socket)).start();
			}
		} 
		finally 
		{
			serveur.close();
		}
	}

	// Cette fonction sert à retirer le pseudonyme de l'utilisateur si c'est lui qui envoie le message
	static void envoyerMessage(String message, String pseudoEnvoyeur) 
	{
		for (Map.Entry<String, PrintWriter> entry : clients.entrySet()) {
			String pseudo = entry.getKey();
			PrintWriter out = entry.getValue();
			if (!pseudo.equals(pseudoEnvoyeur)) 
			{
				out.println(message);
			}
		}
	}


	static boolean addClient(String pseudo, PrintWriter out) 
	{
		// synchnonized évite d'avoir 2 fois le même pseudo si les deux threads demandent en même temps, globalement ça synchronise les threads
		if (!clients.containsKey(pseudo)) {
			clients.put(pseudo, out);
			return true;
		}
		return false; 
	}

	static void removeClient(String pseudo) 
	{
			clients.remove(pseudo);
	}

	static void messagePriv(String message, String pseudoDestinataire, String pseudoEnvoyeur) 
	{
		PrintWriter out = clients.get(pseudoDestinataire);
		if (out != null) 
		{

			out.println("Message privé reçu de : "+ pseudoEnvoyeur +" : " + message);
		}
	}

	static void voirConnectes (String pseudoEnvoyeur)
	{
		PrintWriter out = clients.get(pseudoEnvoyeur);
		if (out !=null) 
		{
			clients.forEach((key, value) -> 
			{
				out.println(key);
			});

		}
	}
	

	private static class GerantDeClient implements Runnable 
	{
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		private String pseudo;

		public GerantDeClient(Socket socket) 
		{
            this.socket = socket;
		}

		public String getPseudo()
		{
			return this.pseudo;
		}

		public void choixPseudo()
		{
			out.println("Bienvenue sur le chat ! Veuillez choisir un pseudonyme : ");
				try {
					pseudo = in.readLine();
				} catch (Exception e) {}

				if (pseudo == null) 
				{
					return;
				}

				if (ServeurChat.addClient(pseudo, out)) 
				{
					out.println("Bienvenue, " + pseudo + " !");
				} 
				else 
				{
					out.println("Ce pseudonyme est déjà utilisé. Veuillez en choisir un autre : ");
				}
		
		}

		@Override
		public void run() 
		{
			try 
			{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// Demander un pseudonyme au client
				choixPseudo();

				System.out.println(pseudo + " s'est connecté.");
				ServeurChat.envoyerMessage(pseudo + " a rejoint le chat.", pseudo);

				// Gestion des messages du client
				String message;
				while ((message = in.readLine()) != null) 
				{
					System.out.println("Message reçu de " + pseudo + " : " + message);
					
					// à mettre en case quand j'aurai bcp de fonctionnalités , variables à
					int espace = message.indexOf(' ');
					if (message.startsWith("@"))
					{
						if (espace > 1) 
						{
							String pseudoDestinataire = message.substring(1, espace); 
							String messagePrive = message.substring(espace + 1); 
							String envoyeur = this.getPseudo();
							ServeurChat.messagePriv(messagePrive,pseudoDestinataire,envoyeur);	
						}
						
					}
					else if (message.startsWith("/quiestenligne"))
					{
						
						System.out.println("test");
						
						ServeurChat.voirConnectes(this.getPseudo());
					}
					else 
					{
						ServeurChat.envoyerMessage(pseudo + " : " + message, pseudo);
					}
				}
			}
			catch (IOException e) 
			{
				System.err.println("Erreur avec le client " + pseudo + " : " + e.getMessage());
			} 
			finally 
			{
				// Déconnexion du client
				if (pseudo != null) 
				{
					System.out.println(pseudo + " s'est déconnecté.");
					ServeurChat.removeClient(pseudo);
					ServeurChat.envoyerMessage(pseudo + " a quitté le chat.", pseudo);
				}
				try 
				{
					socket.close();
				} 
				catch (IOException e) 
				{
					System.err.println("Erreur lors de la fermeture du socket pour " + pseudo + " : " + e.getMessage());
				}
			}
		}
	}
}
