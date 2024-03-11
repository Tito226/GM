package MyVersion.Cells;

import java.awt.*;
import java.util.Random;
import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.World.*;

public  class Cell {
    int[] color=new int[3];
    //Color myColor ;
    //int energy=1;
    public LiveCell liveCell;
    private int x;
    private int y;
    public int organic=CELL_START_ORGANIC;
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLiveCell(LiveCell liveCell) {
        this.liveCell = liveCell;
        setChange(true);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    void step(){
    	if( liveCell!=null){
    		liveCell.step();
    	}
    	else{} //System.out.println(" ");
    }

    boolean getLiveCell(){
        if(liveCell!=null){
            return true;
        }else return false;
    }
    private boolean change=false;

    boolean lastLCell=false;
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
        lastLCell = getLiveCell();
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
        if (liveCell != null ) {
           liveCell.test();
        }
    }

  	public Color getColor(){
  		if(liveCell==null) {
  			return new Color(color[0],color[1],color[2]);
        }else {
        	return liveCell.getColor();
        }  	
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
