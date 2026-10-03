import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    static int MAX_POINT;
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int BLOCK_SIZE = 25;
    static final int BLOCK_COUNT = (HEIGHT * WIDTH) / (BLOCK_SIZE * BLOCK_SIZE);
    private static int DELAY = 75;
    final int[] x = new int[BLOCK_COUNT + 1];
    final int[] y = new int[BLOCK_COUNT + 1];
    private int appleX;
    private int appleY;
    private char DIRECTION = 'R';
    private boolean newRecord = false;
    int applesEaten = 0;
    int bodySize = 6;
    Timer timer;
    Timer wait;
    Random random;
    JButton restart;
    JButton exit;
    JButton back;
    JLabel label;
    boolean isRunning = false;
    private final String musicPath = "「Creditless」Frieren__Beyond_Journey_s_End_OP___Opening_2「UHD_60FPS」(128k).wav";
    Music music;

    GamePanel() {
        music=new Music();
        label=new JLabel();
        getMAX();
        random = new Random();
        restart = new JButton("Restart");
        exit = new JButton("Exit");
        back = new JButton("Back");
        restart.addActionListener(this);
        exit.addActionListener(this);
        back.addActionListener(this);
        back.setVisible(false);
        restart.setVisible(false);
        exit.setVisible(false);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.addKeyListener(new MyKeyAdapter());
        this.add(restart);
        this.add(exit);
        this.add(back);
        this.add(label);
        gameStart();
    }

    public void getMAX(){
        try(BufferedReader br = new BufferedReader(new FileReader("MAX.txt"))) {
            
            MAX_POINT= Integer.parseInt(br.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }
    public void putMAX(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("MAX.txt"))) {
            
            bw.write(String.valueOf(MAX_POINT));
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void gameStart()  {
        newApple();
        isRunning=true;
        final int []i={3};
        ActionListener listener=new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setVisible(true);
                label.setFont(new Font(null,Font.BOLD,100));
                label.setForeground(Color.WHITE);
                label.setBounds(HEIGHT/2-100/2,WIDTH/2-100/2,100,100);
                label.setText(""+i[0]);
                i[0]--;
                if(i[0]<0){
                    label.setVisible(false);
                    wait.stop();
                    
                    music.getMusic();
                    timer = new Timer(DELAY, GamePanel.this);
                    isRunning = true;
                    timer.start();
                }
            }
            
        };
        wait=new Timer(1000, listener);
        wait.setInitialDelay(0);
        wait.start();
        
    }

    class Music {
        Clip clip;
        File file;
        Music() {
            file = new File(musicPath);
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        public void getMusic() {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
        public void stopMusic(){
            clip.stop();
        }

    }

    public void newApple() {
        boolean onSnake;
        do {
            appleX = random.nextInt((int) (WIDTH / BLOCK_SIZE)) * BLOCK_SIZE;
            appleY = random.nextInt((int) (HEIGHT / BLOCK_SIZE)) * BLOCK_SIZE;
            onSnake = false;
            for (int i = 0; i < bodySize; i++) {
                if (x[i] == appleX && y[i] == appleY) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (isRunning) {
            // for (int i = 1; i <= BLOCK_COUNT; i++) {
            //     g.drawLine(0, i * BLOCK_SIZE, WIDTH, i * BLOCK_SIZE);
            //     g.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, HEIGHT);
            // }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, BLOCK_SIZE, BLOCK_SIZE);

            for (int i = 0; i < bodySize; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], BLOCK_SIZE, BLOCK_SIZE);
                } else {
                    g.setColor(new Color(0x00AF00));
                    g.fillRect(x[i], y[i], BLOCK_SIZE, BLOCK_SIZE);
                }
            }
            String str = String.valueOf(applesEaten);
            g.setColor(Color.gray);
            g.setFont(new Font(null, Font.BOLD, 30));
            g.drawString(str, (WIDTH - g.getFontMetrics().stringWidth(str)) / 2, g.getFont().getSize());
        } else {
            String str = "GAME OVER";
            g.setColor(Color.WHITE);
            g.setFont(new Font(null, Font.BOLD, 50));
            g.drawString(str, (WIDTH - g.getFontMetrics().stringWidth(str)) / 2, HEIGHT / 2);
            if (newRecord) {

                str = "CONGRATULATIONS new record";
                g.setColor(Color.yellow);
                g.setFont(new Font(null, Font.BOLD, 18));
                g.drawString(str, (WIDTH - g.getFontMetrics().stringWidth(str)) / 2, 100);
            }
            str = String.valueOf(applesEaten);
            g.setFont(new Font(null, Font.BOLD, 35));
            g.drawString(str, (WIDTH - g.getFontMetrics().stringWidth(str)) / 2, 200);
        }
    }

    public void move() {
        for (int i = bodySize; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (DIRECTION) {
            case 'R':
                x[0] += BLOCK_SIZE;
                break;
            case 'L':
                x[0] -= BLOCK_SIZE;
                break;
            case 'U':
                y[0] -= BLOCK_SIZE;
                break;
            case 'D':
                y[0] += BLOCK_SIZE;
                break;
        }
    }

    public void gameOver() {
        isRunning = false;
        timer.stop();
        music.stopMusic();

        if (applesEaten > MAX_POINT) {
            MAX_POINT = applesEaten;
            newRecord = true;
            putMAX();
        }

        int buttonH = 40, buttonW = 150;
        restart.setVisible(true);
        restart.setFocusable(false);
        restart.setContentAreaFilled(false);
        restart.setFont(new Font(null, Font.BOLD, 25));
        restart.setForeground(new Color(0xCCCCCC));
        restart.setBounds(WIDTH / 4 - buttonW / 2, 400, buttonW, buttonH);

        exit.setVisible(true);
        exit.setFocusable(false);
        exit.setContentAreaFilled(false);
        exit.setFont(new Font(null, Font.BOLD, 25));
        exit.setForeground(new Color(0xCCCCCC));
        exit.setBounds(WIDTH * 3 / 4 - buttonW / 2, 400, buttonW, buttonH);

        back.setVisible(true);
        back.setFocusable(false);
        back.setContentAreaFilled(false);
        back.setFont(new Font(null, Font.BOLD, 25));
        back.setForeground(new Color(0xCCCCCC));
        back.setBounds(10, 550, buttonW, buttonH);
    }

    public void appleEaten() {
        if (x[0] == appleX && y[0] == appleY) {
            newApple();
            applesEaten++;
            bodySize++;
            if (applesEaten % 5 == 0 && DELAY > 20) {
                DELAY--;
                timer.setDelay(DELAY);
            }
        }
    }

    public void collision() {
        for (int i = bodySize; i > 0; i--) {
            if (x[i] == x[0] && y[i] == y[0]) {
                gameOver();
                return;
            }
        }
        if (x[0] < 0 || x[0] > (WIDTH - BLOCK_SIZE) || y[0] < 0 || y[0] > HEIGHT - BLOCK_SIZE) {
            gameOver();
        }
    }

    public void gameRestart() {

        bodySize = 6;
        applesEaten = 0;
        DIRECTION = 'R';
        newRecord = false;
        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        restart.setVisible(false);
        exit.setVisible(false);
        back.setVisible(false);
        DELAY = 75;
        gameStart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            appleEaten();
            collision();
        } else {
            if (e.getSource() == restart) {
                gameRestart();
            }
            if (e.getSource() == exit) {
                System.exit(0);
            }
            if(e.getSource()==back){
                Window window=SwingUtilities.getWindowAncestor(this);
                new HomeFrame();
                window.dispose();
                
            }
        }
        repaint();
    }

    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                case (KeyEvent.VK_LEFT):
                    if (DIRECTION != 'R') {
                        DIRECTION = 'L';
                    }
                    break;
                case KeyEvent.VK_W:
                case (KeyEvent.VK_UP):
                    if (DIRECTION != 'D') {
                        DIRECTION = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                case (KeyEvent.VK_DOWN):
                    if (DIRECTION != 'U') {
                        DIRECTION = 'D';
                    }
                    break;

                case KeyEvent.VK_D:
                case (KeyEvent.VK_RIGHT):
                    if (DIRECTION != 'L') {
                        DIRECTION = 'R';
                    }
                    break;
            }
        }
    }
}