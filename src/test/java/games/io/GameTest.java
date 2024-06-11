package games.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTest {
    private GameConfig config;
    private Game game;

    @BeforeEach
    public void setUp() {
        config = new GameConfig();
        config.setRows(3);
        config.setColumns(3);
        Map<String, Symbol> symbols = new HashMap<>();

        Symbol symbolA = new Symbol();
        symbolA.setRewardMultiplier(50);
        symbolA.setType("standard");
        symbols.put("A", symbolA);

        Symbol symbolB = new Symbol();
        symbolB.setRewardMultiplier(25);
        symbolB.setType("standard");
        symbols.put("B", symbolB);

        Symbol symbolC = new Symbol();
        symbolC.setRewardMultiplier(10);
        symbolC.setType("standard");
        symbols.put("C", symbolC);

        Symbol symbol10x = new Symbol();
        symbol10x.setRewardMultiplier(0); // Bonus symbol, reward multiplier not needed
        symbol10x.setType("bonus");
        symbols.put("10x", symbol10x);

        config.setSymbols(symbols);

        game = new Game(config);
    }

    @Test
    public void testGenerateMatrix() {
        String[][] matrix = game.generateMatrix();
        assertEquals(3, matrix.length);
        assertEquals(3, matrix[0].length);
    }

    @Test
    public void testCheckWinningCombinations_RowWin() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "C", "B"},
                {"C", "B", "C"}
        };
        Map<String, List<String>> winningCombinations = game.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("A"));
        assertTrue(winningCombinations.get("A").contains("row"));
    }

    @Test
    public void testCheckWinningCombinations_ColumnWin() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"A", "C", "B"},
                {"A", "B", "C"}
        };
        Map<String, List<String>> winningCombinations = game.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("A"));
        assertTrue(winningCombinations.get("A").contains("column"));
    }

    @Test
    public void testCheckWinningCombinations_DiagonalWin() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"B", "A", "B"},
                {"C", "B", "A"}
        };
        Map<String, List<String>> winningCombinations = game.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("A"));
        assertTrue(winningCombinations.get("A").contains("diagonal1"));
    }

    @Test
    public void testCalculateReward_NoWin() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"B", "C", "B"},
                {"C", "B", "A"}
        };
        double reward = game.calculateReward(matrix, 100);
        assertEquals(1000, reward);
    }

    @Test
    public void testCalculateReward_WithWin() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "C", "B"},
                {"C", "B", "C"}
        };
        double reward = game.calculateReward(matrix, 100);
        assertEquals(5000, reward); // 100 (bet) * 50 (multiplier) * 3 (row win)
    }

    @Test
    public void testCalculateReward_WithBonus() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "10x", "B"},
                {"C", "B", "C"}
        };
        double reward = game.calculateReward(matrix, 100);
        assertEquals(50000, reward); // 100 (bet) * 50 (multiplier) * 3 (row win) * 10 (bonus)
    }
}
