import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.awt.Rectangle;

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
    public boolean gameOver = false;

    private static final int SLEEP_TIME = 16;
    private ArrayList<Fireball> fireballs = new ArrayList<>();
    private int p1ActionCounter = 0;
    private int p2ActionCounter = 0;

    public int effectX = 0;
    public int effectY = 0;
    public int effectTimer = 0;
    public int shakeTimer = 0;
    public int shakeStrength = 0;
    public String effectType = "";

    private static final int EFFECT_DURATION = 12;

    private boolean p1AttackHit = false;
    private boolean p2AttackHit = false;

    private static final int PUNCH_RANGE = 160;
    private static final int KICK_RANGE = 190;

    private static final int PUNCH_DAMAGE = 6;
    private static final int KICK_DAMAGE = 5;
    private static final int FIREBALL_DAMAGE = 2;
    private static final int BLAST_DAMAGE = 20;


    private HashSet<Integer> keysPressed = new HashSet<>();

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
    }

    private int getRandomSpawnTime() {
        int min = 5 * 60;
        int max = 10 * 60;
        return min + (int)(Math.random() * (max - min));
    }

    private int getCooldownTime() {
        int min = 5 * 60;
        int max = 10 * 60;
        return min + (int)(Math.random() * (max - min));
    }

    public boolean shouldShowPowerUpWarning() {
        int warningTime = 3 * 60;

        return gameState == STATE_FIGHT &&
                powerUp == null &&
                powerUpCooldownTimer == 0 &&
                nextSpawnTime - powerUpSpawnTimer <= warningTime &&
                powerUpSpawnTimer % 30 < 15;
    }

    public PowerUp getPowerUp() {
        return powerUp;
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
        } else if (gameState == STATE_INSTRUCTIONS) {
            if (x >= 430 && x <= 770 && y >= 690 && y <= 780) {
                gameState = STATE_WELCOME;
            }
        }

        window.repaint();
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!keysPressed.contains(key)) {

            // Player 1 actions
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

            if (key == KeyEvent.VK_S && p1ActionCounter == 0) {
                p1.dodge();
                p1ActionCounter = 30;
            }

            // Player 2 actions
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

            if (key == KeyEvent.VK_DOWN && p2ActionCounter == 0) {
                p2.dodge();
                p2ActionCounter = 30;
            }
            if (key == KeyEvent.VK_1) {

                if (getDistance() >= 500) {

                    p1.fireball();

                    int x;

                    if (p1.isFacingRight()) {
                        x = p1.getX() + 180;
                    } else {
                        x = p1.getX() - 20;
                    }

                    fireballs.add(
                            new Fireball(
                                    x,
                                    p1.getY() + 110,
                                    p1.isFacingRight(),
                                    p1
                            )
                    );
                }
            }
            if (key == KeyEvent.VK_BACK_SLASH) {

                if (getDistance() >= 500) {

                    p2.fireball();

                    int x;

                    if (p2.isFacingRight()) {
                        x = p2.getX() + 180;
                    } else {
                        x = p2.getX() - 20;
                    }

                    fireballs.add(
                            new Fireball(
                                    x,
                                    p2.getY() + 110,
                                    p2.isFacingRight(),
                                    p2
                            )
                    );
                }
            }
        }

        keysPressed.add(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
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

        if (gameOver) {
            window.repaint();
            return;
        }

        // Movement
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
        updateFireballs();


        updatePowerUp();

        checkAttacks();

        if (effectTimer > 0) effectTimer--;
        if (shakeTimer > 0) shakeTimer--;

        if (p1ActionCounter > 0 && --p1ActionCounter == 0) {
            p1.resetAction();
            p1AttackHit = false;
        }

        if (p2ActionCounter > 0 && --p2ActionCounter == 0) {
            p2.resetAction();
            p2AttackHit = false;
        }

        if (!gameOver) {
            if (p1.getHealth() <= 0) {
                gameOver = true;
                winner = "Player 2 Wins!";
                gameState = STATE_END;
            } else if (p2.getHealth() <= 0) {
                gameOver = true;
                winner = "Player 1 Wins!";
                gameState = STATE_END;
            }
        }

        window.repaint();
    }
    public ArrayList<Fireball> getFireballs() {
        return fireballs;
    }
    private void updateFireballs() {

        for (int i = 0; i < fireballs.size(); i++) {

            Fireball f = fireballs.get(i);

            f.update();

            Rectangle hitbox = f.getBounds();

            Rectangle p1Box = new Rectangle(
                    p1.getX(),
                    p1.getY(),
                    230,
                    250
            );

            Rectangle p2Box = new Rectangle(
                    p2.getX(),
                    p2.getY(),
                    230,
                    250
            );

            // HIT PLAYER 1

            if (f.getOwner() != p1 &&
                    hitbox.intersects(p1Box)) {

                if (canBeHit(p1)) {

                    p1.takeDamage(FIREBALL_DAMAGE);

                    p1.applyKnockback(
                            18,
                            f.getOwner().isFacingRight()
                    );

                    f.deactivate();
                }
            }

            // HIT PLAYER 2

            if (f.getOwner() != p2 &&
                    hitbox.intersects(p2Box)) {

                if (canBeHit(p2)) {

                    p2.takeDamage(FIREBALL_DAMAGE);

                    p2.applyKnockback(
                            18,
                            f.getOwner().isFacingRight()
                    );

                    f.deactivate();
                }
            }
        }

        fireballs.removeIf(f -> !f.isActive());
    }
    private int getDistance() {
        return Math.abs(p1.getX() - p2.getX());
    }

    private void updatePowerUp() {
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
            } else if (powerUp.collides(p2)) {
                p2.givePowerUp();
                powerUp = null;
                powerUpCooldownTimer = getCooldownTime();
            }
        }
    }

    private boolean isInFront(Player attacker, Player target) {
        int attackerCenter = attacker.getX() + 115;
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

        // P1 hits P2
        if (!p1AttackHit && closeY && isInFront(p1, p2)) {

            if (p1.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE) {
                if (canBeHit(p2)) {
                    p2.takeDamage(PUNCH_DAMAGE);
                    p2.applyKnockback(8, p1.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = (p1.getY() + p2.getY()) / 2 + 100;
                    effectTimer = EFFECT_DURATION;
                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;
                }

                p1AttackHit = true;

            } else if (p1.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE) {
                if (canBeHit(p2)) {
                    p2.takeDamage(KICK_DAMAGE);
                    p2.applyKnockback(14, p1.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = p2.getY() + 180;
                    effectTimer = EFFECT_DURATION;
                    effectType = "kick";

                    shakeTimer = 10;
                    shakeStrength = 10;
                }

                p1AttackHit = true;
            }
        }

        // P2 hits P1
        if (!p2AttackHit && closeY && isInFront(p2, p1)) {

            if (p2.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE) {
                if (canBeHit(p1)) {
                    p1.takeDamage(PUNCH_DAMAGE);
                    p1.applyKnockback(8, p2.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = (p1.getY() + p2.getY()) / 2 + 100;
                    effectTimer = EFFECT_DURATION;
                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;
                }

                p2AttackHit = true;

            } else if (p2.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE) {
                if (canBeHit(p1)) {
                    p1.takeDamage(KICK_DAMAGE);
                    p1.applyKnockback(14, p2.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + 115;
                    effectY = p1.getY() + 180;
                    effectTimer = EFFECT_DURATION;
                    effectType = "kick";

                    shakeTimer = 10;
                    shakeStrength = 10;
                }

                p2AttackHit = true;
            }
        }

        checkBlastDamage();
    }

    private void checkBlastDamage() {
        int p1Center = p1.getX() + 115;
        int p2Center = p2.getX() + 115;
        int distance = Math.abs(p1Center - p2Center);

        if (p1.isBlasting() && !p1.hasBlastHit() && isInFront(p1, p2)) {
            if (distance <= p1.getBlastRadius()) {
                if (canBeHit(p2)) {
                    p2.takeDamage(BLAST_DAMAGE);
                    p2.applyKnockback(20, p1.isFacingRight());
                }

                p1.setBlastHit(true);
            }
        }

        if (p2.isBlasting() && !p2.hasBlastHit() && isInFront(p2, p1)) {
            if (distance <= p2.getBlastRadius()) {
                if (canBeHit(p1)) {
                    p1.takeDamage(BLAST_DAMAGE);
                    p1.applyKnockback(20, p2.isFacingRight());
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