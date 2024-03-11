package MyVersion.Cells;

import java.awt.*;
import java.util.Random;
import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.World.*;

public  class Cell {
    int[] color=new int[3];
    //Color myColor ;
    //int energy=1;
    public NormCell secCell;
    PartCell partCell;
    private int x;
    private int y;
    public int organic=CELL_START_ORGANIC;
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void setSecCell(NormCell secCell){
    	this.secCell=secCell;
    	setChange(true);
    }
    
    public void setPartCell(PartCell partCell) {
        this.partCell = partCell;
        setChange(true);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    void step(){
    	if( secCell!=null){
    		secCell.step();
    	}
    	else{} //System.out.println(" ");
    }
    boolean getSecCell(){
        if(secCell!=null){
            return true;
        }else
        	return false;
    }
    boolean getPartCell(){
        if(partCell!=null){
            return true;
        }else return false;
    }
    private boolean change=false;
    
    boolean lastNCell=false;
    boolean lastPCell=false;
    int lastOrganic=0;
    private synchronized boolean getOrSetChange(boolean get,boolean value) {
    	if(get) {
    		return change;
    	} else {
    		change=value;
    		return false;
    	}
    }
    public void testCell() {
        lastNCell = getSecCell();
        lastPCell = getPartCell();
        lastOrganic = organic;
        try {
            if (organic < 255 && organic > 0) {
                int colorValue = 255 - organic;
                for (int i = 0; i < color.length; i++) {
					color[i]=colorValue;
				}
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid color values");
        }
        if (secCell != null && secCell.myMethods.getEnergy(secCell) <= 0) {
            secCellKill();
        }
        if (secCell != null && secCell.myMethods.getEnergy(secCell) >= 10000) {
            System.out.println("Cell with 10000 died");
            organic += secCell.myMethods.getEnergy(secCell);
            secCell.brain.kill();
            setSecCell(null);

        }
        if (partCell != null) {
            partCell.test();
        }
    }

  	public Color getColor(){
  		if(secCell==null && partCell==null) {
  			return new Color(color[0],color[1],color[2]);
        }else if(secCell!=null) {
        	return secCell.getColor();
        }else {
        	return partCell.getColor();
        }  	
  	}

  	void secCellKill(){
  		organic+=secCell.myMethods.getEnergy(secCell)+10;
  		for (int i = -1; i < 2; i++) {
  			for (int j = -1; j < 2; j++) {
  				if(x+i<cells.length && x+i>0 && y+j>0 && y+j<cells[1].length) {
  					cells[x+i][y+j].organic+=secCell.myMethods.getEnergy(secCell)+ORGANIC_PER_CELL_ON_NORMCELL_DEATH;
  					cells[x+i][y+j].change=true;
  				}
  			}
		}
  		if(!secCell.brain.dontDelete) {
  			secCell.brain.kill();
  		}else {
  			secCell.brain.isDead=true;
  		}
  		
  		setSecCell(null);
  	}
  	
    public void setOrganic(int organic) {
        this.organic = organic;
    }

    public int getOrganic() {
        return organic;
    }

	public synchronized boolean isChange() {
		return getOrSetChange(true,change);
	}

	public synchronized void setChange(boolean change) {
		getOrSetChange(false,change);
	}
}
