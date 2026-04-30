package com.semantic.movies.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class HeaderBar extends JPanel {

    private final Runnable onAll;
    private final Runnable onAcclaimed;
    private final Runnable onMasterpieces;
    private final Consumer<String> onGenre;

    public HeaderBar(Runnable onAll,
                     Runnable onAcclaimed,
                     Runnable onMasterpieces,
                     Consumer<String> onGenre) {
        this.onAll          = onAll;
        this.onAcclaimed    = onAcclaimed;
        this.onMasterpieces = onMasterpieces;
        this.onGenre        = onGenre;

        setBackground(Theme.HEADER_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));

        add(buildBrand(),   BorderLayout.NORTH);
        add(buildFilters(), BorderLayout.CENTER);
    }

    private JPanel buildBrand() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 36, 8, 36));

        JLabel brand = new JLabel("Cinema");
        brand.setFont(Theme.FONT_BRAND);
        brand.setForeground(Theme.TEXT_PRIMARY);

        JLabel tagline = new JLabel("Find your next great film.");
        tagline.setFont(Theme.FONT_HERO);
        tagline.setForeground(Theme.TEXT_SECONDARY);
        tagline.setBorder(new EmptyBorder(2, 0, 0, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        brand.setAlignmentX(LEFT_ALIGNMENT);
        tagline.setAlignmentX(LEFT_ALIGNMENT);
        left.add(brand);
        left.add(tagline);

        panel.add(left, BorderLayout.WEST);
        return panel;
    }

    private JPanel buildFilters() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 30, 18, 30));

        row.add(new Pill("All",          true,  onAll));
        row.add(new Pill("Acclaimed",    true,  onAcclaimed));
        row.add(new Pill("Masterpieces", true,  onMasterpieces));
        row.add(separator());
        row.add(new Pill("Action",    false, () -> onGenre.accept("action")));
        row.add(new Pill("Drama",     false, () -> onGenre.accept("drama")));
        row.add(new Pill("Sci-Fi",    false, () -> onGenre.accept("sci-fi")));
        row.add(new Pill("Thriller",  false, () -> onGenre.accept("thriller")));
        row.add(new Pill("Horror",    false, () -> onGenre.accept("horror")));
        row.add(new Pill("Animation", false, () -> onGenre.accept("animation")));
        row.add(new Pill("Romance",   false, () -> onGenre.accept("romance")));
        row.add(new Pill("Comedy",    false, () -> onGenre.accept("comedy")));

        return row;
    }

    private JPanel separator() {
        JPanel sep = new JPanel();
        sep.setBackground(Theme.BORDER);
        sep.setPreferredSize(new Dimension(1, 22));
        return sep;
    }

    /** Pill chip used as a filter button. */
    private static class Pill extends JLabel {
        private final boolean primary;
        private boolean hovered = false;

        Pill(String text, boolean primary, Runnable onClick) {
            super(text);
            this.primary = primary;
            setFont(Theme.FONT_PILL);
            setHorizontalAlignment(CENTER);
            setForeground(primary ? Theme.TEXT_PRIMARY : Theme.TEXT_SECONDARY);
            setBorder(new EmptyBorder(7, 16, 7, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (primary) {
                Color fill = hovered ? Theme.TEXT_PRIMARY : Theme.PRIMARY;
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                setForeground(Color.WHITE);
            } else {
                Color fill = hovered ? Theme.PRIMARY_SOFT : Theme.BG;
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                setForeground(hovered ? Theme.PRIMARY : Theme.TEXT_SECONDARY);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
