package api;

import java.rmi.*;
import java.util.*;

/**
 * Interfaccia dell'oggetto remoto TicTacToeServer.
 * 
 * @author Gennaro Capo
 * @author Mirko Conte
 * @author Vincenzo De Notaris
 * @author Roberto Pacilio
 * @version 1.0
 * @see server.TicTacToeServerImpl
 * 
 */
public interface TicTacToeServer extends Remote {

	/**
	 * Consente di registrare un player al server, dato un nickname. All'atto
	 * della registrazione non possono essere presenti due player con uno stesso
	 * nickname.
	 * 
	 * @param p player da registrare.
	 * @param playerName nickname del player.
	 * @return true se la registrazione e' andata a buon fine,ovvero non sono
	 *         gia' presenti altri player con lo stesso nickname false
	 *         altrimenti.
	 * @throws RemoteException
	 */
	boolean register(Player p, String playerName) throws RemoteException;

	/**
	 * Consente di creare una nuova partita a un player, dato un idGame
	 * 
	 * @param p player che crea la partita
	 * @param idGame deve avere il seguente formato: 
	 * 			     nickname-ora_di_creazione_partita.<br/>
	 *               Es: mrossi-12:23:22
	 * @throws RemoteException
	 */
	void createGame(Player p, String idGame) throws RemoteException;

	/**
	 * Consente ad un player di aggiungersi a una partita,avente come id
	 * <code>idgame</code>, creata da un altro player e cominciare cosi a
	 * giocare.
	 * 
	 * @param p player che vuole aggiungersi alla partita
	 * @param idGame deve avere il seguente formato:
	 *               nickname-ora_di_creazione_partita.<br/>
	 *               Es: mrossi-12:23:22.
	 * @throws RemoteException
	 * @throws GameJoiningException
	 *             se la partita e' gia' completa oppure non e' piu' disponibile
	 *             perche' e' stata cancellata.
	 */
	void joinGame(Player p, String idGame) throws RemoteException,
			GameJoiningException;

	/**
	 * Restituisce la lista di tutti i player che hanno creato una partita e che
	 * sono in attesa di uno sfidante oppure stanno ancora giocando.
	 * 
	 * @return lista di player in attesa o con partita in corso.
	 * @throws RemoteException
	 */
	ArrayList<String> getAllChallengers() throws RemoteException;

	/**
	 * Consente di rimuovere una partita dalla lista delle partite mantenuta sul
	 * server.
	 * 
	 * @param idGame id della partita da eliminare.Deve avere il seguente formato:
	 *               nickname-ora_di_creazione_partita.<br/>
	 *               Es: mrossi-12:23:22.
	 * @return lista aggiornata dei player dopo la rimozione.
	 * @throws RemoteException
	 */
	ArrayList<String> removeGame(String idGame) throws RemoteException;

	/**
	 * Consente di deregistrare un player, dato il suo nickname quando questi si
	 * disconnette dall'applicazione.
	 * 
	 * @param playerName
	 *            nickname del player da deregistrare.
	 * @throws RemoteException
	 */
	void deregister(String playerName) throws RemoteException;

}
