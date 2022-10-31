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
                System.out.println("могло быть 2 ноды на одну точку");
            return;
            }
        }


       if(from.myType==Dot_Type.BIAS_TYPE){
           weight =0.0f;
       }
       this.to=to;
    }
   void evalute(){
        to.value+= from.value* weight;
   }

    public void setWeight(float weight)  {
        this.weight=weight;
        /* if(changeble){
        this.weight = weight;
        }else try {
            throw new UchengebleExeption("НЕЛЬЗЯ МЕНЯТЬ НЕИЗМЕНЯЕМЫЕ НОДЫ!!!!");
        } catch (UchengebleExeption e) {
         //   e.printStackTrace();
        }*/
    }

    public float getWeight() {
        return weight;
    }
}
