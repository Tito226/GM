package MyVersion.Core;

import java.io.Serializable;

public class Node implements Serializable {
	private static final long serialVersionUID = 4L;
    public boolean changeble=true; //IF IT BECOME TRUE(IN RUNTIME) ,DONT CHANGE IT
    private double weight =0.0d;
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
           weight =1.0d;
       }
       this.to=to;
    }
    
    public Node() {
		// TODO Автоматически созданная заглушка конструктора
	}

	 public void evalute(){
    	if(changeble) {
    		to.value+= from.value* weight;
        }
    }

    public void setWeight(double weight)  {
        this.weight=weight;
    }

    public double getWeight() {
        return weight;
    }

    public void kill() {
    	from=null;
    	to=null;
    }
}
