import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    GamePanel panel;
    GameFrame(){
        panel = new GamePanel();
        add(panel);
        setTitle("Car Race By Hazem Ayman Ibrahim 202004410");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.darkGray.darker());
        setVisible(true);
        setSize(1000,800);
        setLocationRelativeTo(null);

    }
}
