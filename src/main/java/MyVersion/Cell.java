package MyVersion;

import java.awt.*;

import static MyVersion.World.cellls;

public  class Cell {
    boolean isChanged=false;

    Color myColor = new Color(255, 255, 255);
    int energy=1;
NormCell secCell;
PartCell partCell;
   private int x;
    private int y;
    private int organic=3;
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

    public boolean isNothing() {
        return nothing;
    }
    void step(){
if( secCell!=null){
    secCell.step();
}
else{} //System.out.println(" ");
    }

  void testCell(){
        if(secCell!=null  && secCell.getEnergy()<=0  ){
            secCell=null;
            organic+=3;
            cellls--;
        }
      if(secCell!=null  && secCell.getEnergy()>=10000 ){
          secCell=null;
          organic+=3;
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
