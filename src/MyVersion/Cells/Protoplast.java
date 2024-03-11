package MyVersion.Cells;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
import static MyVersion.Frame.GM2_CONFIG.PROTOPLAST_START_ENERGY;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.width;

import java.awt.Color;

import MyVersion.Frame.World;

///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
public class Protoplast implements PartCell{
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
	public void step(double output) {
		eatSunE();
		if(countb%2==0){
			energy--;
		}
		countb++;
	}

	@Override
	public void test() {
		try {
			if( cells[normCell.getX()][normCell.getY()].secCell==null ){
				cells[x][y].partCell=null;
				cells[x][y].organic+=energy;
				if(this.normCell.myParts!=null){
					this.normCell.myParts.remove(this);
				}
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
		if ( nextCell.secCell==null && nextCell.partCell==null) {
			myName = this.normCell.partName + this.normCell.partNum;
			this.normCell.partNum++;
			nextCell.partCell = this;
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

	
}