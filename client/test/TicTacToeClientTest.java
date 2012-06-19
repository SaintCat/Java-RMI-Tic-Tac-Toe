package client.test;

import org.junit.*;
import static org.junit.Assert.*;

import api.*;
import client.*;
import server.*;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class TicTacToeClientTest {

	private TicTacToeClient cl;
	private PlayerImpl p1;
	private TicTacToeClient cl2;
	
	private Registry reg;
	
	@Before
	public void init() throws MalformedURLException, RemoteException, NotBoundException{
		
		try
		{ reg=LocateRegistry.createRegistry(1099);}
		catch (java.rmi.RemoteException re){
			System.out.print("Errore nel creare il registro");
		}
		
		TicTacToeServer server = new TicTacToeServerImpl();
		Naming.rebind("tictactoe", server);
		
		cl= new TicTacToeClient();
		p1= new PlayerImpl();
		cl2 = new TicTacToeClient();
	}
	
	@After
	public void close(){
		
		try {
			reg.unbind("tictactoe");
			UnicastRemoteObject.unexportObject(reg, true);
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRegisterPlayer() throws RemoteException{
		
		assertTrue(cl.registerPlayer("bappo"));
	}
	
	@Test
	public void testDeregisterPlayer() throws RemoteException{
		
		cl.registerPlayer("bappo");
		assertFalse(cl.registerPlayer("bappo"));  //faccio vedere che se un player prova a registrarsi con lo stesso nickname la registrazione fallisce, mentre ha successo se il nickname viene deregistrato
		cl.deregisterPlayer("bappo");
		assertTrue(cl.registerPlayer("bappo"));
	}
	
	@Test
	public void testCreateGame() throws MalformedURLException, RemoteException, NotBoundException{
		
		cl.registerPlayer("bappo");
		
		cl.createGame("bappo-15:50:20");
		assertEquals("bappo-15:50:20", cl.getIdGameCreated());
		assertTrue(cl.isCreator());
	}
	
	@Test
	public void testJoinGame() throws RemoteException, MalformedURLException, NotBoundException, GameJoiningException{
		
		cl.registerPlayer("gennaro");
		cl.createGame("gennaro-13:22:22");
		
		cl2.registerPlayer("mirko");
		cl2.joinGame("gennaro-13:22:22");
		
		assertFalse(cl2.isCreator());
		assertEquals("gennaro-13:22:22", cl2.getIdGameJoined());
	}
	
	@Test
	public void testSendReceiveMove() throws RemoteException, MalformedURLException, NotBoundException, GameJoiningException{
		
		cl.registerPlayer("bappo");
		cl2.registerPlayer("gennaro");
		
		cl.createGame("bappo-15:50:20");
		
		cl2.joinGame("bappo-15:50:20");
			
		cl.sendMove(0);
		
		int grid1[][]=cl.getGrid();
		assertEquals(Game.P1_CELL, grid1[0/Game.GRID_DIMENSION][0%Game.GRID_DIMENSION]);
		
		cl2.receiveMove();
		int grid2[][]=cl2.getGrid();
		assertEquals(Game.P2_CELL, grid2[0/Game.GRID_DIMENSION][0%Game.GRID_DIMENSION]);
	}
	
	@Test
	public void testRemoveGame() throws MalformedURLException, RemoteException, NotBoundException, GameJoiningException{
		
		cl.registerPlayer("bappo");
		
		cl.createGame("bappo-15:50:20");
		assertTrue(cl.receiveList().contains("bappo-15:50:20"));
		cl.removeGame();
		assertFalse(cl.receiveList().contains("bappo-15:50:20"));
	}
	
	@Test
	public void testReceiveList() throws MalformedURLException, RemoteException, NotBoundException, GameJoiningException{
		
		cl.registerPlayer("bappo");
		
		cl.createGame("bappo-15:50:20");
		
		cl2.registerPlayer("genni");
		
		cl2.joinGame("bappo-15:50:20");
		
		ArrayList<String> l = cl.receiveList();
		assertTrue(l.contains("bappo-15:50:20"));
	}
	
	@Test
	public void testGetSet() throws RemoteException{
		
		GameImpl g= new GameImpl(p1, "gennaro-13:23:22");
		
		cl.setGame(g);
		assertEquals(g, cl.getGame());
		
		cl.setPlayer(p1);
		assertEquals(p1, cl.getPlayer());
	}
}
