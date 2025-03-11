package tictactoe.tictactoe;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;

/**
 * Diese Klasse ist ein JavaFX Service, der Daten vom Server empfängt.
 * Sie liest den aktuellen Spielstatus und das Spielfeld aus dem Datenstrom.
 */
public class NetworkClientServiceReceiver extends Service<String[]> {
    private final DataInputStream serverDataInputStream;

    /**
     * Konstruktor, der den Eingabestream des Servers setzt.
     *
     * @param dataInputStream Der Datenstrom, aus dem die Spielfelder und Statusmeldungen gelesen werden.
     */
    public NetworkClientServiceReceiver(DataInputStream dataInputStream) {
        this.serverDataInputStream = dataInputStream;
    }

    /**
     * Erstellt eine neue asynchrone Aufgabe, die das Spielfeld und den Spielstatus vom Server liest.
     *
     * @return Eine Task-Instanz, die die empfangenen Daten zurückgibt.
     */
    @Override
    protected Task<String[]> createTask() {
        return new Task<>() {
            @Override
            protected String[] call() throws Exception {
                var board = serverDataInputStream.readUTF();
                var status = serverDataInputStream.readUTF();

                return new String[] {board, status};
            }
        };
    }
}