package Pokemon;

import java.util.ArrayList;
import Player.*;
import Shop.*;
import i18n.I18n;
import i18n.PokemonNames;

public class Pokemon {

    private int id;
    private String name;
    private Type primaryType;
    private Type secondaryType;
    private int pv;
    private int maxPv;
    private int attack;
    private int defense;
    private int speed;
    private String evolution;
    private int evolutionLevel;
    private int xp;
    private Status foodStatus;
    private Status staminaStatus;
    private int foodBar;
    private int staminaBar;
    private int lvl;
    private Player player;

    public static final int MAXLEVEL = 100;
    public static final int MINLEVEL = 1;
    public static final int MAXFOOD = 100;
    public static final int MINFOOD = 0;
    public static final int MAXSTAMINA = 100;
    public static final int MINSTAMINA = 0;

    public Pokemon(int _id, String _name, Type _primaryType, Type _secondaryType, int _pv, int _attack, int _defense, int _speed, String _evolution, int _evolutionLevel) {
        this.id = _id;
        this.name = _name;
        this.primaryType = _primaryType;
        this.secondaryType = _secondaryType;
        this.pv = _pv;
        this.maxPv = _pv;
        this.attack = _attack;
        this.defense = _defense;
        this.speed = _speed;
        this.evolution = _evolution;
        this.evolutionLevel = _evolutionLevel;
        this.xp = 0;
        this.foodBar = MAXFOOD;
        this.staminaBar = MAXSTAMINA;
        this.lvl = MINLEVEL;
        this.foodStatus = Status.FULL;
        this.staminaStatus = Status.INSHAPE;
    }

    public Pokemon copy() {
        return new Pokemon(
            this.id,
            this.name,
            this.primaryType,
            this.secondaryType,
            this.maxPv,
            this.attack,
            this.defense,
            this.speed,
            this.evolution,
            this.evolutionLevel
        );
    }

    public String feed(Food food) {
        if (player == null) {
            return I18n.t("poke.no_owner");
        }
        if (getFoodBar() == MAXFOOD) {
            return I18n.t("poke.not_hungry", getDisplayName());
        }

        ArrayList<Food> availableFoods = player.getFoodsFromInventory();

        for (Food f : availableFoods) {
            if (f == food || f.getName().equals(food.getName())) {
                player.getInventory().remove(f);
                int newFoodBar = getFoodBar() + f.getFoodPoint();
                setFoodBar(Math.min(newFoodBar, MAXFOOD));
                updateFoodStatus();
                return I18n.t("poke.ate", getDisplayName(), f.getName(), getFoodBar(), MAXFOOD);
            }
        }

        return I18n.t("poke.food_missing", food.getName());
    }

    public String heal(Heal heal) {
        if (player == null) {
            return I18n.t("poke.no_owner");
        }
        if (getPv() == getMaxPv()) {
            return I18n.t("poke.full_hp", getDisplayName());
        }

        ArrayList<Heal> availableHeals = player.getHealsFromInventory();

        for (Heal h : availableHeals) {
            if (h == heal || h.getName().equals(heal.getName())) {
                player.getInventory().remove(h);
                int newPv = getPv() + h.getHealPoint();
                setPv(Math.min(newPv, getMaxPv()));
                return I18n.t("poke.healed", getDisplayName(), h.getName(), getPv(), getMaxPv());
            }
        }

        return I18n.t("poke.heal_missing", heal.getName());
    }

    public String useXpBoost(XpBoost boost) {
        if (player == null) {
            return I18n.t("poke.no_owner");
        }

        ArrayList<XpBoost> boosts = player.getXpBoostsFromInventory();
        for (XpBoost b : boosts) {
            if (b == boost || b.getName().equals(boost.getName())) {
                player.getInventory().remove(b);
                String xpMsg = gainXp(b.getXpPoint());
                return I18n.t("poke.used_boost", getDisplayName(), b.getName(), xpMsg);
            }
        }

        return I18n.t("poke.boost_missing", boost.getName());
    }

    public String train(Training train) {
        if (getPv() <= 0) {
            return I18n.t("poke.ko_train", getDisplayName());
        }
        if (getFoodStatus() == Status.STARVING) {
            return I18n.t("poke.starving_train", getDisplayName());
        }
        if (getStaminaBar() < train.getStaminaCost()) {
            return I18n.t(
                "poke.no_stamina",
                getDisplayName(),
                getStaminaBar(),
                MAXSTAMINA,
                train.getStaminaCost(),
                train.getLabel()
            );
        }

        int statGained = getRandomNumber(2, 4);
        String statName;

        switch (train) {
            case STRENGTH:
                setAttack(getAttack() + statGained);
                statName = I18n.t("stat.attack");
                break;
            case DEFENSE:
                setDefense(getDefense() + statGained);
                statName = I18n.t("stat.defense");
                break;
            case SPEED:
                setSpeed(getSpeed() + statGained);
                statName = I18n.t("stat.speed");
                break;
            default:
                return I18n.t("poke.train_invalid");
        }

        decreaseStaminaAndFoodBars(train.getStaminaCost(), train.getFoodCost());
        updateStaminaStatus();
        updateFoodStatus();

        int strain = getRandomNumber(train.getMinStrain(), train.getMaxStrain());
        if (getFoodStatus() == Status.HUNGER) {
            strain = (int) Math.round(strain * 1.5);
        }
        int lost = applyDamage(strain);

        int xpGained = getRandomNumber(train.getMinXp(), train.getMaxXp());

        String msg = I18n.t(
            "poke.train_result",
            getDisplayName(),
            train.getLabel(),
            statGained,
            statName,
            train.getStaminaCost(),
            getStaminaBar(),
            MAXSTAMINA,
            lost,
            getPv(),
            getMaxPv()
        );

        if (getPv() <= 0) {
            msg += " " + I18n.t("poke.now_ko", getDisplayName());
        } else {
            String xpMsg = gainXp(xpGained);
            if (!xpMsg.isEmpty()) {
                msg += " " + xpMsg;
            }
        }

        return msg;
    }

    /**
     * Fin de journée :
     * - la stamina se recharge (repos) → c'est son seul vrai rôle
     * - la faim baisse
     * - mal nourri → perte de PV
     */
    public void passDay() {
        decreaseFoodBar(15);
        setStaminaBar(MAXSTAMINA);
        updateFoodStatus();
        updateStaminaStatus();

        if (getPv() > 0) {
            if (getFoodStatus() == Status.STARVING) {
                applyDamage(Math.max(5, getMaxPv() / 6));
            } else if (getFoodStatus() == Status.HUNGER) {
                applyDamage(Math.max(2, getMaxPv() / 12));
            }
        }
    }

    /** Retourne les PV réellement perdus. */
    private int applyDamage(int amount) {
        if (amount <= 0 || getPv() <= 0) {
            return 0;
        }
        int before = getPv();
        setPv(Math.max(0, getPv() - amount));
        return before - getPv();
    }

    public int computeDailyIncome() {
        if (getPv() <= 0) {
            return 0;
        }

        double foodFactor;
        switch (getFoodStatus()) {
            case FULL:
                foodFactor = 1.2;
                break;
            case NORMAL:
                foodFactor = 1.0;
                break;
            case HUNGER:
                foodFactor = 0.6;
                break;
            case STARVING:
                foodFactor = 0.3;
                break;
            default:
                foodFactor = 0.5;
                break;
        }

        double staminaFactor = getStaminaStatus() == Status.INSHAPE ? 1.0 : 0.7;
        double healthFactor = (double) getPv() / getMaxPv();
        return (int) Math.round((50 + getLevel() * 5) * foodFactor * staminaFactor * healthFactor);
    }

    private void decreaseStaminaBar(int value) {
        setStaminaBar(Math.max(MINSTAMINA, getStaminaBar() - value));
    }

    private void decreaseStaminaAndFoodBars(int staminaValue, int foodValue) {
        decreaseStaminaBar(staminaValue);
        decreaseFoodBar(foodValue);
    }

    private void decreaseFoodBar(int value) {
        setFoodBar(Math.max(MINFOOD, getFoodBar() - value));
    }

    private void updateStaminaStatus() {
        // Affichage / revenus : fatigué si plus assez pour le plus léger entraînement.
        if (getStaminaBar() < Training.SPEED.getStaminaCost()) {
            setStaminaStatus(Status.TIRED);
        } else {
            setStaminaStatus(Status.INSHAPE);
        }
    }

    private void updateFoodStatus() {
        int currentFood = getFoodBar();
        if (currentFood <= 24) {
            setFoodStatus(Status.STARVING);
        } else if (currentFood <= 49) {
            setFoodStatus(Status.HUNGER);
        } else if (currentFood <= 95) {
            setFoodStatus(Status.NORMAL);
        } else {
            setFoodStatus(Status.FULL);
        }
    }

    public String gainXp(int xpGained) {
        if (xpGained <= 0) {
            return "";
        }

        StringBuilder log = new StringBuilder();
        setXp(getXp() + xpGained);
        int xpNextLvl = getXpForNextLevel(getLevel());
        log.append(I18n.t("poke.xp", xpGained, getXp(), xpNextLvl)).append(" ");

        while (getLevel() < MAXLEVEL && getXp() >= xpNextLvl && xpNextLvl > 0) {
            setXp(getXp() - xpNextLvl);
            log.append(levelUp());
            xpNextLvl = getXpForNextLevel(getLevel());
        }
        return log.toString().trim();
    }

    private String levelUp() {
        if (getLevel() < MAXLEVEL) {
            setLevel(getLevel() + 1);
            String msg = I18n.t("poke.level_up", getDisplayName(), getLevel());
            if (EvolutionRules.canEvolveByLevel(this)) {
                String previousName = getDisplayName();
                if (applyEvolution(getEvolution())) {
                    msg += I18n.t("poke.evolved", previousName, getDisplayName());
                }
            }
            return msg;
        }
        return I18n.t("poke.max_level", getDisplayName());
    }

    private int getXpForNextLevel(int level) {
        if (level < MINLEVEL || level >= MAXLEVEL) {
            return 0;
        }
        return 100 + level * level * 50;
    }

    /** Évolution automatique au niveau requis. */
    public String evolve() {
        if (EvolutionRules.canEvolveByLevel(this)) {
            String previousName = getDisplayName();
            if (applyEvolution(getEvolution())) {
                return I18n.t("poke.evolved", previousName, getDisplayName());
            }
        }
        return I18n.t("poke.cannot_evolve", getDisplayName());
    }

    public String detailedStatus() {
        String types = primaryType.name();
        if (secondaryType != null) {
            types += " / " + secondaryType.name();
        }
        return I18n.t("detail.header", getDisplayName(), lvl)
            + "\n" + I18n.t("detail.type", types)
            + "\n" + I18n.t("detail.hp", pv, maxPv)
            + "\n" + I18n.t("detail.stats", attack, defense, speed)
            + "\n" + I18n.t("detail.xp_stam", xp, staminaBar, statusLabel(staminaStatus))
            + "\n" + I18n.t("detail.food", foodBar, statusLabel(foodStatus))
            + "\n" + I18n.t("detail.evo", evolutionHint());
    }

    private String statusLabel(Status status) {
        return I18n.t("status." + status.name());
    }

    public String evolveWithStone(Stone stone) {
        if (player == null) {
            return I18n.t("poke.no_owner");
        }
        if (stone == null) {
            return I18n.t("poke.no_stone");
        }

        String target = EvolutionRules.resolveEvolutionTarget(this, stone.getStoneType());
        if (target == null) {
            if (getEvolution() == null) {
                return I18n.t("poke.cannot_evolve_more", getDisplayName());
            }
            if (getEvolutionLevel() > 0) {
                return I18n.t("poke.stone_level_evo", getDisplayName(), getEvolutionLevel());
            }
            return I18n.t("poke.stone_no_effect", stone.getName(), getDisplayName());
        }

        ArrayList<Stone> stones = player.getStonesFromInventory();
        boolean owned = false;
        for (Stone s : stones) {
            if (s == stone || (s.getName().equals(stone.getName()) && s.getStoneType() == stone.getStoneType())) {
                player.getInventory().remove(s);
                owned = true;
                break;
            }
        }
        if (!owned) {
            return I18n.t("poke.stone_missing", stone.getName());
        }

        String previousName = getDisplayName();
        if (!applyEvolution(target)) {
            player.addItem(new Stone(stone.getName(), stone.getPrice(), stone.getDescription(), stone.getStoneType()));
            return I18n.t("poke.stone_fail", previousName, PokemonNames.display(target));
        }

        return I18n.t("poke.stone_evolved", previousName, getDisplayName(), stone.getName());
    }

    private boolean applyEvolution(String evolutionName) {
        Pokemon evolutionPokemon = PokemonFactory.obtenirPokemon(evolutionName);
        if (evolutionPokemon == null) {
            return false;
        }

        setId(evolutionPokemon.id);
        setName(evolutionPokemon.name);
        setPrimaryType(evolutionPokemon.primaryType);
        setSecondaryType(evolutionPokemon.secondaryType);
        setMaxPv(evolutionPokemon.maxPv);
        setAttack(evolutionPokemon.attack);
        setDefense(evolutionPokemon.defense);
        setSpeed(evolutionPokemon.speed);
        setEvolution(evolutionPokemon.evolution);
        setEvolutionLevel(evolutionPokemon.evolutionLevel);
        setPv(getMaxPv());
        return true;
    }

    public String evolutionHint() {
        if (getEvolution() == null) {
            return I18n.t("poke.final_form");
        }
        if (getEvolution().equalsIgnoreCase("Multiple")) {
            return I18n.t("poke.eevee_stones");
        }
        if (getEvolutionLevel() > 0) {
            return I18n.t("poke.evo_level", PokemonNames.display(getEvolution()), getEvolutionLevel());
        }
        return I18n.t("poke.evo_stone", PokemonNames.display(getEvolution()));
    }

    public int getBaseStatTotal() {
        return maxPv + attack + defense + speed;
    }

    public String shortStatus() {
        String types = primaryType.name();
        if (secondaryType != null) {
            types += "/" + secondaryType.name();
        }
        return getDisplayName() + " | Lv." + lvl + " | " + types
            + " | PV " + pv + "/" + maxPv
            + " | " + foodBar + " (" + statusLabel(foodStatus) + ")"
            + " | " + staminaBar + " (" + statusLabel(staminaStatus) + ")"
            + " | " + evolutionHint();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /** Nom affiché (français ou anglais selon la langue). */
    public String getDisplayName() {
        return PokemonNames.display(this.name);
    }

    public Type getPrimaryType() {
        return this.primaryType;
    }

    public Type getSecondaryType() {
        return this.secondaryType;
    }

    public int getXp() {
        return this.xp;
    }

    public int getPv() {
        return this.pv;
    }

    public int getMaxPv() {
        return this.maxPv;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Status getFoodStatus() {
        return this.foodStatus;
    }

    public Status getStaminaStatus() {
        return this.staminaStatus;
    }

    public int getStaminaBar() {
        return this.staminaBar;
    }

    public int getFoodBar() {
        return this.foodBar;
    }

    public int getLevel() {
        return this.lvl;
    }

    public String getEvolution() {
        return this.evolution;
    }

    public int getEvolutionLevel() {
        return this.evolutionLevel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player _player) {
        this.player = _player;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setXp(int _xp) {
        this.xp = _xp;
    }

    public void setPv(int _pv) {
        this.pv = _pv;
    }

    public void setMaxPv(int _maxPv) {
        this.maxPv = _maxPv;
    }

    public void setAttack(int _attack) {
        this.attack = _attack;
    }

    public void setDefense(int _defense) {
        this.defense = _defense;
    }

    public void setSpeed(int _speed) {
        this.speed = _speed;
    }

    public void setEvolution(String _evolution) {
        this.evolution = _evolution;
    }

    public void setEvolutionLevel(int _evolutionLevel) {
        this.evolutionLevel = _evolutionLevel;
    }

    public void setFoodStatus(Status _foodStatus) {
        this.foodStatus = _foodStatus;
    }

    public void setStaminaStatus(Status _staminaStatus) {
        this.staminaStatus = _staminaStatus;
    }

    public void setFoodBar(int _foodBar) {
        this.foodBar = _foodBar;
    }

    public void setStaminaBar(int _staminaBar) {
        this.staminaBar = _staminaBar;
    }

    public void setLevel(int _lvl) {
        this.lvl = _lvl;
    }

    public void setPrimaryType(Type _primaryType) {
        this.primaryType = _primaryType;
    }

    public void setSecondaryType(Type _secondaryType) {
        this.secondaryType = _secondaryType;
    }

    private int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
