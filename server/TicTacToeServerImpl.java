package server;

import java.rmi.*;
import java.rmi.server.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import api.*;

/**
 * Implementazione dell'oggetto remoto {@link TicTacToeServer}. Mantiene traccia
 * dei player che si collegano al server e delle partite correntemente attive.
 * Offre inoltre metodi per creare una nuova partita, aggiungersi a una già
 * creata e ottenere la lista di tutte le partite attive.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see TicTacToeServer
 */
public class TicTacToeServerImpl extends UnicastRemoteObject implements
		TicTacToeServer {

	// Attributi privati.
	private static final long serialVersionUID = 2461589075214891427L;
	private Map<String, Game> listGames;
	private Map<String, Player> listPlayers;

	/**
	 * Costruttore di classe
	 * 
	 * @throws RemoteException
	 */
	public TicTacToeServerImpl() throws RemoteException {
		listGames = new HashMap<String, Game>();
		listPlayers = new HashMap<String, Player>();
	}

	/**
	 * Consente di creare una nuova partita a un player, dato un idGame
	 * 
	 * @param p player che crea la partita
	 * @param idGame deve avere il seguente formato: 
	 * 			     nickname-ora_di_creazione_partita.<br/>
	 *               Es: mrossi-12:23:22
	 * @throws RemoteException
	 */
	@Override
	public synchronized void createGame(Player p, String idGame)
			throws RemoteException {

		// Si suppone che p sia gia' registrato
		Game g = new GameImpl(p, idGame);
		listGames.put(idGame, g);
	}

	/**
	 * Consente ad un player di aggiungersi a una partita,avente come id
	 * <code>idgame</code>, creata da un altro player e cominciare cosi a
	 * giocare.
	 * 
	 * @param p player che vuole aggiungersi alla partita
	 * @param idGame deve avere il seguente formato:
	 *               nickname-ora_di_creazione_partita.<br/>
	 *               Es: mrossi-12:23:22.
	 * @throws RemoteException
	 * @throws GameJoiningException
	 *             se la partita e' gia' completa oppure non e' piu' disponibile
	 *             perche' e' stata cancellata.
	 */
	@Override
	public synchronized void joinGame(Player p, String idGame)
			throws RemoteException, GameJoiningException {

		// Si suppone che p sia gia' registrato
		if (listGames.get(idGame) != null) {
			GameImpl g = (GameImpl) listGames.get(idGame);
			// Se la partita non e' gia' piena, si aggiunge il giocatore p.
			if (g.getPlayer2() == null) {
				// Si controlla se i giocatori siano la stessa persona.
				if (g.getPlayer1().equals(p))
					throw new GameJoiningException("Same players.");
				else {
					g.setPlayer2(p);
					Thread checkPlayer1 = new Thread(new PlayerMonitor(
							g.getPlayer1(), g.getPlayer2(), idGame));
					Thread checkPlayer2 = new Thread(new PlayerMonitor(
							g.getPlayer2(), g.getPlayer1(), idGame));
					checkPlayer1.start();
					checkPlayer2.start();
				}
			} else
				throw new GameJoiningException("Game full.");
		} else
			throw new GameJoiningException("Game not available.");
	}

	/**
	 * Consente di registrare un player al server, dato un nickname. All'atto
	 * della registrazione non possono essere presenti due player con uno stesso
	 * nickname.
	 * 
	 * @param p player da registrare.
	 * @param playerName nickname del player.
	 * @return true se la registrazione e' andata a buon fine,ovvero non sono
	 *         gia' presenti altri player con lo stesso nickname false
	 *         altrimenti.
	 * @throws RemoteException
	 */
	@Override
	public synchronized boolean register(Player p, String playerName) {
		if (!listPlayers.containsKey(playerName)) {
			listPlayers.put(playerName, p);
			return true;
		}
		return false;
	}

	/**
	 * Restituisce la lista di tutti i player che hanno creato una partita e che
	 * sono in attesa di uno sfidante oppure stanno ancora giocando.
	 * 
	 * @return lista di player in attesa o con partita in corso.
	 * @throws RemoteException
	 */
	@Override
	public synchronized ArrayList<String> getAllChallengers() {
		Set<String> games = listGames.keySet();
		ArrayList<String> s = new ArrayList<String>(games);
		Collections.sort(s, new Comparator<String>() {
			DateFormat df = new SimpleDateFormat("HH:mm:ss");

			@Override
			public int compare(String s1, String s2) {
				try {
					Date d1 = df.parse(s1.split("-")[1].trim());
					Date d2 = df.parse(s2.split("-")[1].trim());
					return d2.compareTo(d1);
				} catch (ParseException pe) {
					return 0;
				}
			}
		});
		return s;
	}

	/**
	 * Consente di deregistrare un player, dato il suo nickname quando questi si
	 * disconnette dall'applicazione.
	 * 
	 * @param playerName
	 *            nickname del player da deregistrare.
	 * @throws RemoteException
	 */
	@Override
	public synchronized void deregister(String playerName) {
		listPlayers.remove(playerName);
	}

	/**
	 * Consente di rimuovere una partita dalla lista delle partite mantenuta sul
	 * server.
	 * 
	 * @param idGame id della partita da eliminare.Deve avere il seguente formato:
	 *               nickname-ora_di_creazione_partita.<br/>
	 *               Es: mrossi-12:23:22.
	 * @return lista aggiornata dei player dopo la rimozione.
	 * @throws RemoteException
	 */
	@Override
	public synchronized ArrayList<String> removeGame(String idGame) {
		listGames.remove(idGame);
		return getAllChallengers();
	}

	/**
	 * Restituisce la lista dei giocatori correntemente registrati
	 * 
	 * @return lista dei giocatori correntemente registrati
	 * @see server.test.TicTacToeServerImplTest
	 */
	public synchronized Map<String, Player> getListPlayers() {
		return listPlayers;
	}

	/**
	 * Restituisce la lista delle partite in attesa di un player o non ancora
	 * concluse
	 * 
	 * @return lista delle partite
	 * @see server.test.TicTacToeServerImplTest
	 */
	public synchronized Map<String, Game> getListGames() {
		return listGames;
	}

	/*
	 * PlayerMonitor effettua il monitoraggio di un giocatore per avere un
	 * riscontro del suo stato di connessione.
	 */
	private class PlayerMonitor implements Runnable {

		// Attributi privati.
		private Player player1;
		private Player player2;
		private String idGame;
		// Rappresenta l'intervallo di interrogazione verso il giocatore.
		private static final int TIME = 5000;

		/*
		 * Costruttore della classe TicTacToeServerImpl.
		 */
		public PlayerMonitor(Player player1, Player player2, String idGame) {
			super();
			// Id del game associato alla partita tra due giocatori.
			this.idGame = idGame;
			// Giocatore su cui sara' realizzato il ping.
			this.player1 = player1;
			// Giocatore a cui sara' notificato l'evento di disconnesione,
			this.player2 = player2;
		}

		/*
		 * Il metodo interroga un player con una certa cadenza finche' non
		 * avviene l'evento di disconnessione. In tale caso si provvedera' a
		 * notificare l'evento all'altro giocatore, chiudendo quindi il thread.
		 */
		@Override
		public void run() {
			boolean disconnect = false;
			while (!disconnect) {
				try {
					Thread.sleep(TIME);
					player1.ping(); // Operazione di ping verso il giocatore..
					if (!Collections.synchronizedMap(listGames).containsKey(
							idGame)) {
						player2.foeDisconnected();
						disconnect = true;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					try {
						// nel caso in cui un player non ha risposto a un ping
						// comunico l'evento all'altro player.
						player2.foeDisconnected();
						disconnect = true;
					} catch (RemoteException e1) {
						disconnect = true;
					}
				}
			}
		}

	} // Chiusura della inner-class.

}
