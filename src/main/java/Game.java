import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener, ActionListener{
    private GameView window;

    public Player p1;
    public Player p2;
    public String winner;
    private static final int SLEEP_TIME = 16;
    private boolean leftPressed = false;
    private boolean rightPressed = false;


    public Game() {
        this.p1 = new Player();
        this.p2 = new Player();

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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {

            case KeyEvent.VK_A:
                leftPressed = true;
                break;

            case KeyEvent.VK_D:
                rightPressed = true;
                break;

            case KeyEvent.VK_W:
                p1.jump();
                break;

            // Punch: Shift OR F
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_F:
                p1.punch();
                break;
            case KeyEvent.VK_E:
                p1.blast();
                break;

            // Kick: G OR Enter
            case KeyEvent.VK_G:
            case KeyEvent.VK_ENTER:
                p1.kick();
                break;
            // Dodge: Q OR quotation key
            case KeyEvent.VK_Q:
            case KeyEvent.VK_QUOTE:
                p1.dodge();
                break;
        }

        window.repaint();


    }

    public static void main(String[] args) {
        Game g1 = new Game();
        Timer clock = new Timer(SLEEP_TIME, g1);
        clock.start();
    }
}
