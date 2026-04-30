package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class MovieListItem extends JPanel {

    private static final int PAD_H = 6;
    private static final int PAD_V = 4;

    private Color bg = Theme.CARD;
    private Color border = Theme.BORDER;

    public MovieListItem(SearchResult result, Consumer<SearchResult> onClick) {
        setOpaque(false);
        setLayout(new BorderLayout(16, 0));
        setBorder(new EmptyBorder(PAD_V + 14, PAD_H + 18, PAD_V + 14, PAD_H + 18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(buildBadge(result.getTitle()), BorderLayout.WEST);
        add(buildText(result),             BorderLayout.CENTER);
        add(buildMeta(result),             BorderLayout.EAST);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                bg = Theme.CARD_HOVER;
                border = Theme.PRIMARY;
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                bg = Theme.CARD;
                border = Theme.BORDER;
                repaint();
            }
            @Override public void mouseClicked(MouseEvent e) { onClick.accept(result); }
        });
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = PAD_H, y = PAD_V, w = getWidth() - 2 * PAD_H, h = getHeight() - 2 * PAD_V;
        g2.setColor(bg);
        g2.fillRoundRect(x, y, w, h, 10, 10);
        g2.setColor(border);
        g2.drawRoundRect(x, y, w - 1, h - 1, 10, 10);
        g2.dispose();
    }

    private static JLabel buildBadge(String title) {
        JLabel badge = new JLabel(initials(title), SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(Theme.ACCENT_SOFT);
        badge.setForeground(Theme.PRIMARY);
        badge.setFont(new Font("SansSerif", Font.BOLD, 16));
        badge.setPreferredSize(new Dimension(48, 48));
        badge.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY, 1, true));
        return badge;
    }

    private static JPanel buildText(SearchResult result) {
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(result.getTitle());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(title);

        String subtitleText = result.getSubtitle();
        if (!subtitleText.isBlank()) {
            JLabel subtitle = new JLabel(subtitleText);
            subtitle.setFont(Theme.FONT_SUBTITLE);
            subtitle.setForeground(Theme.TEXT_SECONDARY);
            subtitle.setBorder(new EmptyBorder(4, 0, 0, 0));
            subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            text.add(subtitle);
        }
        return text;
    }

    private static JPanel buildMeta(SearchResult result) {
        JPanel meta = new JPanel();
        meta.setOpaque(false);
        meta.setLayout(new BoxLayout(meta, BoxLayout.Y_AXIS));

        String rating = result.getRatingLabel();
        if (!rating.isBlank()) {
            JLabel ratingLabel = new JLabel(rating);
            ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            ratingLabel.setForeground(Theme.PRIMARY);
            ratingLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            meta.add(ratingLabel);
        }

        String dur = result.getDurationLabel();
        if (!dur.isBlank()) {
            JLabel durLabel = new JLabel(dur);
            durLabel.setFont(Theme.FONT_SUBTITLE);
            durLabel.setForeground(Theme.TEXT_MUTED);
            durLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
            durLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            meta.add(durLabel);
        }
        return meta;
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
