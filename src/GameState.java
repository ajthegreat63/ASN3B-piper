import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Represents a GameState
public class GameState
{
    // Number of rows and columns on the board
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private ArrayList<Cell> cells; // cells on the game board
    private boolean blackTurn;     // flag for current turn

    /**
     * Creates a new GameState
     */
    private GameState() {}

    /**
     * Creates a GameState
     * @param cells the 64 cells in the board
     * @param blackTurn true if it is black's turn in the state, false if red's
     */
    private GameState(ArrayList<Cell> cells,  boolean blackTurn) {
        this.cells = cells;
        this.blackTurn = blackTurn;
    }

    /**
     * Removes a piece from the game board
     * @param piece_index the index on the board of the piece to remove
     * @return a new GameState with the piece at the given index removed
     */
    private GameState removePiece(int piece_index) {
        GameState newState = new GameState();

        newState.cells = this.cells;
        newState.blackTurn = this.blackTurn;
        newState.cells.set(piece_index, Cell.EMPTY);

        return newState;
    }

    /**
     * Moves a piece and changes the turn to the other color
     * @param moved_cell_start_index the initial index of the piece to move
     * @param moved_cell_end_index the index to move the piece to
     * @return Updated GameState
     */
    private GameState movePieceFlipTurn(
        int moved_cell_start_index,
        int moved_cell_end_index
    ) {
        GameState newState = new GameState();

        newState.cells = new ArrayList<>(this.cells);

        Cell moved_cell = this.cells.get(moved_cell_start_index);
        newState.cells.set(moved_cell_start_index, Cell.EMPTY);
        newState.cells.set(moved_cell_end_index, moved_cell.promoteIfReachedEnd(moved_cell_end_index));

        return newState;
    }

    /**
     * Get the row index of a Cell
     * @param cell The cell represented as [A-H][1-8]
     * @return the row index (0-7)
     */
    private int rowIndex(String cell) {
        if (cell.length() < 2) {
            return -1;
        }

        return cell.charAt(1) - '1';
    }

    /**
     * Get the col index of a Cell
     * @param cell The cell represented as [A-H][1-8]
     * @return the col index (0-7)
     */
    private int colIndex(String cell) {
        if (cell.length() < 2) {
            return -1;
        }

        return cell.charAt(0) - 'A';
    }

    /**
     * Populate the array of next possible moves for a piece
     * @param start_index The initial index of the piece
     * @param curr_index The curr index (as we are searching possible moves)
     * @param nextMoves The array to populate the moves in
     */
    private void populateNextMoves(
            int start_index,
            int curr_index,
            ArrayList<GameState> nextMoves
    ) {
        Cell start_cell = cells.get(start_index);

        for (int move : start_cell.movement()) {
            int next_index = curr_index + move;

            if (cells.get(next_index).isEmpty()) {
                nextMoves.add(this.movePieceFlipTurn(start_index, next_index));

            } else if (start_cell.isOpponent(cells.get(next_index))) {
                next_index += move;

                nextMoves.add(this.movePieceFlipTurn(start_index, next_index));

                this.populateNextMoves(start_index, next_index, nextMoves);
            }
        }
    }

    /**
     * Gets all the possible moves of a piece located at a cell
     * @param cell The cell represented as [A-H][1-8]
     * @return The list of moves
     */
    public ArrayList<GameState> nextMovesFromCell(String cell) {
        int r = rowIndex(cell);
        int c = colIndex(cell);

        if (r < 0 || c < 0 || r >= ROWS || c >= COLS) {
            return new ArrayList<>();
        }

        int start_index = r * COLS + c;

        ArrayList<GameState> nextGameStates = new ArrayList<>();
        populateNextMoves(start_index, start_index, nextGameStates);

        return nextGameStates;
    }

    /**
     * Gets a string representation of the board
     * @return String of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int horizontalSize = COLS * 2 - 1;
        sb.append("   A B C D E F G H\n");
        sb.append("  ┌").append("─".repeat(horizontalSize)).append("┐\n");

        for (int i = 0; i < ROWS; i++) {
            sb.append(i + 1).append(" │");
            for (int j = 0; j < COLS; j++) {
                sb.append(cells.get(i * COLS + j).toString()).append("│");
            }
            sb.append("\n");
        }

        sb.append("  └").append("─".repeat(horizontalSize)).append("┘\n");
        return sb.toString();
    }

    /**
     * Gets a GameState represented in a file, also given whose turn it is
     * @param filepath Path of the file containg the board
     * @param blackTurn true if it is black's turn, false if red's turn
     * @return the GameState
     * @throws FileNotFoundException Board file not found
     * @throws NoSuchElementException Thrown if error parsing board
     * @throws IllegalArgumentException Thrown if error parsing board
     */
    public static GameState fromFile(
        String filepath,
        boolean blackTurn
    ) throws FileNotFoundException, NoSuchElementException, IllegalArgumentException {

        File file = new File(filepath);

        Scanner reader = new Scanner(file);

        ArrayList<Cell> cells = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {
            String line = reader.nextLine();

            for (int j = 0; j < COLS; j++) {
                char data = line.charAt(j);
                switch (data) {
                    case 'r':
                        cells.add(Cell.RED);
                        break;
                    case 'b':
                        cells.add(Cell.BLACK);
                        break;
                    case 'R':
                        cells.add(Cell.RED_KING);
                        break;
                    case 'B':
                        cells.add(Cell.BLACK_KING);
                        break;
                    case '-':
                        cells.add(Cell.EMPTY);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid file format");
                }
            }
        }

        return new GameState(cells, blackTurn);
    }

    /**
     * Gets the current turn
     * @return true if it is black's turn, false if red's turn
     */
    public boolean isBlackTurn() {
        return this.blackTurn;
    }

    /**
     * Gets the board of the GameState
     * @return List of cells in the board
     */
    public ArrayList<Cell> getCells() {
        return this.cells;
    }
}
