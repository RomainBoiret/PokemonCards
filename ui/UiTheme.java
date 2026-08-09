package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/** Palette sombre, naturelle et accessible pour l'application desktop. */
public final class UiTheme {

    public static final Color BG = new Color(244, 247, 244);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(236, 242, 237);
    public static final Color SIDEBAR = new Color(21, 49, 38);
    public static final Color SIDEBAR_ACTIVE = new Color(44, 91, 68);
    public static final Color SIDEBAR_HOVER = new Color(34, 72, 54);
    public static final Color PRIMARY = new Color(39, 123, 82);
    public static final Color PRIMARY_DARK = new Color(27, 92, 60);
    public static final Color ACCENT = new Color(235, 135, 50);
    public static final Color ACCENT_SOFT = new Color(255, 238, 220);
    public static final Color TEXT = new Color(28, 43, 35);
    public static final Color MUTED = new Color(102, 118, 108);
    public static final Color BORDER = new Color(218, 227, 220);
    public static final Color LOG_BG = new Color(24, 38, 31);
    public static final Color LOG_FG = new Color(224, 237, 227);
    public static final Color SUCCESS = new Color(48, 148, 89);
    public static final Color HEALTH = new Color(221, 78, 73);
    public static final Color STAMINA = new Color(52, 145, 216);
    public static final Color HUNGER = new Color(238, 162, 54);
    public static final Color PROGRESS_TRACK = new Color(224, 230, 225);
    public static final Color DISABLED = new Color(185, 195, 188);

    // Legacy aliases kept for the console-era frame.
    public static final Color PANEL = SURFACE;
    public static final Color HEADER = SIDEBAR;
    public static final Color HEADER_TEXT = Color.WHITE;
    public static final Color ACCENT_DARK = PRIMARY_DARK;

    public static final Font DISPLAY = new Font("Segoe UI Semibold", Font.BOLD, 30);
    public static final Font TITLE = new Font("Segoe UI Semibold", Font.BOLD, 24);
    public static final Font HEADING = new Font("Segoe UI Semibold", Font.BOLD, 18);
    public static final Font SUBHEADING = new Font("Segoe UI Semibold", Font.BOLD, 15);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font("Segoe UI Semibold", Font.BOLD, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_BOLD = new Font("Segoe UI Semibold", Font.BOLD, 11);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 12);

    private UiTheme() {
    }

    public static Border pad(int n) {
        return BorderFactory.createEmptyBorder(n, n, n, n);
    }

    public static Border card() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            pad(12)
        );
    }
}
