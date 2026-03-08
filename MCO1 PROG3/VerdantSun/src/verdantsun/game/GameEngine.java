package verdantsun.game;

import verdantsun.models.Fertilizer;
import verdantsun.models.Field;
import verdantsun.models.Plant;
import verdantsun.models.Tile;
import verdantsun.utils.GameDataReader;
import java.util.HashMap;
import java.util.Scanner;

public class GameEngine {
    private Field field;
    private HashMap<String, Plant> plantTypes;
    private HashMap<String, Fertilizer> fertilizerTypes;
    private double savings;
    private int day;
    private int waterLevel;
    private boolean isRunning;
    private Scanner scanner;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.savings = 1000.0;
        this.day = 1;
        this.waterLevel = 10;
        this.isRunning = true;
    }

    public void start() {
        GameDataReader reader = new GameDataReader();
        this.plantTypes = reader.loadPlants();
        this.fertilizerTypes = reader.loadFertilizers();
        this.field = reader.loadMap();

        System.out.println("Welcome to Verdant Sun!");
        gameLoop();
    }

    private void gameLoop() {
        while (isRunning) {
            printStatus();
            printMenu();
            System.out.print(">> ");
            processCommand(scanner.nextLine());
        }
    }

    private void printStatus() {
        System.out.println("\n--- DAY " + day + " ---");
        System.out.println("Savings: " + savings);
        System.out.println("Water Level: " + waterLevel + "/10");
        field.displayField();
    }

    private void printMenu() {
        System.out.println("\nACTIONS:");
        System.out.println("[1] Plant a seed");
        System.out.println("[2] Water a plant");
        System.out.println("[3] Refill watering can (Cost: 100)");
        System.out.println("[4] Apply fertilizer");
        System.out.println("[5] Remove/Harvest a plant");
        System.out.println("[6] Next Day");
        System.out.println("[0] Quit Game");
    }

    private void processCommand(String choice) {
        switch (choice) {
            case "1": plantSeed(); break;
            case "2": waterPlant(); break;
            case "3": refillWateringCan(); break;
            case "4": applyFertilizer(); break;
            case "5": harvestPlant(); break;
            case "6": advanceDay(); break;
            case "0":
                System.out.println("Thanks for playing!");
                isRunning = false;
                break;
            default: System.out.println("Invalid command.");
        }
    }

    private void plantSeed() {
        System.out.println("\n--- Select a plant ---");
        for (String key : plantTypes.keySet()) {
            Plant p = plantTypes.get(key);
            if (savings >= p.getBuyPrice()) {
                System.out.println("- " + p.getName() + " (Cost: " + p.getBuyPrice() + ")");
            }
        }
        System.out.print("Enter plant name (or 'cancel'): ");
        String name = scanner.nextLine().toLowerCase();

        if (name.equals("cancel") || !plantTypes.containsKey(name)) return;

        Plant p = plantTypes.get(name);
        if (savings < p.getBuyPrice()) {
            System.out.println("Not enough money!");
            return;
        }

        Tile t = selectTile();
        if (t != null) {
            if (t.isPlantable()) {
                t.plantCrop(p.copy());
                savings -= p.getBuyPrice();
                System.out.println("Planted " + p.getName());
            } else {
                System.out.println("Tile occupied.");
            }
        }
    }

    private void waterPlant() {
        if (waterLevel <= 0) {
            System.out.println("Watering can empty!");
            return;
        }
        Tile t = selectTile();
        if (t != null && t.getPlantedCrop() != null) {
            if (!t.getPlantedCrop().isWatered()) {
                t.getPlantedCrop().setWatered(true);
                waterLevel--;
                System.out.println("Plant watered.");
            } else {
                System.out.println("Already watered.");
            }
        } else {
            System.out.println("No plant here.");
        }
    }

    private void refillWateringCan() {
        if (savings >= 100) {
            savings -= 100;
            waterLevel = 10;
            System.out.println("Can refilled.");
        } else {
            System.out.println("Not enough money.");
        }
    }

    private void applyFertilizer() {
        System.out.println("\n--- Select Fertilizer ---");
        for (String key : fertilizerTypes.keySet()) {
            Fertilizer f = fertilizerTypes.get(key);
            System.out.println("- " + f.getName() + " (Cost: " + f.getPrice() + ")");
        }
        System.out.print("Enter name (or 'cancel'): ");
        String name = scanner.nextLine().toLowerCase();

        if (name.equals("cancel") || !fertilizerTypes.containsKey(name)) return;

        Fertilizer selected = fertilizerTypes.get(name);
        if (savings < selected.getPrice()) {
            System.out.println("Not enough money!");
            return;
        }

        Tile t = selectTile();
        if (t != null) {
            if (t.hasFertilizer()) {
                System.out.println("Tile already has fertilizer!");
            } else {
                t.applyFertilizer(selected.copy());
                savings -= selected.getPrice();
                System.out.println("Applied " + selected.getName());
            }
        }
    }

    private void harvestPlant() {
        Tile t = selectTile();
        if (t != null && t.getPlantedCrop() != null) {
            Plant p = t.getPlantedCrop();
            if (p.isMature()) {
                int earned = p.getSellPrice() * p.getCropYield();
                savings += earned;
                System.out.println("Sold " + p.getName() + " for " + earned);
            } else {
                System.out.println("Removed " + p.getName() + " (Not ready).");
            }
            t.removeCrop();
        } else {
            System.out.println("Nothing here.");
        }
    }

    private void advanceDay() {
        System.out.println("Sleeping... Next Day!");
        day++;
        savings += 50;

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                Tile t = field.getTile(r, c);

                if (t.getPlantedCrop() != null && t.getPlantedCrop().isWatered()) {
                    Plant p = t.getPlantedCrop();
                    int growth = 1;

                    if (p.getPreferredSoil().equals(t.getSoilType())) {
                        growth++;
                    }

                    if (t.hasFertilizer()) {
                        growth++;
                        t.reduceFertilizerLife();
                        if (!t.hasFertilizer()) {
                            System.out.println("Fertilizer at (" + r + "," + c + ") ran out.");
                        }
                    }

                    p.increaseGrowth(growth);
                    p.setWatered(false);
                }
            }
        }
    }

    private Tile selectTile() {
        try {
            System.out.print("Row (0-9): ");
            int r = Integer.parseInt(scanner.nextLine());
            System.out.print("Col (0-9): ");
            int c = Integer.parseInt(scanner.nextLine());
            Tile t = field.getTile(r, c);
            if (t == null) System.out.println("Invalid coords.");
            return t;
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return null;
        }
    }
}