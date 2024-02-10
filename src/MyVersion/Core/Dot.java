package MyVersion.Core;

import java.io.Serializable;
import java.util.ArrayList;

import static MyVersion.Core.Core_Config.BIAS_VALUE;
import static MyVersion.Core.Core_Config.func;

public class Dot implements Serializable {
	private static final long serialVersionUID = 3L;
	double weightsDelta;
	double error;
    public double value=0.0f;
    Dot_Type myType;
    ActivationFunctions myFunc;
    transient FunctionChooseInterface choose;
    public ArrayList<Node> nodesFromMe=new ArrayList<>();
    public ArrayList<Node> nodesToMe=new ArrayList<>();
    
    public Dot(double weightsDelta,double error,double value,Dot_Type myType,ActivationFunctions myFunc,ArrayList<Node> nodesFromMe,ArrayList<Node> nodesToMe) {
    	this.weightsDelta=weightsDelta;
    	this.myFunc=myFunc;
    	this.error=error;
    	this.value=value;
    	this.myType=myType;
    	chooseFunction(myFunc);
    }
    
    public Dot( Dot_Type myType,ActivationFunctions myFunc){
    	this.myFunc=myFunc;
        this.myType=myType;
        if (myType==Dot_Type.BIAS_TYPE){
            value=BIAS_VALUE;
        }
        chooseFunction(myFunc);
    }
    
	public Dot() {
		
	}
	
	void clear(){//clears teach values
        if(myType!=Dot_Type.BIAS_TYPE){
         value=0;}
         error=0f;
         weightsDelta=0f;
    }
    
	void evalute(){
        if(myType == Dot_Type.HIDDEN || myType == Dot_Type.INPUT){
            value = choose.activationFunction(value);
            if (nodesFromMe.size() > 0) {
                for (int i = 0; i < nodesFromMe.size(); i++) {
                    nodesFromMe.get(i).evalute();
                }
            }
            return;
        }
        
        else if(myType==Dot_Type.BIAS_TYPE) {
        	if (nodesFromMe.size() > 0) {
                for (int i = 0; i < nodesFromMe.size(); i++) {
                    nodesFromMe.get(i).evalute();
                }
            }	
        }
        else if(myType==Dot_Type.OUTPUT){
            value=sigmoidActivaionFunction(value);
        }
    }
    
	double getOutpup(){
      return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    
    void addNode(Dot to){
        if(to.myType==Dot_Type.BIAS_TYPE){

        }else {
        Node node=new Node(this,to);
        nodesFromMe.add(node);
        to.nodesToMe.add(node);}
    }
    
    void addNodeClone(Dot to,Node nodeToClone){
        if(to.myType==Dot_Type.BIAS_TYPE){
        	return;
        }else {
        Node node=new Node(this,to);
        nodesFromMe.add(node);
        to.nodesToMe.add(node);
        node.changeble=nodeToClone.changeble;
        node.setWeight(nodeToClone.getWeight());
        }
    }

    static double sigmoidActivaionFunction(double x){
        return (1/(1+ Math.pow(Math.E,-x)));
    }
    
    static double tanActivationFunction(double x) {
    	return Math.tanh(x);
    }
    
    static double leackyReluActivaionFunction(double x){
        return Math.max(0.1*x, x);
    }
    
    static double activationFunctionDX(double x,ActivationFunctions myFunc){
    	if(myFunc==ActivationFunctions.Tan) {
    		return tanActivationFunctionDX(x);
    	}else if(myFunc==ActivationFunctions.Sigmoid) {
    		return sigmoidActivationFunctionDX(x);
    	}else {
    		return (Double) null;
    	}
    }
    
    static double tanActivationFunctionDX(double x) {
    	return 1/(Math.pow(Math.cosh(x),2));
    }
    
    static double sigmoidActivationFunctionDX(double x){
        return sigmoidActivaionFunction(x)*(1-sigmoidActivaionFunction(x));
     }
    
     void chooseFunction(ActivationFunctions myFunc) {
    	if(myFunc==ActivationFunctions.Tan){
    		choose=(double x)->{
    			return tanActivationFunction(x);
    		};
    	}else if(myFunc==ActivationFunctions.Sigmoid) {
    		choose=(double x)->{
    			return sigmoidActivaionFunction(x);
    		};
    	}else if(myFunc==ActivationFunctions.LeackyRelu) {
    		choose=(double x)->{
    			return leackyReluActivaionFunction(x);
    		};
    	}
    }
    
    public void kill() {
    	for(Node node: nodesToMe) {
    		node.kill();
    	}
    	nodesFromMe=null;
    	nodesToMe=null;
    }
   
    
}
