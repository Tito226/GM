package MyVersion.Graphic_Builder;
import javax.swing.*;
import java.awt.*;

import static MyVersion.Core.Core_Config.TEACH_ITERATIONS;
import static MyVersion.Graphic_Builder.Graphic_Builder_Config.*;
import static java.lang.Thread.sleep;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Graphic_Builder {
    static int heigh=1400;
    static int weight=900;
    public static void main(String[] args) {//run it to test
        JFrame frame=new JFrame("Frame");
        frame.setSize(weight, heigh);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        Graphics g= frame.getGraphics();
        while(true){
        paintTest(0.1f,g);
            try {
                sleep(90);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


        public static void createGraphic(float[] errors){
            JFrame frame=new JFrame("Result");
            frame.setSize(weight, heigh);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
            Graphics g= frame.getGraphics();
            while(true){
                paint(errors,1,g);
                try {
                    sleep(90);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    static float testFunc(float x){
        return x*x;
   }

   private static void paintTest(float step, Graphics g){
        float i=-25f;
        int y1=heigh/2;
        int x1=0;
        int down=200;
       while(x1<weight && testFunc(i)<heigh){
           g.drawLine(x1,y1,x1+INDENT_BETWEEN_GRAPHIC_COORDS,(int)testFunc(i)+ INDENT_Y);
           y1=(int)testFunc(i)+down;
           x1+=INDENT_BETWEEN_GRAPHIC_COORDS;
           i+=step;
       }
   }
    private static void paint(float[] errors,int step, Graphics g){
        float i=-25f;
        int y1=heigh/2;
        int x1=0;

        for (int j = 0; j < errors.length; j++) {

        }
        while(x1<weight && testFunc(i)<heigh){
            g.drawLine(x1,y1,x1+INDENT_BETWEEN_GRAPHIC_COORDS,(int)testFunc(i)+ INDENT_Y);
            y1=(int)testFunc(i)+INDENT_Y;
            x1+=INDENT_BETWEEN_GRAPHIC_COORDS;
            i+=step;
        }
    }

    int getIterations(){
     return TEACH_ITERATIONS;
    }

}
