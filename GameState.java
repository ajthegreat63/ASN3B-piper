import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameState
{

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private ArrayList<Cell> cells;
    private boolean blackTurn;

    private GameState() {}

    private GameState(ArrayList<Cell> cells,  boolean blackTurn) {
        this.cells = cells;
        this.blackTurn = blackTurn;
    }

    private GameState removePiece(int piece_index) {
        GameState newState = new GameState();

        newState.cells = this.cells;
        newState.blackTurn = this.blackTurn;
        newState.cells.set(piece_index, Cell.EMPTY);

        return newState;
    }

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

    private int rowIndex(String cell) {
        if (cell.length() < 2) {
            return -1;
        }

        return cell.charAt(1) - '1';
    }

    private int colIndex(String cell) {
        if (cell.length() < 2) {
            return -1;
        }

        return cell.charAt(0) - 'A';
    }

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

    public boolean isBlackTurn() {
        return this.blackTurn;
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }
}
