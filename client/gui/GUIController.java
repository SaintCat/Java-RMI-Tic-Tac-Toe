package client.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.UIManager;
import api.*;
import client.*;

/**
 * Classe per la gestione delle finestre che costituiscono l'interfaccia grafica
 * del client.In particolare ne gestisce la corretta alternanza a seconda delle
 * varie fasi di gioco e ne elabora gli input catturati (dall'utente) producendo
 * in output messaggi di errore, messaggi di conferma, abilitazione e
 * disabilitazione di pulsanti grafici.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see client.gui.GameGUI
 * @see client.gui.GameInitGUI
 * @see client.gui.GameListGUI
 * @see client.gui.GameBoardGUI
 * 
 */
public class GUIController implements Watcher {

	// Attributi privati.
	private GameInitGUI gameInitGUI;
	private GameListGUI gameListGUI;
	private GameBoardGUI gameBoardGUI;
	private TicTacToeClient client;
	private String playerNickName;

	/**
	 * Costruttore della classe GUI Controller.
	 */
	public GUIController() {
		gameInitGUI = new GameInitGUI(this);
		gameListGUI = new GameListGUI(this);
		gameBoardGUI = new GameBoardGUI(this);
	}

	// Metodi per lo switching tra le varie schermate.

	/**
	 * Metodo che consente impostare la visibilita' della finestra iniziale
	 * dell'applicazione.
	 * 
	 * @param value true per rendere visibilita la finestra, false altrimenti.
	 */
	public void setVisibleGameInit(boolean value) {
		this.gameInitGUI.setVisible(value);
	}

	/**
	 * Metodo che consente di impostare la visibilita' della finestra contenente
	 * la lista delle partite disponibili.
	 * 
	 * @param value true per rendere visibilita la finestra, false altrimenti.
	 */
	public void setVisibleGameList(boolean value) {
		this.gameListGUI.setTitle(playerNickName);
		this.gameListGUI.setVisible(value);
	}

	/**
	 * Metodo che consente di impostare la visibilita' della finestra contenente
	 * la griglia di gioco.
	 * 
	 * @param value true per rendere visibilita' la finestra, false altrimenti.
	 */
	public void setVisibleGameBoard(boolean value) {
		this.gameBoardGUI = new GameBoardGUI(this);
		this.gameBoardGUI.setTitle(playerNickName);
		this.gameBoardGUI.setVisible(value);
	}

	// Metodi la gestione degli eventi generati mediante l'interazione con la GUI.

	/**
	 * Metodo per gestire l'uscita dall'applicazione a seguito della pressione
	 * del tasto quit.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 * @param deregister
	 *            true per deregistrare un giocatore e la sua eventuale partita,
	 *            false altrimenti.
	 */
	public void quitActionPerformed(ActionEvent e, boolean deregister) {
		try {
			if (deregister) {
				client.deregisterPlayer(playerNickName);
				client.removeGame();
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		} finally {
			gameInitGUI.close();
			gameListGUI.close();
			gameBoardGUI.close();
			System.exit(0); // Viene forzata l'uscita dall'applicazione.
		}
	}

	/**
	 * Metodo per gestire il passaggio dalla finestra iniziale a quella
	 * contenente la lista delle partita.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 * @param nickname nickname associato ad un giocatore.
	 */
	public void initToListActionPerformed(ActionEvent e, String nickname) {
		try {
			client = new TicTacToeClient();
			// Aggiungo come osservatore di client.
			client.add(this);
			if (!client.registerPlayer(nickname))
				gameInitGUI.showNicknameAlreadyTakenMessage();
			else {
				playerNickName = client.getPlayer().getName();
				gameInitGUI.setVisible(false);
				setVisibleGameList(true);
				ArrayList<String> c = client.receiveList();
				gameListGUI.showList(c);
			}
		} catch (MalformedURLException e1) {
			gameInitGUI.showConnectionRefusedMessage();
			e1.printStackTrace();
		} catch (RemoteException e2) {
			gameInitGUI.showConnectionRefusedMessage();
			e2.printStackTrace();
		} catch (NotBoundException e3) {
			gameInitGUI.showConnectionRefusedMessage();
			e3.printStackTrace();
		}

	}

	/**
	 * Metodo per gestire l'aggiunta di una partita all'interno dell'apposita
	 * lista.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 */
	public void addGameActionPerformed(ActionEvent e) {
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			String dateCreation = formatter.format(c.getTime());
			String idGame = client.getPlayer().getName() + "-" + dateCreation;
			client.createGame(idGame);
			gameListGUI.setButtonsEnabled(false, true, false);
			gameListGUI.showGameSuccAddedMessage();
			refreshListActionPerformed(e);
		} catch (MalformedURLException e1) {
			gameListGUI.showConnectionErrorMessage();
			e1.printStackTrace();
		} catch (RemoteException e2) {
			gameListGUI.showConnectionErrorMessage();
			e2.printStackTrace();
		} catch (NotBoundException e3) {
			gameListGUI.showConnectionErrorMessage();
			e3.printStackTrace();
		}

	}

	/**
	 * Metodo per la gestione della rimozione di una partita dall'apposita
	 * lista.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 */
	public void removeGameActionPermorfed(ActionEvent e) {
		try {
			ArrayList<String> c = client.removeGame();
			gameListGUI.setButtonsEnabled(true, false, true);
			gameListGUI.showGameSuccRemovedMessage();
			gameListGUI.showList(c);
		} catch (MalformedURLException e1) {
			gameListGUI.showConnectionErrorMessage();
			e1.printStackTrace();
		} catch (RemoteException e2) {
			gameListGUI.showConnectionErrorMessage();
			e2.printStackTrace();
		} catch (NotBoundException e3) {
			gameListGUI.showConnectionErrorMessage();
			e3.printStackTrace();
		}

	}

	/**
	 * Metodo per gestire l'aggiornamento della lista di partite.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 */
	public void refreshListActionPerformed(ActionEvent e) {
		try {
			ArrayList<String> c = client.receiveList();
			gameListGUI.showList(c);
			if (c.size() == 0)
				gameListGUI.showNoGameAvailableMessage();
		} catch (MalformedURLException e1) {
			gameListGUI.showConnectionErrorMessage();
			e1.printStackTrace();
		} catch (RemoteException e2) {
			gameListGUI.showConnectionErrorMessage();
			e2.printStackTrace();
		} catch (NotBoundException e3) {
			gameListGUI.showConnectionErrorMessage();
			e3.printStackTrace();
		}
	}

	/**
	 * Metodo per la gestione della partecipazione ad una partita.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 * @param idGame identificativo (univoco) associato ad una specifica partita.
	 */
	public void joinGameActionPermorfed(ActionEvent e, String idGame) {
		try {
			client.joinGame(idGame);
		} catch (GameJoiningException e0) {
			gameListGUI.showGameLockedMessage();
		} catch (MalformedURLException e1) {
			gameListGUI.showConnectionErrorMessage();
			e1.printStackTrace();
		} catch (RemoteException e2) {
			gameListGUI.showConnectionErrorMessage();
			e2.printStackTrace();
		} catch (NotBoundException e3) {
			gameListGUI.showConnectionErrorMessage();
			e3.printStackTrace();
		}

	}

	/**
	 * Metodo per gestire l'abbandono di una partita o l'uscita a seguito di una
	 * vittoria, sconfitta o pareggio.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 */
	public void leaveGameActionPerformed(ActionEvent e) {
		try {
			ArrayList<String> c = client.removeGame();
			gameBoardGUI.close();
			client.resetGrid();
			gameListGUI.setVisible(true);
			gameListGUI.setButtonsEnabled(true, false, true);
			gameListGUI.showBackMessage();
			gameListGUI.showList(c);
		} catch (MalformedURLException e1) {
			gameListGUI.showConnectionErrorMessage();
			e1.printStackTrace();
		} catch (RemoteException e2) {
			gameListGUI.showConnectionErrorMessage();
			e2.printStackTrace();
		} catch (NotBoundException e3) {
			gameListGUI.showConnectionErrorMessage();
			e3.printStackTrace();
		}

	}

	/**
	 * Metodo per gestire una mossa di gioco da parte di un partecipante ad una
	 * partita.
	 * 
	 * @param e evento generato mediante l'interazione con la GUI.
	 */
	public void movementClickedActionPerformed(ActionEvent e) {
		int button = Integer.parseInt(e.getActionCommand());
		MovementLabel l;

		if (client.isCreator())
			l = MovementLabel.X;
		else
			l = MovementLabel.O;

		gameBoardGUI.setButtonLabelXO(button, l);
		gameBoardGUI.setBusy();
		try {
			client.sendMove(button);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	// Metodi ridefiniti dall'interfaccia Watcher.

	/**
	 * Metodo per la gestione dei diversi stati in cui puo' trovarsi il client.
	 */
	@Override
	public void update() {

		switch (client.getState()) {
		case MATCH_STARTED: // Inizio della partita.
			gameListGUI.close();
			gameInitGUI.close();
			setVisibleGameBoard(true);
			gameBoardGUI.setHeaderLabel();
			break;

		case YOUR_TURN: // Turno del giocatore corrente.
			gameBoardGUI.setReady();
			enableButtons(client.getGrid(), true);
			break;

		case FOE_TURN: // Turno del giocatore avversario.
			gameBoardGUI.setBusy();
			enableButtons(client.getGrid(), false);
			break;

		case FOE_DISCONNECTED: // Disconnessione del giocatore avversario.
			gameBoardGUI.showLeftFoeMessage();
			gameBoardGUI.disableAllButtons();
			break;

		case FOE_MOVED: // Mossa da parte del giocatore avversario.
			client.receiveMove();
			int move = client.getPlayer().getFoeSelectedMove();

			MovementLabel l;
			if (client.isCreator()) {
				if (client.getGrid()[move / GameBoardGUI.DIM][move % GameBoardGUI.DIM] == Game.P1_CELL)
					l = MovementLabel.X;
				else
					l = MovementLabel.O;
			} else {
				if (client.getGrid()[move / GameBoardGUI.DIM][move % GameBoardGUI.DIM] == Game.P1_CELL)
					l = MovementLabel.O;
				else
					l = MovementLabel.X;
			}

			gameBoardGUI.setButtonLabelXO(move, l);
			client.getPlayer().isYourTurn();
			break;

		case DRAW: // Pareggio della partita.
			gameBoardGUI.disableAllButtons();
			gameBoardGUI.setBusy();
			gameBoardGUI.showDrawMessage();
			break;

		case YOU_WIN: // Vittoria della partita da parte del giocatore corrente.
			gameBoardGUI.disableAllButtons();
			gameBoardGUI.setBusy();
			gameBoardGUI.showWonMessage();
			break;

		case YOU_LOSE: // Sconfitta della partita da parte del giocatore
						// corrente.
			gameBoardGUI.disableAllButtons();
			gameBoardGUI.setBusy();
			gameBoardGUI.showLoseMessage();
			break;
		}

	}

	/**
	 * Main dell'applicazione lato client.
	 */
	public static void main(String[] args) {
		try {
			// Impostazione del look and feel.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIController ctrl = new GUIController();
					ctrl.setVisibleGameInit(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Metodo per abilitare i pulsanti della griglia durante l'alternarsi delle
	 * fasi di gioco.
	 */
	private void enableButtons(int grid[][], boolean enable) {
		for (int i = 0; i < GameBoardGUI.DIM; i++)
			for (int j = 0; j < GameBoardGUI.DIM; j++)
				if (grid[i][j] == Game.FREE_CELL)
					gameBoardGUI.enableButtonAtPosition(i, j, enable);
	}

}