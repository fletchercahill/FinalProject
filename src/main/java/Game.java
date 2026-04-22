import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener{
        private GameView window;

        public static final int STATE_INSTR = 0;
        public static final int STATE_MAIN = 1;
        public static final int STATE_END = 2;
        public Player p1;
        public Player p2;
        public String winner;
        // Get a timer going later
    // Constructors
        public Game() {
            this.p1 = new Player();   // ← REQUIRED
            this.p2 = new Player();

            this.window = new GameView(this);

            // instead of b here we would have player
            //b = new Ball(BALL_START_X, BALL_START_Y, 0, 0, BALL_START_RADIUS, Color.BLUE);
            this.window = new GameView(this);
            //window = new KeyListenerDemoView(b);

            // The addKeyListener method attaches to this KeyListener object
            // an object that implements the KeyListener interface (i.e. supplies the keyTyped, keyReleased, and keyPressed methods)
            // By passsing the parameter "this"
            // we are saying that this specific KeyListenerDemo object
            // supplies its own KeyListener functionality (contains the 3 required KeyListener methods).
            //window.addKeyListener(this);
            // #4 Required for KeyListener

            window.addKeyListener(this);

            window.repaint();
        }

        //////////////////////////////////////////////////////////////
        /*
         * Methods all KeyListeners must supply
         */
        //////////////////////////////////////////////////////////////
        @Override
        public void keyTyped(KeyEvent e) {
            // Nothing required for this program.
            // However, as a KeyListener, this class must supply this method
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // Nothing required for this program.
            // However, as a KeyListener, this class must supply this method
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
                case KeyEvent.VK_F:
                    p1.punch();
                    break;
                case KeyEvent.VK_G:
                    p1.kick();
                    break;
            }

            window.repaint();
            new Thread(() -> {
                try {
                    Thread.sleep(150); // show kick/punch for 150 ms
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                p1.resetAction();
                window.repaint();
            }).start();

            p1.resetAction();
        }


    public static void main(String[] args) {
            Game g1 = new Game();

    }


}
