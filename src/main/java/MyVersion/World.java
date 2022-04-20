package MyVersion;


import MyVersion.NEAT.Pool;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class World extends JPanel implements Runnable {
Pool pooll;
 public static int cellls=0;
    public static int cellSize=4;
   public static int width;
   public static int height;
   public static int sunny=1;
    static Cell[][] cells;
    public static boolean pause=true;
    Paint paint=new Paint();
    public World(int width,int height){
        this.height=height;
        this.width=width;
        cells=new Cell[width][height];
        for (int i = 0; i < width; i++) {

        }
    }

class Paint extends Thread{
    @Override
    public void run() {
       paint(getGraphics());
    }
}

public void sleep(int n) throws InterruptedException {

        Thread.sleep(n);
}
public static Cell[][] getCells(){
    return cells;
}


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JButton pauseButton = new JButton("Pause");
        JButton dePauseButton = new JButton("Continue");
        pauseButton.addActionListener(new PauseListener());
        dePauseButton.addActionListener(new ContinueListener());
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(2, 1));
        controls.add(pauseButton);
        controls.add(dePauseButton);
        World world=new World(400,200);
        frame.add(controls, BorderLayout.EAST); // справа будет панель с управлением
        frame.add(world, BorderLayout.CENTER);

        frame.setSize(1600, 1000);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        Thread thread=new Thread(world);
        thread.run();
    }

class inPaintThread extends Thread{

}
byte c=0;
    public void paint(Graphics g) {
        if(c==0){
            for (int i = 0; i <width ; i++) {
                for (int j = 0; j < height; j++) {
                    if(cells[i][j]!=null){
                            g.setColor(cells[i][j].getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                            cells[i][j].isChanged=true;
                    }
                }
            }
            c++;
        }

        for (int i = 0; i <width ; i++) {
            for (int j = 0; j < height; j++) {
                if(cells[i][j]!=null){
                    if(cells[i][j].secCell==null && cells[i][j].partCell==null && cells[i][j].isChanged){
                        g.setColor(cells[i][j].getColor());
                        g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        cells[i][j].isChanged=true;
                    }else
                    if(cells[i][j].secCell!=null && cells[i][j].partCell==null ){
                    g.setColor(cells[i][j].secCell.getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        cells[i][j].isChanged=true;
                    }

                  if (cells[i][j]!=null && cells[i][j].partCell!=null){
                    g.setColor(cells[i][j].partCell.getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    cells[i][j].isChanged=true;}
                 }
            }
        }


        }

       /* try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/



    @Override
    public void run() {

        long cellsNum=0;
        int gen=0;
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
        cells[397][179].setSecCell(new NormCell());
       cells[399][199].setSecCell(new NormCell());

while(true) {
    System.out.println("Step: "+gen);
    System.gc();
  //System.out.println(NULL_CELLS);
    paint.run();
        if(pause==true){
            int NULL_CELLS=0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
            if (cells[i][j].secCell==null){NULL_CELLS++;

            }
             cells[i][j].testCell();
                if(cells[i][j].secCell!=null){pooll=new Pool();
                    pooll.initializePool(cells[i][j].secCell.pool);
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
    for (int i = 0; i < 1090; i++) {
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll));
        cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(pooll));
    }



pooll=new Pool();

    System.out.println("Restarted");}
            //   paint(getGraphics());
        }
gen++;
}

    }




}
