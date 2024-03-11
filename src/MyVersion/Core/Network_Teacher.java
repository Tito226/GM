package MyVersion.Core;

import MyVersion.Frame.Action_Boundaries;
import MyVersion.Graphic_Builder.Graphic_Builder;
import MyVersion.NEAT.Pool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static MyVersion.Core.Core_Config.*;
import static MyVersion.Core.Data_Set.rnd;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
/*TODO сделать библиотекой*/
public class Network_Teacher extends JPanel {
	public static int percent=0;
	ArrayList<Double[]> keys;
	
	void setKeys(Data_Set dataSet){
		keys=new ArrayList<Double[]>(dataSet.dataSetInputsOutputs.keySet());
	}

	private static final long serialVersionUID = 123425L;
	
	public float[] fullTeach(Network student){
		//learningRateBuffer=LEARNING_RATE;
        float[] errors=new float[TEACH_ITERATIONS];/*TODO may change to long*/
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
        teacher.setKeys(data_set); 
        teacher.randomize(student);
        int percent=0;
        Runnable task = () -> {
        	 JFrame frame = new JFrame("Wait...");  
             JPanel controls = new JPanel();
             controls.setLayout(new GridLayout(2, 1));
             JPanel controls2 = new JPanel(); //справа будет панель с управлением
             frame.add(teacher, BorderLayout.CENTER);
             frame.setSize(300, 100);
             frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
             frame.setVisible(true);
             Graphics g=teacher.getGraphics();
             RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
             while(Network_Teacher.percent!=100) {
            	g.clearRect(0, 0, 200, 20);
            	g.drawString("| Uptime: " +mxBean.getUptime()/1000/60+":"+mxBean.getUptime()/1000%60+" |" , 107, 9);
            	g.setColor(Color.BLACK);
            	g.drawString( Network_Teacher.percent+"%", 7, 9);
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            } 
            frame.setVisible(false);
            frame=null;
            controls=null;
        };
    	Thread thread = new Thread(task);
    	thread.start();
        for (int i = 0; i < TEACH_ITERATIONS; i++) {
        	/*progress counter*/
        	if((int)(percent/100f*TEACH_ITERATIONS)==i  ) {
            	System.out.println(percent+"%");
            	percent++;
            	Network_Teacher.percent=percent;
            }	

        	errors[i]=(float) teacher.teach(student,data_set,i);
        }
        teacher.keys.clear();
        teacher.keys=null;
        data_set.remove();
        data_set=null;
        return errors;
    }
	
	
    public static void main(String[] args) throws IOException {
    	/*run this to test network teacher*/
    	
    	Network_Teacher nt=new Network_Teacher();
        Network student=new Network(1);
        float[] errors= nt.fullTeach(student);
        suitabilityTest(student);
        System.out.println("");
        System.out.println("");
        suitabilityTest(student);
        System.out.println("");
        System.out.println("");
        suitabilityTest(student);
        Graphic_Builder.createGraphic(errors);
    }
    public  Network createAndTeachNetwork() throws IOException {/*create and tune network*/
        Network student=new Network(1);
        float[] errors=fullTeach(student);
        suitabilityTest(student);
        Graphic_Builder.createGraphic(errors);
        return student;
    }

    void randomize(Network student){
        Random r=new Random();
        for (ArrayList<Dot> dotM:student.dotsArr) {
            for(Dot dot:dotM) {
                for(Node node:dot.nodesFromMe){
                	node.setWeight(r.nextFloat());
                }
            }
        }
        

        //всю ето парашуу еще и удалить нельзя ,выдает результат неверный
        if(SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE) {

            for (int i = student.dotsArr.get(0).size() - 1 - BIAS; i > 0; i--) {
                for (int j = 0; j < student.dotsArr.get(1).size() - BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                	Node curNode=student.dotsArr.get(0).get(i).nodesFromMe.get(j);
                	curNode.setWeight(FIRST_LAYER_NODES_VALUE);
                	curNode.changeble=false;//TODO delete it
                }
            }
        }	  if(BLOCK_USELESS_INPUTS){

                for (int i = student.dotsArr.get(0).size()-BIAS-1; i >=HOW_MUCH_INPUTS_MUST_BE_USED; i--) {//Запрет на ввод данных из лишних точек
                    for (int j = 0; j < student.dotsArr.get(1).size()-BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(0d);
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;
                    }
                }
                	int it =9;
    }
    }
   
   
    /*TODO Розделять ошибки в зависимости от ожидаемого результата*/
    

  
    double learningRateBuffer=LEARNING_RATE;//TODO зделать по нормальному
    
    double teach(Network student,Data_Set data_set,int iteration){//Returns final dot error
        Dot crutch=new Dot(Dot_Type.OUTPUT,func);
        for(ArrayList<Dot> dotArr: student.dotsArr){//clear dots value 
            for(Dot dot:dotArr){
                dot.clear();//сброс данных точки(значение,ошибка,дельта весов)
            }
        }
        
        Random r=new Random();
        int i=r.nextInt(data_set.dataSetInputsOutputs.size());//Choose random data from data_set
        
        student.evaluteFitness(this.keys.get(i),true);
        //outputs correction
        int yy=0;
        for (Dot dot : student.dotsArr.get(student.dotsArr.size()-1)) {
            ArrayList<Double> expected = new ArrayList<>(Arrays.asList(data_set.dataSetInputsOutputs.get( keys.get(i))));
            /*down :for more than one output, dont delete*/
            dot.error=dot.value -expected.get(yy);/*ФУНКЦИЯ ПОТЕРЬ */
            crutch=dot;
            dot.weightsDelta=dot.error* Dot.activationFunctionDX(dot.value,dot.myFunc);
            yy++;

        }
        /*Происходит взрыв градиента*/
        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer calculation(error,weightsDelta),just calculation, not changing
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-(j+1))) {//dotsArr contains Arraylist that contains dots
                int counter=0;/*счетчик для подсчета процента готовности*/
                for (Node node:dot.nodesFromMe) {                	
                	dot.error+=node.getWeight()*node.to.weightsDelta;
                	counter++;
                }
                // dot.error=dot.error/counter;
                dot.weightsDelta= dot.error* Dot.activationFunctionDX(dot.value,dot.myFunc);

            }
          
        }

        for (int j = 1; j < student.dotsArr.size()+1; j++) {//hidden layer correction ,счетчик обхода массива масивов с точками пропуская входной слой 
        	for (Dot dot : student.dotsArr.get(student.dotsArr.size()-j)) {//обход масива масивов с точками с конца,используя счетчик из внешнего цыкла,выбор массива с точками с конца и перебор всех точек
                for (Node node: dot.nodesToMe) {//обход всех связей точки
                	double weight = 0;       
                	
                	 weight=node.getWeight()-node.from.value*dot.weightsDelta* learningRateTimeFunction(iteration,learningRateBuffer); 
                	 learningRateBuffer=learningRateTimeFunction(iteration,learningRateBuffer);;
                	 if(weight>THRESHOLD_NODE_VALUE ||weight<-THRESHOLD_NODE_VALUE){//fixes gradient boom(very big node values that turns value into 1)//TODO ПЕРЕДЕЛАТЬ
                		 weight=THRESHOLD_WEIGHT_RESET_VALUE;//установить число по умолчанию если число выходит за рамки
                		 if(NODES_BECOMES_UNCHANGEBLE_IF_WEIGHT_BIGGER_THAN_THRESHOLD) {
                			 node.setWeight(weight);
                			 node.changeble=false;
                			 continue;
                		 }
                	 }
                    /*nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate*/
                    node.setWeight(weight);
                }
            }
        }
        double err=crutch.error;
        crutch=null;
        return err;
    }
    
    double learningRateTimeFunction(int iteration,double prevRate) {
    	return prevRate/1+FADING*iteration;
    }
    
    public static void suitabilityTest(Network student) {
        testAndPrint("Multiply__________________", Data_Set.getMultiplyTrainData()[0], Action_Boundaries.multiplyBoundaries, student);
        testAndPrint("Move up___________________", Data_Set.getMoveUpTrainData()[0], Action_Boundaries.moveUpBoundaries, student);
        testAndPrint("Eat organic0______________", Data_Set.getEatOrganicTrainData0()[0], Action_Boundaries.eatOrganicBoundaries, student);
        testAndPrint("Eat organic_______________", Data_Set.getEatOrganicTrainData()[0], Action_Boundaries.eatOrganicBoundaries, student);
        testAndPrint("Eat organic2______________", Data_Set.getEatOrganicTrainData2()[0], Action_Boundaries.eatOrganicBoundaries, student);
        testAndPrint("Move right on up wall_____", Data_Set.getMoveRightIfUpWallTrainData()[0], Action_Boundaries.moveRightBoundaries, student);
        testAndPrint("Move down on right wall___", Data_Set.getMoveDownOnRightWallTrainData()[0], Action_Boundaries.moveDownBoundaries, student);
        testAndPrint("Move Left If Down Wall____", Data_Set.getMoveLeftIfDownWallTrainData()[0], Action_Boundaries.moveLeftBoundaries, student);
        testAndPrint("Move Left If Down Wall2___", Data_Set.getMoveLeftIfDownWallTrainData2()[0], Action_Boundaries.moveLeftBoundaries, student);
        testAndPrint("Move Left If food_________", Data_Set.getMoveLeftIfFoodTrainData()[0], Action_Boundaries.moveLeftBoundaries, student);
        testAndPrint("Move Right If food________", Data_Set.getMoveRightIfFoodTrainData()[0], Action_Boundaries.moveRightBoundaries, student);
        testAndPrint("Move Up If food___________", Data_Set.getMoveUpIfFoodTrainData()[0], Action_Boundaries.moveUpBoundaries, student);
        testAndPrint("Move Down If food_________", Data_Set.getMoveDownIfFoodTrainData()[0], Action_Boundaries.moveDownBoundaries, student);
        testAndPrint("Move Up If food everywhere", Data_Set.getMoveUpIfFoodEverywhereTrainData()[0], Action_Boundaries.moveUpBoundaries, student);
    }

    private static void testAndPrint(String action, Double[] data, double[] boundaries, Network student) {
        double weightBuffer = student.evaluteFitness(data, false);
        String result = (weightBuffer > boundaries[0] && weightBuffer < boundaries[1]) ? "passed" : "failed";
        String toPrint=String.format("%s : %s %.6f (%.6f - %.6f) %s",action,result,weightBuffer,boundaries[0],boundaries[1] ,Arrays.toString(data));
        System.out.println(toPrint);
        //System.out.println(action + ": " + result+ " " +weightBuffer +" "+  +  " " + Arrays.toString(data));
    }

        void showResult(float[] errors){
            Graphic_Builder.createGraphic(errors);
        }


}
