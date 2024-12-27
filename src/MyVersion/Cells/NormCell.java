package MyVersion.Cells;

import MyVersion.Core.Network_Like;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

import MyVersion.Frame.Action_Boundaries;
import MyVersion.Frame.World;
import MyVersion.Frame.Wrappers.NetworkWrapperLike;
import MyVersion.Frame.Wrappers.WrapperChooser;
import static MyVersion.Core.Core_Config.*;
import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.World.*;

/*TODO клетки в многоклеточном режиме телепортируются */
public class NormCell implements Serializable, LiveCell {
	// *****************************************
	public NetworkWrapperLike brain;// Use while myParts.size()==0
	public NetworkWrapperLike multiCellBrain;// Use while myParts.size()>0

	public Genome genome;
	byte myChromosomeNum=0;
	LiveCellType myCellType=LiveCellType.NormCell;

	NormCellInfo myInfo=new NormCellInfo();
	private String causeOfDeath;
	Random r=new Random();
	public boolean selected=false;
	// *****************************************

	volatile World world;
	volatile Cell[][] cells;

	public int multiplies=0;
	public int energy;
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
	private double lastOutput=0d, preLastOutput=0d;
	public String partName="NormCell";
	// *****************************************
	public ArrayList<LiveCell> myParts=new ArrayList<>();
	private ArrayList<LiveCell> myPartsBuffer;

	public static DataMethods myMethods=new DataMethods();

	NormCellType normCellType=NormCellType.MOVABLE;
	public Semaphore sem=new Semaphore(1);
	Color myColor=Color.green;

	/** copys genome, you can put link to genome in cunstructor */

	private NormCell(Network_Like brain, Network_Like multiCellBrain, Genome genome) {
		if (brain!=null) {
			Random r=new Random();
			this.genome=new Genome(genome);
			this.brain=WrapperChooser.getRightWrapper(brain);
			this.multiCellBrain=WrapperChooser.getRightWrapper(multiCellBrain);
			myNum=num;
			num++;
			energy=NORM_CELL_START_ENERGY;
			if (!DEBUG) {
				if (r.nextInt(100)<MUTATION_CHANCE) {
					this.brain.mutate(MAX_NUMBER_OF_MUTATIONS);
				}
				if (r.nextInt(100)<MUTATION_CHANCE) {
					this.multiCellBrain.mutate(MAX_NUMBER_OF_MUTATIONS);
				}
			}
			synchronized (NormCell.class) {
				normCells.add(getHead());
			}
		} else {
			myNum=-1;
		}
	}

	public NormCell(Network_Like brain, Network_Like multiCellBrain, Genome genome, World world) {
		this(brain,multiCellBrain,genome);
		
		if (brain==null) {
			System.out.println("!!!BRAIN IS NULL IN CONSTRUCTOR!!!  step: "+world.stepsAtAll);//were called while restart
		}
		
		this.world=world;
		cells=world.cells;
	}

	public long getMyNum() {
		return myNum;
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

	/**
	 * Do all things,that must be done in the end of step() (set all last
	 * variables,idle energy decrese,increse life time,call test() method)
	 */
	void doEndThings() {
		myInfo.setLastThings();
		idleEnergyDecrese();
		lifeTime++;
		test();
	}

	void multtiCellStep() {
		myPartsBuffer=new ArrayList<LiveCell>(myParts);

		double output=calculateOutput();

		preLastOutput=getLastOutput();
		lastOutput=output;

		if (output>Action_Boundaries.multiplyBoundaries[0] && output<Action_Boundaries.multiplyBoundaries[1]) {
			Actions.multiply(this,genome.getChromosome(myChromosomeNum));// TODO СДЕЛАТЬ ТАК, ЧТОБЫ МОЖНО ВЫБИРАТЬ //
																			// НАПРАВЛЕНИЕ ДЕЛЕНИЯ(ПО ГЕНОМУ)
		}

		for (LiveCell curCell : myPartsBuffer) {
			curCell.step();
		}
		doEndThings();
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/** Sets color by type */
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
			System.err.println("I must be dead NormCell: step()");
		}
		try {
			sem.acquire();
			if (normCells.contains(this) && brain!=null) {
				checkMyType();
				if (normCellType==NormCellType.CONTROLLER) {
					multtiCellStep();
				} else {
					if (myMethods.isSpaceAvailable(this)==0) {
						energy-=1;
					}
					double output=calculateOutput();
					if (DEBUG) {
						debug(output);
					}
					preLastOutput=getLastOutput();
					lastOutput=output;

					checkAllBoundaries(output);

					doEndThings();
				}
			} else {

				normCells.remove(this);
				if (normCells.contains(this)) {
					while (normCells.contains(this)) {
						normCells.remove(this);
					}
				}
				cells[x][y].setLiveCell(null);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			sem.release();
		}
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

	void debug(double output) {
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
	}

	void checkAllBoundaries(double output) {
		checkMoveBoundaries(output);

		checkEatCellBoundaries(output);

		if (DataMethods.between(Action_Boundaries.eatOrganicBoundaries,output)) {
			Actions.eatOrganic(this);
		} else if (DataMethods.between(Action_Boundaries.multiplyBoundaries,output)) {
			Actions.multiply(this,genome.getChromosome(myChromosomeNum));
		}
	}

	/** checks if output in move boundaries, calls move methods */
	boolean checkMoveBoundaries(double output) {

		if (DataMethods.between(Action_Boundaries.moveUpBoundaries,output)) {
			Actions.move(this,Directions.UP);
			return true;
		} else if (DataMethods.between(Action_Boundaries.moveDownBoundaries,output)) {
			Actions.move(this,Directions.DOWN);
			return true;
		} else if (DataMethods.between(Action_Boundaries.moveLeftBoundaries,output)) {
			Actions.move(this,Directions.LEFT);
			return true;
		} else if (DataMethods.between(Action_Boundaries.moveRightBoundaries,output)) {
			Actions.move(this,Directions.RIGHT);
			return true;
		}
		return false;
	}

	/** checks if output in eatCell boundaries, calls eatCell methods */
	boolean checkEatCellBoundaries(double output) {

		if (DataMethods.between(Action_Boundaries.eatUpCellBoundaries,output)) {
			Actions.eatCell(this,Directions.UP);
			return true;
		} else if (DataMethods.between(Action_Boundaries.eatDownCellBoundaries,output)) {
			Actions.eatCell(this,Directions.DOWN);
			return true;
		} else if (DataMethods.between(Action_Boundaries.eatLeftCellBoundaries,output)) {
			Actions.eatCell(this,Directions.LEFT);
			return true;
		} else if (DataMethods.between(Action_Boundaries.eatRightCellBoundaries,output)) {
			Actions.eatCell(this,Directions.RIGHT);
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

	double isController() {
		if (normCellType==NormCellType.CONTROLLER) {
			return 10.0f;
		} else
			return 0f;
	}

	/**
	 * Calculates the output of the cell by invoking the brain's neural network.
	 * This output is used to determine actions such as movement, reproduction, or
	 * eating.
	 *
	 * @return double The calculated output based on the cell's current input data.
	 */
	public double calculateOutput() {// TODO СДЕЛАТЬ ДЕЛЕНИЕ НА КОНСТАНТУ
		return brain.calculateOutput(getInputData(),false)[0];
	}

	/**
	 * Calculates the output of the cell by invoking the brain's neural network.
	 * This output is used to determine actions such as movement, reproduction, or
	 * eating.
	 *
	 * @param custom input data , as rule you can call calculateOutput()
	 * @return double The calculated output based on the cell's current input data.
	 */
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

	public static int maxEnergy=500;

	/**
	 * Tests this cell, if cell must die, kills it,if bited==true calls
	 * testMyСontinuity()
	 */
	@Override
	public void test() {
		if (energy<=0 || energy>=maxEnergy || lifeTime>NORMCELL_MAX_LIFETIME || myParts.size()>32
				|| cells[x][y].organic>CRITICAL_ORGANIC_VALUE) {
			if (brain!=null) {
				this.kill(true,"Killed by test");
			} else {
				System.out.println("brain is already null");
			}
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
			if (curCell!=null) {
				if (!curCell.getTested()) {
					curCell.kill(true);
				} else {
					curCell.setTested(false);
				}
			} else {
				System.err.println("testMyСontinuity(): for (LiveCell curCell : myPartsBuffer) ;curCell is null");
			}
		}
	}

	void testNeighbours(LiveCell curCell) {// TODO test it
		int x=curCell.getX();
		int y=curCell.getY();
		if (x-1>0 && cells[x-1][y].liveCell!=null) {
			testNeighbour(cells[x-1][y].liveCell);
		}
		if (x+1<width-1 && cells[x+1][y].liveCell!=null) {
			testNeighbour(cells[x+1][y].liveCell);
		}
		if (y-1>0 && cells[x][y-1].liveCell!=null) {
			testNeighbour(cells[x][y-1].liveCell);
		}
		if (y+1<height-1 && cells[x][y+1].liveCell!=null) {
			testNeighbour(cells[x][y+1].liveCell);
		}
	}

	void testNeighbour(LiveCell curCell) {
		if (myParts.contains(curCell) && !curCell.getTested()) {
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

		synchronized (NormCell.class) {
			this.brain=null;
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

	public synchronized void kill(boolean spreadOrganic, String causeOfDeath) {
		this.setCauseOfDeath(causeOfDeath);
		kill(spreadOrganic);
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

	/** Calls this.kill() method */
	@Override
	public void apoptosis() {
		this.kill(true,"Killed by apoptosis");
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

	public String getCauseOfDeath() {
		return causeOfDeath;
	}

	@Override
	public LiveCellType getLiveCellType() {
		return myCellType;
	}

	public void setCauseOfDeath(String causeOfDeath) {
		this.causeOfDeath=causeOfDeath;
	}

	public Double[] getInputData() {
		Double[] inputs= { myMethods.isRaedyToMultiply(this), getEnergyInput(),
				(double) cells[x][y].getOrganic()/myMethods.ORGANIC_DILL,
				myMethods.getNeighbourCellValue(this,Directions.UP),
				myMethods.getNeighbourCellValue(this,Directions.DOWN),
				myMethods.getNeighbourCellValue(this,Directions.LEFT),
				myMethods.getNeighbourCellValue(this,Directions.RIGHT),

				getLastOutput(), preLastOutput, (double) myMethods.getNeighbourCellValue(this,Directions.DOWN_RIGHT),
				myMethods.getNeighbourCellValue(this,Directions.UP_RIGHT),
				myMethods.getNeighbourCellValue(this,Directions.UP_LEFT),
				myMethods.getNeighbourCellValue(this,Directions.DOWN_LEFT), myMethods.isSpaceAvailable(this),

				isController(),

				myMethods.getRightDistance(this), myMethods.getLeftDistance(this), myMethods.getUpDistance(this),
				myMethods.getDownDistance(this),

				(double) myInfo.lastEnergy,

				myInfo.lastUpCell, myInfo.lastDownCell, myInfo.lastLeftCell, myInfo.lastRightCell,
				myInfo.lastRightDownCell, myInfo.lastRightUpCell, myInfo.lastLeftDownCell, myInfo.lastLeftUpCell,

				(double) myInfo.lastOrganic, (double) sunny, (double) myParts.size(),

				(double) myInfo.lastSize, myInfo.lastRightDistance, myInfo.lastLeftDistace, myInfo.lastUpDistance,
				myInfo.lastDownDistance };
		// System.out.println(Arrays.toString(inputs));
		return inputs;
	}

	class NormCellInfo {
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

		/** Sets all last... variables (last...=this...) */
		void setLastThings() {
			lastEnergy=energy;
			lastUpCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.UP);
			lastDownCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.DOWN);
			lastLeftCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.LEFT);
			lastRightCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.RIGHT);
			lastOrganic=cells[x][y].organic;
			lastSize=myParts.size();
			lastRightDistance=myMethods.getRightDistance(NormCell.this);
			lastLeftDistace=myMethods.getLeftDistance(NormCell.this);
			lastUpDistance=myMethods.getUpDistance(NormCell.this);
			lastDownDistance=myMethods.getDownDistance(NormCell.this);
			lastRightUpCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.UP_RIGHT);
			lastRightDownCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.DOWN_RIGHT);
			lastLeftUpCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.UP_LEFT);
			lastLeftDownCell=myMethods.getNeighbourCellValue(NormCell.this,Directions.DOWN_LEFT);
		}
	}

}
