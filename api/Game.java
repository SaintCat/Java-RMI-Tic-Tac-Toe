package api;

import java.rmi.*;

/**
 * Interfaccia dell'oggetto remoto Game e consente di modellare tutte le informazioni
 * relative a una partita tra due player.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 *
 */
public interface Game extends Remote {

	/**
	 * Costante usata per indicare che la cella della matrice e' libera.
	 */
	static final int FREE_CELL = 0;
	
	/**
	 * Costante usata per indicare che la cella della matrice e' stata utilizzata dal player 1.
	 */
	static final int P1_CELL = 1;
	
	/**
	 * Costante usata per indicare che la cella della matrice e' stata utilizzata dal player 2.
	 */
	static final int P2_CELL = 2;
	
	/**
	 * Costante usata per indicare che la dimensione della matrice quadrata.
	 */
	static final int GRID_DIMENSION = 3;
	
	/**
	 * Consente a un player di inviare la mossa effettuata al server. Consente inoltre di:<br/>
	 * - inoltrare le mosse tra i due player; <br/>
	 * - verificare l'eventuale vincita e notificarla; <br/>
	 * - verificare eventuali disconnessioni dei player e notificarle; <br/>
	 * - gestire il turno dei due player.
	 * 
	 * @param p player che invia la mossa.
	 * @param m identificativo della mossa da inviare. E' un intero e deve essere compreso tra 0 e 8.<br/> 
	 *        0 rappresenta la mossa in posizione (0,0);<br/>
	 *        1 rappresenta la mossa in posizione (0,1);<br/>
	 *        2 rappresenta la mossa in posizione (0,2);<br/>
	 *        3 rappresenta la mossa in posizione (1,0);<br/>
	 *        4 rappresenta la mossa in posizione (1,1);<br/>
	 *        5 rappresenta la mossa in posizione (1,2);<br/>
	 *        6 rappresenta la mossa in posizione (2,0);<br/>
	 *        7 rappresenta la mossa in posizione (2,1);<br/>
	 *        8 rappresenta la mossa in posizione (2,2).
	 * @throws RemoteException
	 * 
	 */
	void sendMovement(Player p, int m) throws RemoteException ;
	
}
