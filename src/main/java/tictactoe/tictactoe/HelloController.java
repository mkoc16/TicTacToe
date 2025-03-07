package tictactoe.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

/**
 * Der Controller für die JavaFX-GUI.
 */
public class HelloController {
    @FXML
    private GridPane gridPane;

    @FXML
    private Label statusLabel;

    private Socket connectionToServer;
    private String symbol;

    private DataInputStream serverDataInputStream;
    private DataOutputStream serverDataOutputStream;

    /**
     * Initialisiert die Verbindung zum Server und empfängt erste Spielinformationen.
     *
     * @param connectionToServer Die Socket-Verbindung zum Spielserver.
     * @throws IOException Falls ein Verbindungsfehler auftritt.
     */
    public void initConnectionToServer(Socket connectionToServer) throws IOException {
        this.connectionToServer = connectionToServer;

        this.serverDataInputStream = new DataInputStream(connectionToServer.getInputStream());
        this.serverDataOutputStream = new DataOutputStream(connectionToServer.getOutputStream());

        NetworkClientServiceReceiver networkClientServiceReceiver = new NetworkClientServiceReceiver(this.serverDataInputStream);
        networkClientServiceReceiver.start();
        networkClientServiceReceiver.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                return;
            }

            updateBoard(newValue[0]);
            symbol = (String)newValue[1];
            statusLabel.setText("Du bist Spieler" + newValue[1] + ". Spieler X beginnt");

            if (symbol.equals("O")) {
                var networkClientServiceReceiver1 = new NetworkClientServiceReceiver(this.serverDataInputStream);
                networkClientServiceReceiver1.start();
                networkClientServiceReceiver1.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    /*
                     * This is executed in the JavaFX Application Thread.
                     * We can manipulate the UI here.
                     */
                    if (newValue1 == null) {
                        return;
                    }

                    updateBoard(newValue1[0]);
                    var status = newValue1[1];
                    statusLabel.setText(status);
                });
            }
        });
    }

    /**
     * Aktualisiert das Spielfeld basierend auf dem aktuellen Spielzustand.
     *
     * @param board Das aktuelle Spielfeld als 2D-Array.
     */
    private void updateBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gridPane.getChildren().get(i * 3 + j);
                button.setText(String.valueOf(board[i][j]));
            }
        }
    }

    /**
     * Aktualisiert das Spielfeld basierend auf dem aktuellen Spielzustand.
     *
     * @param board Das aktuelle Spielfeld als Zeichenkette.
     */
    private void updateBoard(String board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gridPane.getChildren().get(i * 3 + j);
                button.setText(String.valueOf(board.charAt(i * 3 + j)));
            }
        }
    }

    /**
     * Behandelt die Klicks auf Spielfeld-Buttons und sendet die Spielzüge an den Server.
     *
     * @param event Das Button-Klick-Event.
     */
    @FXML
    private void handleButtonClick(javafx.event.ActionEvent event) {
        Button button = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(button);
        Integer col = GridPane.getColumnIndex(button);
        if (row == null || col == null) return;

        NetworkClientServiceSender networkClientServiceSender = new NetworkClientServiceSender(serverDataInputStream, serverDataOutputStream, row, col);
        networkClientServiceSender.start();
        networkClientServiceSender.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) {
                return;
            }

            var isLegalMove = (boolean)newValue[0];

            if (!isLegalMove) {
                statusLabel.setText("Illegaler Move! Erneut spielen!");
                return;
            }

            var boardAfterMove = (String)newValue[1];
            var statusAfterMove = (String)newValue[2];

            updateBoard(boardAfterMove);
            statusLabel.setText(statusAfterMove);

            System.out.println("statusAfterMove = " + statusAfterMove);

            if (!statusAfterMove.equals("weiter")) {
                System.out.println("drinnen statusAfterMove = " + statusAfterMove);
                disableButtons();
            }

            NetworkClientServiceReceiver networkClientServiceReceiver = new NetworkClientServiceReceiver(this.serverDataInputStream);
            networkClientServiceReceiver.start();
            networkClientServiceReceiver.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                /*
                 * This is executed in the JavaFX Application Thread.
                 * We can manipulate the UI here.
                 */
                if (newValue1 == null) {
                    return;
                }

                updateBoard(newValue1[0]);
                var status = (String)newValue1[1];
                statusLabel.setText(status);
            });
        });

    }

    /**
     * Behandelt den Neustart des Spiels, indem die Verbindung neu aufgebaut wird.
     */
    @FXML
    private void handleRestart() {
        try {
            connectionToServer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        enableButtons();
        statusLabel.setText("Spieler X ist am Zug.");

        connectionToServer = new Socket();
        try {
            connectionToServer.connect(new InetSocketAddress(HelloApplication.serverAddress, HelloApplication.serverPort));
            initConnectionToServer(connectionToServer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Deaktiviert alle Buttons auf dem Spielfeld.
     */
    private void disableButtons() {
        gridPane.getChildren().forEach(node -> node.setDisable(true));
    }

    /**
     * Aktiviert alle Buttons auf dem Spielfeld.
     */
    private void enableButtons() {
        gridPane.getChildren().forEach(node -> node.setDisable(false));
    }
}