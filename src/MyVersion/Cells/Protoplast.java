package MyVersion.Cells;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
import static MyVersion.Frame.GM2_CONFIG.PROTOPLAST_START_ENERGY;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.width;

import java.awt.Color;

import MyVersion.Frame.World;

///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
        public class Protoplast implements PartCell{
			private NormCell normCell;
			Integer[] myCoords=new Integer[2];
         Color color=Color.CYAN;
         String myName;
         int energy1 =PROTOPLAST_START_ENERGY;

         void transferEnergy(){
         if(energy1>=4){
         cells[this.normCell.getX()][this.normCell.getY()].secCell.energy+=energy1/2;
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
            new Protoplast(this.normCell, output,xxx,yyy);
            }


            if(countb%2==0){
                energy1--;
            }
            countb++;
          }

        @Override
        public void test() {

            try {

            if( cells[this.normCell.getX()][this.normCell.getY()].secCell==null ){
              cells[xxx][yyy].partCell=null;
              cells[xxx][yyy].organic+=energy1;
              if(this.normCell.myParts!=null){
                 this.normCell.myParts.remove(myCoords);
              }
            }
            }catch (Exception e){
                e.printStackTrace();
               System.out.println("proto: "+getXx()+" "+getYy());
                System.out.println("nomCell: "+this.normCell.getX()+" "+this.normCell.getY());
            }
        }

        public Protoplast(NormCell normCell, double nOutput,int x,int y){

            this.normCell = normCell;
			boolean done=false;
          if(nOutput>0.64 && nOutput<0.65){
              if (y>0 && cells[x][y-1].secCell==null && cells[x][y-1].partCell==null) {
                  myName = this.normCell.partName + this.normCell.partNum;
                  this.normCell.partNum++;
                  cells[x][y - 1].partCell = this;
                  myCoords[0]=x;
                  myCoords[1]=y-1;
                  this.normCell.myParts.add(myCoords);
                  done=true;
              }
          }  if(nOutput>0.65 && nOutput<0.66){
                  if (y<height-1 && cells[x][y+1].secCell==null && cells[x][y+1].partCell==null) {
                      myName=this.normCell.partName+this.normCell.partNum;
                      this.normCell.partNum++;
                      cells[x][y+1].partCell=this;
                      myCoords[0]=x;
                      myCoords[1]=y+1;
                      this.normCell.myParts.add(myCoords);
                      done=true;
                  }
              } if(nOutput>0.66 && nOutput<0.67){
                  if (x<width-1 && cells[x+1][y].secCell==null && cells[x+1][y].partCell==null) {
                      myName=this.normCell.partName+this.normCell.partNum;
                      this.normCell.partNum++;
                      cells[x+1][y].partCell=this;
                      myCoords[0]=x+1;
                      myCoords[1]=y;
                      this.normCell.myParts.add(myCoords);
                      done=true;
                  }
              }   if(nOutput>0.67 && nOutput<0.68){
                  if (x>0 && cells[x-1][y].secCell==null && cells[x-1][y].partCell==null) {
                      myName=this.normCell.partName+this.normCell.partNum;
                      this.normCell.partNum++;
                      cells[x-1][y].partCell=this;
                      myCoords[0]=x-1;
                      myCoords[1]=y;
                      this.normCell.myParts.add(myCoords);
                      done=true;
                  }
              }
          if (done){
              this.normCell.energy-=ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST;
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