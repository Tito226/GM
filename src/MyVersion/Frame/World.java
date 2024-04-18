package MyVersion.Frame;


import static MyVersion.Frame.GM2_CONFIG.CELL_START_ORGANIC;

import MyVersion.Cells.Cell;
import MyVersion.Cells.LiveCell;
import MyVersion.Cells.NormCell;
import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Data_Set;
import MyVersion.Core.Network;
import MyVersion.Core.Network_Teacher;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static MyVersion.Frame.GM2_CONFIG.CELL_SIZE;
import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.GM2_CONFIG.PAINT_MODE;
import static MyVersion.Frame.World.normCells;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class World extends JPanel implements Runnable ,Scrollable {
	Painter painter=new Painter(this);
	JScrollPane scroller;
	InfoPanel inf=new InfoPanel(this);
	Cell_InfoPanel cell_inf=new Cell_InfoPanel(this);
    public Network[] relative=new Network[2];
    //public static int cellls=0;
    public volatile static int cellSize=CELL_SIZE;
    public int slowdown=0;
    public static int width;
    public static int height;
    public static int sunny=1;
    public static Cell[][] cells;
    private static boolean pause=false;
    private static boolean windowClosed=false;
    public static ArrayList<NormCell> normCells=new ArrayList<>();//TODO fix bugs(contains dead cells,contains cell which is not exist in cells)
    ArrayList<NormCell> buffer;
    Thread wor;
    JFrame frame;
    public World(int width,int height) throws IOException {
        Network_Teacher network_teacher=new Network_Teacher();
        if(!LOAD_SAVE) {
        	relative[0]=network_teacher.createAndTeachNetwork();
        	relative[1]=relative[0];
        	//relative[0]=network_teacher.createRandomNetwork();
        	//relative[1]=network_teacher.createRandomNetwork();
        }else {
        	
            try {
            	FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Timurs1\\Desktop\\BrainSave.network");
            	byte[] buff=fileInputStream.readAllBytes();
            	fileInputStream.close();
            	ByteArrayInputStream fis = new ByteArrayInputStream(buff);
            	ObjectInputStream ois = new ObjectInputStream(fis);
				relative=(Network[]) ois.readObject();
				ois.close();
				fis.close();
			} catch (ClassNotFoundException | IOException e) {	
				e.printStackTrace();
			}
        }
        if(!AUTO_SIZE) {
        	cellSize=1;
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

	static JFrame createFrame(World world,int width,int height) {
		JFrame frame=world.frame;
		JPanel rightMenu = new JPanel();//Right menu
        JPanel upMenu = new JPanel();//Upper menu
        JButton pauseButton = new JButton("Pause");
        JButton countinueButton = new JButton("Continue");
        world.scroller = new JScrollPane(world);
        JScrollPane scroller=world.scroller;
        if(!AUTO_SIZE) {
        	cellSize=1;
        }
        /***************BUTTONS*******************/
        JButton semaPlusButton = new JButton("+");
        JButton semaMinusButton = new JButton("-");
        rightMenu.add(semaPlusButton);
        rightMenu.add(semaMinusButton);
        pauseButton.addActionListener(new PauseListener());
        countinueButton.addActionListener(new ContinueListener());
        rightMenu.add(pauseButton);
        rightMenu.add(countinueButton);
        rightMenu.setLayout(new BoxLayout(rightMenu, BoxLayout.Y_AXIS));
        
        //rightMenu.setAlignmentX(1f);
        rightMenu.add(Box.createVerticalStrut(10));
        JButton saveButton = new JButton("save");
        JButton loadButton = new JButton("load");
        saveButton.addActionListener(new FileSaveListener(world));
        loadButton.addActionListener(new FileOpenListener());
        upMenu.add(saveButton);
        upMenu.add(loadButton);
        JButton plusButton = new JButton("+");
        JButton minusButton = new JButton("-");
        upMenu.add(plusButton);
        upMenu.add(minusButton);
        
        semaPlusButton.addActionListener((java.awt.event.ActionEvent e) -> {
        	boolean buff=world.getPause();
        	world.setPause(true);
        	world.maxThreads+=1;
        	//world.sem=new Semaphore(world.maxThreads);
        	System.out.println(world.maxThreads);
        	if(!buff) {
        		world.setPause(false);
        	}
        });
        
        semaMinusButton.addActionListener((java.awt.event.ActionEvent e) -> {
        	boolean buff=world.getPause();
        	world.setPause(true);
        	if(world.maxThreads>1) {
        		world.maxThreads-=1;
        	}
        	//world.sem=new Semaphore(world.maxThreads);
        	System.out.println(world.maxThreads);
        	if(!buff) {
        		world.setPause(false);
        	}
        });
        
        plusButton.addActionListener((java.awt.event.ActionEvent e) -> {
        	frame.paintAll(frame.getGraphics());
        	cellSize++;
        	System.out.println(cellSize);
        });
        minusButton.addActionListener((java.awt.event.ActionEvent e) -> {
        	if(cellSize>1) {
        		cellSize--;
        	}
        	frame.paintAll(frame.getGraphics());
        	System.out.println(cellSize);
        });
        /*****************************************/
        rightMenu.add(world.cell_inf); 
        upMenu.add(world.inf);
        world.inf.setPreferredSize(new Dimension(300,40));//метод для задания размера JPanel
        world.setSize(new Dimension(width,height));//метод для задания размера JPanel
        
        /******************Selector******************/
        
        world.addMouseListener(new MouseAdapter() {
        	Cell previousElement;
        	NormCell previousNormCell;
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON3) {
            		for(Cell[] curCellArray : cells) {
            			for(Cell curCell : curCellArray) {
            				curCell.selected=false;
            				if(curCell.liveCell!=null && curCell.liveCell instanceof NormCell) {
            					NormCell curNormCell= (NormCell) curCell.liveCell;
            					curNormCell.selected=false;
            				}
            			}
            		}
            		// Получаем координаты курсора мыши
            		if(previousElement!=null ) {
            			previousElement.selected=false;
            		}
            		if(previousNormCell!=null) {
            			previousNormCell.selected=false;
            		}
            		int x = e.getX();
            		int y = e.getY();
            		//System.out.println(x+"  "+y);
            		int columnIndex = x / cellSize;
            		int rowIndex = y / cellSize;

            		if (columnIndex >= 0 &&  rowIndex< cells[0].length &&
            				rowIndex >= 0 && columnIndex < cells.length) {
            			Cell selectedElement = cells[columnIndex][rowIndex];
            			
            			if(selectedElement.liveCell!=null && selectedElement.liveCell instanceof NormCell) {
            				LiveCell selectedLiveCell=selectedElement.liveCell;
            				NormCell selectedNormCell=(NormCell) selectedLiveCell;
            				previousNormCell=selectedNormCell;
            				world.cell_inf.selectedLiveCell=selectedNormCell;
            				world.cell_inf.selectedCell=null;
            				selectedNormCell.selected=true;
            			}else if(selectedElement.liveCell!=null) {
            				LiveCell selectedLiveCell=selectedElement.liveCell;
            				world.cell_inf.selectedLiveCell=selectedLiveCell;
            			}else if(selectedElement==previousElement) {
            				selectedElement.selected=false;
            			}else  {
            				previousElement=selectedElement;
            				world.cell_inf.selectedLiveCell=null;
            				world.cell_inf.selectedCell=selectedElement;
            				selectedElement.selected=true;
            			}
            		}
            }
           }
        });
        /********************************************/
        /***************SCROLLER******************/

        scroller.setPreferredSize(new Dimension(width,height));
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        Runnable task = () -> {
        	while(true) {
        		if(AUTO_SIZE) {
        			world.setPreferredSize(new Dimension(width*cellSize/CELL_SIZE+10,height*cellSize/CELL_SIZE+10));
            		scroller.setPreferredSize(new Dimension(1200,700));
                }else {
                	world.setPreferredSize(new Dimension(width*cellSize+10,height*cellSize+10));
        			scroller.setPreferredSize(new Dimension(1200,700));
        		}
        		scroller.revalidate();
        		try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
        	}
    	};
    	Thread thread = new Thread(task);
    	thread.start();
    	new FrameMover(world);
        /****************************************/
    	
    	/**************JSlider*******************/
    	
    	 JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 150, 0);
         slider.setMinorTickSpacing(10);
         slider.setMajorTickSpacing(50);
         slider.setPaintTicks(true);
         slider.setPaintLabels(true);
         rightMenu.add(Box.createVerticalStrut(20));
         // Добавляем слушатель изменений
         slider.addChangeListener(new ChangeListener() {
             public void stateChanged(ChangeEvent e) {
                 JSlider source = (JSlider) e.getSource();
                 if (!source.getValueIsAdjusting()) {
                	 world.slowdown = source.getValue();
                 }
             }
         });
        slider.setMaximumSize(new Dimension(200,50));   
        rightMenu.add(slider);
    	/****************************************/
        world.addWinListnerToFrame();
        frame.add(upMenu,BorderLayout.NORTH);
        frame.add(rightMenu,BorderLayout.EAST); // справа будет панель с управлением
        frame.add(scroller, BorderLayout.CENTER);
        world.setSize(width, height);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        return frame;
	}
	

    public static void main(String[] args) throws  IOException {
        int width=1250;
        int height=700;
        World world;
        if(AUTO_SIZE) {
        	 world=new World(width/CELL_SIZE-3,height/CELL_SIZE-3);
        }else {
        	 world=new World(width,height);
        }
        world.frame = new JFrame("GM");
        
        JFrame frame=createFrame(world,width,height);

        world.wor =new Thread(world);
        world.wor.start();


    }
    
    int countt=0; 
    public int stepsAtAll=0;
    public int Restarts=0;
    public volatile int sps=0;//steps(one main cycle turn) per seconds
    public volatile int liveCells=0;
    public static int bestLifeTime=0;
    public static int bestMultiplies=-1;
    public static int thisBestLifeTime=0;
    public static int lastBestLifeTime=0;
    public static int lastLastBestLifeTime=0;
    public static int lastRestarts=0;   
    public static int thisBestSize=0;
    public long fpsMeter1=0;
    public static Network[] thisTopLifeTimeBrain=new Network[2];
    public static Network[] topLifeTimeBrain=new Network[2];
    public static Network[] topMultipliesBrain=new Network[2];
    public static Network[] thisTopSizeBrain=new Network[2];
    //ExecutorService pool=Executors.newFixedThreadPool(1);
    static FileOutputStream fileOutputStream ;
    static ObjectOutputStream objectOutputStream ;
    ArrayList<LimitedArrayList<Network[]>> bestBrainsArrs=new ArrayList<LimitedArrayList<Network[]>>();
    LimitedArrayList<Network[]> bestLifeTimeBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
    LimitedArrayList<Network[]> bestThisLifeTimeBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
    LimitedArrayList<Network[]> bestMultipliesBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
    LimitedArrayList<Network[]> thisBiggestSizeBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
    
    void addListsToBestBrains() {
    	bestBrainsArrs.add(bestLifeTimeBrains);
    	bestBrainsArrs.add(bestThisLifeTimeBrains);
    	bestBrainsArrs.add(bestMultipliesBrains);
    	bestBrainsArrs.add(thisBiggestSizeBrains);
    }
    
    Graphics worldGraphics=this.getGraphics();
    boolean tested=false;
    
    long startTime = System.currentTimeMillis();

    
    Network[] topBrainChoose(NormCell curNormCell,Network[] curTopBrain,LimitedArrayList<Network[]> bestBrains) {
    	synchronized(this) {
    		try {
				curNormCell.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		/*
    		if(curTopBrain!=null && curNormCell.brain!=null && curNormCell.brain.isDead) {
				for (int k = 0; k < curTopBrain.length; k++) {
					curTopBrain[k].kill();
				}
    		}*/
    		curTopBrain[0]=curNormCell.brain;
    		curTopBrain[1]=curNormCell.multiCellBrain;
    		bestBrains.add(curTopBrain);
			curNormCell.brain.dontDelete=true;
			curNormCell.multiCellBrain.dontDelete=true;
			curNormCell.sem.release();
			return curTopBrain;
		}
    }
    //ExecutorService executor = Executors.newFixedThreadPool(2);
    
    synchronized void findGoodCell(NormCell curNormCell) {
    	if(curNormCell.getLifeTime()>bestLifeTime) {
			bestLifeTime=curNormCell.getLifeTime();
			topBrainChoose(curNormCell,topLifeTimeBrain,bestLifeTimeBrains);
		}
		if(curNormCell.getLifeTime()>thisBestLifeTime){
			thisBestLifeTime=curNormCell.getLifeTime();
			topBrainChoose(curNormCell,thisTopLifeTimeBrain,bestThisLifeTimeBrains);
		}
		if(curNormCell.multiplies>bestMultiplies) {
			bestMultiplies=curNormCell.multiplies;
			topBrainChoose(curNormCell,topMultipliesBrain,bestMultipliesBrains);
		}
		if(curNormCell.myParts.size()>thisBestSize) {
			thisBestSize=curNormCell.myParts.size();
			topBrainChoose(curNormCell,thisTopSizeBrain,thisBiggestSizeBrains);
		}
    }
    
    void worldInitial() {
    	addListsToBestBrains();
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
            cells[r.nextInt(width)][r.nextInt(height)].setLiveCell(new NormCell( relative[0], relative[1]));
        }
        multiThreadPaintInitial();
    }
    int maxThreads=1;
    Semaphore sem = new Semaphore(maxThreads);
    @Override
    public void run() {

    ArrayList<Double[]> inputData=new ArrayList<Double[]>();
    worldInitial();
    	
    while(true) {//the main cycle
    	fpsMeter1=System.currentTimeMillis();
    	boolean isStep=false;
    	if(!getPause()){
    		Graphics worldGraphics=this.getGraphics();
    		stepsAtAll++;
            testAllCells();
            
            buffer=new ArrayList<NormCell>(normCells);//to avoid concurrent modification exception
            buffer.parallelStream().forEach(curNormCell ->{
            	try { 		
					sem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	if(normCells.contains(curNormCell) && curNormCell!=null) {
            		curNormCell.step();//TODO понять почему в масиве не удаляются мертвые
            		if(Restarts<4 && DEBUG) {
            			inputData.add(curNormCell.getInputData());
            		}
            		
            	}
            	sem.release();
            });
      
            
            
            for(NormCell curCell : normCells) {
    			if(curCell!=null) {
    				if(curCell.brain==null) {
    					curCell.selected=true;
    					cell_inf.selectedLiveCell=curCell;
    					System.err.println("shet!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    					Thread.currentThread().stop();
    				}
    				findGoodCell(curCell);
    			}
    		}

            testNormCellsArray();
            Collections.shuffle(normCells);
            if(!(Restarts<4 && DEBUG)) {
            	inputData=null;
            }
            
            
            if(!isStep){
            	//System.out.println("shit");
            }
            if(countt%1==0) {
            	liveCells=normCells.size();
            }
            
            /*reset field organic*/
            if (normCells.size()==0){/*on restart*/
        	 	if(lastBestLifeTime==thisBestLifeTime&& thisBestLifeTime!=0 && !tested) {
        	 		//cellDiagnostic(new NormCell(relative),inputData);
        	 		tested=true;
        	 	}
        	 	onRestart();
         	}


            // long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            // usedBytes/1048576>500

            if (System.currentTimeMillis()-startTime>30*1000){
        	   	startGC();
           	}
            /****************0 thread paint****************/
           	if(PAINT_MODE==0) {
           		painter.ecoPaint();
           	} else if(PAINT_MODE==-1) {//0
   	 			paintComponent(worldGraphics);
   	 		}
           	if(slowdown>0) {
           		try {
           			Thread.sleep(slowdown);
           		} catch (InterruptedException e) {
					e.printStackTrace();
				}
           	}
           	/**********************************************/
           	
           	sps=(int) (1000/(System.currentTimeMillis()-fpsMeter1+1));
        } else {
        	
        	if(PAINT_MODE==0) {
        		painter.fullPaint();
        	}
        	try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }

        countt++;
        
    }

    }

    private void onRestart() {
    	Graphics worldGraphics=this.getGraphics();
    	Random r=new Random();
    	//установка начального состояния органики,если все умерли
	 	for (int i = 0; i < height; i++) {
	 		for (int j = 0; j < width; j++) {
	 			cells[j][i].organic=CELL_START_ORGANIC;
	 		}
	 	}
	 	saveBestBrain();
	 	ArrayList<Network> checkGroup=new ArrayList<>();
	 	//Summon cells
	 	for (int i = 0; i < GM2_CONFIG.CELLS_ON_START; i++) {
	 		NormCell nBuf = null;
	 		int buff=r.nextInt(bestBrainsArrs.size());
	 		LimitedArrayList<Network[]> bestBrainsArr=bestBrainsArrs.get(buff);
	 		if(bestBrainsArr.size()>0) {
	 			Network[] curBrain=bestBrainsArr.get(r.nextInt(bestBrainsArr.size()));
	 			nBuf=new NormCell(curBrain[0],curBrain[1]);
	 			cells[r.nextInt(width)][r.nextInt(height)].setLiveCell(nBuf);
	 		}else {
	 			i--;
	 		}
	 	}
	 	Restarts++;
	 	
	 	//+++++++++++++++++++
	 	if(PAINT_MODE!=0) {//0
	 		paintComponent(worldGraphics);
	 		paintComponent(worldGraphics);
	 	}
	 	else {
	 		paintComponent(worldGraphics);
	 	}
	 	
	 	checkGroup=null;
	 	lastRestarts++;
	 	System.out.println("Restarted");
	 	System.err.println("Steps :"+stepsAtAll);
	 	System.out.println("best life time: "+ bestLifeTime);
	 	System.out.println("this Best Life Time: " +thisBestLifeTime);
	 	lastLastBestLifeTime=lastBestLifeTime;
	 	lastBestLifeTime=bestLifeTime;
	 	thisBestLifeTime=0;
	 	stepsAtAll=0;
    }

    void startGC() {
    	 System.out.println("Garbage collector started");
         System.gc();
         startTime = System.currentTimeMillis();
         countt=0;
    }
    
    void saveBestBrain() {
    	Random r=new Random();
    	if(thisBestLifeTime>CREATE_SAVE_ON_LIFETIME /*&&  thisBestLifeTime>lastBestLifeTime*/ ) {//TODO STUB
	 		try {
	 			fileOutputStream = new FileOutputStream("C:\\Users\\Timurs1\\Desktop\\brains\\BrainSave"+thisBestLifeTime+"---"+r.nextLong()+".network");
	 			objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(thisBiggestSizeBrains.get(0));
				objectOutputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
	 	}
    }
    
    void testNormCellsArray() {
    	 boolean toClear=true;
 		ArrayList<NormCell> buffer=new ArrayList<>(normCells);
 		for(NormCell curCell : buffer) {
 			if(curCell!=null) {
 				toClear=false;
 			}
 		}
 		if(toClear) {
 			//System.err.println("giga shit");
 			normCells.clear();
 		}
    }
    
    void testAllCells(){
    	 for (int i = 0; i < width; i++) {//очистка состояния(сделал ход)
             for (int j = 0; j < height ; j++) {
             	Cell curCell=cells[i][j];
             	curCell.testCell();
             }
         }
    }
    
    void testCell(Cell curCell) {
    	 if(curCell.liveCell!=null && curCell.liveCell instanceof NormCell){
         	NormCell curNormCell=(NormCell) curCell.liveCell;
         }/*TODO реализовать механизм восстановления органики*/
    	 curCell.testCell();//проверяет на ошибки, убивает клетку ,если кончилась енергия, назначает цвет
    }
    
    void multiThreadPaintInitial() {
    	if(PAINT_MODE==1 || PAINT_MODE==3 || PAINT_MODE==4) {
        	Runnable task = () -> {
        		Graphics worldGraphics=this.getGraphics();
        		while(true) {
        			while(!windowClosed) {
        				if(!this.getPause()) {
        					if(PAINT_MODE==1 ) {
        						painter.ecoPaint();
        					}
        					else if(PAINT_MODE==3 ) {
        						painter.fullPaint();
        					}
        					else if(PAINT_MODE==4 ) {
        						painter.combinedPaint();
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
        			try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Автоматически созданный блок catch
						e.printStackTrace();
					}
        		}
        };
    	new Thread(task).start();
	}
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	painter.fullPaint();
    }
    
    void setCoordinates(NormCell curNormCell,int x,int y) {
    	curNormCell.setX(x);
		curNormCell.setY(y);
    }
    
    void cellDiagnostic(NormCell norm,ArrayList<Double[]> inputData) {
    	System.out.println("Run Cell diag.");
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

    void addWinListnerToFrame() {
    	frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				
				windowClosed=false;
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				windowClosed=true;
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				windowClosed=false;
			}
			
			
			@Override
			public void windowClosed(WindowEvent e) {
				windowClosed=true;
			}

			@Override
			public void windowClosing(WindowEvent e) {
				windowClosed=true;
			}

			@Override
			public void windowActivated(WindowEvent e) {
				windowClosed=false;
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				windowClosed=true;
			}
			
			
		});
    }

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(cells.length * cellSize, cells[0].length * cellSize);
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		// TODO Автоматически созданная заглушка метода
		return 0;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		// TODO Автоматически созданная заглушка метода
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

}
