import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;

public class Game implements KeyListener, ActionListener{
    private GameView window;

    public Player p1;
    public Player p2;
    public String winner;
    private static final int SLEEP_TIME = 16;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private static final int SLEEP_TIME = 10;

    private int p1ActionCounter = 0;
    private int p2ActionCounter = 0;

    private HashSet<Integer> keysPressed = new HashSet<>();

    public Game() {
        p1 = new Player(100, 500);
        p2 = new Player(900, 500);

        this.window = new GameView(this);
        window.addKeyListener(this);
        window.repaint();
        // Continually updates the screen allowing for jump to work
    }

    public void actionPerformed(ActionEvent e) {

        if (leftPressed) {
            p1.moveLeft();
        }

        if (rightPressed) {
            p1.moveRight();
        }

        p1.update();
        window.repaint();
        //  TODO: Write the actionPerformed method.
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
        switch(e.getKeyCode()) {

            case KeyEvent.VK_A:
                leftPressed = false;
                break;

            case KeyEvent.VK_D:
                rightPressed = false;
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Player 1 movement
        if (keysPressed.contains(KeyEvent.VK_A)) {
            p1.moveLeft();
        }

        if (keysPressed.contains(KeyEvent.VK_D)) {
            p1.moveRight();
        }

        if (keysPressed.contains(KeyEvent.VK_W)) {
            p1.jump();
        }

        // Player 1 actions
        if (keysPressed.contains(KeyEvent.VK_F)) {
            p1.punch();
            p1ActionCounter = 30;
        }

        if (keysPressed.contains(KeyEvent.VK_G)) {
            p1.kick();
            p1ActionCounter = 30;
        }

        if (keysPressed.contains(KeyEvent.VK_Q)) {
            p1.dodge();
            p1ActionCounter = 30;
        }

        // Player 2 movement
        if (keysPressed.contains(KeyEvent.VK_LEFT)) {
            p2.moveLeft();
        }

        if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
            p2.moveRight();
        }

        if (keysPressed.contains(KeyEvent.VK_UP)) {
            p2.jump();
        }

        window.repaint();

        if (keysPressed.contains(KeyEvent.VK_ENTER)) {
            p2.kick();
            p2ActionCounter = 30;
        }

        if (keysPressed.contains(KeyEvent.VK_SLASH)) {
            p2.dodge();
            p2ActionCounter = 30;
        }

        // Reset Player 1 action after 1.5 seconds
        if (p1ActionCounter > 0) {
            p1ActionCounter--;

            if (p1ActionCounter == 0) {
                p1.resetAction();
            }
        }

        // Reset Player 2 action after 1.5 seconds
        if (p2ActionCounter > 0) {
            p2ActionCounter--;

            if (p2ActionCounter == 0) {
                p2.resetAction();
            }
        }

        window.repaint();
    }

    public static void main(String[] args) {
        Game g1 = new Game();

        Timer clock = new Timer(SLEEP_TIME, g1);
        clock.start();
    }
}
