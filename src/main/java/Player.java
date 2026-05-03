public class Player {
    private int health = 50;
    private int positionX;
    private int positionY;
    private boolean facingRight = true;
    private static final int PUNCH_DAMAGE = 4;
    private static final int KICK_DAMAGE = 5;
    private static final int BLAST_DAMAGE = 20;
    private int jumpStartY;
    private int actionTimer = 0;
    private boolean isJumping = false;
    private boolean blastUsed = false;
    private boolean blasting = false;
    private int blastTimer = 0;
    private int blastRadius = 0;
    private static final int MAX_BLAST_TIME = 40;
    private String name;

    private String currentAction = "idle";

    private static final int MOVE_SPEED = 10;
    private static final int JUMP_HEIGHT = 150;

    private double velocityX = 0;
    private double velocityY = 0;
    private static final double GRAVITY = 1.0;
    private static final double JUMP_STRENGTH = -15; // negative = upward
    private static final int GROUND_LEVEL = 500;

    public Player(int x, int y) {
        positionX = x;
        positionY = y;
    }

    public void moveLeft() {
        if (positionX >= 0){
            positionX -= MOVE_SPEED;
            currentAction = "idle";
        }
    }
    public boolean isJumping() {
        return isJumping;
    }
    public void update() {
        // apply gravity
        velocityY += GRAVITY;

        // move player
        positionY += velocityY;

        // check if player hits ground
        if (positionY >= GROUND_LEVEL) {
            positionY = GROUND_LEVEL;
            velocityY = 0;
            isJumping = false;
        }
        // When the character reaches a height of 100, they stop accelerating upwards
        if (jumpStartY - positionY >= 100) {
            velocityY = 0; // stop going up
        }
        // apply horizontal knockback movement
        positionX += velocityX;

        // friction (slows knockback over time)
        velocityX *= 0.8;

        // stop tiny sliding
        if (Math.abs(velocityX) < 0.5) {
            velocityX = 0;
        }
        if (blasting) {
            blastTimer--;
            blastRadius += 20; // how fast it expands

            if (blastTimer <= 0) {
                blasting = false;
                blastRadius = 0;
                currentAction = "idle";
            }
        }// handle action timer
        if (actionTimer > 0) {
            actionTimer--;
            if (actionTimer == 0) {
                currentAction = "idle";
            }
        }
    }

    public void moveRight() {
        if (positionX <= 1000){
            positionX += MOVE_SPEED;
            currentAction = "idle";
        }
    }

    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
            currentAction = "jump";
            jumpStartY = positionY;
        }
    }


    public void kick() {
        currentAction = "kick";
        actionTimer = 25;
        System.out.println("Player kicks!");
    }

    public void punch() {
        currentAction = "punch";
        actionTimer = 25;
        System.out.println("Player punches!");
    }


    public void dodge() {
        currentAction = "dodge";
        System.out.println("Player dodges!");
    }

    public void resetAction() {
        currentAction = "idle";
    }

    public String getCurrentAction() {
        return currentAction;
    }

    // ---- Damage ----
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    // ---- Visual walking effect ----

    public void blast() {
        if (!blastUsed) {
            System.out.println("Player uses special blast!");
            blasting = true;
            blastTimer = MAX_BLAST_TIME;
            blastRadius = 0;
            currentAction = "blast";
            blastUsed = true;
        } else {
            System.out.println("Blast already used!");
        }
    }
    // Checks if a player is facing right to determine if it needs to be reflected
    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
    public boolean isBlasting() { return blasting; }
    public int getBlastRadius() { return blastRadius; }
    public void applyKnockback(double force, boolean attackerFacingRight) {

        // push away from attacker
        if (attackerFacingRight) {
            velocityX = force;   // attacker on left → push right
        } else {
            velocityX = -force;  // attacker on right → push left
        }

        // small upward bump (makes hits feel better)
        velocityY = -6;
    }



    public int getX() { return positionX; }
    public int getY() { return positionY; }
    public int getHealth() { return health; }
}