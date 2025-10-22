import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 test suite for Assignment 3B (Discover the Bugs)
 * Each test corresponds to a specific logical bug in Cell.java or GameState.java.
 */
public class GameStateTest {

    private GameState state;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        // Create a minimal checkerboard: 8x8 of '-' (empty)
        File tempBoard = new File("board_empty.txt");
        try (FileWriter fw = new FileWriter(tempBoard)) {
            for (int i = 0; i < 8; i++) {
                fw.write("--------\n");
            }
        } catch (IOException e) {
            fail("Failed to set up temporary board file");
        }

        state = GameState.fromFile("board_empty.txt", true);
    }

    // -------------------------------
    // 1. Movement direction test
    // -------------------------------
    @Test
    void testBlackMovementDirection() {
        ArrayList<Integer> moves = Cell.BLACK.movement();
        boolean hasPositive = moves.stream().anyMatch(i -> i > 0);
        assertTrue(hasPositive,
            "Black pieces should have at least one positive movement offset (downwards)");
    }

    // -------------------------------
    // 2. Promotion boundary test
    // -------------------------------
    @Test
    void testPromotionOffByOne() {
        Cell promoted = Cell.RED.promoteIfReachedEnd(56);
        assertEquals(Cell.RED_KING, promoted,
            "RED at index 56 should promote to RED_KING (inclusive boundary)");
    }

    // -------------------------------
    // 3. Opponent detection with kings
    // -------------------------------
    @Test
    void testIsOpponentIncludesKings() {
        assertTrue(Cell.RED.isOpponent(Cell.BLACK_KING),
            "RED should recognize BLACK_KING as opponent");
        assertTrue(Cell.BLACK.isOpponent(Cell.RED_KING),
            "BLACK should recognize RED_KING as opponent");
    }

    // -------------------------------
    // 4. Out-of-bounds prevention test
    // -------------------------------
    @Test
    void testNextMovesNoOutOfBounds() throws FileNotFoundException {
        // Create a board with a single red piece in corner (A1)
        File boardFile = new File("board_corner.txt");
        try (FileWriter fw = new FileWriter(boardFile)) {
            fw.write("r-------\n");
            for (int i = 1; i < 8; i++) {
                fw.write("--------\n");
            }
        } catch (IOException e) {
            fail("Failed to create corner test board file");
        }

        GameState cornerState = GameState.fromFile("board_corner.txt", false);

        assertDoesNotThrow(() -> {
            cornerState.nextMovesFromCell("A1");
        }, "No IndexOutOfBoundsException should occur when generating moves for corner piece");
    }

    // -------------------------------
    // 5. Move generation should not mutate original state
    // -------------------------------
    @Test
    void testMoveGenerationDoesNotMutateOriginal() throws FileNotFoundException {
        // Create a simple board with one red piece at A1 and empty rest
        File boardFile = new File("board_singlepiece.txt");
        try (FileWriter fw = new FileWriter(boardFile)) {
            fw.write("r-------\n");
            for (int i = 1; i < 8; i++) {
                fw.write("--------\n");
            }
        } catch (IOException e) {
            fail("Failed to create single-piece test board file");
        }

        GameState testState = GameState.fromFile("board_singlepiece.txt", false);
        ArrayList<Cell> before = new ArrayList<>(testState.getCells());
        testState.nextMovesFromCell("A1");
        ArrayList<Cell> after = testState.getCells();

        assertEquals(before, after,
            "Calling nextMovesFromCell() should not mutate the original GameState cells");
    }

    // -------------------------------
    // 6. Invalid file character handling
    // -------------------------------
    @Test
    void testFromFileInvalidCharacter() throws IOException {
        File badBoard = new File("board_invalid.txt");
        try (FileWriter fw = new FileWriter(badBoard)) {
            for (int i = 0; i < 7; i++) {
                fw.write("--------\n");
            }
            // Write the eighth row with one invalid character 'X'
            fw.write("-------X\n"); 
        }
        
        assertThrows(IllegalArgumentException.class, () -> {
            GameState.fromFile("board_invalid.txt", true);
        }, "Invalid characters in board file should trigger IllegalArgumentException");
    }
}
