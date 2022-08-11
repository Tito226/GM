package MyVersion.Core;

import java.util.ArrayList;

public class Dot {
    public Dot( Dot_Type myType){
        this.myType=myType;
    }
     void clear(){
         value=0;
     }
    void evalute(){
    if(nodesFromMe.size()>0){
        for (int i = 0; i < nodesFromMe.size(); i++) {
           nodesFromMe.get(i).evalute();
        }
       clear();
    }
    }
    float getOutpup(){
      return value;
    }
    Dot_Type myType;
public float value=0.0f;
public ArrayList<Node> nodesFromMe=new ArrayList<>();

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
    void addNode(Dot to){
        nodesFromMe.add(new Node(this,to));
    }
}
