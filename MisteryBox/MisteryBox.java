package MisteryBox;

import Pokemon.Pokemon;
import Pokemon.PokemonFactory;

public class MisteryBox {

    private Rarity rarity;
    private int price;

    public MisteryBox(Rarity rarity) {
        this.rarity = rarity;
        this.price = rarity.getPrice();
    }

    public static MisteryBox createRandom() {
        return new MisteryBox(Rarity.roll());
    }

    public Pokemon open() {
        return PokemonFactory.obtenirPokemonAleatoire(rarity);
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Mystery Box " + rarity + " (" + price + " ₽)";
    }
}
