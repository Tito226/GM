package MyVersion;


import MyVersion.NEAT.Genome;
import MyVersion.NEAT.Pool;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import static MyVersion.Cell.startEnergy;
import static MyVersion.PaintThread.paintMode;
import static MyVersion.World.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

class PaintThread extends Thread{
    inPaintThread paint1;
    inPaintThread paint2;
    inPaintThread paint3;
   static byte paintMode=12;
  public   void rept(){
        pause=false;
        for (int i = 0; i <World.width ; i++) {
            for (int j = 0; j < World.height; j++) {
                if(cells[i][j]!=null)
                    cells[i][j].change=true;
            }
        }

        pause=true;
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
                        }}
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
        if(paintMode==1) {
            if (paint1 != null) {
            } else {
                paint1 = new inPaintThread(g, 1);
                paint2 = new inPaintThread(g, 2);
                paint1.start();
                paint2.start();
            }
        }else{
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

    //  static {
     //   try {
      //    fileInputStream = new FileInputStream("D:\\save.txt");
       // } catch (FileNotFoundException e) {
       //     e.printStackTrace();
        //}
    //}

    static    ObjectInputStream objectInputStream;

   /* static {

    }*/

    static Pool pooll=new Pool();
static Pool lastPooll;


    public static int cellls=0;
    public static int cellSize=3;
   public static int width;
   public static int height;
   public static int sunny=1;
    static Cell[][] cells;
    public static boolean pause=true;
    static PaintThread paintThread;
    public World(int width,int height) throws IOException {
        this.height=height;
        this.width=width;
        cells=new Cell[width][height];
    }
    static World world;
  static   Thread wor;

public static void sleep(int n) throws InterruptedException {

        Thread.sleep(n);
}
public static Cell[][] getCells(){
    return cells;
}


    public static void main(String[] args) throws InterruptedException, IOException {
        int width=1600;
        int height=1000;
        JFrame frame = new JFrame("GM");
        frame.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                pause=false;
                for (int i = 0; i <World.width ; i++) {
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
                pause=true;
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
        wor.setPriority(1);
        wor.start();


    }




   public static Genome topGene = null;
 public static    Pool topLifeTimePool=null;
 public static Pool topMultipliesPool=null;
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


        for (int i = 0; i < 100; i++) {
            Random r =new Random();
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell());
        }





        byte countt=0;
        byte countt3=0;

    while(true) {
    boolean isStep=false;
    countt2++;
    Thread.yield();
    countt3++;
    long startTime = System.currentTimeMillis();
        if(pause){
            countt2++;

            int NULL_CELLS=0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height ; j++) {
                    if( cells[i][j].secCell!=null){
                    cells[i][j].secCell.stepN=false;}
                    cells[i][j].testCell();
                }
            }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
            if (cells[i][j].secCell==null){NULL_CELLS++;

            }
                if(cells[i][j].secCell!=null){
                    if ( NULL_CELLS>width*height-4){
                        pooll.addToSpecies(topLifeTimePool.getTopGenome());
                        pooll.addToSpecies(topMultipliesPool.getTopGenome());
                        pooll.addToSpecies(cells[i][j].secCell.movablePool.getTopGenome());
                        lastPooll=pooll;
                    } if(cells[i][j].secCell.lifeTime>bestLifeTime){
                        try{
                        if(cells[i][j].secCell.movablePool.species.size()>0){
                        pooll.addToSpecies(cells[i][j].secCell.movablePool.getTopGenome());}}catch (Exception e){
                            e.printStackTrace();
                            System.out.println("shit happened again movablePool size is 0");
                        }
                        bestLifeTime=cells[i][j].secCell.lifeTime;
                        topLifeTimePool=cells[i][j].secCell.movablePool;
                        topGene=cells[i][j].secCell.movablePool.getTopGenome();
                    }if(cells[i][j].secCell.lifeTime>thisBestLifeTime){
                        thisBestLifeTime=cells[i][j].secCell.lifeTime;
                    }if (cells[i][j].secCell.multiplies>bestMultiplies){
                        bestMultiplies=cells[i][j].secCell.multiplies;
                        topMultipliesPool=cells[i][j].secCell.movablePool;
                    }
                    countt3++;

                }
if(cells[i][j].partCell!=null){

    cells[i][j].partCell.setX(i);
    cells[i][j].partCell.setY(j);
}

                if(cells[i][j].secCell!=null){
                    cells[i][j].secCell.setX(i);
                    cells[i][j].secCell.setY(j);
                    cellsNum=cells[i][j].secCell.getMyNum();
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


if (NULL_CELLS==width*height){
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            cells[j][i].organic=startEnergy;
        }
    }
Pool pol=pooll;
pol.breedNewGeneration();

  //  Collections.sort(species,Collections.reverseOrder());

    pol.addToSpecies(topGene);
    pol.addToSpecies(topLifeTimePool.getTopGenome());
    pol.addToSpecies(topMultipliesPool.getTopGenome());
    pol.addToSpecies(pooll.getTopGenome());
    lastPooll=pooll;
    pooll=pol;
    for (int i = 0; i < 10; i++) {
        new normCellInit(pooll);
    }
    Restarts++;
    if(paintMode==1)
     paintThread.rept();

    try {
        Thread.yield();
       //Thread.sleep(1000);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    lastRestarts++;
    System.out.println("Restarted");
    System.out.println("best life time: "+ bestLifeTime);
    System.out.println("this Best Life Time: " +thisBestLifeTime);
    thisBestLifeTime=0;
}


          // long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
           // usedBytes/1048576>500
           if (countt%5000==0){
               System.out.println("Garbage collector started");
                System.gc();
                countt=0;
           }
           Thread.yield();
        }

        countt++;
   long endTime = System.currentTimeMillis();
   if(endTime - startTime>100){
   System.out.println("Main took " + (endTime - startTime) + " milliseconds");}
    }

    }

    class normCellInit extends Thread{
        Pool pool;
        public normCellInit(Pool pool){
            this.pool=pool;
            run();
        }

        @Override
        public void run() {
            System.gc();
            Random r =new Random();
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pool,15));
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pool,2));
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pool,1));
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pool,r.nextLong()));
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pool,r.nextLong()));

        }
    }



}
