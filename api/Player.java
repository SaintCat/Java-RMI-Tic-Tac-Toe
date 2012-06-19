package api;

import java.rmi.*;

/**
 * Interfaccia dell'oggetto di callback Player. Consente al server di inviare al
 * client tutte le notifiche necessarie per il corretto svolgimento della
 * partita.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * 
 */
public interface Player extends Remote {
	
	/**
	 * Consente al giocatore di notificare l'avventa iscrizione alla partita
	 * <code>g</code>.
	 * 
	 * @param g partita a cui il giocatore e' stato aggiunto.
	 * @throws RemoteException
	 */
	void joinGame(Game g) throws RemoteException;

	/**
	 * Il metodo notifica che e' il turno del giocatore corrente.
	 * 
	 * @throws RemoteException
	 */
	void isYourTurn() throws RemoteException;

	/**
	 * Il metodo notifica al player che e' il turno del giocatore avversario.
	 * 
	 * @throws RemoteException
	 */
	void foeTurn() throws RemoteException;

	/**
	 * Il metodo consente di testare la connessione di un giocatore, per
	 * rilevare eventuali disconnessioni.
	 * 
	 * @throws RemoteException
	 */
	void ping() throws RemoteException;

	/**
	 * Il metodo notifica al giocatore che il suo avversario si e' disconnesso.
	 * 
	 * @throws RemoteException
	 */
	void foeDisconnected() throws RemoteException;

	/**
	 * Il metodo notifica al giocatore che il suo avversario ha effettuato la
	 * mossa.
	 * 
	 * @param m   identificativo della mossa effettuata. Si tratta di un intero
	 *            e deve essere compreso tra 0 e 8.<br/>
	 *            0 rappresenta la mossa in posizione (0,0);<br/>
	 *            1 rappresenta la mossa in posizione (0,1);<br/>
	 *            2 rappresenta la mossa in posizione (0,2);<br/>
	 *            3 rappresenta la mossa in posizione (1,0);<br/>
	 *            4 rappresenta la mossa in posizione (1,1);<br/>
	 *            5 rappresenta la mossa in posizione (1,2);<br/>
	 *            6 rappresenta la mossa in posizione (2,0);<br/>
	 *            7 rappresenta la mossa in posizione (2,1);<br/>
	 *            8 rappresenta la mossa in posizione (2,2).
	 * @throws RemoteException
	 */
	void foeHasMoved(int m) throws RemoteException;
	
	/**
	 * Il metodo consente di inviare il risultato finale della partita al
	 * giocatore.
	 * 
	 * @param res YOU_WIN: hai vinto;<br/>
	 * 			  YOU_LOSE: hai perso;<br/>
	 * 			  DRAW: hai pareggiato.
	 * @throws RemoteException
	 */
	void sendGameResult(Event res) throws RemoteException;
}
