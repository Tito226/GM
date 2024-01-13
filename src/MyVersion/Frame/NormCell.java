package MyVersion.Frame;


import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.Network;


import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.World.*;

public class NormCell implements  Serializable {
    //TODO ЕНЕРГИЯ СЧИТАЕТСЯ НЕПРАВИЛЬНО, КЛЕТКА МОЖЕТ РАЗМНОЖИТСЯ БЕЗ НУЖНОЙ ЕНЕРГИИ
    //*****************************************
    Network brain;
    //*****************************************
    byte counter=0;
    float readyToMultiply=0.0f;
    int lifeTime=0;
    int partNum=0;
    ArrayList< Integer[]> myParts=new ArrayList<>();
    float[] outputs;
    int multiplies=0;
    Random r=new Random();
    long myParentNum;
    long myChildNum=r.nextLong();
    static long num = 0L;
    private final long myNum;
    boolean stepN=false;
    int energy;
    private int x;
    private int y;
    NormCellType normCellType=NormCellType.MOVABLE;
    public String partName="part";

    public int getX() {
        return x;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    Color myColor=Color.green;
   

    

    public NormCell(Network brain){//brain doesnt copy need to clone
    	Random r=new Random();
        this.brain=BrainCloneClass.networkClone(brain);
        myNum=num;
        num++;
        energy=NORM_CELL_START_ENERGY;
        cellls++;
        if(r.nextInt(4)==1)
        brain.mutate(2);//TODO delete it   !!!
    }
/* public NormCell(long myParentNum){
        myNum=num;
        num++;
        cellls++;
        energy=NORM_CELL_START_ENERGY;
       this.myParentNum=myParentNum;
        Random r=new Random();
        if(r.nextInt(20)==1){

        }
    }*/
    double isRaedyToMultiply(){
        if(energy>ENERGY_NEEDED_TO_MULTIPLY+2){
            return 1;
        }else
            return 0;
 }

 	boolean isMyPart(PartCell partCell){
   if (myParts.contains(partCell.myCoords)){
       System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOORK");

       return true;
   } else{
       return false;}
}
    public long getMyNum() {
        return myNum;
    }
    ////////////////////////////////////////////////
//////////////////////////////////////////////
    public void move(Directions d){
        Random random=new Random();
        int i5 = random.nextInt(2);
        switch (d){/*TODO THINK ABOUT*/
            case DOWN ->{
                y++;
                if (  y<height && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(this);
                cells[x][y-1].setSecCell(null);
                }
                else {
                    y--;

                }
            }
            case UP -> {
                y--;
                if (y>-1 &&  y<height-1 && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(this);
                cells[x][y+1].setSecCell(null);}
                else{
                    y++;

                }

            }
            case LEFT ->{
                x--;
                if (x>-1 && cells[x][y].secCell==null && cells[x][y].partCell==null && myParts.size()==0){
                cells[x][y].setSecCell(this);
                cells[x+1][y].setSecCell(null);
                }
                else {
                    x++;

                }
            }
            case RIGHT ->{ x++;
                if(x<width && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(this);
                cells[x-1][y].setSecCell(null);
                }
                else {
                    x--;

                }
             }
        }
        energy-=1; /*TODO ПЕРЕСМОТРЕТЬ*/
        
    }
    ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }



  public double getLeftCell(){
       if(  x>1 && cells[x-1][y].secCell!=null ){
           if(isRelatives(  cells[x-1][y].secCell)){return 2.5f;}else
           return 1.00f;
       }else if(x>1 && cells[x-1][y].partCell!=null  && isMyPart(cells[x-1][y].partCell)){
           return 1.5f;
       }
       else if(x>1 && cells[x-1][y].partCell!=null ){return 2.00f;}
       else{
       return 0.00f;}
  }
    public double getRightCell(){
        if( x<width-1 &&  cells[x+1][y].secCell!=null  ){
            if(isRelatives( cells[x+1][y].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(x<width-1 &&  cells[x+1][y].partCell!=null  && isMyPart(cells[x+1][y].partCell)){
            return 1.5f;
        }
        else if(x<width-1 &&  cells[x+1][y].partCell!=null){return 2.00f;}
        else{
        return 0.00f;
        }
    }
    public double getUptCell(){
        if( y>0  && cells[x][y-1].secCell!=null  ){
            if(isRelatives( cells[x][y-1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y>0  && cells[x][y-1].partCell!=null  && isMyPart(cells[x][y-1].partCell)){
            return 1.5f;
        }
        else if(y>0  && cells[x][y-1].partCell!=null){return 2.00f;}
        else{
        return 0.00f;}
    }
    public double getDownCell(){
        if(  y<height-1&& cells[x][y+1].secCell!=null ){
            if(isRelatives( cells[x][y+1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y<height-1&& cells[x][y+1].partCell!=null && isMyPart(cells[x][y+1].partCell)){
            return 1.5f;
        } else if( y<height-1&& cells[x][y+1].partCell!=null){return 2.00f;}
        else{
        return 0.00f;}
    }
    public double getRightDownCell(){
        if(  y<height-1&& x<width-1 && cells[x+1][y+1].secCell!=null ){
            if(isRelatives( cells[x+1][y+1].secCell)){return 2.5f;}else
                return 1.00f;
        }else if(y<height-1 && x<width-1&& cells[x+1][y+1].partCell!=null && isMyPart(cells[x+1][y+1].partCell)){
            return 1.5f;
        } else if( y<height-1 && x<width-1&& cells[x+1][y+1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}
    }
    public double getRightUpCell(){
        if(  y>0&& x<width-1 && cells[x+1][y-1].secCell!=null ){
            if(isRelatives(cells[x+1][y-1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y>0 && x<width-1&& cells[x+1][y-1].partCell!=null && isMyPart(cells[x+1][y-1].partCell)){
            return 1.5f;
        } else if( y>0 && x<width-1&& cells[x+1][y-1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}

    }
    public double getLeftUpCell(){
        if(  y>0&& x>0 && cells[x-1][y-1].secCell!=null ){
            if(isRelatives( cells[x-1][y-1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y>0 &&x>0&& cells[x-1][y-1].partCell!=null && isMyPart(cells[x-1][y-1].partCell)){
            return 1.5f;
        } else if( y>0 && x>0&& cells[x-1][y-1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}
    }
    public double getLeftDownCell(){
        if(  y<height-1&& x>0 && cells[x-1][y+1].secCell!=null ){
            if(isRelatives( cells[x-1][y+1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y<height-1 && x>0&& cells[x-1][y+1].partCell!=null && isMyPart(cells[x-1][y+1].partCell)){
            return 1.5f;
        } else if( y<height-1 && x>0&& cells[x-1][y+1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}
    }
    double getLeftDistance(){//TODO MAY FIX FOR DOUBLE
        int ix=x;

        int i=1;
        while (true){
            if(ix<1){ break;}else
            if (cells[ix-1][y].secCell!=null || cells[ix-1][y].partCell!=null){
                break;
            }
            i+=1;
            ix--;
        }
        float ii=i;
        return ii/dil;
    }
    float dil=1000f;
    double getRightDistance(){
        int ix=x;

        int i=1;
    while (true){
        if(ix>=width-5){ break;}else
        if (cells[ix+1][y].secCell!=null || cells[ix+1][y].partCell!=null){
       break;
        }
        i+=1;
        ix++;
    }
     float ii=i;
     return ii/dil;
 }
    double getUpDistance(){
        int iy=y;
        int i=1;
        while (true){
            if(iy<1){ break;}else
            if (cells[x][iy-1].secCell!=null || cells[x][iy-1].partCell!=null){
                break;
            }
            i++;
            iy--;
        }
        float ii=i;
        return ii/dil;
    }
    double getDownDistance(){
        int iy=y;
        int i=1;
        while (true){
            if(iy>=height-10){ break;}else
            if (cells[x][iy+1].secCell!=null || cells[x][iy+1].partCell!=null){
                break;
            }
            i+=1;
            iy++;
        }
        float ii=i;
        return ii/dil;
    }

    void step1(){
    	outputs=new float[brain.getDotsArr().size()];
        for (int i = 0; i < myParts.size(); i++) {
          //  cells[myParts.get(i)[0]][myParts.get(i)[1]].partCell.step(outputs[i+1]);

        }
        if(outputs[0]>0.64 && outputs[0]<0.68){
        	new  Protoplast(this, outputs[0],x,y);
       //   System.out.println("WRYYYYY");
        }
        if (outputs[0]>0.3 && outputs[0]<0.35){
            multiply();
        }
        Random r=new Random();

        energy--;
        lifeTime++;
       }
    Protoplast protoplast;
   //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
   //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    void step(){//TODO  Step
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

       if(isSpaceAvailable()==0){
           energy-=1;
       }
       double output=evaluateFitness();
       if(output>0.1f && output<0.125f){
        move(Directions.UP);
       }else if (output>0.125f && output<0.15f){
        move(Directions.DOWN);
       }else if (output>0.15f && output<0.175f){
    	   move(Directions.LEFT);
       } else if (output>0.175f && output<0.2f){
    	   move(Directions.RIGHT);
       }
       if (output>0.2 && output<0.3){eatOrganic();}
       else  if(output>0.6 && output<0.61){
    	   if(!eatCell(Directions.RIGHT)){energy--;}

       }else if(output>0.61 && output<0.62){
    	   if(!eatCell(Directions.LEFT)){energy--;}

       }else if(output>0.62 && output<0.63){
    	   if(!eatCell(Directions.UP)){energy--;}

       }else if(output>0.63 && output<0.64){
      	 if (!eatCell(Directions.DOWN)){energy--;}

       }else if(output>0.64 && output<0.68){
    	   protoplast=new Protoplast(this, output,x,y);
       }else if(output>0.68 && output<0.8){


       } else if(output>0.9f && output<1f ){
    	   multiply();
       }
       setLastThings();
       energy--;
       lifeTime++;
       }
    }
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    public void setMyColor(Color myColor) {
        this.myColor = myColor;
    }

    public Color getColor(){
        return  myColor;    //TODO set Changeable cell color
    }


    public int getEnergy() {
        return energy;
    }

    void eatOrganic(){
        if(cells[x][y].getOrganic()!=0  && cells[x][y].getOrganic()>2){
           energy+=HOW_MUCH_ORGANIC_EATS_PER_STEP;
            cells[x][y].setOrganic(cells[x][y].getOrganic()-3);
        }
    }

    void transferEnergy(float output){/*TODO ЗАГЛУШКА*/
    	if (output>0.7 && output>0.72) {
    		
    	}else if(output>0.72 && output>0.74){
    		
    	}
    }

double isSpaceAvailable(){
     boolean b1=true;
     boolean b2=true;
     boolean b3=true;
     boolean b4=true;
     if(y>0 && cells[x][y-1].secCell==null){}else b1=false;
     if(y<height-1 && cells[x][y+1].secCell==null){}else b2=false;
     if(x<width-1 &&  cells[x+1][y].secCell==null){}else b3=false;
     if( x>0 && cells[x-1][y].secCell==null){}else b4=false;
     if(!b1 && !b2 && !b3 && !b4){
           return 0.0d;
     }else{return 1.0d;}
 }
    int enValue=10;
    public boolean eatCell(Directions dirs){

        boolean done = false;
        switch (dirs){

            case UP -> {
                if( y>0 && cells[x][y-1].secCell!=null ){
                    energy+= cells[x][y-1].secCell.energy;
                    cells[x][y-1].secCell= cells[x][y].secCell;
                    cells[x][y].secCell=null;
                    done= true;
                }else done= false;
            }

            case DOWN -> {
                if(y<height-1 && cells[x][y+1].secCell!=null){
                    energy+= cells[x][y+1].secCell.energy;
                    cells[x][y+1].secCell= cells[x][y].secCell;
                    cells[x][y].secCell=null;
                    done= true;
                }else done= false;
            }

            case RIGHT -> {
                if( x<width-1 &&  cells[x+1][y].secCell!=null ){
                    energy+= cells[x+1][y].secCell.energy;
                    cells[x+1][y].secCell= cells[x][y].secCell;
                    cells[x][y].secCell=null;
                    done= true;
                }else done = false;
            }

            case LEFT -> {
                if(  x>0 && cells[x-1][y].secCell!=null){
                    energy+= cells[x-1][y].secCell.energy;
                    cells[x-1][y].secCell= cells[x][y].secCell;
                    cells[x][y].secCell=null;
                    done= true;
                }else done= false;
            }

            }

    if(done){cellls--;
    }
      //  System.out.println(done);
        return done;
    }

   boolean isRelatives(NormCell normCell1){
	   if(normCell1.myParentNum ==myParentNum){
		   return true;
	   }else if (normCell1.myChildNum ==myParentNum){
		   return true;}
	   else {
		   return false;
	   }
   }


    void multiply(){
        /*TODO ПЕРЕДЕЛАТЬ*/
       if(isSpaceAvailable()==1.00f && energy>ENERGY_NEEDED_TO_MULTIPLY){
       boolean shitHappened=false;
       Random random1=new Random();
       int xxx =random1.nextInt(4);
       if(xxx==0){
        if(x<width-1 && cells[x+1][y].secCell ==null && cells[x+1][y].partCell==null ){
        	cells[x+1][y].setSecCell(new NormCell(brain));
        	energy-=ENERGY_NEEDED_TO_MULTIPLY;
        	multiplies++;
        }
       }else if(xxx==1){
           if(x>0 && cells[x-1][y].secCell ==null   && cells[x-1][y].partCell==null){
               cells[x-1][y].setSecCell(new NormCell(brain));
               energy-=ENERGY_NEEDED_TO_MULTIPLY;
               multiplies++;
           }
       } else if (xxx==2){
           if(y>0 && cells[x][y-1].secCell ==null && cells[x][y-1].partCell==null){
               cells[x][y-1].setSecCell(new NormCell(brain));
               energy-=ENERGY_NEEDED_TO_MULTIPLY;
               multiplies++;
           } else if(xxx==3){
               if(y<height-1 && cells[x][y+1].secCell ==null && cells[x][y+1].partCell==null ){
                   cells[x][y+1].setSecCell(new NormCell(brain));
                   energy-=ENERGY_NEEDED_TO_MULTIPLY;
                   multiplies++;
               }else{
                   shitHappened=true;
                   multiply();}
           }

       }
       }else{
          energy--;
          return;
       }
       
}



    double isController(){
     if (normCellType==NormCellType.CONTROLLER)  {
         return 10.0f;
     }
     else return 0f;
    }


    double lastRightDistance=0d;
    double lastLeftDistace=0d;
    double lastUpDistance=0d;
    double lastDownDistance=0d;
    int lastEnergy=0;
    double lastRightUpCell=0d;
    double lastRightDownCell=0d;
    double lastLeftUpCell=0d;
    double lastLeftDownCell=0d;
    double lastUpCell=0d;
    double lastDownCell=0d;
    double lastLeftCell=0d;
    double lastRightCell=0d;
    int lastOrganic=0;
    int lastSize=0;
    void setLastThings(){
        lastEnergy=energy;
        lastUpCell=getUptCell();
        lastDownCell=getDownCell();
        lastLeftCell=getLeftCell();
        lastRightCell=getRightCell();
        lastOrganic=cells[x][y].organic;
        lastSize=myParts.size();
        lastRightDistance=getRightDistance();
        lastLeftDistace=getLeftDistance();
        lastUpDistance=getUpDistance();
        lastDownDistance=getDownDistance();
        lastRightUpCell=getRightUpCell();
        lastRightDownCell=getRightDownCell();
        lastLeftUpCell=getLeftUpCell();
        lastLeftDownCell=getLeftDownCell();
    }



//*********************************************
    public float getOutputBuffer() {
        synchronized (this){
        return outputBuffer;
        }
    }

    public void setOutputBuffer(float outputBuffer) {
        synchronized (this){
        this.outputBuffer = outputBuffer;
        }
    }


    void bestOutputsAdd(Float f){
        synchronized (NormCell.this){
        bestOutputs.add(f);
        }
	}
	void bestOuputsClear(){
        synchronized (NormCell.this){
            bestOutputs.clear();
        }
	}

    private float outputBuffer;
    ArrayList<Float> bestOutputs;

    Double[] getInputData() {
    	Double[] inputs = {isRaedyToMultiply() , (double) getEnergy(), (double) cells[x][y].getOrganic(), getUptCell(), getDownCell(), getLeftCell(),
    			 
    			 (double) getRightCell(),(double) getRightDownCell(),getRightUpCell(),getLeftUpCell(),getLeftDownCell(), isSpaceAvailable(), isController(),
    			 
    			 getRightDistance(),getLeftDistance(),getUpDistance(),getDownDistance(), (double) lastEnergy,lastUpCell,lastDownCell,lastLeftCell,
    			 
    			  lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,  (double) lastOrganic, (double) sunny, (double) myParts.size(),
    			 
    			 (double) lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
    	 return inputs;
    }
    
    
    //************************************************
    public double evaluateFitness() {
       return brain.evaluteFitness(getInputData(),false);
    }
    public double evaluateFitness(Double[] inputData) {
       return brain.evaluteFitness(inputData,false);
    }

     public int eatCellT(Directions dirs){
        int done = 0;
        switch (dirs){
            case UP -> {
                if( y>0 && cells[x][y-1].secCell!=null ){
                    if (isRelatives(cells[x][y-1].secCell)){
                        done=2;
                    }else
                    done= 1;
                    enValue=cells[x][y-1].secCell.energy;
                }else done= 0;
            }
            case DOWN -> {
                if(y<height-1 && cells[x][y+1].secCell!=null){
                    if (isRelatives(cells[x][y+1].secCell)){
                        done=2;
                    }else
                    done= 1;
                    enValue=cells[x][y+1].secCell.energy;
                }else done= 0;
            }
            case RIGHT -> {
                if( x<width-1 &&  cells[x+1][y].secCell!=null ){
                    if (isRelatives(cells[x+1][y].secCell)){
                        done=2;
                    }else
                    done= 1;
                    enValue=cells[x+1][y].secCell.energy;
                }else done = 0;
            }
            case LEFT -> {
                if(  x>0 && cells[x-1][y].secCell!=null){
                    if (isRelatives(cells[x-1][y].secCell)){
                        done=2;
                    }else
                    done= 1;
                    enValue=cells[x-1][y].secCell.energy;
                }else done= 0;
            }
        }
        return done;
    }



}