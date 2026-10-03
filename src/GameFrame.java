import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
    GameFrame(){
        this.setIconImage(new ImageIcon("69669274_117981902896657_444710564619354112_n.jpg").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new GamePanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
}