package MyVersion.Core;

public class Node {
    public Node(Dot from,Dot to){
       this.from=from;
       this.to=to;
    }
   void evalute(){
        to.value+= from.value*value1;
   }
float value1=1.0f;
Dot from;
Dot to;
}
