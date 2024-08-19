package MyVersion.Core;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS_PER_ARRAY;
import static MyVersion.Core.Core_Config.BIAS;

import java.util.ArrayList;


public class BrainCloneClass {
	/**Returns Network clone*/
	public static Network networkClone(Network brain) {//клонирует нейросеть	
		Network net =new Network();
		//net.myFunc=brain.myFunc;
		
		net.dotsArr=new Dot[brain.dotsArr.length][];
		for(int i=0;i<brain.dotsArr.length;i++) {
			net.dotsArr[i]=dotArrayClone(brain.dotsArr[i]);
		}
		allNodesClone(brain,net,true);
		return net;
	}
	public static SimplifiedNetwork simplifiedNetworkClone(SimplifiedNetwork brain) {//клонирует нейросеть	
		SimplifiedNetwork net =new SimplifiedNetwork();
		net.dotsArr=cloneSimplifiedNetworkDotsArr(brain);
		net.myFunc=brain.myFunc;
		net.choose=ActivationFunctionUtils.chooseFunctionStatic(net.myFunc);
		net.usingBiasDots=brain.usingBiasDots;
		return net;
	}
	/**Return Network clone automaticly finds network type(SimplifiedNetwork,Network and ect.)*/
	public static Network_Like autoNetworkClone(Network_Like brain) {//клонирует нейросеть
		if(brain instanceof SimplifiedNetwork) {
			return simplifiedNetworkClone((SimplifiedNetwork)brain);
		}else if(brain instanceof Network) {
			return networkClone((Network)brain);
		}
		return null;
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
	
	public static SimplifiedNetwork convertFromBasicNetworkToSimplified(Network convertFrom) {
		SimplifiedNetwork result=new SimplifiedNetwork();
		result.dotsArr=convertFromBasicNetworkTo3DArray(convertFrom);
		result.myFunc=convertFrom.myFunc;
		result.choose=ActivationFunctionUtils.chooseFunctionStatic(convertFrom.myFunc);
		result.usingBiasDots=convertFrom.usingBiasDots;
		return result;
	}
	
	public static double[][][] convertFromBasicNetworkTo3DArray(Network convertFrom){/*TODO исправить назначение веса Dot*/
		double[][][] result=new double[convertFrom.dotsArr.length][][];
		for(int x=0;x<convertFrom.dotsArr.length;x++) {
			result[x]=new double[convertFrom.dotsArr[x].length][];
			for(int y=0;y<convertFrom.dotsArr[x].length;y++) {
				result[x][y]=new double[convertFrom.dotsArr[x][y].nodesFromMe.size()+1];//TODO возникнут проблемы если не использовать BIAS_DOT
				for(int g=0;g<convertFrom.dotsArr[x][y].nodesFromMe.size();g++) {
					result[x][y][g]=convertFrom.dotsArr[x][y].nodesFromMe.get(g).getWeight();
				}
				result[x][y][result[x][y].length-1]=convertFrom.dotsArr[x][y].value;
			}
		}
		return result;
	}
	
	public static double[][][] cloneSimplifiedNetworkDotsArr(SimplifiedNetwork copyFrom){/*TODO исправить назначение веса Dot*/
		double[][][] dotsArrToCopy=copyFrom.dotsArr;
		double[][][] result=new double[copyFrom.dotsArr.length][][];
		for(int x=0;x<dotsArrToCopy.length;x++) {
			result[x]=new double[dotsArrToCopy[x].length][];
			for(int y=0;y<dotsArrToCopy[x].length;y++) {
				result[x][y]=new double[dotsArrToCopy[x][y].length];
				for(int g=0;g<dotsArrToCopy[x][y].length;g++) {
					result[x][y][g]=dotsArrToCopy[x][y][g];
				}
			}
		}
		return result;
	}
	
	public static Dot[][] convertFrom3DArrayToDotArr(double[][][] convertFrom,boolean useBiasDots, ActivationFunctions myFunc){
		Dot[][] result=new Dot[convertFrom.length][];
		result[0]=new Dot[convertFrom[0].length];//inputs (0)

    	for (int i = 1; i <= convertFrom.length-1; i++) {//hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
    		result[i]=new Dot[convertFrom[i].length];
    	}
    	result[result.length-1]=new Dot[convertFrom[convertFrom.length-1].length];//outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)
    	for (int i = 0; i < result[0].length; i++) {//adding input Dots to array,if bias,add dias Dot and add notes to it
    		result[0][i]=new Dot(Dot_Type.INPUT,myFunc);
    		if(useBiasDots) {
    			result[0][result[0].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
    		}
    	}

    	for (int i = 1; i <=convertFrom.length-1; i++) {
    		for (int j = 0; j < result[i].length; j++) {
    			result[i][j]=new Dot(Dot_Type.HIDDEN,myFunc);
    		}
    		if(useBiasDots) {
    			result[i][result[i].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
    		}
    	}
        
        for (int i = 0; i < convertFrom[convertFrom.length-1].length; i++) {
            result[result.length-1][i]=new Dot(Dot_Type.OUTPUT,myFunc);
        }

        //ADDING NODES TO DOTS
        for (int i = 0; i <convertFrom.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                for (int k = 0; k < result[i+1].length; k++) {
                    result[i][j].addNode(result[i+1][k]);
                }
            }

        }
        return result;
	}
	

}
