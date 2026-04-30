package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MovieDetailPanel extends JPanel {

    private final JLabel badgeLabel  = new JLabel("", SwingConstants.CENTER);
    private final JLabel titleLabel  = new JLabel();
    private final JLabel metaLabel   = new JLabel();
    private final JLabel ratingLabel = new JLabel();
    private final JLabel durLabel    = new JLabel();
    private final JTextArea plotArea = new JTextArea();

    private final Runnable onBack;

    public MovieDetailPanel(Runnable onBack) {
        this.onBack = onBack;

        setBackground(Theme.BG);
        setLayout(new BorderLayout());

        styleComponents();

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildScrollableBody(), BorderLayout.CENTER);
    }

    private void styleComponents() {
        badgeLabel.setOpaque(true);
        badgeLabel.setBackground(Theme.GOLD_SOFT);
        badgeLabel.setForeground(Theme.GOLD);
        badgeLabel.setFont(new Font("Serif", Font.BOLD, 36));
        badgeLabel.setPreferredSize(new Dimension(96, 96));
        badgeLabel.setMaximumSize(new Dimension(96, 96));
        badgeLabel.setMinimumSize(new Dimension(96, 96));

        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        titleLabel.setForeground(Theme.TEXT_PRIMARY);

        metaLabel.setFont(Theme.FONT_SUBTITLE);
        metaLabel.setForeground(Theme.TEXT_SECONDARY);

        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        ratingLabel.setForeground(Theme.GOLD);

        durLabel.setFont(Theme.FONT_SUBTITLE);
        durLabel.setForeground(Theme.TEXT_MUTED);

        plotArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        plotArea.setForeground(Theme.TEXT_SECONDARY);
        plotArea.setBackground(Theme.BG);
        plotArea.setLineWrap(true);
        plotArea.setWrapStyleWord(true);
        plotArea.setEditable(false);
        plotArea.setBorder(null);
        plotArea.setOpaque(false);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.HEADER_BG);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(16, 36, 16, 36)));

        JButton back = new JButton("←  Back");
        back.setFont(Theme.FONT_PILL);
        back.setForeground(Color.WHITE);
        back.setBackground(Theme.PRIMARY);
        back.setOpaque(true);
        back.setBorder(new EmptyBorder(8, 18, 8, 18));
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> onBack.run());

        bar.add(back, BorderLayout.WEST);
        return bar;
    }

    private JScrollPane buildScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Theme.BG);
        body.setBorder(new EmptyBorder(36, 48, 36, 48));

        body.add(buildHeaderRow());
        body.add(Box.createVerticalStrut(28));
        body.add(buildRatingRow());
        body.add(Box.createVerticalStrut(32));
        body.add(buildSectionLabel("PLOT"));
        body.add(Box.createVerticalStrut(12));
        plotArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(plotArea);
        body.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        return scroll;
    }

    private JPanel buildHeaderRow() {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(badgeLabel);
        row.add(Box.createHorizontalStrut(28));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        metaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(8));
        text.add(metaLabel);
        text.add(Box.createVerticalGlue());

        row.add(text);
        row.add(Box.createHorizontalGlue());
        return row;
    }

    private JPanel buildRatingRow() {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(ratingLabel);
        row.add(Box.createHorizontalStrut(20));
        row.add(durLabel);
        row.add(Box.createHorizontalGlue());
        return row;
    }

    private JLabel buildSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(Theme.GOLD);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
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
