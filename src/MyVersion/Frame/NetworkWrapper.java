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
	ArrayList<Node> blockedNodes=new ArrayList();
	public boolean dontDelete=false;
	public boolean isDead=false;
	
	public NetworkWrapper() {
		
	}
	public NetworkWrapper(Network network) {
		this.dotsArr=network.dotsArr;
		this.myFunc=network.myFunc;
		for (int i = HOW_MUCH_INPUTS_MUST_BE_USED; i < dotsArr.get(0).size()-BIAS; i++) {
			Dot curDot=dotsArr.get(0).get(i);
			for (int j = 0; j < curDot.nodesFromMe.size(); j++) {
				blockedNodes.add(curDot.nodesFromMe.get(j));
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
		 int rBuffer1=r.nextInt(dotsArr.size()-1);
		 //выбрать случайный елемент dotsArr[rBuffer1]
		 int rBuffer2=r.nextInt( dotsArr.get(rBuffer1).size());
		 Dot buffDot=dotsArr.get(rBuffer1).get(rBuffer2);
		 Node buffNode= buffDot.nodesFromMe.get(r.nextInt(buffDot.nodesFromMe.size()));//выбор случайной ноды
		 buffNode.setWeight(0);
		 buffNode.changeble=false;
		 blockedNodes.add(buffNode);
		 
	 }
	 
	 private void nodeChange(Random r) {
		//выбрать случайный столбец dotsArr
		 int rBuffer1=r.nextInt(dotsArr.size()-1);
		 //выбрать случайный елемент dotsArr[rBuffer1]
		 int rBuffer2=r.nextInt( dotsArr.get(rBuffer1).size());
		 Dot buffDot=dotsArr.get(rBuffer1).get(rBuffer2);
		 Node buffNode= buffDot.nodesFromMe.get(r.nextInt(buffDot.nodesFromMe.size()));//выбор случайной ноды
		 //прибавить к весу случайной ноды случайное число (не больше MUTATION_MULTIPLIER и не меньше -MUTATION_MULTIPLIER)
		 buffNode.setWeight(buffNode.getWeight()+rnd(-MUTATION_MULTIPLIER,MUTATION_MULTIPLIER));
	 }
	 
	 private void nodeUnlock(Random r) { 
		 //выбрать случайную заблокированную Node
		 int rBuff=(int) rnd(0,blockedNodes.size()-1);
		 Node nodeBuff=blockedNodes.get(rBuff);
		 //разрешить изменение Node
		 nodeBuff.changeble=true;
		 //установить вес разблокированого Node на случайное число
		 nodeBuff.setWeight(rnd(-0.09,0.09));;
		 //Удалить разблокированную Node из списка 
		 blockedNodes.remove(rBuff);
	 }
	 
	 public static double rnd(double min, double max){
			max -= min;
			return (Math.random() * ++max) + min;
	 }
	 
}
