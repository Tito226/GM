package MyVersion.Frame;


import static MyVersion.Frame.GM2_CONFIG.CELL_START_ORGANIC;

import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Data_Set;
import MyVersion.Core.Network;
import MyVersion.Core.Network_Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;
import static MyVersion.Frame.GM2_CONFIG.CELL_SIZE;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
import static MyVersion.Frame.GM2_CONFIG.PAINT_MODE;
import static MyVersion.Frame.PaintThread.paintMode;
import static MyVersion.Frame.World.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class World extends JPanel implements Runnable  {
	
    static FileInputStream fileInputStream;
    static ObjectInputStream objectInputStream;
    public Network relative;
    public static int cellls=0;
    public static int cellSize=CELL_SIZE;
    public static int width;
    public static int height;
    public static int sunny=1;
    static Cell[][] cells;
    private static boolean pause=false;
    static PaintThread paintThread;
    static World world;
    static   Thread wor;
    public World(int width,int height) throws IOException {
        Network_Teacher network_teacher=new Network_Teacher();
        relative=network_teacher.createAndTeachNetwork();
        network_teacher=null;
        this.height=height;
        this.width=width;
        cells=new Cell[width][height];
    }
    
    synchronized boolean getPause() {
    	return pause;
    }
    synchronized static void setPause(boolean p) {
    	pause=p;
    }

public static Cell[][] getCells(){
    return cells;
}

	
	

    public static void main(String[] args) throws  IOException {
        int width=1200;
        int height=700;
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


        world=new World(width/cellSize-5,height/cellSize-20);
        frame.add(controls2,BorderLayout.NORTH);
        frame.add(controls, BorderLayout.EAST); // справа будет панель с управлением
        frame.add(world, BorderLayout.CENTER);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        wor =new Thread(world);
        if(PAINT_MODE!=0) {
        	paintThread= new PaintThread();
        	paintThread.start();
        }
        wor.start();


    }


    
    public static Network topLifeTimeBrain=null;
    public static Network topMultipliesCell=null;
    public static int bestLifeTime=0;
    public static int bestMultiplies=-1;
    public static int thisBestLifeTime=0;
    public static int lastBestLifeTime=0;
    public static int lastLastBestLifeTime=0;
    static  byte countt2=0;
    public static int Restarts=0;
    public static int lastRestarts=0;
    @Override
    public void run() {
    	
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


        int countt=0;
        boolean tested=false;
        ArrayList<Double[]> inputData=new ArrayList<Double[]>();
        
    while(true) {
    	boolean isStep=false;
    	countt2++;
    	long startTime = System.currentTimeMillis();
    	if(!getPause()){
        	
    		if(PAINT_MODE==0) {
    	 		Painter.ecoPaint(world.getGraphics());
    	 	}
      
            /*step{*************************************************************************
            *******************************************************************************/
            int NULL_CELLS=0;
            for (int i = 0; i < width; i++) {//очистка состояния(сделал ход)
                for (int j = 0; j < height ; j++) {
                    if( cells[i][j].secCell!=null){
                    cells[i][j].secCell.stepN=false;
                    }
                    cells[i][j].testCell();//проверяет на ошибки, убивает клетку ,если кончилась енергия, назначает цвет
                }
            }
            for (int i = 0; i < width; i++) {
            	for (int j = 0; j < height ; j++) {
            		if (cells[i][j].secCell==null){//подсчет населения наоборот
            			NULL_CELLS++;
            		}

            		if(cells[i][j].secCell!=null){
            			cells[i][j].secCell.setX(i);
            			cells[i][j].secCell.setY(j);
            			if(cells[i][j].secCell.lifeTime>bestLifeTime)
            				bestLifeTime=cells[i][j].secCell.lifeTime;
            			if(cells[i][j].secCell.lifeTime>thisBestLifeTime){
            				topLifeTimeBrain=BrainCloneClass.networkClone(cells[i][j].secCell.brain);
            				thisBestLifeTime=cells[i][j].secCell.lifeTime;
            			}
            			
            			if(!cells[i][j].secCell.stepN){
            				cells[i][j].secCell.stepN=true;
            				//получение входной информации ,для диагностики клетки
            				if(Restarts<10) {
            				inputData.add(cells[i][j].secCell.getInputData());
            				} else {inputData=null;
            				}
            				cells[i][j].secCell.step();//сделать ход если еще не ходил
            				isStep=true;

            			}

            		}

            	}
            }	 	  
            if(!isStep){
            	System.out.println("shit");
            }
            //***************************************************************
            //***************************************************************}step
            /*reset field organic*/
            if (NULL_CELLS==width*height){/*on restart*/
        	 	if(lastBestLifeTime==thisBestLifeTime&& thisBestLifeTime!=0 && !tested) {
        	 		//cellDiagnostic(new NormCell(relative),inputData);
        	 		tested=true;
        	 	}
        	 	onRestart();
         	}


          // long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
           // usedBytes/1048576>500
           if (countt%1000==0){
        	   System.out.println("Garbage collector started");
               System.gc();
               countt=0;
           }
           if(PAINT_MODE==0) {
   	 		Painter.ecoPaint(world.getGraphics());
           }
        }

        countt++;
        long endTime = System.currentTimeMillis();
        if(endTime - startTime>100){
        	//System.out.println("Main took " + (endTime - startTime) + " milliseconds");
	   }
        
    }

    }

    private void onRestart() {
    	//установка начального состояния органики,если все умерли
	 	for (int i = 0; i < height; i++) {
	 		for (int j = 0; j < width; j++) {
	 			cells[j][i].organic=CELL_START_ORGANIC;
	 		}
	 	}
	 	ArrayList<Network> checkGroup=new ArrayList();
	 	//Summon cells
	 	for (int i = 0; i < GM2_CONFIG.CELLS_ON_START; i++) {
	 		NormCell nBuf;
	 		Random r =new Random();
	 		int buff=r.nextInt(2);
	 		if(buff==1) {
	 			nBuf=new NormCell(relative);
	 			cells[r.nextInt(width)][r.nextInt(height)].setSecCell(nBuf);
	 			continue;
	 		}else  {
	 			nBuf=new NormCell(topLifeTimeBrain);
	 			cells[r.nextInt(width)][r.nextInt(height)].setSecCell(nBuf);
	 		}
	 		checkGroup.add(nBuf.brain);
	 	}
	 	
	 	int tltb=0,rel=0;
	 	for (int i = 0; i < checkGroup.size(); i++) {
	 		if(BrainCloneClass.networkCompare(topLifeTimeBrain,checkGroup.get(i))) {
	 			tltb++;
	 		}else if(BrainCloneClass.networkCompare(relative,checkGroup.get(i))) {
	 			rel++;
	 		}
	 			
		}
	 	System.err.println("topLifeTimeBrain "+tltb);
	 	System.err.println("relative "+rel);
	 	Restarts++;
	 	//+++++++++++++++++++
	 	if(PAINT_MODE!=0) {//0
	 		paintThread.rept();
	 		
	 	}
	 	else {
	 		paint1(world.getGraphics());
	 	}
	 	
	 	checkGroup=null;
	 	lastRestarts++;
	 	System.out.println("Restarted");
	 	System.out.println("best life time: "+ bestLifeTime);
	 	System.out.println("this Best Life Time: " +thisBestLifeTime);
	 	lastLastBestLifeTime=lastBestLifeTime;
	 	lastBestLifeTime=thisBestLifeTime;
	 	thisBestLifeTime=0;
    }
    
    public void paint1(Graphics g) {
    	g.setColor(Color.BLACK);
        g.drawString("Restarts: " + Restarts, 7, 9);
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
    
    
    
    void cellDiagnostic(NormCell norm,ArrayList<Double[]> inputData) {//TODO FIX IT
    	System.out.println("Run Cell diag.");//TODO НАЙТИ ОТЛИЧИЯ МЕЖДУ ВХОДНЫМИ ДАННЫМИ ,СЕЙЧАС ТУТ ЧЕРНАЯ МАГИЯ ВХОДНЫЕ ДАННЫЕ ОДИНАКОВЫЕ,МЕТОД ТОЖЕ,А РЕЗУЛЬТАТЫ РАЗНЫЕ
    	System.out.print(":::: "+
        		norm.evaluateFitness(new Double[]{0d,15d,6d,0d,0d,0d,0d})+"тест 1     ");
    	System.out.print(":::: "+														// тесты 1 должны быть одинаковыми
        		norm.evaluateFitness(new Double[]{0d,15d,6d,0d,0d,0d,0d,23456d})+"тест 1     ");
    	Random r =new Random();
    	System.out.print(norm.evaluateFitness(new Double[]{0d,(double) Data_Set.rnd(1,4),(double) Data_Set.rnd(7,100),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)})+"тест 2               ");
    	if(inputData== null)
    		return;
    	for(int i=0;i<inputData.size();i++) {
    		if(i%2==1)
    			continue;
    		for(int j=0;j<7;j++) {
    			switch(j) {
    			case 0:System.out.print("multiplyReady:");
    			break;
    			
    			case 1:System.out.print("energy:");
    			break;
    			
    			case 2:System.out.print("organic:");
    			break;
    			
    			case 3:System.out.print("upCell:");
    			break;
    			case 4:System.out.print("downCell:");
    			break;
    			
    			case 5:System.out.print("leftCell:");
    			break;
    			case 6:
    				System.out.print("rightCell:");
    			break;
    			}
    			Double[] buf=inputData.get(i);
    			System.out.print(buf[j]+" ");
    		}
    		System.out.println(":::: "+
    		norm.evaluateFitness(inputData.get(i)));
    		System.out.print(" ");
    		System.out.print(norm.evaluateFitness(new Double[] {0d,(double) Data_Set.rnd(3,15),6d,0d,0d,0d,0d}));
    		System.out.print(" ");
    	}
    }



}
