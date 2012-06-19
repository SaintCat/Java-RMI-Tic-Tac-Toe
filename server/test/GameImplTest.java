package server.test;

import org.junit.*;
import static org.junit.Assert.*;

import java.rmi.RemoteException;

import server.*;
import server.GameImpl.PlayerNumber;
import api.*;
import client.*;

public class GameImplTest {
	
	private PlayerImpl p1;
	private PlayerImpl p2;
	private GameImpl game1; //inizializzato con un solo player
	private GameImpl game2; //inizializzato con 2 player
	private String id="mygame-12:32:22";
	
	
	@Before
	public void init() throws RemoteException{
		
		this.p1=new PlayerImpl();
	    this.p2= new PlayerImpl();
		this.game1=new GameImpl(p1, id);
		this.game2=new GameImpl(p1, id);
		game2.setPlayer2(p2);
	}
	
	@Test
	public void testGetSetEndGame() throws RemoteException{
		
		assertFalse(game1.isEndGame());
		game1.setEndGame(true);
		assertTrue(game1.isEndGame());
		game1.setEndGame(false);
		assertFalse(game1.isEndGame());
	}
	
	@Test
	public void testSetPlayer2() throws RemoteException{
		
		game1.setPlayer2(p2);
		assertFalse(game1.isEndGame());
	}
	
	@Test
	public void testGetSetId(){
		
		game1.setIdGame("partita-19:22:14");
		assertEquals("partita-19:22:14", game1.getIdGame());
	}
	@Test
	public void testSendMovementP1Win() throws RemoteException {
		GameImpl.PlayerNumber p= game2.getCurrentPlayer();
		if (p.equals(PlayerNumber.P1)){
			game2.sendMovement(p1, 0);
			assertFalse(game2.isEndGame());
			assertEquals(Event.FOE_TURN, p1.getEvent());
			assertEquals(Event.YOUR_TURN, p2.getEvent());

			game2.sendMovement(p2, 1);
			assertFalse(game2.isEndGame());
			assertEquals(Event.YOUR_TURN, p1.getEvent());
			assertEquals(Event.FOE_TURN, p2.getEvent());

			game2.sendMovement(p1, 3);
			assertFalse(game2.isEndGame());
			assertEquals(Event.FOE_TURN, p1.getEvent());
			assertEquals(Event.YOUR_TURN, p2.getEvent());

			game2.sendMovement(p2, 4);
			assertFalse(game2.isEndGame());
			assertEquals(Event.YOUR_TURN, p1.getEvent());
			assertEquals(Event.FOE_TURN, p2.getEvent());

			game2.sendMovement(p1, 6);
			assertTrue(game2.isEndGame());
			assertEquals(Event.YOU_WIN, p1.getEvent());
			assertEquals(Event.YOU_LOSE, p2.getEvent());}
	}

	@Test
	public void testSendMovementP2Win() throws RemoteException {

		GameImpl.PlayerNumber p= game2.getCurrentPlayer();
		if (p.equals(PlayerNumber.P2)){
			System.out.print("p2win");
			game2.sendMovement(p1, 0);
			assertFalse(game2.isEndGame());
			assertEquals(Event.FOE_TURN, p1.getEvent());
			assertEquals(Event.YOUR_TURN, p2.getEvent());

			game2.sendMovement(p2, 1);
			assertFalse(game2.isEndGame());
			assertEquals(Event.YOUR_TURN, p1.getEvent());
			assertEquals(Event.FOE_TURN, p2.getEvent());

			game2.sendMovement(p1, 3);
			assertFalse(game2.isEndGame());
			assertEquals(Event.FOE_TURN, p1.getEvent());
			assertEquals(Event.YOUR_TURN, p2.getEvent());

			game2.sendMovement(p2, 4);
			assertFalse(game2.isEndGame());
			assertEquals(Event.YOUR_TURN, p1.getEvent());
			assertEquals(Event.FOE_TURN, p2.getEvent());

			game2.sendMovement(p1, 2);
			assertFalse(game2.isEndGame());
			assertEquals(Event.FOE_TURN, p1.getEvent());
			assertEquals(Event.YOUR_TURN, p2.getEvent());

			game2.sendMovement(p2, 7);
			assertTrue(game2.isEndGame());
			assertEquals(Event.YOU_LOSE, p1.getEvent());
			assertEquals(Event.YOU_WIN, p2.getEvent());	}
	}

	@Test
	public void testSendMovementDraw() throws RemoteException {
		
		
		game2.sendMovement(p1, 0);
		assertFalse(game2.isEndGame());
		assertEquals(Event.FOE_TURN, p1.getEvent());
		assertEquals(Event.YOUR_TURN, p2.getEvent());
		
		game2.sendMovement(p2, 1);
		assertFalse(game2.isEndGame());
		assertEquals(Event.YOUR_TURN, p1.getEvent());
		assertEquals(Event.FOE_TURN, p2.getEvent());
		
		game2.sendMovement(p1, 3);
		assertFalse(game2.isEndGame());
		assertEquals(Event.FOE_TURN, p1.getEvent());
		assertEquals(Event.YOUR_TURN, p2.getEvent());
		
		game2.sendMovement(p2, 4);
		assertFalse(game2.isEndGame());
		assertEquals(Event.YOUR_TURN, p1.getEvent());
		assertEquals(Event.FOE_TURN, p2.getEvent());
		
		game2.sendMovement(p1, 2);
		assertFalse(game2.isEndGame());
		assertEquals(Event.FOE_TURN, p1.getEvent());
		assertEquals(Event.YOUR_TURN, p2.getEvent());
		
		game2.sendMovement(p2, 6);
		assertFalse(game2.isEndGame());
		assertEquals(Event.YOUR_TURN, p1.getEvent());
		assertEquals(Event.FOE_TURN, p2.getEvent());	
		
		game2.sendMovement(p1, 7);
		assertFalse(game2.isEndGame());
		assertEquals(Event.FOE_TURN, p1.getEvent());
		assertEquals(Event.YOUR_TURN, p2.getEvent());
	
		game2.sendMovement(p2, 5);
		assertFalse(game2.isEndGame());
		assertEquals(Event.YOUR_TURN, p1.getEvent());
		assertEquals(Event.FOE_TURN, p2.getEvent());
		
		game2.sendMovement(p1, 8);
		assertTrue(game2.isEndGame());
		assertEquals(Event.DRAW, p1.getEvent());
		assertEquals(Event.DRAW, p2.getEvent());
	}

}
