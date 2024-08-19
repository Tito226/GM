package MyVersion.Frame.Wrappers;

import static MyVersion.Core.Core_Config.MUTATION_MULTIPLIER;

import java.util.ArrayList;
import java.util.Random;
import MyVersion.Core.ActivationFunctionUtils;
import MyVersion.Core.BrainCloneClass;
import MyVersion.Core.SimplifiedNetwork;
import static MyVersion.Frame.FRAME_CONFIG.CHANCE_OF_NODE_ACTIVATE;
import static MyVersion.Frame.FRAME_CONFIG.CHANCE_OF_NODE_DEACTIVATE;

public class SimplifiedNetworkWrapper extends SimplifiedNetwork implements NetworkWrapperLike {
	private static final long serialVersionUID=-9082519085580679388l;
	ArrayList<Integer[]> blockedNodes;
	public boolean dontDelete=false;
	public boolean isDead=false;
	/***/
	public SimplifiedNetworkWrapper() {

	}

	public SimplifiedNetworkWrapper(SimplifiedNetwork network) {
		// SimplifiedNetwork newNet=BrainCloneClass.simplifiedNetworkClone(network);
		this.dotsArr=BrainCloneClass.cloneSimplifiedNetworkDotsArr(network);
		this.myFunc=network.myFunc;
		this.choose=ActivationFunctionUtils.chooseFunctionStatic(network.myFunc);
		blockedNodes=new ArrayList<Integer[]>();
		for (int x=0; x<dotsArr.length; x++) {
			for (int y=0; y<dotsArr[x].length; y++) {
				for (int g=0; g<dotsArr[x][y].length-1; g++) {
					if (dotsArr[x][y][g]==0d) {
						blockedNodes.add(new Integer[] { x, y, g });
					}
				}
			}
		}
	}
	/*TODO WRITE DOC*/
	public void mutate(int numberOfMutations) {
		Random r=new Random();
		for (int i=0; i<r.nextInt(numberOfMutations); i++) {
			nodeMutate(r);
		}
		if (r.nextInt(CHANCE_OF_NODE_ACTIVATE)==0) {
			nodeUnlock(r);
		} else if (r.nextInt(CHANCE_OF_NODE_DEACTIVATE)==0) {
			nodeDeacticate(r);
		}
	}
	/**add random node to blockedNodes list*/
	private void nodeDeacticate(Random r) {
		// выбрать случайный столбец dotsArr
		int rBufferX=r.nextInt(dotsArr.length-1);
		// выбрать случайный елемент dotsArr[rBuffer1]
		int rBufferY=r.nextInt(dotsArr[rBufferX].length);
		double[] buffDot=dotsArr[rBufferX][rBufferY];
		// выбор случайной ноды
		int rBufferZ=r.nextInt(buffDot.length-1);

		dotsArr[rBufferX][rBufferY][rBufferZ]=0;
		blockedNodes.add(new Integer[] { rBufferX, rBufferY, rBufferZ });

	}

	/**change random node in (-MUTATION_MULTIPLIER ; MUTATION_MULTIPLIER) range */
	private void nodeMutate(Random r) {
		// выбрать случайный столбец dotsArr
		int rBuffer1=r.nextInt(dotsArr.length-1);
		// выбрать случайный елемент dotsArr[rBuffer1]
		int rBuffer2=r.nextInt(dotsArr[rBuffer1].length);
		double[] buffDot=dotsArr[rBuffer1][rBuffer2];
		// выбор слуйчайной связи
		int rBuffer3=r.nextInt(buffDot.length-1);
		double buffNode=buffDot[rBuffer3];// выбор случайной ноды
		// прибавить к весу случайной ноды случайное число (не больше
		// MUTATION_MULTIPLIER и не меньше -MUTATION_MULTIPLIER)
		dotsArr[rBuffer1][rBuffer2][rBuffer3]=buffNode+rnd(-MUTATION_MULTIPLIER,MUTATION_MULTIPLIER);
	}

	static double nodeUnlockMaxValue=0.2;
	/**Unlocks random blocked node from blockedNodes list*/
	private void nodeUnlock(Random r) {
		// выбрать случайную заблокированную Node
		int rBuff=(int) rnd(0,blockedNodes.size()-1);
		Integer[] nodeCoords=blockedNodes.get(rBuff);
		// установить вес разблокированого Node на случайное число
		dotsArr[nodeCoords[0]][nodeCoords[1]][nodeCoords[2]]=rnd(-nodeUnlockMaxValue,nodeUnlockMaxValue);
		// Удалить разблокированную Node из списка
		blockedNodes.remove(rBuff);
	}

	@Override
	public void kill() {
		super.kill();
		blockedNodes=null;
	}

	public static double rnd(double min, double max) {
		max-=min;
		return (Math.random()*++max)+min;
	}

	@Override
	public boolean getDontDelete() {
		return dontDelete;
	}

	@Override
	public void setDontDelete(boolean dontDelete) {
		this.dontDelete=dontDelete;
	}

	@Override
	public boolean getIsDead() {
		return isDead;
	}

	@Override
	public void setIsDead(boolean isDead) {
		this.isDead=isDead;
	}

}
