package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.function.Consumer;

public class ResultsPanel extends JPanel {

    private static final String CARD_LIST        = "list";
    private static final String CARD_PLACEHOLDER = "placeholder";

    private final JPanel list = new JPanel();
    private final JLabel placeholderIcon  = new JLabel("", SwingConstants.CENTER);
    private final JLabel placeholderLabel = new JLabel("", SwingConstants.CENTER);
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);
    private final Consumer<SearchResult> onMovieClicked;

    public ResultsPanel(Consumer<SearchResult> onMovieClicked) {
        this.onMovieClicked = onMovieClicked;

        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        cardHost.setBackground(Theme.BG);
        cardHost.add(buildListCard(),        CARD_LIST);
        cardHost.add(buildPlaceholderCard(), CARD_PLACEHOLDER);
        add(cardHost, BorderLayout.CENTER);

        reset();
    }

    public void reset()                 { showPlaceholder("\uD83C\uDFAC", "Pick a filter above or search for any film."); }
    public void showLoading()           { showPlaceholder("\u29D7", "Searching..."); }
    public void showError(String msg)   { showPlaceholder("\u26A0", "Error: " + msg); }

    public void showResults(List<SearchResult> results) {
        if (results == null || results.isEmpty()) {
            showPlaceholder("\uD83D\uDD0D", "No results found. Try a different search.");
            return;
        }
        list.removeAll();
        for (SearchResult r : results) list.add(new MovieListItem(r, onMovieClicked));
        list.add(Box.createVerticalGlue());
        list.revalidate();
        list.repaint();
        cards.show(cardHost, CARD_LIST);
    }

    private JScrollPane buildListCard() {
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Theme.BG);
        list.setBorder(new EmptyBorder(6, 10, 12, 10));

        JScrollPane scroll = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    private JPanel buildPlaceholderCard() {
        placeholderIcon.setFont(new Font("SansSerif", Font.PLAIN, 42));
        placeholderIcon.setForeground(Theme.TEXT_MUTED);

        placeholderLabel.setFont(Theme.FONT_SUBTITLE);
        placeholderLabel.setForeground(Theme.TEXT_SECONDARY);
        placeholderLabel.setBorder(new EmptyBorder(14, 24, 0, 24));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        placeholderIcon.setAlignmentX(CENTER_ALIGNMENT);
        placeholderLabel.setAlignmentX(CENTER_ALIGNMENT);
        stack.add(placeholderIcon);
        stack.add(placeholderLabel);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Theme.BG);
        center.add(stack, new GridBagConstraints());
        return center;
    }

    private void showPlaceholder(String emoji, String text) {
        placeholderIcon.setText(emoji);
        placeholderLabel.setText(text);
        cards.show(cardHost, CARD_PLACEHOLDER);
    }
}
