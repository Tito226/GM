package MyVersion.Core;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS_PER_ARRAY;
import static MyVersion.Core.Core_Config.BIAS;

import java.util.ArrayList;


public class BrainCloneClass {
	public static Network networkClone(Network brain) {//клонирует нейросеть

		Network net =new Network();
		net.dotsArr=new Dot[brain.dotsArr.length][];
		for(int i=0;i<brain.dotsArr.length;i++) {
			net.dotsArr[i]=dotArrayClone(brain.dotsArr[i]);
		}
		allNodesClone(brain,net,true);
		return net;
	}
	private static Dot[] dotArrayClone(Dot[] dotsArr){//клонирует массив Dot
		Dot[] result=new Dot[dotsArr.length];
		for(int i=0;i<dotsArr.length;i++) {
			result[i]=dotClone(dotsArr[i]);
		}
		return result;
	}
	
	private static Dot dotClone(Dot dot) {//клонирует Dot
		Dot result=new Dot();
		result.myFunc=dot.myFunc;
		result.error=dot.error;
		result.myType=dot.myType;
		result.weightsDelta=dot.weightsDelta;
		result.value=dot.value;
		result.chooseFunction(dot.myFunc);
		return result;
	}
	
	private static void allNodesClone(Network brain,Network clone,boolean clonesToMeArr) {
		for (int i = 0; i < brain.dotsArr.length-1; i++) {/*копирование всех нод кроме тех что ведут к выходам*/
            for (int j = 0; j < brain.dotsArr[i].length; j++) {
                for (int k = 0; k < brain.dotsArr[i+1].length-BIAS; k++) {
                    clone.dotsArr[i][j].addNodeClone(clone.dotsArr[i+1][k],brain.dotsArr[i][j].nodesFromMe.get(k));
                }
            }

        }
		
		for(int i=0;i<brain.dotsArr[brain.dotsArr.length-2].length;i++) {//копирование нод которые ведут к выходам
			for (int j= 0; j < brain.dotsArr[brain.dotsArr.length-1].length; j++) {
				clone.dotsArr[brain.dotsArr.length-2][i].addNodeClone(clone.dotsArr[brain.dotsArr.length-1][j],
						brain.dotsArr[brain.dotsArr.length-2][i]
                			.nodesFromMe.get(j));
            }
		}

	}
	
	public static boolean networkCompare(Network first,Network second) {
		for(int i=0;i<first.dotsArr.length;i++) {
			for(int j=0;j<first.dotsArr[i].length;j++) {
				for(int y=0;y<first.dotsArr[i][j].nodesFromMe.size();y++) {
					if(!nodeCompare(first.dotsArr[i][j].nodesFromMe.get(y),second.dotsArr[i][j].nodesFromMe.get(y)))
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
