package MyVersion.Core;

import static MyVersion.Core.Core_Config.BIAS;
import static MyVersion.Core.Core_Config.FIRST_INPUT_MULTIPLIER;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS;
import static MyVersion.Core.Core_Config.HIDDEN_DOTS_PER_ARRAY;
import static MyVersion.Core.Core_Config.INPUTS;
import static MyVersion.Core.Core_Config.OUTPUTS;

import java.awt.Graphics;
import java.util.Arrays;

public class SimplifiedNetwork implements Network_Like {
	public transient FunctionChooseInterface choose;
	int usingBiasDots;
	public ActivationFunctions myFunc;
	double[] outputs;
	private static final long serialVersionUID=2L;
	public double[][][] dotsArr=new double[2+HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY][][];// [x][y][Node weights,last
																					// element-Dot weight]

	public SimplifiedNetwork() {// создает пустую нейросеть(массив точек пустой) для клонирования

	}

	public SimplifiedNetwork(ActivationFunctions func) {
		myFunc=func;
		choose=ActivationFunctionUtils.chooseFunctionStatic(myFunc);
		usingBiasDots=BIAS;
		dotsArr[0]=new double[INPUTS][HIDDEN_DOTS_PER_ARRAY+1];// inputs (0)

		for (int i=1; i<=HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY; i++) {// hidden dots (1,2,3,...,HIDDEN_DOTS
																	// /HIDDEN_DOTS_PER_MASSIVE-1)
			dotsArr[i]=new double[HIDDEN_DOTS_PER_ARRAY+BIAS][HIDDEN_DOTS_PER_ARRAY+1];
		}
		dotsArr[dotsArr.length-1]=new double[OUTPUTS][1];// outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)

	}

	public double[][][] getDotsArr() {
		return dotsArr;
	}

	public double[] calculateOutput(Double[] inputs, boolean forTeaching) {/* TODO неправильно считает */

		// Dots value , error and weightsDelta clears in next method call
		if (!forTeaching) {
			for (int x=0; x<dotsArr.length; x++) {
				if (!(x==dotsArr.length-1)) {
					for (int y=0; y<dotsArr[x].length-usingBiasDots; y++) {
						dotsArr[x][y][dotsArr[x][y].length-1]=0;
					}
				} else {
					for (int y=0; y<dotsArr[x].length; y++) {
						dotsArr[x][y][dotsArr[x][y].length-1]=0;
					}
				}
			}
		}

		outputs=new double[dotsArr[dotsArr.length-1].length];
		// Set inputs
		for (int y=0; y<dotsArr[0].length; y++) {
			if (y<inputs.length) {
				int curLength=dotsArr[0][y].length;
				dotsArr[0][y][curLength-1]=inputs[y];
			} else {
				break;
			}
		}
		// evalute
		for (int x=0; x<dotsArr.length; x++) {
			for (int y=0; y<dotsArr[x].length; y++) {
				calculateDotOutput(x,y);
			}
		}
		// Getting outputs
		for (int y=0; y<dotsArr[dotsArr.length-1].length; y++) {
			int x=dotsArr.length-1;
			int g=dotsArr[x][y].length-1;
			outputs[y]=dotsArr[x][y][g];
		}
		/*
		 * for(int x=0;x<dotsArr.length;x++) { for(int y=0;y<dotsArr[x].length;y++) {
		 * System.err.print("x: "+x+" y: "+y+"; "); for(int
		 * g=0;g<dotsArr[x][y].length;g++) { System.out.print(String.format("%.2f",
		 * dotsArr[x][y][g])+" "); } System.out.println(" "); System.out.println(" "); }
		 * }
		 */
		return outputs;
	}

	void calculateDotOutput(int x, int y) {/* TODO неправильно считает */
		if (usingBiasDots==0) {
			dotsArr[x][y][dotsArr[x][y].length-1]=choose.activationFunction(dotsArr[x][y][dotsArr[x][y].length-1]);
		} else if (y<dotsArr[x].length-1||x==dotsArr.length-1) {
			dotsArr[x][y][dotsArr[x][y].length-1]=choose.activationFunction(dotsArr[x][y][dotsArr[x][y].length-1]);
		}
		double curDotWeight=dotsArr[x][y][dotsArr[x][y].length-1];
		for (int i=0; i<dotsArr[x][y].length-1; i++) {// обойти все связи,не доходя до веса нейрона(Dot)
			if (x==dotsArr.length-2) {
				int sdfg=0;
				sdfg++;
			}
			int nextDotWeightIndex=dotsArr[x+1][i].length-1;
			double nodeWeight=dotsArr[x][y][i];
			dotsArr[x+1][i][nextDotWeightIndex]+=curDotWeight*nodeWeight;
			if (x==dotsArr.length-2) {
				int sdfg2=0;
				sdfg2++;
			}
		}
	}

	public void kill() {
		for (double[][] dotArr : dotsArr) {
			for (double[] dot : dotArr) {
				dot=null;
			}
		}
	}

	public boolean equals(Network net) {
		boolean result;
		for (int x=0; x<this.dotsArr.length; x++) {
			for (int y=0; y<this.dotsArr[x].length; y++) {
				for (int g=0; g<this.dotsArr[x][y].length-1; g++) {
					if (this.dotsArr[x][y][g]!=net.dotsArr[x][y].nodesFromMe.get(g).getWeight()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean equals(SimplifiedNetwork net) {
		/*
		 * boolean result; for(int i=0;i<this.dotsArr.length;i++) { for(int j
		 * =0;j<this.dotsArr[i].length;j++) { for(int x
		 * =0;x<this.dotsArr[i][j].nodesFromMe.size();x++) {
		 * if(this.dotsArr[i][j].nodesFromMe.get(x).getWeight()!=net.dotsArr[i][j].
		 * nodesFromMe.get(x).getWeight()) { return false; } } } }
		 */
		return true;
	}

	public void print() {
		for (int i=0; i<dotsArr.length; i++) {
			for (int j=0; j<dotsArr[i].length; j++) {
				System.out.print(String.format("%.3f",dotsArr[i][j][dotsArr[i][j].length-1])+", ");
			}
			System.out.println("");
			System.out.println("");

		}
	}

	public void printFull() {
		for (int x=0; x<dotsArr.length; x++) {
			for (int y=0; y<dotsArr[x].length; y++) {
				System.out.print("x: "+x+" y: "+y+"; [");
				for (int g=0; g<dotsArr[x][y].length; g++) {
					System.out.print(String.format("%.3f",dotsArr[x][y][g])+" ");
				}
				System.out.print("]");

				// System.out.println(" ");
				System.out.println(" ");
			}
			System.out.println("--------------------------------------------------");
		}
	}

	public void printLayerSum(int layerIndex) {
		double sum=0;
		for (int i=0; i<dotsArr[layerIndex].length; i++) {
			sum+=Math.abs(dotsArr[layerIndex][i][dotsArr[layerIndex][i].length-1]);
		}
		System.out.println("x: "+layerIndex+" ->"+String.format("%.6f",sum));
	}

	@Override
	public double[][][] returnStandanrtRepresentation() {
		return BrainCloneClass.cloneSimplifiedNetworkDotsArr(this);
	}

	@Override
	public boolean getUsingBiasDots() {
		return usingBiasDots==1 ? true : false;
	}

	@Override
	public ActivationFunctions getMyFunc() {
		return myFunc;
	}
}
