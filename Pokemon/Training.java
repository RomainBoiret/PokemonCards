package Pokemon;

import i18n.I18n;

/**
 * Training types: each targets one stat with a clear stamina/hunger cost.
 * Stamina = remaining efforts for the day (refilled by passing a day).
 */
public enum Training {
    STRENGTH("train.strength", 40, 15, 8, 12, 500, 700),
    DEFENSE("train.defense", 30, 12, 5, 9, 350, 550),
    SPEED("train.speed", 20, 10, 3, 6, 250, 400);

    private final String keyPrefix;
    private final int staminaCost;
    private final int foodCost;
    private final int minStrain;
    private final int maxStrain;
    private final int minXp;
    private final int maxXp;

    Training(String keyPrefix, int staminaCost, int foodCost, int minStrain, int maxStrain, int minXp, int maxXp) {
        this.keyPrefix = keyPrefix;
        this.staminaCost = staminaCost;
        this.foodCost = foodCost;
        this.minStrain = minStrain;
        this.maxStrain = maxStrain;
        this.minXp = minXp;
        this.maxXp = maxXp;
    }

    public String getLabel() {
        return I18n.t(keyPrefix + ".label");
    }

    public String getDescription() {
        return I18n.t(keyPrefix + ".desc");
    }

    public int getStaminaCost() {
        return staminaCost;
    }

    public int getFoodCost() {
        return foodCost;
    }

    public int getMinStrain() {
        return minStrain;
    }

    public int getMaxStrain() {
        return maxStrain;
    }

    public int getMinXp() {
        return minXp;
    }

    public int getMaxXp() {
        return maxXp;
    }
}
