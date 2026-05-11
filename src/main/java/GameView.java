import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameView extends JFrame
{

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private Game backend;

    private final Image bgImage;

    private final Image ryuIdleImage;
    private final Image ryuPunchImage;
    private final Image ryuKickImage;
    private final Image ryuDodgeImage;

    private final Image kenIdleImage;
    private final Image kenPunchImage;
    private final Image kenKickImage;
    private final Image kenDodgeImage;

    private final Image ryuIcon;
    private final Image kenIcon;

    private final Image controlsImage;
    private final Image powerUpImage;
    private final Image logoImage;

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

        p1HealthBar = new HealthBar(100);
        p2HealthBar = new HealthBar(100);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Final Project");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);

        createBufferStrategy(2);
    }

    @Override
    public void paint(Graphics g)
    {

        BufferStrategy bf = this.getBufferStrategy();

        if (bf == null) return;

        Graphics2D g2 =
                (Graphics2D) bf.getDrawGraphics();

        try
        {

            if (backend.getGameState() == Game.stateWelcome)
            {
                drawWelcomeScreen(g2);
                return;
            }

            if (backend.getGameState() == Game.stateInstructions)
            {
                drawInstructionsScreen(g2);
                return;
            }

            int shakeX = 0;
            int shakeY = 0;

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
            drawBlast(g2);
            drawHitEffects(g2);

            g2.translate(-shakeX, -shakeY);

            if (backend.gameOver)
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

    private void drawWelcomeScreen(Graphics2D g2)
    {

        g2.setColor(new Color(240, 220, 180));
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // ---------- TITLE ----------

        g2.setColor(Color.BLACK);

        Font titleFont = new Font("Arial", Font.BOLD, 60);
        g2.setFont(titleFont);

        String title = "BRAWL 360";

        FontMetrics fm = g2.getFontMetrics();

        int titleX = (WINDOW_WIDTH - fm.stringWidth(title)) / 2;

        g2.drawString(title, titleX, 90);

        // ---------- LOGO IMAGE ----------

        int logoWidth = 420;
        int logoHeight = 420;

        int logoX = (WINDOW_WIDTH - logoWidth) / 2;
        int logoY = 80;

        g2.drawImage(
                logoImage,
                logoX,
                logoY,
                logoWidth,
                logoHeight,
                this
        );

        // ---------- BUTTONS ----------

        int buttonWidth = 250;
        int buttonHeight = 60;

        // ---------- FIGHT BUTTON ----------

        int fightX = (WINDOW_WIDTH - buttonWidth) / 2;
        int fightY = 500;

        g2.setColor(Color.WHITE);
        g2.fillRect(fightX, fightY, buttonWidth, buttonHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(fightX, fightY, buttonWidth, buttonHeight);

        Font fightFont = new Font("Arial", Font.BOLD, 35);
        g2.setFont(fightFont);

        String fightText = "FIGHT!";

        FontMetrics fightFM = g2.getFontMetrics();

        int fightTextX =
                fightX + (buttonWidth - fightFM.stringWidth(fightText)) / 2;

        int fightTextY =
                fightY + ((buttonHeight - fightFM.getHeight()) / 2)
                        + fightFM.getAscent();

        g2.drawString(fightText, fightTextX, fightTextY);

        // ---------- INSTRUCTIONS BUTTON ----------

        int instructionsX = (WINDOW_WIDTH - buttonWidth) / 2;
        int instructionsY = 610;

        g2.setColor(Color.WHITE);
        g2.fillRect(instructionsX, instructionsY,
                buttonWidth, buttonHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(instructionsX, instructionsY,
                buttonWidth, buttonHeight);

        Font instructionsFont =
                new Font("Arial", Font.BOLD, 26);

        g2.setFont(instructionsFont);

        String instructionsText = "INSTRUCTIONS";

        FontMetrics instructionsFM =
                g2.getFontMetrics();

        int instructionsTextX =
                instructionsX +
                        (buttonWidth -
                                instructionsFM.stringWidth(instructionsText)) / 2;

        int instructionsTextY =
                instructionsY +
                        ((buttonHeight -
                                instructionsFM.getHeight()) / 2)
                        + instructionsFM.getAscent();

        g2.drawString(
                instructionsText,
                instructionsTextX,
                instructionsTextY
        );
    }

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

    private void drawHealthBars(Graphics2D g2)
    {

        int barWidth = 300;
        int barHeight = 25;
        int iconSize = 50;

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
                        backend.p1.getHealth(),
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
                        backend.p2.getHealth(),
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

    private void drawPlayers(Graphics2D g2)
    {

        Image img1 = ryuIdleImage;

        String a1 =
                backend.p1.getCurrentAction();

        if ("punch".equals(a1))
            img1 = ryuPunchImage;

        else if ("kick".equals(a1))
            img1 = ryuKickImage;

        else if ("dodge".equals(a1))
            img1 = ryuDodgeImage;

        int x1 = backend.p1.getX();
        int y1 = backend.p1.getY();

        if (backend.p1.isFacingRight())
        {

            g2.drawImage(
                    img1,
                    x1,
                    y1,
                    230,
                    250,
                    this
            );

        }
        else
        {

            g2.drawImage(
                    img1,
                    x1 + 230,
                    y1,
                    -230,
                    250,
                    this
            );
        }

        Image img2 = kenIdleImage;

        String a2 =
                backend.p2.getCurrentAction();

        if ("punch".equals(a2))
            img2 = kenPunchImage;

        else if ("kick".equals(a2))
            img2 = kenKickImage;

        else if ("dodge".equals(a2))
            img2 = kenDodgeImage;

        int x2 = backend.p2.getX();
        int y2 = backend.p2.getY();

        if (backend.p2.isFacingRight())
        {

            g2.drawImage(
                    img2,
                    x2,
                    y2,
                    230,
                    250,
                    this
            );

        }
        else
        {

            g2.drawImage(
                    img2,
                    x2 + 230,
                    y2,
                    -230,
                    250,
                    this
            );
        }
    }

    private void drawBlast(Graphics2D g2)
    {

        Player blastingPlayer = null;

        if (backend.p1 != null &&
                backend.p1.isBlasting())
        {

            blastingPlayer = backend.p1;

        } else if (backend.p2 != null &&
                backend.p2.isBlasting())
        {

            blastingPlayer = backend.p2;
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

    private void drawPowerUpWarning(Graphics2D g2)
    {

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

    private void drawHitEffects(Graphics2D g2)
    {

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

    private void drawGameOver(Graphics2D g2)
    {

        g2.setColor(
                new Color(0, 0, 0, 180)
        );

        g2.fillRect(
                0,
                0,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

        g2.setColor(Color.WHITE);

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        60
                )
        );

        g2.drawString(
                "GAME OVER",
                400,
                300
        );

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        40
                )
        );

        g2.drawString(
                backend.winner,
                420,
                380
        );
    }
}
