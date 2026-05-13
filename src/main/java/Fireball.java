import java.awt.Rectangle;

public class Fireball {
    // Coordinates
    private int x;
    private int y;

    private int speed;

    private boolean active = true;

    private Player owner;

    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;

    public Fireball(int startX,
                    int startY,
                    boolean movingRight,
                    Player owner) {

        x = startX;
        y = startY;

        this.owner = owner;
        // Chcks which direction the fireball is moving
        if (movingRight) {
            speed = 26;
        } else {
            speed = -26;
        }
    }
    // Shifts coordinate its X-Coord over by its speed
    public void update() {
        x += speed;

        if (x < -100 || x > 1300) {
            active = false;
        }
    }
    // All fireballs are basically rectangular imafges
    public Rectangle getBounds() {

        return new Rectangle(
                x + 15,
                y + 15,
                WIDTH - 30,
                HEIGHT - 30
        );
    }
    // Getters
    public Player getOwner() {
        return owner;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}