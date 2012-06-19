package client.test;

import org.junit.*;
import static org.junit.Assert.*;

import java.rmi.RemoteException;

import server.*;
import client.*;
import api.*;

public class PlayerImplTest {
	
	private static GameImpl game;
	private static PlayerImpl p1;
	private static PlayerImpl p2;
	private static String id="mygame";
	
	@BeforeClass
	public static void init() throws RemoteException{
		
		p1=new PlayerImpl();
		p2=new PlayerImpl();
		game= new GameImpl(p1, id);
		game.setPlayer2(p2);
		
	}
	
	@Test 
	public void testGetSet() {
	
		p1.setName("bappo");
		assertEquals("bappo", p1.getName());
		
		p1.setFoeSelectedMove(3);
		assertEquals(3,p1.getFoeSelectedMove());
		
		p1.setEvent(Event.YOUR_TURN);
		assertEquals(Event.YOUR_TURN, p1.getEvent());
		
		p1.setGame(game);
		assertSame(game, p1.getGame()); 
		
		p2.setName("maccio");
		assertEquals("maccio", p2.getName());
		
		p2.setFoeSelectedMove(1);
		assertEquals(1,p2.getFoeSelectedMove());
		
		p2.setEvent(Event.YOUR_TURN);
		assertEquals(Event.YOUR_TURN, p2.getEvent());
		
		p2.setGame(game);
		assertSame(game, p2.getGame()); 

	}
	
	@Test
	public void testJoinGame(){
		
		p1.joinGame(game);
		assertEquals(game, p1.getGame());
		assertEquals(Event.MATCH_STARTED, p1.getEvent());	
		
		p2.joinGame(game);
		assertEquals(game, p2.getGame());
		assertEquals(Event.MATCH_STARTED, p2.getEvent());	
	}
	
	@Test
	public void testIsYourTurn(){
		
		p1.isYourTurn();
		assertEquals(Event.YOUR_TURN, p1.getEvent());	
		
		p2.isYourTurn();
		assertEquals(Event.YOUR_TURN, p2.getEvent());
	}
	
	@Test
	public void testFoeTurn(){
		
		p1.foeTurn();
		assertEquals(Event.FOE_TURN, p1.getEvent());	
		
		p2.foeTurn();
		assertEquals(Event.FOE_TURN, p2.getEvent());
	}
	
	@Test
	public void testFoeDisconnected(){
		
		p1.foeDisconnected();
		assertEquals(Event.FOE_DISCONNECTED, p1.getEvent());
		
		p2.foeDisconnected();
		assertEquals(Event.FOE_DISCONNECTED, p2.getEvent());
	}
	
	@Test
	public void testFoeHasMoved(){
		
		p1.foeHasMoved(2);
		assertEquals(2, p1.getFoeSelectedMove());
		assertEquals(Event.FOE_MOVED, p1.getEvent());
		
		p2.foeHasMoved(4);
		assertEquals(4, p2.getFoeSelectedMove());
		assertEquals(Event.FOE_MOVED, p2.getEvent());
	}
	
	@Test
	public void testSendGameresult(){
		
		p1.sendGameResult(Event.YOU_WIN);
		assertEquals(Event.YOU_WIN, p1.getEvent());
	}
	
	

}
