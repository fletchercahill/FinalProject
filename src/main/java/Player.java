public class Player
{
    // Starting health of player
    private int health = 100;
    // Location of player
    private int positionX;
    private int positionY;

    private boolean facingRight = true;
    private int jumpStartY;
    // Length of time before player returns to idle state when doing an action
    private int actionTimer = 0;
    private boolean isJumping = false;

    private boolean blastHit = false;
    private boolean blasting = false;
    private int blastTimer = 0;
    private int blastRadius = 0;

    private boolean hasPowerUp = false;

    private static final int MAX_BLAST_TIME = 40;

    private String currentAction = "idle";

    private static final int MOVE_SPEED = 10;

    private double velocityX = 0;
    private double velocityY = 0;
    // Jumping mechanics
    private static final double gravity = 1.0;
    private static final double jumpStrength = -15;
    private static final int groundLevel = 500;

    public Player(int x, int y)
    {
        positionX = x;
        positionY = y;
    }

    public boolean hasBlastHit()
    {
        return blastHit;
    }

    public void setBlastHit(boolean value)
    {
        blastHit = value;
    }

    public void moveLeft()
    {
        if (positionX >= 0)
        {
            positionX -= MOVE_SPEED;
        }
    }

    public void moveRight()
    {
        if (positionX <= 1000)
        {
            positionX += MOVE_SPEED;
        }
    }

    public boolean isJumping()
    {
        return isJumping;
    }

    public void update()
    {
        // Vertical movement
        velocityY += gravity;
        positionY += velocityY;

        if (positionY >= groundLevel)
        {
            positionY = groundLevel;
            velocityY = 0;
            isJumping = false;
        }

        if (jumpStartY - positionY >= 100)
        {
            velocityY = 0;
        }
        // Horizontal movement
        positionX += velocityX;
        velocityX *= 0.8;
        if (Math.abs(velocityX) < 0.5)
        {
            velocityX = 0;
        }

        if (positionX < 0)
        {
            positionX = 0;
        }

        if (positionX > 1000)
        {
            positionX = 1000;
        }

        if (blasting)
        {
            blastTimer--;
            blastRadius += 20;

            if (blastTimer <= 0)
            {
                blasting = false;
                blastRadius = 0;
                currentAction = "idle";
                blastHit = false;
            }
        }

        if (actionTimer > 0)
        {
            actionTimer--;

            if (actionTimer == 0)
            {
                currentAction = "idle";
            }
        }
    }

    public void jump()
    {
        if (!isJumping)
        {
            velocityY = jumpStrength;
            isJumping = true;
            currentAction = "jump";
            jumpStartY = positionY;
        }
    }

    public void kick()
    {
        currentAction = "kick";
        actionTimer = 25;
    }

    public void givePowerUp()
    {
        hasPowerUp = true;
    }
   // Power up status
    public boolean hasPowerUp()
    {
        return hasPowerUp;
    }

    public void punch()
    {
        currentAction = "punch";
        actionTimer = 25;
    }

    public void dodge()
    {
        currentAction = "dodge";
        actionTimer = 25;
    }
    // Puts player back in idle state
    public void resetAction()
    {
        currentAction = "idle";
    }
    public void fireball() {
        currentAction = "fireball";
        actionTimer = 20;
    }

    public String getCurrentAction()
    {
        return currentAction;
    }

    public void takeDamage(int damage)
    {
        health -= damage;

        if (health < 0)
        {
            health = 0;
        }
    }
    // Blast attack
    public void blast()
    {
        if (hasPowerUp && !blasting)
        {
            blasting = true;
            blastTimer = MAX_BLAST_TIME;
            blastRadius = 0;
            currentAction = "blast";
            blastHit = false;

            hasPowerUp = false;
        }
    }

    public boolean isFacingRight()
    {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight)
    {
        this.facingRight = facingRight;
    }

    public boolean isBlasting()
    {
        return blasting;
    }

    public int getBlastRadius()
    {
        return blastRadius;
    }
    // Response to player being attacked
    public void applyKnockback(double force, boolean attackerFacingRight)
    {
        if (attackerFacingRight)
        {
            velocityX = force;
        }
        else
        {
            velocityX = -force;
        }

        velocityY = -6;
    }
    // Accessors
    public int getX()
    {
        return positionX;
    }

    public int getY()
    {
        return positionY;
    }

    public int getHealth()
    {
        return health;
    }
}