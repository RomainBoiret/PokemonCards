package i18n;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Noms français officiels (1ʳᵉ génération).
 * Les clés restent les noms anglais du JSON.
 */
public final class PokemonNames {

    private static final Map<String, String> FR;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("Bulbasaur", "Bulbizarre");
        map.put("Ivysaur", "Herbizarre");
        map.put("Venusaur", "Florizarre");
        map.put("Charmander", "Salamèche");
        map.put("Charmeleon", "Reptincel");
        map.put("Charizard", "Dracaufeu");
        map.put("Squirtle", "Carapuce");
        map.put("Wartortle", "Carabaffe");
        map.put("Blastoise", "Tortank");
        map.put("Caterpie", "Chenipan");
        map.put("Metapod", "Chrysacier");
        map.put("Butterfree", "Papilusion");
        map.put("Weedle", "Aspicot");
        map.put("Kakuna", "Coconfort");
        map.put("Beedrill", "Dardargnan");
        map.put("Pidgey", "Roucool");
        map.put("Pidgeotto", "Roucoups");
        map.put("Pidgeot", "Roucarnage");
        map.put("Rattata", "Rattata");
        map.put("Raticate", "Rattatac");
        map.put("Spearow", "Piafabec");
        map.put("Fearow", "Rapasdepic");
        map.put("Ekans", "Abo");
        map.put("Arbok", "Arbok");
        map.put("Pikachu", "Pikachu");
        map.put("Raichu", "Raichu");
        map.put("Sandshrew", "Sabelette");
        map.put("Sandslash", "Sablaireau");
        map.put("Nidoran♀", "Nidoran♀");
        map.put("Nidorina", "Nidorina");
        map.put("Nidoqueen", "Nidoqueen");
        map.put("Nidoran♂", "Nidoran♂");
        map.put("Nidorino", "Nidorino");
        map.put("Nidoking", "Nidoking");
        map.put("Clefairy", "Mélofée");
        map.put("Clefable", "Mélodelfe");
        map.put("Vulpix", "Goupix");
        map.put("Ninetales", "Feunard");
        map.put("Jigglypuff", "Rondoudou");
        map.put("Wigglytuff", "Grodoudou");
        map.put("Zubat", "Nosferapti");
        map.put("Golbat", "Nosferalto");
        map.put("Oddish", "Mystherbe");
        map.put("Gloom", "Ortide");
        map.put("Vileplume", "Rafflesia");
        map.put("Paras", "Paras");
        map.put("Parasect", "Parasect");
        map.put("Venonat", "Mimitoss");
        map.put("Venomoth", "Aéromite");
        map.put("Diglett", "Taupiqueur");
        map.put("Dugtrio", "Triopikeur");
        map.put("Meowth", "Miaouss");
        map.put("Persian", "Persian");
        map.put("Psyduck", "Psykokwak");
        map.put("Golduck", "Akwakwak");
        map.put("Mankey", "Férosinge");
        map.put("Primeape", "Colossinge");
        map.put("Growlithe", "Caninos");
        map.put("Arcanine", "Arcanin");
        map.put("Poliwag", "Ptitard");
        map.put("Poliwhirl", "Têtarte");
        map.put("Poliwrath", "Tartard");
        map.put("Abra", "Abra");
        map.put("Kadabra", "Kadabra");
        map.put("Alakazam", "Alakazam");
        map.put("Machop", "Machoc");
        map.put("Machoke", "Machopeur");
        map.put("Machamp", "Mackogneur");
        map.put("Bellsprout", "Chétiflor");
        map.put("Weepinbell", "Boustiflor");
        map.put("Victreebel", "Empiflor");
        map.put("Tentacool", "Tentacool");
        map.put("Tentacruel", "Tentacruel");
        map.put("Geodude", "Racaillou");
        map.put("Graveler", "Gravalanch");
        map.put("Golem", "Grolem");
        map.put("Ponyta", "Ponyta");
        map.put("Rapidash", "Galopa");
        map.put("Slowpoke", "Ramoloss");
        map.put("Slowbro", "Flagadoss");
        map.put("Magnemite", "Magnéti");
        map.put("Magneton", "Magnéton");
        map.put("Farfetch'd", "Canarticho");
        map.put("Doduo", "Doduo");
        map.put("Dodrio", "Dodrio");
        map.put("Seel", "Otaria");
        map.put("Dewgong", "Lamantine");
        map.put("Grimer", "Tadmorv");
        map.put("Muk", "Grotadmorv");
        map.put("Shellder", "Kokiyas");
        map.put("Cloyster", "Crustabre");
        map.put("Gastly", "Fantominus");
        map.put("Haunter", "Spectrum");
        map.put("Gengar", "Ectoplasma");
        map.put("Onix", "Onix");
        map.put("Drowzee", "Soporifik");
        map.put("Hypno", "Hypnomade");
        map.put("Krabby", "Krabby");
        map.put("Kingler", "Krabboss");
        map.put("Voltorb", "Voltorbe");
        map.put("Electrode", "Électrode");
        map.put("Exeggcute", "Noeunoeuf");
        map.put("Exeggutor", "Noadkoko");
        map.put("Cubone", "Osselait");
        map.put("Marowak", "Ossatueur");
        map.put("Hitmonlee", "Kicklee");
        map.put("Hitmonchan", "Tygnon");
        map.put("Lickitung", "Excelangue");
        map.put("Koffing", "Smogo");
        map.put("Weezing", "Smogogo");
        map.put("Rhyhorn", "Rhinocorne");
        map.put("Rhydon", "Rhinoféros");
        map.put("Chansey", "Leveinard");
        map.put("Tangela", "Saquedeneu");
        map.put("Kangaskhan", "Kangourex");
        map.put("Horsea", "Hypotrempe");
        map.put("Seadra", "Hypocéan");
        map.put("Goldeen", "Poissirène");
        map.put("Seaking", "Poissoroy");
        map.put("Staryu", "Stari");
        map.put("Starmie", "Staross");
        map.put("Mr. Mime", "M. Mime");
        map.put("Scyther", "Insécateur");
        map.put("Jynx", "Lippoutou");
        map.put("Electabuzz", "Élektek");
        map.put("Magmar", "Magmar");
        map.put("Pinsir", "Scarabrute");
        map.put("Tauros", "Tauros");
        map.put("Magikarp", "Magicarpe");
        map.put("Gyarados", "Léviator");
        map.put("Lapras", "Lokhlass");
        map.put("Ditto", "Métamorph");
        map.put("Eevee", "Évoli");
        map.put("Vaporeon", "Aquali");
        map.put("Jolteon", "Voltali");
        map.put("Flareon", "Pyroli");
        map.put("Porygon", "Porygon");
        map.put("Omanyte", "Amonita");
        map.put("Omastar", "Amonistar");
        map.put("Kabuto", "Kabuto");
        map.put("Kabutops", "Kabutops");
        map.put("Aerodactyl", "Ptéra");
        map.put("Snorlax", "Ronflex");
        map.put("Articuno", "Artikodin");
        map.put("Zapdos", "Électhor");
        map.put("Moltres", "Sulfura");
        map.put("Dratini", "Minidraco");
        map.put("Dragonair", "Draco");
        map.put("Dragonite", "Dracolosse");
        map.put("Mewtwo", "Mewtwo");
        map.put("Mew", "Mew");
        FR = Collections.unmodifiableMap(map);
    }

    private PokemonNames() {
    }

    /** Nom affiché selon la langue active. */
    public static String display(String englishName) {
        if (englishName == null) {
            return null;
        }
        if (I18n.isEnglish()) {
            return englishName;
        }
        String fr = FR.get(englishName);
        return fr != null ? fr : englishName;
    }
}
