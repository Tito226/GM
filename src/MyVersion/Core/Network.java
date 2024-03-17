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
	 double[] outputs;
	private static final long serialVersionUID = 2L;
	ArrayList<ArrayList<Dot>> dotsArr=null;
    public Dot[][] dotsArr1 =new Dot[2+HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY][];
    
    /*public void clon(){

    	dotsArr1 =new Dot[2+HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY][];
    	for (int i = 0; i < dotsArr.size(); i++) {
    		dotsArr1[i]=new Dot[dotsArr.get(i).size()];
			for (int j = 0; j < dotsArr.get(i).size(); j++) {
				dotsArr1[i][j]=dotsArr.get(i).get(j);
			}
		}
	}*/
    public Network() {//создает пустую нейросеть(массив точек пустой) для клонирования 
    	
   	}
    
    public Network(int iii){
    	myFunc=func;
    	dotsArr1[0]=new Dot[INPUTS];//inputs (0)

    	for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
    		dotsArr1[i]=new Dot[HIDDEN_DOTS_PER_ARRAY+BIAS];
    	}
    	dotsArr1[dotsArr1.length-1]=new Dot[OUTPUTS];//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)
    	for (int i = 0; i < INPUTS; i++) {//adding input Dots to array,if bias,add dias Dot and add notes to it
    		dotsArr1[0][i]=new Dot(Dot_Type.INPUT,myFunc);
    	}

    	for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY ; i++) {
    		for (int j = 0; j < HIDDEN_DOTS_PER_ARRAY+BIAS; j++) {
    			dotsArr1[i][j]=new Dot(Dot_Type.HIDDEN,myFunc);
    		}
    		if(BIAS==1) {
    			dotsArr1[i][dotsArr1[i].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
    		}
    	}
        
        for (int i = 0; i < OUTPUTS; i++) {
            dotsArr1[dotsArr1.length-1][i]=new Dot(Dot_Type.OUTPUT,myFunc);
        }

        //ADDING NODES TO DOTS
        for (int i = 0; i < HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY+1; i++) {
            for (int j = 0; j < dotsArr1[i].length; j++) {
                for (int k = 0; k < dotsArr1[i+1].length; k++) {
                    dotsArr1[i][j].addNode(dotsArr1[i+1][k]);
                }
            }

        }

        //
    }


    public Dot[][] getDotsArr() {
        return dotsArr1;
    }
    
   
    public double[] evaluteFitness(Double[] inputs,boolean forTeaching){

      //Dots value , error and weightsDelta clears in next method call
    	if(!forTeaching){
    		for(Dot[] dotArr: dotsArr1){
    			for(Dot dot:dotArr){
    				dot.clear();
    			}
    		}
    	}
       
        outputs=new double[dotsArr1[dotsArr1.length-1].length];
        //Set inputs
        for (int i = 0; i < inputs.length; i++) {
        	 if(FIRST_INPUT_MULTIPLIER && i==0 ) {
        		 dotsArr1[0][0].setValue(inputs[i]*10);//TODO THINK
            }else {
             	dotsArr1[0][i].setValue(inputs[i]);
            }
            dotsArr1[0][i].evalute();
        }
        //evalute hidden layer
      for (int i = 1; i <HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY+1; i++) {
          for (int j = 0; j < dotsArr1[i].length ; j++) {
        	  dotsArr1[i][j].evalute();
          }
      }
      //Getting outputs
      for (int i = 0; i < dotsArr1[dotsArr1.length-1].length; i++) {
          //вызов метода точки(возможно повторный)
          dotsArr1[dotsArr1.length-1][i].evalute();
          //
          outputs[i]=dotsArr1[dotsArr1.length-1][i].getOutput();

      }
      if(!forTeaching){
    	  for(Dot[] dotArr: dotsArr1){
    		  for(Dot dot:dotArr){
    			  dot.clear();
    		  }
    	  }
      }

      return outputs;
   }
  
  

   public void kill() {
	   for(Dot[] dots : dotsArr1) {
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
	   for(int i=0;i<this.dotsArr1.length;i++) {
		   for(int j =0;j<this.dotsArr1[i].length;j++) {
			   for(int x =0;x<this.dotsArr1[i][j].nodesFromMe.size();x++) {
				  if(this.dotsArr1[i][j].nodesFromMe.get(x).getWeight()!=net.dotsArr1[i][j].nodesFromMe.get(x).getWeight()) {
					  return false;
				  }
			   }
		   }
	   }
	 return true;  
   }
   

}
 