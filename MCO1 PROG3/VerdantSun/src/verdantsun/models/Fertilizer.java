package verdantsun.models;

public class Fertilizer {
    private String name;
    private int price;
    private int effectDays;

    public Fertilizer(String name, int price, int effectDays) {
        this.name = name;
        this.price = price;
        this.effectDays = effectDays;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getEffectDays() { return effectDays; }

    // Reduces the days left by 1
    public void reduceDuration() {
        this.effectDays--;
    }

    // Creates a fresh copy of the fertilizer
    public Fertilizer copy() {
        return new Fertilizer(this.name, this.price, this.effectDays);
    }
}