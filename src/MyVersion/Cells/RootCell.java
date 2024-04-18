package MyVersion.Cells;

import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.sunny;
import static MyVersion.Frame.World.width;
import static MyVersion.Frame.GM2_CONFIG.*;
import java.awt.Color;
import java.util.Random;

import MyVersion.Frame.Action_Boundaries;

public class RootCell implements LiveCell {
	Random r=new Random();
	private NormCell normCell;
	int x;
	int y;
	int lifeTime=0,multiplies=0;
	static Color color=Color.PINK;
	private boolean tested=false;
	String myName;
	@Override
	public void idleEnergyDecrese() {
		if(lifeTime%3==0) {
			decreaseEnergy(1);
		}
	}
	
	public RootCell(LiveCell curCell, double output) {
		this.normCell = curCell.getHead();
		int x=curCell.getX();
		int y=curCell.getY();
		if(output>Action_Boundaries.multiplyRootUpBoundaries[0] && output<Action_Boundaries.multiplyRootUpBoundaries[1]){
			if (y>0) {
				spawn(cells[x][y-1]);
			}
		} else if(output>Action_Boundaries.multiplyRootDownBoundaries[0] && output<Action_Boundaries.multiplyRootDownBoundaries[1]){
			if (y<height-1) {
				spawn(cells[x][y+1]);
			}
		} else if(output>Action_Boundaries.multiplyRootRightBoundaries[0] && output<Action_Boundaries.multiplyRootRightBoundaries[1]){
			if (x<width-1) {
				spawn(cells[x+1][y]);
			}
		} else if(output>Action_Boundaries.multiplyRootLeftBoundaries[0] && output<Action_Boundaries.multiplyRootLeftBoundaries[1]){
			if (x>0) {
				spawn(cells[x-1][y]);
			}
		}
	}

	private void spawn(Cell nextCell) {
		if ( nextCell.liveCell==null ) {
			myName = this.normCell.partName + this.normCell.partNum;
			this.normCell.partNum++;
			nextCell.liveCell = this;
			this.x=nextCell.getX();
			this.y=nextCell.getY();
			this.normCell.myParts.add(this);
			this.normCell.energy-=ENERGY_NEEDED_TO_MULTIPLY_ROOT;
		}
	}
	
	@Override
	public void step() {
		double output =normCell.multiCellBrain.calculateOutput(getInputData(), false)[0];
		preLastOutput=lastOutput;
  		lastOutput=output;
  		if(DataMethods.between(Action_Boundaries.multiplyBoundaries,output)){
      		DataMethods.multiply(this);
      	}else if(DataMethods.between(Action_Boundaries.multiplyProtoplastBoundaries,output)){
      		new Protoplast(this, output);
      	}if(DataMethods.between(Action_Boundaries.eatOrganicBoundaries,output)){
  			DataMethods.eatOrganicByArea(this);
  		}else if(DataMethods.between(Action_Boundaries.apoptosisBoundaries,output)) {
      		apoptosis();
      	}
      	idleEnergyDecrese();
		setLastThings();
		lifeTime++;
	}

	@Override
	public void test() {
		normCell=normCell.getHead();
		if( getEnergy()<=0 || lifeTime>ROOT_MAX_LIFETIME){
			kill(true);
		}
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
		if(spreadOrganic) {
			Cell.organicSpreadOnDeath(this,ENERGY_NEEDED_TO_MULTIPLY_ROOT);
		}
		cells[x][y].liveCell=null;
		normCell.myParts.remove(this);
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
		return normCell;
	}

	@Override
	public int getEnergyToMultiplyMe() {
		return ENERGY_NEEDED_TO_MULTIPLY_ROOT;
	}
	
	private void increaseEnergy(int incrValue) {
		normCell.setEnergy(getEnergy()+incrValue);
	}
	private void decreaseEnergy(int incrValue) {
		normCell.setEnergy(getEnergy()-incrValue);
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
    double lastOutput=0d,preLastOutput=0d;
    
    @Override
	public Double[] getInputData() {
    	Double[] inputs = {DataMethods.isRaedyToMultiply(this) , (double) DataMethods.getEnergy(this), (double) cells[x][y].getOrganic()/DataMethods.organicDil, DataMethods.getUpCell(this), DataMethods.getDownCell(this), DataMethods.getLeftCell(this),
    			 
    			 (double) DataMethods.getRightCell(this),DataMethods.getDeltaX(this),DataMethods.getDeltaY(this),lastOutput,preLastOutput,(double) DataMethods.getRightDownCell(this),DataMethods.getRightUpCell(this),DataMethods.getLeftUpCell(this)
    			 
    			 ,DataMethods.getLeftDownCell(this), DataMethods.isSpaceAvailable(this),DataMethods.getMyType(this), DataMethods.isController(this),
    			 
    			 DataMethods.getRightDistance(this),DataMethods.getLeftDistance(this),DataMethods.getUpDistance(this),DataMethods.getDownDistance(this), (double) lastEnergy,lastUpCell,lastDownCell,lastLeftCell,
    			 
    			  lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,  (double) lastOrganic, (double) sunny, (double) normCell.myParts.size(),
    			 
    			 (double) lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
    	 return inputs;
	}
	
	void setLastThings(){
        lastEnergy=getEnergy();
        lastUpCell=DataMethods.getUpCell(this);
        lastDownCell=DataMethods.getDownCell(this);
        lastLeftCell=DataMethods.getLeftCell(this);
        lastRightCell=DataMethods.getRightCell(this);
        lastOrganic=cells[x][y].organic;
        lastSize=normCell.myParts.size();
        lastRightDistance=DataMethods.getRightDistance(this);
        lastLeftDistace=DataMethods.getLeftDistance(this);
        lastUpDistance=DataMethods.getUpDistance(this);
        lastDownDistance=DataMethods.getDownDistance(this);
        lastRightUpCell=DataMethods.getRightUpCell(this);
        lastRightDownCell=DataMethods.getRightDownCell(this);
        lastLeftUpCell=DataMethods.getLeftUpCell(this);
        lastLeftDownCell=DataMethods.getLeftDownCell(this);
    }

	@Override
	public void apoptosis() {
		getHead().energy+=ENERGY_NEEDED_TO_MULTIPLY_ROOT;
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
}
