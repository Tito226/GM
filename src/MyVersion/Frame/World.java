package MyVersion.Frame;


import static MyVersion.Frame.GM2_CONFIG.CELL_START_ORGANIC;

import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Data_Set;
import MyVersion.Core.Network;
import MyVersion.Core.Network_Teacher;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static MyVersion.Frame.GM2_CONFIG.CELL_SIZE;
import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.GM2_CONFIG.PAINT_MODE;
import static MyVersion.Frame.World.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class World extends JPanel implements Runnable  {
	Painter painter=new Painter(this);
	InfoPanel inf=new InfoPanel(this);
    public Network relative;
    public static int cellls=0;
    public static int cellSize=CELL_SIZE;
    public static int width;
    public static int height;
    public static int sunny=1;
    static Cell[][] cells;
    private static boolean pause=false;
    static World world;
    static Thread wor;
    public World(int width,int height) throws IOException {
        Network_Teacher network_teacher=new Network_Teacher();
        if(!TEST_RUN) {
        	relative=network_teacher.createAndTeachNetwork();
        }else {
        	
            try {
            	FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Timurs1\\Desktop\\BrainSave.network");
            	byte[] buff=fileInputStream.readAllBytes();
            	fileInputStream.close();
            	ByteArrayInputStream fis = new ByteArrayInputStream(buff);
            	ObjectInputStream ois = new ObjectInputStream(fis);
				relative=(Network) ois.readObject();
	ois.close();
				fis.close();
			} catch (ClassNotFoundException | IOException e) {	
				e.printStackTrace();
			}
        }
    
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
        int width=1250;
        int height=700;
        JFrame frame = new JFrame("GM");
        
        JButton pauseButton = new JButton("Pause");
        JButton dePauseButton = new JButton("Continue");
        pauseButton.addActionListener(new PauseListener());
        dePauseButton.addActionListener(new ContinueListener());
        JPanel controls = new JPanel();
        JPanel controls2 = new JPanel();
        controls.setLayout(new GridLayout(2, 1));
        //controls2.setLayout(new GridLayout(1, 3));
        controls.add(pauseButton);
        controls.add(dePauseButton);
        
        world=new World(width/cellSize,height/cellSize-30);
        JButton button = new JButton("save");
        JButton button2 = new JButton("load");
        button.addActionListener(new FileSaveListener());
        button2.addActionListener(new FileOpenListener());
        controls2.add(button);
        controls2.add(button2);
        controls2.add(world.inf);
        world.inf.setPreferredSize(new Dimension(300,40));//метод для задания размера JPanel

        
        frame.add(controls2,BorderLayout.NORTH);
        frame.add(controls, BorderLayout.EAST); // справа будет панель с управлением
        frame.add(world, BorderLayout.CENTER);
        
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        wor =new Thread(world);
        wor.start();


    }
    int stepsBuff=0;
    int countt=0; 
    public int stepsAtAll=0;
    public int Restarts=0;
    public volatile int fps=0;
    public volatile int liveCells=0;
    public static int bestLifeTime=0;
    public static int bestMultiplies=-1;
    public static int thisBestLifeTime=0;
    public static int lastBestLifeTime=0;
    public static int lastLastBestLifeTime=0;
    public static int lastRestarts=0;    
    public long fpsMeter1=0;
    public long fpsMeter2=0;
    public static Network thisTopLifeTimeBrain;
    public static Network topLifeTimeBrain=null;
    public static Network topMultipliesBrain=null;
    ExecutorService pool=Executors.newFixedThreadPool(1);
    static FileOutputStream fileOutputStream ;
    static ObjectOutputStream objectOutputStream ;
    LimitedArrayList<Network> bestLifeTimeBrains=new LimitedArrayList<>(20);
    LimitedArrayList<Network> bestMultipliesBrains=new LimitedArrayList<>(20);
    LimitedArrayList<Network> bestThisLifeTimeBrains=new LimitedArrayList<>(20);
    boolean tested=false;
    @Override
    public void run() {
//**************************************************************************************************        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                cells[x][y]=new Cell();
                if (cells[x][y]!=null) {
                    cells[x][y].setX(x);
                    cells[x][y].setY(y);
                }

            }
        }


        for (int i = 0; i < CELLS_ON_START; i++) {
            Random r =new Random();
            cells[r.nextInt(width)][r.nextInt(height)].setSecCell(new NormCell(relative));
        }

        ArrayList<Double[]> inputData=new ArrayList<Double[]>();
        multiThreadPaintInitial();
//**************************************************************************************************
    while(true) {
    	boolean isStep=false;
    	long startTime = System.currentTimeMillis();
    	if(!getPause()){
    		stepsAtAll++;
    		if(System.currentTimeMillis()>fpsMeter1+1000) {
    			fpsMeter1=System.currentTimeMillis();
    			stepsBuff=stepsAtAll;
    		}
            /*step{*************************************************************************
            *******************************************************************************/
            int NULL_CELLS=0;
            for (int i = 0; i < width; i++) {//очистка состояния(сделал ход)
                for (int j = 0; j < height ; j++) {
                    if( cells[i][j].secCell!=null){
                    cells[i][j].secCell.stepN=false;
                    }/*TODO реализовать мезонизм восстановления органики*/
                    cells[i][j].testCell();//проверяет на ошибки, убивает клетку ,если кончилась енергия, назначает цвет
                }
            }
            for (int i = 0; i < width; i++) {
            	for (int j = 0; j < height ; j++) {
            		Cell curCell=cells[i][j];
            		if(curCell.secCell!=null){
            			NormCell curNormCell=curCell.secCell;
            			curNormCell.setX(i);
            			curNormCell.setY(j);
            			if(curNormCell.lifeTime>bestLifeTime) {
            				bestLifeTime=curNormCell.lifeTime;
            				if(topLifeTimeBrain!=null && curNormCell.brain.isDead) {
            					topLifeTimeBrain.kill();
            				}
            				topLifeTimeBrain=curNormCell.brain;
            				bestLifeTimeBrains.add(curNormCell.brain);
            				curNormCell.brain.dontDelete=true;
            			}
            			if(curNormCell.lifeTime>thisBestLifeTime){
            				thisBestLifeTime=curNormCell.lifeTime;
            				if(thisTopLifeTimeBrain!=null && curNormCell.brain.isDead) {
            					thisTopLifeTimeBrain.kill();
            				}
            				thisTopLifeTimeBrain=curNormCell.brain;
            				bestThisLifeTimeBrains.add(curNormCell.brain);
            				curNormCell.brain.dontDelete=true;
            			}
            			if(curCell.secCell.multiplies>bestMultiplies) {
            				bestMultiplies=curNormCell.multiplies;
            				if(topMultipliesBrain!=null && curNormCell.brain.isDead) {
            					thisTopLifeTimeBrain.kill();
            				}
            				topMultipliesBrain=curNormCell.brain;
            				bestMultipliesBrains.add(curNormCell.brain);
            				curNormCell.brain.dontDelete=true;
            			}
            			if(!curNormCell.stepN){
            				curNormCell.stepN=true;
            				curNormCell.step();//сделать ход если еще не ходил
            				isStep=true;
            				//получение входной информации ,для диагностики клетки
            				if(Restarts<4 && DEBUG) {
            					inputData.add(curNormCell.myMethods.getInputData());
            				} else {
            					inputData=null;
            				}
            			}
            			if(System.currentTimeMillis()>fpsMeter1+1000) {
                			fps=stepsAtAll-stepsBuff;
                		}
            		}else {
            			NULL_CELLS++;//подсчет населения наоборот
            		}

            	}
            }	 	  
            if(!isStep){
            	System.out.println("shit");
            }
            if(countt%13==0) {
            	liveCells=width*height-NULL_CELLS;
            }
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
        	   painter.ecoPaint(world.getGraphics(),inf);
           } else if(PAINT_MODE==-1) {//0
   	 			paintComponent(world.getGraphics());
   	 		}
        } else {
        	painter.fullPaint(world.getGraphics(),inf);
        	try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
    	Random r=new Random();
    	//установка начального состояния органики,если все умерли
	 	for (int i = 0; i < height; i++) {
	 		for (int j = 0; j < width; j++) {
	 			cells[j][i].organic=CELL_START_ORGANIC;
	 		}
	 	}
	 	if(thisBestLifeTime>CREATE_SAVE_ON_LIFETIME && thisBestLifeTime>lastBestLifeTime) {/*TODO реализовать механизм проверки тот ли мозг копируется*/
	 		try {
	 			fileOutputStream = new FileOutputStream("C:\\Users\\Timurs1\\Desktop\\BrainSave"+thisBestLifeTime+r.nextLong()+".network");
	 			objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(topLifeTimeBrain);
				objectOutputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
	 	}
	 	ArrayList<Network> checkGroup=new ArrayList<>();
	 	//Summon cells
	 	for (int i = 0; i < GM2_CONFIG.CELLS_ON_START; i++) {
	 		NormCell nBuf = null;
	 		int buff=r.nextInt(3);
	 		if(buff==1) {
	 				nBuf=new NormCell(bestThisLifeTimeBrains.get(r.nextInt(bestThisLifeTimeBrains.size())));
	 				cells[r.nextInt(width)][r.nextInt(height)].setSecCell(nBuf);
	 				continue;
	 		}else if(buff==0) {
	 			nBuf=new NormCell(bestLifeTimeBrains.get(r.nextInt(bestLifeTimeBrains.size())));
	 			cells[r.nextInt(width)][r.nextInt(height)].setSecCell(nBuf);
	 		} else {
	 			nBuf=new NormCell(bestMultipliesBrains.get(r.nextInt(bestMultipliesBrains.size())));
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
	 		paintComponent(world.getGraphics());
	 		paintComponent(world.getGraphics());
	 	}
	 	else {
	 		paintComponent(world.getGraphics());
	 	}
	 	
	 	checkGroup=null;
	 	lastRestarts++;
	 	System.out.println("Restarted");
	 	System.err.println("Steps :"+stepsAtAll);
	 	System.out.println("best life time: "+ bestLifeTime);
	 	System.out.println("this Best Life Time: " +thisBestLifeTime);
	 	lastLastBestLifeTime=lastBestLifeTime;
	 	lastBestLifeTime=thisBestLifeTime;
	 	thisBestLifeTime=0;
	 	stepsAtAll=0;
    }

    void multiThreadPaintInitial() {
    	if(PAINT_MODE==1 || PAINT_MODE==3 || PAINT_MODE==4) {
        	Runnable task = () -> {
        		while(true) {
        			if(!world.getPause()) {
        				if(PAINT_MODE==1 ) {
        					painter.ecoPaint(world.getGraphics(),inf);
        				}
        				else if(PAINT_MODE==3 ) {
        					painter.fullPaint(world.getGraphics(),inf);
        				}
        				else if(PAINT_MODE==4 ) {
        					painter.combinedPaint(world.getGraphics(),inf);
        				}
        				try {
        					Thread.sleep(10);//20
        				} catch (InterruptedException e) {
        					e.printStackTrace();
        				}	
        			}else {
        				try {
        					Thread.sleep(200);//20
        				} catch (InterruptedException e) {
        					e.printStackTrace();
        				}
        			}
        		}
        };
    	pool.execute(task);
	}
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        // Отрисовка в буфер
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] != null) {
                    g.setColor(cells[i][j].getColor());
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
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
