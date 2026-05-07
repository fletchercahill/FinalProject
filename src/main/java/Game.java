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

    public int effectX = 0;
    public int effectY = 0;
    public int effectTimer = 0;
    public int shakeTimer;
    public int shakeStrength;
    public String effectType = ""; // "punch", "kick", "blast"

    private static final int EFFECT_DURATION = 12;

    private static final int HIT_DURATION = 10; // frames
    private boolean p1AttackHit = false;
    private boolean p2AttackHit = false;

    private static final int PUNCH_RANGE = 160;
    private static final int KICK_RANGE = 190;

    private static final int PUNCH_DAMAGE = 6;
    private static final int KICK_DAMAGE = 5;

    private HashSet<Integer> keysPressed = new HashSet<>();

    // Power-up system
    private PowerUp powerUp;
    private int powerUpSpawnTimer = 0;
    private int nextSpawnTime = 0;
    private int powerUpCooldownTimer = 0;

    public Game() {
        p1 = new Player(100, 500);
        p2 = new Player(900, 500);

        powerUp = null;
        nextSpawnTime = getRandomSpawnTime();

        window = new GameView(this);
        window.addKeyListener(this);
        window.addMouseListener(this);
        window.repaint();
        // Continually updates the screen allowing for jump to work
    }

    private int getRandomSpawnTime() {
        int min = 20 * 60;
        int max = 30 * 60;
        return min + (int)(Math.random() * (max - min));
    }

    private int getCooldownTime() {
        int min = 10 * 60;
        int max = 20 * 60;
        return min + (int)(Math.random() * (max - min));
    }

    public boolean shouldShowPowerUpWarning() {
        int warningTime = 3 * 60; // last 3 seconds before spawn

        return gameState == STATE_FIGHT &&
                powerUp == null &&
                powerUpCooldownTimer == 0 &&
                nextSpawnTime - powerUpSpawnTimer <= warningTime &&
                powerUpSpawnTimer % 30 < 15; // blinking
    }

    public PowerUp getPowerUp() {
        return powerUp;
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
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Only trigger actions ONCE
        if (!keysPressed.contains(key)) {

            // ---- PLAYER 1 ----
            if (key == KeyEvent.VK_F && p1ActionCounter == 0) {
                p1.punch();
                p1ActionCounter = 30;
                p1AttackHit = false;
            }

            if (key == KeyEvent.VK_G && p1ActionCounter == 0) {
                p1.kick();
                p1ActionCounter = 30;
                p1AttackHit = false;
            }

            if (key == KeyEvent.VK_E && p1ActionCounter == 0) {
                p1.blast();
                p1ActionCounter = 30;
                p1AttackHit = false;
            }

            if (key == KeyEvent.VK_Q && p1ActionCounter == 0) {
                p1.dodge();
                p1ActionCounter = 30;
            }

            // ---- PLAYER 2 ----
            if (key == KeyEvent.VK_SHIFT && p2ActionCounter == 0) {
                p2.punch();
                p2ActionCounter = 30;
                p2AttackHit = false;
            }

            if (key == KeyEvent.VK_ENTER && p2ActionCounter == 0) {
                p2.kick();
                p2ActionCounter = 30;
                p2AttackHit = false;
            }

            if (key == KeyEvent.VK_SLASH && p2ActionCounter == 0) {
                p2.blast();
                p2ActionCounter = 30;
                p2AttackHit = false;
            }

            if (key == KeyEvent.VK_QUOTE && p2ActionCounter == 0) {
                p2.dodge();
                p2ActionCounter = 30;
            }
        }

        keysPressed.add(key);
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

        // Movement (continuous)
        if (keysPressed.contains(KeyEvent.VK_A)) p1.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_D)) p1.moveRight();
        if (keysPressed.contains(KeyEvent.VK_W)) p1.jump();

        if (keysPressed.contains(KeyEvent.VK_LEFT)) p2.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) p2.moveRight();
        if (keysPressed.contains(KeyEvent.VK_UP)) p2.jump();

        // Face each other
        if (p1.getX() < p2.getX()) {
            p1.setFacingRight(true);
            p2.setFacingRight(false);
        } else {
            p1.setFacingRight(false);
            p2.setFacingRight(true);
        }

        p1.update();
        p2.update();

        // Power-up system
        if (powerUp == null) {
            if (powerUpCooldownTimer > 0) {
                powerUpCooldownTimer--;
            } else {
                powerUpSpawnTimer++;

                if (powerUpSpawnTimer >= nextSpawnTime) {
                    int spawnX = (int)(Math.random() * 1000);
                    int spawnY = (int)(Math.random() * 400);

                    powerUp = new PowerUp(spawnX, spawnY);

                    powerUpSpawnTimer = 0;
                    nextSpawnTime = getRandomSpawnTime();
                }
            }
        }

        if (powerUp != null) {
            powerUp.update();

            if (powerUp.collides(p1)) {
                p1.givePowerUp();
                powerUp = null;
                powerUpCooldownTimer = getCooldownTime();
            }

            else if (powerUp.collides(p2)) {
                p2.givePowerUp();
                powerUp = null;
                powerUpCooldownTimer = getCooldownTime();
            }
        }

        checkAttacks();

        if (effectTimer > 0) effectTimer--;

        // Reset actions
        if (p1ActionCounter > 0 && --p1ActionCounter == 0) {
            p1.resetAction();
            p1AttackHit = false;
        }

        if (p2ActionCounter > 0 && --p2ActionCounter == 0) {
            p2.resetAction();
            p2AttackHit = false;
        }

        // Game over
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

        int xDistance = Math.abs(p1Center - p2Center);
        int yDistance = Math.abs(p1.getY() - p2.getY());

        boolean closeY = yDistance <= 120;

        // ---- P1 hits P2 ----
        if (!p1AttackHit && closeY && isInFront(p1, p2)) {

            if (p1.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE) {

                if (canBeHit(p2)){
                    p2.takeDamage(PUNCH_DAMAGE);

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = (p1.getY() + p2.getY()) / 2 + 100;
                    effectTimer = EFFECT_DURATION;
                    effectType = "punch";

                    // screen shake
                    shakeTimer = 6;
                    shakeStrength = 6;

                    // knockback
                    p2.applyKnockback(8, p1.isFacingRight());
                    System.out.println("P1 punched P2 (-" + PUNCH_DAMAGE + " HP)");
                } else {
                    System.out.println("P2 dodged!");
                }

                p1AttackHit = true;

            } else if (p1.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE) {

                if (canBeHit(p2)) {
                    p2.takeDamage(KICK_DAMAGE);

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = p2.getY() + 180;
                    effectTimer = EFFECT_DURATION;
                    effectType = "kick";

                    shakeTimer = 10;
                    shakeStrength = 10;

                    p2.applyKnockback(14, p1.isFacingRight());
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
                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = (p1.getY() + p2.getY()) / 2 + 100;
                    effectTimer = EFFECT_DURATION;
                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;

                    p1.applyKnockback(8, p2.isFacingRight());
                    p1.takeDamage(PUNCH_DAMAGE);
                    System.out.println("P2 punched P1 (-" + PUNCH_DAMAGE + " HP)");
                } else {
                    System.out.println("P1 dodged!");
                }

                p2AttackHit = true;

            } else if (p2.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE) {

                if (canBeHit(p1)) {
                    p2.takeDamage(KICK_DAMAGE);

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = p2.getY() + 180; // lower = leg impact
                    effectTimer = EFFECT_DURATION;
                    effectType = "kick";

// stronger shake
                    shakeTimer = 10;
                    shakeStrength = 10;

// stronger knockback
                    p2.applyKnockback(14, p1.isFacingRight());
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

        if (p1.isBlasting() && !p1.hasBlastHit() && isInFront(p1, p2)) {
            if (distance <= p1.getBlastRadius()) {

                if (canBeHit(p2)) {
                    p2.takeDamage(20);
                    System.out.println("P1 BLAST hit P2 (-20 HP)");
                }

                p1.setBlastHit(true);
            }
        }

        if (p2.isBlasting() && !p2.hasBlastHit() && isInFront(p2, p1)) {
            if (distance <= p2.getBlastRadius()) {

                if (canBeHit(p1)) {
                    p1.takeDamage(20);
                    System.out.println("P2 BLAST hit P1 (-20 HP)");
                }

                p2.setBlastHit(true);
            }
        }
    }

    public static void main(String[] args) {
        Game g1 = new Game();

        Timer clock = new Timer(SLEEP_TIME, g1);
        clock.start();
    }
}