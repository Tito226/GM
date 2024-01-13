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
		return result;
	}
	
	private static void allNodesClone(Network brain,Network clone,boolean clonesToMeArr) {
		for (int i = 0; i < brain.dotsArr.size()-1; i++) {/*копирование всех нод кроме тех что ведут к выходам*/
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
	
	public static boolean networkCompare(Network first,Network second) {
		for(int i=0;i<first.dotsArr.size();i++) {
			for(int j=0;j<first.dotsArr.get(i).size();j++) {
				for(int y=0;y<first.dotsArr.get(i).get(j).nodesFromMe.size();y++) {
					if(!nodeCompare(first.dotsArr.get(i).get(j).nodesFromMe.get(y),second.dotsArr.get(i).get(j).nodesFromMe.get(y)))
						return false;
				}
			}
		}
		return true;
	}
	
	private static boolean nodeCompare(Node first,Node second) {
		if(first.getWeight()==second.getWeight() && first.changeble==second.changeble) {
			return true;
		}else {
			return false;
		}
	}
	
	
}
