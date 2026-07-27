package MisteryBox;

public enum Rarity {
    COMMON(200, 60),
    RARE(500, 25),
    EPIC(1200, 10),
    MYTHICAL(3000, 4),
    LEGENDARY(5000, 1);

    private final int price;
    private final int weight;

    Rarity(int price, int weight) {
        this.price = price;
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public int getWeight() {
        return weight;
    }

    public static Rarity roll() {
        int totalWeight = 0;
        for (Rarity rarity : values()) {
            totalWeight += rarity.weight;
        }

        int roll = (int) (Math.random() * totalWeight);
        int cumulative = 0;

        for (Rarity rarity : values()) {
            cumulative += rarity.weight;
            if (roll < cumulative) {
                return rarity;
            }
        }

        return COMMON;
    }
}
