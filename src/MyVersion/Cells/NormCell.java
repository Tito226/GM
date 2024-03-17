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
    public NetworkWrapper brain;//Use while myParts.size()==0
    public NetworkWrapper multiCellbrain;//Use while myParts.size()>0
    Random r=new Random();
    //*****************************************
    public int multiplies=0;
    int energy,lifeTime=0,partNum=0;
    private int x,y;
    byte counter=0;
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
   
    public NormCell(Network brain){
    	Random r=new Random();
    	this.brain=new NetworkWrapper(BrainCloneClass.networkClone(brain));
    	this.multiCellbrain=new NetworkWrapper(BrainCloneClass.networkClone(brain));
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
    private void move(Cell nextCell) {//TODO MAKE CELL EAT ON MOVE
    	if (nextCell.liveCell==null && myParts.size()==0){
    		nextCell.setLiveCell(this);
            cells[x][y].setLiveCell(null);
            this.setX(nextCell.getX());
    		this.setY(nextCell.getY());
        }else if(nextCell.liveCell==null){
        	eatCell(nextCell);
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

    public void idleEnergyDecrese() {
    	if(lifeTime%2==0) {//2
     	   energy--;
        }
    }
    
    void step1(){
    	outputs=new float[brain.getDotsArr().length];
        for (LiveCell curCell : myParts) {
        	curCell.step();
        }
        energy--;
        lifeTime++;
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
        }else {
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
    	   //if(cells[x][y].organic<=0 && between(Action_Boundaries.eatOrganicBoundaries,output) ) {
    	   double[] inputBuff=new double[HOW_MUCH_INPUTS_MUST_BE_USED+10];
    	   for (int i = 0; i < inputBuff.length; i++) {
    		   inputBuff[i]=getInputData()[i];
    	   }
    	   System.out.println(Arrays.toString(inputBuff)+"  ");
    	   System.out.println("output: "+output);
    	   System.out.println("--------------------------------------------------");
    	   try {
    		   Thread.sleep(DEBUG_TIME_AFTER_STEP);
    	   } catch (InterruptedException e) {
    		   e.printStackTrace();
    	   }  
    	  // }
       }
       preLastOutput=lastOutput;
       lastOutput=output;
 
       if(DataMethods.between(Action_Boundaries.moveUpBoundaries,output)){
    	   move(Directions.UP);
       }else if (DataMethods.between(Action_Boundaries.moveDownBoundaries,output)){
    	   move(Directions.DOWN);
       }else if (DataMethods.between(Action_Boundaries.moveLeftBoundaries,output)){
    	   move(Directions.LEFT);
       } else if (DataMethods.between(Action_Boundaries.moveRightBoundaries,output)){
    	   move(Directions.RIGHT);
       }if(DataMethods.between(Action_Boundaries.eatOrganicBoundaries,output)){
    	   eatOrganic();
       }else if(DataMethods.between(Action_Boundaries.eatRightCellBoundaries,output)){
    	   if(!eatCell(Directions.RIGHT)){
    		   energy--;
    	   }

       }else if(DataMethods.between(Action_Boundaries.eatLeftCellBoundaries,output)){
    	   if(!eatCell(Directions.LEFT)){
    		   energy--;
    	   }

       }else if(DataMethods.between(Action_Boundaries.eatUpCellBoundaries,output)){
    	   if(!eatCell(Directions.UP)){
    		   energy--;
    	   }

       }else if(DataMethods.between(Action_Boundaries.eatDownCellBoundaries,output)){
      	 if (!eatCell(Directions.DOWN)){
      		 energy--;
      	 }

       }else if(output>0.64 && output<0.68){
    	   new Protoplast(this, output);
       }else if(output>0.68 && output<0.8){


       } else if(output>Action_Boundaries.multiplyBoundaries[0] && output<Action_Boundaries.multiplyBoundaries[1] ){
    	   multiply();
       }
       setLastThings();
       lifeTime++;
       idleEnergyDecrese();
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
    	//System.out.println("cell was eaten");
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
            energy+= nextCell.liveCell.getEnergy();
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
       return brain.evaluteFitness(getInputData(),false)[0];
    }
    
    public double evaluateFitness(Double[] inputData) {
       return brain.evaluteFitness(inputData,false)[0];
    }
    //*********************************************
    
	 int enValue=10;
    
    //************************************************
     


     public int getLifeTime() {
		return lifeTime;
	}

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
     
     
     public Double[] getInputData() {
	    	Double[] inputs = {myMethods.isRaedyToMultiply(this) , (double) myMethods.getEnergy(this), (double) cells[x][y].getOrganic()/myMethods.organicDil, myMethods.getUpCell(this), myMethods.getDownCell(this), myMethods.getLeftCell(this),
	    			 
	    			 (double) myMethods.getRightCell(this),lastOutput,preLastOutput,(double) myMethods.getRightDownCell(this),myMethods.getRightUpCell(this),myMethods.getLeftUpCell(this),myMethods.getLeftDownCell(this), myMethods.isSpaceAvailable(this), isController(),
	    			 
	    			 myMethods.getRightDistance(this),myMethods.getLeftDistance(this),myMethods.getUpDistance(this),myMethods.getDownDistance(this), (double) lastEnergy,lastUpCell,lastDownCell,lastLeftCell,
	    			 
	    			  lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,  (double) lastOrganic, (double) sunny, (double) myParts.size(),
	    			 
	    			 (double) lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
	    	 return inputs;
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
		Cell.organicSpreadOnDeath(this);
  		if(!this.brain.dontDelete) {
  			this.brain.kill();
  		}else {
  			this.brain.isDead=true;
  		}
  		cells[x][y].setLiveCell(null);
		
	}

	@Override
	public Integer getGeneralEnergy() {
		int generalEnergy=energy;
		for (LiveCell curPart: myParts) {
			generalEnergy+=curPart.getEnergy();
		}
		return generalEnergy;
	}

	@Override
	public NormCell getHead() {
		return this;
	}

	@Override
	public int getEnergyToMultiplyMe() {
		return ENERGY_NEEDED_TO_MULTIPLY;
	}

	
}