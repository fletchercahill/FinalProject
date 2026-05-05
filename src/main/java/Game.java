import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;

public class Game implements KeyListener, ActionListener, MouseListener {

    public static final int STATE_WELCOME = 0;
    public static final int STATE_FIGHT = 1;
    public static final int STATE_END = 2;
    public static final int STATE_INSTRUCTIONS = 3;

    private int gameState = STATE_WELCOME;

    private GameView window;

    public Player p1;
    public Player p2;

    private static final int SLEEP_TIME = 16;

    private int p1ActionCounter = 0;
    private int p2ActionCounter = 0;

    private boolean p1AttackHit = false;
    private boolean p2AttackHit = false;

    private static final int PUNCH_RANGE = 110;
    private static final int KICK_RANGE = 140;

    private static final int PUNCH_DAMAGE = 10;
    private static final int KICK_DAMAGE = 15;

    private HashSet<Integer> keysPressed = new HashSet<>();

    public Game() {
        p1 = new Player(100, 500);
        p2 = new Player(900, 500);

        window = new GameView(this);
        window.addKeyListener(this);
        window.addMouseListener(this);
        window.repaint();
    }

    public int getGameState() {
        return gameState;
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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (gameState == STATE_WELCOME) {

            if (x >= 475 && x <= 725 && y >= 250 && y <= 310) {
                gameState = STATE_FIGHT;
            }

            if (x >= 475 && x <= 725 && y >= 400 && y <= 460) {
                gameState = STATE_INSTRUCTIONS;
            }
        }

        else if (gameState == STATE_INSTRUCTIONS) {

            if (x >= 430 && x <= 770 && y >= 690 && y <= 780) {
                gameState = STATE_WELCOME;
            }
        }

        window.repaint();
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameState != STATE_FIGHT) {
            window.repaint();
            return;
        }

        // -------- RYU --------
        if (keysPressed.contains(KeyEvent.VK_A)) p1.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_D)) p1.moveRight();
        if (keysPressed.contains(KeyEvent.VK_W)) p1.jump();

        if (keysPressed.contains(KeyEvent.VK_F)) {
            p1.punch();
            p1ActionCounter = 30;
            p1AttackHit = false;
        }

        if (keysPressed.contains(KeyEvent.VK_G)) {
            p1.kick();
            p1ActionCounter = 30;
            p1AttackHit = false;
        }

        if (keysPressed.contains(KeyEvent.VK_S)) {
            p1.dodge();
            p1ActionCounter = 30;
        }

        if (keysPressed.contains(KeyEvent.VK_E)) {
            p1.blast();
            p1ActionCounter = 30;
        }

        // -------- KEN --------
        if (keysPressed.contains(KeyEvent.VK_LEFT)) p2.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) p2.moveRight();
        if (keysPressed.contains(KeyEvent.VK_UP)) p2.jump();

        if (keysPressed.contains(KeyEvent.VK_SHIFT)) {
            p2.punch();
            p2ActionCounter = 30;
            p2AttackHit = false;
        }

        if (keysPressed.contains(KeyEvent.VK_ENTER)) {
            p2.kick();
            p2ActionCounter = 30;
            p2AttackHit = false;
        }

        if (keysPressed.contains(KeyEvent.VK_DOWN)) {
            p2.dodge();
            p2ActionCounter = 30;
        }

        if (keysPressed.contains(KeyEvent.VK_SLASH)) {
            p2.blast();
            p2ActionCounter = 30;
        }

        p1.update();
        p2.update();

        checkAttacks();

        if (p1.getHealth() <= 0 || p2.getHealth() <= 0) {
            gameState = STATE_END;
        }

        // Reset Player 1 action
        if (p1ActionCounter > 0) {
            p1ActionCounter--;

            if (p1ActionCounter == 0) {
                p1.resetAction();
                p1AttackHit = false;
            }
        }

        // Reset Player 2 action
        if (p2ActionCounter > 0) {
            p2ActionCounter--;

            if (p2ActionCounter == 0) {
                p2.resetAction();
                p2AttackHit = false;
            }
        }

        window.repaint();
    }

    private void checkAttacks() {
        int distance = Math.abs(p1.getX() - p2.getX());

        if (!p1AttackHit) {
            if (p1.getCurrentAction().equals("punch") && distance <= PUNCH_RANGE) {
                if (!p2.getCurrentAction().equals("dodge")) {
                    p2.takeDamage(PUNCH_DAMAGE);
                }
                p1AttackHit = true;
            } else if (p1.getCurrentAction().equals("kick") && distance <= KICK_RANGE) {
                if (!p2.getCurrentAction().equals("dodge")) {
                    p2.takeDamage(KICK_DAMAGE);
                }
                p1AttackHit = true;
            }
        }

        if (!p2AttackHit) {
            if (p2.getCurrentAction().equals("punch") && distance <= PUNCH_RANGE) {
                if (!p1.getCurrentAction().equals("dodge")) {
                    p1.takeDamage(PUNCH_DAMAGE);
                }
                p2AttackHit = true;
            } else if (p2.getCurrentAction().equals("kick") && distance <= KICK_RANGE) {
                if (!p1.getCurrentAction().equals("dodge")) {
                    p1.takeDamage(KICK_DAMAGE);
                }
                p2AttackHit = true;
            }
        }
    }

    public static void main(String[] args) {
        Game g1 = new Game();
        Timer clock = new Timer(SLEEP_TIME, g1);
        clock.start();
    }
}