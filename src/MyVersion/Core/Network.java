package MyVersion.Core;

import java.util.ArrayList;

import static MyVersion.Core.Core_Config.*;

public class Network {

    ArrayList<ArrayList<Dot>> dots =new ArrayList<>();
    public Network(){
        dots.add(new ArrayList<>());//inputs (0)

        for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
          dots.add(new ArrayList<>());
        }

        dots.add(new ArrayList<>());//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)
        for (int i = 0; i < INPUTS; i++) {
            dots.get(0).add(new Dot(Dot_Type.INPUT));
        }

        for (int i = 1; i < HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY +1; i++) {
            for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY; j++) {
            dots.get(i).add(new Dot(Dot_Type.HIDDEN));
            }
        }
        for (int i = 0; i < OUTPUTS; i++) {
          dots.get(dots.size()-1).add(new Dot(Dot_Type.OUTPUT));
        }
        //
        dots.get(0).get(0).addNode( dots.get(1).get(0));
        dots.get(1).get(0).addNode(dots.get(2).get(0));
        //
    }

    public ArrayList<ArrayList<Dot>> getDots() {
        return dots;
    }

  public   void evaluteFitness(int[] inputs){
        for (int i = 0; i < inputs.length-1; i++) {
            dots.get(0).get(i).setValue(inputs[i]);
            dots.get(0).get(i).evalute();
        }

   }


}
