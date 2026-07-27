package Pokemon;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Shop.StoneType;

/**
 * Règles d'évolution hors niveau (pierres / échange).
 */
public final class EvolutionRules {

    private static final Map<StoneType, Set<String>> STONE_USERS = new HashMap<>();
    private static final Map<String, Map<StoneType, String>> BRANCHING = new HashMap<>();

    static {
        STONE_USERS.put(StoneType.FIRE, set("Vulpix", "Growlithe", "Eevee"));
        STONE_USERS.put(StoneType.WATER, set("Poliwhirl", "Shellder", "Staryu", "Eevee"));
        STONE_USERS.put(StoneType.THUNDER, set("Pikachu", "Eevee"));
        STONE_USERS.put(StoneType.LEAF, set("Gloom", "Weepinbell", "Exeggcute"));
        STONE_USERS.put(StoneType.MOON, set("Nidorina", "Nidorino", "Clefairy", "Jigglypuff"));
        STONE_USERS.put(StoneType.TRADE, set("Kadabra", "Machoke", "Graveler", "Haunter"));

        Map<StoneType, String> eevee = new HashMap<>();
        eevee.put(StoneType.FIRE, "Flareon");
        eevee.put(StoneType.WATER, "Vaporeon");
        eevee.put(StoneType.THUNDER, "Jolteon");
        BRANCHING.put("Eevee", eevee);
    }

    private EvolutionRules() {
    }

    private static Set<String> set(String... names) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(names)));
    }

    public static boolean canUseStone(Pokemon pokemon, StoneType stoneType) {
        Set<String> users = STONE_USERS.get(stoneType);
        return users != null && users.contains(pokemon.getName());
    }

    public static String resolveEvolutionTarget(Pokemon pokemon, StoneType stoneType) {
        if (!canUseStone(pokemon, stoneType)) {
            return null;
        }

        Map<StoneType, String> branches = BRANCHING.get(pokemon.getName());
        if (branches != null) {
            return branches.get(stoneType);
        }

        String evolution = pokemon.getEvolution();
        if (evolution == null || evolution.equalsIgnoreCase("Multiple")) {
            return null;
        }
        return evolution;
    }

    public static boolean needsStone(Pokemon pokemon) {
        if (pokemon.getEvolution() == null) {
            return false;
        }
        if (pokemon.getEvolution().equalsIgnoreCase("Multiple")) {
            return true;
        }
        return pokemon.getEvolutionLevel() <= 0;
    }

    public static boolean canEvolveByLevel(Pokemon pokemon) {
        return pokemon.getEvolution() != null
            && !pokemon.getEvolution().equalsIgnoreCase("Multiple")
            && pokemon.getEvolutionLevel() > 0
            && pokemon.getLevel() >= pokemon.getEvolutionLevel();
    }
}
