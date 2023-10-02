package MyVersion.Core;

import java.util.ArrayList;
import java.util.Random;

import static MyVersion.Core.Core_Config.*;

public class Network {
//сколько нейронов будет в скрытом слое - подбором. Если слишком много,  плохо - сеть начинает запоминать, зубрить примеры (нужно больше примеров чтобы этого избежать),
// если мало нейронов, то она не достаточно гибкая, не сможет ухватить закономерность
//Лучше мало нейронов но больше слоев чем больше нейронов но 1 слой
    ArrayList<ArrayList<Dot>> dotsArr =new ArrayList<>();
    
    public Network() {//создает пустую нейросеть(массив точек пустой) для клонирования 
    	
   	}
    
    public Network(int iii){
       dotsArr.add(new ArrayList<>());//inputs (0)

       for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
           dotsArr.add(new ArrayList<>());

       }
//TODO на входном слое появляется много лишних нейронов(биас в том числе)
       for (int i = 0; i < INPUTS; i++) {//adding input Dots to array,if bias,add dias Dot and add notes to it
           dotsArr.get(0).add(new Dot(Dot_Type.INPUT));
       }
       
       if(BIAS==1){
           dotsArr.get(0).add(new Dot(Dot_Type.BIAS_TYPE));
           for (int j = 0; j < dotsArr.get(1).size() ; j++) {
               dotsArr.get(0).get(dotsArr.size()-1).addNode(dotsArr.get(1).get(j));
           }
       }

       dotsArr.add(new ArrayList<>());//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)


       for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY ; i++) {
           for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY; j++) {
               dotsArr.get(i).add(new Dot(Dot_Type.HIDDEN));
           }

           if (BIAS==1) {
               dotsArr.get(i).add(new Dot(Dot_Type.BIAS_TYPE));
               for (int j = 0; j < dotsArr.get(i + 1).size(); j++) {
                   dotsArr.get(i).get(dotsArr.get(i).size() - 1).addNode(dotsArr.get(i+1).get(j));
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
  
   public void mutate(int numberOfMutations) {
	   Random r =new Random();
	   for(int i=0;i<numberOfMutations;i++) {
		   
		   int rBuffer1=r.nextInt(dotsArr.size()-1);
		   int rBuffer2=r.nextInt( dotsArr.get(rBuffer1).size());
		   Dot gettedDot=dotsArr.get(rBuffer1).get(rBuffer2);
		   Node gettedNode= gettedDot.nodesFromMe.get(r.nextInt(
				   gettedDot.nodesFromMe.size()));//выбор случайной ноды
		   if(r.nextInt(2)==1) {
			   gettedNode.setWeight(gettedNode.getWeight()+r.nextFloat()); 
		   }else {
			   gettedNode.setWeight(gettedNode.getWeight()-r.nextFloat()); 
		   }
		   
	   }
   }


}
