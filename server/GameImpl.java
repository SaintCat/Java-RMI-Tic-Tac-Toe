package server;

import java.rmi.RemoteException;
import java.rmi.server.*;
import api.*;

/**
 * Implementazione dell'oggetto remoto {@link Game}. Offre metodi per gestire
 * tutte le operazioni necessarie al corretto svolgimento della partita tra due
 * player:<br/>
 * consentendo di inviare la mossa effettuata da un giocatore al server;<br/>
 * gestendo la corretta turnazione tra i due player;<br/>
 * verificando il punteggio finale.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see api.Game
 * 
 */
public class GameImpl extends UnicastRemoteObject implements Game {

	// Attributi privati.
	private static final long serialVersionUID = 1L;
	private static final int MIN_NUMBER_OF_MOVES_TO_CHECK_WIN = 5;
	private static final int MIN_MOVE_ALLOWED = 0;
	private static final int MAX_MOVE_ALLOWED = 8;
	private PlayerNumber currentPlayer;
	private Player player1, player2;
	private int numberOfMovements;
	private boolean endGame;
	private int grid[][];
	private String idGame;

	/*
	 * Variabile usata per poter distinguere il player 1, P1 dal player 2, P2
	 */
	public enum PlayerNumber {
		P1, P2
	}

	/**
	 * Costruttore di classe.Consente di creare una nuova partita (senza
	 * avviarla) mettendosi in attesa dell'arrivo di uno sfidante.
	 * 
	 * @param p1
	 *            player che crea la partita
	 * @param id
	 *            id della partita da creare. Formato richiesto:
	 *            nickNameCreatorePartita-oraCreazione. Es: mrossi-12:23:22
	 * @throws RemoteException
	 */
	public GameImpl(Player p1, String id) throws RemoteException {
		grid = new int[GRID_DIMENSION][GRID_DIMENSION];
		this.player1 = p1;
		numberOfMovements = 0;
		this.idGame = id;
	}

	/**
	 * Restituisce l'id della partita.
	 * 
	 * @return id della partita.
	 */
	public String getIdGame() {
		return idGame;
	}

	/**
	 * Assegna un id alla partita.
	 * 
	 * @param id
	 *            id da assegnare alla partita.Formato richiesto:
	 *            nickNameCreatorePartita-oraCreazione. Es: mrossi-12:23:22.
	 */
	public void setIdGame(String id) {
		this.idGame = id;
	}

	/**
	 * Restituisce un flag per sapere se la partita e' ancora in corso o e'
	 * finita.
	 * 
	 * @return true se la partita e' finita false altrimenti.
	 */
	public boolean isEndGame() {
		return endGame;
	}

	/**
	 * Forza la fine o l'inizio di una partita.
	 * 
	 * @param endGame
	 *            true se la partita deve essere terminata,false se deve
	 *            iniziare.
	 */
	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	/**
	 * Restituisce il player che ha creato la partita.
	 * 
	 * @return player che ha creato la partita.
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Imposta il player che crea la partita.
	 * 
	 * @param player1
	 *            player che crea la partita.
	 */
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * Assegna uno sfidante a una partita gia' precedentemente creata dal player
	 * <code>player1</code>, associando i due player e facendo cominciare la
	 * partita.
	 * 
	 * @param p
	 *            sfidante da associare alla partita.
	 * @throws RemoteException
	 */
	public void setPlayer2(Player p) throws RemoteException {
		player2 = p;
		matchCanStart();
	}

	/**
	 * Restituisce il player 2 della partita.
	 * 
	 * @return player 2 della partita.
	 */
	public Player getPlayer2() {
		return player2;
	}

	/**
	 * Restituisce il player a cui spetta effettuare la mossa.
	 * 
	 * @return player a cui spetta effettuare la mossa.
	 */
	public PlayerNumber getCurrentPlayer() {
		return currentPlayer;
	}

	// Implementazione dell'interfaccia Game.
	
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
	@Override
	public synchronized void sendMovement(Player p, int m)
			throws RemoteException {

		if (!endGame && m >= MIN_MOVE_ALLOWED && m <= MAX_MOVE_ALLOWED) {
			int res = -1;
			if (p.equals(player1))
				checkMove(player1, player2, PlayerNumber.P1, m);
			else if (p.equals(player2))
				checkMove(player2, player1, PlayerNumber.P2, m);

			if (numberOfMovements >= MIN_NUMBER_OF_MOVES_TO_CHECK_WIN)
				res = checkWinner();
			if (res > 0)
				notifyFinalResult(res);
		}

	}

	/*
	 * Il metodo controlla se e' possibile decretare un vincitore, in base alle
	 * mosse fino a quel punto effettuate dai due player.
	 * 
	 * @return 0: partita ancora in gioco ; 1: player 1 ha vinto; 2: player 2 ha
	 * vinto; 3: pareggio.
	 */
	private int checkWinner() {
		int gridValue = 0;
		int result = 0;
		int i;
		boolean winner = false;

		// Controllo delle righe.
		for (i = 0; i < GRID_DIMENSION; i++) {
			if ((grid[i][0] == grid[i][1]) && (grid[i][1] == grid[i][2])) {
				gridValue = grid[i][0];
				if (gridValue != Game.FREE_CELL) {
					winner = true;
					result = gridValue;
				}
			}
		}
		if (!winner) {
			// Controllo delle colonne.
			for (i = 0; i < GRID_DIMENSION; i++) {
				if ((grid[0][i] == grid[1][i]) && (grid[1][i] == grid[2][i])) {
					gridValue = grid[0][i];
					if (gridValue != Game.FREE_CELL) {
						winner = true;
						result = gridValue;
					}
				}
			}
		}
		// Controllo delle diagonali.
		if (!winner) {
			if ((grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
					|| (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])) {
				gridValue = grid[1][1];
				if (gridValue != Game.FREE_CELL) {
					winner = true;
					result = gridValue;
				}
			}
		}
		// Si controlla se la partita e' finita in parita'
		if (!winner) {
			result = 3;
			for (i = 0; i < GRID_DIMENSION; i++) {
				for (int j = 0; j < GRID_DIMENSION; j++) {
					if (grid[j][i] == 0)
						result = 0;
				}
			}
		}
		return result;
	}

	/*
	 * Il metodo controlla se la cella e' libera; in tal caso la si aggiorna con
	 * 1 se player1 ha inviato la mossa e 2 se la ha inviata player2.
	 */
	private boolean updateGrids(int m, int val) {
		boolean updated = false;
		int i = (int) m / GRID_DIMENSION;
		int j = (int) m % GRID_DIMENSION;
		if (grid[i][j] == 0) {
			grid[i][j] = val;
			updated = true;
		}
		return updated;
	}

	/*
	 * Aggiorna i valori della griglia di gioco a seconda del player che sta
	 * effettuando la mossa e gestisce il turno per la prossima mossa,
	 * notificando l'evento al client interessato.
	 */
	private void checkMove(Player p1, Player p2, PlayerNumber turn, int m)
			throws RemoteException {
		int value;

		if (currentPlayer.equals(PlayerNumber.P1)) {
			// Se il current player p e' P1, si inserisce nella griglia il
			// valore 1 in corrispondenza della sua mossa.
			value = P1_CELL;
		} else {
			// Se il current player p e' P2, si inserisce nella griglia il
			// valore 2 in corrispondenza della sua mossa.
			value = P2_CELL;
		}

		if (updateGrids(m, value)) {
			numberOfMovements++;
			p2.foeHasMoved(m);
			if (turn.equals(PlayerNumber.P1))
				currentPlayer = PlayerNumber.P2;
			else if (turn.equals(PlayerNumber.P2))
				currentPlayer = PlayerNumber.P1;
			p1.foeTurn();
			p2.isYourTurn();
		}
	}

	/*
	 * Notifica il risultato finale della partita ai due player res = 1 =>
	 * player 1 ha vinto res = 2 => player 2 ha vinto res = 3 => pareggio
	 */
	private void notifyFinalResult(int res) throws RemoteException {
		if (res == 1) {
			player1.sendGameResult(Event.YOU_WIN);
			player2.sendGameResult(Event.YOU_LOSE);
		} else if (res == 2) {
			player1.sendGameResult(Event.YOU_LOSE);
			player2.sendGameResult(Event.YOU_WIN);
		} else {
			player1.sendGameResult(Event.DRAW);
			player2.sendGameResult(Event.DRAW);
		}
		
		endGame = true;
	}

	/*
	 * Invia le notifiche a entrambi i player per avvisarli che la partita sta
	 * per cominciare e assegna il turno in maniera casuale a uno dei due player
	 * per consentirgli di fare la prima mossa.
	 */
	private void matchCanStart() throws RemoteException {
		// aggiungo i due player alla partita
		player1.joinGame(this);
		player2.joinGame(this);
		// scelgo randomicamente chi deve essere il primo a giocare
		int x = (Math.random() < 0.5) ? 0 : 1;
		if (x == 1) {
			currentPlayer = PlayerNumber.P1;
			player2.foeTurn();
			player1.isYourTurn();
		} else {
			currentPlayer = PlayerNumber.P2;
			player1.foeTurn();
			player2.isYourTurn();
		}
		endGame = false;
	}
}
