package MyVersion.Core;

import java.util.ArrayList;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static MyVersion.Core.Core_Config.*;
/*сколько нейронов будет в скрытом слое - подбором. Если слишком много,  плохо - сеть начинает запоминать, зубрить примеры (нужно больше примеров чтобы этого избежать),
  если мало нейронов, то она не достаточно гибкая, не сможет ухватить закономерность
  Лучше мало нейронов но больше слоев чем больше нейронов но 1 слой*/

public class Network implements Serializable {
	public ActivationFunctions myFunc;
	
	private static final long serialVersionUID = 2L;
    public ArrayList<ArrayList<Dot>> dotsArr =new ArrayList<>();
    public Network() {//создает пустую нейросеть(массив точек пустой) для клонирования 
    	
   	}
    
    public Network(int iii){
    	myFunc=func;
    	dotsArr.add(new ArrayList<>());//inputs (0)

    	for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
    		dotsArr.add(new ArrayList<>());

    	}

       for (int i = 0; i < INPUTS; i++) {//adding input Dots to array,if bias,add dias Dot and add notes to it
           dotsArr.get(0).add(new Dot(Dot_Type.INPUT,myFunc));
       }
       
       if(BIAS==1){
           dotsArr.get(0).add(new Dot(Dot_Type.BIAS_TYPE,myFunc));
           for (int j = 0; j < dotsArr.get(1).size() ; j++) {
               dotsArr.get(0).get(dotsArr.size()-1).addNode(dotsArr.get(1).get(j));
           }
       }

       dotsArr.add(new ArrayList<>());//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)


       for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY ; i++) {
           for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY; j++) {
               dotsArr.get(i).add(new Dot(Dot_Type.HIDDEN,myFunc));
           }

           if (BIAS==1) {
               dotsArr.get(i).add(new Dot(Dot_Type.BIAS_TYPE,myFunc));
               for (int j = 0; j < dotsArr.get(i + 1).size(); j++) {
                   dotsArr.get(i).get(dotsArr.get(i).size() - 1).addNode(dotsArr.get(i+1).get(j));
               }
           }

        }
        for (int i = 0; i < OUTPUTS; i++) {
            dotsArr.get(dotsArr.size()-1).add(new Dot(Dot_Type.OUTPUT,myFunc));
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


    public ArrayList<ArrayList<Dot>> getDotsArr() {
        return dotsArr;
    }
    
    ArrayList<Double> outputs;
    
    public double evaluteFitness(Double[] inputs,boolean forTeaching){

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
        	 if(FIRST_INPUT_MULTIPLIER && i==0 ) {
        		 dotsArr.get(0).get(0).setValue(inputs[i]*10);
            }else {
             	dotsArr.get(0).get(i).setValue(inputs[i]);
            }
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
  
  

   public void kill() {
	   for(ArrayList<Dot> dots : dotsArr) {
		   for(Dot dot : dots) {
			   dot.kill();
			   dot.nodesFromMe=null;
			   dot.nodesToMe=null;
			   dot=null;
		   }
	   }
   }
   
   public boolean equals(Network net) {
	   boolean result;
	   for(int i=0;i<this.dotsArr.size();i++) {
		   for(int j =0;j<this.dotsArr.get(i).size();j++) {
			   for(int x =0;x<this.dotsArr.get(i).get(j).nodesFromMe.size();x++) {
				  if(this.dotsArr.get(i).get(j).nodesFromMe.get(x).getWeight()!=net.dotsArr.get(i).get(j).nodesFromMe.get(x).getWeight()) {
					  return false;
				  }
			   }
		   }
	   }
	 return true;  
   }
   

}
interface FunctionChooseInterface {
    double activationFunction(double a);
}