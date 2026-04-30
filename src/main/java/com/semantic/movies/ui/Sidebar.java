package com.semantic.movies.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class Sidebar extends JPanel {

    private static final int WIDTH = 220;

    private final Runnable onAcclaimed;
    private final Runnable onMasterpieces;
    private final Runnable onAll;
    private final Consumer<String> onGenre;

    public Sidebar(Runnable onAll,
                   Runnable onAcclaimed,
                   Runnable onMasterpieces,
                   Consumer<String> onGenre) {
        this.onAll          = onAll;
        this.onAcclaimed    = onAcclaimed;
        this.onMasterpieces = onMasterpieces;
        this.onGenre        = onGenre;

        setBackground(Theme.SIDEBAR_BG);
        setPreferredSize(new Dimension(WIDTH, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(buildBrand());
        add(Box.createVerticalStrut(28));

        addSection("BROWSE");
        addNavItem("All Movies",   "🎬", onAll);
        addNavItem("Acclaimed",    "🏆", onAcclaimed);
        addNavItem("Masterpieces", "✨",       onMasterpieces);
        add(Box.createVerticalStrut(20));

        addSection("GENRES");
        addNavItem("Action",    "💥", () -> onGenre.accept("action"));
        addNavItem("Drama",     "🎭", () -> onGenre.accept("drama"));
        addNavItem("Sci-Fi",    "🚀", () -> onGenre.accept("sci-fi"));
        addNavItem("Thriller",  "🔪", () -> onGenre.accept("thriller"));
        addNavItem("Horror",    "👻", () -> onGenre.accept("horror"));
        addNavItem("Animation", "🎨", () -> onGenre.accept("animation"));
        addNavItem("Romance",   "💕", () -> onGenre.accept("romance"));
        addNavItem("Comedy",    "😂", () -> onGenre.accept("comedy"));

        add(Box.createVerticalGlue());
        add(buildFooter());
    }

    private JPanel buildBrand() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(24, 22, 8, 22));
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel icon = new JLabel("🎥");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JLabel title = new JLabel("CINEMA");
        title.setFont(Theme.FONT_BRAND);
        title.setForeground(Theme.PRIMARY);
        title.setBorder(new EmptyBorder(0, 10, 0, 0));

        panel.add(icon);
        panel.add(title);
        panel.add(Box.createHorizontalGlue());
        panel.setMaximumSize(new Dimension(WIDTH, 60));
        return panel;
    }

    private void addSection(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_NAV_SECTION);
        l.setForeground(Theme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(6, 22, 8, 22));
        l.setAlignmentX(LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(WIDTH, 22));
        add(l);
    }

    private void addNavItem(String label, String emoji, Runnable onClick) {
        NavItem item = new NavItem(label, emoji, onClick);
        item.setAlignmentX(LEFT_ALIGNMENT);
        add(item);
    }

    private JPanel buildFooter() {
        JLabel l = new JLabel("Semantic Web Project");
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(Theme.TEXT_MUTED);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                new EmptyBorder(14, 22, 14, 22)));
        footer.setMaximumSize(new Dimension(WIDTH, 44));
        footer.add(l, BorderLayout.WEST);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        return footer;
    }

    private static class NavItem extends JPanel {
        private final JLabel emoji;
        private final JLabel label;
        private boolean hovered = false;

        NavItem(String text, String emojiText, Runnable onClick) {
            setOpaque(false);
            setLayout(new BorderLayout(12, 0));
            setBorder(new EmptyBorder(9, 22, 9, 22));
            setMaximumSize(new Dimension(WIDTH, 38));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            emoji = new JLabel(emojiText);
            emoji.setFont(new Font("SansSerif", Font.PLAIN, 14));

            label = new JLabel(text);
            label.setFont(Theme.FONT_NAV);
            label.setForeground(Theme.TEXT_SECONDARY);

            add(emoji, BorderLayout.WEST);
            add(label, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; label.setForeground(Theme.PRIMARY); repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; label.setForeground(Theme.TEXT_SECONDARY); repaint(); }
                @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            if (hovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
}
