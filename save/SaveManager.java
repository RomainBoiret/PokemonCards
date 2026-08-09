package save;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Player.Player;
import Pokemon.Pokemon;
import Pokemon.Status;
import Pokemon.Type;
import Shop.Food;
import Shop.Heal;
import Shop.Item;
import Shop.Stone;
import Shop.StoneType;
import Shop.XpBoost;

/**
 * Single-slot, versioned JSON persistence stored in the user's home folder.
 */
public final class SaveManager {

    private static final int SAVE_VERSION = 1;
    private static final Path SAVE_DIRECTORY = Path.of(
        System.getProperty("user.home"),
        ".pokepark"
    );
    private static final Path SAVE_FILE = SAVE_DIRECTORY.resolve("save.json");

    private SaveManager() {
    }

    public static Path getSaveFile() {
        return SAVE_FILE;
    }

    public static boolean exists() {
        return Files.isRegularFile(SAVE_FILE);
    }

    public static void save(Player player) throws IOException {
        Files.createDirectories(SAVE_DIRECTORY);
        Path temporaryFile = SAVE_DIRECTORY.resolve("save.json.tmp");

        JSONObject root = new JSONObject();
        root.put("version", SAVE_VERSION);
        root.put("player", serializePlayer(player));

        try (Writer writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
            root.writeJSONString(writer);
        }

        try {
            Files.move(
                temporaryFile,
                SAVE_FILE,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            );
        } catch (IOException atomicMoveUnsupported) {
            Files.move(temporaryFile, SAVE_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static Player load() throws Exception {
        if (!exists()) {
            throw new IOException("Save file does not exist: " + SAVE_FILE);
        }

        JSONObject root;
        try (Reader reader = Files.newBufferedReader(SAVE_FILE, StandardCharsets.UTF_8)) {
            root = (JSONObject) new JSONParser().parse(reader);
        }

        int version = intValue(root.get("version"), 0);
        if (version != SAVE_VERSION) {
            throw new IOException("Unsupported save version: " + version);
        }

        JSONObject data = objectValue(root.get("player"), "player");
        Player player = new Player(stringValue(data.get("name"), "Manager"));
        player.setPokeDollars(intValue(data.get("pokeDollars"), 1000));
        player.setParkCapacity(intValue(data.get("parkCapacity"), 100));
        player.setInventoryCapacity(intValue(data.get("inventoryCapacity"), 100));
        player.setDay(intValue(data.get("day"), 1));

        JSONArray park = arrayValue(data.get("park"));
        for (Object value : park) {
            player.addPokemon(deserializePokemon(objectValue(value, "pokemon")));
        }

        JSONArray inventory = arrayValue(data.get("inventory"));
        for (Object value : inventory) {
            Item item = deserializeItem(objectValue(value, "item"));
            if (item != null) {
                player.addItem(item);
            }
        }
        return player;
    }

    public static void delete() throws IOException {
        Files.deleteIfExists(SAVE_FILE);
    }

    private static JSONObject serializePlayer(Player player) {
        JSONObject data = new JSONObject();
        data.put("name", player.getName());
        data.put("pokeDollars", player.getPokeDollars());
        data.put("parkCapacity", player.getParkCapacity());
        data.put("inventoryCapacity", player.getInventoryCapacity());
        data.put("day", player.getDay());

        JSONArray park = new JSONArray();
        for (Pokemon pokemon : player.getPark()) {
            park.add(serializePokemon(pokemon));
        }
        data.put("park", park);

        JSONArray inventory = new JSONArray();
        for (Item item : player.getInventory()) {
            inventory.add(serializeItem(item));
        }
        data.put("inventory", inventory);
        return data;
    }

    private static JSONObject serializePokemon(Pokemon pokemon) {
        JSONObject data = new JSONObject();
        data.put("id", pokemon.getId());
        data.put("name", pokemon.getName());
        data.put("primaryType", pokemon.getPrimaryType().name());
        data.put(
            "secondaryType",
            pokemon.getSecondaryType() == null ? null : pokemon.getSecondaryType().name()
        );
        data.put("pv", pokemon.getPv());
        data.put("maxPv", pokemon.getMaxPv());
        data.put("attack", pokemon.getAttack());
        data.put("defense", pokemon.getDefense());
        data.put("speed", pokemon.getSpeed());
        data.put("evolution", pokemon.getEvolution());
        data.put("evolutionLevel", pokemon.getEvolutionLevel());
        data.put("xp", pokemon.getXp());
        data.put("foodStatus", pokemon.getFoodStatus().name());
        data.put("staminaStatus", pokemon.getStaminaStatus().name());
        data.put("foodBar", pokemon.getFoodBar());
        data.put("staminaBar", pokemon.getStaminaBar());
        data.put("level", pokemon.getLevel());
        return data;
    }

    private static Pokemon deserializePokemon(JSONObject data) {
        Type primaryType = Type.valueOf(stringValue(data.get("primaryType"), Type.NORMAL.name()));
        String secondaryName = nullableString(data.get("secondaryType"));
        Type secondaryType = secondaryName == null ? null : Type.valueOf(secondaryName);

        Pokemon pokemon = new Pokemon(
            intValue(data.get("id"), 0),
            stringValue(data.get("name"), "MissingNo"),
            primaryType,
            secondaryType,
            intValue(data.get("maxPv"), 1),
            intValue(data.get("attack"), 1),
            intValue(data.get("defense"), 1),
            intValue(data.get("speed"), 1),
            nullableString(data.get("evolution")),
            intValue(data.get("evolutionLevel"), 0)
        );
        pokemon.setPv(intValue(data.get("pv"), pokemon.getMaxPv()));
        pokemon.setMaxPv(intValue(data.get("maxPv"), pokemon.getMaxPv()));
        pokemon.setXp(intValue(data.get("xp"), 0));
        pokemon.setFoodBar(intValue(data.get("foodBar"), Pokemon.MAXFOOD));
        pokemon.setStaminaBar(intValue(data.get("staminaBar"), Pokemon.MAXSTAMINA));
        pokemon.setLevel(intValue(data.get("level"), Pokemon.MINLEVEL));
        pokemon.setFoodStatus(Status.valueOf(
            stringValue(data.get("foodStatus"), Status.FULL.name())
        ));
        pokemon.setStaminaStatus(Status.valueOf(
            stringValue(data.get("staminaStatus"), Status.INSHAPE.name())
        ));
        return pokemon;
    }

    private static JSONObject serializeItem(Item item) {
        JSONObject data = new JSONObject();
        data.put("name", item.getName());
        data.put("price", item.getPrice());
        data.put("description", item.getDescription());

        if (item instanceof Food) {
            data.put("kind", "food");
            data.put("value", ((Food) item).getFoodPoint());
        } else if (item instanceof Heal) {
            data.put("kind", "heal");
            data.put("value", ((Heal) item).getHealPoint());
        } else if (item instanceof XpBoost) {
            data.put("kind", "xpBoost");
            data.put("value", ((XpBoost) item).getXpPoint());
        } else if (item instanceof Stone) {
            data.put("kind", "stone");
            data.put("stoneType", ((Stone) item).getStoneType().name());
        } else {
            data.put("kind", "unknown");
        }
        return data;
    }

    private static Item deserializeItem(JSONObject data) {
        String kind = stringValue(data.get("kind"), "unknown");
        String name = stringValue(data.get("name"), "Item");
        int price = intValue(data.get("price"), 0);
        String description = stringValue(data.get("description"), "");
        int value = intValue(data.get("value"), 0);

        switch (kind) {
            case "food":
                return new Food(name, price, description, value);
            case "heal":
                return new Heal(name, price, description, value);
            case "xpBoost":
                return new XpBoost(name, price, description, value);
            case "stone":
                return new Stone(
                    name,
                    price,
                    description,
                    StoneType.valueOf(stringValue(data.get("stoneType"), StoneType.FIRE.name()))
                );
            default:
                return null;
        }
    }

    private static JSONObject objectValue(Object value, String label) throws IOException {
        if (!(value instanceof JSONObject)) {
            throw new IOException("Invalid " + label + " data");
        }
        return (JSONObject) value;
    }

    private static JSONArray arrayValue(Object value) {
        return value instanceof JSONArray ? (JSONArray) value : new JSONArray();
    }

    private static int intValue(Object value, int fallback) {
        return value instanceof Number ? ((Number) value).intValue() : fallback;
    }

    private static String stringValue(Object value, String fallback) {
        return value instanceof String ? (String) value : fallback;
    }

    private static String nullableString(Object value) {
        return value instanceof String ? (String) value : null;
    }
}
