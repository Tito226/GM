package MyVersion.Cells;

import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Network;
import MyVersion.Core.Network_Like;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import MyVersion.Frame.Action_Boundaries;
import MyVersion.Frame.Wrappers.NetworkWrapper;
import MyVersion.Frame.Wrappers.NetworkWrapperLike;
import MyVersion.Frame.Wrappers.WrapperChooser;

import static MyVersion.Core.Core_Config.*;
import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.World.*;

public class NormCell implements Serializable, LiveCell {
	// *****************************************
	public NetworkWrapperLike brain;// Use while myParts.size()==0
	public NetworkWrapperLike multiCellBrain;// Use while myParts.size()>0
	public String causeOfDeath;
	Random r=new Random();
	public boolean selected=false;
	// *****************************************
	public int multiplies=0;
	int energy;
	public int lifeTime=0;
	int partNum=0;
	private int x, y;
	byte counter=0;
	float readyToMultiply=0.0f;
	float[] outputs;
	long myParentNum, myChildNum=r.nextLong();
	static long num=0L;
	private final long myNum;
	private boolean tested=false;
	public boolean bited=false;
	private double lastOutput=0d;
	double preLastOutput=0d;
	public String partName="part";
	// *****************************************
	public ArrayList<LiveCell> myParts=new ArrayList<>();
	ArrayList<LiveCell> myPartsBuffer;
	public static DataMethods myMethods=new DataMethods();
	NormCellType normCellType=NormCellType.MOVABLE;
	public Semaphore sem=new Semaphore(1);
	Color myColor=Color.green;

	public NormCell(Network_Like brain, Network_Like multiCellBrain) {
		Random r=new Random();
		this.brain=WrapperChooser.getRightWrapper(brain);
		this.multiCellBrain=WrapperChooser.getRightWrapper(multiCellBrain);
		myNum=num;
		num++;
		energy=NORM_CELL_START_ENERGY;
		if (!DEBUG) {
			if (r.nextInt(MUTATION_CHANCE)==0) {
				this.brain.mutate(NUMBER_OF_MUTATIONS);
			}
			if (r.nextInt(MUTATION_CHANCE)==0) {
				this.multiCellBrain.mutate(NUMBER_OF_MUTATIONS);
			}
		}
		synchronized (cells) {
			normCells.add(getHead());
		}
	}

	public long getMyNum() {
		return myNum;
	}

	/** Calls move(Cell nextCell) */
	public void move(Directions d) {
		Random random=new Random();
		int i5=random.nextInt(2);
		switch (d) {/* TODO THINK ABOUT */
		case DOWN -> {
			if (y<height-1) {
				move(cells[x][y+1]);
			}
		}
		case UP -> {
			if (y>0) {
				move(cells[x][y-1]);
			}
		}
		case LEFT -> {
			if (x>0) {
				move(cells[x-1][y]);
			}
		}
		case RIGHT -> {
			if (x<width-1) {
				move(cells[x+1][y]);
			}
		}

		}

		energy-=1; /* TODO ПЕРЕСМОТРЕТЬ */

	}

	private void move(Cell nextCell) {// TODO MAKE CELL EAT ON MOVE

		if (nextCell.liveCell==null&&myParts.size()==0) {
			nextCell.setLiveCell(this);
			cells[x][y].setLiveCell(null);
			this.setX(nextCell.getX());
			this.setY(nextCell.getY());
		} else if (nextCell.liveCell!=null) {
			eatCell(nextCell);
			if (nextCell.liveCell==null) {
				move(nextCell);
			}
		}

	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x=x;
	}

	public void setY(int y) {
		this.y=y;
	}

	public void idleEnergyDecrese() {
		if (lifeTime%2==0) {// 2
			energy--;
		}
	}
	/**Do all things,that must be done in the end of step() (set all last variables,idle energy decrese,increse life time,call test() method)*/
	void doEndThings() {
		setLastThings();
		idleEnergyDecrese();
		lifeTime++;
		test();// TODO выяснить причину проблемы которую рещает етот костыль(метод test()
				// должен вызыватся только в Cell)
	}

	void step1() {
		myPartsBuffer=new ArrayList<LiveCell>(myParts);
		double output=calculateOutput();
		preLastOutput=getLastOutput();
		lastOutput=output;
		if (output>Action_Boundaries.multiplyBoundaries[0]&&output<Action_Boundaries.multiplyBoundaries[1]) {
			myMethods.multiply(this);
		} else if (output>0.64&&output<0.68) {
			new Protoplast(this,output);
		}
		for (LiveCell curCell : myPartsBuffer) {
			curCell.step();
		}
		doEndThings();
	}
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/**Sets color by type*/
	void checkMyType() {
		if (myParts.size()==0) {
			normCellType=NormCellType.MOVABLE;
			setMyColor(Color.green);
		} else if (myParts.size()>0) {
			normCellType=NormCellType.CONTROLLER;
			setMyColor(Color.ORANGE);
		}
	}

	@Override
	public void step() {// TODO Step
		if (energy<0) {
			System.err.println("I must be dead NormCell: 179");
		}
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (normCells.contains(this)&&brain!=null) {
			// System.out.println("stepping1 :"+sem.availablePermits());
			checkMyType();
			if (normCellType==NormCellType.CONTROLLER) {
				step1();
				sem.release();
				return;
			}
			if (myMethods.isSpaceAvailable(this)==0) {
				energy-=1;
			}
			double output=calculateOutput();
			// (SimplifiedNetwork)brain.print();
			// System.out.println(output);
			if (DEBUG) {
				// if(cells[x][y].organic<=0 &&
				// between(Action_Boundaries.eatOrganicBoundaries,output) ) {
				double[] inputBuff=new double[HOW_MUCH_INPUTS_MUST_BE_USED+10];
				for (int i=0; i<inputBuff.length; i++) {
					inputBuff[i]=getInputData()[i];
				}
				System.out.println(Arrays.toString(inputBuff)+"  ");
				System.out.println("output: "+output);
				System.out.println("--------------------------------------------------");
				try {
					Thread.sleep(DEBUG_TIME_AFTER_STEP);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// }
			}
			preLastOutput=getLastOutput();
			lastOutput=output;

			checkMoveBoundaries(output);
			if (DataMethods.between(Action_Boundaries.eatOrganicBoundaries,output)) {
				myMethods.eatOrganic(this);
			} else if (DataMethods.between(Action_Boundaries.eatRightCellBoundaries,output)) {
				if (!eatCell(Directions.RIGHT)) {
					energy--;
				}

			} else if (DataMethods.between(Action_Boundaries.eatLeftCellBoundaries,output)) {
				if (!eatCell(Directions.LEFT)) {
					energy--;
				}

			} else if (DataMethods.between(Action_Boundaries.eatUpCellBoundaries,output)) {
				if (!eatCell(Directions.UP)) {
					energy--;
				}

			} else if (DataMethods.between(Action_Boundaries.eatDownCellBoundaries,output)) {
				if (!eatCell(Directions.DOWN)) {
					energy--;
				}
			} else if (DataMethods.between(Action_Boundaries.multiplyProtoplastBoundaries,output)) {
				new Protoplast(this,output);
			} else if (DataMethods.between(Action_Boundaries.multiplyRootBoundaries,output)) {
				new RootCell(this,output);
			} else if (DataMethods.between(Action_Boundaries.multiplyBoundaries,output)) {
				myMethods.multiply(this);
			}
			doEndThings();
		} else {
			// System.err.println("Im dead, leave me alone , harakiri NormCell: 246");
			normCells.remove(this);
			cells[x][y].setLiveCell(null);
		}
		sem.release();
		// System.out.println("stepped :"+sem.availablePermits());

	}
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/***/
	boolean checkMoveBoundaries(double output) {

		if (DataMethods.between(Action_Boundaries.moveUpBoundaries,output)) {
			move(Directions.UP);
			return true;
		} else if (DataMethods.between(Action_Boundaries.moveDownBoundaries,output)) {
			move(Directions.DOWN);
			return true;
		} else if (DataMethods.between(Action_Boundaries.moveLeftBoundaries,output)) {
			move(Directions.LEFT);
			return true;
		} else if (DataMethods.between(Action_Boundaries.moveRightBoundaries,output)) {
			move(Directions.RIGHT);
			return true;
		}
		return false;
	}

	public void setMyColor(Color myColor) {
		this.myColor=myColor;
	}

	@Override
	public Color getColor() {
		return myColor; // TODO set Changeable cell color
	}

	public synchronized boolean eatCell(Directions dirs) {
		// System.out.println("cell was eaten");
		switch (dirs) {

		case UP -> {
			if (y>0) {
				return eatCell(cells[x][y-1]);
			}
		}

		case DOWN -> {
			if (y<height-1) {
				return eatCell(cells[x][y+1]);
			}
		}

		case RIGHT -> {
			if (x<width-1) {
				return eatCell(cells[x+1][y]);
			}
		}

		case LEFT -> {
			if (x>0) {
				return eatCell(cells[x-1][y]);
			}
		}

		}

		// System.out.println(done);
		return false;
	}

	private boolean eatCell(Cell nextCell) {// TODO доработать
		LiveCell nextLiveCell=nextCell.liveCell;
		if (nextLiveCell!=null&&normCells.contains(nextLiveCell)) {
			Semaphore curSem=nextLiveCell.getHead().sem;
			try {
				if (!curSem.tryAcquire(100,TimeUnit.MILLISECONDS)) {
					System.err.println("conflict");
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (nextLiveCell!=null) {
				energy+=nextLiveCell.getEnergy();
				nextLiveCell.getHead().bited=true;
				if (!normCells.contains(nextLiveCell)&&nextLiveCell instanceof NormCell) {
					System.err.println("!normCells.contains(nextLiveCell)"+nextLiveCell.getEnergy()+" : "
							+nextLiveCell.getClass()+" : "+((NormCell) nextLiveCell).bited);
					return false;
				}

				if (nextLiveCell instanceof NormCell) {
					NormCell norm=(NormCell) nextLiveCell;
					norm.causeOfDeath="was eaten";
				}

				nextLiveCell.kill(false);
				nextCell.setLiveCell(null);
				curSem.release();
				return true;

			} else {
				curSem.release();
				// System.out.println("Cell eaten :");
				return false;
			}

		} else {
			return false;
		}
		// return false;
	}
	
	double isController() {
		if (normCellType==NormCellType.CONTROLLER) {
			return 10.0f;
		} else
			return 0f;
	}

	int lastOrganic=0;
	int lastSize=0;
	int lastEnergy=0;
	double lastRightDistance=0d;
	double lastLeftDistace=0d;
	double lastUpDistance=0d;
	double lastDownDistance=0d;
	double lastRightUpCell=0d;
	double lastRightDownCell=0d;
	double lastLeftUpCell=0d;
	double lastLeftDownCell=0d;
	double lastUpCell=0d;
	double lastDownCell=0d;
	double lastLeftCell=0d;
	double lastRightCell=0d;
	/**Sets all last... variables (last...=this...)*/
	void setLastThings() {
		lastEnergy=energy;
		lastUpCell=myMethods.getUpCell(this);
		lastDownCell=myMethods.getDownCell(this);
		lastLeftCell=myMethods.getLeftCell(this);
		lastRightCell=myMethods.getRightCell(this);
		lastOrganic=cells[x][y].organic;
		lastSize=myParts.size();
		lastRightDistance=myMethods.getRightDistance(this);
		lastLeftDistace=myMethods.getLeftDistance(this);
		lastUpDistance=myMethods.getUpDistance(this);
		lastDownDistance=myMethods.getDownDistance(this);
		lastRightUpCell=myMethods.getRightUpCell(this);
		lastRightDownCell=myMethods.getRightDownCell(this);
		lastLeftUpCell=myMethods.getLeftUpCell(this);
		lastLeftDownCell=myMethods.getLeftDownCell(this);
	}
	/**calls brain.calculateOutput() method*/
	public double calculateOutput() {// TODO СДЕЛАТЬ ДЕЛЕНИЕ НА КОНСТАНТУ
		return brain.calculateOutput(getInputData(),false)[0];
	}
	/**calls brain.calculateOutput() method with custom inputs*/
	public double calculateOutput(Double[] inputData) {
		return brain.calculateOutput(inputData,false)[0];
	}
	// *********************************************

	int enValue=10;

	// ************************************************

	public int getLifeTime() {
		return lifeTime;
	}

	public int getX() {
		return x;
	}

	public void setEnergy(int energy) {
		this.energy=energy;
	}

	@Override
	public int getEnergy() {
		return energy;
	}

	public double getEnergyInput() {
		return energy/myMethods.ENERGY_DILL;
	}

	public Double[] getInputData() {
		Double[] inputs= { myMethods.isRaedyToMultiply(this), getEnergyInput(),
				(double) cells[x][y].getOrganic()/myMethods.ORGANIC_DILL, myMethods.getUpCell(this),
				myMethods.getDownCell(this), myMethods.getLeftCell(this),

				(double) myMethods.getRightCell(this), getLastOutput(), preLastOutput,
				(double) myMethods.getRightDownCell(this), myMethods.getRightUpCell(this),
				myMethods.getLeftUpCell(this), myMethods.getLeftDownCell(this), myMethods.isSpaceAvailable(this),
				isController(),

				myMethods.getRightDistance(this), myMethods.getLeftDistance(this), myMethods.getUpDistance(this),
				myMethods.getDownDistance(this), (double) lastEnergy, lastUpCell, lastDownCell, lastLeftCell,

				lastRightCell, lastRightDownCell, lastRightUpCell, lastLeftDownCell, lastLeftUpCell,
				(double) lastOrganic, (double) sunny, (double) myParts.size(),

				(double) lastSize, lastRightDistance, lastLeftDistace, lastUpDistance, lastDownDistance };
		// System.out.println(Arrays.toString(inputs));
		return inputs;
	}

	public static int maxEnergy=500;
	/**Tests this cell, if cell must die, kills it,if bited==true calls testMyСontinuity()*/
	@Override
	public void test() {
		if (energy<=0||energy>=maxEnergy||lifeTime>NORMCELL_MAX_LIFETIME||myParts.size()>32
				||cells[x][y].organic>CRITICAL_ORGANIC_VALUE) {
			if (brain!=null) {
				this.kill(true);
			} else {
				System.out.println("brain is already null");
			}
			this.causeOfDeath="Killed by test";
		}
		if (bited) {
			testMyСontinuity();
			bited=false;
		}
	}
	
	void testMyСontinuity() {
		testNeighbours(this);
		myPartsBuffer=new ArrayList<LiveCell>(myParts);
		for (LiveCell curCell : myPartsBuffer) {
			if (!curCell.getTested()) {
				curCell.kill(true);
			} else {
				curCell.setTested(false);
			}
		}
	}

	void testNeighbours(LiveCell curCell) {// TODO test it
		int x=curCell.getX();
		int y=curCell.getY();
		if (x-1>0&&cells[x-1][y].liveCell!=null) {
			testNeighbour(cells[x-1][y].liveCell);
		}
		if (x+1<width-1&&cells[x+1][y].liveCell!=null) {
			testNeighbour(cells[x+1][y].liveCell);
		}
		if (y-1>0&&cells[x][y-1].liveCell!=null) {
			testNeighbour(cells[x][y-1].liveCell);
		}
		if (y+1<height-1&&cells[x][y+1].liveCell!=null) {
			testNeighbour(cells[x][y+1].liveCell);
		}
	}

	void testNeighbour(LiveCell curCell) {
		if (myParts.contains(curCell)&&!curCell.getTested()) {
			curCell.setTested(true);
			testNeighbours(curCell);
		}
	}

	@Override
	public synchronized void kill(boolean spreadOrganic) {
		this.brain.setIsDead(true);
		myPartsBuffer=new ArrayList<LiveCell>(myParts);
		if (spreadOrganic) {
			Cell.organicSpreadOnDeath(this);
		}

		if (!this.brain.getDontDelete()) {
			// this.brain.kill();
			// this.multiCellBrain.kill();
			// this.multiCellBrain=null;
		}
		this.brain=null;
		synchronized (cells) {
			for (LiveCell curCell : myPartsBuffer) {
				curCell.kill(spreadOrganic);// TODO stub
			}
			cells[x][y].setLiveCell(null);
			normCells.remove(this);
			if (normCells.contains(this)) {
				while (normCells.contains(this)) {
					normCells.remove(this);
				}
			}
		}
	}

	@Override
	public Integer getGeneralEnergy() {
		int generalEnergy=energy;
		for (LiveCell curPart : myParts) {
			generalEnergy+=curPart.getEnergy();
		}
		return generalEnergy;
	}

	@Override
	public NormCell getHead() {
		return this;
	}

	@Override
	public int getEnergyToMultiplyMe() {
		return ENERGY_NEEDED_TO_MULTIPLY;
	}
	/**Calls this.kill() method */
	@Override
	public void apoptosis() {
		this.causeOfDeath="Killed by apoptosis";
		this.kill(true);
	}

	@Override
	public boolean getTested() {
		return tested;
	}

	@Override
	public void setTested(boolean value) {
		tested=value;
	}

	public double getLastOutput() {
		return lastOutput;
	}

}