package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ResultsPanel extends JPanel {

    private final JPanel listPanel = new JPanel();
    private final Consumer<SearchResult> onMovieClicked;

    public ResultsPanel(Consumer<SearchResult> onMovieClicked) {
        this.onMovieClicked = onMovieClicked;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE0E0E0)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        showPlaceholder("Search above to see results.");
    }

    public void showLoading()  { showPlaceholder("Searching..."); }
    public void showError(String message) { showPlaceholder("Error: " + message); }

    public void showResults(List<SearchResult> results) {
        listPanel.removeAll();
        if (results == null || results.isEmpty()) {
            showPlaceholder("No results found.");
            return;
        }
        for (SearchResult r : results) {
            MovieListItem item = new MovieListItem(r, onMovieClicked);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(item);
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void showPlaceholder(String text) {
        listPanel.removeAll();
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(new Color(0x888888));
        label.setBorder(new EmptyBorder(24, 24, 24, 24));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(label);
        listPanel.revalidate();
        listPanel.repaint();
    }
}
