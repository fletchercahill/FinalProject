public class PowerUp {

    private int x, y;
    private int dx = 4, dy = 3;
    private int size = 60;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += dx;
        y += dy;

        if (x <= 0 || x >= 1200 - size) {
            dx *= -1;
        }

        if (y <= 0 || y >= 500 - size) {
            dy *= -1;
        }
    }

    public boolean collides(Player p) {
        return p.getX() < x + size &&
                p.getX() + 230 > x &&
                p.getY() < y + size &&
                p.getY() + 250 > y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}