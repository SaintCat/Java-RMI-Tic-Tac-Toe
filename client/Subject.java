package client;

/**
 * Interfaccia utile a implementare il pattern Observer. Rappresenta il subject
 * da osservare.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see Watcher
 */
public interface Subject {

	/**
	 * Consente di aggiungere un osservatore al subject.
	 * 
	 * @param aWatcher osservatore da aggiungere.
	 */
	void add(Watcher aWatcher);

	/**
	 * Consente di rimuovere un osservatore che era stato precedentemente
	 * aggiunto.
	 * 
	 * @param aWatcher osservatore da rimuovere.
	 */
	void remove(Watcher aWatcher);

	/**
	 * Consente di notificare il cambiamento di stato del subject a tutti gli
	 * osservatori aggiunti.
	 */
	void sendNotification();
}
