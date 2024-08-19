package MyVersion.Cells;

import java.awt.*;
import java.util.Random;
import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.World.*;
/**Cell class */
//TODO  ПРИДУМАТЬ ОПИСАНИЕ КЛАССА
public class Cell {
	int[] color=new int[3];
	// Color myColor ;
	// int energy=1;
	public boolean selected=false;
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

	public synchronized void setLiveCell(LiveCell liveCell) {
		this.liveCell=liveCell;
		setChange(true);
	}

	public void setX(int x) {
		this.x=x;
	}

	public void setY(int y) {
		this.y=y;
	}

	void step() {
		if (liveCell!=null) {
			liveCell.step();
		} else {
		} // System.out.println(" ");
	}

	boolean getLiveCell() {
		if (liveCell!=null) {
			return true;
		} else
			return false;
	}

	private boolean change=false;

	boolean lastLCell=false;
	int lastOrganic=0;

	private synchronized boolean getOrSetChange(boolean get, boolean value) {
		if (get) {
			return change;
		} else {
			change=value;
			return false;
		}
	}

	public void testCell() {
		lastLCell=getLiveCell();
		lastOrganic=organic;
		if (organic<CRITICAL_ORGANIC_VALUE) {
			try {
				if (organic<255&&organic>0) {
					int colorValue=255-organic;
					for (int i=0; i<color.length; i++) {
						color[i]=colorValue;
					}
				}
			} catch (IllegalArgumentException e) {
				System.err.println("Invalid color values");
			}
		} else {
			color[0]=205;
			color[1]=0;
			color[2]=0;// RED

		}
		if (liveCell!=null) {
			liveCell.setX(x);
			liveCell.setY(y);
			liveCell.test();
		}
	}

	public static void organicSpreadOnDeath(LiveCell dyingCell) {
		int range=(ORGANIC_ON_DAETH_RANGE-1)/2;
		int x=dyingCell.getX();
		int y=dyingCell.getY();
		for (int i=-range; i<=range; i++) {
			for (int j=-range; j<range; j++) {
				if (x+i<cells.length&&x+i>0&&y+j>0&&y+j<cells[x+i].length) {
					cells[x+i][y+j].organic+=dyingCell.getEnergy()+ORGANIC_PER_CELL_ON_NORMCELL_DEATH;
					cells[x+i][y+j].setChange(true);
				}
			}
		}
	}
	
	public static void organicSpreadOnDeath(LiveCell dyingCell, int energyNeededToBorn) {
		int range=(ORGANIC_ON_DAETH_RANGE-1)/2;
		int x=dyingCell.getX();
		int y=dyingCell.getY();
		for (int i=-range; i<=range; i++) {
			for (int j=-range; j<=range; j++) {
				if (x+i<cells.length&&x+i>0&&y+j>0&&y+j<cells[x+i].length) {
					cells[x+i][y+j].organic+=energyNeededToBorn;
					cells[x+i][y+j].setChange(true);
				}
			}
		}
	}

	public Color getColor() {
		if (liveCell==null) {
			return new Color(color[0],color[1],color[2]);
		} else {
			return liveCell.getColor();
		}
	}

	public void setOrganic(int organic) {
		this.organic=organic;
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
