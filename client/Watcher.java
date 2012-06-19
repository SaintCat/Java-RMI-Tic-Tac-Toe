package client;

/**
 * Interfaccia utile a implementare il pattern Observer. Rappresenta
 * l'osservatore di un subject.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see Subject
 */
public interface Watcher {

	/**
	 * Metodo che viene eseguito non appena viene rilevato un cambiamento nello
	 * stato del subject osservato.
	 */
	void update();

}
