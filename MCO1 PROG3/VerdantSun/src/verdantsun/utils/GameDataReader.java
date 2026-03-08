package verdantsun.utils;

import verdantsun.models.Fertilizer;
import verdantsun.models.Field;
import verdantsun.models.Plant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class GameDataReader {

    //Reads a whole file into a String
    public String readJsonFile(String filename) {
        StringBuilder data = new StringBuilder();
        try {
            File myObj = new File("resources/" + filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + filename + " not found.");
            e.printStackTrace();
        }
        return data.toString();
    }

    // Loads Plants.json
    public HashMap<String, Plant> loadPlants() {
        HashMap<String, Plant> plantsMap = new HashMap<>();
        String json = readJsonFile("Plants.json");

        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        String[] objectStrings = json.split("},");

        for (String rawObj : objectStrings) {
            if (!rawObj.endsWith("}")) rawObj = rawObj + "}";

            String[] keyAndValue = rawObj.split(":\\{");
            if (keyAndValue.length < 2) continue;

            String propertiesString = keyAndValue[1].replace("}", "").replace("\"", "");
            String[] props = propertiesString.split(",");

            String name = "";
            int price = 0;
            int yield = 0;
            int maxGrowth = 0;
            String soil = "";
            int cropPrice = 0;

            for (String prop : props) {
                String[] pair = prop.split(":");
                if (pair.length < 2) continue;

                String key = pair[0].trim();
                String val = pair[1].trim();

                switch (key) {
                    case "name": name = val; break;
                    case "price": price = Integer.parseInt(val); break;
                    case "yield": yield = Integer.parseInt(val); break;
                    case "max_growth": maxGrowth = Integer.parseInt(val); break;
                    case "preferred_soil": soil = val; break;
                    case "crop_price": cropPrice = Integer.parseInt(val); break;
                }
            }

            if (!name.isEmpty()) {
                Plant p = new Plant(name, price, cropPrice, yield, maxGrowth, soil);
                plantsMap.put(name.toLowerCase(), p);
            }
        }
        return plantsMap;
    }

    // Loads Fertilizers.json
    public HashMap<String, Fertilizer> loadFertilizers() {
        HashMap<String, Fertilizer> fertilizerMap = new HashMap<>();
        String json = readJsonFile("Fertilizers.json");

        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        String[] objectStrings = json.split("},");

        for (String rawObj : objectStrings) {
            if (!rawObj.endsWith("}")) rawObj = rawObj + "}";

            String[] keyAndValue = rawObj.split(":\\{");
            if (keyAndValue.length < 2) continue;

            String keyId = keyAndValue[0].replace("\"", "").trim();
            String propertiesString = keyAndValue[1].replace("}", "").replace("\"", "");
            String[] props = propertiesString.split(",");

            String name = "";
            int price = 0;
            int days = 0;

            for (String prop : props) {
                String[] pair = prop.split(":");
                if (pair.length < 2) continue;

                String key = pair[0].trim();
                String val = pair[1].trim();

                switch (key) {
                    case "name": name = val; break;
                    case "price": price = Integer.parseInt(val); break;
                    case "days": days = Integer.parseInt(val); break;
                }
            }

            if (!name.isEmpty()) {
                Fertilizer f = new Fertilizer(name, price, days);
                fertilizerMap.put(keyId.toLowerCase(), f);
            }
        }
        return fertilizerMap;
    }

    // Loads Map.json
    public Field loadMap() {
        Field field = new Field();
        String json = readJsonFile("Map.json");

        int mapStartIndex = json.indexOf("\"map\":");
        if (mapStartIndex == -1) return field;

        int arrayStart = json.indexOf("[", mapStartIndex);
        int arrayEnd = json.lastIndexOf("]");

        if (arrayStart == -1 || arrayEnd == -1) return field;

        String mapData = json.substring(arrayStart + 1, arrayEnd);
        String[] rows = mapData.split("\\],\\s*\\[");

        int r = 0;
        for (String rowString : rows) {
            if (r >= 10) break;
            String cleanRow = rowString.replace("[", "").replace("]", "").replace("\"", "");
            String[] cells = cleanRow.split(",");

            int c = 0;
            for (String cell : cells) {
                if (c >= 10) break;
                cell = cell.trim();
                String soilType = "dirt";
                if (cell.equals("l")) soilType = "loam";
                else if (cell.equals("s")) soilType = "sand";
                else if (cell.equals("g")) soilType = "gravel";

                field.setTile(r, c, soilType);
                c++;
            }
            r++;
        }
        return field;
    }
}