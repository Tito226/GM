package MyVersion.Graphic_Builder;
import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Graphic_Builder {
    public static void main(String[] args) {
        JFrame frame=new JFrame("Frame");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        Graphics g= frame.getGraphics();
        while(true){

        }

    }

   float testFunc(float x){
        return x*x;
   }
}
