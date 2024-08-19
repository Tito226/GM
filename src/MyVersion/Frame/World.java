package MyVersion.Frame;

import static MyVersion.Frame.FRAME_CONFIG.CELL_START_ORGANIC;

import MyVersion.Cells.Cell;
import MyVersion.Cells.LiveCell;
import MyVersion.Cells.NormCell;
import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Data_Set;
import MyVersion.Core.Network;
import MyVersion.Core.Network_Like;
import MyVersion.Core.Network_Teacher;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Semaphore;
import static MyVersion.Frame.FRAME_CONFIG.CELL_SIZE;
import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.FRAME_CONFIG.PAINT_MODE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

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
	public static int sunny=1;
	public static Cell[][] cells;
	private static boolean pause=false;
	public static ArrayList<NormCell> normCells=new ArrayList<>();// TODO fix bugs(contains dead cells,contains cell
																	// which is not exist in cells)
	ArrayList<NormCell> buffer;
	Thread wor;

	public World(int width, int height,int realWidth,int realHeight) throws IOException {
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

	public static Cell[][] getCells() {
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

		world.wor=new Thread(world);
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
	public long fpsMeter1=0;

	public static Network_Like[] thisTopLifeTimeBrain=new Network_Like[2];
	public static Network_Like[] topLifeTimeBrain=new Network_Like[2];
	public static Network_Like[] topMultipliesBrain=new Network_Like[2];
	public static Network_Like[] thisTopSizeBrain=new Network_Like[2];

	ArrayList<LimitedArrayList<Network_Like[]>> bestBrainsArrs=new ArrayList<LimitedArrayList<Network_Like[]>>();
	LimitedArrayList<Network_Like[]> bestLifeTimeBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
	LimitedArrayList<Network_Like[]> bestThisLifeTimeBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
	LimitedArrayList<Network_Like[]> bestMultipliesBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);
	LimitedArrayList<Network_Like[]> thisBiggestSizeBrains=new LimitedArrayList<>(LIMITED_ARRAY_SIZE);

	// ExecutorService pool=Executors.newFixedThreadPool(1);
	static FileOutputStream fileOutputStream;
	static ObjectOutputStream objectOutputStream;

	void addListsToBestBrains() {
		bestBrainsArrs.add(bestLifeTimeBrains);
		bestBrainsArrs.add(bestThisLifeTimeBrains);
		bestBrainsArrs.add(bestMultipliesBrains);
		bestBrainsArrs.add(thisBiggestSizeBrains);
	}

	//Graphics worldGraphics=worldFrame.getGraphics();
	boolean tested=false;

	long startTime=System.currentTimeMillis();

	Network_Like[] topBrainChoose(NormCell curNormCell, Network_Like[] curTopBrain,
			LimitedArrayList<Network_Like[]> bestBrains) {
		synchronized (this) {
			try {
				curNormCell.sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/*
			 * if(curTopBrain!=null && curNormCell.brain!=null && curNormCell.brain.isDead)
			 * { for (int k = 0; k < curTopBrain.length; k++) { curTopBrain[k].kill(); } }
			 */
			curTopBrain[0]=curNormCell.brain;
			curTopBrain[1]=curNormCell.multiCellBrain;
			bestBrains.add(curTopBrain);
			curNormCell.brain.setDontDelete(true);
			curNormCell.multiCellBrain.setDontDelete(true);
			curNormCell.sem.release();
			return curTopBrain;
		}
	}
	// ExecutorService executor = Executors.newFixedThreadPool(2);

	synchronized void findGoodCell(NormCell curNormCell) {
		if (curNormCell.getLifeTime()>bestLifeTime) {
			bestLifeTime=curNormCell.getLifeTime();
			topBrainChoose(curNormCell,topLifeTimeBrain,bestLifeTimeBrains);
		}
		if (curNormCell.getLifeTime()>thisBestLifeTime) {
			thisBestLifeTime=curNormCell.getLifeTime();
			topBrainChoose(curNormCell,thisTopLifeTimeBrain,bestThisLifeTimeBrains);
		}
		if (curNormCell.multiplies>bestMultiplies) {
			bestMultiplies=curNormCell.multiplies;
			topBrainChoose(curNormCell,topMultipliesBrain,bestMultipliesBrains);
		}
		if (curNormCell.myParts.size()>thisBestSize) {
			thisBestSize=curNormCell.myParts.size();
			topBrainChoose(curNormCell,thisTopSizeBrain,thisBiggestSizeBrains);
		}
	}

	/** Sets cells coordinates(x,y) and calls addListsToBestBrains(); */
	void worldInitial() {
		addListsToBestBrains();
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				cells[x][y]=new Cell();
				if (cells[x][y]!=null) {
					cells[x][y].setX(x);
					cells[x][y].setY(y);
				}
			}
		}
		for (int i=0; i<CELLS_ON_START; i++) {
			Random r=new Random();
			cells[r.nextInt(width)][r.nextInt(height)].setLiveCell(new NormCell(relative[0],relative[1]));
		}
		worldFrame.painter.painterInitial();
	}

	int maxThreads=1;
	Semaphore sem=new Semaphore(maxThreads);

	@Override
	public void run() {
		long stepsBuff=0;
		ArrayList<Double[]> inputData=new ArrayList<Double[]>();
		worldInitial();
		long timeBuff=System.currentTimeMillis();
		/* the main cycle*/
		while (true) {
			fpsMeter1=System.currentTimeMillis();
			boolean isStep=false;

			if (!getPause()) {
				
				if (System.currentTimeMillis()-timeBuff>=300) {
					timeBuff=System.currentTimeMillis();
					sps=(int) (stepsAtAll-stepsBuff);
					stepsBuff=stepsAtAll;
				}
				
				Graphics worldGraphics=worldFrame.getGraphics();
				stepsAtAll++;

				testAllCells();

				buffer=new ArrayList<NormCell>(normCells);// to avoid concurrent modification exception
				buffer.parallelStream().forEach(curNormCell -> {
					try {
						sem.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (normCells.contains(curNormCell) && curNormCell!=null) {
						curNormCell.step();// TODO понять почему в масиве не удаляются мертвые
						if (Restarts<4 && DEBUG) {
							inputData.add(curNormCell.getInputData());
						}

					}
					sem.release();
				});

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

				testNormCellsArray();
				Collections.shuffle(normCells);
				
				if (!(Restarts<4 && DEBUG)) {
					inputData=null;
				}
				/*
				if (!isStep) {
					System.out.println("shit");
				}
				*/
				liveCells=normCells.size();
				/* reset field organic */
				if (normCells.size()==0) {/* on restart */
					/*
					if (lastBestLifeTime==thisBestLifeTime && thisBestLifeTime!=0 && !tested) {
						cellDiagnostic(new NormCell(relative),inputData);
						tested=true;
					}
					*/
					onRestart();
				}

				if (System.currentTimeMillis()-startTime>30*1000) {
					startGC();
				}
				/**************** 0 thread paint ****************/
				if (PAINT_MODE==0) {
					worldFrame.painter.fastPaint();
				} else if (PAINT_MODE==-1) {// 0
					worldFrame.painter.fullPaint();
				}
				if (slowdown>0) {
					try {
						Thread.sleep(slowdown);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				/**********************************************/
			} else {

				if (PAINT_MODE==0) {
					worldFrame.painter.fullPaint();
				}
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private void onRestart() {
		Graphics worldGraphics=worldFrame.getGraphics();
		Random r=new Random();
		// установка начального состояния органики,если все умерли
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				cells[j][i].organic=CELL_START_ORGANIC;
			}
		}
		saveBestBrain();
		//ArrayList<Network> checkGroup=new ArrayList<>();
		
		// Summon cells
		for (int i=0; i<FRAME_CONFIG.CELLS_ON_START; i++) {
			NormCell nBuf=null;
			int buff=r.nextInt(bestBrainsArrs.size());
			LimitedArrayList<Network_Like[]> bestBrainsArr=bestBrainsArrs.get(buff);
			if (bestBrainsArr.size()>0) {
				Network_Like[] curBrain=bestBrainsArr.get(r.nextInt(bestBrainsArr.size()));
				nBuf=new NormCell(curBrain[0],curBrain[1]);
				cells[r.nextInt(width)][r.nextInt(height)].setLiveCell(nBuf);
			} else {
				i--;
			}
		}
		Restarts++;
		// +++++++++++++++++++
		worldFrame.painter.fullPaint();
		
		checkGroup=null;
		lastRestarts++;
		System.out.println("Restarted");
		System.err.println("Steps :"+stepsAtAll);
		System.out.println("best life time: "+bestLifeTime);
		System.out.println("this Best Life Time: "+thisBestLifeTime);
		lastLastBestLifeTime=lastBestLifeTime;
		lastBestLifeTime=bestLifeTime;
		thisBestLifeTime=0;
		stepsAtAll=0;
	}

	void startGC() {
		System.out.println("Garbage collector started");
		System.gc();
		startTime=System.currentTimeMillis();
	}

	void saveBestBrain() {
		Random r=new Random();
		if (thisBestLifeTime>CREATE_SAVE_ON_LIFETIME /* && thisBestLifeTime>lastBestLifeTime */ && CREATE_SAVES) {// TODO
			BrainSaver.saveBestBrain(thisBiggestSizeBrains.get(0),thisBestLifeTime);// STUB
		}
	}

	void testNormCellsArray() {
		boolean toClear=true;
		ArrayList<NormCell> buffer=new ArrayList<>(normCells);
		for (NormCell curCell : buffer) {
			if (curCell!=null) {
				toClear=false;
			}
		}
		if (toClear) {
			// System.err.println("giga shit");
			normCells.clear();
		}
	}

	/** Calls test() method in every cell in cells[][] */
	void testAllCells() {
		for (int i=0; i<width; i++) {// очистка состояния(сделал ход)
			for (int j=0; j<height; j++) {
				Cell curCell=cells[i][j];
				curCell.testCell();
			}
		}
	}

	void testCell(Cell curCell) {
		/*if (curCell.liveCell!=null && curCell.liveCell instanceof NormCell) {
			NormCell curNormCell=(NormCell) curCell.liveCell;
		}*/ /* TODO реализовать механизм восстановления органики */
		curCell.testCell();// проверяет на ошибки, убивает клетку ,если кончилась енергия, назначает цвет
	}

	void setCoordinates(NormCell curNormCell, int x, int y) {
		curNormCell.setX(x);
		curNormCell.setY(y);
	}

	void cellDiagnostic(NormCell norm, ArrayList<Double[]> inputData) {
		System.out.println("Run Cell diag.");
		System.out.print(":::: "+norm.calculateOutput(new Double[] { 0d, 15d, 6d, 0d, 0d, 0d, 0d })+"тест 1     ");
		System.out.print(":::: "+ // тесты 1 должны быть одинаковыми
				norm.calculateOutput(new Double[] { 0d, 15d, 6d, 0d, 0d, 0d, 0d, 23456d })+"тест 1     ");
		Random r=new Random();
		System.out.print(norm
				.calculateOutput(new Double[] { 0d, (double) Data_Set.rnd(1,4), (double) Data_Set.rnd(7,100),
						(double) r.nextInt(2), (double) r.nextInt(2), (double) r.nextInt(2), (double) r.nextInt(2) })
				+"тест 2               ");
		if (inputData==null)
			return;
		for (int i=0; i<inputData.size(); i++) {
			if (i%2==1)
				continue;
			for (int j=0; j<7; j++) {
				switch (j) {
				case 0:
					System.out.print("multiplyReady:");
					break;

				case 1:
					System.out.print("energy:");
					break;

				case 2:
					System.out.print("organic:");
					break;

				case 3:
					System.out.print("upCell:");
					break;
				case 4:
					System.out.print("downCell:");
					break;

				case 5:
					System.out.print("leftCell:");
					break;
				case 6:
					System.out.print("rightCell:");
					break;
				}
				Double[] buf=inputData.get(i);
				System.out.print(buf[j]+" ");
			}
			System.out.println(":::: "+norm.calculateOutput(inputData.get(i)));
			System.out.print(" ");
			System.out
					.print(norm.calculateOutput(new Double[] { 0d, (double) Data_Set.rnd(3,15), 6d, 0d, 0d, 0d, 0d }));
			System.out.print(" ");
		}
	}

	


}
