package MyVersion.Core;

public class Node {
    public Node(Dot from,Dot to){
       this.from=from;

        for (int i = 0; i < from.nodesFromMe.size(); i++) {
            if(from.nodesFromMe.get(i).to.equals(to)){
                System.out.println("могло быть 2 ноды на одну точку");
            return;
            }
        }


       if(from.myType==Dot_Type.BIAS_TYPE){
           value1=0.0f;
       }
       this.to=to;
    }
   void evalute(){
        to.value+= from.value*value1;
   }
float value1=1.0f;
Dot from;
Dot to;
}
