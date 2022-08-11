package MyVersion.Core;

import java.util.ArrayList;
import java.util.Random;

import static MyVersion.Core.Core_Config.*;

public class Network {

    ArrayList<ArrayList<Dot>> dots =new ArrayList<>();
    public Network(){
        dots.add(new ArrayList<>());//inputs (0)

        for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
          dots.add(new ArrayList<>());

        }
        for (int i = 0; i < INPUTS+BIAS; i++) {
            dots.get(0).add(new Dot(Dot_Type.INPUT));
            if(BIAS==1){
                dots.get(0).add(new Dot(Dot_Type.BIAS_TYPE));
                for (int j = 0; j <dots.get(1).size() ; j++) {
                    dots.get(0).get(dots.size()-1).addNode(dots.get(1).get(j));
                }
            }
        }


        dots.add(new ArrayList<>());//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)


        for (int i = 1; i < HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY +1; i++) {
            for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY; j++) {
            dots.get(i).add(new Dot(Dot_Type.HIDDEN));
            }
            dots.get(i).add(new Dot(Dot_Type.BIAS_TYPE));
            if (BIAS==0) {
                for (int j = 0; j < dots.get(j + 1).size(); j++) {
                    dots.get(i).get(dots.size() - 1).addNode(dots.get(i+1).get(j));
                }
            }

        }
        for (int i = 0; i < OUTPUTS; i++) {
          dots.get(dots.size()-1).add(new Dot(Dot_Type.OUTPUT));
        }
        //
        Random r=new Random();
        dots.get(0).get(0).addNode( dots.get(1).get(0));
        dots.get(0).get(0).nodesFromMe.get(0).value1=0.6f;
        dots.get(0).get(1).addNode(dots.get(1).get(0));
        dots.get(0).get(1).nodesFromMe.get(0).value1=0.8f;
        for (int i = 1; i < HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY+1; i++) {
            dots.get(i).get(0).addNode(dots.get(i+1).get(0));
        }
        //
    }

    public ArrayList<ArrayList<Dot>> getDots() {
        return dots;
    }
    ArrayList< Float> outputs;
  public float evaluteFitness(float[] inputs){
     outputs=new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            dots.get(0).get(i).setValue(inputs[i]);
            dots.get(0).get(i).evalute();
        }
      for (int i = 1; i <HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY+1; i++) {
          for (int j = 0; j <dots.get(i).size() ; j++) {
           dots.get(i).get(j).evalute();
          }
      }
      for (int i = 0; i < dots.get(dots.size()-1).size(); i++) {
         outputs.add( dots.get(dots.size()-1).get(i).getOutpup());
          dots.get(dots.size()-1).get(i).clear();
      }
return outputs.get(0);
   }


}
