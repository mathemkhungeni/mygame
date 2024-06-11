package games.io;// GameConfig.java
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GameConfig {
    @JsonProperty("columns")
    private int columns;
    @JsonProperty("rows")
    private int rows;
    @JsonProperty("symbols")
    private Map<String, Symbol> symbols;

    // Getters and setters
    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Symbol> symbols) {
        this.symbols = symbols;
    }
}
