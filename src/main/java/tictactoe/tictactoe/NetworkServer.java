package tictactoe.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Diese Klasse stellt einen Netzwerkserver für das Tic-Tac-Toe-Spiel bereit.
 * Sie wartet auf die Verbindung von zwei Spielern und startet dann ein neues Spiel.
 */
public class NetworkServer {
    private ServerSocket serverSocket;

    /**
     * Konstruktor, der den Server initialisiert und auf Spieler wartet.
     */
    public NetworkServer() {
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Server gestartet. Warte auf Verbindung auf Port 5555...");

            while (true) {
                Socket clientSocket1 = serverSocket.accept();
                System.out.println("Spieler 1 verbunden.");
                Socket clientSocket2 = serverSocket.accept();
                System.out.println("Spieler 2 verbunden.");

                // Starte ein neues Spiel für die beiden Clients
                new GameServer(clientSocket1, clientSocket2).run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Der Einstiegspunkt für den Server. Erstellt eine neue Instanz des NetworkServers.
     *
     * @param args Die Befehlszeilenargumente (nicht verwendet).
     */
    public static void main(String[] args) {
        new NetworkServer();
    }
}