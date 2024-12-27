package MyVersion.Cells;

import static MyVersion.Frame.FRAME_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
import static MyVersion.Frame.FRAME_CONFIG.HOW_MUCH_ORGANIC_EATS_PER_STEP;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.normCells;
import static MyVersion.Frame.World.width;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Actions {
	/**
	 * This method makes the cell eat organic in its current position.
	 * If there is more than 2 units of organic material, the cell eats a fixed amount.
	 * If there is less than 2, the cell eats whatever is left.
	 * The cell's energy increases when it eats.
	 *
	 * @param curLiveCell The cell that is eating.
	 */
	static void eatOrganic(LiveCell curLiveCell) {
		Cell[][] cells=curLiveCell.getHead().cells;
		int x=curLiveCell.getX();
		int y=curLiveCell.getY();
		Cell curCell=cells[x][y];
		NormCell head=curLiveCell.getHead();
		if (curCell.getOrganic()!=0 && curCell.getOrganic()>2) {
			head.energy+=HOW_MUCH_ORGANIC_EATS_PER_STEP;
			curCell.setOrganic(curCell.getOrganic()-HOW_MUCH_ORGANIC_EATS_PER_STEP);
		} else {
			head.energy+=curCell.getOrganic();
			curCell.setOrganic(0);
		}
	}

	static void eatOrganicByArea(LiveCell curLiveCell) {
		int range=1;// 3==1
		int eatByCell=1;
		int x=curLiveCell.getX();
		int y=curLiveCell.getY();
		Cell[][] cells=curLiveCell.getHead().cells;
		NormCell head=curLiveCell.getHead();
		for (int i=-range; i<=range; i++) {
			for (int j=-range; j<=range; j++) {
				if (x+i<cells.length && x+i>0 && y+j>0 && y+j<cells[x+i].length) {
					Cell curCell=cells[x+i][y+j];
					if (curCell.getOrganic()!=0 && curCell.getOrganic()>eatByCell) {
						head.energy+=eatByCell;
						curCell.setOrganic(curCell.getOrganic()-eatByCell);
					} else {
						head.energy+=curCell.getOrganic();
						curCell.setOrganic(0);
					}
				}
			}
		}
	}

	/**
	 * This method creates a new cell by splitting from the parent cell.
	 * It will create a new cell in the direction given.
	 * The new cell's type depends on `newCellType` (for example, Protoplast or NormCell).
	 * Calls multiply(LiveCell parent, byte[] curCellChromosome).
	 *
	 * @param parent The cell that is splitting.
	 * @param newCellType The type of new cell that will be created.
	 * @param direction The direction where the new cell will be created.
	 * @param myChromosomeNum The chromosome number that is passed to the new cell.
	 */
	static void multiply(LiveCell parent, LiveCellType newCellType, Directions direction,
			byte myChromosomeNum) {
		NormCell head=parent.getHead();
		Cell nextCell=DataMethods.getNextCell(parent,direction);
	
		if (nextCell!=null && nextCell.liveCell==null) {
			// System.out.println("multiplied");
			switch (newCellType) {/* TODO THINK ABOUT */
	
			case Protoplast -> {
				nextCell.setLiveCell(new Protoplast(parent,nextCell,myChromosomeNum));
				head.multiplies++;
			}
			case RootCell -> {
				nextCell.setLiveCell(new RootCell(parent,nextCell,myChromosomeNum));
				head.multiplies++;
			}
			case NormCell -> {
				nextCell.setLiveCell(new NormCell(head.brain,head.multiCellBrain,
						head.genome,head.world));/* TODO STUB зделать клонирование генома(сейчас только ссылка) */
				head.energy-=ENERGY_NEEDED_TO_MULTIPLY;
				head.multiplies++;
			}
	
			}
		} /*
			 * else { System.out.println("!nextCell.liveCell==null"); }
			 */
	}

	static void multiply(LiveCell parent, byte[] curCellChromosome) {
		/* TODO ПЕРЕДЕЛАТЬ */
		Directions direction=DataMethods.getDirection(parent,curCellChromosome);
		if (direction!=null && parent.getEnergy()>ENERGY_NEEDED_TO_MULTIPLY) {
			LiveCellType newCellType=Genome.getCellType(curCellChromosome[curCellChromosome.length-1]);
			multiply(parent,newCellType,direction,DataMethods.getChromosomeNum(curCellChromosome,direction));
		} else {
	
			parent.getHead().energy--;
		}
	}
	/**
	 * This method moves the cell in the direction given.
	 * If the cell cannot move because anouther LiveCell is in the way, the cell will try to eat it.
	 * Calls  move(NormCell curCell,Cell nextCell)
	 *
	 * @param curCell The cell that is trying to move.
	 * @param d The direction to move the cell (UP, DOWN, LEFT, or RIGHT).
	 */
	public static void move(NormCell curCell,Directions d) {
		int x=curCell.getX();
		int y=curCell.getY();
		Cell[][] cells=curCell.cells;
		switch (d) {/* TODO THINK ABOUT */
		case DOWN -> {
			if (y<height-1) {
				move(curCell,cells[x][y+1]);
			}
		}
		case UP -> {
			if (y>0) {
				move(curCell,cells[x][y-1]);
			}
		}
		case LEFT -> {
			if (x>0) {
				move(curCell,cells[x-1][y]);
			}
		}
		case RIGHT -> {
			if (x<width-1) {
				move(curCell,cells[x+1][y]);
			}
		}

		}

		curCell.energy-=1; /* TODO ПЕРЕСМОТРЕТЬ */

	}
	/**
	 * This method moves the current cell to the next cell if it is empty.
	 * If there is another cell in the next cell, the current cell will try to eat it.
	 *
	 * @param curCell The cell that is moving.
	 * @param nextCell The cell that is the target of the movement.
	 */
	private static void move(NormCell curCell,Cell nextCell) {
		int x=curCell.getX();
		int y=curCell.getY();
		Cell[][] cells=curCell.cells;
		if (nextCell.liveCell==null&&curCell.myParts.size()==0) {
			nextCell.setLiveCell(curCell);
			cells[x][y].setLiveCell(null);
			curCell.setX(nextCell.getX());
			curCell.setY(nextCell.getY());
		} else if (nextCell.liveCell!=null) {
			eatCell(curCell,nextCell);
			if (nextCell.liveCell==null) {
				move(curCell,nextCell);
			}
		}

	}
	/**
	 * This method tries to eat another cell that is in the direction given.
	 * If a cell is found in that direction, it will be eaten, and the current cell will gain energy.
	 *
	 * @param curCell The cell that is trying to eat.
	 * @param dirs The direction to check for another cell to eat (UP, DOWN, LEFT, or RIGHT).
	 * @return boolean True if the cell successfully ate the other cell, False if it did not.
	 */
	public static boolean eatCell(NormCell curCell,Directions dirs) {
		// System.out.println("cell was eaten");
		int x=curCell.getX();
		int y=curCell.getY();
		Cell[][] cells=curCell.cells;
		switch (dirs) {
	
		case UP -> {
			if (y>0) {
				return eatCell(curCell,cells[x][y-1]);
			}
		}
	
		case DOWN -> {
			if (y<height-1) {
				return eatCell(curCell,cells[x][y+1]);
			}
		}
	
		case RIGHT -> {
			if (x<width-1) {
				return eatCell(curCell,cells[x+1][y]);
			}
		}
	
		case LEFT -> {
			if (x>0) {
				return eatCell(curCell,cells[x-1][y]);
			}
		}
	
		}
	
		
		return false;
	}

	private static synchronized boolean eatCell(NormCell curCell,Cell nextCell) {// TODO доработать
		LiveCell nextLiveCell=nextCell.liveCell;
		
		if (nextLiveCell!=null && normCells.contains(nextLiveCell) && curCell.getCauseOfDeath()==null) {
			
			Semaphore curSem=nextLiveCell.getHead().sem;
			try {
				if (!curSem.tryAcquire(100,TimeUnit.MILLISECONDS)) {
					System.err.println("conflict: try to eat cell that makes step");
					//no need to decrease energy, because Cell will got eaten
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (nextLiveCell!=null) {
				curCell.energy+=nextLiveCell.getEnergy();
				nextLiveCell.getHead().bited=true;
				if (!normCells.contains(nextLiveCell)&&nextLiveCell instanceof NormCell) {
					
					System.err.println("try to eat not existing cell");
					
					System.err.println("!normCells.contains(nextLiveCell) "+nextLiveCell.getEnergy()+" : "
							+nextLiveCell.getClass()+" bited: "+((NormCell) nextLiveCell).bited+"  World.steps:"+curCell.world.getStepsAtAll());
					
					curCell.energy-=1;
					return false;
				}
	
				if (nextLiveCell instanceof NormCell) {
					NormCell norm=(NormCell) nextLiveCell;
					norm.setCauseOfDeath("was eaten");
				}
	
				nextLiveCell.kill(false);
				nextCell.setLiveCell(null);
				curSem.release();
				return true;
	
			} else {
				curSem.release();
				curCell.energy-=1;
				return false;
			}
	
		} else {
			curCell.energy-=1;
			return false;
		}
		// return false;
	}

}
