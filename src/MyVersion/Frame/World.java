package MyVersion.Frame;

import static MyVersion.Frame.FRAME_CONFIG.CELL_START_ORGANIC;
import MyVersion.Cells.Cell;
import MyVersion.Cells.Genome;
import MyVersion.Cells.NormCell;
import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Network;
import MyVersion.Core.Network_Like;
import MyVersion.Core.Network_Teacher;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import static MyVersion.Frame.FRAME_CONFIG.CELL_SIZE;
import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.FRAME_CONFIG.PAINT_MODE;
/**
 * This class represents the world where cells live and interact. 
 * It handles the simulation, including initializing cells, managing the grid, 
 * and running the main simulation loop. It also manages important statistics 
 * like the number of steps and the best-performing cells.
 * 
 * Implements Runnable to run the simulation in a separate thread.
 */
public class World implements Runnable {
	WorldFrame worldFrame;
	public Network_Like[] relative=new Network_Like[2];
	// public static int cellls=0;
	public volatile static int cellSize=CELL_SIZE;
	public int slowdown=0;
	public static int realWidth;
	public static int realHeight;
	public static int width;
	public static int height;
	public static int sunny=3;
	public volatile Cell[][] cells;
	private static volatile boolean pause=false;
	public static ArrayListWrapper<NormCell> normCells;
	// which is not exist in cells)
	ExecutorService pool=Executors.newFixedThreadPool(2); //TODO зделать возможным изменение числа потоков в рантайме
	Phaser phaser=new Phaser(1);
	ArrayList<NormCell> buffer;
	Thread wor;

	public World(int width, int height, int realWidth, int realHeight) throws IOException {
		normCells=new ArrayListWrapper<NormCell>(this);
		Network_Teacher network_teacher=new Network_Teacher();
		if (!LOAD_SAVE) {
			Network buff=network_teacher.createAndTeachNetwork();
			// relative[1]=relative[0];
			relative[0]=BrainCloneClass.convertFromBasicNetworkToSimplified(buff);
			relative[1]=relative[0];
		} else {
			/*
			 * try { FileInputStream fileInputStream = new
			 * FileInputStream("C:\\Users\\Timurs1\\Desktop\\BrainSave.network"); byte[]
			 * buff=fileInputStream.readAllBytes(); fileInputStream.close();
			 * ByteArrayInputStream fis = new ByteArrayInputStream(buff); ObjectInputStream
			 * ois = new ObjectInputStream(fis); relative=(Network[]) ois.readObject();
			 * ois.close(); fis.close(); } catch (ClassNotFoundException | IOException e) {
			 * e.printStackTrace(); }
			 */
		}
		if (!AUTO_SIZE) {
			cellSize=1;
		}
		network_teacher=null;
		this.height=height;
		this.width=width;
		this.realHeight=realHeight;
		this.realWidth=realWidth;
		cells=new Cell[width][height];

		WorldFrame.createWorldFrame(this);
	}

	synchronized boolean getPause() {
		return pause;
	}

	synchronized static void setPause(boolean p) {
		pause=p;
	}

	public Cell[][] getCells() {
		return cells;
	}

	/** Creates JFrame with all buttons,scrollers and ect. */

	public static void main(String[] args) throws IOException {
		int width=1250;
		int height=700;
		World world;
		if (AUTO_SIZE) {
			world=new World(width/CELL_SIZE-3,height/CELL_SIZE-3,width,height);
		} else {
			world=new World(width,height,width,height);
		}
		System.out.println(width);
		System.out.println(world.width);

		world.wor=new Thread(world,"World");
		world.wor.start();

	}

	public int stepsAtAll=0;
	public int Restarts=0;
	public volatile int sps=0;// steps(one main cycle turn) per seconds
	public volatile int liveCells=0;
	public static int bestLifeTime=0;
	public static int bestMultiplies=-1;
	public static int thisBestLifeTime=0;
	public static int lastBestLifeTime=0;
	public static int lastLastBestLifeTime=0;
	public static int lastRestarts=0;
	public static int thisBestSize=0;
	// public long fpsMeter1=0;

	ConcurrentHashMap<Network_Like[], Genome> lastLeftBrains=new ConcurrentHashMap<Network_Like[], Genome>();
	static FileOutputStream fileOutputStream;
	static ObjectOutputStream objectOutputStream;

	// Graphics worldGraphics=worldFrame.getGraphics();
	boolean tested=false;

	long startTime=System.currentTimeMillis();

	/**
	 * Initializes the cells in the world grid, assigning random positions 
	 * to new cells and preparing the graphical painter.
	 * This method is typically called at the start of the simulation.
	 */
	void worldInitial() {
		// addListsToBestBrains();
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				cells[x][y]=new Cell(this);
				if (cells[x][y]!=null) {
					cells[x][y].setX(x);
					cells[x][y].setY(y);
				}
			}
		}
		for (int i=0; i<CELLS_ON_START; i++) {
			Random r=new Random();
			cells[r.nextInt(width)][r.nextInt(height)].setLiveCell(new NormCell(relative[0],relative[1],new Genome(),this));
		}
		worldFrame.painter.painterInitial();
	}

	long timeBuff;
	long stepsBuff;
	public int maxThreads=1;
	/**
	 * Compares the current cell's statistics with the best cells so far.
	 * Updates the best life time, best multiplication count, and best size
	 * if the current cell exceeds the previous best values.
	 *
	 * @param curNormCell The current NormCell being evaluated.
	 */
	synchronized void findGoodCell(NormCell curNormCell) {
		if (curNormCell.getLifeTime()>bestLifeTime) {
			bestLifeTime=curNormCell.getLifeTime();
		}
		if (curNormCell.getLifeTime()>thisBestLifeTime) {
			thisBestLifeTime=curNormCell.getLifeTime();
		}
		if (curNormCell.multiplies>bestMultiplies) {
			bestMultiplies=curNormCell.multiplies;
		}
		if (curNormCell.myParts.size()>thisBestSize) {
			thisBestSize=curNormCell.myParts.size();
		}
	}
	/**
	 * Saves the last surviving cells into a map for later use. 
	 * It stores their neural networks and genomes. 
	 * If there are more cells than allowed to save, 
	 * the method will remove older entries from the map.
	 */
	void saveLastCells() {
		if (normCells.size()<=HOW_MANY_LAST_CELLS_TO_SAVE) {
			for (NormCell n : normCells) {
				if (n!=null) {
					lastLeftBrains.put(new Network_Like[] { n.brain, n.multiCellBrain },n.genome);
				}
			}
		} else {
			for (NormCell n : normCells) {
				if (n!=null) {
					Network_Like[] curBrain=new Network_Like[] { n.brain, n.multiCellBrain };
					if (!lastLeftBrains.containsKey(curBrain)) {
						lastLeftBrains.remove(curBrain);

					}
				}
			}
		}
	}
	/**
	 * The main loop of the simulation. This method runs continuously while 
	 * updating the state of the world, including the cells and their interactions. 
	 * It checks for pauses, manages the cell steps, handles restarts when needed, 
	 * and performs garbage collection at regular intervals.
	 */
	@Override
	public void run() {
		stepsBuff=0;
		// ArrayList<Double[]> inputData=new ArrayList<Double[]>();
		worldInitial();
		timeBuff=System.currentTimeMillis();
		/* the main cycle */
		while (true) {

			if (!getPause()) {

				calculateSPS();

				stepsAtAll++;

				testAllCells();

				stepsCycle();

				findArrayNullElements();

				testNormCellsArray();

				liveCells=normCells.size();
				if (normCells.size()==0) {/* on restart */
					onRestart();
				}
				
				if (System.currentTimeMillis()-startTime>30*1000) {
					startGC();
				}
				
				paint();
			} else {

				if (PAINT_MODE==0) {
					worldFrame.painter.fullPaint();
				}
				sleep(400);
			}

		}

	}

	void paint() {
		/**************** 0 thread paint ****************/
		if (PAINT_MODE==0) {
			worldFrame.painter.fastPaint();
		} else if (PAINT_MODE==-1) {// 0
			worldFrame.painter.fullPaint();
		}
		if (slowdown>0) {
			sleep(slowdown);
		}
		/**********************************************/
	}
	/**
	 * Calculates the number of steps per second (SPS) that the simulation is performing. 
	 * It updates the SPS value every 400 milliseconds by comparing the number of steps 
	 * taken in that time.
	 */
	public void calculateSPS() {
		if (System.currentTimeMillis()-timeBuff>=400) {
			timeBuff=System.currentTimeMillis();
			sps=(int) ((stepsAtAll-stepsBuff)*2.5);
			stepsBuff=stepsAtAll;
		}
	}

	private void onRestart() {
		worldFrame.painter.fullPaint();
		worldFrame.painter.stopPainting=true;
		resetOrganic();
		saveBestBrain();

		summonCells(FRAME_CONFIG.CELLS_ON_START);

		Restarts++;
		lastRestarts++;
		//printRestartInfo();
		lastLastBestLifeTime=lastBestLifeTime;
		lastBestLifeTime=bestLifeTime;
		thisBestLifeTime=0;
		stepsAtAll=0;

		worldFrame.painter.stopPainting=false;
		lastLeftBrains.clear();
		// sleep(RESTART_DELAY);
	}

	void printRestartInfo() {
		System.out.println("Restarted");
		System.err.println("Steps :"+stepsAtAll);
		System.out.println("best life time: "+bestLifeTime);
		System.out.println("this Best Life Time: "+thisBestLifeTime);
	}

	/** calls "normCells.clear()" if in normCells left only null values */
	void testNormCellsArray() {
		boolean toClear=true;
		ArrayList<NormCell> buffer=new ArrayList<>(normCells);
		for (NormCell curCell : buffer) {
			if (curCell!=null) {
				toClear=false;
			}
		}
		if (toClear) {
			normCells.clear();
		}
	}
	/**
	 * Executes one cycle of steps for all cells in the simulation. 
	 * It registers all cells with the Phaser to synchronize the execution.
	 * After all cells have completed their step, it shuffles the order 
	 * to avoid predictable patterns in the simulation.
	 */
	public void stepsCycle() {
		buffer=new ArrayList<NormCell>(normCells);// to avoid concurrent modification exception
		phaser.bulkRegister(buffer.size());// TODO OPTIMIZE PHISER
		for (NormCell curNormCell : buffer) {

			Runnable task=() -> {
				if (normCells.contains(curNormCell) && curNormCell!=null) {
					curNormCell.step();// TODO понять почему в масиве не удаляются мертвые

				}
				phaser.arriveAndDeregister();

			};
			pool.execute(task);

		}
		phaser.arriveAndAwaitAdvance();
		Collections.shuffle(normCells);
	}

	private void findArrayNullElements() {
		for (NormCell curCell : normCells) {
			if (curCell!=null) {
				if (curCell.brain==null) {
					curCell.selected=true;
					worldFrame.cell_inf.selectedLiveCell=curCell;
					System.err.println("shet!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					Thread.currentThread().stop();
				}
				findGoodCell(curCell);
			}
		}
	}

	/**
	 * Calls the `testCell()` method for every cell in the world grid.
	 * This method ensures that each cell updates its status after each step.
	 */
	void testAllCells() {
		for (int i=0; i<width; i++) {// очистка состояния(сделал ход)
			for (int j=0; j<height; j++) {
				Cell curCell=cells[i][j];
				curCell.testCell();
			}
		}
	}

	void setCoordinates(NormCell curNormCell, int x, int y) {
		curNormCell.setX(x);
		curNormCell.setY(y);
	}


	/**summon new cells with genes and network from lastLeftBrains*/
	public void summonCells(int cellsNum) {
		Random r=new Random();
		ArrayList<Network_Like[]> keys=new ArrayList<Network_Like[]>(lastLeftBrains.keySet());
		for (int i=0; i<cellsNum; i++) {
			NormCell nBuf=null;
			int buff=r.nextInt(keys.size());//keys.size()==0
			Network_Like[] curKey=keys.get(buff);
			if(curKey[0]==null) {
				i--;
				continue;
			}
			nBuf=new NormCell(curKey[0],curKey[1],
					new Genome(lastLeftBrains.get(curKey)),this);/* TODO STUB , зделать созхранение генома */
			cells[r.nextInt(width)][r.nextInt(height)].setLiveCell(nBuf);
		}
	}

	public void resetOrganic() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				cells[j][i].organic=CELL_START_ORGANIC;
			}
		}
	}
	/**
	 * Triggers the Java garbage collector to clean up memory.Calls after 30 seconds(in main cycle) of simulation time.
	 * This helps to free up resources and maintain performance in long-running simulations.
	 */
	void startGC() {
		System.out.println("Garbage collector started");
		System.gc();
		startTime=System.currentTimeMillis();
	}

	void saveBestBrain() {
		// TODO stub
	}

	void sleep(int miilis) {
		try {
			Thread.sleep(miilis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getStepsAtAll() {
		return stepsAtAll;
	}

		
}
