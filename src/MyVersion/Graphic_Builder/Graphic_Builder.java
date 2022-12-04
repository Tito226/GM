package MyVersion.Graphic_Builder;
import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Graphic_Builder {
    static int heigh=1400;
    static int weight=500;
    public static void main(String[] args) {

        int weight=500;
        JFrame frame=new JFrame("Frame");
        frame.setSize(weight, heigh);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        Graphics g= frame.getGraphics();
        while(true){
        paint(0.1f,g);
        }

    }

    static float testFunc(float x){
        return x*x;
   }

   static void paint(float step, Graphics g){
        float i=0f;
        int y1=heigh/2;
        int x1=0;
       while(x1<weight && testFunc(i)<heigh){
           g.drawLine(x1,y1,x1+3,(int)testFunc(i));
           x1+=3;
           i+=step;
       }
   }

}
