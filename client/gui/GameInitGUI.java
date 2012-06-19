package client.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JMenuBar;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.regex.Pattern;

/**
 * Classe che implementa la finestra contenitore iniziale dell'interfaccia
 * grafica del client. L'istanza di questa classe rappresenta la schemata
 * iniziale che il client mostra all'utente all'avvio dell'applicazione.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see "WindowBuilder Pro"
 * 
 */
public class GameInitGUI extends GameGUI {

	// Messaggi di notifica predefinti.
	private static final String CONNECTION_REFUSED_MSG = "Connection to server refused";
	private static final String NICKNAME_ALREADY_TAKEN_MSG = "Selected nickame is already taken";
	private static final String NICK_LENGTH_ERROR_MSG = "Nickname must contain at least 3 characters";
	private static final String NICK_CHAR_ERROR_MSG = "Special characters are not allowed";
	private static final String STATUSBAR_INIT_MSG = "Choose a nickname and play with other players";
	
	// Attributi privati.
	private JTextField nicknameField;
	private GUIController ctrl = null;

	/**
	 * Costruttore della classe GameInitGUI.
	 */
	public GameInitGUI(GUIController ctrl) {
		super();
		this.ctrl = ctrl;
		initialize();
	}
	
	/**
	 * Metodo che mostra nella statusbar il messaggio di errore per connessione rifiutata.
	 */
	public void showConnectionRefusedMessage() {
		super.setStatusBar(CONNECTION_REFUSED_MSG);
	}
	
	/**
	 * Metodo che mostra nella statusbar il messaggio di errore per nickname gi� usato.
	 */
	public void showNicknameAlreadyTakenMessage() {
		super.setStatusBar(NICKNAME_ALREADY_TAKEN_MSG);
	}
	
	/*
	 * Metodo per la generazione casuale di un numero interno nell'intervallo [min, max].
	 */
	private int randomCoverIndex(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	/*
	 * Metodo per la validazione del nickname inserito nella casella di testo.
	 * Si desidera che il nickname selezionato contenga solamente caratteri alfanumerici.
	 */
	private boolean validateNickname(String nickname) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		return p.matcher(nickname).matches(); 
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
		            ctrl.quitActionPerformed(null, false);
		    }
		});
		JPanel panelGameList = new JPanel();
		panelGameList.setAutoscrolls(true);
		panelGameList.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		
		JPanel panelToolBar = new JPanel();
		
		JLabel lblAdvLabel = new JLabel("Hi, how are  you? Are you ready to play Tic Tac Toe?");
		lblAdvLabel.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/robot.png")));
		lblAdvLabel.setForeground(Color.BLACK);
		lblAdvLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		
		JPanel panelStatusBar = new JPanel();
		panelStatusBar.setBorder(null);
		
		JPanel textfieldPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frmTictactoe.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAdvLabel, 0, 0, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panelGameList, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textfieldPanel, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
								.addComponent(panelToolBar, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)))
						.addComponent(panelStatusBar, GroupLayout.PREFERRED_SIZE, 438, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAdvLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panelGameList, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textfieldPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelToolBar, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)))
					.addGap(9)
					.addComponent(panelStatusBar, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(8, Short.MAX_VALUE))
		);
		panelToolBar.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		panelToolBar.add(toolBar, BorderLayout.NORTH);
		textfieldPanel.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator_6 = new JSeparator();
		toolBar.add(separator_6);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_7);
		
		JButton btnNewButton = new JButton(" Connect to server");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.setMinimumSize(new Dimension(175, 29));
		btnNewButton.setMaximumSize(new Dimension(175, 29));
		btnNewButton.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/lightning.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nickname = nicknameField.getText();
				if (nickname.trim().equals("") || nickname.trim().length() < 3)
					setStatusBar(NICK_LENGTH_ERROR_MSG);
				else {
					if (validateNickname(nickname))
						ctrl.initToListActionPerformed(e,
								(nickname.substring(0,1)).toUpperCase()
								+ nickname.substring(1).toLowerCase());
					else
						setStatusBar(NICK_CHAR_ERROR_MSG);
				}
			}
		});
		toolBar.add(btnNewButton);
		
		JButton btnQuit = new JButton(" Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.quitActionPerformed(e, false);
			}
		});
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(UIManager.getColor("Button.background"));
		toolBar.add(separator_2);
		btnQuit.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/door-open.png")));
		btnQuit.setMinimumSize(new Dimension(175, 29));
		btnQuit.setMaximumSize(new Dimension(175, 29));
		btnQuit.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnQuit);
		
		JLabel lblNicknameLabel = new JLabel("Insert your nickname:");
		lblNicknameLabel.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/selection-input.png")));
		lblNicknameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		textfieldPanel.add(lblNicknameLabel, BorderLayout.NORTH);
		
		nicknameField = new JTextField(10);
		nicknameField.setDocument(new JTextFieldLimit(12));
		nicknameField.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		textfieldPanel.add(nicknameField, BorderLayout.SOUTH);
		nicknameField.setColumns(12);
		GridBagLayout gbl_panelStatusBar = new GridBagLayout();
		gbl_panelStatusBar.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelStatusBar.rowHeights = new int[]{0, 0};
		gbl_panelStatusBar.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelStatusBar.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelStatusBar.setLayout(gbl_panelStatusBar);
		
		JLabel labelStatusBat = new JLabel("");
		labelStatusBat.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/infocard.png")));
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
		
		JLabel labelReady = new JLabel("");
		labelReady.setEnabled(false);
		labelReady.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/status.png")));
		GridBagConstraints gbc_labelReady = new GridBagConstraints();
		gbc_labelReady.insets = new Insets(0, 0, 0, 5);
		gbc_labelReady.gridx = 3;
		gbc_labelReady.gridy = 0;
		panelStatusBar.add(labelReady, gbc_labelReady);
		
		JLabel labelBusy = new JLabel("");
		labelBusy.setEnabled(false);
		labelBusy.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/status-busy.png")));
		GridBagConstraints gbc_labelBusy = new GridBagConstraints();
		gbc_labelBusy.gridx = 4;
		gbc_labelBusy.gridy = 0;
		panelStatusBar.add(labelBusy, gbc_labelBusy);
		
		GridBagLayout gbl_panelGameList = new GridBagLayout();
		gbl_panelGameList.columnWidths = new int[]{0, 0};
		gbl_panelGameList.rowHeights = new int[]{0, 0};
		gbl_panelGameList.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelGameList.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelGameList.setLayout(gbl_panelGameList);
		
		JLabel lblCoverLabel = new JLabel("New label");
		lblCoverLabel.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/back-" + randomCoverIndex(1,3) + ".png")));
		GridBagConstraints gbc_lblCoverLabel = new GridBagConstraints();
		gbc_lblCoverLabel.gridx = 0;
		gbc_lblCoverLabel.gridy = 0;
		panelGameList.add(lblCoverLabel, gbc_lblCoverLabel);
		
		frmTictactoe.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmTictactoe.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/application-blue.png")));
		menuBar.add(mnFile);
		
		JMenuItem mntmConnectToServer = new JMenuItem("Connect to Server");
		mntmConnectToServer.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/lightning.png")));
		mnFile.add(mntmConnectToServer);
		mntmConnectToServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nickname = nicknameField.getText();
				if (nickname.trim().equals("") || nickname.trim().length() < 3)
					setStatusBar(NICK_LENGTH_ERROR_MSG);
				else {
					if (validateNickname(nickname))
						ctrl.initToListActionPerformed(e,
								(nickname.substring(0,1)).toUpperCase()
								+ nickname.substring(1).toLowerCase());
					else
						setStatusBar(NICK_CHAR_ERROR_MSG);
				}
			}
		});
		
		JSeparator separator_3 = new JSeparator();
		mnFile.add(separator_3);
		
		JMenuItem mntmAddYourGame = new JMenuItem("Add Your Game");
		mntmAddYourGame.setEnabled(false);
		mntmAddYourGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/tag--plus.png")));
		mnFile.add(mntmAddYourGame);
		
		JMenuItem mntmRemoveYourGame = new JMenuItem("Remove Your Game");
		mntmRemoveYourGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/tag--minus.png")));
		mntmRemoveYourGame.setEnabled(false);
		mnFile.add(mntmRemoveYourGame);
		
		JMenuItem mntmJoinSelectedGame = new JMenuItem("Join Selected Game");
		mntmJoinSelectedGame.setEnabled(false);
		mntmJoinSelectedGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/tag--arrow.png")));
		mnFile.add(mntmJoinSelectedGame);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmRefreshList = new JMenuItem("Refresh List");
		mntmRefreshList.setEnabled(false);
		mntmRefreshList.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/list-refresh.png")));
		mnFile.add(mntmRefreshList);
		
		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);
		
		JMenuItem mntmLeaveGame = new JMenuItem("Leave Game");
		mntmLeaveGame.setEnabled(false);
		mntmLeaveGame.setIcon(new ImageIcon(GameInitGUI.class.getResource("/client/gui/icons/control-stop-180.png")));
		mnFile.add(mntmLeaveGame);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.quitActionPerformed(e,false);
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
	
	/*
	 * Un JTextFieldLimit e' un componente grafico che consente l'input di una
	 * stringa di testo con numero di caratteri limitato.
	 */
	private class JTextFieldLimit extends PlainDocument {

		private static final long serialVersionUID = 1L;
		private int limit;

		/*
		 * Costruttore della classe JTextFieldLimit.
		 */
		public JTextFieldLimit(int limit) {
			super();
			this.limit = limit; // Limite sul numero di caratteri.
		}

		/*
		 * Metodo per la gestione dell'inserimento della stringa nel campo di
		 * testo.
		 */
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str != null) 
				if ((getLength() + str.length()) <= limit)
					super.insertString(offset, str, attr);
		}
		
	} // Chiusura della inner-class.
	
}