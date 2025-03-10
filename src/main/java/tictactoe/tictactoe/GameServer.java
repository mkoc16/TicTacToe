package tictactoe.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Diese Klasse implementiert einen Game-Server für Tic-Tac-Toe.
 * Sie verwaltet die Kommunikation zwischen zwei Spielern über Sockets
 * und steuert den Spielfluss.
 */

public class GameServer implements Runnable {
    private Socket playerX, playerO;

    /**
     * Konstruktor für den GameServer.
     *
     * @param playerX Der Socket für Spieler X.
     * @param playerO Der Socket für Spieler O.
     */
    public GameServer(Socket playerX, Socket playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    /**
     * Startet die Spiel-Logik und verwaltet den Spielfluss zwischen den beiden Spielern.
     * Die Methode wartet auf Spielzüge, überprüft deren Gültigkeit und sendet Updates an beide Spieler.
     */
    @Override
    public void run() {
        try {
            var playerX_Input = new DataInputStream(playerX.getInputStream());
            var playerX_Output = new DataOutputStream(playerX.getOutputStream());

            var playerO_Input = new DataInputStream(playerO.getInputStream());
            var playerO_Output = new DataOutputStream(playerO.getOutputStream());

            var game = new Gamelogic();

            // Senden des initialen Spielbretts an beide Spieler
            playerX_Output.writeUTF(game.getBoardString());
            playerO_Output.writeUTF(game.getBoardString());

            // Spielerrollen festlegen
            playerX_Output.writeUTF("X");
            playerO_Output.writeUTF("O");

            var currentPlayerSymbol = 'X';
            var currentPlayerInput = playerX_Input;
            var currentPlayerOutput = playerX_Output;

            while (true) {

                var legalMove = false;

                // Spielerzug entgegennehmen und verarbeiten
                do {
                    var currentPlayer_row = currentPlayerInput.readInt();
                    var currentPlayer_col = currentPlayerInput.readInt();

                    System.out.println("currentPlayerSymbol = " + currentPlayerSymbol);
                    System.out.println("currentPlayer_row = " + currentPlayer_row);
                    System.out.println("currentPlayer_col = " + currentPlayer_col);

                    legalMove = game.makeMove(currentPlayerSymbol, currentPlayer_row, currentPlayer_col);
                    currentPlayerOutput.writeBoolean(legalMove);
                } while (!legalMove);

                // Aktualisiertes Spielfeld an beide Spieler senden
                playerX_Output.writeUTF(game.getBoardString());
                playerO_Output.writeUTF(game.getBoardString());

                // Überprüfung auf Sieg oder Unentschieden
                if (game.checkWin()) {
                    if (currentPlayerSymbol == 'X') {
                        playerX_Output.writeUTF("Spieler X hat gewonnen");
                        playerO_Output.writeUTF("Spieler X hat gewonnen");
                    }
                    else {
                        playerX_Output.writeUTF("Spieler O hat gewonnen");
                        playerO_Output.writeUTF("Spieler O hat gewonnen");
                    }
                    break;
                } else if (game.checkDraw()) {
                    playerX_Output.writeUTF("Unentschieden");
                    playerO_Output.writeUTF("Unentschieden");
                    break;
                }

                // Nachricht senden, dass das Spiel weitergeht
                playerX_Output.writeUTF("weiter");
                playerO_Output.writeUTF("weiter");

                // Spielerwechsel
                if (currentPlayerSymbol == 'X') {
                    currentPlayerSymbol = 'O';
                    currentPlayerInput = playerO_Input;
                    currentPlayerOutput = playerO_Output;
                }
                else {
                    currentPlayerSymbol = 'X';
                    currentPlayerInput = playerX_Input;
                    currentPlayerOutput = playerX_Output;
                }
            }

        } catch (Exception e) {
            return;
        }
    }


}
