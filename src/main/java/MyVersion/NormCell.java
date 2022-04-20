package MyVersion;


import MyVersion.NEAT.Environment;
import MyVersion.NEAT.Genome;
import MyVersion.NEAT.Pool;

// (x-a)/(b-a)*(B-A)+A     формула преобразования из одного диапазона в другой, из диапазона [a,b] в [A,B], х число
import java.awt.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static MyVersion.World.*;

public class NormCell implements Environment, Serializable {
  // final int MAX_ENERGY=100;
    byte counter=0;
    int lifeTime=0;
    int partNum=0;
HashMap<String, Integer[]> myParts=new HashMap<>();
    HashMap<String, PartCell> myPartsObj=new HashMap<>();
float output=0;
int multiplies=0;
    static long num = 0l;
    private long myNum;
    Cell[][] cells=getCells();
    int stepN=0;
    private int energy=90;
    private int x;
    private int y;
NormCellType normCellType=NormCellType.MOVABLE;
    public int getX() {
        return x;
    }
    Random random=new Random();
   // int[][] comands = new int[64][2];//[commands][parameters]
public Pool pool=new Pool();


    public NormCell(Pool dadPool){
        myNum=num;
        num++;
        pool.initializePool(dadPool);
        cellls++;
        energy=39;
    }

    public NormCell(){

        myNum=num;
        num++;
pool.initializePool();
cellls++;

    }

boolean isMyPart(PartCell partCell){
   if (myPartsObj.containsValue(partCell)){
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
                if (  y<height && cells[x][y].secCell==null && (cells[x][y].partCell==null || isMyPart(cells[x][y].partCell)) && myParts.size()==0){
                cells[x][y].setSecCell(cells[x][y-1].secCell);
                cells[x][y-1].setSecCell(null);
                }
                else {
                    y--;
                stepN++;}
            }
            case UP -> {
                y--;
                if (y>-1 &&  y<height-1 && cells[x][y].secCell==null && (cells[x][y].partCell==null || isMyPart(cells[x][y].partCell)) && myParts.size()==0){
                cells[x][y].setSecCell(cells[x][y+1].secCell);
                cells[x][y+1].setSecCell(null);}
                else{y++;
                stepN++;}

            }
            case LEFT ->{
                x--;
                if (x>-1 && cells[x][y].secCell==null && (cells[x][y].partCell==null || isMyPart(cells[x][y].partCell)) && myParts.size()==0){
                cells[x][y].setSecCell(cells[x+1][y].secCell);
                cells[x+1][y].setSecCell(null);
                }
                else {x++;}
            }
            case RIGHT ->{ x++;
                if(x<width && cells[x][y].secCell==null && (cells[x][y].partCell==null || isMyPart(cells[x][y].partCell)) && myParts.size()==0){
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
           return 1.00f;
       }
       return 0.00f;
}
    public float getRightCell(){
        if( x<width-1 &&  cells[x+1][y].secCell!=null  ){
            return 1.00f;
        }
        return 0.00f;
    }
    public float getUptCell(){
        if( y>0  && cells[x][y-1].secCell!=null  ){
            return 1.00f;
        }
        return 0.00f;
    }
    public float getDownCell(){
        if(  y<height-1&& cells[x][y+1].secCell!=null ){
            return 1.00f;
        }
        return 0.00f;
    }

    void step1(){
        for (int i = 0; i < myPartsObj.size(); i++) {
            myPartsObj.get("protoplast"+i).step();
            System.out.println(energy);
        }
       }
    Protoplast protoplast;
    void step(){
        if (normCellType==NormCellType.CONTROLLER){
            step1();

        }
        if(normCellType!=NormCellType.CONTROLLER){
        if(myParts.size()>0){
            normCellType=NormCellType.CONTROLLER;
        }

       if(isSpaceAvailable()==0){
           energy-=2;
       }
    pool.evaluateFitness(this);
    for (int i = 0; i < myPartsObj.size(); i++) {
        myPartsObj.get("protoplast"+i).step();
    }
     if (output>0.2 && output<0.3){eatOrganic();}else
    if(output>0.3f && output<0.35 ){
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

    }else if(output>0.64 && output<0.7){
        protoplast=new Protoplast(output,x,y);

}
    else{energy--;}

    energy--;
    lifeTime++;
    if(counter%2==0){
    pool.breedNewGeneration();
    counter=0;
    }
counter++;
        }
    }
    public Color getColor(){
        return Color.GREEN;    //TODO set Changeable cell color
    }
    void eatSunE(){
        energy=energy+ World.sunny;
        System.out.println("Cell:"+x+" "+y+" have:"+energy+" energy" );
    }

    public int getEnergy() {
        return energy;
    }

   // public int[][] getComands() {
   //     return comands;
  //  }

   // public void setComands(int[][] comands) {
   //     this.comands = comands;
   // }

    void eatOrganic(){
        if(cells[x][y].getOrganic()!=0  && cells[x][y].getOrganic()>=3){
           energy+= 3;
            cells[x][y].setOrganic(cells[x][y].getOrganic()-3);
        }
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
    int enValue=30;
    public boolean eatCell(Directions dirs){

        boolean done = false;
        switch (dirs){

            case UP -> {
                if( y>0 && cells[x][y-1].secCell!=null ){
                cells[x][y-1].secCell=cells[x][y].secCell;
                    cells[x][y-1].secCell.energy+=enValue;
                    cells[x][y].secCell=null;
                    done= true;
                }else done= false;
            }

            case DOWN -> {
                if(y<height-1 && cells[x][y+1].secCell!=null){
                    cells[x][y+1].secCell=cells[x][y].secCell;
                    cells[x][y+1].secCell.energy+=enValue;
                    cells[x][y].secCell=null;
                    done= true;
                }else done= false;
            }

            case RIGHT -> {
                if( x<width-1 &&  cells[x+1][y].secCell!=null ){
                    cells[x+1][y].secCell=cells[x][y].secCell;
                    cells[x+1][y].secCell.energy+=enValue;
                    cells[x][y].secCell=null;
                    done= true;
                }else done = false;
            }

            case LEFT -> {
                if(  x>0 && cells[x-1][y].secCell!=null){
                    cells[x-1][y].secCell=cells[x][y].secCell;
                    cells[x-1][y].secCell.energy+=enValue;
                    cells[x][y].secCell=null;
                    done= true;
                }else done= false;
            }

            }

    if(done){cellls--;}
      //  System.out.println(done);
        return done;
    }

    void multiply(){
       if(isSpaceAvailable()==1.00f && energy>40){
       boolean shitHappened=false;
       Random random1=new Random();
       int xxx =random1.nextInt(4);
        //   System.out.println(xxx+"-Random");
       if(xxx==0){
        if(x<width-1 && cells[x+1][y].secCell ==null  ){

    cells[x+1][y].setSecCell(new NormCell(pool));
energy-=10;
multiplies++;
        }
       }else if(xxx==1){
           if(x>0 && cells[x-1][y].secCell ==null  ){

               cells[x-1][y].setSecCell(new NormCell(pool));
               energy-=10;
               multiplies++;
           }
       } else if (xxx==2){
           if(y>0 && cells[x][y-1].secCell ==null  ){

               cells[x][y-1].setSecCell(new NormCell(pool));
               energy-=10;
               multiplies++;
           } else if(xxx==3){
               if(y<height-1 && cells[x][y+1].secCell ==null  ){

                   cells[x][y+1].setSecCell(new NormCell(pool));
                   energy-=40;
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

    public void evaluateFitness(ArrayList<Genome> population) {
        int successfully;
       if(multiplies!=0){
            successfully=energy+multiplies*2+lifeTime;
       }else{
        successfully=energy+lifeTime;}
        float outputBuffer=0f;
       int gSuccess=0;
        for (Genome gene : population) {
        Random r=new Random();
        byte b= (byte) r.nextInt(101);
        if ( b==45){gene.Mutate();
       // System.out.println("Mutate");
        }
        int gSuccessfully=successfully;
        int gEnergy=energy;
        gene.setFitness(0);
        float[] inputs={getX(),getY(),getEnergy(),getUptCell(),getDownCell(),getLeftCell(),getRightCell(),isSpaceAvailable()};
        float output[] = gene.evaluateNetwork(inputs);
        if(output[0]>0.7f || output[0]==0.5f || output[0]==0.0f){gSuccessfully-=10;}
            if(energy<=3){
                gSuccessfully-=4;
            }
            if(energy>=98){
                gSuccessfully--;
            }
            if(output[0]>0.3f && output[0]<0.35 ){
              //  multiply();
                if(isSpaceAvailable()==1.0f && gEnergy>=40){
                    gSuccessfully+=4;
                }else{gEnergy--;}
            }else if(output[0]>0.5f && output[0]<0.52f){
               gEnergy++;
             //   move(Directions.UP);
            }else if (output[0]>0.52f && output[0]<0.54f){
             //   move(Directions.DOWN);
                gEnergy++;
            }else if (output[0]>0.54f && output[0]<0.58f){
              //  move(Directions.LEFT);
                gEnergy++;
            } else if (output[0]>0.58f && output[0]<0.6f){
               // move(Directions.RIGHT);
                gEnergy++;
            }else if(output[0]>0.6f && output[0]<0.61f){
                if(eatCellT(Directions.RIGHT)){gEnergy+=enValue;}else{gEnergy--;}
              //  eatCell(Directions.RIGHT);

            }else if(output[0]>0.61f && output[0]<0.62f){
               // eatCell(Directions.LEFT);
                if(eatCellT(Directions.LEFT)){gEnergy+=enValue;}else{gEnergy--;}
            }else if(output[0]>0.62f && output[0]<0.63f){
              //  eatCell(Directions.UP);
                if(eatCellT(Directions.UP)){gEnergy+=enValue;}else{gEnergy--;}
            }else if(output[0]>0.63f && output[0]<0.64f){
               // eatCell(Directions.DOWN);
                if(eatCellT(Directions.DOWN)){gEnergy+=enValue;}else{gEnergy--;}
            }


         gSuccessfully=gSuccessfully-energy+gEnergy;
            if(gSuccessfully>gSuccess){
            gSuccess=gSuccessfully;
           outputBuffer= output[0];
            }
        gene.setFitness(gSuccessfully);
        }
        this.output=outputBuffer;
    }

     public boolean eatCellT(Directions dirs){
        boolean done = false;
        switch (dirs){
            case UP -> {
                if( y>0 && cells[x][y-1].secCell!=null ){
                    done= true;
                }else done= false;
            }
            case DOWN -> {
                if(y<height-1 && cells[x][y+1].secCell!=null){
                    done= true;
                }else done= false;
            }
            case RIGHT -> {
                if( x<width-1 &&  cells[x+1][y].secCell!=null ){
                    done= true;
                }else done = false;
            }
            case LEFT -> {
                if(  x>0 && cells[x-1][y].secCell!=null){
                    done= true;
                }else done= false;
            }
        }
        if(done){cellls--;}
        return done;
    }
        public class Protoplast implements PartCell{
        Color color=Color.CYAN;
        String myName;
        int energy1 =1;

        void transferEnergy(){
        if(energy1>1){
        cells[x][y].secCell.energy+=energy1/2;
        energy1-=energy1/2;
    }
}

        @Override
        public int getEnergy() {
            return energy1;
        }

        @Override
        public void step() {
            eatSunE();
            transferEnergy();
        }

        @Override
        public void test() {

            try {
            if(getY()<0){cells[xxx][yyy].partCell=null;}
            else if( getX()<width && getY()<height && cells[getX()][getY()].secCell==null ){
              cells[xxx][yyy].partCell=null;
            }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("proto: "+getXx()+" "+getYy());
                System.out.println("nomCell: "+getX()+" "+getY());
            }
        }

        public Protoplast(){

        }
        public Protoplast(float f,int x,int y){
          if(f>0.64 && f<0.65){
              if (y>0 && cells[x][y-1].secCell==null && cells[x][y-1].partCell==null) {
                  myName = "protoplast" + partNum;
                  partNum++;
                  Protoplast protoplast = new Protoplast();
                  cells[x][y - 1].partCell = protoplast;
                  myParts.put(myName, new Integer[]{x, y - 1});
                  myPartsObj.put(myName, protoplast);
                  //        System.out.println("proto");
              }
          }  if(f>0.65 && f<0.66){
                  if (y<height-1 && cells[x][y+1].secCell==null && cells[x][y+1].partCell==null) {
                      myName="protoplast"+partNum;
                      partNum++;
                      Protoplast protoplast=new Protoplast();
                      cells[x][y+1].partCell=protoplast;

                      myParts.put(myName,new Integer[]{x,y+1});
                      myPartsObj.put(myName,protoplast);
                  }
              } if(f>0.66 && f<0.67){
                  if (x<width-1 && cells[x+1][y].secCell==null && cells[x+1][y].partCell==null) {
                      myName="protoplast"+partNum;
                      partNum++;
                      Protoplast protoplast=new Protoplast();
                      cells[x+1][y].partCell=protoplast;
                   //   System.out.println("proto");
                      myParts.put(myName,new Integer[]{x+1,y});
                      myPartsObj.put(myName,protoplast);
                  }
              }   if(f>0.67 && f<0.68){
                  if (x>0 && cells[x-1][y].secCell==null && cells[x-1][y].partCell==null) {
                      myName="protoplast"+partNum;
                      partNum++;
                      Protoplast protoplast=new Protoplast();
                      cells[x-1][y].partCell=protoplast;

                      myParts.put(myName,new Integer[]{x-1,y});
                      myPartsObj.put(myName,protoplast);
                  }
              }

          //  System.out.println(f);
        }
     private    int xxx;
       private int yyy;

        void eatSunE(){
            energy1=energy1+ World.sunny;
            System.out.println("Cell:"+x+" "+y+" have:"+energy1+" energy" );
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
        void step();
        void test();
        int getEnergy();
        Color getColor();
        void setY(int y);
        void setX(int x);
    }