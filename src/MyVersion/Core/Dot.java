package MyVersion.Core;

import java.io.Serializable;
import java.util.ArrayList;

import static MyVersion.Core.Core_Config.BIAS_VALUE;

public class Dot implements Serializable {
	private static final long serialVersionUID = 3L;
	double weightsDelta;
	double error;
    public double value=0.0f;
    Dot_Type myType;
    public ArrayList<Node> nodesFromMe=new ArrayList<>();
    public ArrayList<Node> nodesToMe=new ArrayList<>();
    
    public Dot(double weightsDelta,double error,double value,Dot_Type myType,ArrayList<Node> nodesFromMe,ArrayList<Node> nodesToMe) {
    	this.weightsDelta=weightsDelta;
    	this.error=error;
    	this.value=value;
    	this.myType=myType;
    	
    }
    public Dot( Dot_Type myType){
        this.myType=myType;
        if (myType==Dot_Type.BIAS_TYPE){
            value=BIAS_VALUE;
        }
    }
    
	public Dot() {
		// TODO Автоматически созданная заглушка конструктора
	}
	void clear(){//clears teach values
        if(myType!=Dot_Type.BIAS_TYPE){
         value=0;}
         error=0f;
         weightsDelta=0f;
    }
    void evalute(){
        if(myType == Dot_Type.HIDDEN || myType == Dot_Type.INPUT){
            value = activaionFunction(value);
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
            value=activaionFunction(value);
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

    static double activaionFunction(double x){
        return (1/(1+ Math.pow(Math.E,-x)));
    }
    
    static double activationFunctionDX(double x){
      return   activaionFunction(x)*(1-activaionFunction(x));
    }
    
    public void kill() {
    	for(Node node: nodesToMe) {
    		node.kill();
    	}
    	nodesFromMe=null;
    	nodesToMe=null;
    }
   
    
}
