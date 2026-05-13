import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.awt.Rectangle;
// Brawl 360 by Fletcher Cahill and Dylan Kothari
public class Game implements KeyListener, ActionListener, MouseListener
{
    // Game states
    public static final int STATE_WELCOME = 0;
    public static final int STATE_FIGHT = 1;
    public static final int STATE_END = 2;
    public static final int STATE_INSTRUCTIONS = 3;

    // Variable to keep track of current game state
    private int gameState = STATE_WELCOME;

    private GameView window;
    // Two players objects for 2 users
    private Player p1;
    private Player p2;

    private String winner;
    private boolean gameOver = false;


    // Game runs at roughly 60 FPS
    private static final int SLEEP_TIME = 16;

    private ArrayList<Fireball> fireballs = new ArrayList<>();

    private int p1ActionCounter = 0;
    private int p2ActionCounter = 0;

    private boolean p1AttackHit = false;
    private boolean p2AttackHit = false;

    private static final int PUNCH_RANGE = 160;
    private static final int KICK_RANGE = 190;

    private static final int PUNCH_DAMAGE = 6;
    private static final int KICK_DAMAGE = 5;
    private static final int FIREBALL_DAMAGE = 2;
    private static final int BLAST_DAMAGE = 20;

    private static final int DEFAULT_COUNTER = 30;
    private static final int CENTER_OFFSET = 115;
    private static final int EFFECT_DURATION = 12;

    private static final int TIME_CONVERSION_FACTOR = 60;
    private static final int WARNING_TIME = 3 * TIME_CONVERSION_FACTOR;
    // Variables for visual effects
    public int effectX = 0;
    public int effectY = 0;
    public int effectTimer = 0;
    public int shakeTimer = 0;
    public int shakeStrength = 0;
    public String effectType = "";

    private HashSet<Integer> keysPressed = new HashSet<>();

    // Parameters for the powerUp feature
    private PowerUp powerUp;
    private int powerUpSpawnTimer = 0;
    private int nextSpawnTime = 0;
    private int powerUpCooldownTimer = 0;

    // Parameters for the fireballs feature
    private int p1FireballCooldown = 0;
    private int p2FireballCooldown = 0;
    private static final int FIREBALL_COOLDOWN_TIME = 20;


    // =====================================================
    // Game constructor initializes the two player objects and the graphics window
    // =====================================================
    public Game()
    {
        p1 = new Player(100, 500);
        p2 = new Player(900, 500);

        powerUp = null;
        nextSpawnTime = getRandomPowerUpTime();

        window = new GameView(this);
        window.addKeyListener(this);
        window.addMouseListener(this);
        window.repaint();
    }

    // =====================================================
    // Returns a random time between 5-10 seconds
    // =====================================================
    private int getRandomPowerUpTime()
    {
        int min = 5 * TIME_CONVERSION_FACTOR;
        int max = 10 * TIME_CONVERSION_FACTOR;
        return min + (int)(Math.random() * (max - min));
    }


    // =====================================================
    // Determines whether powerUp warning should be displayed based on current game conditions
    // =====================================================
    public boolean shouldShowPowerUpWarning()
    {
        return gameState == STATE_FIGHT &&
                powerUp == null &&
                powerUpCooldownTimer == 0 &&
                nextSpawnTime - powerUpSpawnTimer <= WARNING_TIME &&
                powerUpSpawnTimer % 30 < 15;
    }

    public PowerUp getPowerUp()
    {
        return powerUp;
    }

    public int getGameState()
    {
        return gameState;
    }

    // =====================================================
    // Handle mouse click events based on click x,y position
    // =====================================================

    @Override
    public void mouseClicked(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        // =====================================================
        // If user clicks to Play Again
        // =====================================================

        if (gameState == STATE_END)
        {
            if (x >= 460 && x <= 740 &&
                    y >= 470 && y <= 550)
            {
                // Reset players
                p1 = new Player(100, 500);
                p2 = new Player(900, 500);

                // Clear fireballs
                fireballs.clear();

                // Reset power-up
                powerUp = null;

                // Reset game state
                gameOver = false;

                winner = "";

                gameState = STATE_FIGHT;

                window.repaint();

                return;
            }
        }

        // =====================================================
        // If users clicks to go back from the Instructions screen
        // =====================================================

        if (gameState == STATE_INSTRUCTIONS)
        {
            // BACK BUTTON AREA
            if (y >= 600)
            {
                gameState = STATE_WELCOME;

                window.repaint();

                return;
            }
        }

        // =====================================================
        // Handling user clicks for both buttons on the Welcome screen
        // =====================================================

        if (gameState == STATE_WELCOME)
        {
            // =====================================================
            // If user clicks the Fight button to start the game
            // =====================================================

            if (x >= 450 && x <= 750 &&
                    y >= 455 && y <= 705)
            {
                gameState = STATE_FIGHT;

                window.repaint();

                return;
            }

            // =====================================================
            // If user clicks the button to view Instructions
            // =====================================================

            if (x >= 475 && x <= 725 &&
                    y >= 730 && y <= 790)
            {
                gameState = STATE_INSTRUCTIONS;

                window.repaint();

                return;
            }
        }

        window.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }


    // =====================================================
    // Handle keyboard inputs based on key pressed
    // =====================================================
    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        if (!keysPressed.contains(key))
        {
            // Key for P1 to punch
            if (key == KeyEvent.VK_4 && p1ActionCounter == 0)
            {
                p1.punch();
                p1ActionReset();
            }

            // Key for P1 to kick
            if (key == KeyEvent.VK_5 && p1ActionCounter == 0)
            {
                p1.kick();
                p1ActionReset();
            }

            // Key for P1 to blast
            if (key == KeyEvent.VK_6 && p1ActionCounter == 0)
            {
                p1.blast();
                p1ActionReset();
            }

            // Key for P1 to dodge
            if (key == KeyEvent.VK_S && p1ActionCounter == 0)
            {
                p1.dodge();
                p1ActionCounter = DEFAULT_COUNTER;
            }

            // Key for P2 to punch
            if (key == KeyEvent.VK_P && p2ActionCounter == 0)
            {
                p2.punch();
                p2ActionReset();
            }

            // Key for P2 to kick
            if (key == KeyEvent.VK_OPEN_BRACKET && p2ActionCounter == 0)
            {
                p2.kick();
                p2ActionReset();
            }

            // Key for P2 to blast
            if (key == KeyEvent.VK_CLOSE_BRACKET && p2ActionCounter == 0)
            {
                p2.blast();
                p2ActionReset();
            }

            // Key for P2 to dodge
            if (key == KeyEvent.VK_DOWN && p2ActionCounter == 0)
            {
                p2.dodge();
                p2ActionCounter = DEFAULT_COUNTER;
            }

            // Key for P1 to fireball
            if (key == KeyEvent.VK_7)
            {
                if (p1FireballCooldown == 0)
                {
                    createFireball(p1);

                    p1FireballCooldown =
                            FIREBALL_COOLDOWN_TIME;
                }
            }
            // Key for P2 to fireball
            if (key == KeyEvent.VK_BACK_SLASH)
            {
                if (p2FireballCooldown == 0)
                {
                    createFireball(p2);

                    p2FireballCooldown =
                            FIREBALL_COOLDOWN_TIME;
                }
            }
        }

        keysPressed.add(key);
    }


    private void p1ActionReset()
    {
        p1ActionCounter = DEFAULT_COUNTER;
        p1AttackHit = false;
    }

    private void p2ActionReset()
    {
        p2ActionCounter = DEFAULT_COUNTER;
        p2AttackHit = false;
    }

    // =====================================================
    // Create a fireball for the player indicated in the parameter
    // =====================================================
    private void createFireball(Player player)
    {
        if (getDistance() >= 500)
        {
            player.fireball();

            int x;

            if (player.isFacingRight())
            {
                x = player.getX() + 180;
            }
            else
            {
                x = player.getX() - 20;
            }

            fireballs.add(
                    new Fireball(
                            x,
                            player.getY() + 110,
                            player.isFacingRight(),
                            player
                    )
            );
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        keysPressed.remove(e.getKeyCode());
    }

    // =====================================================
    // Determine if player can be hit based on current action
    // =====================================================
    private boolean canBeHit(Player target)
    {
        return !target.isJumping() && !target.getCurrentAction().equals("dodge");
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // =====================================================
    // Updates player position and game parameters
    // =====================================================
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (gameState != STATE_FIGHT)
        {
            window.repaint();
            return;
        }

        if (gameOver)
        {
            window.repaint();
            return;
        }

        // Updates player positions
        if (keysPressed.contains(KeyEvent.VK_A)) p1.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_D)) p1.moveRight();
        if (keysPressed.contains(KeyEvent.VK_W)) p1.jump();

        if (keysPressed.contains(KeyEvent.VK_LEFT)) p2.moveLeft();
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) p2.moveRight();
        if (keysPressed.contains(KeyEvent.VK_UP)) p2.jump();

        // Determines which way players are facing based on relative position
        if (p1.getX() < p2.getX())
        {
            p1.setFacingRight(true);
            p2.setFacingRight(false);
        }
        else
        {
            p1.setFacingRight(false);
            p2.setFacingRight(true);
        }

        p1.update();
        p2.update();

        updateFireballs();
        updatePowerUp();
        checkAttacks();

        // Update fireball parameters
        if (p1FireballCooldown > 0)
        {
            p1FireballCooldown--;
        }

        if (p2FireballCooldown > 0)
        {
            p2FireballCooldown--;
        }
        if (effectTimer > 0) effectTimer--;
        if (shakeTimer > 0) shakeTimer--;

        if (p1ActionCounter > 0 && --p1ActionCounter == 0)
        {
            p1.resetAction();
            p1AttackHit = false;
        }

        if (p2ActionCounter > 0 && --p2ActionCounter == 0)
        {
            p2.resetAction();
            p2AttackHit = false;
        }

        // Check if either player's health is down to zero, in which case, game is over
        if (!gameOver)
        {
            if (p1.getHealth() <= 0)
            {
                gameOver = true;
                winner = "Player 2 Wins!";
                gameState = STATE_END;
            }
            else if (p2.getHealth() <= 0)
            {
                gameOver = true;
                winner = "Player 1 Wins!";
                gameState = STATE_END;
            }
        }

        window.repaint();
    }

    public ArrayList<Fireball> getFireballs()
    {
        return fireballs;
    }

    // =====================================================
    // Controls the fireball feature
    // =====================================================
    private void updateFireballs()
    {
        for (int i = 0; i < fireballs.size(); i++)
        {
            Fireball f = fireballs.get(i);

            f.update();

            Rectangle hitbox = f.getBounds();

            Rectangle p1Box = new Rectangle(p1.getX(), p1.getY(), 230, 250);
            Rectangle p2Box = new Rectangle(p2.getX(), p2.getY(), 230, 250);

            // determine if P1 makes contact with a fireball from P2

            if (f.getOwner() != p1 && hitbox.intersects(p1Box))
            {
                if (canBeHit(p1))
                {
                    p1.takeDamage(FIREBALL_DAMAGE);
                    p1.applyKnockback(18, f.getOwner().isFacingRight());
                    f.deactivate();
                }
            }

            // determine if P2 makes contact with a fireball from P1
            if (f.getOwner() != p2 && hitbox.intersects(p2Box))
            {
                if (canBeHit(p2))
                {
                    p2.takeDamage(FIREBALL_DAMAGE);
                    p2.applyKnockback(18, f.getOwner().isFacingRight());
                    f.deactivate();
                }
            }
        }

        fireballs.removeIf(f -> !f.isActive());
    }

    private int getDistance()
    {
        return Math.abs(p1.getX() - p2.getX());
    }

    // =====================================================
    // Controls the powerUp feature
    // =====================================================
    private void updatePowerUp()
    {
        if (powerUp == null)
        {
            if (powerUpCooldownTimer > 0)
            {
                powerUpCooldownTimer--;
            }
            else
            {
                powerUpSpawnTimer++;

                if (powerUpSpawnTimer >= nextSpawnTime)
                {
                    int spawnX = (int)(Math.random() * 950);
                    int spawnY = (int)(Math.random() * 350 + 150);

                    powerUp = new PowerUp(spawnX, spawnY);

                    powerUpSpawnTimer = 0;
                    nextSpawnTime = getRandomPowerUpTime();
                }
            }
        }

        if (powerUp != null)
        {
            powerUp.update();

            // Handle player colliding with the powerUp element
            if (powerUp.collides(p1))
            {
                p1.givePowerUp();
                powerUp = null;
                powerUpCooldownTimer = getRandomPowerUpTime();
            }
            else if (powerUp.collides(p2))
            {
                p2.givePowerUp();
                powerUp = null;
                powerUpCooldownTimer = getRandomPowerUpTime();
            }
        }
    }

    // =====================================================
    // Determines if the other player in front of the attacking player
    // =====================================================
    private boolean isInFront(Player attacker, Player target)
    {
        int attackerCenter = attacker.getX() + CENTER_OFFSET;
        int targetCenter = target.getX() + CENTER_OFFSET;

        if (attacker.isFacingRight())
        {
            return targetCenter > attackerCenter;
        }
        else
        {
            return targetCenter < attackerCenter;
        }
    }

    // =====================================================
    // Handles the effect of an attack based on type and players' relative positions
    // =====================================================
    private void checkAttacks()
    {
        int p1Center = p1.getX() + CENTER_OFFSET;
        int p2Center = p2.getX() + CENTER_OFFSET;

        int xDistance = Math.abs(p1Center - p2Center);
        int yDistance = Math.abs(p1.getY() - p2.getY());

        boolean closeY = yDistance <= 120;

        if (!p1AttackHit && closeY && isInFront(p1, p2))
        {
            // if P1 is punched by P2
            if (p1.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE)
            {
                if (canBeHit(p2))
                {
                    p2.takeDamage(PUNCH_DAMAGE);
                    p2.applyKnockback(8, p1.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + CENTER_OFFSET;
                    effectY = (p1.getY() + p2.getY()) / 2 + 100;
                    effectTimer = EFFECT_DURATION;
                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;
                }

                p1AttackHit = true;
            }
            // if P1 is kicked by P2
            else if (p1.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE)
            {
                if (canBeHit(p2))
                {
                    p2.takeDamage(KICK_DAMAGE);
                    p2.applyKnockback(14, p1.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + CENTER_OFFSET;
                    effectY = p2.getY() + 180;
                    effectTimer = EFFECT_DURATION;
                    effectType = "kick";

                    shakeTimer = 10;
                    shakeStrength = 10;
                }

                p1AttackHit = true;
            }
        }

        if (!p2AttackHit && closeY && isInFront(p2, p1))
        {
            // if P2 is punched by P1
            if (p2.getCurrentAction().equals("punch") && xDistance <= PUNCH_RANGE)
            {
                if (canBeHit(p1))
                {
                    p1.takeDamage(PUNCH_DAMAGE);
                    p1.applyKnockback(8, p2.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + CENTER_OFFSET;
                    effectY = (p1.getY() + p2.getY()) / 2 + 100;
                    effectTimer = EFFECT_DURATION;
                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;
                }

                p2AttackHit = true;
            }
            // if P2 is kicked by P1
            else if (p2.getCurrentAction().equals("kick") && xDistance <= KICK_RANGE)
            {
                if (canBeHit(p1))
                {
                    p1.takeDamage(KICK_DAMAGE);
                    p1.applyKnockback(14, p2.isFacingRight());

                    effectX = (p1.getX() + p2.getX()) / 2 + CENTER_OFFSET;
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

    // =====================================================
    // Handles the effect of a player hit with a blast
    // =====================================================
    private void checkBlastDamage()
    {
        int p1Center = p1.getX() + CENTER_OFFSET;
        int p2Center = p2.getX() + CENTER_OFFSET;
        int distance = Math.abs(p1Center - p2Center);

        // if P2 is within P1's blast radius and can be hit, P2 takes damage
        if (p1.isBlasting() && !p1.hasBlastHit() && isInFront(p1, p2))
        {
            if (distance <= p1.getBlastRadius())
            {
                if (canBeHit(p2))
                {
                    p2.takeDamage(BLAST_DAMAGE);
                    p2.applyKnockback(20, p1.isFacingRight());
                }

                p1.setBlastHit(true);
            }
        }

        // if P1 is within P2's blast radius and can be hit, P1 takes damage
        if (p2.isBlasting() && !p2.hasBlastHit() && isInFront(p2, p1))
        {
            if (distance <= p2.getBlastRadius())
            {
                if (canBeHit(p1))
                {
                    p1.takeDamage(BLAST_DAMAGE);
                    p1.applyKnockback(20, p2.isFacingRight());
                }

                p2.setBlastHit(true);
            }
        }
    }
    // Assessors for instance variables
    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public static void main(String[] args)
    {
        Game g1 = new Game();

        Timer clock = new Timer(SLEEP_TIME, g1);
        clock.start();
    }
}