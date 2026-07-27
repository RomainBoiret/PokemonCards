package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/** Palette nature / parc — pas de thème violet générique. */
public final class UiTheme {

    public static final Color BG = new Color(232, 242, 230);
    public static final Color PANEL = new Color(250, 252, 248);
    public static final Color HEADER = new Color(34, 89, 58);
    public static final Color HEADER_TEXT = new Color(245, 250, 242);
    public static final Color ACCENT = new Color(214, 122, 42);
    public static final Color ACCENT_DARK = new Color(168, 86, 20);
    public static final Color TEXT = new Color(28, 42, 34);
    public static final Color MUTED = new Color(90, 110, 95);
    public static final Color LOG_BG = new Color(28, 42, 34);
    public static final Color LOG_FG = new Color(210, 230, 205);

    public static final Font TITLE = new Font("Segoe UI Semibold", Font.BOLD, 26);
    public static final Font HEADING = new Font("Segoe UI Semibold", Font.BOLD, 16);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 12);

    private UiTheme() {
    }

    public static Border pad(int n) {
        return BorderFactory.createEmptyBorder(n, n, n, n);
    }

    public static Border card() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(190, 210, 190)),
            pad(10)
        );
    }
}
