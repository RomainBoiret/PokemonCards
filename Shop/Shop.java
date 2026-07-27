package Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Player.Player;
import i18n.I18n;

public class Shop {

    private final ArrayList<Item> shopList;

    public Shop() {
        this.shopList = new ArrayList<>();
        stockDefaultItems();
    }

    private void stockDefaultItems() {
        shopList.add(new Food(I18n.t("item.oran.name"), 50, I18n.t("item.oran.desc"), 20));
        shopList.add(new Food(I18n.t("item.meal.name"), 120, I18n.t("item.meal.desc"), 50));
        shopList.add(new Food(I18n.t("item.feast.name"), 250, I18n.t("item.feast.desc"), 100));

        shopList.add(new Heal(I18n.t("item.potion.name"), 100, I18n.t("item.potion.desc"), 20));
        shopList.add(new Heal(I18n.t("item.super_potion.name"), 250, I18n.t("item.super_potion.desc"), 50));
        shopList.add(new Heal(I18n.t("item.hyper_potion.name"), 500, I18n.t("item.hyper_potion.desc"), 100));

        shopList.add(new XpBoost(I18n.t("item.xp_candy.name"), 300, I18n.t("item.xp_candy.desc"), 400));
        shopList.add(new XpBoost(I18n.t("item.super_xp.name"), 800, I18n.t("item.super_xp.desc"), 1200));

        shopList.add(new Stone(I18n.t("item.fire_stone.name"), 1500, I18n.t("item.fire_stone.desc"), StoneType.FIRE));
        shopList.add(new Stone(I18n.t("item.water_stone.name"), 1500, I18n.t("item.water_stone.desc"), StoneType.WATER));
        shopList.add(new Stone(I18n.t("item.thunder_stone.name"), 1500, I18n.t("item.thunder_stone.desc"), StoneType.THUNDER));
        shopList.add(new Stone(I18n.t("item.leaf_stone.name"), 1500, I18n.t("item.leaf_stone.desc"), StoneType.LEAF));
        shopList.add(new Stone(I18n.t("item.moon_stone.name"), 1500, I18n.t("item.moon_stone.desc"), StoneType.MOON));
        shopList.add(new Stone(I18n.t("item.link_cable.name"), 2000, I18n.t("item.link_cable.desc"), StoneType.TRADE));
    }

    public List<Item> getShopList() {
        return Collections.unmodifiableList(shopList);
    }

    public String buy(Player player, int index) {
        if (index < 0 || index >= shopList.size()) {
            return I18n.t("shop.invalid");
        }

        Item catalogItem = shopList.get(index);
        Item purchased = cloneItem(catalogItem);

        if (player.getPokeDollars() < purchased.getPrice()) {
            return I18n.t("shop.no_money", purchased.getPrice());
        }

        if (!player.addItem(purchased)) {
            return I18n.t("shop.inv_full");
        }

        player.setPokeDollars(player.getPokeDollars() - purchased.getPrice());
        return I18n.t("shop.bought", purchased.getName(), purchased.getPrice());
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
