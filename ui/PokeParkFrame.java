package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import MisteryBox.Rarity;
import Player.Player;
import Pokemon.Pokemon;
import Pokemon.Training;
import Shop.Food;
import Shop.Heal;
import Shop.Item;
import Shop.Shop;
import Shop.Stone;
import Shop.XpBoost;
import i18n.I18n;

public class PokeParkFrame extends JFrame {

    private final Player player;
    private final Shop shop = new Shop();

    private final JLabel headerLabel = new JLabel();
    private final JTextArea logArea = new JTextArea(5, 40);

    private final DefaultListModel<String> parkModel = new DefaultListModel<>();
    private final JList<String> parkList = new JList<>(parkModel);
    private final JTextArea detailArea = new JTextArea();

    private final DefaultListModel<String> inventoryModel = new DefaultListModel<>();
    private final JList<String> inventoryList = new JList<>(inventoryModel);

    private final DefaultListModel<String> shopModel = new DefaultListModel<>();
    private final JList<String> shopList = new JList<>(shopModel);

    public PokeParkFrame(Player player) {
        super(I18n.t("app.title"));
        this.player = player;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UiTheme.BG);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UiTheme.BG);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildTabs(), BorderLayout.CENTER);
        root.add(buildLog(), BorderLayout.SOUTH);
        setContentPane(root);

        fillShopList();
        refreshAll();
        log(I18n.t("app.welcome", player.getName()));
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.HEADER);
        header.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel brand = new JLabel(I18n.t("app.title"));
        brand.setFont(UiTheme.TITLE);
        brand.setForeground(UiTheme.HEADER_TEXT);

        headerLabel.setFont(UiTheme.BODY);
        headerLabel.setForeground(UiTheme.HEADER_TEXT);
        headerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton dayBtn = accentButton(I18n.t("header.pass_day"));
        dayBtn.addActionListener(e -> {
            log(player.collectParkIncomeAndPassDay());
            refreshAll();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(headerLabel);
        right.add(dayBtn);

        header.add(brand, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiTheme.HEADING);
        tabs.setBackground(UiTheme.BG);
        tabs.addTab(I18n.t("tab.park"), buildParkTab());
        tabs.addTab(I18n.t("tab.shop"), buildShopTab());
        tabs.addTab(I18n.t("tab.box"), buildBoxTab());
        tabs.addTab(I18n.t("tab.inventory"), buildInventoryTab());
        tabs.addTab(I18n.t("tab.upgrade"), buildUpgradeTab());
        return tabs;
    }

    private JPanel buildParkTab() {
        parkList.setFont(UiTheme.BODY);
        parkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        parkList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedPokemon();
            }
        });

        detailArea.setEditable(false);
        detailArea.setFont(UiTheme.MONO);
        detailArea.setBackground(UiTheme.PANEL);
        detailArea.setForeground(UiTheme.TEXT);
        detailArea.setBorder(UiTheme.pad(8));

        JPanel actions = new JPanel(new GridLayout(0, 2, 8, 8));
        actions.setOpaque(false);
        actions.setBorder(UiTheme.pad(8));

        JButton feedBtn = plainButton(I18n.t("park.feed"));
        JButton healBtn = plainButton(I18n.t("park.heal"));
        JButton boostBtn = plainButton(I18n.t("park.boost"));
        JButton stoneBtn = plainButton(I18n.t("park.evolve_stone"));
        JButton strengthBtn = accentButton(I18n.t("park.train_strength"));
        JButton defenseBtn = plainButton(I18n.t("park.train_defense"));
        JButton speedBtn = plainButton(I18n.t("park.train_speed"));

        feedBtn.addActionListener(e -> careFeed());
        healBtn.addActionListener(e -> careHeal());
        boostBtn.addActionListener(e -> careBoost());
        stoneBtn.addActionListener(e -> careStone());
        strengthBtn.addActionListener(e -> doTrain(Training.STRENGTH));
        defenseBtn.addActionListener(e -> doTrain(Training.DEFENSE));
        speedBtn.addActionListener(e -> doTrain(Training.SPEED));

        actions.add(feedBtn);
        actions.add(healBtn);
        actions.add(boostBtn);
        actions.add(stoneBtn);
        actions.add(strengthBtn);
        actions.add(defenseBtn);
        actions.add(speedBtn);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(UiTheme.BG);
        right.setBorder(UiTheme.pad(8));
        right.add(new JScrollPane(detailArea), BorderLayout.CENTER);
        right.add(actions, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(parkList),
            right
        );
        split.setResizeWeight(0.38);
        split.setBorder(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UiTheme.BG);
        panel.setBorder(UiTheme.pad(10));
        panel.add(hint(I18n.t("park.hint")), BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildShopTab() {
        shopList.setFont(UiTheme.BODY);
        JButton buyBtn = accentButton(I18n.t("shop.buy"));
        buyBtn.addActionListener(e -> {
            int index = shopList.getSelectedIndex();
            if (index < 0) {
                log(I18n.t("shop.pick_item"));
                return;
            }
            log(shop.buy(player, index));
            refreshAll();
        });

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(UiTheme.BG);
        panel.setBorder(UiTheme.pad(10));
        panel.add(hint(I18n.t("shop.hint")), BorderLayout.NORTH);
        panel.add(new JScrollPane(shopList), BorderLayout.CENTER);
        panel.add(buyBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildBoxTab() {
        JPanel grid = new JPanel(new GridLayout(0, 1, 8, 8));
        grid.setOpaque(false);
        for (Rarity rarity : Rarity.values()) {
            JButton btn = accentButton(I18n.t("box.button", rarity.name(), rarity.getPrice()));
            btn.addActionListener(e -> {
                log(player.buyMysteryBox(rarity));
                refreshAll();
            });
            grid.add(btn);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UiTheme.BG);
        panel.setBorder(UiTheme.pad(10));
        panel.add(hint(I18n.t("box.hint")), BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildInventoryTab() {
        inventoryList.setFont(UiTheme.BODY);
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(UiTheme.BG);
        panel.setBorder(UiTheme.pad(10));
        panel.add(hint(I18n.t("inv.hint")), BorderLayout.NORTH);
        panel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildUpgradeTab() {
        JButton parkBtn = accentButton(I18n.t(
            "upgrade.park",
            Player.PARK_INCREMENT_SIZE,
            Player.PARK_INCREMENT_COST
        ));
        JButton invBtn = plainButton(I18n.t(
            "upgrade.inventory",
            Player.INVENTORY_INCREMENT_SIZE,
            Player.INVENTORY_INCREMENT_COST
        ));

        parkBtn.addActionListener(e -> {
            if (player.updatePark(1)) {
                log(I18n.t("upgrade.park_ok", player.getParkCapacity()));
            } else {
                log(I18n.t("upgrade.no_money"));
            }
            refreshAll();
        });
        invBtn.addActionListener(e -> {
            if (player.updateInventory(1)) {
                log(I18n.t("upgrade.inv_ok", player.getInventoryCapacity()));
            } else {
                log(I18n.t("upgrade.no_money"));
            }
            refreshAll();
        });

        JPanel grid = new JPanel(new GridLayout(0, 1, 8, 8));
        grid.setOpaque(false);
        grid.add(parkBtn);
        grid.add(invBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UiTheme.BG);
        panel.setBorder(UiTheme.pad(10));
        panel.add(hint(I18n.t("upgrade.hint")), BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLog() {
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(UiTheme.SMALL);
        logArea.setBackground(UiTheme.LOG_BG);
        logArea.setForeground(UiTheme.LOG_FG);
        logArea.setBorder(UiTheme.pad(8));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UiTheme.LOG_BG);
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(100, 120));
        return panel;
    }

    private JLabel hint(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UiTheme.SMALL);
        label.setForeground(UiTheme.MUTED);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }

    private JButton accentButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.BODY);
        btn.setBackground(UiTheme.ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        return btn;
    }

    private JButton plainButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.BODY);
        btn.setBackground(UiTheme.PANEL);
        btn.setForeground(UiTheme.TEXT);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(UiTheme.card());
        return btn;
    }

    private void fillShopList() {
        shopModel.clear();
        List<Item> items = shop.getShopList();
        for (Item item : items) {
            shopModel.addElement(I18n.t("shop.item_line", item.getName(), item.getPrice(), item.getDescription()));
        }
    }

    private void refreshAll() {
        headerLabel.setText(I18n.t(
            "header.status",
            player.getName(),
            player.getDay(),
            player.getPokeDollars(),
            player.parkSize(),
            player.getParkCapacity(),
            player.inventorySize(),
            player.getInventoryCapacity()
        ));

        int selected = parkList.getSelectedIndex();
        parkModel.clear();
        for (Pokemon p : player.getPark()) {
            parkModel.addElement(I18n.t(
                "park.list_line",
                p.getName(),
                p.getLevel(),
                p.getPv(),
                p.getMaxPv(),
                p.getStaminaBar()
            ));
        }
        if (selected >= 0 && selected < parkModel.size()) {
            parkList.setSelectedIndex(selected);
        } else if (!parkModel.isEmpty()) {
            parkList.setSelectedIndex(0);
        } else {
            detailArea.setText(I18n.t("park.empty"));
        }
        showSelectedPokemon();

        inventoryModel.clear();
        for (Item item : player.getInventory()) {
            inventoryModel.addElement(I18n.t("inv.line", item.getName(), item.getDescription()));
        }
        if (inventoryModel.isEmpty()) {
            inventoryModel.addElement(I18n.t("inv.empty"));
        }
    }

    private void showSelectedPokemon() {
        Pokemon p = selectedPokemon();
        if (p == null) {
            if (player.parkSize() == 0) {
                detailArea.setText(I18n.t("park.empty"));
            }
            return;
        }
        detailArea.setText(p.detailedStatus());
    }

    private Pokemon selectedPokemon() {
        int index = parkList.getSelectedIndex();
        if (index < 0 || index >= player.parkSize()) {
            return null;
        }
        return player.getPark().get(index);
    }

    private void doTrain(Training training) {
        Pokemon p = selectedPokemon();
        if (p == null) {
            log(I18n.t("park.select"));
            return;
        }
        log(p.train(training));
        refreshAll();
    }

    private void careFeed() {
        Pokemon p = selectedPokemon();
        if (p == null) {
            log(I18n.t("park.select"));
            return;
        }
        Food food = pickFrom(
            player.getFoodsFromInventory(),
            I18n.t("dialog.food"),
            f -> I18n.t("dialog.food_line", f.getName(), f.getFoodPoint())
        );
        if (food != null) {
            log(p.feed(food));
            refreshAll();
        }
    }

    private void careHeal() {
        Pokemon p = selectedPokemon();
        if (p == null) {
            log(I18n.t("park.select"));
            return;
        }
        Heal heal = pickFrom(
            player.getHealsFromInventory(),
            I18n.t("dialog.heal"),
            h -> I18n.t("dialog.heal_line", h.getName(), h.getHealPoint())
        );
        if (heal != null) {
            log(p.heal(heal));
            refreshAll();
        }
    }

    private void careBoost() {
        Pokemon p = selectedPokemon();
        if (p == null) {
            log(I18n.t("park.select"));
            return;
        }
        XpBoost boost = pickFrom(
            player.getXpBoostsFromInventory(),
            I18n.t("dialog.boost"),
            b -> I18n.t("dialog.boost_line", b.getName(), b.getXpPoint())
        );
        if (boost != null) {
            log(p.useXpBoost(boost));
            refreshAll();
        }
    }

    private void careStone() {
        Pokemon p = selectedPokemon();
        if (p == null) {
            log(I18n.t("park.select"));
            return;
        }
        Stone stone = pickFrom(
            player.getStonesFromInventory(),
            I18n.t("dialog.stone"),
            s -> I18n.t("dialog.stone_line", s.getName(), s.getStoneType())
        );
        if (stone != null) {
            log(p.evolveWithStone(stone));
            refreshAll();
        }
    }

    private <T> T pickFrom(List<T> items, String title, java.util.function.Function<T, String> label) {
        if (items == null || items.isEmpty()) {
            log(I18n.t("dialog.no_item"));
            return null;
        }
        String[] labels = items.stream().map(label).toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(labels);
        int result = JOptionPane.showConfirmDialog(
            this,
            combo,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        return items.get(combo.getSelectedIndex());
    }

    private void log(String message) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        if (logArea.getText().isEmpty()) {
            logArea.setText(message);
        } else {
            logArea.append("\n" + message);
        }
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
