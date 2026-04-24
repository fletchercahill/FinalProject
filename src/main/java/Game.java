import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener {
    private GameView window;

    public Player p1;
    public Player p2;
    public String winner;

    public Game() {
        this.p1 = new Player();
        this.p2 = new Player();

        this.window = new GameView(this);
        window.addKeyListener(this);
        window.repaint();
        // Continually updates the screen allowing for jump to work
        new Thread(() -> {
            while (true) {
                p1.update();
                window.repaint();

                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {

            case KeyEvent.VK_A:
                p1.moveLeft();
                break;

            case KeyEvent.VK_D:
                p1.moveRight();
                break;

            case KeyEvent.VK_W:
                p1.jump();
                break;

            // Punch: Shift OR F
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_F:
                p1.punch();
                break;

            // Kick: G OR Enter
            case KeyEvent.VK_G:
            case KeyEvent.VK_ENTER:
                p1.kick();
                break;
        }

        window.repaint();

        if (p1.getCurrentAction().equals("punch") || p1.getCurrentAction().equals("kick")) {
            new Thread(() -> {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }


                p1.resetAction();
                window.repaint();
            }).start();
        }
    }

    public static void main(String[] args) {
        Game g1 = new Game();
    }
}
