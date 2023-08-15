import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener{

    public static final int WIDTH = 300, HEIGHT = 300;
    public static boolean running = false;
    public Thread thread;
    public Player player;
    public Section randomSection;
    private Random r;

    public Game() {
        addKeyListener(this);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        game.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        JFrame frame = new JFrame("Snake");
        frame.setSize(WIDTH+5, HEIGHT+25);
        frame.setLocation(500, 200);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.add(game);
        frame.setVisible(true);

        game.player = new Player(WIDTH/10 * 3, HEIGHT/10 * 3, 3, game);
        game.setFocusable(true);

        game.start();
    }

    public void finish() {
        System.out.println("Collision. Finished.");
        JOptionPane.showMessageDialog(null, "Collision. Game over.");
        stop();
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        r = new Random();
        randomSection = new Section(r.nextInt(WIDTH/10 - 1)*10, r.nextInt(HEIGHT/10 - 1)*10, 10);
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double AMOUNT_OF_TICKS = 6D;
        double ns = 1000000000 / AMOUNT_OF_TICKS;
        double delta = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                update();
                delta--;
            }
            render();
        }
        stop();
    }

    public  void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLUE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        player.draw(g);
        randomSection.draw(g);

        g.dispose();
        bs.show();
    }

    public void update(){
        player.update();
        if (randomSection == null) {
            randomSection = new Section(r.nextInt(WIDTH/10)*10, r.nextInt(HEIGHT/10)*10, 10);
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (player.direction != Player.MovingDirection.Down) {
                    player.direction = Player.MovingDirection.Up;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (player.direction != Player.MovingDirection.Up) {
                    player.direction = Player.MovingDirection.Down;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (player.direction != Player.MovingDirection.Right) {
                    player.direction = Player.MovingDirection.Left;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (player.direction != Player.MovingDirection.Left) {
                    player.direction = Player.MovingDirection.Right;
                }
                break;
        }

    }

    public void keyReleased(KeyEvent e) {
    }
}
