package MyVersion;


import MyVersion.NEAT.Environment;
import MyVersion.NEAT.Genome;
import MyVersion.NEAT.Pool;
import MyVersion.NEAT.Species;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static MyVersion.GM2_CONFIG.*;
import static MyVersion.World.*;

public class NormCell implements Environment, Serializable {
    byte counter=0;
    int lifeTime=0;
    int partNum=0;
ArrayList< Integer[]> myParts=new ArrayList<>();
    float output=0;
    float[] outputs;
    int multiplies=0;
    Random r=new Random();
    long myParentNum;
    long myChildNum=r.nextLong();
    static long num = 0L;
    private final long myNum;
    boolean stepN=false;
    private int energy=30;
    private int x;
    private int y;
    private final int startEnergy=NORM_CELL_START_ENERGY;
NormCellType normCellType=NormCellType.MOVABLE;
    public String partName="part";

    public int getX() {
        return x;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Pool movablePool =new Pool();

Color myColor=Color.green;
    public NormCell(Pool dadPool,long myParentNum){
        myNum=num;
        num++;
        movablePool=dadPool;
        cellls++;
        energy=startEnergy;
       this.myParentNum=myParentNum;
        Random r=new Random();
        if(r.nextInt(20)==1){
            movablePool.breedNewGeneration();
        }
    }

    public NormCell(Pool dadPool,int anyNum){
        myNum=num;
        num++;



        movablePool.initializePool(dadPool);
            Random r=new Random();
            if(r.nextInt(20)==1){
                movablePool.breedNewGeneration();
            }
        cellls++;
        energy=startEnergy;
    }

    public NormCell(){

        myNum=num;
        num++;
        movablePool.initializePool();
        cellls++;
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

    public void move(Directions d){
        switch (d){
            case DOWN ->{
                y++;
                if (  y<height && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(cells[x][y-1].secCell);
                cells[x][y-1].setSecCell(null);
                }
                else {
                    y--;
                }
            }
            case UP -> {
                y--;
                if (y>-1 &&  y<height-1 && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(cells[x][y+1].secCell);
                cells[x][y+1].setSecCell(null);}
                else{y++;
                }

            }
            case LEFT ->{
                x--;
                if (x>-1 && cells[x][y].secCell==null && cells[x][y].partCell==null && myParts.size()==0){
                cells[x][y].setSecCell(cells[x+1][y].secCell);
                cells[x+1][y].setSecCell(null);
                }
                else {x++;}
            }
            case RIGHT ->{ x++;
                if(x<width && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(cells[x-1][y].secCell);
                cells[x-1][y].setSecCell(null);
                }
                else {x--;}
             }
        }
      energy-=1;
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



  public float getLeftCell(){
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
    public float getRightCell(){
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
    public float getUptCell(){
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
    public float getDownCell(){
        if(  y<height-1&& cells[x][y+1].secCell!=null ){
            if(isRelatives( cells[x][y+1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y<height-1&& cells[x][y+1].partCell!=null && isMyPart(cells[x][y+1].partCell)){
            return 1.5f;
        } else if( y<height-1&& cells[x][y+1].partCell!=null){return 2.00f;}
        else{
        return 0.00f;}
    }
    public float getRightDownCell(){
        if(  y<height-1&& x<width-1 && cells[x+1][y+1].secCell!=null ){
            if(isRelatives( cells[x+1][y+1].secCell)){return 2.5f;}else
                return 1.00f;
        }else if(y<height-1 && x<width-1&& cells[x+1][y+1].partCell!=null && isMyPart(cells[x+1][y+1].partCell)){
            return 1.5f;
        } else if( y<height-1 && x<width-1&& cells[x+1][y+1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}
    }
    public float getRightUpCell(){
        if(  y>0&& x<width-1 && cells[x+1][y-1].secCell!=null ){
            if(isRelatives(cells[x+1][y-1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y>0 && x<width-1&& cells[x+1][y-1].partCell!=null && isMyPart(cells[x+1][y-1].partCell)){
            return 1.5f;
        } else if( y>0 && x<width-1&& cells[x+1][y-1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}

    }
    public float getLeftUpCell(){
        if(  y>0&& x>0 && cells[x-1][y-1].secCell!=null ){
            if(isRelatives( cells[x-1][y-1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y>0 &&x>0&& cells[x-1][y-1].partCell!=null && isMyPart(cells[x-1][y-1].partCell)){
            return 1.5f;
        } else if( y>0 && x>0&& cells[x-1][y-1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}
    }
    public float getLeftDownCell(){
        if(  y<height-1&& x>0 && cells[x-1][y+1].secCell!=null ){
            if(isRelatives( cells[x-1][y+1].secCell)){return 2.5f;}else
            return 1.00f;
        }else if(y<height-1 && x>0&& cells[x-1][y+1].partCell!=null && isMyPart(cells[x-1][y+1].partCell)){
            return 1.5f;
        } else if( y<height-1 && x>0&& cells[x-1][y+1].partCell!=null){return 2.00f;}
        else{
            return 0.00f;}
    }
    float getLeftDistance(){
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
 float getRightDistance(){
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
    float getUpDistance(){
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
    float getDownDistance(){
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

        for (int i = 0; i < myParts.size(); i++) {
            movablePool.evaluateFitness(this);
            cells[myParts.get(i)[0]][myParts.get(i)[1]].partCell.step(outputs[i+1]);

        }
        if(outputs[0]>0.64 && outputs[0]<0.68){
      new  Protoplast(outputs[0],x,y);
       //     System.out.println("WRYYYYY");
        }
        if (outputs[0]>0.3 && outputs[0]<0.35){
            multiply();
        }
        Random r=new Random();

        energy-=2;
        lifeTime++;
       }
    Protoplast protoplast;
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
           energy-=2;
       }movablePool.evaluateFitness(this);
            //  System.out.println(output);
     if (output>0.2 && output<0.3){eatOrganic();}
     else if(output>0.3f && output<0.35 ){
        multiply();
    }else if(output>0.5f && output<0.52f){
        move(Directions.UP);
    }else if (output>0.52f && output<0.54f){
        move(Directions.DOWN);
    }else if (output>0.54f && output<0.58f){
        move(Directions.LEFT);
    } else if (output>0.58f && output<0.6f){
        move(Directions.RIGHT);
    }else if(output>0.6 && output<0.61){
        if(!eatCell(Directions.RIGHT)){energy--;}

    }else if(output>0.61 && output<0.62){
        if(!eatCell(Directions.LEFT)){energy--;}

    }else if(output>0.62 && output<0.63){
        if(!eatCell(Directions.UP)){energy--;}

    }else if(output>0.63 && output<0.64){
        if (!eatCell(Directions.DOWN)){energy--;}

    }else if(output>0.64 && output<0.68){
        protoplast=new Protoplast(output,x,y);
}else if(output>0.68 && output<0.8){


     }
            setLastThings();
    energy--;
    lifeTime++;
    Random r=new Random();

            //Mutate on first gen
            if(r.nextInt(900)==12 && Restarts==0){
                   for(Species gena : movablePool.getSpecies()){
                       for(Genome gane : gena.getGenomes()){
                           gane.Mutate();
                       }
                   }
            }
            //
        }
    }

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
           energy+= 3;
            cells[x][y].setOrganic(cells[x][y].getOrganic()-3);
        }
}

void transferEnergy(float output){
      if (output>0.7 && output>0.72) {

      }else if(output>0.72 && output>0.74){}
}

 float isSpaceAvailable(){
       boolean b1=true;
     boolean b2=true;
     boolean b3=true;
     boolean b4=true;
       if(y>0 && cells[x][y-1].secCell==null){}else b1=false;
       if(y<height-1 && cells[x][y+1].secCell==null){}else b2=false;
       if(x<width-1 &&  cells[x+1][y].secCell==null){}else b3=false;
       if( x>0 && cells[x-1][y].secCell==null){}else b4=false;
       if(!b1 && !b2 && !b3 && !b4){
           return 0.0f;
       }else{return 1.0f;}
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

    int   neededEnergy=ENERGY_NEEDED_TO_MULTIPLY;
    void multiply(){

       if(isSpaceAvailable()==1.00f && energy>neededEnergy){
       boolean shitHappened=false;
       Random random1=new Random();
       int xxx =random1.nextInt(4);
        //   System.out.println(xxx+"-Random");
       if(xxx==0){
        if(x<width-1 && cells[x+1][y].secCell ==null && cells[x+1][y].partCell==null ){

    cells[x+1][y].setSecCell(new NormCell(movablePool,myChildNum));
energy-=neededEnergy;
multiplies++;
        }
       }else if(xxx==1){
           if(x>0 && cells[x-1][y].secCell ==null   && cells[x-1][y].partCell==null){

               cells[x-1][y].setSecCell(new NormCell(movablePool,myChildNum));
               energy-=neededEnergy;
               multiplies++;
           }
       } else if (xxx==2){
           if(y>0 && cells[x][y-1].secCell ==null && cells[x][y-1].partCell==null){

               cells[x][y-1].setSecCell(new NormCell(movablePool,myChildNum));
               energy-=neededEnergy;
               multiplies++;
           } else if(xxx==3){
               if(y<height-1 && cells[x][y+1].secCell ==null && cells[x][y+1].partCell==null ){

                   cells[x][y+1].setSecCell(new NormCell(movablePool,myChildNum));
                   energy-=neededEnergy;
                   multiplies++;
               }else{
                   shitHappened=true;
                   multiply();}
           }

       }
       }else{
          energy--;
           return;}
}

float isController(){
     if (normCellType==NormCellType.CONTROLLER)  {
         return 10.0f;
     }
     else return 0f;
}
    float lastRightDistance=0f;
    float lastLeftDistace=0f;
    float lastUpDistance=0f;
    float lastDownDistance=0f;
    int lastEnergy=0;
    float lastRightUpCell=0f;
    float lastRightDownCell=0f;
    float lastLeftUpCell=0f;
    float lastLeftDownCell=0f;
    float lastUpCell=0f;
    float lastDownCell=0f;
    float lastLeftCell=0f;
    float lastRightCell=0f;
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

    public void evaluateFitness(ArrayList<Genome> population) {
        if (normCellType == NormCellType.MOVABLE){
        ArrayList<Float> bestOutputs=new ArrayList<>();

        float outputBuffer = 0f;
        int gSuccess = 0;
        for (Genome gene : population) {
            Random r = new Random();
            byte b = (byte) r.nextInt(101);
            if (b == 45) {
                gene.Mutate();
                // System.out.println("Mutate");
            }
            int gSuccessfully = 20;
            int gEnergy = energy;
           // gene.setFitness(0);
            float[] inputs = { getEnergy()*0.01f, getUptCell(), getDownCell(), getLeftCell(), getRightCell(),getRightDownCell(),getRightUpCell(),getLeftUpCell(),getLeftDownCell(), isSpaceAvailable()*0.1f, isController(),getRightDistance(),getLeftDistance(),getUpDistance(),getDownDistance(), cells[x][y].getOrganic()/100f,lastEnergy*0.01f,lastUpCell,lastDownCell,lastLeftCell,lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,lastOrganic/100f,sunny*0.1f,myParts.size(),lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
            float[] output = gene.evaluateNetwork(inputs);
            if (output[0] > 0.7f || output[0] == 0.5f || output[0] <= 0.0f) {
             gSuccessfully -= 1000;
            }
            if (output[0]>0.2 && output[0]<0.3){
                if(cells[x][y].getOrganic()>=3){
                gEnergy+=3;
            gSuccessfully+=3;
                }else {
                    gSuccessfully-=4;
                    gEnergy--;
                }
            }
            if (output[0] > 0.3f && output[0] < 0.35) {
                //  multiply();
                if (isSpaceAvailable() == 1.0f && gEnergy > neededEnergy) {
                    gSuccessfully += 3;
                    gEnergy-=neededEnergy;
                } else {
                    gEnergy-=2;
                    gSuccessfully-=3;
                }
            } else if (output[0] > 0.5f && output[0] < 0.52f) {
                if(gEnergy>=4){
           gSuccessfully +=1;
                gEnergy--;
                }else {
                    gSuccessfully-=1;
                    gEnergy--;
                }
                //   move(Directions.UP);
            } else if (output[0] > 0.52f && output[0] < 0.54f) {
                //   move(Directions.DOWN);
                if(gEnergy>=4){
                    gSuccessfully +=1;
                    gEnergy--;
                }else {
                    gSuccessfully-=1;
                    gEnergy--;
                }
            } else if (output[0] > 0.54f && output[0] < 0.58f) {
                //  move(Directions.LEFT);
                if(gEnergy>=4){
                    gSuccessfully +=1;
                    gEnergy--;
                }else {
                    gSuccessfully-=1;
                    gEnergy--;
                }
            } else if (output[0] > 0.58f && output[0] < 0.6f) {
                // move(Directions.RIGHT);
                if(gEnergy>=4){
                    gSuccessfully +=1;
                    gEnergy--;
                }
                else {
                    gSuccessfully-=1;
                    gEnergy--;
                }
            } else if (output[0] > 0.6f && output[0] < 0.61f) {
                if (eatCellT(Directions.RIGHT)==1) {
                    gEnergy += enValue;
                } else {

                    if(eatCellT(Directions.RIGHT)==2){gSuccessfully-=3;}
                    else {
                        gSuccessfully-=2;
                        gEnergy--;
                    }
                }
                //  eatCell(Directions.RIGHT);

            } else if (output[0] > 0.61f && output[0] < 0.62f) {
                // eatCell(Directions.LEFT);
                if (eatCellT(Directions.LEFT)==1) {
                    gEnergy += enValue;
                } else if(eatCellT(Directions.LEFT)==2){gSuccessfully-=3;}
                else {
                    gSuccessfully-=2;
                    gEnergy--;
                }
            } else if (output[0] > 0.62f && output[0] < 0.63f) {
                //  eatCell(Directions.UP);
                if (eatCellT(Directions.UP)==1) {
                    gEnergy += enValue;
                }else if(eatCellT(Directions.UP)==2){gSuccessfully-=3;}
                else {
                    gSuccessfully-=2;
                    gEnergy--;
                }
            } else if (output[0] > 0.63f && output[0] < 0.64f) {
                // eatCell(Directions.DOWN);
                if (eatCellT(Directions.DOWN)==1) {
                    gEnergy += enValue;
                } else if(eatCellT(Directions.DOWN)==2){gSuccessfully-=3;}
                else {
                    gSuccessfully-=2;
                    gEnergy--;
                }
            }else if(output[0]>0.64 && output[0]<0.68){
              //  protoplast=new Protoplast(output[0],x,y);
                if(gEnergy>ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST){
            gEnergy-=ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
            gSuccessfully+= PROTOPLAST_SPAWN_REWARD;
                }
            }

            if(gEnergy>energy){
                gSuccessfully+=1;
            }
            else if (gEnergy <= 4) {
                gSuccessfully -= 20;
            }




            if (gSuccessfully > gSuccess) {
                gSuccess = gSuccessfully;
                outputBuffer = output[0];
                bestOutputs.clear();
                bestOutputs.add(output[0]);
            }else if(gSuccessfully==gSuccess){
                bestOutputs.add(output[0]);
            }
            gene.setFitness(gSuccessfully);
gene.setPoints(gSuccessfully*0.01f);
        }

        if(bestOutputs.size()!=0){
            Random rr=new Random();
            this.output=bestOutputs.get(rr.nextInt(bestOutputs.size()));
        }else{
        this.output = outputBuffer;
        }


    }else
        if(normCellType==NormCellType.CONTROLLER){
         //  System.out.println(lifeTime);

            int successfully=20;


            float[] outputBuffer = new float[myParts.size()+1];
            int gSuccess = 0;
            for (Genome gene : population) {
                gene.setOutputs(myParts.size()+1);
                Random r = new Random();
                byte b = (byte) r.nextInt(101);
                if (b == 45) {
                    gene.Mutate();
                    // System.out.println("Mutate");
                }
                int gSuccessfully = successfully;
                int gEnergy = energy;
               // gene.setFitness(0);
                float[] inputs = { getEnergy()*0.01f, getUptCell(), getDownCell(), getLeftCell(), getRightCell(),getRightDownCell(),getRightUpCell(),getLeftUpCell(),getLeftDownCell(), isSpaceAvailable()*0.1f, isController(),getRightDistance(),getLeftDistance(),getUpDistance(),getDownDistance(), cells[x][y].getOrganic()/100f,lastEnergy*0.01f,lastUpCell,lastDownCell,lastLeftCell,lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,lastOrganic/100f,sunny*0.1f,myParts.size(),lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};
                 float[]  myOutput= gene.evaluateNetwork(inputs);
                if ( myOutput[0]>0.64 && myOutput[0]<0.68 ){
                    gEnergy-=10;
                    gSuccessfully+=4;
                }else if( myOutput[0]>0.3 && myOutput[0]<0.35){
                    gEnergy-=10;
                    gSuccessfully+=3;
                }
                for (int i = 1; i < outputBuffer.length; i++) {
                    if (myOutput[i]==0.5 || myOutput[i]==0){gSuccessfully-=50;}
                       // myPartsObj.get(partName+i).step(myOutput[i]);
                    Integer[] coordinates = myParts.get(i-1);
                        if (cells[coordinates[0]][coordinates[1]].partCell instanceof Protoplast){
                            if(myOutput[i]>0.0 && myOutput[i]<0.2){
                            gEnergy+=sunny;
                                gSuccessfully+=1;
                            }else {gSuccessfully-=10;}

                        }

                }
                if(gEnergy>energy){
                    gSuccessfully+=2;
                }
                else if (gEnergy <= 2) {
                    gSuccessfully -= 20;
                }



               // if (gEnergy >= 98) {
                 //   gSuccessfully-=2;
              //  }

                if (gSuccessfully > gSuccess) {
                    gSuccess = gSuccessfully;
                    outputBuffer = myOutput;
                }
               // System.out.println(gSuccessfully+" gSucsefully");
                gene.setFitness(gSuccessfully);
            }
            this.outputs=outputBuffer;

        }
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





        public class Protoplast implements PartCell{
            Integer[] myCoords=new Integer[2];
         Color color=Color.CYAN;
         String myName;
         int energy1 =PROTOPLAST_START_ENERGY;

         void transferEnergy(){
         if(energy1>=4){
         cells[x][y].secCell.energy+=energy1/2;
         energy1-=energy1/2;
         }
         }
         byte countb=0;

         public int getEnergy() {
            return energy1;
        }

          @Override
          public void step(float output) {
            eatSunE();
            if(output>0.0 && output<0.2){
            transferEnergy();
            }else if(output>0.64 && output<0.68) {
            new Protoplast(output,xxx,yyy);
            }


            if(countb%2==0){
                energy1--;
            }
            countb++;
          }

        @Override
        public void test() {

            try {

            if( cells[getX()][getY()].secCell==null ){
              cells[xxx][yyy].partCell=null;
              cells[xxx][yyy].organic+=energy1;
              if(myParts!=null){
                 myParts.remove(myCoords);
              }
            }
            }catch (Exception e){
                e.printStackTrace();
               System.out.println("proto: "+getXx()+" "+getYy());
                System.out.println("nomCell: "+getX()+" "+getY());
            }
        }

        public Protoplast(float f,int x,int y){

            boolean done=false;
          if(f>0.64 && f<0.65){
              if (y>0 && cells[x][y-1].secCell==null && cells[x][y-1].partCell==null) {
                  myName = partName + partNum;
                  partNum++;
                  cells[x][y - 1].partCell = this;
                  myCoords[0]=x;
                  myCoords[1]=y-1;
                  myParts.add(myCoords);
                  done=true;
              }
          }  if(f>0.65 && f<0.66){
                  if (y<height-1 && cells[x][y+1].secCell==null && cells[x][y+1].partCell==null) {
                      myName=partName+partNum;
                      partNum++;
                      cells[x][y+1].partCell=this;
                      myCoords[0]=x;
                      myCoords[1]=y+1;
                      myParts.add(myCoords);
                      done=true;
                  }
              } if(f>0.66 && f<0.67){
                  if (x<width-1 && cells[x+1][y].secCell==null && cells[x+1][y].partCell==null) {
                      myName=partName+partNum;
                      partNum++;
                      cells[x+1][y].partCell=this;
                      myCoords[0]=x+1;
                      myCoords[1]=y;
                      myParts.add(myCoords);
                      done=true;
                  }
              }   if(f>0.67 && f<0.68){
                  if (x>0 && cells[x-1][y].secCell==null && cells[x-1][y].partCell==null) {
                      myName=partName+partNum;
                      partNum++;
                      cells[x-1][y].partCell=this;
                      myCoords[0]=x-1;
                      myCoords[1]=y;
                      myParts.add(myCoords);
                      done=true;
                  }
              }
          if (done){
              energy-=ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
              xxx=myCoords[0];
              yyy=myCoords[1];
          }
        }
     private    int xxx;
       private int yyy;

        void eatSunE(){
            energy1=energy1+ World.sunny;

        }


        public int getYy() {
            return yyy;
        }

        public int getXx() {
            return xxx;
        }

        public void setY(int y) {
            yyy = y;
        }

        public void setX(int x) {
            xxx = x;
        }

        @Override
        public Color getColor() {
            return color;
        }
    }



}


        interface PartCell{
    Integer[] myCoords = new Integer[2];
        void step(float output);
        void test();
        int getEnergy();
        Color getColor();
        void setY(int y);
        void setX(int x);
            int getXx();
            int getYy();
        }