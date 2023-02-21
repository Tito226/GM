package MyVersion.Core;

public class Node {
    boolean changeble=true; //IF IT BECOME TRUE(IN RUNTIME) ,DONT CHANGE IT
    private float weight =0.0f;
    Dot from;
    Dot to;
    public Node(Dot from,Dot to){
       this.from=from;

        for (int i = 0; i < from.nodesFromMe.size(); i++) {
            if(from.nodesFromMe.get(i).to.equals(to)){
                System.out.println("могло быть 2 ноды на одну точку(node class)");
            return;
            }
        }


       if(from.myType==Dot_Type.BIAS_TYPE){
           weight =1.0f;
       }
       this.to=to;
    }
   void evalute(){
	   if(changeble) {
        to.value+= from.value* weight;
        }
   }

    public void setWeight(float weight)  {
        this.weight=weight;
    }

    public float getWeight() {
        return weight;
    }
}
