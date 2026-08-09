package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

final class UiComponents {

    private UiComponents() {
    }

    static final class RoundedPanel extends JPanel {
        private final int radius;
        private Color fill;
        private Color outline;

        RoundedPanel(int radius, Color fill) {
            this(radius, fill, null);
        }

        RoundedPanel(int radius, Color fill, Color outline) {
            this.radius = radius;
            this.fill = fill;
            this.outline = outline;
            setOpaque(false);
        }

        void setFill(Color fill) {
            this.fill = fill;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(fill);
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            if (outline != null) {
                g.setColor(outline);
                g.setStroke(new BasicStroke(1f));
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    static class ModernButton extends JButton {
        private final Color base;
        private final Color hover;
        private final Color pressed;
        private final int radius;
        private boolean hovered;

        ModernButton(String text, Color base, Color foreground, int radius) {
            super(text);
            this.base = base;
            this.hover = brighten(base, 12);
            this.pressed = darken(base, 12);
            this.radius = radius;
            setForeground(foreground);
            setFont(UiTheme.BODY_BOLD);
            setBorder(new EmptyBorder(10, 16, 10, 16));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color color = getModel().isPressed() ? pressed : (hovered ? hover : base);
            g.setColor(isEnabled() ? color : UiTheme.DISABLED);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    static final class SidebarButton extends JButton {
        private final String marker;
        private boolean active;
        private boolean hovered;

        SidebarButton(String marker, String text) {
            super(text);
            this.marker = marker;
            setFont(UiTheme.BODY_BOLD);
            setForeground(new Color(208, 226, 215));
            setHorizontalAlignment(LEFT);
            setBorder(new EmptyBorder(0, 52, 0, 14));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(188, 46));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        void setActive(boolean active) {
            this.active = active;
            setForeground(active ? Color.WHITE : new Color(208, 226, 215));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (active || hovered) {
                g.setColor(active ? UiTheme.SIDEBAR_ACTIVE : UiTheme.SIDEBAR_HOVER);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }

            int markerSize = 28;
            int markerY = (getHeight() - markerSize) / 2;
            g.setColor(active ? UiTheme.ACCENT : new Color(61, 103, 80));
            g.fillOval(12, markerY, markerSize, markerSize);
            g.setColor(active ? Color.WHITE : new Color(188, 215, 197));
            g.setFont(UiTheme.SMALL_BOLD);
            FontMetrics metrics = g.getFontMetrics();
            int markerX = 12 + (markerSize - metrics.stringWidth(marker)) / 2;
            int textY = markerY + (markerSize - metrics.getHeight()) / 2 + metrics.getAscent();
            g.drawString(marker, markerX, textY);
            g.dispose();

            super.paintComponent(graphics);
        }
    }

    static final class ProgressBar extends JComponent {
        private int value;
        private final int max;
        private final Color color;

        ProgressBar(int max, Color color) {
            this.max = max;
            this.color = color;
            setPreferredSize(new Dimension(160, 9));
            setMinimumSize(new Dimension(80, 9));
        }

        void setValue(int value) {
            this.value = Math.max(0, Math.min(max, value));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int h = Math.max(6, getHeight());
            int y = (getHeight() - h) / 2;
            g.setColor(UiTheme.PROGRESS_TRACK);
            g.fillRoundRect(0, y, getWidth(), h, h, h);
            int width = (int) Math.round(getWidth() * (value / (double) max));
            if (width > 0) {
                g.setColor(color);
                g.fillRoundRect(0, y, width, h, h, h);
            }
            g.dispose();
        }
    }

    static final class Badge extends JComponent {
        private final String text;
        private final Color background;
        private final Color foreground;

        Badge(String text, Color background, Color foreground) {
            this.text = text;
            this.background = background;
            this.foreground = foreground;
            setFont(UiTheme.SMALL_BOLD);
            FontMetrics metrics = getFontMetrics(getFont());
            setPreferredSize(new Dimension(metrics.stringWidth(text) + 20, 26));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(background);
            g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
            g.setFont(getFont());
            g.setColor(foreground);
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(text, x, y);
            g.dispose();
        }
    }

    private static Color brighten(Color color, int amount) {
        return new Color(
            Math.min(255, color.getRed() + amount),
            Math.min(255, color.getGreen() + amount),
            Math.min(255, color.getBlue() + amount)
        );
    }

    private static Color darken(Color color, int amount) {
        return new Color(
            Math.max(0, color.getRed() - amount),
            Math.max(0, color.getGreen() - amount),
            Math.max(0, color.getBlue() - amount)
        );
    }
}
