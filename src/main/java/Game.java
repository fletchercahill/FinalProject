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
    public String winner;
    private static final int SLEEP_TIME = 16;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean facingRight = true;
    public boolean gameOver = false;

    private int p1ActionCounter = 0;
    private int p2ActionCounter = 0;
    public int hitX = 0;
    public int hitY = 0;
    public int hitTimer = 0;

    private static final int HIT_DURATION = 10; // frames
    private boolean p1AttackHit = false;
    private boolean p2AttackHit = false;

    private static final int PUNCH_RANGE = 110;
    private static final int KICK_RANGE = 140;

    private static final int PUNCH_DAMAGE = 4;
    private static final int KICK_DAMAGE = 5;

    private HashSet<Integer> keysPressed = new HashSet<>();

    public Game() {
        p1 = new Player(100, 500);
        p2 = new Player(900, 500);

        window = new GameView(this);
        window.addKeyListener(this);
        window.addMouseListener(this);
        window.repaint();
        // Continually updates the screen allowing for jump to work
    }

    public int getGameState() {
        return gameState;
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
    private boolean canBeHit(Player target) {
        return !target.isJumping() && !target.getCurrentAction().equals("dodge");
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

        if (keysPressed.contains(KeyEvent.VK_W)) {
            p1.jump();
        }

        // Player 1 actions
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
        if (keysPressed.contains(KeyEvent.VK_SLASH)) {
            p2.blast();
            p1ActionCounter = 30;
        }
        if (keysPressed.contains(KeyEvent.VK_E)) {
            p1.blast();
            p1ActionCounter = 30;
        }

        if (keysPressed.contains(KeyEvent.VK_Q)) {
            p1.dodge();
            p1ActionCounter = 30;
        }

        // ---- PLAYER 2 ----
        if (keysPressed.contains(KeyEvent.VK_LEFT)) p2.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) p2.moveRight();
        if (keysPressed.contains(KeyEvent.VK_UP)) p2.jump();

        if (keysPressed.contains(KeyEvent.VK_SHIFT)) {
            p2.punch();
            p2ActionCounter = 30;
            p2AttackHit = false;
        }
        if (keysPressed.contains(KeyEvent.VK_UP)) {
            p2.jump();
        }
        // Make players face each other
        if (p1.getX() < p2.getX()) {
            p1.setFacingRight(true);
            p2.setFacingRight(false);
        } else {
            p1.setFacingRight(false);
            p2.setFacingRight(true);
        }
        p1.update();
        p2.update();
        window.repaint();

        if (keysPressed.contains(KeyEvent.VK_ENTER)) {
            p2.kick();
            p2ActionCounter = 30;
            p2AttackHit = false;
        }

        if (keysPressed.contains(KeyEvent.VK_QUOTE)) {
            p2.dodge();
            p2ActionCounter = 30;
        }

        checkAttacks();
        if (hitTimer > 0) {
            hitTimer--;
        }

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

        // Reset Player 2 action after 1.5 seconds
        if (p2ActionCounter > 0) {
            p2ActionCounter--;

            if (p2ActionCounter == 0) {
                p2.resetAction();
                p2AttackHit = false;
            }
        }
        if (!gameOver) {
            if (p1.getHealth() <= 0) {
                gameOver = true;
                winner = "Player 2 Wins!";
            } else if (p2.getHealth() <= 0) {
                gameOver = true;
                winner = "Player 1 Wins!";
            }
        }
        window.repaint();
    }
    private boolean isInFront(Player attacker, Player target) {
        int attackerCenter = attacker.getX() + 115; // half width (230/2)
        int targetCenter = target.getX() + 115;

        if (attacker.isFacingRight()) {
            return targetCenter > attackerCenter;
        } else {
            return targetCenter < attackerCenter;
        }
    }
    private void checkAttacks() {
        int p1Center = p1.getX() + 115;
        int p2Center = p2.getX() + 115;

        int xDistance = Math.abs(p1Center - p2Center);        int yDistance = Math.abs(p1.getY() - p2.getY());

        boolean closeY = yDistance <= 120;

        // ---- P1 hits P2 ----
        if (!p1AttackHit && closeY && isInFront(p1, p2)) {

            if (p1.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE) {

                if (canBeHit(p2)){
                    p2.takeDamage(PUNCH_DAMAGE);
                    hitX = (p1.getX() + p2.getX()) / 2 + 115;
                    hitY = (p1.getY() + p2.getY()) / 2 + 100;
                    hitTimer = HIT_DURATION;
                    System.out.println("P1 punched P2 (-" + PUNCH_DAMAGE + " HP)");
                } else {
                    System.out.println("P2 dodged!");
                }

                p1AttackHit = true;

            } else if (p1.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE) {

                if (canBeHit(p2)) {
                    hitX = (p1.getX() + p2.getX()) / 2 + 115;
                    hitY = (p1.getY() + p2.getY()) / 2 + 100;
                    hitTimer = HIT_DURATION;
                    p2.takeDamage(KICK_DAMAGE);
                    System.out.println("P1 kicked P2 (-" + KICK_DAMAGE + " HP)");
                } else {
                    System.out.println("P2 dodged!");
                }

                p1AttackHit = true;
            }
        }

        // ---- P2 hits P1 ----
        if (!p2AttackHit && closeY && isInFront(p2, p1)) {

            if (p2.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE) {

                if (canBeHit(p1)) {
                    hitX = (p1.getX() + p2.getX()) / 2 + 115;
                    hitY = (p1.getY() + p2.getY()) / 2 + 100;
                    hitTimer = HIT_DURATION;
                    p1.takeDamage(PUNCH_DAMAGE);
                    System.out.println("P2 punched P1 (-" + PUNCH_DAMAGE + " HP)");
                } else {
                    System.out.println("P1 dodged!");
                }

                p2AttackHit = true;

            } else if (p2.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE) {

                if (canBeHit(p1)) {
                    hitX = (p1.getX() + p2.getX()) / 2 + 115;
                    hitY = (p1.getY() + p2.getY()) / 2 + 100;
                    hitTimer = HIT_DURATION;
                    p1.takeDamage(KICK_DAMAGE);
                    System.out.println("P2 kicked P1 (-" + KICK_DAMAGE + " HP)");
                } else {
                    System.out.println("P1 dodged!");
                }

                p2AttackHit = true;
            }
        }

        checkBlastDamage();
    }
    private void checkBlastDamage() {

        int distance = Math.abs(p1.getX() - p2.getX());

        if (p1.isBlasting() && !p1AttackHit && isInFront(p1, p2)) {
            if (distance <= p1.getBlastRadius()) {

                if (canBeHit(p2)) {
                    p2.takeDamage(20);
                    System.out.println("P1 BLAST hit P2 (-20 HP)");
                }

                p1AttackHit = true;
            }
        }

        if (p2.isBlasting() && !p2AttackHit && isInFront(p2, p1)) {
            if (distance <= p2.getBlastRadius()) {

                if (canBeHit(p1)) {
                    p1.takeDamage(20);
                    System.out.println("P2 BLAST hit P1 (-20 HP)");
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