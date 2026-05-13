public class PowerUp
{
    // Position
    private int x;
    private int y;

    // Movement
    private int dx = 4;
    private int dy = 3;

    // Size

    private int size = 60;

    // Constructor

    public PowerUp(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    // Updates movement

    public void update()
    {
        x += dx;
        y += dy;

        // Bounce off left/right walls
        if (x <= 0 || x >= 1200 - size)
        {
            dx *= -1;
        }

        // Bounce off top/bottom walls
        if (y <= 0 || y >= 500 - size)
        {
            dy *= -1;
        }
    }

    // Collision

    public boolean collides(Player p)
    {
        return p.getX() < x + size &&
                p.getX() + 230 > x &&
                p.getY() < y + size &&
                p.getY() + 250 > y;
    }

    // Getters

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getSize()
    {
        return size;
    }
}
