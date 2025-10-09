import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single square on the checkerboard
 */
public enum Cell
{

    /**
     * An empty square
     */
    EMPTY(" "),

    /**
     * Square with a red piece in it
     */
    RED("●"),

    /**
     * Square with a black piece in it
     */
    BLACK("○"),

    /**
     * Square with a red piece that is kinged
     */
    RED_KING("♛"),

    /**
     * Square with a black piece that is kinged
     */
    BLACK_KING("♕");

    /**
     * The symbol to use for the cell
     */
    private final String symbol;

    Cell(String symbol)
    {
        this.symbol = symbol;
    }

    /**
     * Adding this to the piece's position moves them diagonally down right
     */
    private static final int RIGHT_DOWN = 7;

    /**
     * Adding this to the piece's position moves them diagonally down left
     */
    private static final int LEFT_DOWN = 9;

    /**
     * Adding this to the piece's position moves them diagonally up right
     */
    private static final int RIGHT_UP = -7;

    /**
     * Adding this to the piece's position moves them diagonally up left
     */
    private static final int LEFT_UP = -9;

    /**
     * Red king row start
     */
    private static final int RED_KING_START = 56;

    /**
     * Red king row end
     */
    private static final int RED_KING_END = 64;

    /**
     * Black king row start
     */
    private static final int BLACK_KING_START = 0;

    /**
     * Black king row end
     */
    private static final int BLACK_KING_END = 8;

    /**
     * Get the way a piece moves from the
     * @return A list of movement options for the piece
     */
    public ArrayList<Integer> movement() {
        switch (this) {
            case RED:
                return new ArrayList<>(List.of(RIGHT_UP, LEFT_UP));
            case BLACK:
                return new ArrayList<>(List.of(RIGHT_UP, LEFT_UP));
            case RED_KING, BLACK_KING:
                return new ArrayList<>(List.of(RIGHT_DOWN, LEFT_DOWN, RIGHT_UP, LEFT_UP));
        }

        return new ArrayList<>();
    }

    /**
     * @return Is this red or red king
     */
    public boolean isRedPiece() {
        return this.equals(RED) || this.equals(RED_KING);
    }

    /**
     * @return Is this black or black king
     */
    public boolean isBlackPiece() {
        return this.equals(BLACK) || this.equals(BLACK_KING);
    }

    /**
     * @return Is this red or red king
     */
    public boolean isOpponent(Cell other) {
        switch (this) {
            case RED:
                return other.equals(Cell.BLACK);
            case BLACK:
                return other.equals(Cell.RED);
        }

        return false;
    }

    /**
     * Try to promote a piece to king
     * @param cell_index The cell index
     * @return The new cell, promoted if at the promotion row
     */
    public Cell promoteIfReachedEnd(int cell_index) {
        switch (this) {
            case RED:
                if (RED_KING_START < cell_index && cell_index < RED_KING_END)
                    return RED_KING;
                break;
            case BLACK:
                if (BLACK_KING_START < cell_index && cell_index < BLACK_KING_END)
                    return BLACK_KING;
        }

        return this;
    }

    /**
     * @return If the cell is empty
     */
    public boolean isEmpty() {
        return this.equals(Cell.EMPTY);
    }

    @Override
    public String toString()
    {
        return symbol;
    }
}
