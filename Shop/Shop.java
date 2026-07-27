package Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Player.Player;

public class Shop {

    private final ArrayList<Item> shopList;

    public Shop() {
        this.shopList = new ArrayList<>();
        stockDefaultItems();
    }

    private void stockDefaultItems() {
        shopList.add(new Food("Baie Oran", 50, "Une petite baie nourrissante.", 20));
        shopList.add(new Food("Repas Standard", 120, "Un repas équilibré pour Pokémon.", 50));
        shopList.add(new Food("Festin Gourmet", 250, "Remplit presque toute la barre de faim.", 100));

        shopList.add(new Heal("Potion", 100, "Restaure un peu de PV.", 20));
        shopList.add(new Heal("Super Potion", 250, "Restaure une bonne quantité de PV.", 50));
        shopList.add(new Heal("Hyper Potion", 500, "Soins importants.", 100));

        shopList.add(new XpBoost("Bonbon XP", 300, "Donne un peu d'expérience.", 400));
        shopList.add(new XpBoost("Super Bonbon XP", 800, "Donne beaucoup d'expérience.", 1200));

        shopList.add(new Stone("Pierre Feu", 1500, "Fait évoluer Vulpix, Growlithe, Évoli…", StoneType.FIRE));
        shopList.add(new Stone("Pierre Eau", 1500, "Fait évoluer Poliwhirl, Shellder, Staryu, Évoli…", StoneType.WATER));
        shopList.add(new Stone("Pierre Foudre", 1500, "Fait évoluer Pikachu et Évoli.", StoneType.THUNDER));
        shopList.add(new Stone("Pierre Plante", 1500, "Fait évoluer Gloom, Weepinbell, Exeggcute.", StoneType.LEAF));
        shopList.add(new Stone("Pierre Lune", 1500, "Fait évoluer Nidorina, Nidorino, Clefairy, Jigglypuff.", StoneType.MOON));
        shopList.add(new Stone("Cable Link", 2000, "Fait évoluer Kadabra, Machoke, Graveler, Haunter.", StoneType.TRADE));
    }

    public List<Item> getShopList() {
        return Collections.unmodifiableList(shopList);
    }

    public String buy(Player player, int index) {
        if (index < 0 || index >= shopList.size()) {
            return "Article invalide.";
        }

        Item catalogItem = shopList.get(index);
        Item purchased = cloneItem(catalogItem);

        if (player.getPokeDollars() < purchased.getPrice()) {
            return "Pas assez de Pokédollars. Il faut " + purchased.getPrice() + " ₽.";
        }

        if (!player.addItem(purchased)) {
            return "Inventaire plein !";
        }

        player.setPokeDollars(player.getPokeDollars() - purchased.getPrice());
        return "Acheté : " + purchased.getName() + " (-" + purchased.getPrice() + " ₽).";
    }

    private Item cloneItem(Item item) {
        if (item instanceof Food) {
            Food food = (Food) item;
            return new Food(food.getName(), food.getPrice(), food.getDescription(), food.getFoodPoint());
        }
        if (item instanceof Heal) {
            Heal heal = (Heal) item;
            return new Heal(heal.getName(), heal.getPrice(), heal.getDescription(), heal.getHealPoint());
        }
        if (item instanceof XpBoost) {
            XpBoost boost = (XpBoost) item;
            return new XpBoost(boost.getName(), boost.getPrice(), boost.getDescription(), boost.getXpPoint());
        }
        if (item instanceof Stone) {
            Stone stone = (Stone) item;
            return new Stone(stone.getName(), stone.getPrice(), stone.getDescription(), stone.getStoneType());
        }
        return null;
    }
}
