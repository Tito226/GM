package MyVersion.Core;

import java.util.ArrayList;

import static MyVersion.Core.Core_Config.BIAS_VALUE;

public class Dot {
    float weightsDelta;
    float error;
    public float value=0.0f;
    Dot_Type myType;
    public ArrayList<Node> nodesFromMe=new ArrayList<>();
    public ArrayList<Node> nodesToMe=new ArrayList<>();
    
    public Dot(float weightsDelta,float error,float value,Dot_Type myType,ArrayList<Node> nodesFromMe,ArrayList<Node> nodesToMe) {
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
        if(myType==Dot_Type.OUTPUT){
            value=activaionFunction(value);
        }else if(myType!= Dot_Type.BIAS_TYPE){
            value = activaionFunction(value);
            if (nodesFromMe.size() > 0) {
                for (int i = 0; i < nodesFromMe.size(); i++) {
                    nodesFromMe.get(i).evalute();
                }
            }
        }else if(myType==Dot_Type.BIAS_TYPE) {
        	if (nodesFromMe.size() > 0) {
                for (int i = 0; i < nodesFromMe.size(); i++) {
                    nodesFromMe.get(i).evalute();
                }
            }	
        	}
        
    }
    float getOutpup(){
      return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
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

    static float activaionFunction(float x){
        float e =2.71828f;
        return (float) (1/(1+ Math.pow(e,-x)));
    }
    
    static float activationFunctionDX(float x){
      return   activaionFunction(x)*(1-activaionFunction(x));
    }
    
    public void kill() {
    	for(Node node: nodesToMe) {
    		node.kill();
    	}
    }
   
    
}
