package games.io;

import java.util.*;
public class Game {
    private GameConfig config;
    private Random random;

    public Game(GameConfig config) {
        this.config = config;
        this.random = new Random();
    }

    public String[][] generateMatrix() {
        String[][] matrix = new String[config.getRows()][config.getColumns()];

        // Fill matrix with symbols based on probabilities
        // (for simplicity, assuming equal probability for all symbols)
        Object[] symbols = config.getSymbols().keySet().toArray();

        for (int i = 0; i < config.getRows(); i++) {
            for (int j = 0; j < config.getColumns(); j++) {
                matrix[i][j] = (String) symbols[random.nextInt(symbols.length)];
            }
        }

        return matrix;
    }

    public Map<String, List<String>> checkWinningCombinations(String[][] matrix) {
        Map<String, List<String>> winningCombinations = new HashMap<>();

        // Check rows for winning combinations
        for (int i = 0; i < config.getRows(); i++) {
            Map<String, Integer> rowCount = new HashMap<>();
            for (int j = 0; j < config.getColumns(); j++) {
                rowCount.put(matrix[i][j], rowCount.getOrDefault(matrix[i][j], 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : rowCount.entrySet()) {
                if (entry.getValue() == config.getColumns()) {
                    winningCombinations.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add("row");
                }
            }
        }

        // Check columns for winning combinations
        for (int j = 0; j < config.getColumns(); j++) {
            Map<String, Integer> colCount = new HashMap<>();
            for (int i = 0; i < config.getRows(); i++) {
                colCount.put(matrix[i][j], colCount.getOrDefault(matrix[i][j], 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : colCount.entrySet()) {
                if (entry.getValue() == config.getRows()) {
                    winningCombinations.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add("column");
                }
            }
        }

        // Check diagonals for winning combinations
        if (config.getRows() == config.getColumns()) {
            Map<String, Integer> diag1Count = new HashMap<>();
            Map<String, Integer> diag2Count = new HashMap<>();
            for (int i = 0; i < config.getRows(); i++) {
                diag1Count.put(matrix[i][i], diag1Count.getOrDefault(matrix[i][i], 0) + 1);
                diag2Count.put(matrix[i][config.getColumns() - 1 - i], diag2Count.getOrDefault(matrix[i][config.getColumns() - 1 - i], 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : diag1Count.entrySet()) {
                if (entry.getValue() == config.getRows()) {
                    winningCombinations.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add("diagonal1");
                }
            }
            for (Map.Entry<String, Integer> entry : diag2Count.entrySet()) {
                if (entry.getValue() == config.getRows()) {
                    winningCombinations.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add("diagonal2");
                }
            }
        }

        return winningCombinations;
    }

    public double calculateReward(String[][] matrix, double betAmount) {
        Map<String, List<String>> winningCombinations = checkWinningCombinations(matrix);
        double reward = 0;

        if (!winningCombinations.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
                Symbol symbol = config.getSymbols().get(entry.getKey());
                reward += betAmount * symbol.getRewardMultiplier() * entry.getValue().size();
            }

            // Apply bonus symbols if there are winning combinations
            for (int i = 0; i < config.getRows(); i++) {
                for (int j = 0; j < config.getColumns(); j++) {
                    String cell = matrix[i][j];
                    switch (cell) {
                        case "10x":
                            reward *= 10;
                            break;
                        case "5x":
                            reward *= 5;
                            break;
                        case "+1000":
                            reward += 1000;
                            break;
                        case "+500":
                            reward += 500;
                            break;
                        case "MISS":
                            // No action needed
                            break;
                    }
                }
            }
        }

        return reward;
    }
}
