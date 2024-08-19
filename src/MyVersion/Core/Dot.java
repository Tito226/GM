package MyVersion.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static MyVersion.Core.Core_Config.BIAS_VALUE;
import static MyVersion.Core.Core_Config.*;

public class Dot implements Serializable {
	private static final long serialVersionUID = 3L;
	double weightsDelta;
	double error;
    public double value=0.0f;
    Dot_Type myType;
    ActivationFunctions myFunc;
    transient FunctionChooseInterface choose;
    public ArrayList<Node> nodesFromMe=new ArrayList<>(HIDDEN_DOTS_PER_ARRAY+BIAS+1);
    public ArrayList<Node> nodesToMe=new ArrayList<>(HIDDEN_DOTS_PER_ARRAY+BIAS+1);
    
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
	
	public void clear(){//clears teach values
        if(myType!=Dot_Type.BIAS_TYPE){
        	value=0;
        }
        error=0f;
        weightsDelta=0f;
    }
    
	public void evalute() {
	    if (myType == Dot_Type.HIDDEN || myType == Dot_Type.INPUT) {
	        value = choose.activationFunction(value);
	        evaluateNodesFromMe();
	    } else if (myType == Dot_Type.BIAS_TYPE) {
	        evaluateNodesFromMe();
	    } else if (myType == Dot_Type.OUTPUT) {
	    	value = choose.activationFunction(value);
	    }
	}

	private void evaluateNodesFromMe() {
	    if (nodesFromMe.size() > 0) {
	        for (Node node : nodesFromMe) {
	        	node.evalute();
	        }
	    }
	}
    
	public double getOutput(){
      return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    
    public void addNode(Dot to){
        if(!(to.myType==Dot_Type.BIAS_TYPE)){
        	Node node=new Node(this,to);
        	nodesFromMe.add(node);
        	to.nodesToMe.add(node);
        }
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
    
     static FunctionChooseInterface chooseFunctionStatic(ActivationFunctions myFunc) {
    	if(myFunc==ActivationFunctions.Tan){
    		return (double x)->{
    			return ActivationFunctionUtils.tanActivationFunction(x);
    		};
    	}else if(myFunc==ActivationFunctions.Sigmoid) {
    		return (double x)->{
    			return ActivationFunctionUtils.sigmoidActivaionFunction(x);
    		};
    	}else if(myFunc==ActivationFunctions.LeackyRelu) {
    		return (double x)->{
    			return ActivationFunctionUtils.leackyReluActivaionFunction(x);
    		};
    	}
    	return null;
    }
     void chooseFunction(ActivationFunctions myFunc) {
      	if(myFunc==ActivationFunctions.Tan){
      		choose=(double x)->{
      			return ActivationFunctionUtils.tanActivationFunction(x);
      		};
      	}else if(myFunc==ActivationFunctions.Sigmoid) {
      		choose=(double x)->{
      			return ActivationFunctionUtils.sigmoidActivaionFunction(x);
      		};
      	}else if(myFunc==ActivationFunctions.LeackyRelu) {
      		choose=(double x)->{
      			return ActivationFunctionUtils.leackyReluActivaionFunction(x);
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
    public FunctionChooseInterface getChoose() {
    	return choose;
    }
    
}
