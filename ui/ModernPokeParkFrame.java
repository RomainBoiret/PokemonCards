package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
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
import save.SaveManager;
import ui.UiComponents.Badge;
import ui.UiComponents.ModernButton;
import ui.UiComponents.ProgressBar;
import ui.UiComponents.RoundedPanel;
import ui.UiComponents.SidebarButton;

/**
 * Main desktop experience: sidebar navigation, dashboard cards and focused
 * interactions while preserving the existing game logic.
 */
public class ModernPokeParkFrame extends JFrame {

    private static final String PARK = "park";
    private static final String SHOP = "shop";
    private static final String BOXES = "boxes";
    private static final String INVENTORY = "inventory";
    private static final String UPGRADES = "upgrades";

    private final Player player;
    private final Shop shop = new Shop();

    private final CardLayout contentLayout = new CardLayout();
    private final JPanel content = new JPanel(contentLayout);
    private final Map<String, SidebarButton> navButtons = new LinkedHashMap<>();
    private final JLabel pageTitle = new JLabel();
    private final JLabel dayValue = new JLabel();
    private final JLabel moneyValue = new JLabel();
    private final JLabel parkValue = new JLabel();
    private final JLabel inventoryValue = new JLabel();
    private final JTextArea activityLog = new JTextArea();

    private final DefaultListModel<String> parkModel = new DefaultListModel<>();
    private final JList<String> parkList = new JList<>(parkModel);
    private final JLabel pokemonName = new JLabel();
    private final JLabel pokemonMeta = new JLabel();
    private final JLabel hpLabel = new JLabel();
    private final JLabel staminaLabel = new JLabel();
    private final JLabel hungerLabel = new JLabel();
    private final ProgressBar hpBar = new ProgressBar(100, UiTheme.HEALTH);
    private final ProgressBar staminaBar = new ProgressBar(100, UiTheme.STAMINA);
    private final ProgressBar hungerBar = new ProgressBar(100, UiTheme.HUNGER);
    private final JTextArea pokemonDetails = new JTextArea();

    private final DefaultListModel<String> shopModel = new DefaultListModel<>();
    private final JList<String> shopList = new JList<>(shopModel);
    private final JLabel shopSelection = new JLabel(" ");
    private final DefaultListModel<String> inventoryModel = new DefaultListModel<>();
    private final JList<String> inventoryList = new JList<>(inventoryModel);

    public ModernPokeParkFrame(Player player) {
        super(I18n.t("app.title"));
        this.player = player;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
        setSize(1220, 790);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                saveGame();
            }
        });

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildWorkspace(), BorderLayout.CENTER);
        setContentPane(root);

        configureLists();
        fillShop();
        refreshAll();
        showPage(PARK, I18n.t("tab.park"));
        log(I18n.t("app.welcome", player.getName()));
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UiTheme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(236, 0));
        sidebar.setBorder(new EmptyBorder(24, 18, 20, 18));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(buildBrand());
        sidebar.add(Box.createVerticalStrut(18));
        sidebar.add(buildManagerCard());
        sidebar.add(Box.createVerticalStrut(26));

        JLabel navigation = new JLabel(I18n.isEnglish() ? "NAVIGATION" : "NAVIGATION");
        navigation.setForeground(new Color(111, 153, 126));
        navigation.setFont(UiTheme.SMALL_BOLD);
        navigation.setBorder(new EmptyBorder(0, 10, 8, 0));
        navigation.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navigation);

        addNav(sidebar, PARK, "P", I18n.t("tab.park"));
        addNav(sidebar, SHOP, "B", I18n.t("tab.shop"));
        addNav(sidebar, BOXES, "M", I18n.t("tab.box"));
        addNav(sidebar, INVENTORY, "I", I18n.t("tab.inventory"));
        addNav(sidebar, UPGRADES, "+", I18n.t("tab.upgrade"));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(buildSidebarTip());
        sidebar.add(Box.createVerticalStrut(18));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(UiTheme.SIDEBAR_ACTIVE);
        separator.setBackground(UiTheme.SIDEBAR_ACTIVE);
        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(16));

        JLabel caption = new JLabel("PokePark Desktop");
        caption.setForeground(new Color(132, 170, 145));
        caption.setFont(UiTheme.SMALL);
        caption.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(caption);
        return sidebar;
    }

    private JPanel buildBrand() {
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mark = new JLabel("P", SwingConstants.CENTER);
        mark.setPreferredSize(new Dimension(40, 40));
        mark.setOpaque(true);
        mark.setBackground(UiTheme.ACCENT);
        mark.setForeground(Color.WHITE);
        mark.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        mark.setBorder(BorderFactory.createLineBorder(UiTheme.ACCENT, 8));

        JLabel logo = new JLabel("POKEPARK");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI Semibold", Font.BOLD, 21));
        brand.add(mark);
        brand.add(logo);
        return brand;
    }

    private JPanel buildManagerCard() {
        RoundedPanel card = new RoundedPanel(14, new Color(28, 64, 48));
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(new EmptyBorder(10, 11, 10, 11));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatar = new JLabel(
            player.getName().isEmpty() ? "M" : player.getName().substring(0, 1).toUpperCase(),
            SwingConstants.CENTER
        );
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setOpaque(true);
        avatar.setBackground(UiTheme.PRIMARY);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(UiTheme.BODY_BOLD);

        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        JLabel manager = new JLabel(player.getName());
        manager.setForeground(Color.WHITE);
        manager.setFont(UiTheme.BODY_BOLD);
        JLabel role = new JLabel(I18n.isEnglish() ? "Park manager" : "Gestionnaire du parc");
        role.setForeground(new Color(150, 188, 163));
        role.setFont(UiTheme.SMALL);
        identity.add(manager);
        identity.add(role);
        card.add(avatar, BorderLayout.WEST);
        card.add(identity, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildSidebarTip() {
        RoundedPanel tip = new RoundedPanel(14, new Color(31, 70, 52));
        tip.setLayout(new BorderLayout(0, 6));
        tip.setBorder(new EmptyBorder(11, 12, 11, 12));
        tip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        tip.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel(I18n.isEnglish() ? "DAILY TIP" : "CONSEIL");
        title.setForeground(UiTheme.ACCENT);
        title.setFont(UiTheme.SMALL_BOLD);
        JLabel copy = new JLabel(
            I18n.isEnglish()
                ? "<html>Pass a day to refill<br>every Pokémon's stamina.</html>"
                : "<html>Passe un jour pour remplir<br>la stamina de tes Pokémon.</html>"
        );
        copy.setForeground(new Color(194, 218, 202));
        copy.setFont(UiTheme.SMALL);
        tip.add(title, BorderLayout.NORTH);
        tip.add(copy, BorderLayout.CENTER);
        return tip;
    }

    private void addNav(JPanel sidebar, String key, String marker, String text) {
        SidebarButton button = new SidebarButton(marker, text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(e -> showPage(key, pageName(key)));
        navButtons.put(key, button);
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(7));
    }

    private String pageName(String key) {
        if (SHOP.equals(key)) return I18n.t("tab.shop");
        if (BOXES.equals(key)) return I18n.t("tab.box");
        if (INVENTORY.equals(key)) return I18n.t("tab.inventory");
        if (UPGRADES.equals(key)) return I18n.t("tab.upgrade");
        return I18n.t("tab.park");
    }

    private JPanel buildWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout(0, 16));
        workspace.setBackground(UiTheme.BG);
        workspace.setBorder(new EmptyBorder(22, 26, 20, 26));
        workspace.add(buildTopArea(), BorderLayout.NORTH);

        content.setOpaque(false);
        content.add(buildParkPage(), PARK);
        content.add(buildShopPage(), SHOP);
        content.add(buildBoxesPage(), BOXES);
        content.add(buildInventoryPage(), INVENTORY);
        content.add(buildUpgradesPage(), UPGRADES);
        workspace.add(content, BorderLayout.CENTER);
        workspace.add(buildActivityBar(), BorderLayout.SOUTH);
        return workspace;
    }

    private JPanel buildTopArea() {
        JPanel top = new JPanel(new BorderLayout(16, 14));
        top.setOpaque(false);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        pageTitle.setFont(UiTheme.DISPLAY);
        pageTitle.setForeground(UiTheme.TEXT);
        titleRow.add(pageTitle, BorderLayout.WEST);

        ModernButton nextDay = new ModernButton(
            I18n.t("header.pass_day"),
            UiTheme.ACCENT,
            Color.WHITE,
            14
        );
        nextDay.addActionListener(e -> {
            log(player.collectParkIncomeAndPassDay());
            refreshAll();
        });
        titleRow.add(nextDay, BorderLayout.EAST);
        top.add(titleRow, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.add(statCard(I18n.isEnglish() ? "DAY" : "JOUR", dayValue, UiTheme.PRIMARY));
        stats.add(statCard(I18n.isEnglish() ? "BALANCE" : "SOLDE", moneyValue, UiTheme.ACCENT));
        stats.add(statCard(I18n.isEnglish() ? "PARK" : "PARC", parkValue, UiTheme.STAMINA));
        stats.add(statCard(I18n.isEnglish() ? "INVENTORY" : "INVENTAIRE", inventoryValue, new Color(126, 91, 177)));
        top.add(stats, BorderLayout.CENTER);
        return top;
    }

    private JPanel statCard(String label, JLabel value, Color accent) {
        RoundedPanel card = new RoundedPanel(18, UiTheme.SURFACE, UiTheme.BORDER);
        card.setLayout(new BorderLayout(10, 2));
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        JLabel title = new JLabel(label);
        title.setFont(UiTheme.SMALL_BOLD);
        title.setForeground(UiTheme.MUTED);
        value.setFont(UiTheme.HEADING);
        value.setForeground(UiTheme.TEXT);
        Badge dot = new Badge("●", new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 30), accent);
        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.add(dot, BorderLayout.EAST);
        return card;
    }

    private JPanel buildParkPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 16, 0));
        page.setOpaque(false);

        RoundedPanel listCard = card();
        listCard.setLayout(new BorderLayout(0, 12));
        listCard.add(sectionHeader(I18n.t("tab.park"), I18n.t("park.hint")), BorderLayout.NORTH);
        JScrollPane listScroll = cleanScroll(parkList);
        listCard.add(listScroll, BorderLayout.CENTER);

        RoundedPanel detailCard = card();
        detailCard.setLayout(new BorderLayout(0, 14));
        detailCard.add(buildPokemonSummary(), BorderLayout.NORTH);

        pokemonDetails.setEditable(false);
        pokemonDetails.setOpaque(false);
        pokemonDetails.setFont(UiTheme.BODY);
        pokemonDetails.setForeground(UiTheme.MUTED);
        pokemonDetails.setLineWrap(true);
        pokemonDetails.setWrapStyleWord(true);
        detailCard.add(pokemonDetails, BorderLayout.CENTER);
        detailCard.add(buildPokemonActions(), BorderLayout.SOUTH);

        page.add(listCard);
        page.add(detailCard);
        return page;
    }

    private JPanel buildPokemonSummary() {
        JPanel summary = new JPanel(new BorderLayout(12, 10));
        summary.setOpaque(false);

        JLabel avatar = new JLabel("PK", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(58, 58));
        avatar.setOpaque(true);
        avatar.setBackground(UiTheme.ACCENT_SOFT);
        avatar.setForeground(UiTheme.ACCENT);
        avatar.setFont(UiTheme.HEADING);
        avatar.setBorder(BorderFactory.createLineBorder(UiTheme.ACCENT_SOFT, 8));

        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        pokemonName.setFont(UiTheme.TITLE);
        pokemonName.setForeground(UiTheme.TEXT);
        pokemonMeta.setFont(UiTheme.SMALL);
        pokemonMeta.setForeground(UiTheme.MUTED);
        identity.add(pokemonName);
        identity.add(Box.createVerticalStrut(3));
        identity.add(pokemonMeta);

        JPanel bars = new JPanel(new GridLayout(3, 1, 0, 7));
        bars.setOpaque(false);
        bars.add(metricRow(hpLabel, hpBar));
        bars.add(metricRow(staminaLabel, staminaBar));
        bars.add(metricRow(hungerLabel, hungerBar));

        summary.add(avatar, BorderLayout.WEST);
        summary.add(identity, BorderLayout.CENTER);
        summary.add(bars, BorderLayout.SOUTH);
        return summary;
    }

    private JPanel metricRow(JLabel label, ProgressBar bar) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        label.setPreferredSize(new Dimension(115, 18));
        label.setFont(UiTheme.SMALL);
        label.setForeground(UiTheme.MUTED);
        row.add(label, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildPokemonActions() {
        JPanel actions = new JPanel(new GridLayout(2, 4, 8, 8));
        actions.setOpaque(false);
        addAction(actions, I18n.t("park.feed"), UiTheme.PRIMARY, this::careFeed);
        addAction(actions, I18n.t("park.heal"), UiTheme.HEALTH, this::careHeal);
        addAction(actions, I18n.t("park.boost"), UiTheme.STAMINA, this::careBoost);
        addAction(actions, I18n.t("park.evolve_stone"), UiTheme.ACCENT, this::careStone);
        addAction(actions, I18n.t("park.train_strength"), UiTheme.PRIMARY_DARK, () -> train(Training.STRENGTH));
        addAction(actions, I18n.t("park.train_defense"), UiTheme.PRIMARY_DARK, () -> train(Training.DEFENSE));
        addAction(actions, I18n.t("park.train_speed"), UiTheme.PRIMARY_DARK, () -> train(Training.SPEED));
        return actions;
    }

    private void addAction(JPanel parent, String text, Color color, Runnable action) {
        ModernButton button = new ModernButton(text, color, Color.WHITE, 12);
        button.addActionListener(e -> action.run());
        parent.add(button);
    }

    private JPanel buildShopPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 16, 0));
        page.setOpaque(false);

        RoundedPanel catalog = card();
        catalog.setLayout(new BorderLayout(0, 12));
        catalog.add(sectionHeader(I18n.t("tab.shop"), I18n.t("shop.hint")), BorderLayout.NORTH);
        catalog.add(cleanScroll(shopList), BorderLayout.CENTER);

        RoundedPanel checkout = card();
        checkout.setLayout(new BorderLayout(0, 18));
        checkout.add(sectionHeader(I18n.isEnglish() ? "CHECKOUT" : "ACHAT", I18n.t("shop.pick_item")), BorderLayout.NORTH);
        shopSelection.setFont(UiTheme.HEADING);
        shopSelection.setForeground(UiTheme.TEXT);
        shopSelection.setVerticalAlignment(SwingConstants.TOP);
        checkout.add(shopSelection, BorderLayout.CENTER);
        ModernButton buy = new ModernButton(I18n.t("shop.buy"), UiTheme.ACCENT, Color.WHITE, 14);
        buy.addActionListener(e -> buySelectedItem());
        checkout.add(buy, BorderLayout.SOUTH);

        page.add(catalog);
        page.add(checkout);
        return page;
    }

    private JPanel buildBoxesPage() {
        JPanel page = new JPanel(new BorderLayout(0, 14));
        page.setOpaque(false);
        page.add(sectionHeader(I18n.t("tab.box"), I18n.t("box.hint")), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        for (Rarity rarity : Rarity.values()) {
            grid.add(boxCard(rarity));
        }
        page.add(grid, BorderLayout.CENTER);
        return page;
    }

    private JPanel boxCard(Rarity rarity) {
        Color color = rarityColor(rarity);
        RoundedPanel card = new RoundedPanel(20, UiTheme.SURFACE, UiTheme.BORDER);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel rarityLabel = new JLabel(rarity.name());
        rarityLabel.setFont(UiTheme.HEADING);
        rarityLabel.setForeground(color);
        JLabel price = new JLabel(rarity.getPrice() + " ₽");
        price.setFont(UiTheme.TITLE);
        price.setForeground(UiTheme.TEXT);
        JLabel description = new JLabel(
            "<html><body style='width:150px;color:#66766c'>"
                + rarityDescription(rarity)
                + "</body></html>"
        );
        description.setFont(UiTheme.SMALL);

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(rarityLabel);
        copy.add(Box.createVerticalStrut(8));
        copy.add(price);
        copy.add(Box.createVerticalStrut(8));
        copy.add(description);

        ModernButton open = new ModernButton(
            I18n.isEnglish() ? "Open box" : "Ouvrir la box",
            color,
            Color.WHITE,
            12
        );
        open.addActionListener(e -> {
            log(player.buyMysteryBox(rarity));
            refreshAll();
        });
        card.add(copy, BorderLayout.CENTER);
        card.add(open, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildInventoryPage() {
        RoundedPanel page = card();
        page.setLayout(new BorderLayout(0, 12));
        page.add(sectionHeader(I18n.t("tab.inventory"), I18n.t("inv.hint")), BorderLayout.NORTH);
        page.add(cleanScroll(inventoryList), BorderLayout.CENTER);
        return page;
    }

    private JPanel buildUpgradesPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 16, 0));
        page.setOpaque(false);
        page.add(upgradeCard(
            I18n.isEnglish() ? "PARK CAPACITY" : "CAPACITÉ DU PARC",
            I18n.t("upgrade.park", Player.PARK_INCREMENT_SIZE, Player.PARK_INCREMENT_COST),
            UiTheme.PRIMARY,
            () -> {
                if (player.updatePark(1)) log(I18n.t("upgrade.park_ok", player.getParkCapacity()));
                else log(I18n.t("upgrade.no_money"));
                refreshAll();
            }
        ));
        page.add(upgradeCard(
            I18n.isEnglish() ? "INVENTORY CAPACITY" : "CAPACITÉ DE L'INVENTAIRE",
            I18n.t("upgrade.inventory", Player.INVENTORY_INCREMENT_SIZE, Player.INVENTORY_INCREMENT_COST),
            new Color(126, 91, 177),
            () -> {
                if (player.updateInventory(1)) log(I18n.t("upgrade.inv_ok", player.getInventoryCapacity()));
                else log(I18n.t("upgrade.no_money"));
                refreshAll();
            }
        ));
        return page;
    }

    private JPanel upgradeCard(String title, String actionLabel, Color color, Runnable action) {
        RoundedPanel card = card();
        card.setLayout(new BorderLayout(0, 18));
        JLabel heading = new JLabel(title);
        heading.setFont(UiTheme.HEADING);
        heading.setForeground(UiTheme.TEXT);
        JLabel description = new JLabel("<html><body style='width:260px'>" + I18n.t("upgrade.hint") + "</body></html>");
        description.setForeground(UiTheme.MUTED);
        description.setFont(UiTheme.BODY);
        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(heading);
        copy.add(Box.createVerticalStrut(12));
        copy.add(description);
        ModernButton actionButton = new ModernButton(actionLabel, color, Color.WHITE, 14);
        actionButton.addActionListener(e -> action.run());
        card.add(copy, BorderLayout.NORTH);
        card.add(actionButton, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildActivityBar() {
        RoundedPanel panel = new RoundedPanel(14, UiTheme.LOG_BG);
        panel.setLayout(new BorderLayout(12, 0));
        panel.setBorder(new EmptyBorder(9, 14, 9, 14));
        JLabel label = new JLabel(I18n.isEnglish() ? "ACTIVITY" : "ACTIVITÉ");
        label.setForeground(UiTheme.ACCENT);
        label.setFont(UiTheme.SMALL_BOLD);
        activityLog.setEditable(false);
        activityLog.setOpaque(false);
        activityLog.setForeground(UiTheme.LOG_FG);
        activityLog.setFont(UiTheme.SMALL);
        activityLog.setLineWrap(true);
        activityLog.setWrapStyleWord(true);
        activityLog.setRows(2);
        panel.add(label, BorderLayout.WEST);
        panel.add(activityLog, BorderLayout.CENTER);
        return panel;
    }

    private RoundedPanel card() {
        RoundedPanel panel = new RoundedPanel(20, UiTheme.SURFACE, UiTheme.BORDER);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private JPanel sectionHeader(String title, String subtitle) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(UiTheme.SUBHEADING);
        titleLabel.setForeground(UiTheme.TEXT);
        JLabel subtitleLabel = new JLabel("<html><body style='width:420px'>" + subtitle + "</body></html>");
        subtitleLabel.setFont(UiTheme.SMALL);
        subtitleLabel.setForeground(UiTheme.MUTED);
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitleLabel);
        return header;
    }

    private JScrollPane cleanScroll(Component view) {
        JScrollPane scroll = new JScrollPane(view);
        scroll.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        scroll.getViewport().setBackground(UiTheme.SURFACE);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        return scroll;
    }

    private void configureLists() {
        configureList(parkList);
        configureList(shopList);
        configureList(inventoryList);

        parkList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) refreshPokemonDetails();
        });
        shopList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) refreshShopSelection();
        });
    }

    private void configureList(JList<String> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(UiTheme.BODY);
        list.setForeground(UiTheme.TEXT);
        list.setBackground(UiTheme.SURFACE);
        list.setFixedCellHeight(48);
        list.setBorder(new EmptyBorder(4, 4, 4, 4));
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> source,
                Object value,
                int index,
                boolean selected,
                boolean focused
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    source, value, index, selected, focused
                );
                label.setBorder(new EmptyBorder(7, 12, 7, 12));
                label.setFont(selected ? UiTheme.BODY_BOLD : UiTheme.BODY);
                label.setBackground(selected ? UiTheme.SURFACE_ALT : UiTheme.SURFACE);
                label.setForeground(selected ? UiTheme.PRIMARY_DARK : UiTheme.TEXT);
                return label;
            }
        });
    }

    private void showPage(String key, String title) {
        contentLayout.show(content, key);
        pageTitle.setText(title);
        navButtons.forEach((navKey, button) -> button.setActive(navKey.equals(key)));
    }

    private void fillShop() {
        shopModel.clear();
        for (Item item : shop.getShopList()) {
            shopModel.addElement(I18n.t("shop.item_line", item.getName(), item.getPrice(), item.getDescription()));
        }
        if (!shopModel.isEmpty()) shopList.setSelectedIndex(0);
    }

    private void refreshAll() {
        dayValue.setText(String.valueOf(player.getDay()));
        moneyValue.setText(player.getPokeDollars() + " ₽");
        parkValue.setText(player.parkSize() + " / " + player.getParkCapacity());
        inventoryValue.setText(player.inventorySize() + " / " + player.getInventoryCapacity());

        int selectedPokemon = parkList.getSelectedIndex();
        parkModel.clear();
        for (Pokemon pokemon : player.getPark()) {
            parkModel.addElement(
                pokemon.getDisplayName()
                    + "   ·   Lv." + pokemon.getLevel()
                    + "   ·   " + pokemon.getPv() + "/" + pokemon.getMaxPv() + " HP"
            );
        }
        if (!parkModel.isEmpty()) {
            parkList.setSelectedIndex(
                selectedPokemon >= 0 && selectedPokemon < parkModel.size() ? selectedPokemon : 0
            );
        }

        inventoryModel.clear();
        for (Item item : player.getInventory()) {
            inventoryModel.addElement(I18n.t("inv.line", item.getName(), item.getDescription()));
        }
        if (inventoryModel.isEmpty()) inventoryModel.addElement(I18n.t("inv.empty"));
        refreshPokemonDetails();
        refreshShopSelection();
        saveGame();
    }

    private void refreshPokemonDetails() {
        Pokemon pokemon = selectedPokemon();
        if (pokemon == null) {
            pokemonName.setText(I18n.t("park.empty"));
            pokemonMeta.setText(" ");
            pokemonDetails.setText(I18n.t("box.hint"));
            hpLabel.setText("HP —");
            staminaLabel.setText("Stamina —");
            hungerLabel.setText(I18n.isEnglish() ? "Hunger —" : "Faim —");
            hpBar.setValue(0);
            staminaBar.setValue(0);
            hungerBar.setValue(0);
            return;
        }

        pokemonName.setText(pokemon.getDisplayName());
        String type = pokemon.getPrimaryType().name();
        if (pokemon.getSecondaryType() != null) type += " / " + pokemon.getSecondaryType().name();
        pokemonMeta.setText("Lv." + pokemon.getLevel() + "  ·  " + type + "  ·  " + pokemon.evolutionHint());
        pokemonDetails.setText(pokemon.detailedStatus());

        hpLabel.setText("HP  " + pokemon.getPv() + "/" + pokemon.getMaxPv());
        staminaLabel.setText("Stamina  " + pokemon.getStaminaBar() + "/100");
        hungerLabel.setText((I18n.isEnglish() ? "Hunger  " : "Faim  ") + pokemon.getFoodBar() + "/100");
        hpBar.setValue((int) Math.round(pokemon.getPv() * 100.0 / Math.max(1, pokemon.getMaxPv())));
        staminaBar.setValue(pokemon.getStaminaBar());
        hungerBar.setValue(pokemon.getFoodBar());
    }

    private void refreshShopSelection() {
        int index = shopList.getSelectedIndex();
        if (index < 0 || index >= shop.getShopList().size()) {
            shopSelection.setText(I18n.t("shop.pick_item"));
            return;
        }
        Item item = shop.getShopList().get(index);
        shopSelection.setText(
            "<html><body style='width:330px'>"
                + "<span style='font-size:16px'><b>" + item.getName() + "</b></span>"
                + "<br><br>" + item.getDescription()
                + "<br><br><span style='font-size:18px;color:#eb8732'><b>" + item.getPrice() + " ₽</b></span>"
                + "</body></html>"
        );
    }

    private void buySelectedItem() {
        int index = shopList.getSelectedIndex();
        if (index < 0) {
            log(I18n.t("shop.pick_item"));
            return;
        }
        log(shop.buy(player, index));
        refreshAll();
    }

    private Pokemon selectedPokemon() {
        int index = parkList.getSelectedIndex();
        return index >= 0 && index < player.parkSize() ? player.getPark().get(index) : null;
    }

    private void train(Training training) {
        Pokemon pokemon = requirePokemon();
        if (pokemon == null) return;
        log(pokemon.train(training));
        refreshAll();
    }

    private void careFeed() {
        Pokemon pokemon = requirePokemon();
        if (pokemon == null) return;
        Food food = pick(
            player.getFoodsFromInventory(),
            I18n.t("dialog.food"),
            item -> I18n.t("dialog.food_line", item.getName(), item.getFoodPoint())
        );
        if (food != null) {
            log(pokemon.feed(food));
            refreshAll();
        }
    }

    private void careHeal() {
        Pokemon pokemon = requirePokemon();
        if (pokemon == null) return;
        Heal heal = pick(
            player.getHealsFromInventory(),
            I18n.t("dialog.heal"),
            item -> I18n.t("dialog.heal_line", item.getName(), item.getHealPoint())
        );
        if (heal != null) {
            log(pokemon.heal(heal));
            refreshAll();
        }
    }

    private void careBoost() {
        Pokemon pokemon = requirePokemon();
        if (pokemon == null) return;
        XpBoost boost = pick(
            player.getXpBoostsFromInventory(),
            I18n.t("dialog.boost"),
            item -> I18n.t("dialog.boost_line", item.getName(), item.getXpPoint())
        );
        if (boost != null) {
            log(pokemon.useXpBoost(boost));
            refreshAll();
        }
    }

    private void careStone() {
        Pokemon pokemon = requirePokemon();
        if (pokemon == null) return;
        Stone stone = pick(
            player.getStonesFromInventory(),
            I18n.t("dialog.stone"),
            item -> I18n.t("dialog.stone_line", item.getName(), item.getStoneType())
        );
        if (stone != null) {
            log(pokemon.evolveWithStone(stone));
            refreshAll();
        }
    }

    private Pokemon requirePokemon() {
        Pokemon pokemon = selectedPokemon();
        if (pokemon == null) log(I18n.t("park.select"));
        return pokemon;
    }

    private <T> T pick(List<T> items, String title, Function<T, String> formatter) {
        if (items == null || items.isEmpty()) {
            log(I18n.t("dialog.no_item"));
            return null;
        }
        String[] labels = items.stream().map(formatter).toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(labels);
        combo.setFont(UiTheme.BODY);
        int result = JOptionPane.showConfirmDialog(
            this,
            combo,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        return result == JOptionPane.OK_OPTION ? items.get(combo.getSelectedIndex()) : null;
    }

    private void log(String message) {
        if (message == null || message.trim().isEmpty()) return;
        activityLog.setText(message);
        activityLog.setCaretPosition(0);
    }

    private void saveGame() {
        try {
            SaveManager.save(player);
        } catch (IOException error) {
            log(I18n.t("save.write_error", error.getMessage()));
        }
    }

    private Color rarityColor(Rarity rarity) {
        switch (rarity) {
            case COMMON: return new Color(84, 118, 95);
            case RARE: return new Color(48, 121, 194);
            case EPIC: return new Color(132, 78, 180);
            case MYTHICAL: return new Color(212, 78, 130);
            case LEGENDARY: return new Color(217, 143, 35);
            default: return UiTheme.PRIMARY;
        }
    }

    private String rarityDescription(Rarity rarity) {
        if (I18n.isEnglish()) {
            switch (rarity) {
                case COMMON: return "58 accessible base Pokémon.";
                case RARE: return "10 uncommon base Pokémon.";
                case EPIC: return "6 powerful base Pokémon.";
                case MYTHICAL: return "Contains Mew.";
                case LEGENDARY: return "Legendary birds or Mewtwo.";
                default: return "";
            }
        }
        switch (rarity) {
            case COMMON: return "58 Pokémon de base accessibles.";
            case RARE: return "10 Pokémon de base peu communs.";
            case EPIC: return "6 Pokémon de base puissants.";
            case MYTHICAL: return "Contient Mew.";
            case LEGENDARY: return "Oiseaux légendaires ou Mewtwo.";
            default: return "";
        }
    }
}
