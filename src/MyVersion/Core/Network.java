package MyVersion.Core;

import java.util.ArrayList;
import java.util.Random;

import static MyVersion.Core.Core_Config.*;

public class Network {
//сколько нейронов будет в скрытом слое - подбором. Если слишком много,  плохо - сеть начинает запоминать, зубрить примеры (нужно больше примеров чтобы этого избежать),
// если мало нейронов, то она не достаточно гибкая, не сможет ухватить закономерность
//Лучше мало нейронов но больше слоев чем больше нейронов но 1 слой
    ArrayList<ArrayList<Dot>> dotsArr =new ArrayList<>();
    public Network(int iii){
        dotsArr.add(new ArrayList<>());//inputs (0)

        for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
            dotsArr.add(new ArrayList<>());

        }

        for (int i = 0; i < INPUTS+BIAS; i++) {//adding input Dots to array,if bias,add dias Dot and add notes to it
            dotsArr.get(0).add(new Dot(Dot_Type.INPUT));
            if(BIAS==1){
                dotsArr.get(0).add(new Dot(Dot_Type.BIAS_TYPE));
                for (int j = 0; j < dotsArr.get(1).size() ; j++) {
                    dotsArr.get(0).get(dotsArr.size()-1).addNode(dotsArr.get(1).get(j));
                }
            }
        }


        dotsArr.add(new ArrayList<>());//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)


        for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY ; i++) {
            for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY; j++) {
                dotsArr.get(i).add(new Dot(Dot_Type.HIDDEN));
            }

            if (BIAS==1) {
                dotsArr.get(i).add(new Dot(Dot_Type.BIAS_TYPE));
                for (int j = 0; j < dotsArr.get(j + 1).size(); j++) {
                    dotsArr.get(i).get(dotsArr.size() - 1).addNode(dotsArr.get(i+1).get(j));
                }
            }

        }
        for (int i = 0; i < OUTPUTS; i++) {
            dotsArr.get(dotsArr.size()-1).add(new Dot(Dot_Type.OUTPUT));
        }
        //

        //ADDING NOTES TO DOTS
        for (int i = 0; i < HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY+1; i++) {
            for (int j = 0; j < dotsArr.get(i).size(); j++) {
                for (int k = 0; k < dotsArr.get(i+1).size(); k++) {
                    dotsArr.get(i).get(j).addNode(dotsArr.get(i+1).get(k));
                }
            }

        }

        //
    }







    //it dont work now
    public Network(){//it dont work now
        dotsArr.add(new ArrayList<>());//inputs (0)

        for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
          dotsArr.add(new ArrayList<>());

        }
        for (int i = 0; i < INPUTS+BIAS; i++) {
            dotsArr.get(0).add(new Dot(Dot_Type.INPUT));
            if(BIAS==1){
                dotsArr.get(0).add(new Dot(Dot_Type.BIAS_TYPE));
                for (int j = 0; j < dotsArr.get(1).size() ; j++) {
                    dotsArr.get(0).get(dotsArr.size()-1).addNode(dotsArr.get(1).get(j));
                }
            }
        }


        dotsArr.add(new ArrayList<>());//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)


        for (int i = 1; i < HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY +BIAS; i++) {
            for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY; j++) {
            dotsArr.get(i).add(new Dot(Dot_Type.HIDDEN));
            }

            if (BIAS==1) {
                dotsArr.get(i).add(new Dot(Dot_Type.BIAS_TYPE));
                for (int j = 0; j < dotsArr.get(j + 1).size(); j++) {
                    dotsArr.get(i).get(dotsArr.size() - 1).addNode(dotsArr.get(i+1).get(j));
                }
            }

        }
        for (int i = 0; i < OUTPUTS; i++) {
          dotsArr.get(dotsArr.size()-1).add(new Dot(Dot_Type.OUTPUT));
        }
        //
        Random r=new Random();
        dotsArr.get(0).get(0).addNode( dotsArr.get(1).get(0));
      //  dotsArr.get(0).get(0).nodesFromMe.get(0).weight =0.6f;
        dotsArr.get(0).get(1).addNode(dotsArr.get(1).get(0));
      //  dotsArr.get(0).get(1).nodesFromMe.get(0).weight =0.8f;
        for (int i = 1; i < HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY+1; i++) {
            dotsArr.get(i).get(0).addNode(dotsArr.get(i+1).get(0));
        }
        //
    }

    public ArrayList<ArrayList<Dot>> getDotsArr() {
        return dotsArr;
    }
    ArrayList< Float> outputs;
  public float evaluteFitness(Float[] inputs,boolean forTeaching){

      //Dots value , error and weightsDelta clears in next method call
       if(!forTeaching){
          for(ArrayList<Dot> dotArr: dotsArr){
              for(Dot dot:dotArr){
                  dot.clear();
              }
          }
       }

        outputs=new ArrayList<>();
        //Set inputs
        for (int i = 0; i < inputs.length; i++) {
            dotsArr.get(0).get(i).setValue(inputs[i]);
            dotsArr.get(0).get(i).evalute();
        }
        //evalute hidden layer
      for (int i = 1; i <HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY+1; i++) {
          for (int j = 0; j < dotsArr.get(i).size() ; j++) {
           dotsArr.get(i).get(j).evalute();
          }
      }
      //Getting outputs
      for (int i = 0; i < dotsArr.get(dotsArr.size()-1).size(); i++) {
          //вызов метода точки(возможно повторный)
          dotsArr.get(dotsArr.size()-1).get(i).evalute();
          //
         outputs.add( dotsArr.get(dotsArr.size()-1).get(i).getOutpup());

      }
      if(!forTeaching){
      for(ArrayList<Dot> dotArr: dotsArr){
          for(Dot dot:dotArr){
              dot.clear();
          }
      }
      }
return outputs.get(0);
   }


}
