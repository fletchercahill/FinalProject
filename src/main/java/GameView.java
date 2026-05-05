import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameView extends JFrame {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private Game backend;

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

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

            drawHealthBars(g2);
            drawPlayers(g2);
            drawBlast(g2);

        } finally {
            g2.dispose();
            bf.show();
        }
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

    private void drawPlayers(Graphics2D g2) {
        Image img1 = ryuIdleImage;
        String action1 = backend.p1.getCurrentAction();

        if ("punch".equals(action1)) img1 = ryuPunchImage;
        else if ("kick".equals(action1)) img1 = ryuKickImage;
        else if ("dodge".equals(action1)) img1 = ryuDodgeImage;

        g2.drawImage(img1, backend.p1.getX(), backend.p1.getY(), 230, 250, this);

        Image img2 = kenIdleImage;
        String action2 = backend.p2.getCurrentAction();

        if ("punch".equals(action2)) img2 = kenPunchImage;
        else if ("kick".equals(action2)) img2 = kenKickImage;
        else if ("dodge".equals(action2)) img2 = kenDodgeImage;

        g2.drawImage(img2, backend.p2.getX(), backend.p2.getY(), 230, 250, this);
    }

    private void drawBlast(Graphics2D g2) {
        Player blastingPlayer = null;

        if (backend.p1 != null && backend.p1.isBlasting()) {
            blastingPlayer = backend.p1;
        } else if (backend.p2 != null && backend.p2.isBlasting()) {
            blastingPlayer = backend.p2;
        }

        if (blastingPlayer != null) {
            int r = blastingPlayer.getBlastRadius();

            int cx = blastingPlayer.getX() + 115;
            int cy = blastingPlayer.getY() + 125;

            g2.setColor(new Color(255, 0, 0, 120));
            g2.fillOval(cx - r / 2, cy - r / 2, r, r);

            g2.setColor(new Color(255, 0, 0, 60));
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        }
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

    private void drawInstructionsScreen(Graphics2D g2) {

        // background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // draw your Controls.png full screen
        g2.drawImage(controlsImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
    }
}