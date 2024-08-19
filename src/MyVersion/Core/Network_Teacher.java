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
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static MyVersion.Core.Core_Config.*;
import static MyVersion.Core.Data_Set.rnd;
import static MyVersion.Frame.FRAME_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;
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
        Network student=new Network(ACTIVATION_FUNCTION);
        float[] errors= nt.fullTeach(student);
        SimplifiedNetwork student2=new SimplifiedNetwork(student.myFunc);
        student2.dotsArr=BrainCloneClass.convertFromBasicNetworkTo3DArray(student);
        suitabilityTest(student);
        for(int i=0;i<1;i++) {
        	 suitabilityTest(student2);
        	 //student2.print();
        	 System.out.println("");
        	 System.out.println("");
        }
        /*student.calculateOutput(new Double[]{0d,0d,0d}, false);
        student2.calculateOutput(new Double[]{0d,0d,0d}, false);
        System.out.println("------------------------------------------");
        student.printFull();
        System.out.println("------------------------------------------");
        student2.printFull();
        System.out.println("------------------------------------------");*/
        Graphic_Builder.createGraphic(errors);
    }
    public  Network createAndTeachNetwork() throws IOException {/*create and tune network*/
        Network student=new Network(ACTIVATION_FUNCTION);
        float[] errors=fullTeach(student);
        suitabilityTest(student);
        Graphic_Builder.createGraphic(errors);
        return student;
    }
    /**Creates new network with random node values*/
    public Network createRandomNetwork() {
    	Network student=new Network(ACTIVATION_FUNCTION);
    	randomize(student);
    	return student;
    }
    /**Sets random weight for every node*/
    void randomize(Network student){
        Random r=new Random();
        for (Dot[] dotM:student.dotsArr) {
            for(Dot dot:dotM) {
                for(Node node:dot.nodesFromMe){
                	if(r.nextBoolean()) {
                		node.setWeight(r.nextDouble());
                	}else {
                		node.setWeight(-r.nextDouble());
                	}
                	
                }
            }
        }
        

        //удалить нельзя ,выдает результат неверный
        if(SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE) {

            for (int i = student.dotsArr[0].length - 1 - BIAS; i > 0; i--) {
                for (int j = 0; j < student.dotsArr[1].length - BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                	Node curNode=student.dotsArr[0][i].nodesFromMe.get(j);
                	curNode.setWeight(FIRST_LAYER_NODES_VALUE);
                	curNode.changeble=false;//TODO delete it
                }
            }
        }	  if(BLOCK_USELESS_INPUTS){

                for (int i = student.dotsArr[0].length-BIAS-1; i >HOW_MUCH_INPUTS_MUST_BE_USED; i--) {//Запрет на ввод данных из лишних точек
                    for (int j = 0; j < student.dotsArr[1].length-BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                        student.dotsArr[0][i].nodesFromMe.get(j).setWeight(0d);
                        student.dotsArr[0][i].nodesFromMe.get(j).changeble=false;
                    }
                }
                	int it =9;
    }
    }
   
   
    /*TODO Розделять ошибки в зависимости от ожидаемого результата*/
    

  
    double learningRateBuffer=LEARNING_RATE;//TODO зделать по нормальному
    
    double teach(Network student,Data_Set data_set,int iteration){//Returns final dot error
    	Double[] errors=new Double[student.dotsArr[student.dotsArr.length-1].length];
        clearDots(student);
        
        Random r=new Random();
        int i=r.nextInt(data_set.dataSetInputsOutputs.size());//Choose random data from data_set
        Double[] curData=data_set.dataSetInputsOutputs.get( keys.get(i));
        student.calculateOutput(this.keys.get(i),true);
        //outputs correction
        double error=calculateOutputsCorrection(student,data_set,r,i);
        
        /*Происходит взрыв градиента*/
        for (int j = 1; j < student.dotsArr.length; j++) {//hidden layer calculation(error,weightsDelta),just calculation, not changing
            for (Dot dot : student.dotsArr[student.dotsArr.length-(j+1)]) {//dotsArr contains Arraylist that contains dots
                int counter=0;/*счетчик для подсчета процента готовности*/
                for (Node node:dot.nodesFromMe) {                	
                	dot.error+=node.getWeight()*node.to.weightsDelta;
                	counter++;
                }
                // dot.error=dot.error/counter;
                dot.weightsDelta= dot.error* ActivationFunctionUtils.activationFunctionDX(dot.value,dot.myFunc);

            }
          
        }

        for (int j = 1; j < student.dotsArr.length+1; j++) {//hidden layer correction ,счетчик обхода массива масивов с точками пропуская входной слой 
        	for (Dot dot : student.dotsArr[student.dotsArr.length-j]) {//обход масива масивов с точками с конца,используя счетчик из внешнего цыкла,выбор массива с точками с конца и перебор всех точек
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
        
        return error;
    }
    
    private void clearDots(Network student) {
    	for(Dot[] dotArr: student.dotsArr){//clear dots value 
            for(Dot dot:dotArr){
                dot.clear();//сброс данных точки(значение,ошибка,дельта весов)
            }
        }
    }
    
    double calculateOutputsCorrection(Network student,Data_Set data_set,Random r,int i) {
    	//outputs correction
        int counter=0;
        Double[] errors=new Double[student.dotsArr[student.dotsArr.length-1].length];
        ArrayList<Double> expected = new ArrayList<>(Arrays.asList(data_set.dataSetInputsOutputs.get( keys.get(i))));
        for (Dot dot : student.dotsArr[student.dotsArr.length-1]) {
            /*down :for more than one output, dont delete*/
            dot.error=dot.value -expected.get(counter);/*ФУНКЦИЯ ПОТЕРЬ */
            errors[counter]=dot.error;
            dot.weightsDelta=dot.error* ActivationFunctionUtils.activationFunctionDX(dot.value,dot.myFunc);
            counter++;
        }
        
        double error=0;
        for(Double d :errors) {
        	error+=d;
        }
        error=error/errors.length;
        return error;
    }
    
    double learningRateTimeFunction(int iteration,double prevRate) {
    	return prevRate/1+FADING*iteration;
    }
    /**Test network ability to do main things*/
    public static void suitabilityTest(Network_Like student) {
    	System.out.println("-------------------------------------------------------");
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
        System.out.println("-------------------------------------------------------");
    }

    private static void testAndPrint(String action, Double[] data, double[] boundaries, Network_Like student) {
        double weightBuffer = student.calculateOutput(data, false)[0];
        String result = (weightBuffer > boundaries[0] && weightBuffer < boundaries[1]) ? "passed" : "failed";
        String toPrint=String.format("%s : %s %.6f (%.6f - %.6f) %s",action,result,weightBuffer,boundaries[0],boundaries[1] ,Arrays.toString(data));
        System.out.println(toPrint);
        //System.out.println(action + ": " + result+ " " +weightBuffer +" "+  +  " " + Arrays.toString(data));
    }

        void showResult(float[] errors){
            Graphic_Builder.createGraphic(errors);
        }


}
