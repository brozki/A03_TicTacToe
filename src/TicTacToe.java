import java.util.Scanner;

public class TicTacToe {
    // Board size constants and the board itself, shared by all methods
    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static String[][] board = new String[ROWS][COLS];

    public static void main(String[] args) {
        /*
         * PSEUDOCODE
         * Do (play-again loop):
         *   - Clear the board, set player to X, reset move count
         *   While the game is not over:
         *     - Show the board
         *     - Get row and col (1-3), convert to 0-2, repeat until the move is valid
         *     - Place the player's mark on the board
         *     - If a win is possible, check for a win; announce and end the game
         *     - If a tie is possible, check for a tie; announce and end the game
         *     - Otherwise toggle the player (X <-> O)
         *   - Ask whether to play again
         * While the players want to play again
         */
        Scanner in = new Scanner(System.in);
        boolean playAgain;

        do {
            clearBoard();
            String player = "X";      // X always moves first
            int moveCount = 0;
            boolean gameOver = false;

            while (!gameOver) {
                display();
                int row;
                int col;
                boolean valid;

                // Keep asking until the player picks an empty square
                do {
                    row = SafeInput.getRangedInt(in, "Player " + player + ", enter row", 1, 3) - 1;
                    col = SafeInput.getRangedInt(in, "Player " + player + ", enter col", 1, 3) - 1;
                    valid = isValidMove(row, col);
                    if (!valid) {
                        System.out.println("That square is already taken. Try again.");
                    }
                } while (!valid);

                board[row][col] = player;
                moveCount++;

                // A win needs at least 5 moves; a tie needs at least 7
                if (moveCount >= 5 && isWin(player)) {
                    display();
                    System.out.println("Player " + player + " wins!");
                    gameOver = true;
                } else if (moveCount >= 7 && isTie()) {
                    display();
                    System.out.println("It's a tie!");
                    gameOver = true;
                } else {
                    player = player.equals("X") ? "O" : "X";   // toggle player
                }
            }

            playAgain = SafeInput.getYNConfirm(in, "Play again?");
        } while (playAgain);

        System.out.println("Thanks for playing!");
    }

    /** Sets every square on the board to a blank space. */
    private static void clearBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = " ";
            }
        }
    }

    /** Prints the board with row/column separators. */
    private static void display() {
        System.out.println();
        System.out.println("     1   2   3");
        for (int r = 0; r < ROWS; r++) {
            System.out.print("  " + (r + 1) + "  ");
            for (int c = 0; c < COLS; c++) {
                System.out.print(board[r][c]);
                if (c < COLS - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (r < ROWS - 1) {
                System.out.println("    ---+---+---");
            }
        }
        System.out.println();
    }

    /** Returns true if the given square is empty (a legal move). */
    private static boolean isValidMove(int row, int col) {
        return board[row][col].equals(" ");
    }

    /** Returns true if the player has won by row, column, or diagonal. */
    private static boolean isWin(String player) {
        return isRowWin(player) || isColWin(player) || isDiagWin(player);
    }

    /** Returns true if the player fills an entire row. */
    private static boolean isRowWin(String player) {
        for (int r = 0; r < ROWS; r++) {
            if (board[r][0].equals(player) &&
                    board[r][1].equals(player) &&
                    board[r][2].equals(player)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if the player fills an entire column. */
    private static boolean isColWin(String player) {
        for (int c = 0; c < COLS; c++) {
            if (board[0][c].equals(player) &&
                    board[1][c].equals(player) &&
                    board[2][c].equals(player)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if the player fills either diagonal. */
    private static boolean isDiagWin(String player) {
        if (board[0][0].equals(player) &&
                board[1][1].equals(player) &&
                board[2][2].equals(player)) {
            return true;
        }
        return board[0][2].equals(player) &&
                board[1][1].equals(player) &&
                board[2][0].equals(player);
    }

    /**
     * Returns true if no one can win: every one of the 8 possible
     * winning lines already contains both an X and an O.
     */
    private static boolean isTie() {
        // 3 rows and 3 columns
        for (int i = 0; i < 3; i++) {
            if (!isBlocked(board[i][0], board[i][1], board[i][2])) return false;
            if (!isBlocked(board[0][i], board[1][i], board[2][i])) return false;
        }
        // 2 diagonals
        if (!isBlocked(board[0][0], board[1][1], board[2][2])) return false;
        if (!isBlocked(board[0][2], board[1][1], board[2][0])) return false;
        return true;
    }

    /** Helper: true if the three cells contain both an X and an O. */
    private static boolean isBlocked(String a, String b, String c) {
        boolean hasX = a.equals("X") || b.equals("X") || c.equals("X");
        boolean hasO = a.equals("O") || b.equals("O") || c.equals("O");
        return hasX && hasO;
    }
}