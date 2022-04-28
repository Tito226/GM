package MyVersion;


import MyVersion.NEAT.Pool;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class World extends JPanel  {
  static   FileInputStream fileInputStream;

    static {
        try {
            fileInputStream = new FileInputStream("D:\\save.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static    ObjectInputStream objectInputStream;

    static {
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Pool pooll;

    static {
        try {
            pooll = (Pool) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int cellls=0;
    public static int cellSize=4;
   public static int width;
   public static int height;
   public static int sunny=1;
    static Cell[][] cells;
    public static boolean pause=true;

    public World(int width,int height) throws IOException {
        this.height=height;
        this.width=width;
        cells=new Cell[width][height];
        for (int i = 0; i < width; i++) {

        }
    }



public void sleep(int n) throws InterruptedException {

        Thread.sleep(n);
}
public static Cell[][] getCells(){
    return cells;
}


    public static void main(String[] args) throws InterruptedException, IOException {
        int width=1600;
        int height=1000;
        JFrame frame = new JFrame("GM");
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


        World world=new World(width/cellSize-5,height/cellSize-20);
        frame.add(controls2,BorderLayout.NORTH);
        frame.add(controls, BorderLayout.EAST); // справа будет панель с управлением
        frame.add(world, BorderLayout.CENTER);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        world.run();
    }

class inPaintThread extends Thread{
        int type;
        Graphics g;
public inPaintThread(Graphics g,int type){
  this.g=g;
  this.type=type;
}
    @Override
    public void run() {
       switch(type){
        case 1 ->{
            for (int i = 0; i <width/5 ; i++) {
                for (int j = 0; j < height; j++) {
                    if(cells[i][j]!=null){
                        if(cells[i][j].secCell==null && cells[i][j].partCell==null ){
                            g.setColor(cells[i][j].getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        }
                        else
                        if(cells[i][j].secCell!=null && cells[i][j].partCell==null ){
                            g.setColor(cells[i][j].secCell.getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        }
                        if (cells[i][j]!=null && cells[i][j].partCell!=null){
                            g.setColor(cells[i][j].partCell.getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        }
                    }
                }
            }
        }
        case 2 ->{
            for (int i = width/5; i <(width/5)*2 ; i++) {
                for (int j = 0; j < height; j++) {
                    if(cells[i][j]!=null){
                        if(cells[i][j].secCell==null && cells[i][j].partCell==null ){
                            g.setColor(cells[i][j].getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        }
                        else
                        if(cells[i][j].secCell!=null && cells[i][j].partCell==null ){
                            g.setColor(cells[i][j].secCell.getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        }
                        if (cells[i][j]!=null && cells[i][j].partCell!=null){
                            g.setColor(cells[i][j].partCell.getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        }
                    }
                }
            }
        }
case 3 ->{
    for (int i = (width/5)*2; i <(width/5)*3 ; i++) {
        for (int j = 0; j < height; j++) {
            if(cells[i][j]!=null){
                if(cells[i][j].secCell==null && cells[i][j].partCell==null ){
                    g.setColor(cells[i][j].getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
                else
                if(cells[i][j].secCell!=null && cells[i][j].partCell==null ){
                    g.setColor(cells[i][j].secCell.getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
                if (cells[i][j]!=null && cells[i][j].partCell!=null){
                    g.setColor(cells[i][j].partCell.getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
    }
}
case 4->{
    for (int i = (width/5)*3; i <(width/5)*4 ; i++) {
        for (int j = 0; j < height; j++) {
            if(cells[i][j]!=null){
                if(cells[i][j].secCell==null && cells[i][j].partCell==null ){
                    g.setColor(cells[i][j].getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
                else
                if(cells[i][j].secCell!=null && cells[i][j].partCell==null ){
                    g.setColor(cells[i][j].secCell.getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
                if (cells[i][j]!=null && cells[i][j].partCell!=null){
                    g.setColor(cells[i][j].partCell.getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
    }
}
           case 5->{
               for (int i = (width/5)*4; i <width ; i++) {
                   for (int j = 0; j < height; j++) {
                       if(cells[i][j]!=null){
                           if(cells[i][j].secCell==null && cells[i][j].partCell==null ){
                               g.setColor(cells[i][j].getColor());
                               g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                           }
                           else
                           if(cells[i][j].secCell!=null && cells[i][j].partCell==null ){
                               g.setColor(cells[i][j].secCell.getColor());
                               g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                           }
                           if (cells[i][j]!=null && cells[i][j].partCell!=null){
                               g.setColor(cells[i][j].partCell.getColor());
                               g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                           }
                       }
                   }
               }
           }
       }
    }
}

    public void paint(Graphics g) {
if(Restarts>0){
        new inPaintThread(g,1).run();
        new inPaintThread(g,2).run();
        new inPaintThread(g,3).run();
        new inPaintThread(g,4).run();
        new inPaintThread(g,5).run();
      }

    }

public class poolInitThread extends Thread{
        int j;
        int i;
        public poolInitThread(int i,int j){
            this.j=j;
            this.i=i;
            run();
        }
    @Override
    public void run() {
        pooll.initializePool(cells[i][j].secCell.movablePool.getTopGenome());
    }
}

    int Restarts=0;
    public void run() throws InterruptedException {

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
        cells[0][1].setSecCell(new NormCell());
        cells[100][9].setSecCell(new NormCell());
        cells[394][179].setSecCell(new NormCell());
       cells[374][199].setSecCell(new NormCell());
        byte countt=0;
        byte countt2=0;
        byte countt3=0;
while(true) {
    long startTime = System.currentTimeMillis();
        if(pause==true){
            countt2++;
            if(countt2%2==0){
                //   long startTime1 = System.currentTimeMillis();
                paint(getGraphics());
                countt2=0;
                //  long endTime1 = System.currentTimeMillis();
                // System.out.println("Paint took " + (endTime1 - startTime1) + " milliseconds");
            }
            int NULL_CELLS=0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
            if (cells[i][j].secCell==null){NULL_CELLS++;

            }
             cells[i][j].testCell();
                if(cells[i][j].secCell!=null){
                    if ( NULL_CELLS<width*height-7){

                    new poolInitThread(i,j).join();}
                    countt3++;
                    cells[i][j].secCell.stepN++;
                }
if(cells[i][j].partCell!=null){
    cells[i][j].partCell.setX(i);
    cells[i][j].partCell.setY(j);
}

                if(cells[i][j].secCell!=null){
                    cells[i][j].secCell.setX(i);
                    cells[i][j].secCell.setY(j);
                    cellsNum=cells[i][j].secCell.getMyNum();
                    if(cells[i][j].secCell.stepN%2==0){
                        cells[i][j].secCell.step();
                    }
                    if(i+1<width && cells[i+1][j].secCell!=null && cells[i+1][j].secCell.getMyNum() == cellsNum) {
                            cells[i+1][j].secCell.stepN++;
                    }

                  if(j+1<height &&  cells[i][j+1].secCell!=null && cells[i][j+1].secCell.getMyNum() == cellsNum){
                        cells[i][j+1].secCell.stepN++;

                    }

                }
            }
        }


if (NULL_CELLS==width*height){

    for (int i = 0; i < 10; i++) {
       new normCellInit(pooll);
    }
 //  System.out.println(pooll.getTopFitness());
pooll=new Pool();
Restarts++;
    System.out.println("Restarted");}
            //   paint(getGraphics());
        }else {
            paint(getGraphics());
        }
if (countt%3==0){
    System.gc();
countt=0;
}
        countt++;
   long endTime = System.currentTimeMillis();
   System.out.println("Main took " + (endTime - startTime) + " milliseconds");
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
        Random r =new Random();
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,3));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,2));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,3));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll,1));
    }
}

}
