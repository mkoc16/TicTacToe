package tictactoe.tictactoe;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Diese Klasse ist ein JavaFX Service, der Spielzüge an den Server sendet
 * und die Antwort des Servers verarbeitet.
 */
public class NetworkClientServiceSender extends Service<Object[]> {
    private final DataInputStream serverDataInputStream;
    private final DataOutputStream serverDataOutputStream;
    private final int row, col;

    /**
     * Konstruktor, der den Eingabe- und Ausgabestream des Servers sowie die
     * Position des Spielzugs setzt.
     *
     * @param dataInputStream Der Eingabestream des Servers.
     * @param dataOutputStream Der Ausgabestream zum Server.
     * @param row Die Zeilenposition des Spielzugs.
     * @param col Die Spaltenposition des Spielzugs.
     */
    public NetworkClientServiceSender(DataInputStream dataInputStream, DataOutputStream dataOutputStream, int row, int col) {
        this.serverDataInputStream = dataInputStream;
        this.serverDataOutputStream = dataOutputStream;
        this.row = row;
        this.col = col;
    }

    /**
     * Erstellt eine neue asynchrone Aufgabe, die die Spielfeld-Koordinaten
     * an den Server sendet und die Serverantwort verarbeitet.
     *
     * @return Eine Task-Instanz mit den Serverantwortdaten.
     */
    @Override
    protected Task<Object[]> createTask() {
        return new Task<>() {
            @Override
            protected Object[] call() throws Exception {

                serverDataOutputStream.writeInt(row);
                serverDataOutputStream.writeInt(col);

                var legalMove = serverDataInputStream.readBoolean();

                if (!legalMove) {
                    return new Object[] { false };
                }

                var board = serverDataInputStream.readUTF();
                var status = serverDataInputStream.readUTF();

                return new Object[] {true, board, status};
            }
        };
    }
}