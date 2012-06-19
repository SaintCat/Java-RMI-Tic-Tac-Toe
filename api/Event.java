package api;

/**
 * Rappresenta tutti gli eventi che si possono verificare all'interno del gioco:<br/>
 * 
 * MATCH_STARTED:la partita e' cominciata;<br/>
 * FOE_MOVED: l'avversario ha effettuato la sua mossa;<br/>
 * FOE_DISCONNECTED: l'avversario si e' disconnesso;<br/>
 * YOU_WIN: Hai vinto;<br/>
 * YOU_LOSE: Hai perso;<br/>
 * DRAW: Hai pareggiato;<br/>
 * YOUR_TURN: E' il tuo turno per effettuare la mossa;<br/>
 * FOE_TURN: E' il turno dell'avversario per effettuare la mossa.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * 
 */
public enum Event {
	FOE_MOVED, FOE_DISCONNECTED, YOU_WIN, YOU_LOSE, DRAW, YOUR_TURN, FOE_TURN, MATCH_STARTED
}
