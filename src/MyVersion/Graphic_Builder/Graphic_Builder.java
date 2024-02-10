package MyVersion.Graphic_Builder;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import static MyVersion.Core.Core_Config.TEACH_ITERATIONS;
import static MyVersion.Graphic_Builder.Graphic_Builder_Config.*;
import static java.lang.Thread.sleep;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static MyVersion.Graphic_Builder.Graphic_Builder_Config.VALUE_MULTIPLIER;
public class Graphic_Builder {
    static int width=1200;
    static int height=700;
    static int multiplier=VALUE_MULTIPLIER;
    static boolean clear=false;
    
    public static void main(String[] args) {//run it to test
    	Graphic_Builder builder=new Graphic_Builder();
        JFrame frame=new JFrame("Frame");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(3);//EXIT_ON_CLOSE
        JPanel controls = new JPanel();
        
        JButton button = new JButton("+");
        JButton button2 = new JButton("-");
        
        controls.add(button2);
        controls.add(button);
        button.addActionListener((java.awt.event.ActionEvent e) -> {
        	multiplier+=100;
        	clear=true;
        });
        button2.addActionListener((java.awt.event.ActionEvent e) -> {
        	multiplier-=100;
        	clear=true;	
        });
        frame.add(controls,BorderLayout.EAST);
        frame.setVisible(true);
        Graphics g= frame.getGraphics();
        float[] floats=new float[990000];
        for (int i = 0; i < 990000; i++) {
            floats[i]=0.43f;
        }

        while(true){
        	builder.paint(floats,g);
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
            	Graphic_Builder builder=new Graphic_Builder();
                JFrame frame=new JFrame("Result");
                frame.setSize(width, height);
                frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                JPanel controls = new JPanel();
                
                JButton button = new JButton("+");
                JButton button2 = new JButton("-");
                
                controls.add(button2);
                controls.add(button);
                frame.add(controls,BorderLayout.EAST);
                frame.setVisible(true);
                Graphics g= frame.getGraphics();
                
                button.addActionListener((java.awt.event.ActionEvent e) -> {
                	multiplier+=600;
                	clear=true;
                });
                button2.addActionListener((java.awt.event.ActionEvent e) -> {
                	multiplier-=600;
                	clear=true;	
                });
                while(true){
                	builder.paint(errors,g);
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
       while(x1<width && testFunc(i)<height){
           g.drawLine(x1,y1,x1+INDENT_BETWEEN_GRAPHIC_COORDS,(int)testFunc(i)+ INDENT_Y);
           y1=(int)testFunc(i)+down;
           x1+=INDENT_BETWEEN_GRAPHIC_COORDS;
           i+=step;
       }
   }
   	//BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);;
    private void paint(float[] errors, Graphics g){	
    	//Graphics bufferGraphics = buff.getGraphics();
    	if(clear) {
    		clear=false;
    		g.clearRect(0, 0, width, height);
    	}
    	
        ArrayList<Integer> realValue=new ArrayList<>();
        int di = (errors.length/width)*INDENT_BETWEEN_GRAPHIC_COORDS;//THIS
        for (int i = 0; i < width/INDENT_BETWEEN_GRAPHIC_COORDS; i++) {//AND THIS CAN WORK INCORRECT, NEED TO FIX
            int[] buffer=new int[di];
            for (int j = 0; j < di; j++) {
                if(errors[j+di*i]<0){//error = |error|
                    errors[j+di*i]=-errors[j+di*i];
                }
                buffer[j]=(int)(errors[j+di*i]* multiplier);
            }
            int sum = 0;
            for(int bb:buffer){
                sum+=bb;
            }
            realValue.add(sum/di);
        }
       
        //bufferGraphics.clearRect(0, di, di, di)
        int x1=INDENT_X;
        int counter =0;
        int y1=height/2;
        int yLineIndent=20;
        double realGraphicHeight=height-INDENT_Y;
        while(x1<width ){
        	
            int y2=(int) (realGraphicHeight-realValue.get(counter));
            g.setColor(Color.black);
            g.drawLine(x1,y1,x1+INDENT_BETWEEN_GRAPHIC_COORDS,y2);
            g.setColor(Color.BLUE);//set color before paint
            g.drawLine(yLineIndent,(int) realGraphicHeight,yLineIndent,0);//draw y line
            g.drawLine(0,(int)realGraphicHeight,width,(int)realGraphicHeight);//draw zero line
            //g.drawLine(width/2+40,height-INDENT_Y-20,width/2+40,height-INDENT_Y+20);
            g.setColor(Color.BLACK);
            int dill=25;
            for(int i=0;i<=dill;i++) { 
            	double v=realGraphicHeight/dill*i;//делим рабочую область(там где график будет.потому((height-INDENT_Y)) на dill частей
            	double result=(realGraphicHeight-v)/multiplier;//(error=realValue/multiplier) 
            	g.setColor(Color.black);
            	g.drawString(String.format("%.3f",result),yLineIndent,(int) v);//draw error 
            	g.setColor(Color.BLUE);
            	g.drawLine(INDENT_X,(int) (realGraphicHeight-v),width,(int) (realGraphicHeight-v));
            }
            	//g.drawString("0."+i,yLineIndent,height-(int)(v*multiplier)- INDENT_Y);//draw error level
            y1=y2;
            x1+=INDENT_BETWEEN_GRAPHIC_COORDS;
            counter++;
        }
        //g.drawImage(buff, 0, 0,this);
        //bufferGraphics.dispose();
    }



	

}
