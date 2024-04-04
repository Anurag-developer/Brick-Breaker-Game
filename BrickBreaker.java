import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreaker extends JPanel implements KeyListener, ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_HEIGHT = 20;
    private final int BALL_SIZE = 20;
    private final int BRICK_ROWS = 5;
    private final int BRICK_COLS = 10;
    private final int BRICK_WIDTH = 50;
    private final int BRICK_HEIGHT = 20;
    private int paddleX, ballX, ballY, ballXDir = 1, ballYDir = -1;
    private boolean[][] bricks;
    private Timer timer;

    public BrickBreaker() {
        bricks = new boolean[BRICK_ROWS][BRICK_COLS];
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j] = true;
            }
        }
        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT - PADDLE_HEIGHT - BALL_SIZE;
        timer = new Timer(10, this);
        timer.start();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw paddle
        g.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
        // Draw ball
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
        // Draw bricks
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j]) {
                    g.fillRect(j * BRICK_WIDTH, i * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        ballX += ballXDir;
        ballY += ballYDir;
        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            ballXDir *= -1;
        }
        if (ballY <= 0) {
            ballYDir *= -1;
        }
        if (ballY >= HEIGHT - BALL_SIZE) {
            // Game over
            timer.stop();
        }
        if (ballY + BALL_SIZE >= HEIGHT - PADDLE_HEIGHT &&
                ballX + BALL_SIZE >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballYDir *= -1;
        }
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j]) {
                    Rectangle brickRect = new Rectangle(j * BRICK_WIDTH, i * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
                    Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = false;
                        ballYDir *= -1;
                    }
                }
            }
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 20;
        }
        if (key == KeyEvent.VK_RIGHT && paddleX < WIDTH - PADDLE_WIDTH) {
            paddleX += 20;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brick Breaker");
        BrickBreaker game = new BrickBreaker();
        frame.getContentPane().add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}