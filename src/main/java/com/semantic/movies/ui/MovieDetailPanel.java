package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MovieDetailPanel extends JPanel {

    private static final int WIDTH = 300;

    private final JLabel badgeLabel  = new JLabel("", SwingConstants.CENTER);
    private final JLabel titleLabel  = new JLabel();
    private final JLabel metaLabel   = new JLabel();
    private final JLabel ratingLabel = new JLabel();
    private final JLabel durLabel    = new JLabel();
    private final JTextArea plotArea  = new JTextArea();

    public MovieDetailPanel() {
        setPreferredSize(new Dimension(WIDTH, 0));
        setBackground(Theme.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Theme.BORDER),
                new EmptyBorder(28, 22, 28, 22)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        styleComponents();
        addAll();
    }

    private void styleComponents() {
        badgeLabel.setOpaque(true);
        badgeLabel.setBackground(Theme.ACCENT_SOFT);
        badgeLabel.setForeground(Theme.PRIMARY);
        badgeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        badgeLabel.setPreferredSize(new Dimension(72, 72));
        badgeLabel.setMaximumSize(new Dimension(72, 72));
        badgeLabel.setAlignmentX(CENTER_ALIGNMENT);

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        metaLabel.setFont(Theme.FONT_SUBTITLE);
        metaLabel.setForeground(Theme.TEXT_SECONDARY);
        metaLabel.setAlignmentX(LEFT_ALIGNMENT);

        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(0xD97706));
        ratingLabel.setAlignmentX(LEFT_ALIGNMENT);

        durLabel.setFont(Theme.FONT_SUBTITLE);
        durLabel.setForeground(Theme.TEXT_MUTED);
        durLabel.setAlignmentX(LEFT_ALIGNMENT);

        plotArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        plotArea.setForeground(Theme.TEXT_SECONDARY);
        plotArea.setBackground(Theme.CARD);
        plotArea.setLineWrap(true);
        plotArea.setWrapStyleWord(true);
        plotArea.setEditable(false);
        plotArea.setBorder(null);
        plotArea.setOpaque(false);
        plotArea.setAlignmentX(LEFT_ALIGNMENT);
        plotArea.setMaximumSize(new Dimension(WIDTH - 44, Integer.MAX_VALUE));
    }

    private void addAll() {
        add(badgeLabel);
        add(Box.createVerticalStrut(18));
        add(titleLabel);
        add(Box.createVerticalStrut(6));
        add(metaLabel);
        add(Box.createVerticalStrut(10));
        add(ratingLabel);
        add(Box.createVerticalStrut(4));
        add(durLabel);
        add(Box.createVerticalStrut(14));
        add(new JSeparator());
        add(Box.createVerticalStrut(14));
        add(plotArea);
        add(Box.createVerticalGlue());
    }

    public void show(SearchResult r) {
        badgeLabel.setText(initials(r.getTitle()));
        titleLabel.setText(r.getTitle());
        metaLabel.setText(buildMeta(r));
        ratingLabel.setText(r.getRatingLabel());
        durLabel.setText(r.getDurationLabel());
        plotArea.setText(r.getPlot() != null ? r.getPlot() : "No plot available.");
        plotArea.setCaretPosition(0);
        revalidate();
        repaint();
    }

    private static String buildMeta(SearchResult r) {
        StringBuilder sb = new StringBuilder();
        if (r.getYear() != null)     sb.append(r.getYear());
        if (r.getDirector() != null) { if (sb.length() > 0) sb.append("  •  "); sb.append("Dir. ").append(r.getDirector()); }
        if (r.getGenre() != null)    { if (sb.length() > 0) sb.append("  •  "); sb.append(r.getGenre()); }
        return sb.toString();
    }

    private static String initials(String title) {
        if (title == null || title.isBlank()) return "?";
        StringBuilder sb = new StringBuilder();
        for (String part : title.trim().split("\\s+")) {
            if (!part.isEmpty() && Character.isLetterOrDigit(part.charAt(0))) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (sb.length() == 2) break;
            }
        }
        return sb.length() == 0 ? "?" : sb.toString();
    }
}
