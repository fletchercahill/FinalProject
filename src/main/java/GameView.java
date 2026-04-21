import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class GameView extends JFrame {

    private static final int    WINDOW_WIDTH = 1200,
            WINDOW_HEIGHT = 800;
    private Game backend;
    private final Image bgImage;

    public GameView(Game backend) {
        bgImage = new ImageIcon("src/main/resources/background.png").getImage();
        this.backend = backend;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Tells the program what to do when the window is closed.
        this.setTitle("Final Project");		                        // Sets the title of the window.
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);	        // Sets the width and height of the window.
        this.setVisible(true);
        // TODO: Write the view's constructor.
    }

    public void paint(Graphics g) {
        g.drawImage(bgImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
        // TODO: write paint() so it draws all the circles on the window.

    }

}
