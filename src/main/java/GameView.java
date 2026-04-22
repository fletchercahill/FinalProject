import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class GameView extends JFrame {

    private static final int    WINDOW_WIDTH = 1200,
            WINDOW_HEIGHT = 800;
    private Game backend;


    private final Image playerImage;

    public GameView(Game backend) {
        this.backend = backend;
        playerImage = new ImageIcon("src/main/resources/ryu.jpeg").getImage();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Final Project");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1200, 800); // clears screen

        if (backend.p1 != null) {
            g.drawImage(playerImage,
                    backend.p1.getX(),
                    backend.p1.getY(),
                    100, 100,
                    this);
        }
    }
}
