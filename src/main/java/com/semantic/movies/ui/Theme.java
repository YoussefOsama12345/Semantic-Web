package com.semantic.movies.ui;

import java.awt.Color;
import java.awt.Font;

public final class Theme {

    // Modern premium light theme - v2

    // Backgrounds
    public static final Color BG          = new Color(0xFB, 0xF9, 0xF4);  // warm cream
    public static final Color HEADER_BG   = new Color(0xFF, 0xFF, 0xFF);  // pure white header
    public static final Color CARD        = new Color(0xFF, 0xFF, 0xFF);  // pure white cards
    public static final Color CARD_HOVER  = new Color(0xF7, 0xF3, 0xEC);  // warm cream hover
    public static final Color BORDER      = new Color(0xEF, 0xEA, 0xE2);  // warm soft border
    public static final Color BORDER_SOFT = new Color(0xF5, 0xF1, 0xEA);  // even softer

    // Accents
    public static final Color PRIMARY      = new Color(0x1B, 0x2A, 0x3A);  // refined navy
    public static final Color PRIMARY_SOFT = new Color(0xE8, 0xEC, 0xF1);  // pale navy tint
    public static final Color GOLD         = new Color(0xB8, 0x84, 0x3A);  // sophisticated tan/gold
    public static final Color GOLD_SOFT    = new Color(0xF5, 0xED, 0xDB);  // gold tint backgrounds

    // Text
    public static final Color TEXT_PRIMARY   = new Color(0x0F, 0x14, 0x19);  // rich near-black
    public static final Color TEXT_SECONDARY = new Color(0x5C, 0x6B, 0x7C);  // warm gray
    public static final Color TEXT_MUTED     = new Color(0x9A, 0xA5, 0xB1);  // soft gray

    // Backwards-compat aliases used by existing code
    public static final Color ACCENT_SOFT = GOLD_SOFT;

    // Fonts — modern premium typography
    public static final Font FONT_BRAND    = new Font("Serif",     Font.BOLD,  22);
    public static final Font FONT_HERO     = new Font("Serif",     Font.PLAIN, 14);
    public static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD,  17);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_INPUT    = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font FONT_STATUS   = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_PILL     = new Font("SansSerif", Font.BOLD,  12);

    public static final int INPUT_HEIGHT = 46;

    private Theme() { }
}
