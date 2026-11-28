package org.twozerofoureightgame.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.twozerofoureightgame.utility.Easing;

/**
 * Displays the game title, score, best score,
 * move count and current game status.
 * Rounded statistics cards improve visual organisation
 * and readability while preserving score animation.
 */
public final class HeaderPanel extends JPanel{

    public static final long SCORE_ANIMATION_NANOS=250000000L;

    private static final int TICK_INTERVAL_MILLISECONDS=16;

    private static final Color CARD_CAPTION_COLOR=new Color(0xCBD5E1);
    private static final Color SUBTITLE_COLOR=new Color(0x475569);
    private static final Font SUBTITLE_FONT=
        new Font("SansSerif",Font.PLAIN,13);

    private final JLabel scoreValueLabel=new JLabel("0");
    private final JLabel bestValueLabel=new JLabel("0");
    private final JLabel movesValueLabel=new JLabel("0");
    private final JLabel statusValueLabel=new JLabel("Ready");

    private final JLabel titleLabel=
        new JLabel("2048 — Expectimax");

    private int displayedScore=0;
    private int startingScore=0;
    private int targetScore=0;
    private long animationStartNanos=0L;

    private final javax.swing.Timer scoreTimer;

    /**
     * Creates the header and statistics cards.
     */
    public HeaderPanel(){

        setOpaque(true);
        setBackground(Theme.WINDOW_BACKGROUND);
        setLayout(new BorderLayout(0,10));
        setBorder(BorderFactory.createEmptyBorder(10,18,8,18));

        // Game title
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.HEADING_COLOR);

        // Game subtitle
        JLabel subtitleLabel=
            new JLabel("AI-Powered Puzzle Game");

        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(SUBTITLE_COLOR);

        JPanel titlePanel=new JPanel(new BorderLayout(0,2));
        titlePanel.setOpaque(false);

        titlePanel.add(titleLabel,BorderLayout.NORTH);
        titlePanel.add(subtitleLabel,BorderLayout.CENTER);

        add(titlePanel,BorderLayout.NORTH);

        // Statistics cards
        JPanel readoutPanel=new JPanel(new GridLayout(1,4,10,0));
        readoutPanel.setOpaque(false);

        readoutPanel.add(createReadout("Score",scoreValueLabel));
        readoutPanel.add(createReadout("Best",bestValueLabel));
        readoutPanel.add(createReadout("Moves",movesValueLabel));
        readoutPanel.add(createReadout("Status",statusValueLabel));

        add(readoutPanel,BorderLayout.CENTER);

        // Preserve the original score animation
        scoreTimer=new javax.swing.Timer(
            TICK_INTERVAL_MILLISECONDS,
            new java.awt.event.ActionListener(){
                public void actionPerformed(
                    java.awt.event.ActionEvent event
                ){
                    tickScoreAnimation();
                }
            }
        );

        scoreTimer.setRepeats(true);
    }

    /**
     * Creates an individual rounded statistics card.
     */
    private JPanel createReadout(String caption,JLabel valueLabel){

        JPanel readout=new JPanel(new BorderLayout(0,4)){

            @Override
            protected void paintComponent(Graphics graphics){

                super.paintComponent(graphics);

                Graphics2D graphics2D=
                    (Graphics2D)graphics.create();

                graphics2D.setRenderingHints(
                    Theme.ANTIALIASED_RENDERING_HINTS
                );

                graphics2D.setColor(Theme.ACCENT_COLOR);

                graphics2D.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    16,
                    16
                );

                graphics2D.dispose();
            }
        };

        readout.setOpaque(false);

        readout.setBorder(
            BorderFactory.createEmptyBorder(8,8,8,8)
        );

        // Card caption
        JLabel captionLabel=
            new JLabel(caption.toUpperCase(Locale.ROOT));

        captionLabel.setFont(Theme.CAPTION_FONT);
        captionLabel.setForeground(CARD_CAPTION_COLOR);
        captionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Card value
        valueLabel.setFont(Theme.VALUE_FONT);
        valueLabel.setForeground(Theme.BUTTON_TEXT_COLOR);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        readout.add(captionLabel,BorderLayout.NORTH);
        readout.add(valueLabel,BorderLayout.CENTER);

        return readout;
    }

    /**
     * Updates score, best score and move count.
     * The current score retains its original animation.
     */
    public void updateScore(int score,int bestScore,int moveCount){

        bestValueLabel.setText(Integer.toString(bestScore));
        movesValueLabel.setText(Integer.toString(moveCount));

        if(score!=targetScore){

            startingScore=displayedScore;
            targetScore=score;
            animationStartNanos=System.nanoTime();

            if(!scoreTimer.isRunning()){
                scoreTimer.start();
            }
        }
    }

    /**
     * Updates the current game status.
     */
    public void setStatus(String status){
        statusValueLabel.setText(status);
    }

    /**
     * Preserves the original animated score transition.
     */
    private void tickScoreAnimation(){

        long elapsed=System.nanoTime()-animationStartNanos;

        if(elapsed>=SCORE_ANIMATION_NANOS){

            displayedScore=targetScore;

            scoreValueLabel.setText(
                Integer.toString(displayedScore)
            );

            scoreTimer.stop();
            return;
        }

        float progress=Easing.easeOutCubic(
            (float)((double)elapsed/(double)SCORE_ANIMATION_NANOS)
        );

        displayedScore=startingScore+
            (int)((targetScore-startingScore)*progress);

        scoreValueLabel.setText(
            Integer.toString(displayedScore)
        );
    }
}