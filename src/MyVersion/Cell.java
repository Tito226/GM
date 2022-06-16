package MyVersion;

import java.awt.*;
import java.util.Random;

import static MyVersion.GM2_CONFIG.CELL_START_ORGANIC;
import static MyVersion.GM2_CONFIG.NORM_CELL_START_ENERGY;
import static MyVersion.World.*;

public  class Cell {
    boolean isChanged=true;
public static int startEnergy=CELL_START_ORGANIC;
int[] color;
    Color myColor ;
    int energy=1;
NormCell secCell;
PartCell partCell;
   private int x;
    private int y;
    public int organic=30;
private boolean nothing=true;
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
public void setSecCell(NormCell secCell){
      this.secCell=secCell;
}
    public void setSecCell(int i){
        switch(i){
            case 1->{
        this.secCell=topLifeTimeCell;
                Random r=new Random();
                if(r.nextInt(20)==1){
                    secCell.movablePool.breedNewGeneration();
                }
            }
            case 2 ->{
                this.secCell=topMultipliesCell;
                Random r=new Random();
                if(r.nextInt(20)==1){
                    secCell.movablePool.breedNewGeneration();
                }
            }
        }
        secCell.setEnergy(NORM_CELL_START_ENERGY);
        secCell.myParts.clear();
        secCell.lifeTime=0;
        secCell.multiplies=0;
    }
    public void setPartCell(PartCell partCell) {
        this.partCell = partCell;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    void step(){
if( secCell!=null){
    secCell.step();
}
else{} //System.out.println(" ");
    }
   boolean getSecCell(){
        if(secCell!=null){
            return true;
        }else return false;
   }
   boolean getPartCell(){
        if(partCell!=null){
            return true;
        }else return false;
    }
    boolean change=false;
   boolean lastNCell=false;
    boolean lastPCell=false;
    int lastOrganic=0;
boolean isChanged(){
        if (lastPCell!=getPartCell() || lastNCell!=getSecCell() || lastOrganic!=organic || lastRestarts!=Restarts){
            return true;
        }
        if(change){
            change=false;
            return true;
        }
        else{
            return false;}
}
  void testCell(){
    lastNCell=getSecCell();
    lastPCell=getPartCell();
    lastOrganic=organic;
        try{
        if(organic<255 && organic>0){
        color=new int[3];

          color[0]=255-organic;
          color[1]=255-organic;
            color[2]=255-organic;
        myColor=new Color(color[0],color[1],color[2]);
         }
        }catch (IllegalArgumentException e){
            System.out.println(color[0]);
            System.out.println(color[1]);
            System.out.println(color[2]);
        }
        if(secCell!=null  && secCell.getEnergy()<=0  ){
            organic+=secCell.getEnergy();
            secCell=null;
            cellls--;
        }
      if(secCell!=null  && secCell.getEnergy()>=10000 ){
          System.out.println("Cell with 10000 died");
          organic+=secCell.getEnergy();
          secCell=null;
          cellls--;
      }
      if(partCell!=null){
          partCell.test();
      }
  }

public Color getColor(){
        return myColor;
}


    public void setOrganic(int organic) {
        this.organic = organic;
    }

    public int getOrganic() {
        return organic;
    }
}
