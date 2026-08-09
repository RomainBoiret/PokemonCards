package Player;

import MisteryBox.*;
import Pokemon.*;
import Shop.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Player {

    private String name;
    private int pokeDollars;
    private ArrayList<Pokemon> park;
    private ArrayList<Item> inventory;
    private int parkCapacity;
    private int inventoryCapacity;
    private int day;

    public static final int PARK_INCREMENT_COST = 1000;
    public static final int INVENTORY_INCREMENT_COST = 500;
    public static final int PARK_INCREMENT_SIZE = 50;
    public static final int INVENTORY_INCREMENT_SIZE = 50;

    public Player(String _name) {
        this.name = _name;
        this.pokeDollars = 1000;
        this.park = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.parkCapacity = 100;
        this.inventoryCapacity = 100;
        this.day = 1;
    }

    public boolean addPokemon(Pokemon pokemon) {
        if (park.size() < parkCapacity) {
            pokemon.setPlayer(this);
            return park.add(pokemon);
        }
        return false;
    }

    public boolean removePokemon(Pokemon pokemon) {
        boolean removed = park.remove(pokemon);
        if (removed) {
            pokemon.setPlayer(null);
        }
        return removed;
    }

    public int parkSize() {
        return park.size();
    }

    public ArrayList<Pokemon> getPark() {
        return this.park;
    }

    public boolean addItem(Item item) {
        if (inventory.size() < inventoryCapacity) {
            return inventory.add(item);
        }
        return false;
    }

    public boolean sellItem(Item item) {
        if (!inventory.remove(item)) {
            return false;
        }
        int refund = Math.max(1, item.getPrice() / 2);
        setPokeDollars(getPokeDollars() + refund);
        return true;
    }

    public int inventorySize() {
        return inventory.size();
    }

    public boolean updatePark(int numberOfSpaces) {
        int cost = numberOfSpaces * PARK_INCREMENT_COST;
        if (getPokeDollars() >= cost) {
            setPokeDollars(getPokeDollars() - cost);
            setParkCapacity(getParkCapacity() + PARK_INCREMENT_SIZE * numberOfSpaces);
            return true;
        }
        return false;
    }

    public boolean updateInventory(int numberOfSpaces) {
        int cost = numberOfSpaces * INVENTORY_INCREMENT_COST;
        if (getPokeDollars() >= cost) {
            setPokeDollars(getPokeDollars() - cost);
            setInventoryCapacity(getInventoryCapacity() + INVENTORY_INCREMENT_SIZE * numberOfSpaces);
            return true;
        }
        return false;
    }

    /** Revenus du parc selon l'état des Pokémon, puis passage au jour suivant. */
    public String collectParkIncomeAndPassDay() {
        int income = 0;
        for (Pokemon pokemon : park) {
            income += pokemon.computeDailyIncome();
            pokemon.passDay();
        }

        setPokeDollars(getPokeDollars() + income);
        day++;

        return i18n.I18n.t("player.day_done", day - 1, income, day);
    }

    public String buyMysteryBox(Rarity rarity) {
        MisteryBox box = new MisteryBox(rarity);

        if (getPokeDollars() < box.getPrice()) {
            return i18n.I18n.t("player.box_no_money", box.getPrice());
        }

        if (parkSize() >= parkCapacity) {
            return i18n.I18n.t("player.park_full");
        }

        setPokeDollars(getPokeDollars() - box.getPrice());
        Pokemon pokemon = box.open();
        addPokemon(pokemon);

        return i18n.I18n.t("player.box_opened", box.getRarity(), box.getPrice(), pokemon.getDisplayName());
    }

    public String getName() {
        return this.name;
    }

    public int getPokeDollars() {
        return this.pokeDollars;
    }

    public int getParkCapacity() {
        return this.parkCapacity;
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    public int getInventoryCapacity() {
        return this.inventoryCapacity;
    }

    public int getDay() {
        return this.day;
    }

    public ArrayList<Food> getFoodsFromInventory() {
        return inventory.stream()
            .filter(item -> item instanceof Food)
            .map(item -> (Food) item)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Heal> getHealsFromInventory() {
        return inventory.stream()
            .filter(item -> item instanceof Heal)
            .map(item -> (Heal) item)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<XpBoost> getXpBoostsFromInventory() {
        return inventory.stream()
            .filter(item -> item instanceof XpBoost)
            .map(item -> (XpBoost) item)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Stone> getStonesFromInventory() {
        return inventory.stream()
            .filter(item -> item instanceof Stone)
            .map(item -> (Stone) item)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public void setPokeDollars(int _pokeDollars) {
        this.pokeDollars = _pokeDollars;
    }

    public void setParkCapacity(int _parkCapacity) {
        this.parkCapacity = _parkCapacity;
    }

    public void setInventoryCapacity(int _inventoryCapacity) {
        this.inventoryCapacity = _inventoryCapacity;
    }

    public void setDay(int _day) {
        this.day = Math.max(1, _day);
    }
}
