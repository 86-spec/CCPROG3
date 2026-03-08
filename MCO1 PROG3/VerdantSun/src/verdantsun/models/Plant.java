package verdantsun.models;

public class Plant {
    private final String name;
    private final int buyPrice;
    private final int sellPrice;
    private final int cropYield;
    private final int maxGrowth;
    private int currentGrowth;
    private final String preferredSoil;
    private boolean isWatered;

    public Plant(String name, int buyPrice, int sellPrice, int cropYield, int maxGrowth, String preferredSoil) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.cropYield = cropYield;
        this.maxGrowth = maxGrowth;
        this.preferredSoil = preferredSoil;
        this.currentGrowth = 0;
        this.isWatered = false;
    }

    public String getName() { return name; }
    public int getBuyPrice() { return buyPrice; }
    public int getSellPrice() { return sellPrice; }
    public int getCropYield() { return cropYield; }
    public String getPreferredSoil() { return preferredSoil; }
    public boolean isWatered() { return isWatered; }

    public void setWatered(boolean watered) {
        isWatered = watered;
    }

    public void increaseGrowth(int amount) {
        this.currentGrowth += amount;
        if (this.currentGrowth > this.maxGrowth) {
            this.currentGrowth = this.maxGrowth;
        }
    }

    public boolean isMature() {
        return currentGrowth >= maxGrowth;
    }

    public Plant copy() {
        return new Plant(this.name, this.buyPrice, this.sellPrice, this.cropYield, this.maxGrowth, this.preferredSoil);
    }

    @Override
    public String toString() {
        return name + " (Growth: " + currentGrowth + "/" + maxGrowth + ")";
    }
}