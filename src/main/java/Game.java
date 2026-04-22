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
            // The keyCode lets you know which key was pressed
           // switch(e.getKeyCode())
            //{
                /*
                case KeyEvent.VK_LEFT:
                    b.shiftX(-STEP_SIZE, 0, KeyListenerDemoView.SCREEN_WIDTH);
                    break;
                case KeyEvent.VK_RIGHT:
                    b.shiftX(STEP_SIZE, 0, KeyListenerDemoView.SCREEN_WIDTH);
                    break;
                case KeyEvent.VK_UP:
                    int topOfPane = window.getInsets().top;
                    b.shiftY(-STEP_SIZE, topOfPane, KeyListenerDemoView.SCREEN_HEIGHT);
                    break;
                case KeyEvent.VK_DOWN:
                    b.shiftY(STEP_SIZE, 0, KeyListenerDemoView.SCREEN_HEIGHT);
                    break;

                case KeyEvent.VK_Z:
                    int newX = (int)(Math.random()*500);
                    int newY = (int)(Math.random()*500);
                    b = new Ball(newX, newY, 0, 0, BALL_START_RADIUS, Color.BLUE);

            */
// Helloa
           // }
            //window.repaint();
        //}
    }

    public static void main(String[] args) {
            Game g1 = new Game();

    }


}
