import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private Game backend;

    private final Image idleImage;
    private final Image punchImage;
    private final Image kickImage;
    private final Image dodgeImage;

    public GameView(Game backend) {
        this.backend = backend;

        idleImage = new ImageIcon("src/main/resources/ryu.png").getImage();
        punchImage = new ImageIcon("src/main/resources/ryu_punch.png").getImage();
        kickImage = new ImageIcon("src/main/resources/ryu_kick.png").getImage();
        dodgeImage = new ImageIcon("src/main/resources/ryu_dodge.png").getImage();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Final Project");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        Image currentImage = idleImage;
        if (backend.p1.getCurrentAction().equals("punch")) {
            currentImage = punchImage;
        } else if (backend.p1.getCurrentAction().equals("kick")) {
            currentImage = kickImage;
        } else if (backend.p1.getCurrentAction().equals("dodge")) {
            currentImage = dodgeImage;
        }

            g.drawImage(currentImage,
                    backend.p1.getX(),
                    backend.p1.getY(),
                    100, 100,
                    this);
        }
    }
