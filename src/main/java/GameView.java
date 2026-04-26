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

        playerImage = new ImageIcon("src/main/resources/ryu.png").getImage();
        bgImage = new ImageIcon("src/main/resources/bg.jpg").getImage();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Final Project");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        createBufferStrategy(2);
    }

    public void paint(Graphics g) {
        BufferStrategy bf = this.getBufferStrategy();

        if (bf == null) {
            return;
        }

        Graphics g2 = null;

        try {
            g2 = bf.getDrawGraphics();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Player 1: Ryu
            if (backend.p1 != null) {
                Image img = ryuIdleImage;

                if (backend.p1.getCurrentAction().equals("punch")) {
                    img = ryuPunchImage;
                } else if (backend.p1.getCurrentAction().equals("kick")) {
                    img = ryuKickImage;
                } else if (backend.p1.getCurrentAction().equals("dodge")) {
                    img = ryuDodgeImage;
                }

            g.drawImage(currentImage,
                    backend.p1.getX(),
                    backend.p1.getY(),
                    200, 300,
                    this);
        }
        if (backend.p1.isBlasting()) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // strength tied to explosion size (strong early, weaker later)
            int r = backend.p1.getBlastRadius();
            int shakeStrength = Math.max(0, 20 - r / 15);

// random offset
            int shakeX = (int)(Math.random() * shakeStrength * 2 - shakeStrength);
            int shakeY = (int)(Math.random() * shakeStrength * 2 - shakeStrength);

// apply shake
            g2.translate(shakeX, shakeY);
            int cx = backend.p1.getX() + 100;
            int cy = backend.p1.getY() + 150;


            // fake "time" using radius
            float progress = Math.min(1.0f, r / 300.0f);

            // ⚡ SCREEN FLASH (strong at start, fades out)
            int flashAlpha = (int)(200 * (1 - progress));
            g2.setColor(new Color(255, 255, 255, flashAlpha));
            g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            // 🌞 GLOW BLOOM (layered light)
            for (int i = 0; i < 4; i++) {
                int glowR = r + i * 20;
                int alpha = (int)(120 * (1 - progress)) - i * 20;

                if (alpha > 0) {
                    g2.setColor(new Color(255, 200 - i * 40, 50, alpha));
                    g2.fillOval(cx - glowR, cy - glowR, glowR * 2, glowR * 2);
                }
            }

            // ☀️ WHITE HOT CORE
            int coreSize = Math.max(10, r / 4);
            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillOval(cx - coreSize/2, cy - coreSize/2, coreSize, coreSize);

            // 🔥 FIREBALL
            g2.setColor(new Color(255, 120, 0, 180));
            g2.fillOval(cx - r/2, cy - r/2, r, r);

            g2.setColor(new Color(255, 60, 0, 120));
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);

            // 💥 SHOCKWAVE RINGS (derived from radius)
            g2.setStroke(new BasicStroke(4));

            for (int i = 0; i < 3; i++) {
                int ringR = (int)(r * (1.2 + i * 0.4));
                int alpha = (int)(150 * (1 - progress)) - i * 40;

                if (alpha > 0) {
                    g2.setColor(new Color(255, 255, 255, alpha));
                    g2.drawOval(cx - ringR, cy - ringR, ringR * 2, ringR * 2);
                }
            }

            // 🌫 SMOKE EDGE
            int smokeR = (int)(r * 1.6);
            int smokeAlpha = (int)(100 * (1 - progress));

            g2.setStroke(new BasicStroke(6));
            g2.setColor(new Color(80, 80, 80, smokeAlpha));
            g2.drawOval(cx - smokeR, cy - smokeR, smokeR * 2, smokeR * 2);

            // 🌪 HEAT DISTORTION LINES
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < 5; i++) {
                int heatR = r + i * 12;
                g2.setColor(new Color(255, 255, 255, 20));
                g2.drawOval(cx - heatR, cy - heatR, heatR * 2, heatR * 2);
            }

        }
    }
}
                g2.drawImage(img,
                        backend.p1.getX(),
                        backend.p1.getY(),
                        100, 100,
                        this);
            }

            // Player 2: Ken
            if (backend.p2 != null) {
                Image img = kenIdleImage;

                if (backend.p2.getCurrentAction().equals("punch")) {
                    img = kenPunchImage;
                } else if (backend.p2.getCurrentAction().equals("kick")) {
                    img = kenKickImage;
                } else if (backend.p2.getCurrentAction().equals("dodge")) {
                    img = kenDodgeImage;
                }

                g2.drawImage(img,
                        backend.p2.getX(),
                        backend.p2.getY(),
                        100, 100,
                        this);
            }

        } finally {
            if (g2 != null) {
                g2.dispose();
            }
        }
