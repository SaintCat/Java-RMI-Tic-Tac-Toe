package server;

import java.net.*;
import java.rmi.*;

import api.*;

/**
 * Questa classe contiene il metodo main per far partire l'applicazione lato server.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 */
public class StartServer {

	/**
	 * Main dell'applicazione lato server.
	 */
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		TicTacToeServer server = new TicTacToeServerImpl();
		Naming.rebind("tictactoe", server);
	}

}
