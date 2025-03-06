module tictactoe.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens tictactoe.tictactoe to javafx.fxml;
    exports tictactoe.tictactoe;
}