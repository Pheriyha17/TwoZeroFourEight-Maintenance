package org.twozerofoureightgame.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;

/**
 * Every colour, font, radius, and rendering setting used by the interface, all
 * created once when the class is loaded. Painting code looks values up here
 * instead of building new objects, which is what keeps a repaint free of
 * allocation.
 *
 * The interface uses a modern blue-grey colour scheme with a light grey
 * background, slate blue board, navy blue buttons and improved text contrast.
 * The original tile colours are retained to preserve visual recognition
 * of different tile values.
 */
public final class Theme{

    /** The colour behind everything, a cool light grey. */
    public static final Color WINDOW_BACKGROUND=new Color(0xF1F5F9);

    /** The colour of the board itself. */
    public static final Color BOARD_BACKGROUND=new Color(0x64748B);

    /** The colour of the sixteen empty cells. */
    public static final Color EMPTY_CELL_COLOR=new Color(0xCBD5E1);

    /** The colour used for buttons. */
    public static final Color ACCENT_COLOR=new Color(0x1E3A5F);

    /** The colour of the button captions. */
    public static final Color BUTTON_TEXT_COLOR=new Color(0xFFFFFF);

    /** The colour of text drawn on a pale tile. */
    public static final Color DARK_TEXT_COLOR=new Color(0x334155);

    /** The colour of text drawn on a dark tile. */
    public static final Color LIGHT_TEXT_COLOR=new Color(0xF9F6F2);

    /** The colour of the heading and the header numbers. */
    public static final Color HEADING_COLOR=new Color(0x1E293B);

    /** The colour of the dimmed wash behind an overlay message. */
    public static final Color OVERLAY_COLOR=new Color(0x0F172A);

        /** The corner radius of every rounded rectangle, in pixels. */
    public static final int ARC_RADIUS=12;

    /** Anti aliased shapes, text, and strokes, reused on every repaint. */
    public static final RenderingHints ANTIALIASED_RENDERING_HINTS=buildRenderingHints();

    /** The font of the heading drawn inside the header. */
    public static final Font TITLE_FONT=new Font("SansSerif",Font.BOLD,30);

    /** The font of the small captions above the header numbers. */
    public static final Font CAPTION_FONT=new Font("SansSerif",Font.BOLD,14);

    /** The font of the header numbers and the status text. */
    public static final Font VALUE_FONT=new Font("SansSerif",Font.BOLD,22);

    /** The font of a tile showing one digit, such as the tile worth 8. */
    public static final Font TILE_FONT_ONE_DIGIT=new Font("SansSerif",Font.BOLD,42);

    /** The font of a tile showing two digits. */
    public static final Font TILE_FONT_TWO_DIGITS=new Font("SansSerif",Font.BOLD,36);

    /** The font of a tile showing three digits. */
    public static final Font TILE_FONT_THREE_DIGITS=new Font("SansSerif",Font.BOLD,30);

    /** The font of a tile showing four or more digits. */
    public static final Font TILE_FONT_FOUR_DIGITS=new Font("SansSerif",Font.BOLD,24);

    /** The font of the button captions. */
    public static final Font BUTTON_FONT=new Font("SansSerif",Font.BOLD,14);

    /** The font of the large overlay message. */
    public static final Font OVERLAY_FONT=new Font("SansSerif",Font.BOLD,40);


    private static final Color[] TILE_PALETTE=new Color[16];
    private static final String[] TILE_TEXTS=new String[16];
    private static final AlphaComposite[] ALPHA_COMPOSITES=new AlphaComposite[101];

       private static final int[][] TILE_COLOUR_SOURCE={
        {0xE0,0xF2,0xFE}, // 2
        {0xBA,0xE6,0xFD}, // 4
        {0x7D,0xD3,0xFC}, // 8
        {0x38,0xBD,0xF8}, // 16
        {0x02,0x84,0xC7}, // 32
        {0x03,0x69,0xA1}, // 64
        {0x1D,0x4E,0xD8}, // 128
        {0x43,0x38,0xCA}, // 256
        {0x6D,0x28,0xD9}, // 512
        {0x7E,0x22,0xCE}, // 1024
        {0xFB,0xBF,0x24}, // 2048
        {0x0F,0x17,0x2A}, // 4096
        {0x1E,0x29,0x3B}, // 8192
        {0x31,0x2E,0x81}, // 16384
        {0x4C,0x1D,0x95}, // 32768
        {0x02,0x06,0x17}  // Reserved
    };

    static{
        for(int exponent=0;exponent<16;exponent++){
            int sourceIndex=exponent<=1?0:exponent-1;
            TILE_PALETTE[exponent]=new Color(
                TILE_COLOUR_SOURCE[sourceIndex][0],
                TILE_COLOUR_SOURCE[sourceIndex][1],
                TILE_COLOUR_SOURCE[sourceIndex][2]
            );
            TILE_TEXTS[exponent]=Integer.toString(1<<exponent);
        }

        for(int step=0;step<=100;step++){
            ALPHA_COMPOSITES[step]=AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,step/100f
            );
        }
    }

    /**
     * This class only exposes static helpers and is never instantiated.
     */
    private Theme(){
    }

    private static RenderingHints buildRenderingHints(){
        RenderingHints hints=new RenderingHints((RenderingHints.Key)null,null);

        hints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        hints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
        hints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        return hints;
    }

    /**
     * Returns the fill colour of a tile.
     * @param exponent the tile exponent
     * @return the tile colour
     */
    public static Color tileColor(byte exponent){
        return TILE_PALETTE[exponent&0xF];
    }

    /**
     * Returns the text drawn on a tile.
     * @param exponent the tile exponent
     * @return the tile value as text, for example "2048"
     */
    public static String tileText(byte exponent){
        return TILE_TEXTS[exponent&0xF];
    }

    /**
     * Returns a readable text colour based on the tile background.
     * Light blue and gold tiles use dark text,
     * while darker blue and purple tiles use light text.
     * @param exponent the tile exponent
     * @return the contrasting text colour
     */
    public static Color tileTextColor(byte exponent){
        int value=exponent&0xF;

        if(value<=5 || value==11){
            return Theme.HEADING_COLOR;
        }

        return LIGHT_TEXT_COLOR;
    }

    /**
     * Returns the font that fits a tile value of the given size.
     * @param exponent the tile exponent
     * @return the largest font that still fits inside the cell
     */
    public static Font tileFont(byte exponent){

        int digitCount=Theme.tileText(exponent).length();

        if(digitCount<=1){
            return TILE_FONT_ONE_DIGIT;
        }

        if(digitCount==2){
            return TILE_FONT_TWO_DIGITS;
        }

        if(digitCount==3){
            return TILE_FONT_THREE_DIGITS;
        }

        return TILE_FONT_FOUR_DIGITS;
    }

    /**
     * Returns a translucent version of the source rule from the prebuilt table,
     * so that painting never creates a composite rule of its own.
     * @param alpha the transparency from zero for invisible to one for solid
     * @return the matching composite rule
     */
    public static AlphaComposite alphaComposite(float alpha){

        int step=(int)(alpha*100f+0.5f);

        if(step<0){
            step=0;
        }

        if(step>100){
            step=100;
        }

        return ALPHA_COMPOSITES[step];
    }
}