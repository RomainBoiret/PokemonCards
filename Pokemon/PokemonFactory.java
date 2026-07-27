package Pokemon;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import MisteryBox.Rarity;

public class PokemonFactory {

    private static final Set<String> LEGENDARIES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "Articuno", "Zapdos", "Moltres", "Mewtwo"
    )));
    private static final Set<String> MYTHICALS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Mew")));

    private static List<Pokemon> pokemons = new ArrayList<>();
    private static Set<String> evolutionNames = new HashSet<>();

    /** Charge depuis le classpath (JAR) puis depuis le fichier local. */
    public static void chargerPokemons(String fichierJSON) {
        pokemons.clear();
        evolutionNames.clear();

        try {
            String contenu = lireContenu(fichierJSON);
            if (contenu == null) {
                System.err.println("Impossible de lire " + fichierJSON);
                return;
            }

            JSONParser parser = new JSONParser();
            JSONArray pokemonData = (JSONArray) parser.parse(contenu);
            parsePokemonArray(pokemonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String lireContenu(String fichierJSON) throws Exception {
        String resourcePath = fichierJSON.startsWith("/") ? fichierJSON : "/" + fichierJSON;
        InputStream stream = PokemonFactory.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            stream = PokemonFactory.class.getResourceAsStream("/Pokemon/PokemonList.json");
        }
        if (stream != null) {
            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[4096];
                int n;
                while ((n = reader.read(buf)) >= 0) {
                    sb.append(buf, 0, n);
                }
                return sb.toString();
            }
        }

        if (Files.exists(Paths.get(fichierJSON))) {
            return new String(Files.readAllBytes(Paths.get(fichierJSON)), StandardCharsets.UTF_8);
        }
        return null;
    }

    private static void parsePokemonArray(JSONArray pokemonData) {
        for (Object obj : pokemonData) {
            try {
                JSONObject data = (JSONObject) obj;

                int id = ((Long) data.get("id")).intValue();
                String name = (String) data.get("name");
                JSONArray typesArray = (JSONArray) data.get("type");

                Type primaryType = Type.valueOf(((String) typesArray.get(0)).toUpperCase());

                Type secondaryType = null;
                if (typesArray.size() > 1) {
                    secondaryType = Type.valueOf(((String) typesArray.get(1)).toUpperCase());
                }

                int pv = ((Long) data.get("pv")).intValue();
                int attack = ((Long) data.get("attack")).intValue();
                int defense = ((Long) data.get("defense")).intValue();
                int speed = ((Long) data.get("speed")).intValue();
                String evolution = (String) data.get("evolution");

                int evolutionLevel = data.get("evolutionLevel") != null
                        ? ((Long) data.get("evolutionLevel")).intValue()
                        : 0;

                Pokemon pokemon = new Pokemon(
                    id,
                    name,
                    primaryType,
                    secondaryType,
                    pv,
                    attack,
                    defense,
                    speed,
                    evolution,
                    evolutionLevel
                );

                pokemons.add(pokemon);

                if (evolution != null && !evolution.equalsIgnoreCase("Multiple")) {
                    evolutionNames.add(evolution);
                }
                if ("Eevee".equals(name)) {
                    evolutionNames.add("Vaporeon");
                    evolutionNames.add("Jolteon");
                    evolutionNames.add("Flareon");
                }
            } catch (Exception entryError) {
                System.err.println("Pokémon ignoré (entrée JSON invalide) : " + entryError.getMessage());
            }
        }
    }

    public static boolean estCharge() {
        return !pokemons.isEmpty();
    }

    public static boolean isBaseForm(Pokemon pokemon) {
        return !evolutionNames.contains(pokemon.getName());
    }

    public static Pokemon obtenirPokemon(String nom) {
        if (nom == null) {
            return null;
        }

        for (Pokemon p : pokemons) {
            if (p.getName().equals(nom)) {
                return p.copy();
            }
        }
        return null;
    }

    public static Pokemon obtenirPokemonAleatoire(Rarity rarity) {
        List<Pokemon> pool = pokemons.stream()
            .filter(PokemonFactory::isBaseForm)
            .filter(p -> getRarity(p) == rarity)
            .collect(Collectors.toList());

        if (pool.isEmpty()) {
            pool = pokemons.stream()
                .filter(PokemonFactory::isBaseForm)
                .collect(Collectors.toList());
        }

        if (pool.isEmpty()) {
            pool = new ArrayList<>(pokemons);
        }

        Pokemon template = pool.get((int) (Math.random() * pool.size()));
        return template.copy();
    }

    public static Rarity getRarity(Pokemon pokemon) {
        String name = pokemon.getName();

        if (MYTHICALS.contains(name)) {
            return Rarity.MYTHICAL;
        }
        if (LEGENDARIES.contains(name)) {
            return Rarity.LEGENDARY;
        }

        int bst = pokemon.getBaseStatTotal();
        if (bst < 280) {
            return Rarity.COMMON;
        }
        if (bst < 360) {
            return Rarity.RARE;
        }
        return Rarity.EPIC;
    }

    public static List<Pokemon> obtenirTousPokemons() {
        return pokemons;
    }

    public static List<Pokemon> obtenirPokemonsDeBase() {
        return pokemons.stream()
            .filter(PokemonFactory::isBaseForm)
            .collect(Collectors.toList());
    }
}
