package tictactoe.tictactoe;

/**
 * Diese Klasse implementiert die Spielregeln und Logik für Tic-Tac-Toe.
 */
public class Gamelogic {
    private char[][] board;

    /**
     * Konstruktor: Initialisiert das Spielfeld.
     */
    public Gamelogic() {
        board = new char[3][3];
        initializeBoard();
    }

    /**
     * Initialisiert das Spielfeld mit Leerzeichen.
     */
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    /**
     * Überprüft, ob ein Spieler gewonnen hat.
     *
     * @return true, wenn ein Spieler gewonnen hat, sonst false.
     */
    public boolean checkWin() {
        return (checkRows() || checkColumns() || checkDiagonals());
    }

    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        return (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }

    /**
     * Überprüft, ob das Spiel unentschieden ist.
     *
     * @return true, wenn das Spiel unentschieden ist, sonst false.
     */
    public boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Setzt ein Symbol auf das Spielfeld, wenn das Feld frei ist.
     *
     * @param symbol Das Symbol des Spielers ('X' oder 'O').
     * @param row    Die Zeile des Spielfelds (0-2).
     * @param col    Die Spalte des Spielfelds (0-2).
     * @return true, wenn der Zug gültig war, sonst false.
     */
    public boolean makeMove(Character symbol, int row, int col) {
        System.out.println("board[row][col] = " + board[row][col]);
        System.out.println("(board[row][col] == ' ') = " + (board[row][col] == ' '));
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ') {
            board[row][col] = symbol;
            return true;
        }
        return false;
    }

    public char[][] getBoard() {
        return board;
    }

    public String getBoardString() {
        return "" +
                board[0][0] + board[0][1] + board[0][2] +
                board[1][0] + board[1][1] + board[1][2] +
                board[2][0] + board[2][1] + board[2][2];
    }

    /**
     * Setzt das Spielfeld zurück.
     */
    public void resetBoard() {
        initializeBoard();
    }
}