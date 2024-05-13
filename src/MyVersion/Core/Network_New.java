package MyVersion.Core;

import static MyVersion.Core.Core_Config.BIAS;
import static MyVersion.Core.Core_Config.FIRST_INPUT_MULTIPLIER;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS_PER_ARRAY;
import static MyVersion.Core.Core_Config.INPUTS;
import static MyVersion.Core.Core_Config.OUTPUTS;

public class Network_New implements Network_Like{
	transient FunctionChooseInterface choose;
	public ActivationFunctions myFunc;
	double[] outputs;
	private static final long serialVersionUID = 2L;
	public double[][][] dotsArr =new double[2+HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY][][];//[x][y][Node weights,last element-Dot weight]
   
 
   public Network_New() {//создает пустую нейросеть(массив точек пустой) для клонирования 
   	
  	}
   
   public Network_New(ActivationFunctions func){
   	myFunc=func;
   	Dot.chooseFunction(myFunc,choose);
   	dotsArr[0]=new double[INPUTS][HIDDEN_DOTS_PER_ARRAY+1];//inputs (0)

   	for (int i = 1; i <= HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
   		dotsArr[i]=new double[HIDDEN_DOTS_PER_ARRAY+BIAS][HIDDEN_DOTS_PER_ARRAY+1];
   	}
   	dotsArr[dotsArr.length-1]=new double[OUTPUTS][1];//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)
   
   }


   public double[][][] getDotsArr() {
       return dotsArr;
   }
   
  
   public double[] calculateOutput(Double[] inputs,boolean forTeaching){

	   //Dots value , error and weightsDelta clears in next method call
   		if(!forTeaching){
   			for(double[][] dotArr: dotsArr){
   				for(double[] dot:dotArr){
   					dot[dot.length-1]=0;
   				}
   			}
   		}
      
   		outputs=new double[dotsArr[dotsArr.length-1].length];
   		//Set inputs
   		for (int i = 0; i < dotsArr[0].length; i++) {
   			if(i<inputs.length) {
   				int curLength=dotsArr[0][i].length;
   				dotsArr[0][i][curLength-1]=inputs[i];
   				try {
   					calculateDotOutput(0,i);
   				}catch(Exception e) {
   					System.err.println("Nodes in input layer does not exsist");
   					e.printStackTrace();
   					Thread.currentThread().interrupt();
   				}
           }
   		}
   		//evalute hidden layer
   		for (int i = 1; i <HIDDEN_DOTS / HIDDEN_DOTS_PER_ARRAY+1; i++) {
   			for (int j = 0; j < dotsArr[i].length ; j++) {
   				calculateDotOutput(i,j);
   			}
   		}
   		//Getting outputs
   		for (int i = 0; i < dotsArr[dotsArr.length-1].length; i++) {
   			//вызов метода точки(возможно повторный)
   			calculateDotOutput(dotsArr[dotsArr.length-1].length,i);
   			outputs[i]=dotsArr[dotsArr.length-1][i][dotsArr[dotsArr.length-1].length];

   		}
   		return outputs;
  }
 
   

   public void kill() {
	   for(double[][] dotArr: dotsArr){
		   for(double[] dot:dotArr){
			   dot=null;
		   }
	   }
   }
  
   void calculateDotOutput(int x,int y) {
	   if(x<dotsArr[x].length-1) {
		   for(int i=0;i<dotsArr[x+1].length;i++) {
			   int nextDotWeightIndex=dotsArr[x+1][i].length-1;
			   int curDotWeightIndex=dotsArr[x][i].length-1;
			   double nodeWeight=dotsArr[x][i][i];
			   dotsArr[x][i][curDotWeightIndex]= choose.activationFunction(dotsArr[x][i][curDotWeightIndex]);
		  
			   double curDotWeight=dotsArr[x][i][curDotWeightIndex];
			   dotsArr[x+1][i][nextDotWeightIndex]= curDotWeight*nodeWeight;
	   		}
	   }else {
		   for(int i=0;i<dotsArr[x].length;i++) {
			   int curDotWeightIndex=dotsArr[x][i].length-1;
			   dotsArr[x][i][curDotWeightIndex]= choose.activationFunction(dotsArr[x][i][curDotWeightIndex]);
		   }
	   }
   }
   
   public boolean equals(Network net) {
	   /*boolean result;
	   for(int i=0;i<this.dotsArr.length;i++) {
		   for(int j =0;j<this.dotsArr[i].length;j++) {
			   for(int x =0;x<this.dotsArr[i][j].nodesFromMe.size();x++) {
				   if(this.dotsArr[i][j].nodesFromMe.get(x).getWeight()!=net.dotsArr[i][j].nodesFromMe.get(x).getWeight()) {
					   return false;
				   }
			   }
		   }
	   }*/
	   return true;  
   }
   /*static double[][][] convertFromBasicNetwork(Network convertFrom){
	   double[][][] result=new double[convertFrom.dotsArr.length][][];
	   for(int i=0;i>convertFrom.dotsArr.length;i++) {
		   result[i]=new double[convertFrom.dotsArr[i].length][];
		   for(int j=0;j>convertFrom.dotsArr[i].length;j++) {
			   
			   for(int y=0;y>convertFrom.dotsArr[i][j].nodesFromMe.size();y++) {
				   result[i][j][]
			   }
		   }
	   }
   }*/

}
