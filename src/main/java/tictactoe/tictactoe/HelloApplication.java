package tictactoe.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Die Hauptanwendungsklasse für das Tic-Tac-Toe-Spiel.
 * Diese Klasse startet die JavaFX-Oberfläche und stellt eine Verbindung zum Server her.
 */
public class HelloApplication extends Application {
    public static InetAddress serverAddress;
    public static int serverPort;

    /**
     * Startet die JavaFX-Anwendung und stellt eine Verbindung zum Server her.
     *
     * @param stage Das Hauptfenster der Anwendung.
     * @throws Exception Falls ein Fehler beim Laden der Benutzeroberfläche oder der Serververbindung auftritt.
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 350);
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();

        var serverConfiguration = Files.readAllLines(Path.of("C:\\tictactoe\\server.txt"));
        System.out.println("serverConfiguration = " + serverConfiguration);

        serverAddress = Inet4Address.getByName(serverConfiguration.get(0));
        serverPort = Integer.parseInt(serverConfiguration.get(1));

        var connectionToServer = new Socket();
        connectionToServer.connect(new InetSocketAddress(serverAddress, serverPort));

        HelloController controller = fxmlLoader.getController();
        controller.initConnectionToServer(connectionToServer);
    }

    /**
     * Der Einstiegspunkt der Anwendung.
     *
     * @param args Die Befehlszeilenargumente.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

