package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class MovieDetailPanel extends JPanel {

    private final JLabel badgeLabel  = new JLabel("", SwingConstants.CENTER);
    private final JLabel titleLabel  = new JLabel();
    private final JLabel metaLabel   = new JLabel();
    private final JLabel ratingLabel = new JLabel();
    private final JLabel durLabel    = new JLabel();
    private final JTextArea plotArea = new JTextArea();
    private final JLabel similarHeader = new JLabel("Similar Movies");
    private final JPanel similarList = new JPanel();
    private final JLabel similarEmpty = new JLabel("No similar movies found.", SwingConstants.LEFT);

    private final Runnable onBack;
    private final Consumer<SearchResult> onSimilarClicked;

    public MovieDetailPanel(Runnable onBack, Consumer<SearchResult> onSimilarClicked) {
        this.onBack = onBack;
        this.onSimilarClicked = onSimilarClicked;

        setBackground(Theme.BG);
        setLayout(new BorderLayout());

        styleComponents();

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildScrollableBody(), BorderLayout.CENTER);
    }

    private void styleComponents() {
        badgeLabel.setOpaque(true);
        badgeLabel.setBackground(Theme.ACCENT_SOFT);
        badgeLabel.setForeground(Theme.PRIMARY);
        badgeLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        badgeLabel.setPreferredSize(new Dimension(88, 88));
        badgeLabel.setMaximumSize(new Dimension(88, 88));
        badgeLabel.setMinimumSize(new Dimension(88, 88));

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.TEXT_PRIMARY);

        metaLabel.setFont(Theme.FONT_SUBTITLE);
        metaLabel.setForeground(Theme.TEXT_SECONDARY);

        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        ratingLabel.setForeground(new Color(0xD97706));

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

        similarHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        similarHeader.setForeground(Theme.TEXT_PRIMARY);

        similarEmpty.setFont(Theme.FONT_SUBTITLE);
        similarEmpty.setForeground(Theme.TEXT_MUTED);

        similarList.setLayout(new BoxLayout(similarList, BoxLayout.Y_AXIS));
        similarList.setOpaque(false);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(12, 18, 12, 18)));

        JButton back = new JButton("←  Back");
        back.setFont(new Font("SansSerif", Font.BOLD, 13));
        back.setForeground(Theme.PRIMARY);
        back.setBackground(Theme.CARD);
        back.setOpaque(true);
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(6, 14, 6, 14)));
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
        body.setBorder(new EmptyBorder(28, 36, 28, 36));

        body.add(buildHeaderRow());
        body.add(Box.createVerticalStrut(20));
        body.add(buildRatingRow());
        body.add(Box.createVerticalStrut(22));
        body.add(buildSectionLabel("Plot"));
        body.add(Box.createVerticalStrut(8));
        plotArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(plotArea);
        body.add(Box.createVerticalStrut(28));
        similarHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(similarHeader);
        body.add(Box.createVerticalStrut(10));
        similarList.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(similarList);
        similarEmpty.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(similarEmpty);
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
        row.add(Box.createHorizontalStrut(20));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        metaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(6));
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
        row.add(Box.createHorizontalStrut(18));
        row.add(durLabel);
        row.add(Box.createHorizontalGlue());
        return row;
    }

    private JLabel buildSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(Theme.TEXT_PRIMARY);
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

        similarList.removeAll();
        similarEmpty.setVisible(false);
        revalidate();
        repaint();
    }

    public void setSimilar(List<SearchResult> similar) {
        similarList.removeAll();
        if (similar == null || similar.isEmpty()) {
            similarEmpty.setVisible(true);
        } else {
            similarEmpty.setVisible(false);
            for (SearchResult r : similar) {
                similarList.add(new MovieListItem(r, onSimilarClicked));
            }
        }
        similarList.revalidate();
        similarList.repaint();
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
