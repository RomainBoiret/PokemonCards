import java.util.ArrayList;
import java.util.Scanner;

import MisteryBox.Rarity;
import Player.Player;
import Pokemon.Pokemon;
import Pokemon.PokemonFactory;
import Pokemon.Training;
import Shop.Food;
import Shop.Heal;
import Shop.Item;
import Shop.Shop;
import Shop.Stone;
import Shop.XpBoost;

/**
 * PokePark — gestionnaire de parc Pokémon (console).
 */
public class PokePark {

    private static final String POKEMON_JSON = "Pokemon/PokemonList.json";

    private final Scanner scanner = new Scanner(System.in);
    private final Shop shop = new Shop();
    private Player player;

    public static void main(String[] args) {
        new PokePark().run();
    }

    private void run() {
        System.out.println("=================================");
        System.out.println("          P O K E P A R K        ");
        System.out.println("   Gère ton parc, fais évoluer   ");
        System.out.println("      ta collection Pokémon      ");
        System.out.println("=================================");

        PokemonFactory.chargerPokemons(POKEMON_JSON);
        if (!PokemonFactory.estCharge()) {
            System.out.println("Impossible de charger " + POKEMON_JSON);
            return;
        }

        System.out.print("\nNom du dresseur-gestionnaire : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Manager";
        }
        player = new Player(name);

        System.out.println("\nBienvenue " + player.getName() + " ! Tu démarres avec "
            + player.getPokeDollars() + " ₽.");
        System.out.println("Ouvre des Mystery Boxes (formes de base), fais-les évoluer, et gagne des revenus chaque jour.\n");

        boolean running = true;
        while (running) {
            printHeader();
            printMainMenu();
            int choice = readInt("Choix : ");
            System.out.println();

            switch (choice) {
                case 1:
                    showPark();
                    break;
                case 2:
                    careMenu();
                    break;
                case 3:
                    trainMenu();
                    break;
                case 4:
                    showInventory();
                    break;
                case 5:
                    shopMenu();
                    break;
                case 6:
                    mysteryBoxMenu();
                    break;
                case 7:
                    System.out.println(player.collectParkIncomeAndPassDay());
                    break;
                case 8:
                    upgradeMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("À bientôt dans PokePark, " + player.getName() + " !");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
            System.out.println();
        }
    }

    private void printHeader() {
        System.out.println("---------------------------------");
        System.out.println(player.getName()
            + " | Jour " + player.getDay()
            + " | " + player.getPokeDollars() + " ₽"
            + " | Parc " + player.parkSize() + "/" + player.getParkCapacity()
            + " | Inv. " + player.inventorySize() + "/" + player.getInventoryCapacity());
        System.out.println("---------------------------------");
    }

    private void printMainMenu() {
        System.out.println("1. Voir le parc");
        System.out.println("2. Soigner / Nourrir / XP Boost");
        System.out.println("3. Entraîner un Pokémon");
        System.out.println("4. Inventaire");
        System.out.println("5. Boutique");
        System.out.println("6. Mystery Box");
        System.out.println("7. Passer un jour (revenus + repos)");
        System.out.println("8. Agrandir parc / inventaire");
        System.out.println("0. Quitter");
    }

    private void showPark() {
        if (player.parkSize() == 0) {
            System.out.println("Le parc est vide. Ouvre une Mystery Box !");
            return;
        }

        ArrayList<Pokemon> park = player.getPark();
        for (int i = 0; i < park.size(); i++) {
            System.out.println((i + 1) + ". " + park.get(i).shortStatus());
        }
    }

    private void careMenu() {
        Pokemon pokemon = pickPokemon();
        if (pokemon == null) {
            return;
        }

        System.out.println("1. Nourrir");
        System.out.println("2. Soigner");
        System.out.println("3. Utiliser un XP Boost");
        System.out.println("4. Faire évoluer (pierre / cable)");
        int action = readInt("Action : ");

        switch (action) {
            case 1:
                Food food = pickFood();
                if (food != null) {
                    System.out.println(pokemon.feed(food));
                }
                break;
            case 2:
                Heal heal = pickHeal();
                if (heal != null) {
                    System.out.println(pokemon.heal(heal));
                }
                break;
            case 3:
                XpBoost boost = pickXpBoost();
                if (boost != null) {
                    System.out.println(pokemon.useXpBoost(boost));
                }
                break;
            case 4:
                Stone stone = pickStone();
                if (stone != null) {
                    System.out.println(pokemon.evolveWithStone(stone));
                }
                break;
            default:
                System.out.println("Action invalide.");
        }
    }

    private void trainMenu() {
        Pokemon pokemon = pickPokemon();
        if (pokemon == null) {
            return;
        }

        System.out.println("Stamina actuelle : " + pokemon.getStaminaBar() + "/100 (se recharge en passant un jour)");
        System.out.println();
        Training[] options = Training.values();
        for (int i = 0; i < options.length; i++) {
            Training t = options[i];
            System.out.println((i + 1) + ". " + t.getLabel()
                + " — " + t.getDescription()
                + " [coût " + t.getStaminaCost() + " stamina]");
        }
        int action = readInt("Entraînement : ");
        if (action < 1 || action > options.length) {
            System.out.println("Entraînement invalide.");
            return;
        }

        System.out.println(pokemon.train(options[action - 1]));
    }

    private void showInventory() {
        ArrayList<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Inventaire vide.");
            return;
        }

        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " — " + item.getDescription()
                + " (valeur " + item.getPrice() + " ₽)");
        }
    }

    private void shopMenu() {
        java.util.List<Item> list = shop.getShopList();
        for (int i = 0; i < list.size(); i++) {
            Item item = list.get(i);
            System.out.println((i + 1) + ". " + item.getName()
                + " — " + item.getPrice() + " ₽ — " + item.getDescription());
        }
        System.out.println("0. Retour");

        int index = readInt("Acheter n° : ") - 1;
        if (index == -1) {
            return;
        }
        System.out.println(shop.buy(player, index));
    }

    private void mysteryBoxMenu() {
        Rarity[] rarities = Rarity.values();
        for (int i = 0; i < rarities.length; i++) {
            Rarity r = rarities[i];
            System.out.println((i + 1) + ". Box " + r + " — " + r.getPrice() + " ₽");
        }
        System.out.println("0. Retour");

        int choice = readInt("Ouvrir : ");
        if (choice <= 0 || choice > rarities.length) {
            return;
        }
        System.out.println(player.buyMysteryBox(rarities[choice - 1]));
    }

    private void upgradeMenu() {
        System.out.println("1. Agrandir le parc (+" + Player.PARK_INCREMENT_SIZE
            + " places, " + Player.PARK_INCREMENT_COST + " ₽)");
        System.out.println("2. Agrandir l'inventaire (+" + Player.INVENTORY_INCREMENT_SIZE
            + " places, " + Player.INVENTORY_INCREMENT_COST + " ₽)");
        int choice = readInt("Choix : ");

        if (choice == 1) {
            if (player.updatePark(1)) {
                System.out.println("Parc agrandi ! Capacité : " + player.getParkCapacity());
            } else {
                System.out.println("Pas assez de Pokédollars.");
            }
        } else if (choice == 2) {
            if (player.updateInventory(1)) {
                System.out.println("Inventaire agrandi ! Capacité : " + player.getInventoryCapacity());
            } else {
                System.out.println("Pas assez de Pokédollars.");
            }
        } else {
            System.out.println("Choix invalide.");
        }
    }

    private Pokemon pickPokemon() {
        if (player.parkSize() == 0) {
            System.out.println("Aucun Pokémon dans le parc.");
            return null;
        }

        showPark();
        int index = readInt("N° du Pokémon : ") - 1;
        if (index < 0 || index >= player.parkSize()) {
            System.out.println("Pokémon invalide.");
            return null;
        }
        return player.getPark().get(index);
    }

    private Food pickFood() {
        ArrayList<Food> foods = player.getFoodsFromInventory();
        if (foods.isEmpty()) {
            System.out.println("Pas de nourriture dans l'inventaire.");
            return null;
        }
        for (int i = 0; i < foods.size(); i++) {
            System.out.println((i + 1) + ". " + foods.get(i).getName()
                + " (+" + foods.get(i).getFoodPoint() + " faim)");
        }
        int index = readInt("Nourriture : ") - 1;
        if (index < 0 || index >= foods.size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        return foods.get(index);
    }

    private Heal pickHeal() {
        ArrayList<Heal> heals = player.getHealsFromInventory();
        if (heals.isEmpty()) {
            System.out.println("Pas de soin dans l'inventaire.");
            return null;
        }
        for (int i = 0; i < heals.size(); i++) {
            System.out.println((i + 1) + ". " + heals.get(i).getName()
                + " (+" + heals.get(i).getHealPoint() + " PV)");
        }
        int index = readInt("Soin : ") - 1;
        if (index < 0 || index >= heals.size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        return heals.get(index);
    }

    private XpBoost pickXpBoost() {
        ArrayList<XpBoost> boosts = player.getXpBoostsFromInventory();
        if (boosts.isEmpty()) {
            System.out.println("Pas de boost XP dans l'inventaire.");
            return null;
        }
        for (int i = 0; i < boosts.size(); i++) {
            System.out.println((i + 1) + ". " + boosts.get(i).getName()
                + " (+" + boosts.get(i).getXpPoint() + " XP)");
        }
        int index = readInt("Boost : ") - 1;
        if (index < 0 || index >= boosts.size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        return boosts.get(index);
    }

    private Stone pickStone() {
        ArrayList<Stone> stones = player.getStonesFromInventory();
        if (stones.isEmpty()) {
            System.out.println("Pas de pierre d'évolution dans l'inventaire.");
            return null;
        }
        for (int i = 0; i < stones.size(); i++) {
            System.out.println((i + 1) + ". " + stones.get(i).getName()
                + " (" + stones.get(i).getStoneType() + ")");
        }
        int index = readInt("Pierre : ") - 1;
        if (index < 0 || index >= stones.size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        return stones.get(index);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Entre un nombre.");
            }
        }
    }
}
