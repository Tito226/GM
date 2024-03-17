package MyVersion.Cells;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
import static MyVersion.Frame.GM2_CONFIG.ORGANIC_PER_CELL_ON_NORMCELL_DEATH;
import static MyVersion.Frame.GM2_CONFIG.PROTOPLAST_START_ENERGY;
import static MyVersion.Frame.GM2_CONFIG.SIMPLE_DISTANCE;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.sunny;
import static MyVersion.Frame.World.width;

import java.awt.Color;

import MyVersion.Frame.World;

///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
public class Protoplast implements LiveCell{
	
	private NormCell normCell;
	int x;
	int y;
	Color color=Color.CYAN;
	String myName;
	int energy =PROTOPLAST_START_ENERGY;

	void transferEnergy(){
		
	}
	byte countb=0;

	public int getEnergy() {
		return energy;
	}

	@Override
	public void step() {
		eatSunE();
		if(countb%2==0){
			energy--;
		}
		countb++;
		normCell.multiCellbrain.evaluteFitness(getInputData(), false);
	}

	@Override
	public void test() {
		try {
			if( cells[normCell.getX()][normCell.getY()].liveCell==null || energy<=0){
				cells[x][y].organic+=energy;
				kill();
			}
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("proto: "+x+" "+y);
			System.out.println("nomCell: "+this.normCell.getX()+" "+this.normCell.getY());
		}
	}

	public Protoplast(NormCell normCell, double nOutput){
		this.normCell = normCell;
		int x=normCell.getX();
		int y=normCell.getY();
		if(nOutput>0.64 && nOutput<0.65){
			if (y>0) {
				spawn(cells[x][y-1]);
			}
		} else if(nOutput>0.65 && nOutput<0.66){
			if (y<height-1) {
				spawn(cells[x][y+1]);
			}
		} else if(nOutput>0.66 && nOutput<0.67){
			if (x<width-1) {
				spawn(cells[x+1][y]);
			}
		} else if(nOutput>0.67 && nOutput<0.68){
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
			this.normCell.energy-=ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
		}
	}


	void eatSunE(){
		energy+= World.sunny;
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
	public void kill() {
		Cell.organicSpreadOnDeath(this);
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
		return normCell;
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
    			 
    			 (double) DataMethods.getRightCell(this),lastOutput,preLastOutput,(double) DataMethods.getRightDownCell(this),DataMethods.getRightUpCell(this),DataMethods.getLeftUpCell(this),DataMethods.getLeftDownCell(this), DataMethods.isSpaceAvailable(this), DataMethods.isController(this),
    			 
    			 DataMethods.getRightDistance(this),DataMethods.getLeftDistance(this),DataMethods.getUpDistance(this),DataMethods.getDownDistance(this), (double) lastEnergy,lastUpCell,lastDownCell,lastLeftCell,
    			 
    			  lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,  (double) lastOrganic, (double) sunny, (double) normCell.myParts.size(),
    			 
    			 (double) lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
    	 return inputs;
	}
	
	void setLastThings(){
        lastEnergy=energy;
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
	public int getEnergyToMultiplyMe() {
		return ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
	}
	
}