package client.gui;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JMenuBar;
import javax.swing.border.LineBorder;
import javax.swing.JFrame;
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

import java.util.ArrayList;

/**
 * Classe che implementa la finestra contenitore per la creazione/selezione
 * dell'interfaccia grafica del client. L'istanza di questa classe rappresenta
 * la schemata dalla quale  l'utente puo' creare una partita ed attendere uno
 * sfidante oppure partecipare ad una partita precedentemente creata da un altro
 * utente.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see "WindowBuilder Pro"
 * 
 */
public class GameListGUI extends GameGUI {

	// Messaggi di notifica predefinti.
	private static final String GAME_SUCC_ADDED_MSG = "Game successfully added";
	private static final String GAME_SUCC_REMOVED_MSG = "Game succefully removed";
	private static final String SELECTION_ERROR_MSG = "You must select a game";
	private static final String STATUSBAR_INIT_MSG = "To add or to join? That is the question!";
	private static final String NO_GAME_AVAILABLE_MSG = "No game available. Why don't you add one?";
	private static final String GAME_LOCKED_MSG = "Sorry, the game is not available, try choosing another one";
	private static final String BACK_MSG = "Welcome back! And now... add or join?";
	
	// Attributi privati.
	private GUIController ctrl = null;
	private JButton btnAddGame;
	private JButton btnRemoveGame;
	private JButton btnJoinSelected;
	private JButton btnRefreshList;
	private JPanel panelGameList;
	private ArrayList<String> list;
	private DefaultListModel listModel;
	private JList gamesList;
	private JMenuItem mntmAddYourGame, mntmRemoveYourGame, mntmJoinSelectedGame;

	/**
	 * Costruttore della classe GameListGUI.
	 */
	public GameListGUI(GUIController ctrl) {
		super();
		this.ctrl = ctrl;
		initialize();
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di notifica per partita
	 * aggiunta con successo.
	 */
	public void showGameSuccAddedMessage() {
		setStatusBar(GAME_SUCC_ADDED_MSG);
	}

	/**
	 * Metodo che mostra nella statusbar il messaggio di notifica per partita
	 * rimossa con successo.
	 */
	public void showGameSuccRemovedMessage() {
		setStatusBar(GAME_SUCC_REMOVED_MSG);
	}

	/**
	 * Metodo che mostra nella statusbad il messaggio che notifica l'assenza di
	 * partite nella lista.
	 */
	public void showNoGameAvailableMessage() {
		setStatusBar(NO_GAME_AVAILABLE_MSG);
	}

	/**
	 * Metodo che mostra nella statusbad il messaggio che notifica
	 * l'indisponibilit� della partita selezionata.
	 */
	public void showGameLockedMessage() {
		setStatusBar(GAME_LOCKED_MSG);
	}

	/**
	 * Metodo che mostra nella statusbad il messaggio che notifica il ritorno
	 * alla finestra della lista.
	 */
	public void showBackMessage() {
		setStatusBar(BACK_MSG);
	}
	
	/**
	 * Metodo che mostra all'interno della JList della GUI le partite create.
	 * 
	 * @param games lista delle partite.
	 */
	public void showList(ArrayList<String> games) {
		list = games;
		listModel.clear();
		for (int i = 0; i < list.size(); i++)
			listModel.add(i, list.get(i));
	}

	/**
	 * Metodo che aggiorna gli elementi grafici presenti nella finestra.
	 * 
	 * @param addGame
	 *            valore di verita' associato all'abilitazione del tasto per la
	 *            creazione di una partita.
	 * @param removeGame
	 *            valore di verita' associato all'disabilitazione del tasto per
	 *            la creazione di una partita.
	 * @param joinGame
	 *            valore di verita' associato all'abilitazione del tasto per la
	 *            partecipazione ad una partita.
	 */
	public void setButtonsEnabled(boolean addGame, boolean removeGame, boolean joinGame) {
		btnAddGame.setEnabled(addGame);
		mntmAddYourGame.setEnabled(addGame);
		btnRemoveGame.setEnabled(removeGame);
		mntmRemoveYourGame.setEnabled(removeGame);
		btnJoinSelected.setEnabled(joinGame);
		mntmJoinSelectedGame.setEnabled(joinGame);
	}

	/*
	 * Inizializzazione della finestra.
	 */
	private void initialize() {
		frmTictactoe.addWindowListener( new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		        JFrame frame = (JFrame)e.getSource();
		        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit the application?",
		            "Exit Application",JOptionPane.YES_NO_OPTION);
		        if (result == JOptionPane.YES_OPTION)
		            ctrl.quitActionPerformed(null, true);
		    }
		});
		panelGameList = new JPanel();
		panelGameList.setAutoscrolls(true);
		panelGameList.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		
		JPanel panelToolBar = new JPanel();
		
		JLabel lblGameBoardSelect = new JLabel("Select a game and join in or add your own game and wait for an opponent");
		lblGameBoardSelect.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/clipboard-list.png")));
		lblGameBoardSelect.setForeground(Color.BLACK);
		lblGameBoardSelect.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JPanel panelStatusBar = new JPanel();
		panelStatusBar.setBorder(null);
		GroupLayout groupLayout = new GroupLayout(frmTictactoe.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblGameBoardSelect, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(panelGameList, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelToolBar, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
						.addComponent(panelStatusBar, GroupLayout.PREFERRED_SIZE, 438, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGameBoardSelect)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelGameList, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
		labelStatusBat.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/infocard.png")));
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
		
		listModel = new DefaultListModel();
		
		JLabel labelReady = new JLabel("");
		labelReady.setEnabled(false);
		labelReady.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/status.png")));
		GridBagConstraints gbc_labelReady = new GridBagConstraints();
		gbc_labelReady.insets = new Insets(0, 0, 0, 5);
		gbc_labelReady.gridx = 3;
		gbc_labelReady.gridy = 0;
		panelStatusBar.add(labelReady, gbc_labelReady);
		
		JLabel labelBusy = new JLabel("");
		labelBusy.setEnabled(false);
		labelBusy.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/status-busy.png")));
		GridBagConstraints gbc_labelBusy = new GridBagConstraints();
		gbc_labelBusy.gridx = 4;
		gbc_labelBusy.gridy = 0;
		panelStatusBar.add(labelBusy, gbc_labelBusy);
		panelToolBar.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		panelToolBar.add(toolBar, BorderLayout.NORTH);
		
		btnAddGame = new JButton(" Add your game");
		btnAddGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddGame.setMinimumSize(new Dimension(175, 29));
		btnAddGame.setMaximumSize(new Dimension(175, 29));
		btnAddGame.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/tag--plus.png")));
		toolBar.add(btnAddGame);
		btnAddGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.addGameActionPerformed(e);
			}
		});
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_2);
		
		btnRemoveGame = new JButton(" Remove your game");
		btnRemoveGame.setEnabled(false);
		btnRemoveGame.setMaximumSize(new Dimension(175, 29));
		btnRemoveGame.setMinimumSize(new Dimension(175, 29));
		btnRemoveGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemoveGame.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/tag--minus.png")));
		toolBar.add(btnRemoveGame);
		btnRemoveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.removeGameActionPermorfed(e);
			}
		});
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_3);

		btnJoinSelected = new JButton(" Join selected game");
		btnJoinSelected.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/tag--arrow.png")));
		btnJoinSelected.setMinimumSize(new Dimension(175, 29));
		btnJoinSelected.setMaximumSize(new Dimension(175, 29));
		btnJoinSelected.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnJoinSelected);
		btnJoinSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] index = gamesList.getSelectedIndices();
				String idGame=(String)gamesList.getSelectedValue();
				if (index.length==0)
					setStatusBar(SELECTION_ERROR_MSG);
				else ctrl.joinGameActionPermorfed(e,idGame);
			}
		});
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_4);
		
		btnRefreshList = new JButton(" Refresh list");
		btnRefreshList.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/list-refresh.png")));
		btnRefreshList.setMinimumSize(new Dimension(175, 29));
		btnRefreshList.setMaximumSize(new Dimension(175, 29));
		btnRefreshList.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnRefreshList);
		btnRefreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.refreshListActionPerformed(e);
			}
		});
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_5);
		
		JButton btnQuit = new JButton(" Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.quitActionPerformed(e,true);
			}
		});
		btnQuit.setIcon(new ImageIcon(GameListGUI.class.getResource("/client/gui/icons/door-open.png")));
		btnQuit.setMinimumSize(new Dimension(175, 29));
		btnQuit.setMaximumSize(new Dimension(175, 29));
		btnQuit.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnQuit);
		
		GridBagLayout gbl_panelGameList = new GridBagLayout();
		gbl_panelGameList.columnWidths = new int[]{0, 0};
		gbl_panelGameList.rowHeights = new int[]{0, 0};
		gbl_panelGameList.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelGameList.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelGameList.setLayout(gbl_panelGameList);
		
		gamesList = new JList(listModel);
		GridBagConstraints gbc_gamesList = new GridBagConstraints();
		gbc_gamesList.fill = GridBagConstraints.BOTH;
		gbc_gamesList.gridx = 0;
		gbc_gamesList.gridy = 0;
		panelGameList.add(gamesList, gbc_gamesList);
		
		frmTictactoe.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmTictactoe.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/application-blue.png")));
		menuBar.add(mnFile);
		
		JMenuItem mntmConnectToServer = new JMenuItem("Connect to Server");
		mntmConnectToServer.setEnabled(false);
		mntmConnectToServer.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/lightning.png")));
		mnFile.add(mntmConnectToServer);
		
		JSeparator separator_6 = new JSeparator();
		mnFile.add(separator_6);
		
		mntmAddYourGame = new JMenuItem("Add Your Game");
		mntmAddYourGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/tag--plus.png")));
		mnFile.add(mntmAddYourGame);
		mntmAddYourGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.addGameActionPerformed(e);
			}
		});
		
		mntmRemoveYourGame = new JMenuItem("Remove Your Game");
		mntmRemoveYourGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/tag--minus.png")));
		mntmRemoveYourGame.setEnabled(false);
		mnFile.add(mntmRemoveYourGame);
		mntmRemoveYourGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.removeGameActionPermorfed(e);
			}
		});
		
		mntmJoinSelectedGame = new JMenuItem("Join Selected Game");
		mntmJoinSelectedGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/tag--arrow.png")));
		mnFile.add(mntmJoinSelectedGame);
		mntmJoinSelectedGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] index = gamesList.getSelectedIndices();
				String idGame=(String)gamesList.getSelectedValue();
				if (index.length==0)
					setStatusBar(SELECTION_ERROR_MSG);
				else ctrl.joinGameActionPermorfed(e,idGame);
			}
		});

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmRefreshList = new JMenuItem("Refresh List");
		mntmRefreshList.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/list-refresh.png")));
		mnFile.add(mntmRefreshList);
		mntmRefreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.refreshListActionPerformed(e);
			}
		});
		
		JSeparator separator_7 = new JSeparator();
		mnFile.add(separator_7);
		
		JMenuItem mntmLeaveGame = new JMenuItem("Leave Game");
		mntmLeaveGame.setEnabled(false);
		mntmLeaveGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/control-stop-180.png")));
		mnFile.add(mntmLeaveGame);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.quitActionPerformed(e,true);
			}
		});
		mntmQuit.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/door-open.png")));
		mnFile.add(mntmQuit);
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/information-balloon.png")));
		menuBar.add(mnAbout);
		
		JMenuItem mntmAuthors = new JMenuItem("Authors");
		mntmAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showAuthorsMessage();
			}
		});
		mntmAuthors.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/hard-hat.png")));
		mnAbout.add(mntmAuthors);
		
		setStatusBar(STATUSBAR_INIT_MSG);
	}
}
