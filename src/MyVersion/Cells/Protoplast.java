package MyVersion.Cells;

import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.World.sunny;
import MyVersion.Frame.Action_Boundaries;
import java.awt.Color;
import java.util.Random;
import MyVersion.Frame.World;

///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
public class Protoplast implements LiveCell {
	Random r=new Random();
	final LiveCellType myCellType=LiveCellType.Protoplast;
	private NormCell normCell;// head cell reference
	int x;
	int y;
	byte myChromosomeNum=0;
	int lifeTime=0, multiplies=0;
	private boolean tested=false;
	static Color color=Color.CYAN;
	String myName;
	Cell[][] cells;
	// int energy =PROTOPLAST_START_ENERGY;

	byte countb=0;

	public Protoplast(LiveCell curCell, Cell nextCell,byte myChromosomeNum) {
		this.normCell=curCell.getHead();
		cells=normCell.cells;
		this.myChromosomeNum=myChromosomeNum;
		spawn(nextCell);
		
	}
	
	@Override
	public void step() {

		double output=normCell.multiCellBrain.calculateOutput(getInputData(),false)[0];
		preLastOutput=lastOutput;
		lastOutput=output;

		if (DataMethods.between(Action_Boundaries.eatOrganicBoundaries,output)) {
			eatSunE();
		} else if (DataMethods.between(Action_Boundaries.multiplyBoundaries,output)) {
			Actions.multiply(this,normCell.genome.getChromosome(myChromosomeNum));
		} else if (DataMethods.between(Action_Boundaries.apoptosisBoundaries,output)) {
			apoptosis();
		}
		idleEnergyDecrese();
		setLastThings();
		lifeTime++;
	}

	@Override
	public void test() {
		//normCell=normCell.getHead();
		if ((getEnergy()<=0 || lifeTime>PROTOPLAST_MAX_LIFETIME)&& normCell.getCauseOfDeath()==null ) {
			kill(true);
		} else if (cells[x][y].organic>CRITICAL_ORGANIC_VALUE) {// TODO toxic organic
			this.kill(true);
		}
	}
	/**sets coordinates ,adds to heads parts, decreases energy needed for spawn*/
	

	private void spawn(Cell nextCell) {
		if (nextCell.liveCell==null) {
			myName=this.normCell.partName+this.normCell.partNum;
			this.normCell.partNum++;
			nextCell.liveCell=this;
			this.x=nextCell.getX();
			this.y=nextCell.getY();
			this.normCell.myParts.add(this);
			this.normCell.energy-=ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
		}
	}

	void eatSunE() {
		increaseEnergy(World.sunny);
	}

	private void increaseEnergy(int incrValue) {
		normCell.setEnergy(getEnergy()+incrValue);
	}

	private void decreaseEnergy(int incrValue) {
		normCell.setEnergy(getEnergy()-incrValue);
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setY(int y) {
		this.y=y;
	}

	@Override
	public void setX(int x) {
		this.x=x;
	}

	@Override
	public void kill(boolean spreadOrganic) {
		if (spreadOrganic) {
			Cell.organicSpreadOnDeath(this,ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST);
		}
		normCell.bited=true;
		cells[x][y].liveCell=null;
		normCell.myParts.remove(this);
	}

	@Override
	public Integer getGeneralEnergy() {
		return normCell.getGeneralEnergy();
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public NormCell getHead() {

		return normCell.getHead();
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
	double lastOutput=0d, preLastOutput=0d;

	@Override
	public Double[] getInputData() {
		Double[] inputs= { 
				DataMethods.isRaedyToMultiply(this), (double) DataMethods.getEnergy(this),
				(double) cells[x][y].getOrganic()/DataMethods.ORGANIC_DILL,
				DataMethods.getNeighbourCellValue(this,Directions.UP),
				DataMethods.getNeighbourCellValue(this,Directions.DOWN),
				DataMethods.getNeighbourCellValue(this,Directions.LEFT),

				DataMethods.getNeighbourCellValue(this,Directions.RIGHT), DataMethods.getDeltaX(this),
				DataMethods.getDeltaY(this), lastOutput, preLastOutput,
				DataMethods.getNeighbourCellValue(this,Directions.DOWN_RIGHT),
				DataMethods.getNeighbourCellValue(this,Directions.UP_RIGHT),
				DataMethods.getNeighbourCellValue(this,Directions.UP_LEFT),
				DataMethods.getNeighbourCellValue(this,Directions.DOWN_LEFT), DataMethods.isSpaceAvailable(this),
				DataMethods.getMyType(this), DataMethods.isController(this),

				DataMethods.getRightDistance(this), DataMethods.getLeftDistance(this), DataMethods.getUpDistance(this),
				DataMethods.getDownDistance(this), (double) lastEnergy, lastUpCell, lastDownCell, lastLeftCell,

				lastRightCell, lastRightDownCell, lastRightUpCell, lastLeftDownCell, lastLeftUpCell,
				(double) lastOrganic, (double) sunny, (double) normCell.myParts.size(),

				(double) lastSize, lastRightDistance, lastLeftDistace, lastUpDistance, lastDownDistance };
		return inputs;
	}

	void setLastThings() {
		lastEnergy=getEnergy();
		lastUpCell=DataMethods.getNeighbourCellValue(this,Directions.UP);
		lastDownCell=DataMethods.getNeighbourCellValue(this,Directions.DOWN);
		lastLeftCell=DataMethods.getNeighbourCellValue(this,Directions.LEFT);
		lastRightCell=DataMethods.getNeighbourCellValue(this,Directions.RIGHT);
		lastOrganic=cells[x][y].organic;
		lastSize=normCell.myParts.size();
		lastRightDistance=DataMethods.getRightDistance(this);
		lastLeftDistace=DataMethods.getLeftDistance(this);
		lastUpDistance=DataMethods.getUpDistance(this);
		lastDownDistance=DataMethods.getDownDistance(this);
		lastRightUpCell=DataMethods.getNeighbourCellValue(this,Directions.UP_RIGHT);
		lastRightDownCell=DataMethods.getNeighbourCellValue(this,Directions.DOWN_RIGHT);
		lastLeftUpCell=DataMethods.getNeighbourCellValue(this,Directions.UP_LEFT);
		lastLeftDownCell=DataMethods.getNeighbourCellValue(this,Directions.DOWN_LEFT);
	}

	@Override
	public int getEnergyToMultiplyMe() {
		return ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
	}

	@Override
	public void idleEnergyDecrese() {
		if (lifeTime%3==0) {
			decreaseEnergy(1);
		}

	}

	@Override
	public void apoptosis() {
		getHead().energy+=ENERGY_NEEDED_TO_MULTIPLY_ROOT;
		getHead().bited=true;
		this.kill(false);
	}

	@Override
	public boolean getTested() {
		return tested;
	}

	@Override
	public void setTested(boolean value) {
		tested=value;
	}

	@Override
	public LiveCellType getLiveCellType() {
		return myCellType;
	}

}