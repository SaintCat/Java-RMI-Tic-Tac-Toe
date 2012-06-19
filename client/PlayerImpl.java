package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import api.*;

/**
 * Rappresenta l'implementazione dell'oggetto remoto {@link Player} e incapsula
 * tutte le informazioni di sessione utili per identificare un player. Inoltre
 * questa classe implementa l'interfaccia {@link Subject} dal momento che si
 * vuole informare la classe {@link TicTacToeClient} su eventuali cambiamenti di
 * stato.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see api.Player
 */
public class PlayerImpl extends UnicastRemoteObject implements Player, Subject {

	// Attributi privati.
	private static final long serialVersionUID = 1L;
	private String name;
	private Game game;
	private int foeSelectedMove;
	private List<Watcher> wrs;
	private Event event;

	/**
	 * Costruttore della classe PlayerImpl.
	 * 
	 * @throws RemoteException
	 */
	public PlayerImpl() throws RemoteException {
		wrs = new LinkedList<Watcher>();
	}

	// Setter e getter della classe.

	/**
	 * Il metodo restituisce il nickname del giocatore.
	 * 
	 * @return nickname del giocatore.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Il metodo assegna il nickname del giocatore.
	 * 
	 * @param name nickname del giocatore.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Il metodo restituisce la partita a cui partecipa il giocatore.
	 * 
	 * @return partita a cui partecipa il giocatore.
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Il metodo assegna la partita <code>g</code> al giocatore.
	 * 
	 * @param g parita a cui il giocatore partecipa.
	 */
	public void setGame(Game g) {
		this.game = g;
	}

	/**
	 * Il metodo restituisce la mossa effettuata dal giocatore avversario.
	 * 
	 * @return mossa effettuata dall'avversario.
	 */
	public int getFoeSelectedMove() {
		return foeSelectedMove;
	}

	/**
	 * Il metodo assegna la mossa effettuata dal giocatore avversario.
	 * 
	 * @param foeSelectedMove mossa effettuata dall'avversario.
	 */
	public void setFoeSelectedMove(int foeSelectedMove) {
		this.foeSelectedMove = foeSelectedMove;
	}

	/**
	 * Il metodo restituisce l'evento verificatosi sul giocatore.
	 *
	 * @return evento verificatosi sul giocatore.
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Il metodo associa al giocatore l'evento verificatosi.
	 * 
	 * @param event evento che si e' verificato.
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	// Metodi ridefiniti dell'interfaccia Player.

	/**
	 * Consente al giocatore di notificare l'avventa iscrizione alla partita
	 * <code>g</code>.
	 * 
	 * @param g partita a cui il giocatore e' stato aggiunto.
	 * @throws RemoteException
	 */
	@Override
	public void joinGame(Game g) {
		game = g;
		event = Event.MATCH_STARTED;
		sendNotification();
	}

	/**
	 * Il metodo notifica che e' il turno del giocatore corrente.
	 * 
	 * @throws RemoteException
	 */
	@Override
	public void isYourTurn() {
		event = Event.YOUR_TURN;
		sendNotification();

	}

	/**
	 * Il metodo notifica al player che e' il turno del giocatore avversario.
	 * 
	 * @throws RemoteException
	 */
	@Override
	public void foeTurn() {
		event = Event.FOE_TURN;
		sendNotification();
	}

	/**
	 * Il metodo consente di testare la connessione di un giocatore, per
	 * rilevare eventuali disconnessioni.
	 * 
	 * @throws RemoteException
	 */
	@Override
	public void ping() {
		// Metodo vuoto.
	}

	/**
	 * Il metodo notifica al giocatore che il suo avversario si e' disconnesso.
	 * 
	 * @throws RemoteException
	 */
	@Override
	public void foeDisconnected() {
		event = Event.FOE_DISCONNECTED;
		sendNotification();
	}

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
	@Override
	public void foeHasMoved(int m) {
		foeSelectedMove = m;
		event = Event.FOE_MOVED;
		sendNotification();
	}


	/**
	 * Il metodo consente di inviare il risultato finale della partita al
	 * giocatore.
	 * 
	 * @param res YOU_WIN: hai vinto;<br/>
	 * 			  YOU_LOSE: hai perso;<br/>
	 * 			  DRAW: hai pareggiato.
	 * @throws RemoteException
	 */
	@Override
	public void sendGameResult(Event res) {
		event = res;
		sendNotification();
	}

	// Metodi ridefiniti dell'interfaccia Subject.

	/**
	 * Consente di aggiungere un osservatore al subject.
	 * 
	 * @param aWatcher osservatore da aggiungere.
	 */
	@Override
	public void add(Watcher aWatcher) {
		wrs.add(aWatcher);
	}

	/**
	 * Consente di rimuovere un osservatore che era stato precedentemente
	 * aggiunto.
	 * 
	 * @param aWatcher osservatore da rimuovere.
	 */
	@Override
	public void remove(Watcher aWatcher) {
		wrs.remove(aWatcher);
	}

	/**
	 * Consente di notificare il cambiamento di stato del subject a tutti gli
	 * osservatori aggiunti.
	 */
	@Override
	public void sendNotification() {
		for (Watcher w : wrs)
			w.update();
	}

}