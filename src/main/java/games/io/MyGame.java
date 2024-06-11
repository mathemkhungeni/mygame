package games.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@SpringBootApplication
public class MyGame {

    public static void main(String[] args) {
        SpringApplication.run(MyGame.class, args);

        if (args.length != 2) {
            System.out.println("Usage: java -jar <your-jar-file> --config config.json --betting-amount 100");
            return;
        }

        String configFilePath = args[0].split("=")[1];
        double bettingAmount = Double.parseDouble(args[1].split("=")[1]);

        try {
            String result = null;

            DataInputStream reader = new DataInputStream(new FileInputStream(configFilePath));
            int nBytesToRead = reader.available();
            if(nBytesToRead > 0) {
                byte[] bytes = new byte[nBytesToRead];
                reader.read(bytes);
                result = new String(bytes);
            }


            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject jObject = new JSONObject(result);

            GameConfig config = new GameConfig();
            config.setColumns(jObject.getInt("columns"));
            config.setRows(jObject.getInt("rows"));
            JSONObject symbols = jObject.getJSONObject("symbols");

            Map<String, Symbol> syb = new HashMap<>();
            Iterator<?> keys = symbols.keys();
            while( keys.hasNext() ){
                String key = (String)keys.next();
                JSONObject symbol = symbols.getJSONObject(key);
                Symbol configSymbol = new Symbol(symbol.has("reward_multiplier")  ? Double.valueOf(symbol.get("reward_multiplier").toString()) : 0, symbol.get("type").toString());
                syb.put(key, configSymbol );
            }
            config.setSymbols(syb);

            Game game = new Game(config);
            String[][] matrix = game.generateMatrix();

            // Print generated matrix
            System.out.println("Generated Matrix:");
            for (int i = 0; i < config.getRows(); i++) {
                for (int j = 0; j < config.getColumns(); j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }

            double reward = game.calculateReward(matrix, bettingAmount);
            System.out.println("Reward: " + reward);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
