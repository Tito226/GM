package MyVersion.Core;

import java.io.Serializable;

import static MyVersion.Core.Core_Config.*;
/*сколько нейронов будет в скрытом слое - подбором. Если слишком много,  плохо - сеть начинает запоминать, зубрить примеры (нужно больше примеров чтобы этого избежать),
  если мало нейронов, то она не достаточно гибкая, не сможет ухватить закономерность
  Лучше мало нейронов но больше слоев чем больше нейронов но 1 слой*/

public class Network implements Serializable, Network_Like {
	public ActivationFunctions myFunc;
	double[] outputs;
	private static final long serialVersionUID=2L;
	int usingBiasDots;
	public Dot[][] dotsArr=new Dot[2+HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY][];

	public Network() {// создает пустую нейросеть(массив точек пустой) для клонирования

	}

	public Network(int inputs, int hiddenDots, int hiddenDotsPerArray, int outputs, boolean useBiasDots,
			ActivationFunctions func) {
		this.usingBiasDots=useBiasDots ? 1 : 0;
		int bias=0;
		int hiddenLayerLength=hiddenDots/hiddenDotsPerArray;
		if (useBiasDots) {
			bias=1;
		}
		myFunc=func;
		dotsArr[0]=new Dot[inputs+bias];// inputs (0)

		for (int i=1; i<=hiddenLayerLength; i++) {// hidden dots (1,2,3,...,HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE-1)
			dotsArr[i]=new Dot[hiddenDotsPerArray+bias];
		}
		dotsArr[dotsArr.length-1]=new Dot[outputs];// outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)
		for (int i=0; i<dotsArr[0].length; i++) {// adding input Dots to array,if bias,add dias Dot and add notes to it
			dotsArr[0][i]=new Dot(Dot_Type.INPUT,myFunc);
			if (useBiasDots) {
				dotsArr[0][dotsArr[0].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
			}
		}

		for (int i=1; i<=hiddenLayerLength; i++) {
			for (int j=0; j<hiddenDotsPerArray+bias; j++) {
				dotsArr[i][j]=new Dot(Dot_Type.HIDDEN,myFunc);
			}
			if (useBiasDots) {
				dotsArr[i][dotsArr[i].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
			}
		}

		for (int i=0; i<outputs; i++) {
			dotsArr[dotsArr.length-1][i]=new Dot(Dot_Type.OUTPUT,myFunc);
		}

		// ADDING NODES TO DOTS
		for (int i=0; i<hiddenLayerLength+1; i++) {
			for (int j=0; j<dotsArr[i].length; j++) {
				for (int k=0; k<dotsArr[i+1].length; k++) {
					dotsArr[i][j].addNode(dotsArr[i+1][k]);
				}
			}

		}

		//
	}

	public Network(ActivationFunctions func) {
		this.usingBiasDots=BIAS;
		myFunc=func;
		dotsArr[0]=new Dot[INPUTS+BIAS];// inputs (0)

		for (int i=1; i<=HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY; i++) {// hidden dots (1,2,3,...,HIDDEN_DOTS
																	// /HIDDEN_DOTS_PER_MASSIVE-1)
			dotsArr[i]=new Dot[HIDDEN_DOTS_PER_ARRAY+BIAS];
		}
		dotsArr[dotsArr.length-1]=new Dot[OUTPUTS];// outputs(HIDDEN_DOTS /HIDDEN_DOTS_PER_MASSIVE)
		for (int i=0; i<dotsArr[0].length; i++) {// adding input Dots to array,if bias,add dias Dot and add notes to it
			dotsArr[0][i]=new Dot(Dot_Type.INPUT,myFunc);
			if (usingBiasDots==1) {
				dotsArr[0][dotsArr[0].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
			}
		}

		for (int i=1; i<=HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY; i++) {
			for (int j=0; j<HIDDEN_DOTS_PER_ARRAY+BIAS; j++) {
				dotsArr[i][j]=new Dot(Dot_Type.HIDDEN,myFunc);
			}
			if (usingBiasDots==1) {
				dotsArr[i][dotsArr[i].length-1]=new Dot(Dot_Type.BIAS_TYPE,myFunc);
			}
		}

		for (int i=0; i<OUTPUTS; i++) {
			dotsArr[dotsArr.length-1][i]=new Dot(Dot_Type.OUTPUT,myFunc);
		}

		// ADDING NODES TO DOTS
		for (int i=0; i<HIDDEN_DOTS/HIDDEN_DOTS_PER_ARRAY+1; i++) {
			for (int j=0; j<dotsArr[i].length; j++) {
				for (int k=0; k<dotsArr[i+1].length; k++) {
					dotsArr[i][j].addNode(dotsArr[i+1][k]);
				}
			}

		}

		//
	}

	public Dot[][] getDotsArr() {
		return dotsArr;
	}

	public double[] calculateOutput(Double[] inputs, boolean forTeaching) {

		// Dots value , error and weightsDelta clears in next method call

		if (!forTeaching) {// TODO пересмотреть
			for (Dot[] dotArr : dotsArr) {
				for (Dot dot : dotArr) {
					dot.clear();
				}
			}
		}
		outputs=new double[dotsArr[dotsArr.length-1].length];
		// Set inputs
		for (int i=0; i<dotsArr[0].length; i++) {
			if (i<inputs.length) {
				dotsArr[0][i].setValue(inputs[i]);
			}
		}
		// evalute
		for (int i=0; i<dotsArr.length; i++) {
			for (int j=0; j<dotsArr[i].length; j++) {
				dotsArr[i][j].evalute();
			}
		}
		// Getting outputs
		for (int i=0; i<dotsArr[dotsArr.length-1].length; i++) {
			outputs[i]=dotsArr[dotsArr.length-1][i].getOutput();
		}
		return outputs;
	}

	public void kill() {
		for (Dot[] dots : dotsArr) {
			for (Dot dot : dots) {
				dot.kill();
				dot.nodesFromMe=null;
				dot.nodesToMe=null;
				dot=null;
			}
		}
	}

	public boolean equals(Network net) {
		boolean result;
		for (int i=0; i<this.dotsArr.length; i++) {
			for (int j=0; j<this.dotsArr[i].length; j++) {
				for (int x=0; x<this.dotsArr[i][j].nodesFromMe.size(); x++) {
					if (this.dotsArr[i][j].nodesFromMe.get(x).getWeight()!=net.dotsArr[i][j].nodesFromMe.get(x)
							.getWeight()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public void print() {
		for (int i=0; i<dotsArr.length; i++) {
			for (int j=0; j<dotsArr[i].length; j++) {
				System.out.print(String.format("%.3f",dotsArr[i][j].value)+", ");
			}
			System.out.println("");
			System.out.println("");

		}
	}

	public void printFull() {
		for (int x=0; x<dotsArr.length; x++) {
			for (int y=0; y<dotsArr[x].length; y++) {
				System.out.print("x: "+x+" y: "+y+"; [");
				for (int g=0; g<dotsArr[x][y].nodesFromMe.size(); g++) {
					System.out.print(String.format("%.3f",dotsArr[x][y].nodesFromMe.get(g).getWeight())+" ");
				}
				System.out.print(String.format("%.3f",dotsArr[x][y].value)+" ");
				System.out.print("]");

				// System.out.println(" ");
				System.out.println(" ");
			}
			System.out.println("--------------------------------------------------");
		}
	}

	@Override
	public double[][][] returnStandanrtRepresentation() {/* TODO исправить назначение веса Dot */
		double[][][] result=new double[this.dotsArr.length][][];
		for (int x=0; x<this.dotsArr.length; x++) {
			result[x]=new double[this.dotsArr[x].length][];
			for (int y=0; y<this.dotsArr[x].length; y++) {
				result[x][y]=new double[this.dotsArr[x][y].nodesFromMe.size()+1];// TODO возникнут проблемы если не
																					// использовать BIAS_DOT
				for (int g=0; g<this.dotsArr[x][y].nodesFromMe.size(); g++) {
					result[x][y][g]=this.dotsArr[x][y].nodesFromMe.get(g).getWeight();
				}
				result[x][y][result[x][y].length-1]=this.dotsArr[x][y].value;
			}
		}
		return result;
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
