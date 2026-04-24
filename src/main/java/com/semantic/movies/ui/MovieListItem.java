package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class MovieListItem extends JPanel {

    private static final Color HOVER_BG  = new Color(0xE3F2FD);
    private static final Color NORMAL_BG = Color.WHITE;
    private static final Color DIVIDER   = new Color(0xEEEEEE);

    private final JLabel titleLabel;

    public MovieListItem(SearchResult result, Consumer<SearchResult> onClick) {
        setLayout(new BorderLayout());
        setBackground(NORMAL_BG);
        setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER),
                new EmptyBorder(12, 20, 12, 20)));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        titleLabel = new JLabel(result.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 19));
        titleLabel.setForeground(new Color(0x202124));
        add(titleLabel, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { setBackground(HOVER_BG); }
            @Override public void mouseExited(MouseEvent e)  { setBackground(NORMAL_BG); }
            @Override public void mouseClicked(MouseEvent e) { onClick.accept(result); }
        });
    }
}
