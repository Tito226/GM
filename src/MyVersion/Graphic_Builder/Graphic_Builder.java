package MyVersion.Graphic_Builder;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
        float[] floats=new float[990000];
        for (int i = 0; i < 990000; i++) {
            floats[i]=0.4f;
        }

        while(true){
        paint(floats,g);
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
                paint(errors,g);
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
    private static void paint(float[] errors, Graphics g){

        ArrayList<Integer> realValue=new ArrayList<>();
        int di = (errors.length/weight)*INDENT_BETWEEN_GRAPHIC_COORDS;//THIS
        for (int i = 0; i < weight/INDENT_BETWEEN_GRAPHIC_COORDS; i++) {//AND THIS CAN WORK INCORRECT, NEED TO FIX
            int[] buffer=new int[di];
            int forDebug=0;
            for (int j = 0; j < di; j++) {
                buffer[j]=(int)(errors[j+di*i]*1000);
                forDebug=j+di*i;
            }
            int fg=forDebug;
            int sum = 0;
            for(int bb:buffer){
                sum+=bb;
            }
            realValue.add(sum/di);
        }
        int iter=0;
        int x1=0;
        int counter =0;
        while(x1<weight && realValue.get(iter)<heigh){
            int y1=heigh/2;
            g.drawLine(x1,y1,x1+INDENT_BETWEEN_GRAPHIC_COORDS,realValue.get(counter)+ INDENT_Y);
            y1=realValue.get(counter)+INDENT_Y;
            x1+=INDENT_BETWEEN_GRAPHIC_COORDS;
            counter++;
        }
        if(realValue.get(iter)<heigh){
            System.out.println("to big values (Graphic_Builder(92))");
        }
    }

    int getIterations(){
     return TEACH_ITERATIONS;
    }

}
