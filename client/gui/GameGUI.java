package client.gui;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Classe associata a finestre generiche dell'interfaccia grafica
 * dell'applicazione.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see "WindowBuilder Pro"
 * 
 */
public class GameGUI {

	// Messaggi di notifica predefinti.
	private static final String AUTHORS_MSG = "Gennaro Capo, Mirko Conte, Vincenzo De Notaris, Roberto Pacilio";
	private static final String CONNECTION_ERROR_MSG = "Connection error";

	// Attributi condivisi con le classi figlie.
	protected JFrame frmTictactoe;
	protected JTextArea textArea;

	/**
	 * Costruttore della classe GameGUI.
	 */
	public GameGUI() {
		frmTictactoe = new JFrame();
		initializeFrame();
		textArea = new JTextArea();
	}

	/**
	 * Metodo che consente di impostare la visibilita' della finestra
	 * contenitore.
	 */
	public void setVisible(boolean value) {
		this.frmTictactoe.setVisible(value);
	}

	/**
	 * Metodo che consente di chiudere la finestra contenitore.
	 */
	public void close() {
		this.frmTictactoe.setVisible(false);
	}

	/**
	 * Metodo che consente di impostare un messaggio nella status bar.
	 * 
	 * @param message messaggio da inserire nella status bar.
	 */
	public void setStatusBar(String message) {
		textArea.setText(message);
	}

	/**
	 * Metodo che consente di impostare il titolo della finestra.
	 * 
	 * @param value titolo della finestra.
	 */
	public void setTitle(String value) {
		this.frmTictactoe.setTitle("Tic Tac Toe - " + value);
	}

	/**
	 * Metodo che mostra nella statusbar il nome degli autori dell'applicazione.
	 */
	public void showAuthorsMessage() {
		setStatusBar(AUTHORS_MSG);
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di errore nella
	 * connessione.
	 */
	public void showConnectionErrorMessage() {
		setStatusBar(CONNECTION_ERROR_MSG);
	}

	// Inizializzazione della finestra.
	private void initializeFrame() {
		frmTictactoe.setTitle("Tic Tac Toe");
		frmTictactoe.setResizable(false);
		frmTictactoe.setBounds(100, 100, 450, 400);
		frmTictactoe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

}