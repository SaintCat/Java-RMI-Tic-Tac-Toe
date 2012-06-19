package server.test;

import org.junit.*;
import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

import api.*;
import client.*;
import server.*;

public class TicTacToeServerImplTest {

	private TicTacToeServerImpl server;
	private PlayerImpl player1;
	private PlayerImpl player2;

	@Before
	public void init() throws RemoteException {

		server = new TicTacToeServerImpl();
		player1 = new PlayerImpl();
		player2 = new PlayerImpl();
	}

	@Test
	public void testRegister() {

		assertTrue(server.register(player1, "bappo"));
		assertTrue(server.getListPlayers().containsKey("bappo"));
		assertFalse(server.register(player1, "bappo"));
	}

	@Test
	public void testDeregister() {

		server.register(player1, "bappo");
		server.deregister("bappo");
		assertFalse(server.getListPlayers().containsKey("bappo"));

	}

	@Test
	public void testCreateGame() throws RemoteException {

		server.createGame(player1, "mygame-20:22:11");
		assertTrue(server.getListGames().containsKey("mygame-20:22:11"));
	}

	@Test
	public void testJoinGame() throws RemoteException, GameJoiningException {

		server.register(player1, "bappo");
		server.register(player2, "bicienzo");
		server.createGame(player1, "partita1-22:12:12");
		server.joinGame(player2, "partita1-22:12:12");

		GameImpl g1 = (GameImpl) player1.getGame();
		GameImpl g2 = (GameImpl) player2.getGame();

		assertEquals("partita1-22:12:12", g1.getIdGame());
		assertEquals("partita1-22:12:12", g2.getIdGame());
	}

	@Test
	public void testJoinGameNotAvailable() throws RemoteException {
		
		server.register(player1, "bappo");
		server.register(player2, "bicienzo");

		try {
			server.joinGame(player2, "partita1-22:12:12");
		} catch (GameJoiningException e) {
			assertEquals("Game not available.", e.getMessage());
		}
	}

	@Test
	public void testJoinSamePlayers() throws RemoteException {

		server.register(player1, "bappo");
		server.register(player2, "bappo");
		server.createGame(player1, "partita1-22:12:12");

		try {
			server.joinGame(player2, "partita1-22:12:12");
		} catch (GameJoiningException e) {
			assertEquals("Same players.", e.getMessage());
		}
	}

	@Test
	public void testJoinGameFull() throws RemoteException {

		PlayerImpl player3 = new PlayerImpl();

		server.register(player1, "bappo");
		server.register(player2, "bicienzo");
		server.register(player3, "genni");
		server.createGame(player1, "partita1-22:12:12");

		try {
			server.joinGame(player2, "partita1-22:12:12");
		} catch (GameJoiningException e) {
			e.printStackTrace();
		}

		try {
			server.joinGame(player3, "partita1-22:12:12");
		} catch (GameJoiningException e) {

			assertEquals("Game full.", e.getMessage());

		}
	}

	@Test
	public void testGetAllChallengers() throws RemoteException {

		server.register(player1, "mirko");
		server.register(player2, "genni");

		server.createGame(player1, "mirko-15:47:22");
		server.createGame(player2, "genni-15:50:20");

		ArrayList<String> ch = server.getAllChallengers();
		assertTrue(ch.contains("mirko-15:47:22"));
		assertTrue(ch.contains("genni-15:50:20"));
		assertTrue(ch.size() == 2);
	}

	@Test
	public void testRemoveGame() throws RemoteException {

		server.register(player1, "mirko");
		server.register(player2, "genni");

		server.createGame(player1, "mirko-15:47:22");
		server.createGame(player2, "genni-15:50:20");

		server.removeGame("mirko-15:47:22");

		ArrayList<String> ch = server.getAllChallengers();

		assertFalse(ch.contains("mirko-15:47:22"));
		assertTrue(ch.contains("genni-15:50:20"));
		assertTrue(ch.size() == 1);
	}
}
