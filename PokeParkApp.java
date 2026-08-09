import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Player.Player;
import Pokemon.PokemonFactory;
import i18n.I18n;
import save.SaveManager;
import ui.ModernPokeParkFrame;

/**
 * Graphical entry point for PokePark.
 */
public class PokeParkApp {

    private static final String POKEMON_JSON = "Pokemon/PokemonList.json";

    public static void main(String[] args) {
        configureLookAndFeel();
        chooseLanguage();

        PokemonFactory.chargerPokemons(POKEMON_JSON);
        if (!PokemonFactory.estCharge()) {
            JOptionPane.showMessageDialog(
                null,
                I18n.t("app.load_error", POKEMON_JSON),
                I18n.t("app.title"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Player player = choosePlayer();
            if (player == null) {
                return;
            }

            ModernPokeParkFrame frame = new ModernPokeParkFrame(player);
            frame.setVisible(true);
        });
    }

    private static Player choosePlayer() {
        if (SaveManager.exists()) {
            try {
                Player savedPlayer = SaveManager.load();
                String[] options = {
                    I18n.t("save.resume"),
                    I18n.t("save.new_game")
                };
                int choice = JOptionPane.showOptionDialog(
                    null,
                    I18n.t("save.found", savedPlayer.getName(), savedPlayer.getDay()),
                    I18n.t("app.title"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                if (choice == JOptionPane.CLOSED_OPTION) {
                    return null;
                }
                if (choice == 0) {
                    return savedPlayer;
                }
            } catch (Exception error) {
                JOptionPane.showMessageDialog(
                    null,
                    I18n.t("save.load_error"),
                    I18n.t("app.title"),
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }

        String name = JOptionPane.showInputDialog(
            null,
            I18n.t("app.ask_name"),
            I18n.t("app.title"),
            JOptionPane.QUESTION_MESSAGE
        );
        if (name == null) {
            return null;
        }
        name = name.trim();
        return new Player(name.isEmpty() ? "Manager" : name);
    }

    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.arc", 14);
            UIManager.put("Component.arc", 14);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("ScrollBar.width", 11);
        } catch (Exception ignored) {
            // The application keeps working with Swing's default look and feel.
        }
    }

    private static void chooseLanguage() {
        String[] options = {
            I18n.t("app.lang_fr"),
            I18n.t("app.lang_en")
        };
        int choice = JOptionPane.showOptionDialog(
            null,
            I18n.t("app.ask_language"),
            "PokePark",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        if (choice == 1) {
            I18n.setLocale(Locale.US);
        } else {
            I18n.setLocale(Locale.FRENCH);
        }
    }
}
