import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameView extends JFrame {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private Game backend;

    private final Image idleImage;
    private final Image punchImage;
    private final Image kickImage;
    private final Image bgImage;
    private final Image playerImage;
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

    private final Image bgImage;

    // ✅ NEW: controls image
    private final Image controlsImage;

    private HealthBar p1HealthBar;
    private HealthBar p2HealthBar;

    public GameView(Game backend) {
        this.backend = backend;

        ryuIdleImage = new ImageIcon("src/main/resources/ryu.png").getImage();
        ryuPunchImage = new ImageIcon("src/main/resources/ryu_punch.png").getImage();
        ryuKickImage = new ImageIcon("src/main/resources/ryu_kick.png").getImage();
        ryuDodgeImage = new ImageIcon("src/main/resources/ryu_dodge.png").getImage();

        kenIdleImage = new ImageIcon("src/main/resources/ken.png").getImage();
        kenPunchImage = new ImageIcon("src/main/resources/ken_punch.png").getImage();
        kenKickImage = new ImageIcon("src/main/resources/ken_kick.png").getImage();
        kenDodgeImage = new ImageIcon("src/main/resources/ken_dodge.png").getImage();

        ryuIcon = new ImageIcon("src/main/resources/ryu_icon.png").getImage();
        kenIcon = new ImageIcon("src/main/resources/ken_icon.png").getImage();

        bgImage = new ImageIcon("src/main/resources/bg.jpg").getImage();

        // ✅ LOAD YOUR CONTROLS IMAGE
        controlsImage = new ImageIcon("src/main/resources/Controls.png").getImage();

        p1HealthBar = new HealthBar(100);
        p2HealthBar = new HealthBar(100);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Final Project");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        createBufferStrategy(2);
    }
    private void drawInstructionsScreen(Graphics2D g2) {

        // background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // draw your Controls.png full screen
        g2.drawImage(controlsImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
    }
    private void drawHealthBars(Graphics2D g2) {
        int barWidth = 300;
        int barHeight = 25;
        int iconSize = 50;

        g2.drawImage(ryuIcon, 10, 40, iconSize, iconSize, this);

        int p1Width = p1HealthBar.getWidth(backend.p1.getHealth(), barWidth);

        g2.setColor(Color.RED);
        g2.fillRect(70, 50, barWidth, barHeight);

        g2.setColor(Color.GREEN);
        g2.fillRect(70, 50, p1Width, barHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(70, 50, barWidth, barHeight);

        g2.drawImage(kenIcon, WINDOW_WIDTH - 60, 40, iconSize, iconSize, this);

        int p2Width = p2HealthBar.getWidth(backend.p2.getHealth(), barWidth);

        g2.setColor(Color.RED);
        g2.fillRect(WINDOW_WIDTH - 370, 50, barWidth, barHeight);

        g2.setColor(Color.GREEN);
        g2.fillRect(WINDOW_WIDTH - 370, 50, p2Width, barHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(WINDOW_WIDTH - 370, 50, barWidth, barHeight);
    }
    private void drawWelcomeScreen(Graphics2D g2) {
        g2.setColor(new Color(240, 220, 180));
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("BRAWL 360", 420, 100);

        g2.setColor(Color.WHITE);
        g2.fillRect(475, 250, 250, 60);

        g2.setColor(Color.BLACK);
        g2.drawRect(475, 250, 250, 60);

        g2.setFont(new Font("Arial", Font.BOLD, 35));
        g2.drawString("FIGHT!", 540, 292);

        g2.setColor(Color.WHITE);
        g2.fillRect(475, 400, 250, 60);

        g2.setColor(Color.BLACK);
        g2.drawRect(475, 400, 250, 60);

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("INSTRUCTIONS", 495, 440);
    }
    @Override
    public void paint(Graphics g) {
        BufferStrategy bf = this.getBufferStrategy();
        if (bf == null) return;

        Graphics2D g2 = (Graphics2D) bf.getDrawGraphics();

        try {
            if (backend.getGameState() == Game.STATE_WELCOME) {
                drawWelcomeScreen(g2);
                return;
            }

            if (backend.getGameState() == Game.STATE_INSTRUCTIONS) {
                drawInstructionsScreen(g2);
                return;
            }
            // Background
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            drawHealthBars(g2);

            // -------- PLAYER 1 --------
            if (backend.p1 != null) {
                Image img = ryuIdleImage;

                String action = backend.p1.getCurrentAction();
                if ("punch".equals(action)) img = ryuPunchImage;
                else if ("kick".equals(action)) img = ryuKickImage;
                else if ("dodge".equals(action)) img = ryuDodgeImage;

                g2.drawImage(img,
                        backend.p1.getX(),
                        backend.p1.getY(),
                        230, 250,
                        this);
            }

            // -------- PLAYER 2 --------
            if (backend.p2 != null) {
                Image img = kenIdleImage;

                String action = backend.p2.getCurrentAction();
                if ("punch".equals(action)) img = kenPunchImage;
                else if ("kick".equals(action)) img = kenKickImage;
                else if ("dodge".equals(action)) img = kenDodgeImage;

                g2.drawImage(img,
                        backend.p2.getX(),
                        backend.p2.getY(),
                        230, 250,
                        this);
            }

            // -------- BLAST (shared) --------
            Player blastingPlayer = null;

            if (backend.p1 != null && backend.p1.isBlasting()) {
                blastingPlayer = backend.p1;
            } else if (backend.p2 != null && backend.p2.isBlasting()) {
                blastingPlayer = backend.p2;
            }

            if (blastingPlayer != null) {
                int r = blastingPlayer.getBlastRadius();
                int shakeStrength = Math.max(0, 20 - r / 15);

                int shakeX = (int)(Math.random() * shakeStrength * 2 - shakeStrength);
                int shakeY = (int)(Math.random() * shakeStrength * 2 - shakeStrength);

                g2.translate(shakeX, shakeY);

                int cx = blastingPlayer.getX() + 100;
                int cy = blastingPlayer.getY() + 150;

                float progress = Math.min(1.0f, r / 300.0f);

                // Flash
                int flashAlpha = (int)(200 * (1 - progress));
                g2.setColor(new Color(255, 255, 255, flashAlpha));
                g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                // Glow
                for (int i = 0; i < 4; i++) {
                    int glowR = r + i * 20;
                    int alpha = (int)(120 * (1 - progress)) - i * 20;

                    if (alpha > 0) {
                        g2.setColor(new Color(255, 200 - i * 40, 50, alpha));
                        g2.fillOval(cx - glowR, cy - glowR, glowR * 2, glowR * 2);
                    }
                }

                // Core
                int coreSize = Math.max(10, r / 4);
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillOval(cx - coreSize / 2, cy - coreSize / 2, coreSize, coreSize);

                // Fireball
                g2.setColor(new Color(255, 120, 0, 180));
                g2.fillOval(cx - r / 2, cy - r / 2, r, r);

                g2.setColor(new Color(255, 60, 0, 120));
                g2.fillOval(cx - r, cy - r, r * 2, r * 2);

                // Shockwaves
                g2.setStroke(new BasicStroke(4));
                for (int i = 0; i < 3; i++) {
                    int ringR = (int)(r * (1.2 + i * 0.4));
                    int alpha = (int)(150 * (1 - progress)) - i * 40;

                    if (alpha > 0) {
                        g2.setColor(new Color(255, 255, 255, alpha));
                        g2.drawOval(cx - ringR, cy - ringR, ringR * 2, ringR * 2);
                    }
                }

                // Smoke
                int smokeR = (int)(r * 1.6);
                int smokeAlpha = (int)(100 * (1 - progress));

                g2.setStroke(new BasicStroke(6));
                g2.setColor(new Color(80, 80, 80, smokeAlpha));
                g2.drawOval(cx - smokeR, cy - smokeR, smokeR * 2, smokeR * 2);

                // Heat distortion
                g2.setStroke(new BasicStroke(2));
                for (int i = 0; i < 5; i++) {
                    int heatR = r + i * 12;
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.drawOval(cx - heatR, cy - heatR, heatR * 2, heatR * 2);
                }

                g2.translate(-shakeX, -shakeY);
            }
            if (backend.hitTimer > 0) {
                int size = 40 + (10 - backend.hitTimer) * 5; // grows slightly

                // outer glow
                g2.setColor(new Color(255, 200, 50, 150));
                g2.fillOval(backend.hitX - size/2, backend.hitY - size/2, size, size);

                // inner flash
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillOval(backend.hitX - 10, backend.hitY - 10, 20, 20);
            }

        } finally {
            if (backend.gameOver) {
                g2.setColor(new Color(0, 0, 0, 180)); // dark overlay
                g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 60));
                g2.drawString("GAME OVER", 400, 300);

                g2.setFont(new Font("Arial", Font.BOLD, 40));
                g2.drawString(backend.winner, 420, 380);
            }
            g2.dispose();
            bf.show();
        }
    }
}