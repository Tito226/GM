package MyVersion.Core;

import MyVersion.Graphic_Builder.Graphic_Builder;
import MyVersion.NEAT.Pool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
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

public class Network_Teacher extends JPanel {
	public static int percent=0;

    /**
	 * 
	 */
	private static final long serialVersionUID = 123425L;
	public static float[] fullTeach(Network student){
		ExecutorService pool=Executors.newFixedThreadPool(2);
        float[] errors=new float[TEACH_ITERATIONS];/*TODO may change to long*/
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
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
            while(Network_Teacher.percent!=100) {
            	teacher.getGraphics().clearRect(0, 0, 50, 20);
            	teacher.getGraphics().setColor(Color.BLACK);
            	teacher.getGraphics().drawString( Network_Teacher.percent+"%", 7, 9);
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
            if(!(TEACH_ITERATIONS/2<i)){
                errors[i]=(float) teacher.teach(student,data_set,false);//teaches network and save error
            }else{
                errors[i]=(float) teacher.teach(student,data_set,true);
            } 
        }
        return errors;
    }
	/*TODO НЕ ИИСПОЛЬЗОВАТЬ (ЕТО НЕ ОСНОВНОЙ МЕТОД)*/
    public static float[] fullTeach(Network student,Data_Set dataSet){
    	Object[] errorsErr=new Object[4];
        float[] errors=new float[TEACH_ITERATIONS];
        Data_Set data_set=dataSet;
        Network_Teacher teacher=new Network_Teacher();
        teacher.randomize(student);
        int percent=100;
        for (int i = 0; i < TEACH_ITERATIONS; i++) {
        	if(i==117000) {
        		System.out.println(i);
        	}
        	if(percent/100f*TEACH_ITERATIONS==i) {
            	System.out.println(percent+"%");
            	System.out.println(i);
            	percent++;
            }
            if(!(TEACH_ITERATIONS/2<i)){
                errors[i]=(float) teacher.teach(student,data_set,false);//teaches network and save error
            }else{
                errors[i]=(float) teacher.teach(student,data_set,true);/*TODO ЗДЕЛАТЬ ФУНКЦИЮ УМЕНЬШЕНИЯ LEARNING RATE*/
            }
        }
        return errors;
    }

    public static void main(String[] args) throws IOException {
    	/*run this to test network teacher*/
        Network student=new Network(1);
        float[] errors= fullTeach(student);
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
                    student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(FIRST_LAYER_NODES_VALUE);
                    student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;//TODO delete it
                }
            }
        }	  if(BLOCK_USELESS_INPUTS){

                for (int i = student.dotsArr.get(0).size()-BIAS-1; i >HOW_MUCH_INPUTS_MUST_BE_USED; i--) {//Запрет на ввод данных из лишних точек
                    for (int j = 0; j < student.dotsArr.get(1).size()-BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(0d);
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;
                    }
                }
                	int it =9;
    }
    }
   
   
    /*TODO Розделять ошибки в зависимости от ожидаемого результата*/
    
    double teach(Network student,Data_Set data_set,boolean secondLRate){//Returns final dot error
    	/*TODO УБРАТЬ КОСТЫЛЬ*/
        Dot crutch=new Dot(Dot_Type.OUTPUT);
        for(ArrayList<Dot> dotArr: student.dotsArr){/*clear dots value (DON`T DELETE)*/
            for(Dot dot:dotArr){
                dot.clear();/*сброс данных точки(значение,ошибка,дельта весов)*/
            }
        }
        Random r=new Random();
        int i=r.nextInt(data_set.inOuts.size());//Choose random data from data_set
        ArrayList<Double[]> keys=new ArrayList(data_set.inOuts.keySet());/*TODO Сохранить массив вне метода для улучшения производительности*/
        student.evaluteFitness( keys.get(i),true);/*TODO  ПРИДУМАТЬ КАК УСКОРИТЬ, ПРИ БОЛЬШОМ ДАТАСЕТЕ СИЛЬЕО ЗАМЕДЛЯЕТСЯ*/
        //outputs correction
        int yy=0;
        for (Dot dot : student.dotsArr.get(student.dotsArr.size()-1)) {
            ArrayList<Double> expected = new ArrayList<>(Arrays.asList(data_set.inOuts.get( keys.get(i))));
            /*down :for more than one output, dont delete*/
            dot.error=dot.value -expected.get(yy);/*ФУНКЦИЯ ПОТЕРЬ */
            crutch=dot;
            dot.weightsDelta=dot.error* Dot.activationFunctionDX(dot.value);
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
                dot.weightsDelta= dot.error* Dot.activationFunctionDX(dot.value);

            }
          
        }
        Random rand=new Random();
        rand.nextFloat();
        //pool.;
        for (int j = 1; j < student.dotsArr.size()+1; j++) {//hidden layer correction ,счетчик обхода массива масивов с точками пропуская входной слой 
        	for (Dot dot : student.dotsArr.get(student.dotsArr.size()-j)) {//обход масива масивов с точками с конца,используя счетчик из внешнего цыкла,выбор массива с точками с конца и перебор всех точек
                for (Node node: dot.nodesToMe) {//обход всех связей точки
                	
                	double weight = 0;
                    
					if(!secondLRate){
                        weight=node.getWeight()-node.from.value*dot.weightsDelta*LEARNING_RATE* 
                        		(USE_R_WHILE_LEARNING==true ?rand.nextFloat() :1d);					/*использовать первый коеф. обучения*/
                    }else{
                        weight=node.getWeight()-node.from.value*dot.weightsDelta*SECOND_LEARNING_RATE*
                        		(USE_R_WHILE_LEARNING==true ?rand.nextFloat() :1d);					/*использовать второй коеф. обучения*/
                    }
                    if(weight>THRESHOLD_NODE_VALUE ||weight<-THRESHOLD_NODE_VALUE){//fixes gradient boom(very big node values that turns value into 1)//TODO ПЕРЕДЕЛАТЬ
                        weight=THRESHOLD_WEIGHT_RESET_VALUE;//установить число по умолчанию если число выходит за рамки
                        if(NODES_BECOMES_UNCHANGEBLE_IF_WEIGHT_BIGGER_THAN_THRESHOLD) {
                        node.setWeight(weight);
                        node.changeble=false;
                        continue;}
                    }
                    /*nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate*/
                    node.setWeight(weight);
                }
            }
        }
        
      

        
    return crutch.error;
    }
    /*TODO Тест не работает правильно */
    public static void suitabilityTest(Network student) {//tests network,ввыводит что на входе и на выходе и подходит ли ответ 
    	Double[] f =new Double[]{1d,rnd(ENERGY_NEEDED_TO_MULTIPLY,100),rnd(3,100),0d,0d,0d,0d};
    	double weightBuffer=student.evaluteFitness(f,false);
        String answer;
        Random r = new Random();
        if(weightBuffer<1f && weightBuffer>0.9f){
           answer=" passed";
        }else{
           answer=" failed";
        }
        System.out.println("Multiply:"+ weightBuffer+answer+"____________________"+Arrays.toString(f));//print evaluteFitness parametres
        
        f =new Double[]{0d,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3),0d,0d,0d,0d};
        weightBuffer=student.evaluteFitness(f,false);
        if(weightBuffer<0.125 && weightBuffer>0.1f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("Move up:"+weightBuffer+answer+"____________________"+Arrays.toString(f));
        
        f=new Double[]{0d,rnd(1,4),rnd(7,100),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)};
        weightBuffer=student.evaluteFitness(new Double[]{0d,rnd(1,4),rnd(7,100), (double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)},false);
        if(weightBuffer<0.3f && weightBuffer>0.2f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("Eat organic:"+weightBuffer+answer+"________________"+Arrays.toString(f));
        
        f=new Double[]{0d,rnd(4,30),rnd(1,4),1d,0d,0d,0d};
        weightBuffer=student.evaluteFitness(new Double[]{0d,rnd(4,30),rnd(1,4),1d,0d,0d,0d},false);
        if(weightBuffer>0.15f && weightBuffer<0.175f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("move left on up wall:"+weightBuffer+answer+"________"+Arrays.toString(f));
        
        f=new Double[]{0d,rnd(4,30),rnd(1,4),0d,0d,0d,1d};
        weightBuffer=student.evaluteFitness(new Double[]{0d,rnd(4,30),rnd(1,4),0d,0d,0d,1d},false);
        if(weightBuffer>0.125 && weightBuffer<0.15){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("move down on right wall:"+weightBuffer+answer+"____"+Arrays.toString(f));

    }

        void showResult(float[] errors){
            Graphic_Builder.createGraphic(errors);
        }


}
