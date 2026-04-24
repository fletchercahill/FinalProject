public class Player {
    private int health = 100;
    private int positionX = 100;
    private int positionY = 500;
    private boolean isJumping = false;
    private boolean blastUsed = false;
    private String name;

    private String currentAction = "idle";

    private static final int MOVE_SPEED = 10;
    private static final int JUMP_HEIGHT = 150;

    public void moveLeft() {
        positionX -= MOVE_SPEED;
        currentAction = "idle";
    }

    public void moveRight() {
        positionX += MOVE_SPEED;
        currentAction = "idle";
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            currentAction = "jump";

            positionY -= JUMP_HEIGHT;
            positionY += JUMP_HEIGHT;

            isJumping = false;
            currentAction = "idle";
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