package MyVersion.Cells;

import static MyVersion.Frame.FRAME_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
import static MyVersion.Frame.FRAME_CONFIG.HOW_MUCH_ORGANIC_EATS_PER_STEP;
import static MyVersion.Frame.FRAME_CONFIG.ORGANIC_PER_CELL_ON_NORMCELL_DEATH;
import static MyVersion.Frame.FRAME_CONFIG.*;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.sunny;
import static MyVersion.Frame.World.width;

import java.util.Random;
public class DataMethods{
	static Random r=new Random();
	static final double DISTANCE_DILL=1000d;//1000
	static final int MAX_DISTANCE=50;
	public final static double ORGANIC_DILL=200d;
	public final static double ENERGY_DILL=200d;
	public static final double ORGANIC_CRIT_MASS_VALUE=1.5;
	//--------------------get...Cell()-----------------
	public static final double RELATIVES_VALUE=3.4d;//2.5
	public static final double NORMCELL_VALUE=2.0d;//2
	public static final double NEXT_ORGANIC_VALUE=0.25d;//0.5
	public static final double WALL_VALUE=1d;//1
	//-------------------------------------------------
	

	static boolean isRelatives(NormCell normCell, NormCell normCell1){
		try {
		   if(normCell1.myParentNum ==normCell.myParentNum){
			   return true;
		   }else if (normCell1.myChildNum ==normCell.myParentNum){
			   return true;}
		   else {
			   return false;
		   }
		 }catch(Exception e) {
			 return false;
		 }
	   }

	public static  int getEnergy(LiveCell liveCell) {
	    return liveCell.getEnergy();
	}

	static double getDownDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max(MAX_DISTANCE,liveCell.getY())/DISTANCE_DILL;
	    }else {
	    	for (int curY =liveCell.getY() ; curY > 0; curY--) {
				Cell curCell=cells[liveCell.getX()][curY];
				if(curCell.liveCell!=null || curY>MAX_DISTANCE){
					return curY/DISTANCE_DILL;
				}
			}
	    	return Math.max(MAX_DISTANCE, liveCell.getY())/DISTANCE_DILL;
	    }
	}

	static double getUpDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max( MAX_DISTANCE, cells[0].length-liveCell.getY())/DISTANCE_DILL;
		}else {
			for (int curY =liveCell.getY() ; curY < cells[0].length; curY++) {
				Cell curCell=cells[liveCell.getX()][curY];
				if(curCell.liveCell!=null || cells[0].length-curY>MAX_DISTANCE){
					return (cells[0].length-curY)/DISTANCE_DILL;
				}
			}
			return Math.max( MAX_DISTANCE, cells[0].length-liveCell.getY())/DISTANCE_DILL;
	    }
	}

	static double getRightDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max(MAX_DISTANCE, cells.length-liveCell.getX())/DISTANCE_DILL;
		}else {
			for (int curX =liveCell.getX() ; curX < cells.length; curX++) {
				Cell curCell=cells[curX][liveCell.getY()];
				if(curCell.liveCell!=null || cells.length-curX>MAX_DISTANCE){
					return (cells.length-curX)/DISTANCE_DILL;
				}
			}
	    	return Math.max(MAX_DISTANCE, cells.length-liveCell.getX())/DISTANCE_DILL;
	    }
	}

	
	static void eatOrganic(LiveCell curLiveCell){
		int x =curLiveCell.getX();
		int y=curLiveCell.getY();
		Cell curCell=cells[x][y];
		NormCell head=curLiveCell.getHead();
        if(curCell.getOrganic()!=0  && curCell.getOrganic()>2){
        	head.energy+=HOW_MUCH_ORGANIC_EATS_PER_STEP;
        	curCell.setOrganic(curCell.getOrganic()-HOW_MUCH_ORGANIC_EATS_PER_STEP);
        }else {
        	head.energy+=curCell.getOrganic();
        	curCell.setOrganic(0);
        }
    }
	static void eatOrganicByArea(LiveCell curLiveCell){
		int range=1;//3==1
		int eatByCell=1;
		int x =curLiveCell.getX();
		int y=curLiveCell.getY();
		NormCell head=curLiveCell.getHead();
		for (int i = -range; i <=range ; i++) {
  			for (int j = -range; j <= range; j++) {
  				if(x+i<cells.length && x+i>0 && y+j>0 && y+j<cells[x+i].length) {
  					Cell curCell=cells[x+i][y+j];
  			        if(curCell.getOrganic()!=0  && curCell.getOrganic()>eatByCell){
  			        	head.energy+=eatByCell;
  			        	curCell.setOrganic(curCell.getOrganic()-eatByCell);
  			        }else {
  			        	head.energy+=curCell.getOrganic();
  			        	curCell.setOrganic(0);
  			        }
  				}
  			}
		}
    }
	
	static double getLeftDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max(MAX_DISTANCE, liveCell.getX())/DISTANCE_DILL;
		}else {
			for (int curX =liveCell.getX() ; curX > 0; curX--) {
				Cell curCell=cells[curX][liveCell.getY()];
				if(curCell.liveCell!=null || curX>MAX_DISTANCE){
					return curX/DISTANCE_DILL;
				}
			}
	    	return Math.max(MAX_DISTANCE, cells.length-liveCell.getX())/DISTANCE_DILL;
	    }
	}

	public static  double getLeftDownCell(LiveCell liveCell){
		if(  liveCell.getY()<height-1 && liveCell.getX()>1) {
			Cell nextCell=cells[liveCell.getX()-1][liveCell.getY()+1];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}

	public static  double getLeftUpCell(LiveCell liveCell){
		if(  liveCell.getY()>1 && liveCell.getX()>1) {
			Cell nextCell=cells[liveCell.getX()-1][liveCell.getY()-1];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}

	public static  double getRightUpCell(LiveCell liveCell){
		if(  liveCell.getY()>1 && liveCell.getX()<width-1) {
			Cell nextCell=cells[liveCell.getX()+1][liveCell.getY()-1];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}

	public static double getRightDownCell(LiveCell liveCell){
		if( liveCell.getY()<height-1 && liveCell.getX()<width-1) {
			Cell nextCell=cells[liveCell.getX()+1][liveCell.getY()+1];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}
	//

	public static double getDownCell(LiveCell liveCell){
		if( liveCell.getY()<height-1) {
			Cell nextCell=cells[liveCell.getX()][liveCell.getY()+1];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}

	public static double getUpCell(LiveCell liveCell){
		if( liveCell.getY()>1) {
			Cell nextCell=cells[liveCell.getX()][liveCell.getY()-1];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}

	public static double getRightCell(LiveCell liveCell){
		
		if( liveCell.getX()<width-1) {
			Cell nextCell=cells[liveCell.getX()+1][liveCell.getY()];
			return getNextCell(nextCell,liveCell);
		}else {
			return WALL_VALUE;
		}
	}

	public static double getLeftCell(LiveCell liveCell){
		if( liveCell.getX()>1) {
			Cell nextCell=cells[liveCell.getX()-1][liveCell.getY()];
			return getNextCell(nextCell,liveCell);
		}
		else {
			return WALL_VALUE;
		}
	}
		
	public static double getNextCell(Cell nextCell,LiveCell liveCell) {
		LiveCell nextLiveCell=nextCell.liveCell;
		if(nextLiveCell!=null){
			boolean isRelative=nextLiveCell instanceof NormCell? isRelatives(liveCell.getHead(),(NormCell)nextLiveCell) : false;
          	if(isRelative || isMyPart(liveCell.getHead(), nextLiveCell) ){
          		return RELATIVES_VALUE;
          	}else {
          		return NORMCELL_VALUE;
          	}
		} 
		else{
    	   if(nextCell.organic>1) {
				return Math.min(nextCell.organic/ORGANIC_DILL, NEXT_ORGANIC_VALUE);
    	   }else if(nextCell.organic >= CRITICAL_ORGANIC_VALUE) {
    		   return ORGANIC_CRIT_MASS_VALUE;
    	   }
    	   return 0d;
       }
	}
	
	static double isRaedyToMultiply(LiveCell liveCell){
	    if(liveCell.getEnergy()>ENERGY_NEEDED_TO_MULTIPLY){
	        return 1d;
	    }else
	        return 0d;
	}

	static boolean isMyPart(NormCell normCell, LiveCell partCell){
	   if (normCell.myParts.contains(partCell)){
	       //System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOORK");
	
	       return true;
	   } else{
	       return false;}
	}

	static double isSpaceAvailable(LiveCell liveCell){
	     int x=liveCell.getX();
	     int y=liveCell.getY();
	     boolean b1=y>0        && cells[x][y-1].liveCell==null;
	     boolean b2=y<height-1 && cells[x][y+1].liveCell==null;
	     boolean b3=x<width-1  && cells[x+1][y].liveCell==null;
	     boolean b4=x>0        && cells[x-1][y].liveCell==null;
	     if(!b1 && !b2 && !b3 && !b4){
	           return 0.0d;
	     }else{
	    	 return 1.0d;
	     }
	 }

	static boolean between(double[] boundaries ,double output) {
  		if(output>boundaries[0] && output<boundaries[1]) {
  			return true;
  		}else {
  			return false;
  		}
  	}
	
	public static Double isController(LiveCell curCell) {
		return curCell.getHead().isController();
	}
	 
	static double getDeltaX(LiveCell curCell) {
		return curCell.getX()-curCell.getHead().getX();
	}
	static double getDeltaY(LiveCell curCell) {
		return curCell.getY()-curCell.getHead().getY();
	}
	
	static void multiply(LiveCell parent){
        /*TODO ПЕРЕДЕЛАТЬ*/
		
		if(isSpaceAvailable(parent)==1.00f && parent.getEnergy()>ENERGY_NEEDED_TO_MULTIPLY){
			int x=parent.getX();
			int y=parent.getY();
			boolean shitHappened=false;
			int xxx =r.nextInt(4);
    	   	switch (xxx){
    	   	 	case 1 -> {
    	   	 		if( y>0){
    	   	 			multiply(cells[x][y-1],parent); 
    	   	 		}
    	   	 	}

    	   	 	case 2 -> {
    	   	 		if(y<height-1 ){
    			   multiply(cells[x][y+1],parent);
    	   	 		}
    	   	 	}

    	   	 	case 3 -> {
    	   	 		if( x<width-1 ){
    	   	 			multiply(cells[x+1][y],parent);
    	   	 		}
    	   	 	}

    	   	 	case 4 -> {
    	   	 		if(  x>0 ){
    	   	 			multiply(cells[x-1][y],parent);
    	   	 		}
    	   	 	}
    	   	 	default->{
    	   	 		multiply(parent); 
    	   	 	}
    	   	 }

       }
       else{
    	   
          parent.getHead().energy--;
       } 
    }
    private static void multiply(Cell nextCell,LiveCell parent) {
    	NormCell head=parent.getHead();
    	if(nextCell.liveCell ==null){
    		nextCell.setLiveCell(new NormCell(head.brain,head.multiCellBrain));
    		head.energy-=ENERGY_NEEDED_TO_MULTIPLY;
    		head.multiplies++;
        }
    }
	
    static double getMyType(LiveCell curCell) {
    	if(curCell instanceof NormCell) {
    		return 0.1d;
    	}else if(curCell instanceof Protoplast) {
    		return 0.2d;
    	}else if(curCell instanceof RootCell) {
    		return 0.3d;
    	}
    	return 0;
    }
    
    
    
 }