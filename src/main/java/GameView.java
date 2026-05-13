import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameView extends JFrame
{
    // WINDOW SETTINGS
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    // FIREBALL IMAGES
    private final Image redBallImage;
    private final Image blueBallImage;
    // GAME REFERENCE
    private Game backend;
    // BACKGROUND
    private final Image bgImage;
    // PLAYER SPRITES
    private final Image ryuIdleImage;
    private final Image ryuPunchImage;
    private final Image ryuKickImage;
    private final Image ryuDodgeImage;

    private final Image kenIdleImage;
    private final Image kenPunchImage;
    private final Image kenKickImage;
    private final Image kenDodgeImage;
    // HEALTH BAR ICONS
    private final Image ryuIcon;
    private final Image kenIcon;
    // UI IMAGES
    private final Image controlsImage;
    private final Image powerUpImage;
    private final Image logoImage;
    // HEALTH BARS
    private HealthBar p1HealthBar;
    private HealthBar p2HealthBar;

    public GameView(Game backend)
    {
        this.backend = backend;

        ryuIdleImage =
                new ImageIcon("src/main/resources/ryu.png").getImage();

        ryuPunchImage =
                new ImageIcon("src/main/resources/ryu_punch.png").getImage();

        ryuKickImage =
                new ImageIcon("src/main/resources/ryu_kick.png").getImage();

        ryuDodgeImage =
                new ImageIcon("src/main/resources/ryu_dodge.png").getImage();

        kenIdleImage =
                new ImageIcon("src/main/resources/ken.png").getImage();

        kenPunchImage =
                new ImageIcon("src/main/resources/Ken_punch.png").getImage();

        kenKickImage =
                new ImageIcon("src/main/resources/ken_kick.png").getImage();

        kenDodgeImage =
                new ImageIcon("src/main/resources/ken_dodge.png").getImage();

        ryuIcon =
                new ImageIcon("src/main/resources/ryu_icon.png").getImage();

        kenIcon =
                new ImageIcon("src/main/resources/ken_icon.png").getImage();

        bgImage =
                new ImageIcon("src/main/resources/bg.jpg").getImage();

        controlsImage =
                new ImageIcon("src/main/resources/Controls.png").getImage();

        powerUpImage =
                new ImageIcon("src/main/resources/powerUp.png").getImage();

        logoImage =
                new ImageIcon("src/main/resources/Illustration.png").getImage();

        redBallImage =
                new ImageIcon("src/main/resources/red_ball.png").getImage();

        blueBallImage =
                new ImageIcon("src/main/resources/blue_ball.png").getImage();

        p1HealthBar = new HealthBar(100);
        p2HealthBar = new HealthBar(100);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Final Project");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        // Establishes the double buffer strategy
        createBufferStrategy(2);
    }

    @Override
    public void paint(Graphics g)
    {
        // Initializes double buffer strategy
        BufferStrategy bf = this.getBufferStrategy();

        if (bf == null)
        {
            return;
        }

        Graphics2D g2 =
                (Graphics2D) bf.getDrawGraphics();

        try
        {
            // Draws game states
            if (backend.getGameState() == Game.STATE_WELCOME)
            {
                drawWelcomeScreen(g2);
                return;
            }

            if (backend.getGameState() == Game.STATE_INSTRUCTIONS)
            {
                drawInstructionsScreen(g2);
                return;
            }

            int shakeX = 0;
            int shakeY = 0;
            // Checks for shaking effect
            if (backend.shakeTimer > 0)
            {
                shakeX =
                        (int)(Math.random()
                                * backend.shakeStrength * 2
                                - backend.shakeStrength);

                shakeY =
                        (int)(Math.random()
                                * backend.shakeStrength * 2
                                - backend.shakeStrength);
            }

            g2.translate(shakeX, shakeY);

            g2.setColor(Color.WHITE);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
            // Draws all screen elements
            g2.drawImage(
                    bgImage,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );

            drawHealthBars(g2);
            drawPowerUp(g2);
            drawPowerUpWarning(g2);
            drawPlayers(g2);
            drawFireballs(g2);
            drawBlast(g2);
            drawHitEffects(g2);

            g2.translate(-shakeX, -shakeY);

            if (backend.isGameOver())
            {
                drawGameOver(g2);
            }
        }
        finally
        {
            g2.dispose();
            bf.show();
        }
    }
// =====================================================
// WELCOME SCREEN
// =====================================================

    private void drawWelcomeScreen(Graphics2D g2)
    {
        // Background

        g2.setColor(new Color(240, 220, 180));

        g2.fillRect(
                0,
                0,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );
        // Title
        g2.setColor(Color.BLACK);

        g2.setFont(
                new Font("Arial", Font.BOLD, 60)
        );

        String title = "BRAWL 360";

        FontMetrics titleMetrics =
                g2.getFontMetrics();

        int titleX =
                (WINDOW_WIDTH -
                        titleMetrics.stringWidth(title)) / 2;

        g2.drawString(
                title,
                titleX,
                90
        );

        // Logo image

        int logoWidth = 420;
        int logoHeight = 420;

        int logoX =
                (WINDOW_WIDTH - logoWidth) / 2;

        int logoY = 70;

        g2.drawImage(
                logoImage,
                logoX,
                logoY,
                logoWidth,
                logoHeight,
                this
        );

        // Fight button

        int fightCenterX = WINDOW_WIDTH / 2;
        int fightCenterY = 575;

        int spikes = 18;

        int outerRadius = 150;
        int innerRadius = 105;

        // Pulsating animation
        double pulse =
                1.0 + 0.08 *
                        Math.sin(System.currentTimeMillis() / 180.0);

        int pulseOuterRadius =
                (int)(outerRadius * pulse);

        int pulseInnerRadius =
                (int)(innerRadius * pulse);

        Polygon burst = new Polygon();

        for (int i = 0; i < spikes * 2; i++)
        {
            double angle =
                    Math.PI * i / spikes;

            int radius;

            if (i % 2 == 0)
            {
                radius = pulseOuterRadius;
            }
            else
            {
                radius = pulseInnerRadius;
            }

            int px =
                    (int)(fightCenterX +
                            Math.cos(angle) * radius);

            int py =
                    (int)(fightCenterY +
                            Math.sin(angle) * radius);

            burst.addPoint(px, py);
        }

        // Main burst
        g2.setColor(
                new Color(255, 220, 0)
        );

        g2.fillPolygon(burst);

        // Outline
        g2.setColor(Color.BLACK);

        g2.setStroke(
                new BasicStroke(6)
        );

        g2.drawPolygon(burst);

        // Comic text
        g2.setFont(
                new Font("Impact", Font.BOLD, 80)
        );

        String fightText = "FIGHT!";

        FontMetrics fightMetrics =
                g2.getFontMetrics();

        int fightTextX =
                fightCenterX -
                        fightMetrics.stringWidth(fightText) / 2;

        int fightTextY =
                fightCenterY +
                        fightMetrics.getAscent() / 3;

        // Shadow
        g2.setColor(Color.BLACK);

        g2.drawString(
                fightText,
                fightTextX + 6,
                fightTextY + 6
        );

        // Main text
        g2.setColor(
                new Color(255, 50, 50)
        );

        g2.drawString(
                fightText,
                fightTextX,
                fightTextY
        );

        // Instructions button

        int instructionsWidth = 250;
        int instructionsHeight = 60;

        int instructionsX =
                (WINDOW_WIDTH - instructionsWidth) / 2;

        int instructionsY = 730;

        g2.setColor(
                new Color(0, 0, 0, 190)
        );

        g2.fillRoundRect(
                instructionsX,
                instructionsY,
                instructionsWidth,
                instructionsHeight,
                25,
                25
        );

        g2.setColor(Color.WHITE);

        g2.setStroke(
                new BasicStroke(4)
        );

        g2.drawRoundRect(
                instructionsX,
                instructionsY,
                instructionsWidth,
                instructionsHeight,
                25,
                25
        );

        g2.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        String instructionsText =
                "INSTRUCTIONS";

        FontMetrics instructionsMetrics =
                g2.getFontMetrics();

        int instructionsTextX =
                instructionsX +
                        (instructionsWidth -
                                instructionsMetrics.stringWidth(instructionsText)) / 2;

        int instructionsTextY =
                instructionsY +
                        ((instructionsHeight -
                                instructionsMetrics.getHeight()) / 2)
                        + instructionsMetrics.getAscent();

        g2.drawString(
                instructionsText,
                instructionsTextX,
                instructionsTextY
        );
    }
    // Draws the image of the instructions
    private void drawInstructionsScreen(Graphics2D g2)
    {
        g2.setColor(Color.BLACK);

        g2.fillRect(
                0,
                0,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

        g2.drawImage(
                controlsImage,
                0,
                0,
                WINDOW_WIDTH,
                WINDOW_HEIGHT,
                this
        );
    }
    // Draws each player's health bar
    private void drawHealthBars(Graphics2D g2)
    {
        int barWidth = 300;
        int barHeight = 25;
        int iconSize = 50;

        // PLAYER 1

        g2.drawImage(
                ryuIcon,
                10,
                40,
                iconSize,
                iconSize,
                this
        );

        int p1Width =
                p1HealthBar.getWidth(
                        backend.getP1().getHealth(),
                        barWidth
                );

        g2.setColor(Color.RED);

        g2.fillRect(
                70,
                50,
                barWidth,
                barHeight
        );

        g2.setColor(Color.GREEN);

        g2.fillRect(
                70,
                50,
                p1Width,
                barHeight
        );

        g2.setColor(Color.BLACK);

        g2.drawRect(
                70,
                50,
                barWidth,
                barHeight
        );

        // PLAYER 2

        g2.drawImage(
                kenIcon,
                WINDOW_WIDTH - 60,
                40,
                iconSize,
                iconSize,
                this
        );

        int p2Width =
                p2HealthBar.getWidth(
                        backend.getP2().getHealth(),
                        barWidth
                );

        g2.setColor(Color.RED);

        g2.fillRect(
                WINDOW_WIDTH - 370,
                50,
                barWidth,
                barHeight
        );

        g2.setColor(Color.GREEN);

        g2.fillRect(
                WINDOW_WIDTH - 370,
                50,
                p2Width,
                barHeight
        );

        g2.setColor(Color.BLACK);

        g2.drawRect(
                WINDOW_WIDTH - 370,
                50,
                barWidth,
                barHeight
        );
    }
    // Player rendering
    private void drawPlayers(Graphics2D g2)
    {
        // PLAYER 1
        if (backend.getP1() != null)
        {
            drawPlayerGlow(g2, backend.getP1());

            Image img = ryuIdleImage;

            String action =
                    backend.getP1().getCurrentAction();
            // Draws their image based on current action
            if ("punch".equals(action))
            {
                img = ryuPunchImage;
            }
            else if ("kick".equals(action))
            {
                img = ryuKickImage;
            }
            else if ("dodge".equals(action))
            {
                img = ryuDodgeImage;
            }

            int x = backend.getP1().getX();
            int y = backend.getP1().getY();
            // Reflects the image depending on its orientation to other player
            if (backend.getP1().isFacingRight())
            {
                g2.drawImage(
                        img,
                        x,
                        y,
                        230,
                        250,
                        this
                );
            }
            else
            {
                g2.drawImage(
                        img,
                        x + 230,
                        y,
                        -230,
                        250,
                        this
                );
            }
        }

        // PLAYER 2

        if (backend.getP2() != null)
        {
            drawPlayerGlow(g2, backend.getP2());

            Image img = kenIdleImage;

            String action =
                    backend.getP2().getCurrentAction();
            // Draws current action state of the player
            if ("punch".equals(action))
            {
                img = kenPunchImage;
            }
            else if ("kick".equals(action))
            {
                img = kenKickImage;
            }
            else if ("dodge".equals(action))
            {
                img = kenDodgeImage;
            }

            int x = backend.getP2().getX();
            int y = backend.getP2().getY();
            // Reflects the image depending on orientation to other player
            if (backend.getP2().isFacingRight())
            {
                g2.drawImage(
                        img,
                        x,
                        y,
                        230,
                        250,
                        this
                );
            }
            else
            {
                g2.drawImage(
                        img,
                        x + 230,
                        y,
                        -230,
                        250,
                        this
                );
            }
        }
    }
    // Power up glow effect
    private void drawPlayerGlow(Graphics2D g2,
                                Player player)
    {
        if (!player.hasPowerUp())
        {
            return;
        }

        int x = player.getX();
        int y = player.getY();

        int width = 230;
        int height = 250;

        double pulse =
                1.0 + 0.08 *
                        Math.sin(System.currentTimeMillis() / 120.0);

        int outerWidth =
                (int)(width * 1.45 * pulse);

        int outerHeight =
                (int)(height * 1.35 * pulse);

        int middleWidth =
                (int)(width * 1.28 * pulse);

        int middleHeight =
                (int)(height * 1.20 * pulse);

        int innerWidth =
                (int)(width * 1.12 * pulse);

        int innerHeight =
                (int)(height * 1.08 * pulse);

        int outerX =
                x - (outerWidth - width) / 2;

        int outerY =
                y - (outerHeight - height) / 2;

        int middleX =
                x - (middleWidth - width) / 2;

        int middleY =
                y - (middleHeight - height) / 2;

        int innerX =
                x - (innerWidth - width) / 2;

        int innerY =
                y - (innerHeight - height) / 2;

        g2.setColor(
                new Color(255, 0, 0, 45)
        );

        g2.fillOval(
                outerX,
                outerY,
                outerWidth,
                outerHeight
        );

        g2.setColor(
                new Color(255, 50, 50, 75)
        );

        g2.fillOval(
                middleX,
                middleY,
                middleWidth,
                middleHeight
        );

        g2.setColor(
                new Color(255, 120, 120, 110)
        );

        g2.fillOval(
                innerX,
                innerY,
                innerWidth,
                innerHeight
        );
    }
    // Draws the blast image emanating from the player
    private void drawBlast(Graphics2D g2)
    {
        Player blastingPlayer = null;

        if (backend.getP1() != null &&
                backend.getP1().isBlasting())
        {
            blastingPlayer = backend.getP1();
        }
        else if (backend.getP2() != null &&
                backend.getP2().isBlasting())
        {
            blastingPlayer = backend.getP2();
        }

        if (blastingPlayer != null)
        {
            int r =
                    blastingPlayer.getBlastRadius();

            int cx =
                    blastingPlayer.getX() + 115;

            int cy =
                    blastingPlayer.getY() + 125;

            g2.setColor(
                    new Color(255, 0, 0, 120)
            );

            g2.fillOval(
                    cx - r / 2,
                    cy - r / 2,
                    r,
                    r
            );

            g2.setColor(
                    new Color(255, 0, 0, 60)
            );

            g2.fillOval(
                    cx - r,
                    cy - r,
                    r * 2,
                    r * 2
            );
        }
    }
    // Draws the power up image
    private void drawPowerUp(Graphics2D g2)
    {
        PowerUp p =
                backend.getPowerUp();

        if (p != null)
        {
            g2.drawImage(
                    powerUpImage,
                    p.getX(),
                    p.getY(),
                    p.getSize(),
                    p.getSize(),
                    this
            );
        }
    }
    // Draws the power up warning
    private void drawPowerUpWarning(Graphics2D g2)
    {
        // Checks if it should display a warning
        if (backend.shouldShowPowerUpWarning())
        {
            g2.setColor(
                    new Color(255, 255, 0, 200)
            );

            g2.setFont(
                    new Font("Arial",
                            Font.BOLD,
                            32)
            );

            g2.drawString(
                    "POWER-UP INCOMING!",
                    410,
                    140
            );
        }
    }
    // Draws the effect when a player is hit with an attack
    private void drawHitEffects(Graphics2D g2)
    {
        // Draws punch effect
        if (backend.effectTimer > 0 &&
                backend.effectType.equals("punch"))
        {
            int t = backend.effectTimer;

            int size =
                    30 + (12 - t) * 6;

            g2.setColor(
                    new Color(255, 120, 0, 180)
            );

            g2.fillOval(
                    backend.effectX - size / 2,
                    backend.effectY - size / 2,
                    size,
                    size
            );
        }
        // Draws kick effect
        if (backend.effectTimer > 0 &&
                backend.effectType.equals("kick"))
        {
            int t = backend.effectTimer;

            for (int i = 0; i < 6; i++)
            {
                int size =
                        30 + i * 10 - t * 2;

                int alpha =
                        120 - i * 15;

                if (alpha > 0)
                {
                    g2.setColor(
                            new Color(
                                    100,
                                    100,
                                    100,
                                    alpha
                            )
                    );

                    int offsetX =
                            (int)(Math.random() * 30 - 15);

                    int offsetY =
                            (int)(Math.random() * 20 - 10);

                    g2.fillOval(
                            backend.effectX +
                                    offsetX -
                                    size / 2,

                            backend.effectY +
                                    offsetY -
                                    size / 2,

                            size,
                            size
                    );
                }
            }
        }
    }
    // Draws each fireball
    private void drawFireballs(Graphics2D g2)
    {
        for (Fireball f : backend.getFireballs())
        {
            Image img;

            if (f.getOwner() == backend.getP1())
            {
                img = redBallImage;
            }
            else
            {
                img = blueBallImage;
            }

            g2.drawImage(
                    img,
                    f.getX(),
                    f.getY(),
                    150,
                    150,
                    this
            );
        }
    }
    // Game over screen
    private void drawGameOver(Graphics2D g2)
    {
        // Dark overlay
        g2.setColor(
                new Color(0, 0, 0, 180)
        );

        g2.fillRect(
                0,
                0,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );
        // Gameover text
        g2.setColor(Color.WHITE);

        g2.setFont(
                new Font("Arial", Font.BOLD, 60)
        );

        g2.drawString(
                "GAME OVER",
                435,
                300
        );
        // Winner text
        g2.setFont(
                new Font("Arial", Font.BOLD, 40)
        );

        g2.drawString(
                backend.getWinner(),
                470,
                380
        );

        // Play again button
        int buttonWidth = 280;
        int buttonHeight = 80;

        int buttonX =
                (WINDOW_WIDTH - buttonWidth) / 2;

        int buttonY = 470;

        g2.setColor(
                new Color(255, 220, 0)
        );

        g2.fillRoundRect(
                buttonX,
                buttonY,
                buttonWidth,
                buttonHeight,
                30,
                30
        );

        g2.setColor(Color.BLACK);

        g2.setStroke(
                new BasicStroke(5)
        );

        g2.drawRoundRect(
                buttonX,
                buttonY,
                buttonWidth,
                buttonHeight,
                30,
                30
        );

        g2.setFont(
                new Font("Impact", Font.BOLD, 42)
        );

        String text = "PLAY AGAIN";

        FontMetrics fm =
                g2.getFontMetrics();

        int textX =
                buttonX +
                        (buttonWidth -
                                fm.stringWidth(text)) / 2;

        int textY =
                buttonY +
                        ((buttonHeight -
                                fm.getHeight()) / 2)
                        + fm.getAscent();

        g2.drawString(
                text,
                textX,
                textY
        );
    }
}