package MyVersion.Core;

import java.util.ArrayList;

public class Dot {
    float weightsDelta;
    float error;
    public float value=0.0f;
    Dot_Type myType;
    public ArrayList<Node> nodesFromMe=new ArrayList<>();
    public ArrayList<Node> nodesToMe=new ArrayList<>();
    public Dot( Dot_Type myType){
        this.myType=myType;
        if (myType==Dot_Type.BIAS_TYPE){
            value=1;
        }
    }
     void clear(){
         value=0;
         error=0f;
         weightsDelta=0f;
     }
    void evalute(){
        if(myType==Dot_Type.OUTPUT){
            value=activaionFunction(value);
        }else {
            value = activaionFunction(value);
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
      Node node=  new Node(this,to);
        nodesFromMe.add(node);
        to.nodesToMe.add(node);}
    }


  static   float activaionFunction(float x){
        float e =2.71828f;
        return (float) (1/(1+ Math.pow(e,-x)));
    }
   static float activationFunctionDX(float x){
      return   activaionFunction(x)*(1-activaionFunction(x));
    }
}
