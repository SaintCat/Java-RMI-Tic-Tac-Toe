package client.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Classe che implementa la finestra contenitore per la griglia di gioco
 * dell'interfaccia grafica del client. L'istanza di questa classe rappresenta
 * la schemata dalla quale il l'utente puo' prendere parte ad una partita di tic
 * tac toe.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see "WindowBuilder Pro"
 * @see client.gui.GameGUI
 * 
 */
public class GameBoardGUI extends GameGUI {

	// Messaggi di notifica predefiniti.
	private final static String WIN_MSG = "You won";
	private final static String DRAW_MSG = "Drawn game";
	private final static String LOSE_MSG = "You lost";
	private final static String FOE_DISC_MSG = "Your foe disconnected";
	private final static String FOE_LEFT_MSG = "Your foe has left the game";
	private static final String WAIT_MOVE_MSG = "Waiting for opponent move";
	private static final String DO_MOVE_MSG = "You can now make your move";
	private static final String LABEL = "Ivan Drago says: I must break you!";

	// Dimensione della griglia di gioco (quadrata).
	public final static int DIM = 3;

	// Attributi privati.
	private GUIController ctrl = null;
	private JPanel panelGameBoard;
	private JLabel labelReady, labelBusy;
	private JLabel lblGameBoard;
	private JButton buttons[][];

	/**
	 * Costruttore della classe GameBoardGUI.
	 */
	public GameBoardGUI(GUIController ctrl) {
		super();
		this.ctrl = ctrl;
		buttons = new JButton[DIM][DIM];
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++) {
				buttons[i][j] = new JButton("");
				enableButtonAtPosition(i, j, true);
			}
		initialize();
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di partita vinta.
	 */
	public void showWonMessage() {
		setStatusBar(WIN_MSG);
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di partita pareggiata.
	 */
	public void showDrawMessage() {
		setStatusBar(DRAW_MSG);
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di partita persa.
	 */
	public void showLoseMessage() {
		setStatusBar(LOSE_MSG);
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di notifica della
	 * disconnessione dell'avversario.
	 */
	public void showDisconnectedFoeMessage() {
		setStatusBar(FOE_DISC_MSG);
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di notifica dell'abbandono
	 * dell'avversario.
	 */
	public void showLeftFoeMessage() {
		setStatusBar(FOE_LEFT_MSG);
	}

	/**
	 * Metodo per abilitare l'icona che segnala al giocatore la possibilita' di
	 * effettuare la sua mossa. Viene impostato anche un apposito messaggio
	 * nella statusbar.
	 */
	public void setReady() {
		labelReady.setEnabled(true);
		labelBusy.setEnabled(false);
		setStatusBar(DO_MOVE_MSG);
	}

	/**
	 * Metodo per abilitare l'icona che segnala al giocatore l'impossibilita' di
	 * effettuare la sua mossa. Viene impostato anche un apposito messaggio
	 * nella statusbar.
	 */
	public void setBusy() {
		labelReady.setEnabled(false);
		labelBusy.setEnabled(true);
		setStatusBar(WAIT_MOVE_MSG);
	}
	
	/**
	 * Metodo che consente di abilitare o disabilitare uno specifico pulsante
	 * della griglia di gioco (modellata mediante una matrice quadrata). Si noti
	 * come la posizione all'interno della griglia di gioco sia codnificata
	 * mediante due indici interi i, j appartenenti all'intervallo [0, DIM-1],
	 * con DIM dimensione della matrice quadrata che modella la griglia.
	 * 
	 * @param i indice di riga all'interno della griglia.
	 * @param j indice di colonna all'interno della griglia.
	 * @param enable true per abilitare, false altrimenti.
	 */
	public void enableButtonAtPosition(int i,int j,boolean enable) {
		buttons[i][j].setEnabled(enable);
	}
	
	/**
	 * Metodo per disabilitare tutti i pulsanti che compongono la grigla di gioco.
	 */
	public void disableAllButtons() {
		for (int i = 0; i < DIM; i++)
			for (int j = 0; j < DIM; j++)
				buttons[i][j].setEnabled(false);
	}
	
	/**
	 * Metodo per impostare il simbolo di una casella (realizzata mediante un
	 * pulsante) della griglia di gioco. Una volta impostata la label, la
	 * casella viene disabilitata.
	 * 
	 * @param m indice della mossa.
	 * @param mLabel simbolo da usare per la casella.
	 */
	public void setButtonLabelXO(int m, MovementLabel mLabel) {
		String label;
		int i, j;

		i = m / DIM;
		j = m % DIM;

		if (mLabel == MovementLabel.X)
			label = "x";
		else label = "o";

		buttons[i][j].setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/" + label + ".png")));
		buttons[i][j].setEnabled(false);
	}
	
	/**
	 * Metodo che imposta l'icona ed il testo della label dell'header della
	 * finestra a seconda del simbolo associato al giocatore.
	 * 
	 * @param mLabel simbolo associato al giocatore.
	 */
	public void setHeaderLabel() {
			lblGameBoard
					.setText(LABEL);
	}
	
	/*
	 * Inizializzazione della finestra.
	 */
	private void initialize() {	
		frmTictactoe.addWindowListener( new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		        JFrame frame = (JFrame)e.getSource();
		        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit the application?",
		            "Exit Application",JOptionPane.YES_NO_OPTION);
		        if (result == JOptionPane.YES_OPTION)
		            ctrl.quitActionPerformed(null, true);
		    }
		});
		panelGameBoard = new JPanel();
		panelGameBoard.setAutoscrolls(true);
		panelGameBoard.setBorder(null);
		
		JPanel panelToolBar = new JPanel();
		
		lblGameBoard = new JLabel("It's time to play the game!");
		lblGameBoard.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/clipboard-list.png")));
		lblGameBoard.setForeground(Color.BLACK);
		lblGameBoard.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JPanel panelStatusBar = new JPanel();
		panelStatusBar.setBorder(null);
		GroupLayout groupLayout = new GroupLayout(frmTictactoe.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblGameBoard, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(panelGameBoard, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelToolBar, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
						.addComponent(panelStatusBar, GroupLayout.PREFERRED_SIZE, 438, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGameBoard)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelGameBoard, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panelToolBar, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
					.addGap(9)
					.addComponent(panelStatusBar, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		GridBagLayout gbl_panelStatusBar = new GridBagLayout();
		gbl_panelStatusBar.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelStatusBar.rowHeights = new int[]{0, 0};
		gbl_panelStatusBar.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelStatusBar.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelStatusBar.setLayout(gbl_panelStatusBar);
		
		JLabel labelStatusBat = new JLabel("");
		labelStatusBat.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/infocard.png")));
		GridBagConstraints gbc_labelStatusBat = new GridBagConstraints();
		gbc_labelStatusBat.insets = new Insets(0, 0, 0, 5);
		gbc_labelStatusBat.gridx = 0;
		gbc_labelStatusBat.gridy = 0;
		panelStatusBar.add(labelStatusBat, gbc_labelStatusBat);
		
		textArea.setText("");
		textArea.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		textArea.setBackground(UIManager.getColor("Button.background"));
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 0;
		panelStatusBar.add(textArea, gbc_textArea);
		
		JPanel panel11 = new JPanel();
		panel11.setBorder(null);
		
		JPanel panel12 = new JPanel();
		panel12.setBorder(null);
		
		JPanel panel13 = new JPanel();
		panel13.setBorder(null);
		
		JPanel panel21 = new JPanel();
		panel21.setBorder(null);
		
		JPanel panel31 = new JPanel();
		panel31.setBorder(null);
		
		JPanel panel22 = new JPanel();
		panel22.setBorder(null);
		
		JPanel panel23 = new JPanel();
		panel23.setBorder(null);
		
		JPanel panel32 = new JPanel();
		panel32.setBorder(null);
		
		JPanel panel33 = new JPanel();
		panel33.setBorder(null);
		
		JSeparator separator_3 = new JSeparator();
		GroupLayout gl_panelGameBoard = new GroupLayout(panelGameBoard);
		gl_panelGameBoard.setHorizontalGroup(
			gl_panelGameBoard.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGameBoard.createSequentialGroup()
					.addGroup(gl_panelGameBoard.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelGameBoard.createSequentialGroup()
							.addComponent(panel11, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel12, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel13, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelGameBoard.createSequentialGroup()
							.addComponent(panel21, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel22, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel23, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelGameBoard.createSequentialGroup()
							.addComponent(panel31, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel32, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel33, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(separator_3, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
		);
		gl_panelGameBoard.setVerticalGroup(
			gl_panelGameBoard.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGameBoard.createSequentialGroup()
					.addGroup(gl_panelGameBoard.createParallelGroup(Alignment.LEADING)
						.addComponent(panel11, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel12, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel13, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelGameBoard.createParallelGroup(Alignment.LEADING)
						.addComponent(panel21, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel22, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel23, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelGameBoard.createParallelGroup(Alignment.LEADING)
						.addComponent(panel33, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel32, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel31, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		panel11.setLayout(new BorderLayout(0, 0));
		panel12.setLayout(new BorderLayout(0, 0));
		panel13.setLayout(new BorderLayout(0, 0));
		panel21.setLayout(new BorderLayout(0, 0));
		panel22.setLayout(new BorderLayout(0, 0));
		panel23.setLayout(new BorderLayout(0, 0));
		panel31.setLayout(new BorderLayout(0, 0));
		panel32.setLayout(new BorderLayout(0, 0));
		panel33.setLayout(new BorderLayout(0, 0));

		panel11.add(buttons[0][0], BorderLayout.CENTER);
		panel12.add(buttons[0][1], BorderLayout.CENTER);
		panel13.add(buttons[0][2], BorderLayout.CENTER);
		panel21.add(buttons[1][0], BorderLayout.CENTER);
		panel22.add(buttons[1][1], BorderLayout.CENTER);
		panel23.add(buttons[1][2], BorderLayout.CENTER);
		panel31.add(buttons[2][0], BorderLayout.CENTER);
		panel32.add(buttons[2][1], BorderLayout.CENTER);
		panel33.add(buttons[2][2], BorderLayout.CENTER);

		panelGameBoard.setLayout(gl_panelGameBoard);

		// Setting degi Action Command per i pulsanti.
		for (int i = 0, id = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++, id++) {
				buttons[i][j].setActionCommand(Integer.toString(id));
				buttons[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							ctrl.movementClickedActionPerformed(arg0);
						}
				});
			}
		}
		
		labelReady = new JLabel("");
		labelReady.setEnabled(false);
		labelReady.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/status.png")));
		GridBagConstraints gbc_labelReady = new GridBagConstraints();
		gbc_labelReady.insets = new Insets(0, 0, 0, 5);
		gbc_labelReady.gridx = 3;
		gbc_labelReady.gridy = 0;
		panelStatusBar.add(labelReady, gbc_labelReady);
		
		labelBusy = new JLabel("");
		labelBusy.setEnabled(false);
		labelBusy.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/status-busy.png")));
		GridBagConstraints gbc_labelBusy = new GridBagConstraints();
		gbc_labelBusy.gridx = 4;
		gbc_labelBusy.gridy = 0;
		panelStatusBar.add(labelBusy, gbc_labelBusy);
		panelToolBar.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		panelToolBar.add(toolBar, BorderLayout.NORTH);
		
		JButton btnQuit = new JButton(" Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.quitActionPerformed(e,true);
			}
		});
		
		JButton btnLeaveGame = new JButton("Leave game"); 
		btnLeaveGame.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/control-stop-180.png")));
		btnLeaveGame.setMinimumSize(new Dimension(175, 29));
		btnLeaveGame.setMaximumSize(new Dimension(175, 29));
		btnLeaveGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnLeaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.leaveGameActionPerformed(e);
			}
		});
		toolBar.add(btnLeaveGame);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_2);
		btnQuit.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/door-open.png")));
		btnQuit.setMinimumSize(new Dimension(175, 29));
		btnQuit.setMaximumSize(new Dimension(175, 29));
		btnQuit.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnQuit);
		
		frmTictactoe.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmTictactoe.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/application-blue.png")));
		menuBar.add(mnFile);
		
		JMenuItem mntmConnectToServer = new JMenuItem("Connect to Server");
		mntmConnectToServer.setEnabled(false);
		mntmConnectToServer.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/lightning.png")));
		mnFile.add(mntmConnectToServer);
		
		JSeparator separator_6 = new JSeparator();
		mnFile.add(separator_6);
		
		JMenuItem mntmAddYourGame = new JMenuItem("Add Your Game");
		mntmAddYourGame.setEnabled(false);
		mntmAddYourGame.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/tag--plus.png")));
		mnFile.add(mntmAddYourGame);
		
		JMenuItem mntmRemoveYourGame = new JMenuItem("Remove Your Game");
		mntmRemoveYourGame.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/tag--minus.png")));
		mntmRemoveYourGame.setEnabled(false);
		mnFile.add(mntmRemoveYourGame);
		
		JMenuItem mntmJoinSelectedGame = new JMenuItem("Join Selected Game");
		mntmJoinSelectedGame.setEnabled(false);
		mntmJoinSelectedGame.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/tag--arrow.png")));
		mnFile.add(mntmJoinSelectedGame);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmRefreshList = new JMenuItem("Refresh List");
		mntmRefreshList.setEnabled(false);
		mntmRefreshList.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/list-refresh.png")));
		mnFile.add(mntmRefreshList);
		
		JSeparator separator_7 = new JSeparator();
		mnFile.add(separator_7);
		
		JMenuItem mntmLeaveGame = new JMenuItem("Leave Game");
		mntmLeaveGame.setEnabled(true);
		mntmLeaveGame.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/control-stop-180.png")));
		mnFile.add(mntmLeaveGame);
		mntmLeaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.leaveGameActionPerformed(e);
			}
		});
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.quitActionPerformed(e,true);
			}
		});
		mntmQuit.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/door-open.png")));
		mnFile.add(mntmQuit);
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/information-balloon.png")));
		menuBar.add(mnAbout);
		
		JMenuItem mntmAuthors = new JMenuItem("Authors");
		mntmAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAuthorsMessage();
			}
		});
		mntmAuthors.setIcon(new ImageIcon(GameBoardGUI.class.getResource("/client/gui/icons/hard-hat.png")));
		mnAbout.add(mntmAuthors);
	}

}