package MyVersion;

import java.util.ArrayList;

public class AvailableMethods {
   // ArrayList<Integer> allKeys= new ArrayList<>();
    int[] allKeys=new int[7];
    public void call(int key,NormCell normCell){
        for (int i = 0; i < allKeys.length; i++) {
            int j=allKeys[i];
            switch (j){
                case 0 ->{normCell.move(Directions.UP);}
                case 1 ->{normCell.move(Directions.DOWN);}
                case 2 ->{normCell.move(Directions.LEFT);}
                case 3 ->{normCell.move(Directions.RIGHT);}
                case 4 ->{normCell.eatSunE();}
                case 5 ->{normCell.eatOrganic();}
                case 6 ->{normCell.multiply();}
            }
        }

    }
    public AvailableMethods(){

    }
}
