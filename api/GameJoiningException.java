package api;

import java.io.Serializable;

/**
 * Rappresenta un' eccezione che si puo' verificare quando un giocatore vuole
 * aggiungersi a una partita cancellata dalla lista oppure quando la partita e'
 * gia' cominciata e gia' sono presenti due giocatori.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * 
 */
public class GameJoiningException extends Exception implements Serializable {

	private static final long serialVersionUID = -5982382329250873917L;

	/**
	 * Costruttore della classe GameJoingException.
	 */
	public GameJoiningException() {
		super();
	}

	/**
	 * Costruttore con messaggio di errore rilevato.
	 * 
	 * @param message
	 *            messaggio di errore rilevato.
	 */
	public GameJoiningException(String message) {
		super(message);
	}

}