package MyVersion;


import MyVersion.NEAT.Pool;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class World extends JPanel  {
Pool pooll;
 public static int cellls=0;
    public static int cellSize=4;
   public static int width;
   public static int height;
   public static int sunny=1;
    static Cell[][] cells;
    public static boolean pause=true;

    public World(int width,int height){
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


    public static void main(String[] args) {
        int width=1600;
        int height=1000;
        JFrame frame = new JFrame();
        JButton pauseButton = new JButton("Pause");
        JButton dePauseButton = new JButton("Continue");
        pauseButton.addActionListener(new PauseListener());
        dePauseButton.addActionListener(new ContinueListener());
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(2, 1));
        controls.add(pauseButton);
        controls.add(dePauseButton);
        World world=new World(width/cellSize-5,height/cellSize-10);
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
        int i;
        int j;
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
        long startTime = System.currentTimeMillis();
        new inPaintThread(g,1).run();
        new inPaintThread(g,2).run();
        new inPaintThread(g,3).run();
        new inPaintThread(g,5).run();
        new inPaintThread(g,4).run();
        long endTime = System.currentTimeMillis();
       // System.out.println("Paint took " + (endTime - startTime) + " milliseconds");
    }






    public void run() {

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

while(true) {
    long startTime = System.currentTimeMillis();
countt2++;
if(countt2%2==0){
    paint(getGraphics());
    countt2=0;
}
        if(pause==true){
            int NULL_CELLS=0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
            if (cells[i][j].secCell==null){NULL_CELLS++;

            }
             cells[i][j].testCell();
                if(cells[i][j].secCell!=null){pooll=new Pool();
                    pooll.initializePool(cells[i][j].secCell.movablePool);
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
   Random r =new Random();
    for (int i = 0; i < 100; i++) {
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell());
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell());
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll));
    }
 //  System.out.println(pooll.getTopFitness());
pooll=new Pool();

    System.out.println("Restarted");}
            //   paint(getGraphics());
        }
if (countt%3==0){
    System.gc();
countt=0;
}
        countt++;
    long endTime = System.currentTimeMillis();
   // System.out.println("Main took " + (endTime - startTime) + " milliseconds");
}

    }




}
