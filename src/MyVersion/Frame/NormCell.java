package MyVersion.Frame;


import MyVersion.Core.Network;


import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static MyVersion.Frame.GM2_CONFIG.*;
import static MyVersion.Frame.World.*;

public class NormCell implements  Serializable {
    //*****************************************
    Network brain=new Network();
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

Color myColor=Color.green;
    public NormCell(long myParentNum){
        myNum=num;
        num++;
        cellls++;
        energy=startEnergy;
       this.myParentNum=myParentNum;
        Random r=new Random();
        if(r.nextInt(20)==1){

        }
    }

    public NormCell(int anyNum){
        myNum=num;
        num++;




            Random r=new Random();
            if(r.nextInt(20)==1){

            }
        cellls++;
        energy=startEnergy;
    }

    public NormCell(){
        myNum=num;
        num++;
        energy=startEnergy;
        cellls++;
    }

 float isRaedyToMultiply(){
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
        switch (d){
            case DOWN ->{
                y++;
                if (  y<height && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(cells[x][y-1].secCell);
                cells[x][y-1].setSecCell(null);
                }
                else {
                    y--;
                    switch (i5){
                        case 0->{
                        }
                        case 1->{
                        }
                    }
                }
            }
            case UP -> {
                y--;
                if (y>-1 &&  y<height-1 && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(cells[x][y+1].secCell);
                cells[x][y+1].setSecCell(null);}
                else{
                    y++;
                    switch (i5){
                        case 0->{
                        }
                        case 1->{
                        }
                    }
                }

            }
            case LEFT ->{
                x--;
                if (x>-1 && cells[x][y].secCell==null && cells[x][y].partCell==null && myParts.size()==0){
                cells[x][y].setSecCell(cells[x+1][y].secCell);
                cells[x+1][y].setSecCell(null);
                }
                else {
                    x++;
                    switch (i5){
                        case 0->{
                        }
                        case 1->{
                        }
                    }
                }
            }
            case RIGHT ->{ x++;
                if(x<width && cells[x][y].secCell==null && cells[x][y].partCell==null  && myParts.size()==0){
                cells[x][y].setSecCell(cells[x-1][y].secCell);
                cells[x-1][y].setSecCell(null);
                }
                else {
                    x--;
                    switch (i5){
                        case 0->{
                        }
                        case 1->{
                        }
                    }
                }
             }
        }
      energy-=1;
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
            float output=evaluateFitness();
            //  System.out.println(output);
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
        protoplast=new Protoplast(output,x,y);
}else if(output>0.68 && output<0.8){


     } else if(output>0.9f && output<1 ){
         multiply();
     }
            setLastThings();
    energy--;
    lifeTime++;
    Random r=new Random();

            //Mutate on first gen

            //
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


    void multiply(){
        //TODO REMAKE
       if(isSpaceAvailable()==1.00f && energy>ENERGY_NEEDED_TO_MULTIPLY){
       boolean shitHappened=false;
       Random random1=new Random();
       int xxx =random1.nextInt(4);
        //   System.out.println(xxx+"-Random");
       if(xxx==0){
        if(x<width-1 && cells[x+1][y].secCell ==null && cells[x+1][y].partCell==null ){

    cells[x+1][y].setSecCell(new NormCell(myChildNum));
energy-=ENERGY_NEEDED_TO_MULTIPLY;
multiplies++;
        }
       }else if(xxx==1){
           if(x>0 && cells[x-1][y].secCell ==null   && cells[x-1][y].partCell==null){

               cells[x-1][y].setSecCell(new NormCell(myChildNum));
               energy-=ENERGY_NEEDED_TO_MULTIPLY;
               multiplies++;
           }
       } else if (xxx==2){
           if(y>0 && cells[x][y-1].secCell ==null && cells[x][y-1].partCell==null){

               cells[x][y-1].setSecCell(new NormCell(myChildNum));
               energy-=ENERGY_NEEDED_TO_MULTIPLY;
               multiplies++;
           } else if(xxx==3){
               if(y<height-1 && cells[x][y+1].secCell ==null && cells[x][y+1].partCell==null ){

                   cells[x][y+1].setSecCell(new NormCell(myChildNum));
                   energy-=ENERGY_NEEDED_TO_MULTIPLY;
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



//*********************************************
int gSuccess ;
    public void setGSuccess(int gSuccess) {
        synchronized (this){
        this.gSuccess = gSuccess;
        }
    }

    public int getGSuccess() {
        synchronized (this){
        return gSuccess;
        }
    }

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
    int threadsFinished;

    public void setThreadsFinished(int threadsFinished) {
        synchronized (World.class){
        this.threadsFinished = threadsFinished;
        }
    }

    public int getThreadsFinished() {
        synchronized (World.class){
        return threadsFinished;
        }
    }

    //************************************************
    public float evaluateFitness() {
        float[] inputs = {isRaedyToMultiply() ,getEnergy(),cells[x][y].getOrganic(), getUptCell(), getDownCell(), getLeftCell(), getRightCell(),getRightDownCell(),getRightUpCell(),getLeftUpCell(),getLeftDownCell(), isSpaceAvailable(), isController(),getRightDistance(),getLeftDistance(),getUpDistance(),getDownDistance(),lastEnergy,lastUpCell,lastDownCell,lastLeftCell,lastRightCell,lastRightDownCell,lastRightUpCell,lastLeftDownCell,lastLeftUpCell,lastOrganic,sunny,myParts.size(),lastSize,lastRightDistance,lastLeftDistace,lastUpDistance,lastDownDistance};

       return brain.evaluteFitness(inputs);
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


///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
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