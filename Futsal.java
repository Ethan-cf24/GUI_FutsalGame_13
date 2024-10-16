import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Futsal extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int player1Score = 0, player2Score = 0;
    private int delay = 10;

    private Timer timer;

    // Paddle positions
    private int paddle1Y = 250, paddle2Y = 250;
    private int ballX = 350, ballY = 250, ballDirX = -1, ballDirY = -2;

    // Ball Image
    private Image ballImage;
    private int ballDiameter = 20;

    // Player Images
    private Image player1Image, player2Image;
    private int imageWidth = 30, imageHeight = 100;

    // Goal Images
    private Image goal1Image, goal2Image;

    // Background Image
    private Image backgroundImage;

    public Futsal() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

        // Load images (players, ball, background, goals)
        try {
            player1Image = ImageIO.read(new File("player1.png"))
                    .getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            player2Image = ImageIO.read(new File("player2.png"))
                    .getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ballImage = ImageIO.read(new File("ball.png"))
                    .getScaledInstance(ballDiameter, ballDiameter, Image.SCALE_SMOOTH);
            backgroundImage = ImageIO.read(new File("ground.jpg"));
            goal1Image = ImageIO.read(new File("goal1.png")).getScaledInstance(50, 150, Image.SCALE_SMOOTH);
            goal2Image = ImageIO.read(new File("goal2.png")).getScaledInstance(50, 150, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        // Draw the background image first
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 700, 600, null); // Draw background to fill the game area
        } else {
            // Fallback in case the background image fails to load
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 700, 600);
        }

        // Draw goals
        if (goal1Image != null) {
            g.drawImage(goal1Image, 0, 225, null); // Draw left goal
        }
        if (goal2Image != null) {
            g.drawImage(goal2Image, 650, 225, null); // Draw right goal
        }

        if (player1Image != null) {
            g.drawImage(player1Image, 10, paddle1Y, null); // Player 1 image on the left
        }
        if (player2Image != null) {
            g.drawImage(player2Image, 640, paddle2Y, null); // Player 2 image on the right
        }

        if (ballImage != null) {
            g.drawImage(ballImage, ballX, ballY, null);
        }

        // Scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 25));
        g.drawString("Player 1: " + player1Score, 50, 30);
        g.drawString("Player 2: " + player2Score, 500, 30);

        // Game over condition
        if (player1Score >= 3) {
            play = false;
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 50));
            g.drawString("Player 1 Wins", 200, 300);
            timer.stop();
        } else if (player2Score >= 3) {
            play = false;
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 50));
            g.drawString("Player 2 Wins", 200, 300);
            timer.stop();
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            // Ball movement
            ballX += ballDirX;
            ballY += ballDirY;

            // Ball bounce off top and bottom walls
            if (ballY <= 0 || ballY >= 580) {
                ballDirY = -ballDirY;
            }

            // Ball collision with paddles (detect based on image height)
            if (ballX <= 20 && ballY >= paddle1Y && ballY <= paddle1Y + imageHeight) {
                ballDirX = -ballDirX;
            }
            if (ballX >= 640 && ballY >= paddle2Y && ballY <= paddle2Y + imageHeight) { // Updated collision for player2
                ballDirX = -ballDirX;
            }

            // Check if the ball goes out of bounds (score update)
            if (ballX < 0) {
                player2Score++;
                resetBall();
            } else if (ballX > 700) {
                player1Score++;
                resetBall();
            }
        }
        repaint();
    }

    public void resetBall() {
        ballX = 250;
        ballY = 250;
        ballDirX = -ballDirX;
        ballDirY = -ballDirY;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (paddle1Y > 10) {
                paddle1Y -= 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (paddle1Y < 490) {
                paddle1Y += 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (paddle2Y > 10) {
                paddle2Y -= 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (paddle2Y < 490) {
                paddle2Y += 20;
            }
        }
        if (!play) {
            play = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Futsal"); // title = "Futsal"
        Futsal gamePanel = new Futsal();
        frame.setBounds(10, 10, 700, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setVisible(true);
    }
}
