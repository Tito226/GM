package MyVersion.Frame;



import MyVersion.Core.Network;
import MyVersion.Core.Network_Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import static MyVersion.Frame.Cell.startEnergy;
import static MyVersion.Frame.GM2_CONFIG.CELL_SIZE;
import static MyVersion.Frame.GM2_CONFIG.PAINT_MODE;
import static MyVersion.Frame.PaintThread.paintMode;
import static MyVersion.Frame.World.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

class PaintThread extends Thread{
    inPaintThread paint1;
    inPaintThread paint2;
    inPaintThread paint3;
   static byte paintMode=PAINT_MODE;
  public   void rept(){
      if(!pause){
        pause=true;
        for (int i = 0; i < width ; i++) {
            for (int j = 0; j < World.height; j++) {
                if(cells[i][j]!=null)
                    cells[i][j].change=true;
            }
        }
        pause=false;
  }else {
        for (int i = 0; i < width ; i++) {
            for (int j = 0; j < World.height; j++) {
                if(cells[i][j]!=null)
                    cells[i][j].change=true;
            }
        }
    }
    }
    public PaintThread(){

    }
    class inPaintThread extends Thread {
        int type;
        Graphics g;

        public inPaintThread(Graphics g, int type) {
            this.g = g;
            this.type = type;
        }

        @Override
        public void run() {
            while (true) {
                if (0 == 0){
                    switch (type) {
                        case 1 -> {
                            for (int i = 0; i < width / 2; i++) {
                                for (int j = 0; j < height; j++) {
                                    if (cells[i][j] != null) {
                                        if (cells[i][j].secCell == null && cells[i][j].partCell == null) {
                                            if(cells[i][j].isChanged()){
                                            g.setColor(cells[i][j].getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);}
                                        } else if (cells[i][j].secCell != null && cells[i][j].partCell == null) {
                                            if(cells[i][j].isChanged()){
                                            g.setColor(cells[i][j].secCell.getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);}
                                        }
                                        if (cells[i][j] != null && cells[i][j].partCell != null) {
                                            if(cells[i][j].isChanged()){
                                            g.setColor(cells[i][j].partCell.getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);}
                                        }
                                    }
                                }
                            }
                        }
                        case 2 -> {
                            for (int i = width / 2; i < width; i++) {
                                for (int j = 0; j < height; j++) {
                                    if (cells[i][j] != null) {
                                        if (cells[i][j].secCell == null && cells[i][j].partCell == null) {
                                            if(cells[i][j].isChanged()){
                                            g.setColor(cells[i][j].getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);}
                                        } else if (cells[i][j].secCell != null && cells[i][j].partCell == null) {
                                            if(cells[i][j].isChanged()){
                                            g.setColor(cells[i][j].secCell.getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);}
                                        }
                                        if (cells[i][j] != null && cells[i][j].partCell != null) {
                                            if(cells[i][j].isChanged()){
                                            g.setColor(cells[i][j].partCell.getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);}
                                        }
                                    }
                                }
                            }

                        }
                        case 3->{
                            for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                if (cells[i][j] != null) {
                                    if (cells[i][j].secCell == null && cells[i][j].partCell == null) {
                                            g.setColor(cells[i][j].getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                                    } else if (cells[i][j].secCell != null && cells[i][j].partCell == null) {
                                            g.setColor(cells[i][j].secCell.getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                                    }
                                    if (cells[i][j] != null && cells[i][j].partCell != null) {
                                            g.setColor(cells[i][j].partCell.getColor());
                                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                                    }
                                }
                            }
                        }
                            g.setColor(Color.BLACK);
                            g.drawString("Restarts: " + Restarts, 7, 9);
                        }
                    }
                    g.setColor(Color.BLACK);
                    g.drawString("Restarts: " + Restarts, 7, 9);
            }
         }
        }

    }

    @Override
    public void run() {
                paint(world.getGraphics());
    }

    public void paint(Graphics g) {
        long startTime = System.currentTimeMillis();
        if(paintMode==2) {
            if (paint1 == null) {

                paint1 = new inPaintThread(g, 1);
                paint2 = new inPaintThread(g, 2);
                paint1.start();
                paint2.start();
            }
        }else if (paintMode==1){
            if (paint1 == null) {
                paint1 = new inPaintThread(g, 3);
                paint1.start();
            }
        }
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 100) {
            System.out.println("Paint took " + (endTime - startTime) + " milliseconds");
        }

    }
}

public class World extends JPanel implements Runnable  {

    static   FileInputStream fileInputStream;
    static    ObjectInputStream objectInputStream;
    public Network relative;
    public static int cellls=0;
    public static int cellSize=CELL_SIZE;
    public static int width;
    public static int height;
    public static int sunny=1;
    static Cell[][] cells;
    public static boolean pause=false;
    static PaintThread paintThread;
    public World(int width,int height) throws IOException {
        Network_Teacher network_teacher=new Network_Teacher();
        relative=network_teacher.mainy();
        this.height=height;
        this.width=width;
        cells=new Cell[width][height];
    }
    static World world;
  static   Thread wor;


public static Cell[][] getCells(){
    return cells;
}


    public static void main(String[] args) throws  IOException {
        int width=1600;
        int height=1000;
        JFrame frame = new JFrame("GM");
        frame.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if(!pause){
                pause=true;
                for (int i = 0; i < World.width; i++) {
                    for (int j = 0; j < World.height; j++) {
                        if(cells[i][j]!=null)
                        cells[i][j].change=true;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                pause=false;
                }else{
                    for (int i = 0; i < World.width; i++) {
                        for (int j = 0; j < World.height; j++) {
                            if(cells[i][j]!=null)
                                cells[i][j].change=true;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        JButton pauseButton = new JButton("Pause");
        JButton dePauseButton = new JButton("Continue");
        pauseButton.addActionListener(new PauseListener());
        dePauseButton.addActionListener(new ContinueListener());
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(2, 1));
        controls.add(pauseButton);
        controls.add(dePauseButton);
        JPanel controls2 = new JPanel();

        JButton button = new JButton("save");
        button.addActionListener(new FileSaveListener());
        JButton button2 = new JButton("load");
        button2.addActionListener(new FileOpenListener());
        controls2.add(button);
        controls2.add(button2);


         world=new World(width/cellSize-5,height/cellSize-20);
        frame.add(controls2,BorderLayout.NORTH);
        frame.add(controls, BorderLayout.EAST); // справа будет панель с управлением
        frame.add(world, BorderLayout.CENTER);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
         wor =new Thread(world);
         paintThread= new PaintThread();
        paintThread.start();
        wor.start();


    }


public class StepThread extends Thread{

   void setXYAndStart(int x,int y){
       this.x=x;
       this.y=y;
    }
    int x;
    int y;


    @Override
    public void run() {
        cells[x][y].secCell.step();
    }
}
   StepThread stepThread=new StepThread();
 public static  NormCell topLifeTimeCell=null;
 public static NormCell topMultipliesCell=null;
    static  byte countt2=0;
   public static int Restarts=0;
   public  static int lastRestarts=0;
   @Override
    public void run() {
        int bestLifeTime=0;
        int bestMultiplies=-1;
        int thisBestLifeTime=0;
        long cellsNum=0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width ; i++) {
                cells[i][j]=new Cell();
                if (cells[i][j]!=null) {
                    cells[i][j].setX(i);
                    cells[i][j].setY(j);
                }

            }
        }


        for (int i = 0; i < 10; i++) {
            Random r =new Random();
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(relative));
        }





        byte countt=0;


    while(true) {

    boolean isStep=false;
    countt2++;
    Thread.yield();
    long startTime = System.currentTimeMillis();
        if(!pause){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //step{*************************************************************************
            //******************************************************************************
            int NULL_CELLS=0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height ; j++) {
                    if( cells[i][j].secCell!=null){
                    cells[i][j].secCell.stepN=false;
                    }
                    cells[i][j].testCell();
                }
            }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
            if (cells[i][j].secCell==null){
                NULL_CELLS++;
            }

                if(cells[i][j].secCell!=null){
                    cells[i][j].secCell.setX(i);
                    cells[i][j].secCell.setY(j);
                    if(cells[i][j].secCell.lifeTime>thisBestLifeTime){
                        thisBestLifeTime=cells[i][j].secCell.lifeTime;
                    }
                    if(!cells[i][j].secCell.stepN){
                        cells[i][j].secCell.stepN=true;
                        cells[i][j].secCell.step();
                       isStep=true;

                    }

                }

            }
            //   Thread.yield();
        }

            if(!isStep){
                System.out.println("shit");
            }
            //***************************************************************
            //***************************************************************}step

            //reset field organic
         if (NULL_CELLS==width*height){
         for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            cells[j][i].organic=startEnergy;
          }
         }

            //Summon cells
             for (int i = 0; i < 10; i++) {
                 Random r =new Random();
                 cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(relative));
             }

    Restarts++;
    //+++++++++++++++++++
    paintThread.rept();
    Thread.yield();

    lastRestarts++;
    System.out.println("Restarted");
    System.out.println("best life time: "+ bestLifeTime);
    System.out.println("this Best Life Time: " +thisBestLifeTime);
    thisBestLifeTime=0;
         }


          // long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
           // usedBytes/1048576>500
           if (countt%5000==0){
             //  System.out.println("Garbage collector started");
               // System.gc();
                countt=0;
           }
        }

        countt++;
   long endTime = System.currentTimeMillis();
   if(endTime - startTime>100){
   System.out.println("Main took " + (endTime - startTime) + " milliseconds");}
    }

    }





}
