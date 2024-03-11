package MyVersion.Cells;


import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Network;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import MyVersion.Frame.Action_Boundaries;
import MyVersion.Frame.NetworkWrapper;

import static MyVersion.Core.Core_Config.*;
import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.World.*;

public class NormCell implements  Serializable,LiveCell {
    //*****************************************
    public NetworkWrapper brain;
    public NetworkWrapper multiCellbrain;
    Random r=new Random();
    //*****************************************
    public int multiplies=0;
    int energy,lifeTime=0,partNum=0;
    private int x,y;
    byte counter=0,idleEnergyDecreseCounter=0;
    float readyToMultiply=0.0f;
    float[] outputs;
    long myParentNum,myChildNum=r.nextLong();
    static long num = 0L;
    private final long myNum;
    public boolean stepN=false;
    double lastOutput=0d,preLastOutput=0d;
    public String partName="part";
    //*****************************************
    ArrayList<LiveCell> myParts=new ArrayList<>();
    public DataMethods myMethods=new DataMethods();
    NormCellType normCellType=NormCellType.MOVABLE;
    Color myColor=Color.green;
    
    public int getX() {
        return x;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    @Override
    public int getEnergy() {
    	return energy;
    }
   
    public NormCell(Network brain){
    	Random r=new Random();
    	this.brain=new NetworkWrapper(BrainCloneClass.networkClone(brain));
        myNum=num;
        num++;
        energy=NORM_CELL_START_ENERGY;
        if(!DEBUG) {
        	if(r.nextInt(MUTATION_CHANCE)==0) {
        		this.brain.mutate(NUMBER_OF_MUTATIONS);
        	}
        }
    }
    
    public long getMyNum() {
        return myNum;
    }
    
    public void move(Directions d){
        Random random=new Random();
        int i5 = random.nextInt(2);
        switch (d){/*TODO THINK ABOUT*/
            case DOWN ->{              
                if (y<height-1){
                	move(cells[x][y+1]);
                }
            }
            case UP -> {
                if (y>0){
                	move(cells[x][y-1]);
                } 
            }
            case LEFT ->{
                if (x>0){
                	move(cells[x-1][y]);
                }   
            }
            case RIGHT ->{ 
                if(x<width-1){
                	move(cells[x+1][y]);
                }            
            }
        }
        energy-=1; /*TODO ПЕРЕСМОТРЕТЬ*/
        
    }
    private void move(Cell nextCell) {
    	if (nextCell.liveCell==null && myParts.size()==0){
    		nextCell.setLiveCell(this);
            cells[x][y].setLiveCell(null);
            this.setX(nextCell.getX());
    		this.setY(nextCell.getY());
        }
    }
    
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

  void step1(){
    	outputs=new float[brain.getDotsArr().size()];
        for (int i = 0; i < myParts.size(); i++) {
          //  cells[myParts.get(i)[0]][myParts.get(i)[1]].partCell.step(outputs[i+1]);

        }
        if(outputs[0]>0.64 && outputs[0]<0.68){
        	new  Protoplast(this, outputs[0]);
       //   System.out.println("WRYYYYY");
        }
        if (outputs[0]>0.3 && outputs[0]<0.35){
            multiply();
        }
        Random r=new Random();

        energy--;
        lifeTime = getLifeTime() + 1;
       }
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  	@Override
    public void step(){//TODO  Step
        if(myParts.size()==0){
            normCellType=NormCellType.MOVABLE;
            setMyColor(Color.green);
        }
        if (normCellType==NormCellType.CONTROLLER){
            setMyColor(Color.darkGray);
            step1();
        }
        else {
        if(myParts.size()>0){
            normCellType=NormCellType.CONTROLLER;
            step1();
            return;
        }

       if(myMethods.isSpaceAvailable(this)==0){
           energy-=1;
       }
       double output=evaluateFitness();
       if(DEBUG) {
    	   double[] inputBuff=new double[HOW_MUCH_INPUTS_MUST_BE_USED+1];
    	   for (int i = 0; i < inputBuff.length; i++) {
    		   inputBuff[i]=myMethods.getInputData()[i];
    	   }
    	   System.out.println(Arrays.toString(inputBuff)+"  ");
    	   System.out.println("output: "+output);
    	   System.out.println("--------------------------------------------------");
    	   try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	   
       }
       preLastOutput=lastOutput;
       lastOutput=output;
       if(output>Action_Boundaries.moveUpBoundaries[0] && output<Action_Boundaries.moveUpBoundaries[1]){
    	   move(Directions.UP);
       }else if (output>Action_Boundaries.moveDownBoundaries[0] && output<Action_Boundaries.moveDownBoundaries[1]){
    	   move(Directions.DOWN);
       }else if (output>Action_Boundaries.moveLeftBoundaries[0] && output<Action_Boundaries.moveLeftBoundaries[1]){
    	   move(Directions.LEFT);
       } else if (output>Action_Boundaries.moveRightBoundaries[0] && output<Action_Boundaries.moveRightBoundaries[1]){
    	   move(Directions.RIGHT);
       }
       if (output>Action_Boundaries.eatOrganicBoundaries[0] && output<Action_Boundaries.eatOrganicBoundaries[1]){
    	   eatOrganic();
       }
       else  if(output>0.6 && output<0.61){
    	   if(!eatCell(Directions.RIGHT)){energy--;}

       }else if(output>0.61 && output<0.62){
    	   if(!eatCell(Directions.LEFT)){energy--;}

       }else if(output>0.62 && output<0.63){
    	   if(!eatCell(Directions.UP)){energy--;}

       }else if(output>0.63 && output<0.64){
      	 if (!eatCell(Directions.DOWN)){energy--;}

       }else if(output>0.64 && output<0.68){
    	   new Protoplast(this, output);
       }else if(output>0.68 && output<0.8){


       } else if(output>Action_Boundaries.multiplyBoundaries[0] && output<Action_Boundaries.multiplyBoundaries[1] ){
    	   multiply();
       }
       idleEnergyDecreseCounter++;
       if(idleEnergyDecreseCounter%2==0) {//2
    	   energy--;
       }
       setLastThings();
       lifeTime = getLifeTime() + 1;
       }
   }
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    public void setMyColor(Color myColor) {
        this.myColor = myColor;
    }
    @Override
    public Color getColor(){
        return  myColor;    //TODO set Changeable cell color
    }

    void eatOrganic(){
        if(cells[x][y].getOrganic()!=0  && cells[x][y].getOrganic()>2){
           energy+=HOW_MUCH_ORGANIC_EATS_PER_STEP;
            cells[x][y].setOrganic(cells[x][y].getOrganic()-3);
        }else {
        	energy+=cells[x][y].getOrganic();
        	cells[x][y].setOrganic(0);
        }
    }

    void transferEnergy(double output){/*TODO ЗАГЛУШКА*/
    	if (output>0.7 && output>0.72) {
    		
    	}else if(output>0.72 && output>0.74){
    		
    	}
    }

    public boolean eatCell(Directions dirs){

        switch (dirs){

            case UP -> {
                if( y>0){
                	return eatCell(cells[x][y-1]);
                }
            }

            case DOWN -> {
                if(y<height-1 ){
                	return eatCell(cells[x][y+1]); 
                }
            }

            case RIGHT -> {
                if( x<width-1 ){
                	return eatCell(cells[x+1][y]);
                }
            }

            case LEFT -> {
                if(  x>0 ){
                	return eatCell(cells[x-1][y]);
                }
            }

            }

    
      //  System.out.println(done);
        return false;
    }

    private boolean eatCell(Cell nextCell) {//TODO доработать
    	if(nextCell.liveCell!=null ){
            energy+= nextCell.liveCell.energy;
            nextCell.liveCell=null;
            move(nextCell);
            return true;
        }else { 
        	return false;
        }
    }
    
    void multiply(){
        /*TODO ПЕРЕДЕЛАТЬ*/
       if(myMethods.isSpaceAvailable(this)==1.00f && energy>ENERGY_NEEDED_TO_MULTIPLY){
    	   boolean shitHappened=false;
    	   int xxx =r.nextInt(4);
    	   	 switch (xxx){
    	   	 	case 1 -> {
    	   	 		if( y>0){
    	   	 			multiply(cells[x][y-1]); 
    	   	 		}
    	   	 	}

    	   	 	case 2 -> {
    	   	 		if(y<height-1 ){
    			   multiply(cells[x][y+1]);
    	   	 		}
    	   	 	}

    	   	 	case 3 -> {
    	   	 		if( x<width-1 ){
    	   	 			multiply(cells[x+1][y]);
    	   	 		}
    	   	 	}

    	   	 	case 4 -> {
    	   	 		if(  x>0 ){
    	   	 			multiply(cells[x-1][y]);
    	   	 		}
    	   	 	}
    	   	 	default->{
    	   	 		multiply(); 
    	   	 	}
    	   	 }

       }
       else{
          energy--;
       } 
    }
    private void multiply(Cell nextCell) {
    	if(nextCell.liveCell ==null){
    		nextCell.setLiveCell(new NormCell(brain));
        	energy-=ENERGY_NEEDED_TO_MULTIPLY;
        	multiplies++;
        }
    }
    double isController(){
     if (normCellType==NormCellType.CONTROLLER)  {
         return 10.0f;
     }
     else return 0f;
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
    
    void setLastThings(){
        lastEnergy=energy;
        lastUpCell=myMethods.getUpCell(this);
        lastDownCell=myMethods.getDownCell(this);
        lastLeftCell=myMethods.getLeftCell(this);
        lastRightCell=myMethods.getRightCell(this);
        lastOrganic=cells[x][y].organic;
        lastSize=myParts.size();
        lastRightDistance=myMethods.getRightDistance(this);
        lastLeftDistace=myMethods.getLeftDistance(this);
        lastUpDistance=myMethods.getUpDistance(this);
        lastDownDistance=myMethods.getDownDistance(this);
        lastRightUpCell=myMethods.getRightUpCell(this);
        lastRightDownCell=myMethods.getRightDownCell(this);
        lastLeftUpCell=myMethods.getLeftUpCell(this);
        lastLeftDownCell=myMethods.getLeftDownCell(this);
    }


    
    public double evaluateFitness() {
       return brain.evaluteFitness(myMethods.getInputData(),false);
    }
    
    public double evaluateFitness(Double[] inputData) {
       return brain.evaluteFitness(inputData,false);
    }
    //*********************************************
    
	 int enValue=10;
    
    //************************************************
     


     public int getLifeTime() {
		return lifeTime;
	}


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
		public Double[] getInputData() {
    	    	Double[] inputs = {myMethods.isRaedyToMultiply(NormCell.this) , (double) myMethods.getEnergy(NormCell.this), (double) cells[x][y].getOrganic()/organicDil, myMethods.getUpCell(NormCell.this), myMethods.getDownCell(NormCell.this), myMethods.getLeftCell(NormCell.this),
    	    			 
    	    			 (double) myMethods.getRightCell(NormCell.this),lastOutput,preLastOutput,(double) myMethods.getRightDownCell(NormCell.this),myMethods.getRightUpCell(NormCell.this),myMethods.getLeftUpCell(NormCell.this),myMethods.getLeftDownCell(NormCell.this), myMethods.isSpaceAvailable(NormCell.this), isController(),
    	    			 
    	    			 myMethods.getRightDistance(NormCell.this),myMethods.getLeftDistance(NormCell.this),myMethods.getUpDistance(NormCell.this),myMethods.getDownDistance(NormCell.this), (double) lastEnergy,lastUpCell,lastDownCell,lastLeftCell,
    	    			 
    	    			  lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,  (double) lastOrganic, (double) sunny, (double) myParts.size(),
    	    			 
    	    			 (double) lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
    	    	 return inputs;
    	    }

		boolean isRelatives(NormCell normCell, NormCell normCell1){
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

		public int getEnergy(LiveCell liveCell) {
		    return liveCell.getEnergy();
		}

		double getDownDistance(NormCell normCell){
			if(SIMPLE_DISTANCE) {
				return Math.max(maxDistance,normCell.y)/distanceDill;
		    }else {
		    	for (int curY =normCell.y ; curY > 0; curY--) {
					Cell curCell=cells[normCell.x][curY];
					if(curCell.liveCell!=null || curY>maxDistance){
						return curY/distanceDill;
					}
				}
		    	return Math.max(maxDistance, normCell.y)/distanceDill;
		    }
		}

		double getUpDistance(NormCell normCell){
			if(SIMPLE_DISTANCE) {
				return Math.max( maxDistance, cells[0].length-normCell.y)/distanceDill;
			}else {
				for (int curY =normCell.y ; curY < cells[0].length; curY++) {
					Cell curCell=cells[normCell.x][curY];
					if(curCell.liveCell!=null || cells[0].length-curY>maxDistance){
						return (cells[0].length-curY)/distanceDill;
					}
				}
				return Math.max( maxDistance, cells[0].length-normCell.y)/distanceDill;
		    }
		}

		double getRightDistance(NormCell normCell){
			if(SIMPLE_DISTANCE) {
				return Math.max(maxDistance, cells.length-normCell.x)/distanceDill;
			}else {
				for (int curX =normCell.x ; curX < cells.length; curX++) {
					Cell curCell=cells[curX][normCell.y];
					if(curCell.liveCell!=null || cells.length-curX>maxDistance){
						return (cells.length-curX)/distanceDill;
					}
				}
		    	return Math.max(maxDistance, cells.length-normCell.x)/distanceDill;
		    }
		}

		double getLeftDistance(NormCell normCell){
			if(SIMPLE_DISTANCE) {
				return Math.max(maxDistance, normCell.x)/distanceDill;
			}else {
				for (int curX =normCell.x ; curX > 0; curX--) {
					Cell curCell=cells[curX][normCell.y];
					if(curCell.liveCell!=null || curX>maxDistance){
						return curX/distanceDill;
					}
				}
		    	return Math.max(maxDistance, cells.length-normCell.x)/distanceDill;
		    }
		}

		public double getLeftDownCell(NormCell normCell){
			if(  normCell.y<height-1 && normCell.x>1) {
				Cell nextCell=cells[normCell.x-1][normCell.y+1];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}

		public double getLeftUpCell(NormCell normCell){
			if(  normCell.y>1 && normCell.x>1) {
				Cell nextCell=cells[normCell.x-1][normCell.y-1];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}

		public double getRightUpCell(NormCell normCell){
			if(  normCell.y>1 && normCell.x<width-1) {
				Cell nextCell=cells[normCell.x+1][normCell.y-1];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}

		public double getRightDownCell(NormCell normCell){
			if( normCell.y<height-1 && normCell.x<width-1) {
				Cell nextCell=cells[normCell.x+1][normCell.y+1];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}
		//

		public double getDownCell(NormCell normCell){
			if( normCell.y<height-1) {
				Cell nextCell=cells[normCell.x][normCell.y+1];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}

		public double getUpCell(NormCell normCell){
			if( normCell.y>1) {
				Cell nextCell=cells[normCell.x][normCell.y-1];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}

		public double getRightCell(NormCell normCell){
			
			if( normCell.x<width-1) {
				Cell nextCell=cells[normCell.x+1][normCell.y];
				return getNextCell(nextCell,normCell);
			}else {
				return wallValue;
			}
		}

		public double getLeftCell(NormCell normCell){
			if( normCell.x>1) {
				Cell nextCell=cells[normCell.x-1][normCell.y];
				return getNextCell(nextCell,normCell);
			}
			else {
				return wallValue;
			}
		}
			
		public double getNextCell(Cell nextCell,NormCell normCell) {
			LiveCell nextLiveCell=nextCell.liveCell;
			if(nextLiveCell!=null){
				boolean isRelative=nextLiveCell instanceof NormCell? isRelatives(normCell,(NormCell)nextLiveCell) : false;
	          	if(isRelative || normCell.myMethods.isMyPart(normCell, nextLiveCell) ){
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
		
		double isRaedyToMultiply(NormCell normCell){
		    if(normCell.energy>ENERGY_NEEDED_TO_MULTIPLY+2){
		        return 1d;
		    }else
		        return 0d;
		}

		boolean isMyPart(NormCell normCell, LiveCell partCell){
		   if (normCell.myParts.contains(partCell)){
		       System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOORK");
		
		       return true;
		   } else{
		       return false;}
		}

		double isSpaceAvailable(NormCell normCell){
		     boolean b1=true;
		     boolean b2=true;
		     boolean b3=true;
		     boolean b4=true;
		     if(normCell.y>0 && cells[normCell.x][normCell.y-1].liveCell==null){}else b1=false;
		     if(normCell.y<height-1 && cells[normCell.x][normCell.y+1].liveCell==null){}else b2=false;
		     if(normCell.x<width-1 &&  cells[normCell.x+1][normCell.y].liveCell==null){}else b3=false;
		     if( normCell.x>0 && cells[normCell.x-1][normCell.y].liveCell==null){}else b4=false;
		     if(!b1 && !b2 && !b3 && !b4){
		           return 0.0d;
		     }else{return 1.0d;}
		 }
    	 
     }


	@Override
	public void test() {
		if (this.myMethods.getEnergy(this) <= 0) {
			this.kill();
        }/*else if (this != null && this.myMethods.getEnergy(this) >= 10000) {
            System.out.println("Cell with 10000 died");
            organic += liveCell.myMethods.getEnergy(liveCell);
            liveCell.brain.kill();
            setSecCell(null);

        }*/
		
	}

	@Override
	public void kill() {
		for (int i = -1; i < 2; i++) {
  			for (int j = -1; j < 2; j++) {
  				if(x+i<cells.length && x+i>0 && y+j>0 && y+j<cells[1].length) {
  					cells[x+i][y+j].organic+=this.myMethods.getEnergy(this)+ORGANIC_PER_CELL_ON_NORMCELL_DEATH;
  					cells[x+i][y+j].setChange(true);
  				}
  			}
		}
  		if(!this.brain.dontDelete) {
  			this.brain.kill();
  		}else {
  			this.brain.isDead=true;
  		}
  		cells[x][y].setLiveCell(null);
		
	}

	
}