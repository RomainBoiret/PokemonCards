package Pokemon;

/**
 * Types d'entraînement : chacun cible une stat, avec un coût en stamina/faim clair.
 *
 * Stamina = efforts restants dans la journée (recharge en passant un jour).
 */
public enum Training {
    STRENGTH(
        "Force",
        "Augmente l'attaque. Intense : coûte beaucoup de stamina.",
        40,
        15,
        8,
        12,
        500,
        700
    ),
    DEFENSE(
        "Défense",
        "Augmente la défense. Effort modéré.",
        30,
        12,
        5,
        9,
        350,
        550
    ),
    SPEED(
        "Vitesse",
        "Augmente la vitesse. Léger : peu de stamina, un peu moins d'XP.",
        20,
        10,
        3,
        6,
        250,
        400
    );

    private final String label;
    private final String description;
    private final int staminaCost;
    private final int foodCost;
    private final int minStrain;
    private final int maxStrain;
    private final int minXp;
    private final int maxXp;

    Training(
        String label,
        String description,
        int staminaCost,
        int foodCost,
        int minStrain,
        int maxStrain,
        int minXp,
        int maxXp
    ) {
        this.label = label;
        this.description = description;
        this.staminaCost = staminaCost;
        this.foodCost = foodCost;
        this.minStrain = minStrain;
        this.maxStrain = maxStrain;
        this.minXp = minXp;
        this.maxXp = maxXp;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
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
