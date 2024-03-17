package MyVersion.Cells;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
import static MyVersion.Frame.GM2_CONFIG.SIMPLE_DISTANCE;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.sunny;
import static MyVersion.Frame.World.width;
public class DataMethods{
	
	static double distanceDill=1000f;//1000
	static int maxDistance=50;
	public static double organicDil=200f;
	//--------------------get...Cell()-----------------
	public static double relativesValue=3.4d;//2.5
	public static double normCellValue=2.0d;//2
	public static double nextOrganicValue=0.25d;//0.5
	public static double wallValue=1d;//1
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
			return Math.max(maxDistance,liveCell.getY())/distanceDill;
	    }else {
	    	for (int curY =liveCell.getY() ; curY > 0; curY--) {
				Cell curCell=cells[liveCell.getX()][curY];
				if(curCell.liveCell!=null || curY>maxDistance){
					return curY/distanceDill;
				}
			}
	    	return Math.max(maxDistance, liveCell.getY())/distanceDill;
	    }
	}

	static double getUpDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max( maxDistance, cells[0].length-liveCell.getY())/distanceDill;
		}else {
			for (int curY =liveCell.getY() ; curY < cells[0].length; curY++) {
				Cell curCell=cells[liveCell.getX()][curY];
				if(curCell.liveCell!=null || cells[0].length-curY>maxDistance){
					return (cells[0].length-curY)/distanceDill;
				}
			}
			return Math.max( maxDistance, cells[0].length-liveCell.getY())/distanceDill;
	    }
	}

	static double getRightDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max(maxDistance, cells.length-liveCell.getX())/distanceDill;
		}else {
			for (int curX =liveCell.getX() ; curX < cells.length; curX++) {
				Cell curCell=cells[curX][liveCell.getY()];
				if(curCell.liveCell!=null || cells.length-curX>maxDistance){
					return (cells.length-curX)/distanceDill;
				}
			}
	    	return Math.max(maxDistance, cells.length-liveCell.getX())/distanceDill;
	    }
	}

	static double getLeftDistance(LiveCell liveCell){
		if(SIMPLE_DISTANCE) {
			return Math.max(maxDistance, liveCell.getX())/distanceDill;
		}else {
			for (int curX =liveCell.getX() ; curX > 0; curX--) {
				Cell curCell=cells[curX][liveCell.getY()];
				if(curCell.liveCell!=null || curX>maxDistance){
					return curX/distanceDill;
				}
			}
	    	return Math.max(maxDistance, cells.length-liveCell.getX())/distanceDill;
	    }
	}

	public static  double getLeftDownCell(LiveCell liveCell){
		if(  liveCell.getY()<height-1 && liveCell.getX()>1) {
			Cell nextCell=cells[liveCell.getX()-1][liveCell.getY()+1];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}

	public static  double getLeftUpCell(LiveCell liveCell){
		if(  liveCell.getY()>1 && liveCell.getX()>1) {
			Cell nextCell=cells[liveCell.getX()-1][liveCell.getY()-1];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}

	public static  double getRightUpCell(LiveCell liveCell){
		if(  liveCell.getY()>1 && liveCell.getX()<width-1) {
			Cell nextCell=cells[liveCell.getX()+1][liveCell.getY()-1];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}

	public static double getRightDownCell(LiveCell liveCell){
		if( liveCell.getY()<height-1 && liveCell.getX()<width-1) {
			Cell nextCell=cells[liveCell.getX()+1][liveCell.getY()+1];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}
	//

	public static double getDownCell(LiveCell liveCell){
		if( liveCell.getY()<height-1) {
			Cell nextCell=cells[liveCell.getX()][liveCell.getY()+1];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}

	public static double getUpCell(LiveCell liveCell){
		if( liveCell.getY()>1) {
			Cell nextCell=cells[liveCell.getX()][liveCell.getY()-1];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}

	public static double getRightCell(LiveCell liveCell){
		
		if( liveCell.getX()<width-1) {
			Cell nextCell=cells[liveCell.getX()+1][liveCell.getY()];
			return getNextCell(nextCell,liveCell);
		}else {
			return wallValue;
		}
	}

	public static double getLeftCell(LiveCell liveCell){
		if( liveCell.getX()>1) {
			Cell nextCell=cells[liveCell.getX()-1][liveCell.getY()];
			return getNextCell(nextCell,liveCell);
		}
		else {
			return wallValue;
		}
	}
		
	public static double getNextCell(Cell nextCell,LiveCell liveCell) {
		LiveCell nextLiveCell=nextCell.liveCell;
		if(nextLiveCell!=null){
			boolean isRelative=nextLiveCell instanceof NormCell? isRelatives(liveCell.getHead(),(NormCell)nextLiveCell) : false;
          	if(isRelative || isMyPart(liveCell.getHead(), nextLiveCell) ){
          		return relativesValue;
          	}else {
          		return normCellValue;
          	}
		} 
		else{
    	   if(nextCell.organic>1) {
				return Math.min(nextCell.organic/organicDil, nextOrganicValue);
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
	       System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOORK");
	
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
	 
 }