import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class HomeFrame extends JFrame implements ActionListener{
    JFrame frame;
    JPanel panel;
    JButton newGame;
    JButton highScore;
    JButton exit;
    JLabel lable;
    static int MAX_POINT;
    HomeFrame(){
        lable=new JLabel();
        lable.setVisible(false);
        newGame=new JButton("newGame");
        newGame.setFocusable(false);
        highScore=new JButton("highScore");
        highScore.setFocusable(false);
        exit=new JButton("exit");
        exit.setFocusable(false);
        panel=new JPanel();
        panel.setLayout(new GridLayout(3,1));
        panel.setBounds(150, 300, 250, 200);
        this.setLayout(null);
        this.setIconImage(new ImageIcon("69669274_117981902896657_444710564619354112_n.jpg").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.add(newGame);
        panel.add(highScore);
        panel.add(exit);
        newGame.addActionListener(this);
        highScore.addActionListener(this);
        exit.addActionListener(this);
        this.add(panel);
        this.add(lable);
        this.setBounds(0, 0, 600, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==newGame){
            new GameFrame();
            this.dispose();
        }
        else if(e.getSource()==highScore){
            if(lable.isVisible()){
                lable.setVisible(false);
            }
            else
            showScore();
        }
        else if(e.getSource()==exit){
            System.exit(0);
        }
    }
    public void showScore(){
        frame=new JFrame(); 
        getMAX();
        
        lable.setVisible(true);
        lable.setForeground(Color.BLACK);
        lable.setBounds(125,200,400,25);
        lable.setFont(new Font(null,Font.BOLD,25));
        lable.setText("Your current score is: "+MAX_POINT);
        lable.setSize(lable.getPreferredSize());
        lable.setLayout(null);
        


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
}
