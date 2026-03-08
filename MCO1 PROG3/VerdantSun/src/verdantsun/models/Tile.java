package verdantsun.models;

public class Tile {
    private String soilType;
    private Plant plantedCrop;
    private Fertilizer fertilizer;

    public Tile(String soilType) {
        this.soilType = soilType;
        this.plantedCrop = null;
        this.fertilizer = null;
    }

    public String getSoilType() { return soilType; }
    public Plant getPlantedCrop() { return plantedCrop; }
    public Fertilizer getFertilizer() { return fertilizer; }

    // Check if we can plant here (must be empty)
    public boolean isPlantable() {
        return plantedCrop == null;
    }

    // Plant a crop
    public void plantCrop(Plant plant) {
        this.plantedCrop = plant;
    }

    // Remove or Harvest a crop (returns the plant object)
    public Plant removeCrop() {
        Plant removed = this.plantedCrop;
        this.plantedCrop = null;
        return removed;
    }

    // --- Fertilizer Methods ---

    public boolean hasFertilizer() {
        return fertilizer != null;
    }

    public void applyFertilizer(Fertilizer f) {
        this.fertilizer = f;
    }

    public void reduceFertilizerLife() {
        if (fertilizer != null) {
            fertilizer.reduceDuration();
            if (fertilizer.getEffectDays() <= 0) {
                fertilizer = null;
            }
        }
    }

    // Returns the plant char, or soil char if empty
    public String getDisplayChar() {
        if (plantedCrop != null) {
            return plantedCrop.getName().substring(0, 1).toUpperCase();
        }
        return ".";
    }
}