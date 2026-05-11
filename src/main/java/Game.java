import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;

public class Game implements KeyListener, ActionListener, MouseListener
{

    // CHECK WHETHER ALL OF THE PUBLIC VARIABLES NEED TO BE PUBLIC

    // =====================================================
    // GAME STATE MANAGEMENT
    // =====================================================

    public static final int stateWelcome = 0;
    public static final int stateFight = 1;
    public static final int stateEnd = 2;
    public static final int stateInstructions = 3;
    private int gameState = stateWelcome;
    public boolean gameOver = false;

    // =====================================================
    // CORE GAME OBJECTS
    // =====================================================

    private GameView window;
    public Player p1;
    public Player p2;

    // =====================================================
    // PLAYER ACTIONS
    // =====================================================

    private int p1ActionCounter = 0;
    private int p2ActionCounter = 0;
    private boolean p1AttackHit = false;
    private boolean p2AttackHit = false;
    private static final int punchRange = 160;
    private static final int kickRange = 190;
    private static final int punchDamage = 6;
    private static final int kickDamage = 5;
    private static final int defaultCounter = 30;
    private static final int centerOffset = 115;

    // =====================================================
    // GAME CONTROLS
    // =====================================================

    public String winner;
    private static final int sleepTime = 16;
    public int effectX = 0;
    public int effectY = 0;
    public int effectTimer = 0;
    public int shakeTimer = 0;
    public int shakeStrength = 0;
    public String effectType = "";
    private static final int effectDuration = 12;
    private static final int timeConversionFactor = 60;
    private HashSet<Integer> keysPressed = new HashSet<>();

    // =====================================================
    // POWER-UP SYSTEM
    // =====================================================

    private PowerUp powerUp;
    private int powerUpSpawnTimer = 0;
    private int nextSpawnTime = 0;
    private int powerUpCooldownTimer = 0;

    // Game constructor to create two players and graphics window
    public Game()
    {

        p1 = new Player(100, 500);
        p2 = new Player(900, 500);

        powerUp = null;

        nextSpawnTime = getRandomTime();

        window = new GameView(this);

        window.addKeyListener(this);
        window.addMouseListener(this);

        window.repaint();
    }

    // Returns a random time between 5-10 seconds used for spawn and cooldown times
    private int getRandomTime()
    {

        int min = 5 * timeConversionFactor;
        int max = 10 * timeConversionFactor;

        return min + (int)(Math.random() * (max - min));
    }

    // to be removed
    /*
    private int getCooldownTime()
    {

        int min = 5 * timeConversionFactor;
        int max = 10 * timeConversionFactor;

        return min + (int)(Math.random() * (max - min));
    }
     */

    // Determines if the Power Up advance indicator should be displayed based on current game state
    public boolean shouldShowPowerUpWarning()
    {

        int warningTime = 3 * timeConversionFactor;

        return gameState == stateFight &&
                powerUp == null &&
                powerUpCooldownTimer == 0 &&
                nextSpawnTime - powerUpSpawnTimer <= warningTime &&
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
    // MOUSE INPUT
    // =====================================================

    // Handles mouse click events
    @Override
    public void mouseClicked(MouseEvent e)
    {

        int x = e.getX();
        int y = e.getY();

        // =====================================================
        // HANDLE CLICKS ON INSTRUCTIONS SCREEN
        // =====================================================

        if (gameState == stateInstructions)
        {

            // BACK BUTTON AREA

            // this is inconsistent, is not checking the boundary of the back button unlike the rest of the buttons

            if (y >= 600)
            {

                gameState = stateWelcome;

                window.repaint();

                return;
            }
        }

        // =====================================================
        // HANDLE CLICKS ON WELCOME SCREEN
        // =====================================================

        if (gameState == stateWelcome)
        {

            // FIGHT BUTTON
            if (x >= 475 && x <= 725 &&
                    y >= 500 && y <= 560)
            {

                gameState = stateFight;

                window.repaint();

                return;
            }

            // INSTRUCTIONS BUTTON
            if (x >= 475 && x <= 725 &&
                    y >= 610 && y <= 670)
            {

                gameState = stateInstructions;

                window.repaint();

                return;
            }
        }

        window.repaint();
    }

    // =====================================================
    // KEYBOARD INPUT
    // =====================================================

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    // Handle keyboard click events
    @Override
    public void keyPressed(KeyEvent e)
    {

        int key = e.getKeyCode();

        if (!keysPressed.contains(key))
        {

            // =====================================================
            // RYU ACTIONS
            // =====================================================

            if (key == KeyEvent.VK_4 && p1ActionCounter == 0)
            {

                p1.punch();
                p1ActionCounter = defaultCounter;
                p1AttackHit = false;
            }

            if (key == KeyEvent.VK_5 && p1ActionCounter == 0)
            {

                p1.kick();
                p1ActionCounter = defaultCounter;
                p1AttackHit = false;
            }

            // BLAST
            if (key == KeyEvent.VK_6 && p1ActionCounter == 0)
            {

                p1.blast();
                p1ActionCounter = defaultCounter;
                p1AttackHit = false;
            }

            // FIREBALL
            if (key == KeyEvent.VK_7 && p1ActionCounter == 0)
            {

                // FIREBALL GOES HERE
            }

            // DODGE
            if (key == KeyEvent.VK_S && p1ActionCounter == 0)
            {

                p1.dodge();
                p1ActionCounter = defaultCounter;
            }

            // =====================================================
            // KEN ACTIONS
            // =====================================================

            if (key == KeyEvent.VK_P && p2ActionCounter == 0)
            {

                p2.punch();
                p2ActionCounter = defaultCounter;
                p2AttackHit = false;
            }

            if (key == KeyEvent.VK_OPEN_BRACKET && p2ActionCounter == 0)
            {

                p2.kick();
                p2ActionCounter = defaultCounter;
                p2AttackHit = false;
            }

            // BLAST
            if (key == KeyEvent.VK_CLOSE_BRACKET &&
                    p2ActionCounter == 0)
            {

                p2.blast();
                p2ActionCounter = defaultCounter;
                p2AttackHit = false;
            }

            // FIREBALL
            if (key == KeyEvent.VK_BACK_SLASH && p2ActionCounter == 0)
            {

                // FIREBALL GOES HERE
            }

            // DODGE
            if (key == KeyEvent.VK_DOWN &&  p2ActionCounter == 0)
            {

                p2.dodge();
                p2ActionCounter = defaultCounter;
            }
        }

        keysPressed.add(key);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

        keysPressed.remove(e.getKeyCode());
    }

    // =====================================================
    // EMPTY MOUSE METHODS
    // =====================================================

    @Override public void mousePressed(MouseEvent e)
    {
    }
    @Override public void mouseReleased(MouseEvent e)
    {
    }
    @Override public void mouseEntered(MouseEvent e)
    {
    }
    @Override public void mouseExited(MouseEvent e)
    {
    }

    // =====================================================
    // HIT VALIDATION
    // =====================================================
    // Determine if a player can take a hit or is immune from jumping or ducking
    private boolean canBeHit(Player target)
    {

        return !target.isJumping() && !target.getCurrentAction().equals("dodge");
    }

    // =====================================================
    // GAME LOOP
    // =====================================================
    // Controls main game action, player movement
    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (gameState != stateFight)
        {
            window.repaint();
            return;
        }

        if (gameOver)
        {
            window.repaint();
            return;
        }

        // =====================================================
        // RYU MOVEMENT
        // =====================================================

        if (keysPressed.contains(KeyEvent.VK_A))
            p1.moveLeft();

        if (keysPressed.contains(KeyEvent.VK_D))
            p1.moveRight();

        if (keysPressed.contains(KeyEvent.VK_W))
            p1.jump();

        // =====================================================
        // KEN MOVEMENT
        // =====================================================

        if (keysPressed.contains(KeyEvent.VK_LEFT))
            p2.moveLeft();

        if (keysPressed.contains(KeyEvent.VK_RIGHT))
            p2.moveRight();

        if (keysPressed.contains(KeyEvent.VK_UP))
            p2.jump();

        // =====================================================
        // FACE EACH OTHER
        // =====================================================

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

        updatePowerUp();

        checkAttacks();

        if (effectTimer > 0)
            effectTimer--;

        if (shakeTimer > 0)
            shakeTimer--;

        // =====================================================
        // RESET ACTIONS
        // =====================================================

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

        // =====================================================
        // GAME OVER
        // =====================================================

        if (!gameOver)
        {

            if (p1.getHealth() <= 0)
            {

                gameOver = true;

                winner = "Player 2 Wins!";

                gameState = stateEnd;
            }

            else if (p2.getHealth() <= 0)
            {

                gameOver = true;

                winner = "Player 1 Wins!";

                gameState = stateEnd;
            }
        }

        window.repaint();
    }

    // =====================================================
    // POWER-UP UPDATE
    // =====================================================
    // Manages the power-up feature
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

                    nextSpawnTime = getRandomTime();
                }
            }
        }

        if (powerUp != null)
        {

            powerUp.update();

            // PLAYER 1 PICKS UP
            if (powerUp.collides(p1))
            {

                p1.givePowerUp();

                powerUp = null;

                powerUpCooldownTimer =
                        getRandomTime();
            }

            // PLAYER 2 PICKS UP
            else if (powerUp.collides(p2))
            {

                p2.givePowerUp();

                powerUp = null;

                powerUpCooldownTimer =
                        getRandomTime();
            }
        }
    }

    // =====================================================
    // ATTACK DIRECTION
    // =====================================================

    private boolean isInFront(Player attacker,
                              Player target)
    {

        int attackerCenter =
                attacker.getX() + centerOffset;

        int targetCenter =
                target.getX() + centerOffset;

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
    // ATTACK COLLISIONS
    // =====================================================

    private void checkAttacks()
    {

        int p1Center = p1.getX() + 115;
        int p2Center = p2.getX() + 115;

        int xDistance =
                Math.abs(p1Center - p2Center);

        int yDistance =
                Math.abs(p1.getY() - p2.getY());

        boolean closeY = yDistance <= 120;

        // =====================================================
        // PLAYER 1 HITS PLAYER 2
        // =====================================================

        if (!p1AttackHit &&
                closeY &&
                isInFront(p1, p2))
        {

            // PUNCH
            if (p1.getCurrentAction().equals("punch") &&
                    xDistance <= punchRange)
            {

                if (canBeHit(p2))
                {

                    p2.takeDamage(punchDamage);

                    p2.applyKnockback(
                            8,
                            p1.isFacingRight()
                    );

                    effectX =
                            (p1.getX() + p2.getX()) / 2 + 115;

                    effectY =
                            (p1.getY() + p2.getY()) / 2 + 100;

                    effectTimer = effectDuration;

                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;
                }

                p1AttackHit = true;
            }

            // KICK
            else if (p1.getCurrentAction().equals("kick") &&
                    xDistance <= kickRange)
            {

                if (canBeHit(p2))
                {

                    p2.takeDamage(kickDamage);

                    p2.applyKnockback(
                            14,
                            p1.isFacingRight()
                    );

                    effectX =
                            (p1.getX() + p2.getX()) / 2 + 115;

                    effectY =
                            p2.getY() + 180;

                    effectTimer = effectDuration;

                    effectType = "kick";

                    shakeTimer = 10;
                    shakeStrength = 10;
                }

                p1AttackHit = true;
            }
        }

        // =====================================================
        // PLAYER 2 HITS PLAYER 1
        // =====================================================

        if (!p2AttackHit &&
                closeY &&
                isInFront(p2, p1))
        {

            // PUNCH
            if (p2.getCurrentAction().equals("punch") &&
                    xDistance <= punchRange)
            {

                if (canBeHit(p1))
                {

                    p1.takeDamage(punchDamage);

                    p1.applyKnockback(
                            8,
                            p2.isFacingRight()
                    );

                    effectX =
                            (p1.getX() + p2.getX()) / 2 + 115;

                    effectY =
                            (p1.getY() + p2.getY()) / 2 + 100;

                    effectTimer = effectDuration;

                    effectType = "punch";

                    shakeTimer = 6;
                    shakeStrength = 6;
                }

                p2AttackHit = true;
            }

            // KICK
            else if (p2.getCurrentAction().equals("kick") &&
                    xDistance <= kickRange)
            {

                if (canBeHit(p1))
                {

                    p1.takeDamage(kickDamage);

                    p1.applyKnockback(
                            14,
                            p2.isFacingRight()
                    );

                    effectX =
                            (p1.getX() + p2.getX()) / 2 + 115;

                    effectY =
                            p1.getY() + 180;

                    effectTimer = effectDuration;

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
    // BLAST DAMAGE
    // =====================================================

    private void checkBlastDamage()
    {

        int p1Center = p1.getX() + 115;
        int p2Center = p2.getX() + 115;

        int distance =
                Math.abs(p1Center - p2Center);

        // PLAYER 1 BLAST
        if (p1.isBlasting() &&
                !p1.hasBlastHit() &&
                isInFront(p1, p2))
        {

            if (distance <= p1.getBlastRadius())
            {

                if (canBeHit(p2))
                {

                    p2.takeDamage(20);

                    p2.applyKnockback(
                            20,
                            p1.isFacingRight()
                    );
                }

                p1.setBlastHit(true);
            }
        }

        // PLAYER 2 BLAST
        if (p2.isBlasting() &&
                !p2.hasBlastHit() &&
                isInFront(p2, p1))
        {

            if (distance <= p2.getBlastRadius())
            {

                if (canBeHit(p1))
                {

                    p1.takeDamage(20);

                    p1.applyKnockback(
                            20,
                            p2.isFacingRight()
                    );
                }

                p2.setBlastHit(true);
            }
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args)
    {

        Game g1 = new Game();

        Timer clock =
                new Timer(sleepTime, g1);

        clock.start();
    }
}
