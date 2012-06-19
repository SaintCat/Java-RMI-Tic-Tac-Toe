package client;

import api.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

/**
 * E' la classe che si interfaccia direttamente con il server.Fornisce tutti i
 * metodi utili a implementare tutte le funzionalita' dell'applicazione lato
 * client:<br/>
 * registrare o deregistrare un player<br/>
 * creare, aggiungersi o rimuovere una partita,<br/>
 * inviare e ricevere le mosse effettutate dai player <br/>
 * visualizzare la lista della partita in attesa di uno sfidante.<br/>
 * 
 * Questa classe registra tutti i cambiamenti di stato della classe
 * {@link api.Player}, trasmettendoli direttamente alla classe che si occupa
 * della gestione della grafica del gioco {@link client.gui.GUIController}.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * 
 */
public class TicTacToeClient implements Watcher,Subject {

	// Attributi ptivati.
	private static final String IP_SERVER = "localhost";
	private Game g;
	private PlayerImpl p;
	private int grid[][];
	private List<Watcher> wrs;
	private Event state;
	private TicTacToeServer server;
	private boolean creator;
	private String idGameCreated;
	private String idGameJoined;

	/**
	 * Costruttore di classe. Inizializza la griglia e si collega al server.
	 * 
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public TicTacToeClient() throws MalformedURLException, RemoteException, NotBoundException{
		idGameCreated="";
		idGameJoined="";
		grid = new int[Game.GRID_DIMENSION][Game.GRID_DIMENSION];
		for (int i=0; i< Game.GRID_DIMENSION; i++)
			for (int j=0;j<Game.GRID_DIMENSION;j++)
				grid[i][j] = Game.FREE_CELL;
		wrs = new LinkedList<Watcher>();
		server = (TicTacToeServer)Naming.lookup("rmi://"+IP_SERVER+"/tictactoe");
	}

	/**
	 * Consente di inviare al server la mossa effettuata dal player.
	 * 
	 * @param m identificativo della mossa da inviare.
	 * 		  E' un intero e deve essere compreso tra 0 e 8. 
	 *        0 rappresenta la mossa in posizione (0,0);
	 *        1 rappresenta la mossa in posizione (0,1);
	 *        2 rappresenta la mossa in posizione (0,2);
	 *        3 rappresenta la mossa in posizione (1,0);
	 *        4 rappresenta la mossa in posizione (1,1);
	 *        5 rappresenta la mossa in posizione (1,2);
	 *        6 rappresenta la mossa in posizione (2,0);
	 *        7 rappresenta la mossa in posizione (2,1);
	 *        8 rappresenta la mossa in posizione (2,2).
	 * @throws RemoteException
	 */
	public void sendMove(int m) throws RemoteException{
		g.sendMovement(p, m);
		grid[m/Game.GRID_DIMENSION][m%Game.GRID_DIMENSION]=Game.P1_CELL;

	}

	/**
	 * Resetta la griglia dopo la fine di una partita.
	 */
	public void resetGrid() {
		for (int i = 0; i < Game.GRID_DIMENSION; i++)
			for (int j = 0; j < Game.GRID_DIMENSION; j++)
				grid[i][j] = Game.FREE_CELL;
	}

	/**
	 * Consente di ricevere la mossa effettuata dall'avversario nella partita
	 * corrente.
	 */
	public void receiveMove() {
		int m = p.getFoeSelectedMove();
		grid[m / Game.GRID_DIMENSION][m % Game.GRID_DIMENSION] = Game.P2_CELL;
	}
	
	/**
	 * Consente a un player di creare una nuova partita.
	 * 
	 * @param idGame id della partita da creare.
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void createGame(String idGame) throws MalformedURLException, RemoteException, NotBoundException{
		idGameCreated=idGame;
		creator=true;
		server.createGame(p, idGame);
	}
	/**
	 * Consente a un player di aggiungersi a una partita creata precedentemente da un altro player.
	 * 
	 * @param idGame id della partita in cui aggiungersi.
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws GameJoiningException se la partita non e' piu' disponibile perche' e' stata cancellata
	 *                              oppure e' gia' completa
	 */
	public void joinGame(String idGame) throws MalformedURLException, RemoteException, NotBoundException, GameJoiningException{
		idGameJoined=idGame;
		creator=false;
		server.joinGame(p, idGame);
	}

	/**
	 * Consente a un player di registrarsi presso il server. Il nickname deve
	 * essere univoco nel sistema fino alla deregistrazione del player.
	 * 
	 * @param nickname
	 *            nome con cui registrarsi.
	 * @return true se la registrazione e' andata a buon fine, false altrimenti
	 * @throws RemoteException
	 */
	public boolean registerPlayer(String nickname) throws RemoteException {
		p = new PlayerImpl();
		p.setName(nickname);
		p.add(this);// mi aggiungo come osservatore di p
		return server.register(p, nickname);
	}
	
	/**
	 * Deregistra un player dal server, rendendo nuovamente disponibile il nickname ad altri player.
	 * 
	 * @param nickname nome del player da deregistrare
	 */
	public void deregisterPlayer(String nickname) {
		try {
			 server.deregister(nickname);
		} catch (RemoteException e) {
			System.out.println("Impossible to logout."+e.getMessage());

		}
	}

	/**
	 * Riceve la lista delle partite create che sono in attesa di uno sfidante.
	 * 
	 * @return lista delle partite
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public ArrayList<String> receiveList() throws MalformedURLException,
			RemoteException, NotBoundException {
		return (ArrayList<String>) server.getAllChallengers();
	}

	/**
	 * Rimuove una partita precedentemente creata da un player.
	 * 
	 * @return lista delle partite aggiornata dopo la rimozione
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public ArrayList<String> removeGame() throws MalformedURLException,
			RemoteException, NotBoundException {
		if (creator)
			return (ArrayList<String>) server.removeGame(idGameCreated);
		else
			return (ArrayList<String>) server.removeGame(idGameJoined);

	}

	// Metodi di get e set.

	/**
	 * Restituisce la partita a cui partecipa il player corrente.
	 * 
	 * @return partita a cui partecipa il player corrente.
	 */
	public Game getGame() {
		return g;
	}

	/**
	 * Assegna la partita al player corrente.
	 * 
	 * @param g
	 *            partita a cui partecipa il player corrente.
	 */
	public void setGame(Game g) {
		this.g = g;
	}

	/**
	 * Restituisce il player corrente.
	 * 
	 * @return player corrente.
	 */
	public PlayerImpl getPlayer() {
		return p;
	}

	/**
	 * Assegna il player corrente.
	 * 
	 * @param p
	 *            player corrente.
	 */
	public void setPlayer(PlayerImpl p) {
		this.p = p;
	}

	/**
	 * Restituisce la griglia di gioco.
	 * 
	 * @return griglia di gioco.
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Restituisce lo stato corrente in cui si trova il player.
	 * 
	 * @return stato corrente.
	 * @see api.Event
	 */
	public Event getState() {
		return state;
	}

	/**
	 * Restituisce un flag per sapere se il player corrente e' il creatore della
	 * partita oppure no.
	 * 
	 * @return true se il player corrente e' il creatore della partita, false
	 *         altrimenti.
	 */
	public boolean isCreator() {
		return creator;
	}

	/**
	 * Restituisce l'id della partita creata dal player corrente.
	 * 
	 * @return id della partita creata.
	 */
	public String getIdGameCreated() {
		return idGameCreated;
	}

	/**
	 * Assegna l'id della partita creata dal player corrente.
	 * 
	 * @param idGameCreated
	 *            .
	 */
	public void setIdGameCreated(String idGameCreated) {
		this.idGameCreated = idGameCreated;
	}

	/**
	 * Restituisce l'id della partita a cui si e' aggiunto il player corrente.
	 * 
	 * @return idGameJoined della partita creata.
	 */
	public String getIdGameJoined() {
		return idGameJoined;
	}

	/**
	 * Assegna l'id della partita a cui si vuole aggiungere il player corrente.
	 * 
	 * @param idGameJoined
	 *            id della partita considerata.
	 */
	public void setIdGameJoined(String idGameJoined) {
		this.idGameJoined = idGameJoined;
	}

	// Metodi ridefiniti dell'interfaccia Watcher.

	/**
	 * Metodo che viene eseguito non appena viene rilevato un cambiamento nello
	 * stato del subject osservato.
	 */
	@Override
	public void update() {
		switch (p.getEvent()) {
		case FOE_MOVED:
			receiveMove();
			break;
		case MATCH_STARTED:
			setGame(p.getGame());
			break;
		}
		state = p.getEvent();
		sendNotification();
	}

	// Metodi ridefiniti dell'interfaccia Subject.

	/**
	 * Consente di aggiungere un osservatore al subject.
	 * 
	 * @param aWatcher
	 *            osservatore da aggiungere.
	 */
	@Override
	public void add(Watcher aWatcher) {
		wrs.add(aWatcher);

	}

	/**
	 * Consente di rimuovere un osservatore che era stato precedentemente
	 * aggiunto.
	 * 
	 * @param aWatcher
	 *            osservatore da rimuovere.
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
