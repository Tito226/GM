package MyVersion.Frame;

import static MyVersion.Core.Core_Config.BIAS;
import static MyVersion.Core.Core_Config.HOW_MUCH_INPUTS_MUST_BE_USED;
import static MyVersion.Core.Core_Config.MUTATION_MULTIPLIER;

import java.util.ArrayList;
import java.util.Random;

import MyVersion.Core.Dot;
import MyVersion.Core.Network;
import MyVersion.Core.Node;

public class NetworkWrapper extends Network {

	private static final long serialVersionUID = -9082519085580679388l;
	ArrayList<Node> blockedNodes;
	public boolean dontDelete=false;
	public boolean isDead=false;
	
	public NetworkWrapper() {
		
	}
	public NetworkWrapper(Network network) {
		this.dotsArr=network.dotsArr;
		this.myFunc=network.myFunc;
		blockedNodes=new ArrayList<Node>();
		for(Dot[] curDots : network.dotsArr) {
			if(curDots!=null) {
				for (Dot curDot : curDots) {
					for (Node curNode : curDot.nodesFromMe) {
						if(!curNode.changeble) {
							blockedNodes.add(curNode);
						}
					}
				}
			}
		}
		
	}
	 public void mutate(int numberOfMutations) {
		 Random r =new Random();
		 for(int i=0;i<r.nextInt(numberOfMutations);i++) {
			 nodeChange(r);
		 }
		 if(r.nextInt(2)==0) {
			 nodeUnlock(r);
		 }else if(r.nextInt(2)==0) {
			 nodeDeacticate(r);
		 }
	 }
	 private void nodeDeacticate(Random r) {
		 //выбрать случайный столбец dotsArr
		 int rBuffer1=r.nextInt(dotsArr.length-1);
		 //выбрать случайный елемент dotsArr[rBuffer1]
		 int rBuffer2=r.nextInt( dotsArr[rBuffer1].length);
		 Dot buffDot=dotsArr[rBuffer1][rBuffer2];
		 Node buffNode= buffDot.nodesFromMe.get(r.nextInt(buffDot.nodesFromMe.size()));//выбор случайной ноды
		 buffNode.setWeight(0);
		 buffNode.changeble=false;
		 blockedNodes.add(buffNode);
		 
	 }
	 
	 private void nodeChange(Random r) {
		//выбрать случайный столбец dotsArr
		 int rBuffer1=r.nextInt(dotsArr.length-1);
		 //выбрать случайный елемент dotsArr[rBuffer1]
		 int rBuffer2=r.nextInt( dotsArr[rBuffer1].length);
		 Dot buffDot=dotsArr[rBuffer1][rBuffer2];
		 Node buffNode= buffDot.nodesFromMe.get(r.nextInt(buffDot.nodesFromMe.size()));//выбор случайной ноды
		 //прибавить к весу случайной ноды случайное число (не больше MUTATION_MULTIPLIER и не меньше -MUTATION_MULTIPLIER)
		 buffNode.setWeight(buffNode.getWeight()+rnd(-MUTATION_MULTIPLIER,MUTATION_MULTIPLIER));
	 }
	 static double nodeUnlockMaxValue=0.2;
	 private void nodeUnlock(Random r) { 
		 //выбрать случайную заблокированную Node
		 int rBuff=(int) rnd(0,blockedNodes.size()-1);
		 Node nodeBuff=blockedNodes.get(rBuff);
		 //разрешить изменение Node
		 nodeBuff.changeble=true;
		 //установить вес разблокированого Node на случайное число
		 nodeBuff.setWeight(rnd(-nodeUnlockMaxValue,nodeUnlockMaxValue));;
		 //Удалить разблокированную Node из списка 
		 blockedNodes.remove(rBuff);
	 }
	 @Override
	 public void kill() {
		 super.kill();
		 blockedNodes=null;
	 }
	 public static double rnd(double min, double max){
			max -= min;
			return (Math.random() * ++max) + min;
	 }
	 
}
