public class Player {
    private int health = 100;
    private int positionX = 100;
    private int positionY = 500;
    private int jumpStartY;
    private boolean isJumping = false;
    private boolean blastUsed = false;
    private String name;

    private String currentAction = "idle";

    private static final int MOVE_SPEED = 20;
    private static final int JUMP_HEIGHT = 150;

    private double velocityY = 0;
    private static final double GRAVITY = 1.0;
    private static final double JUMP_STRENGTH = -15; // negative = upward
    private static final int GROUND_LEVEL = 500;

    public void moveLeft() {
        positionX -= MOVE_SPEED;
        currentAction = "idle";
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
    }

    public void moveRight() {
        positionX += MOVE_SPEED;
        currentAction = "idle";
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
        System.out.println("Player kicks!");
    }

    public void punch() {
        currentAction = "punch";
        System.out.println("Player punches!");
    }

    public void resetAction() {
        currentAction = "idle";
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void useBlast() {
        if (!blastUsed) {
            System.out.println("Player uses special blast!");
            blastUsed = true;
        } else {
            System.out.println("Blast already used!");
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public int getX() { return positionX; }
    public int getY() { return positionY; }
    public int getHealth() { return health; }
}