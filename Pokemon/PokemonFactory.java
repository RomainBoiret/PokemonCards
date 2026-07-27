package Pokemon;

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

import java.nio.file.Files;
import java.nio.file.Paths;

public class PokemonFactory {

    private static final Set<String> LEGENDARIES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "Articuno", "Zapdos", "Moltres", "Mewtwo"
    )));
    private static final Set<String> MYTHICALS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Mew")));

    private static List<Pokemon> pokemons = new ArrayList<>();
    private static Set<String> evolutionNames = new HashSet<>();

    public static void chargerPokemons(String fichierJSON) {
        pokemons.clear();
        evolutionNames.clear();

        try {
            String contenu = new String(Files.readAllBytes(Paths.get(fichierJSON)));

            JSONParser parser = new JSONParser();
            JSONArray pokemonData = (JSONArray) parser.parse(contenu);

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
                    // Branches d'Évoli : formes évoluées, pas des bases
                    if ("Eevee".equals(name)) {
                        evolutionNames.add("Vaporeon");
                        evolutionNames.add("Jolteon");
                        evolutionNames.add("Flareon");
                    }
                } catch (Exception entryError) {
                    System.err.println("Pokémon ignoré (entrée JSON invalide) : " + entryError.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean estCharge() {
        return !pokemons.isEmpty();
    }

    /** Forme de base = n'est l'évolution d'aucun autre Pokémon. */
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

    /** Tirage Mystery Box : uniquement des formes de base de la rareté demandée. */
    public static Pokemon obtenirPokemonAleatoire(Rarity rarity) {
        List<Pokemon> pool = pokemons.stream()
            .filter(PokemonFactory::isBaseForm)
            .filter(p -> getRarity(p) == rarity)
            .collect(Collectors.toList());

        if (pool.isEmpty()) {
            // Repli : toute forme de base (évite une box vide si peu d'EPIC de base)
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
