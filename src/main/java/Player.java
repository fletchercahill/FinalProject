public class Player {
    private int health = 100;
    private int positionX = 100;
    private int positionY = 500; // ground level
    private boolean isJumping = false;
    private boolean blastUsed = false;
    private String name;
    private String currentAction = "idle";

    private static final int MOVE_SPEED = 10;
    private static final int JUMP_HEIGHT = 150;

    public void moveLeft() {
        positionX -= MOVE_SPEED;
    }

    public void moveRight() {
        positionX += MOVE_SPEED;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            positionY -= JUMP_HEIGHT;

            // simple "gravity" reset (you'll improve this later)
            positionY += JUMP_HEIGHT;
            isJumping = false;
        }
    }

    public void kick() {
        currentAction = "kick";
        System.out.println(name + " kicks!");
    }

    public void punch() {
        currentAction = "punch";
        System.out.println(name + " punches!");
    }

    public void useBlast() {
        if (!blastUsed) {
            System.out.println(name + " uses special blast!");
            blastUsed = true;
        } else {
            System.out.println("Blast already used!");
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }
    public void resetAction() {
        currentAction = "idle";
    }

    public String getCurrentAction() {
        return currentAction;
    }

    // Getters (important for drawing later)
    public int getX() { return positionX; }
    public int getY() { return positionY; }
    public int getHealth() { return health; }
}