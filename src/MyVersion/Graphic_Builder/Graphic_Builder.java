package MyVersion.Graphic_Builder;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static MyVersion.Core.Core_Config.TEACH_ITERATIONS;
import static MyVersion.Graphic_Builder.Graphic_Builder_Config.*;
import static java.lang.Thread.sleep;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static MyVersion.Graphic_Builder.Graphic_Builder_Config.VALUE_MULTIPLIER;
public class Graphic_Builder {
    static int weight=1200;
    static int height=700;
    
    
    public static void main(String[] args) {//run it to test
        JFrame frame=new JFrame("Frame");
        frame.setSize(weight, height);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        Graphics g= frame.getGraphics();
        float[] floats=new float[990000];
        for (int i = 0; i < 990000; i++) {
            floats[i]=0.6f;
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
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                JFrame frame=new JFrame("Result");
                frame.setSize(weight, height);
                frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                Graphics g= frame.getGraphics();
                while(true){
                    paint(errors,g);
                    try {
                        sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
        }

    static float testFunc(float x){
        return x*x;
   }

   private static void paintTest(float step, Graphics g){
        float i=-25f;
        int y1=height;
        int x1=0;
        int down=200;
       while(x1<weight && testFunc(i)<height){
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
            for (int j = 0; j < di; j++) {
                if(errors[j+di*i]<0){//error = |error|
                    errors[j+di*i]=-errors[j+di*i];
                }
                buffer[j]=(int)(errors[j+di*i]* VALUE_MULTIPLIER);
            }
            int sum = 0;
            for(int bb:buffer){
                sum+=bb;
            }
            realValue.add(sum/di);
        }

        int x1=INDENT_X;
        int counter =0;
        int y1=height/2;
        int yLineIndent=20;
        while(x1<weight && realValue.get(counter)<height){
        	
            int y2=(height-realValue.get(counter))- INDENT_Y;
            g.drawLine(x1,y1,x1+INDENT_BETWEEN_GRAPHIC_COORDS,y2);
            g.setColor(Color.BLUE);//set color before paint
            g.drawLine(yLineIndent,height-INDENT_Y,yLineIndent,0);//draw y line
            g.drawLine(0,height-INDENT_Y,weight,height-INDENT_Y);//draw zero line
            g.drawLine(weight/2+40,height-INDENT_Y-20,weight/2+40,height-INDENT_Y+20);
            g.setColor(Color.BLACK);
            for(int i=0;i<10;i++) {
            	float v=((float)i)/10f;
            	g.drawString("0."+i,yLineIndent,height-(int)(v*VALUE_MULTIPLIER)- INDENT_Y);//draw error level
            }
            y1=y2;
            x1+=INDENT_BETWEEN_GRAPHIC_COORDS;
            counter++;
        }

    }

    int getIterations(){
     return TEACH_ITERATIONS;
    }

}
