package MyVersion.Core;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS_PER_ARRAY;
import static MyVersion.Core.Core_Config.BIAS;

import java.util.ArrayList;

public class BrainCloneClass {
	public static Network networkClone(Network brain) {//клонирует нейросеть
		ArrayList<ArrayList<Dot>> dotsArrClone =new ArrayList<>();
		Network net =new Network();
		for(int i=0;i<brain.dotsArr.size();i++) {
			net.dotsArr.add(dotArrayClone(brain.dotsArr.get(i)));
		}
		allNodesClone(brain,net,true);
		return net;
	}
	private static ArrayList<Dot> dotArrayClone(ArrayList<Dot> arr){//клонирует массив Dot
		ArrayList<Dot> result=new ArrayList();
		for(int i=0;i<arr.size();i++) {
			result.add(dotClone(arr.get(i)));
		}
		return result;
	}
	
	private static Dot dotClone(Dot dot) {//клонирует Dot
		Dot result=new Dot();
		result.error=dot.error;
		result.myType=dot.myType;
		result.weightsDelta=dot.weightsDelta;
		result.value=dot.value;
		//result.nodesToMe=nodeArrClone(dot.nodesToMe);
		//result.nodesFromMe=nodeArrClone(dot.nodesFromMe);
		return result;
	}
	
	private static void allNodesClone(Network brain,Network clone,boolean clonesToMeArr) {
		for (int i = 0; i < brain.dotsArr.size()-1; i++) {//копирование всех нод кроме тех что ведут к выходам
            for (int j = 0; j < brain.dotsArr.get(i).size(); j++) {
                for (int k = 0; k < brain.dotsArr.get(i+1).size()-BIAS; k++) {
                    clone.dotsArr.get(i).get(j).addNodeClone(clone.dotsArr.get(i+1).get(k),brain.dotsArr.get(i).get(j)
                    		.nodesFromMe.get(k));
                }
            }

        }
		
		for(int i=0;i<brain.dotsArr.get(brain.dotsArr.size()-2).size();i++) {//копирование нод которые ведут к выходам
			for (int j= 0; j < brain.dotsArr.get(brain.dotsArr.size()-1).size(); j++) {//TODO  сверить ноды клона и родителя
				clone.dotsArr.get(brain.dotsArr.size()-2).get(i).addNodeClone(clone.dotsArr.get(brain.dotsArr.size()-1).get(j),
						brain.dotsArr.get(brain.dotsArr.size()-2).get(i)
                			.nodesFromMe.get(j));
            }
		}

	}
	
	/*private static ArrayList<Node> nodeArrClone(ArrayList<Node> arr,boolean clonesToMeArr) {//клонирует массив Node
		ArrayList<Node> result=new ArrayList();
		for(int i=0;i<arr.size();i++) {
			result.add(nodeClone(arr.get(i)));
		}
		return result;
	}*/
	
	private static Node nodeClone(Node node,Dot to,Dot from) {//клонирует Node, доты передавать уже клонированные
		//TODO Stub
		Node result=new Node();
		result.setWeight(node.getWeight());
		result.changeble=node.changeble;
		result.to=to;
		result.from=from;
		return result;
	}
}
