Java-RMI-Tic-Tac-Toe
====================
Implementazione Java del gioco Tic Tac Toe (tris) mediante Remote Method Invocation.

*Autori: Gennato Capo, Mirko Conte, Vincenzo De Notaris, Roberto Pacilio*

*Versione: 1.0 del 10 Maggio 2012.*

## 1. Introduzione

### 1.1 Scopo del documento
Scopo del presente documento è quello di definire i requisiti funzionali individuati per la nostra versione del gioco del Tris distribuito. Una volta chiarite in maniera esauriente le funzionalità dell'applicazione ed il modo in cui l'utente interagisce con essa, cercheremo di illustrare le scelte progettuali effettuate per la sua realizzazione.


### 1.2 Panoramica del documento
Il capitolo 2 presenta una panoramica generale delle funzionalità dell'applicazione.
Il capitolo 3 descrive nel dettaglio le interazioni dell'utente con l'applicazione.
Il capitolo 4 si occuperà di illustrare le scelte progettuali.
Infine il capitolo 5 conclude il documento con una serie di screenshot ed un diagramma a stati finiti per illustrare il funzionamento dell’applicazione.

## 2. Descrizione generale

### 2.1 Scopo dell'applicazione
L'applicazione è un clone del gioco del Tris, realizzata però in modo da permettere a due giocatori, ubicati su macchine differenti, di sfidarsi in una partita.

### 2.2 Funzioni del prodotto

#### 2.2.1 Identificazione dei ruoli
L'applicazione dovrà gestire un unico ruolo, ovvero quello del giocatore. Ovviamente per ogni partita sarà necessario gestire due giocatori. Ogni giocatore sarà identificato da un nickname univoco.

#### 2.2.2 Partita
Per ogni partita sarà necessario tenere traccia delle seguenti informazioni:

* la coppia di giocatori che si sta sfidando;
* lo stato della scacchiera;
* a quale giocatore spetta effettuare la mossa;
* se la partita è terminata.

## 3. Requisiti specifici

### 3.1 Requisiti funzionali
Di seguito vengono riportate le funzionalità dell'applicazione.

Le funzioni saranno presentate utilizzando il seguente schema:

1. Identificativo;
2. Funzione;
3. Descrizione;
4. Input;
5. Output;
6. Precondizione;
7. Postcondizione;
8. Condizione di errore.

#### 3.1.1 Connessione al server

* **Identificativo:** *F1*;

* **Funzione:** connette un giocatore al server;

* **Descrizione:** connette un giocatore al server dopo che ha inserito il proprio nickname;

* **Input:** nickname del giocatore;

* **Output:** sarà visualizzata la game room o un messaggio d'errore;

* **Precondizioni:** nessuna;

* **Postcondizioni:** il giocatore sarà regolarmente registrato nel sistema col proprio nickname;

* **Condizioni d'errore:** è già presente un giocatore con lo stesso nickname; non si riesce a raggiungere il server.

#### 3.1.2 Creazione di una nuova partita

* **Identificativo:** *F2*;

* **Funzione:** crea una nuova partita;

* **Descrizione:** consente ad un giocatore di creare una nuova partita;

* **Input:** nessuno;

* **Output:** la game room sarà aggiornata per contenere la partita appena creata;

* **Precondizioni:** il giocatore deve essere connesso al server con un nickname valido; un giocatore non può aggiungere una partita se ne ha creata un’altra in precedenza ed essa è ancora in attesa d’essere giocata;

* **Postcondizioni:** la nuova partita sarà registrata sul server;

* **Condizioni d'errore:** non si riesce a raggiungere il server.

#### 3.1.3 Iscrizione ad una partita

**Identificativo:** *F3*;

* **Funzione:** iscrizione ad una partita;

* **Descrizione:** consente ad un giocatore di prendere parte ad una partita;

* **Input:** la partita alla quale si vuole prendere parte;

* **Output:** sarà visualizzata la griglia per giocare la partita;

* **Precondizioni:** il giocatore deve essere connesso al server con un nickname valido; deve esserci necessariamente almeno una partita disponibile ed essa;

* **Postcondizioni:** deve essere stata creata da un altro giocatore La partita non sarà più disponibile nella game room;

* **Condizioni d'errore:** non si riesce a raggiungere il server.

#### 3.1.4 Cancellazione di una partita

* **Identificativo:** *F4*;

* **Funzione:** cancellazione di una partita;

* **Descrizione:** consente ad un giocatore di cancellare una partita;

* **Input:** la partita che si vuole eliminare;

* **Output:** sarà aggiornata la game room;

* **Precondizioni:** il giocatore deve essere connesso al server con un nickname valido; la partita che si vuole eliminare deve essere stata creata dal giocatore che desidera eliminarla;

* **Postcondizioni:** la partita viene cancellata dal server;

* **Condizioni d'errore:** non si riesce a raggiungere il server.

#### 3.1.5 Giocare una partita

* **Identificativo:** *F5*;   
 
* **Funzione:** giocare una partita;

* **Descrizione:** consente ad un giocatore di giocare una partita

* **Input:** nessuno;

* **Output:** l’esito della partita;

* **Precondizioni:** il giocatore deve essere connesso al server con un nickname valido; La partita che si vuole giocare deve avere due giocatori iscritti

* **Postcondizioni:** la partita viene cancellata dal server;

* **Condizioni d'errore:** non si riesce a raggiungere il server; uno dei due giocatori si disconnette (la partita è cancellata dal server).

## 4. Scelte progettuali
Di seguito s’illustreranno le scelte progettuali. Si farà ampio uso di diagrammi UML per mostrare in maniera più chiara le relazioni fra le classi. Per le descrizioni esaustive dei metodi, si rimanda alla documentazione Javadoc allegata al progetto.

### 4.1 Diagramma delle classi in UML

![Diagramma UML delle classi](https://github.com/vdenotaris/Java-RMI-Tic-Tac-Toe/raw/master/images/umlclassdiagram.png)

L’immagine mostra come nel package ``api`` siano presenti le interfacce remote per gli oggetti distribuiti (``Player``, ``Game`` e ``TicTacToeServer``) più una classe che realizza un’eccezione (``GameJoiningException``) ed un enum (``Event``) che contiene etichette utili per identificare lo stato della partita.

Il package ``server`` contiene l’implementazione di due interfacce remote (``GameImpl`` e ``TicTacToeServerImpl``).


Il package ``client`` contiene due classi: ``PlayerImpl``, che implementa l’interfaccia remota ``Player``, e ``TicTacToeClient``. Le due interfacce (``Watcher`` e ``Subject``) sono necessarie per implementare il pattern *Observer* aggirando il problema dell’ereditarietà multipla, in quanto ``PlayerImpl`` (da osservare) estende già la classe ``UnicastRemoteObject``.

Infine il package ``client.gui`` contiene tutte le classi che gestiscono l’interfaccia grafica del gioco.

### 4.2 Strategie di test
Di seguito s’illustreranno le due strategie di test condotte per verificare il corretto funzionamento dell’applicazione.

#### 4.2.1 JUnit test
Tramite la libreria ``JUnit`` sono stati eseguiti i test sulla parte di applicativo che realizza la business logic. I test sono stati lanciati dopo ogni update del progetto. È stata creata una classe di test per ogni classe da testare ed è stato implementato almeno un test case per metodo; nel nostro particolare caso le classi sono ``GameImpl``, ``TicTacToeServerImpl``, ``PlayerImpl``, ``TicTacToeClient``. Sono stati creati in tutto 30 metodi di test.

Com’è possibile notare questo numero non corrisponde esattamente al numero di metodi pubblici testati ma ciò è dovuto unicamente al fatto che alcuni metodi sono dipendenti tra di loro (ad esempio i metodi di get e set) e sono stati testati insieme. I test eseguiti sono stati sia funzionali di tipo black box verificando che l’output fosse quello desiderato quando gli input rispettavano le precondizioni, sia di tipo strutturale di tipo white box sfruttando la conoscenza del codice. Quest’ultimo tipo di test si è rilevato fondamentale per correggere alcuni errori trovati nelle prime fasi del progetto. I test funzionali, inoltre, sono stati scelti in modo da coprire i casi eccezionali come il lancio di eccezioni. Per i test strutturali si è cercato di ottenere almeno la copertura delle istruzioni; nei casi in cui abbiamo riscontrato degli errori si è preferito arrivare a coprire le diramazioni o le condizioni.

## 5. Uso dell’applicazione
A conclusione del documento si presentano degli screenshot per illustrare in maniera più esaustiva il funzionamento dell’applicazione lato client.

### 5.1 Schermata di connessione al server

Questa schermata realizza il requisito funzionale *F1*.

![Game Init](https://github.com/vdenotaris/Java-RMI-Tic-Tac-Toe/raw/master/images/gameinit.png)

E' la schermata iniziale che si presenta al giocatore. Consente di scegliere un nickname e di collegarsi al server. Nel caso in cui il nickname scelto sia già registrato sul server, si visualizzerà un messaggio d’errore.

### 5.2 Schermata "game room"

Questa schermata realizza i requisiti funzionali *F2*, *F3* ed *F4*.

![Game Room](https://github.com/vdenotaris/Java-RMI-Tic-Tac-Toe/raw/master/images/gameroom.png)

Vi si accede dopo aver inserito un nickname valido ed essersi collegati al server. Un utente può visualizzare la lista delle partite in attesa di un secondo giocatore e scegliere quella alla quale partecipare. Inoltre ha la possibilità di creare una nuova partita, o eventualmente di rimuoverla. Infine c’è un bottone per effettuare un refresh della lista delle partite disponibili.

### 5.3 Schermata "partita"

Questa schermata realizza il requisito funzionale *F5*. 

![Game Board](https://github.com/vdenotaris/Java-RMI-Tic-Tac-Toe/raw/master/images/gameboard.png)

Consente ad un utente di giocare una partita. Attraverso un meccanismo random verrà deciso quale utente muoverà per primo, se l’utente che ha creato la partita (ovvero con il simbolo X) oppure chi ha fatto “join” sulla partita (ovvero quello con il simbolo O). La status bar presenta sulla destra due indicatori: verde dà il via libera alla mossa, rosso indica che è il turno dell’avversario o la partita è terminata. Sulla sinistra invece si presenta un messaggio testuale che consente di capire qual è lo stato della partita. 

### 5.4 GUI test
Per l’interfaccia grafica sono stati effettuati dei test manuali vagliando la corretta interazione e l'avvicendamento dei componenti grafici dell'applicazione e ricreando alcune condizioni che si possono verificare nella realtà.
￼￼￼￼￼￼￼Le condizioni esaminate sono le seguenti:

* L'utente non può inserire un nickname con meno di 3 caratteri e più di 12;
* L'utente non può inserire caratteri speciali nel nickname ma solo caratteri alfanumerici;
* E' garantita la mutua esclusione tra le diverse finestre che compongono l'interfaccia (ne è visibile sempre una alla volta, in accordo alla sequenza delle azioni compiute dall'utente);
* Le transizioni tra le tre diverse finestre dell'applicazione sono coerenti con le azioni effettuate dall'utente;
* E' garantita l'abilitazione/disabilitazione dei pulsanti a cui sono associate operazioni disponibili/non disponibili in un dato momento;
* La disponibilità degli elementi del menu a comparsa risultano coerenti con l'abilitazione dei pulsanti della finestra corrente a cui essi si riferiscono;
* La barra di stato è sempre aggiornata rispetto allo stato corrente dell'applicazione, quest'ultimo determinato dalle azioni dell'utente;
* E' sempre possibile marcare una casella sulla griglia di quando è il proprio turno. Il simbolo (X o O) è coerente con quello associato al giocatore considerato;
* Non è possibile marcare una casella sulla griglia di gioco quando è il turno del giocatore avversario;
* Non è possibile marcare una casella sulla griglia di gioco quando si verifica una condizione di vittoria, pareggio o sconfitta della partita o in seguito a una disconnessione di uno dei due giocatori;
* Quando si verifica una condizione di vittoria, pareggio o sconfitta di un giocatore a una partita o viene rilevata una disconnessione di uno dei due giocatori, essa viene notificata correttamente;
* ￼Quando una partita finisce (con un risultato oppure a seguito di una disconnessione di un giocatore), essa viene cancellata dalla lista di partite presenti nella game room;
* ￼Un giocatore che crea una partita non può crearne altre e non può aggiungersi a un'altra partita creata da un altro giocatore (i pulsanti di Add New Game e di Join Game sono disabilitati);
* ￼Un giocatore non può rimuovere una partita che non ha creato lui stesso (il pulsante di Remove Game è disabilitato);
* Al termine di una partita, quando il giocatore ritorna nella game room, essa viene automaticamente aggiornata con una la lista delle partite disponibili in quel momento;
* Possibilità per un giocatore di effettuare altre partite in seguito alla fine di un incontro.

### 6. Lanciare l'applicazione in locale

Dopo aver generato i ``.class``, dal lato server è necessario dapprima lanciare il server ``rmiregistry`` per il binding dell'oggetto remoto di bootstrap:

	cd <project_path>/bin

	java rmiregistry

In un altro terminale, in contemporanea:

	cd <project_path>/bin

	java server.StartServer

Dal lato client, è possibile o eseguibile un ``.jar`` opportunamente generato, oppure lanciare sempre da terminale:

	cd <project_path>/bin

	java client.gui.ClientGUI