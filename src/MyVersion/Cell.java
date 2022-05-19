package MyVersion;

import java.awt.*;

import static MyVersion.World.cellls;

public  class Cell {
    boolean isChanged=true;
public static int startEnergy=20;
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

  void testCell(){
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
            secCell.myPartsObj=null;
            secCell.myParts=null;
            secCell=null;

            cellls--;
        }
      if(secCell!=null  && secCell.getEnergy()>=10000 ){
          System.out.println("Cell with 10000 died");
          organic+=secCell.getEnergy();
          secCell.myPartsObj=null;
          secCell.myParts=null;
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
