package com.semantic.movies.ui;

import java.awt.Color;
import java.awt.Font;

public final class Theme {

    public static final Color PRIMARY       = new Color(0x4F46E5);
    public static final Color PRIMARY_HOVER = new Color(0x4338CA);
    public static final Color ACCENT_SOFT   = new Color(0xEEF2FF);

    public static final Color BG            = new Color(0xFAFAFB);
    public static final Color CARD          = Color.WHITE;
    public static final Color CARD_HOVER    = new Color(0xF5F7FF);

    public static final Color TEXT_PRIMARY   = new Color(0x1F2937);
    public static final Color TEXT_SECONDARY = new Color(0x6B7280);
    public static final Color TEXT_MUTED     = new Color(0x9CA3AF);

    public static final Color BORDER        = new Color(0xE5E7EB);
    public static final Color BORDER_FOCUS  = new Color(0x4F46E5);

    public static final Font FONT_H1       = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_TAGLINE  = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD, 17);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_INPUT    = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font FONT_BUTTON   = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_STATUS   = new Font("SansSerif", Font.PLAIN, 12);

    public static final int INPUT_HEIGHT = 44;

    private Theme() { }
}
