package MyVersion.Core;

import MyVersion.Frame.World;
import MyVersion.Graphic_Builder.Graphic_Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static MyVersion.Core.Core_Config.*;
import static MyVersion.Core.Data_Set.rnd;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Network_Teacher {

    public static float[] fullTeach(Network student){
        float[] errors=new float[TEACH_ITERATIONS];//TODO may change to long
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
        teacher.randomize(student);
        int percent=0;
        for (int i = 0; i < TEACH_ITERATIONS; i++) {
        	
        	if((int)(percent/100f*TEACH_ITERATIONS)==i  ) {//this is progress counter
            	System.out.println(percent+"%");
            	percent++;
            }//else if(i>116998 && i<117003)
            	//System.out.println(percent/100f*TEACH_ITERATIONS);
            	
            if(!(TEACH_ITERATIONS/2<i)){
                errors[i]=teacher.teach(student,data_set,false);//teaches network and save error
            }else{
                errors[i]=teacher.teach(student,data_set,true);
            } 
        }
        return errors;
    }
    public static float[] fullTeach(Network student,Data_Set dataSet){//TODO НЕ ИИСПОЛЬЗОВАТЬ (ЕТО НЕ ОСНОВНОЙ МЕТОД)
    	Object[] errorsErr=new Object[4];
        float[] errors=new float[TEACH_ITERATIONS];
        //ArrayList errorsFromEatOrganic=new ArrayList();
        //ArrayList errorsFromMultiply=new ArrayList();
        //ArrayList errorsFromMoveDown=new ArrayList();
        //ArrayList errorsFromMoveDownOnLeftWall=new ArrayList();
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
                errors[i]=teacher.teach(student,data_set,false);//teaches network and save error
            }else{
                errors[i]=teacher.teach(student,data_set,true);    
            }
        }
        return errors;
    }

    public static void main(String[] args) throws IOException {//run this to test network teacher
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

    public  Network createAndTeachNetwork() throws IOException {//must create and tune network
        Network student=new Network(1);
        float[] errors=fullTeach(student);
        suitabilityTest(student);
        Graphic_Builder.createGraphic(errors);
        return student;
    }

    void randomize(Network student){
        //попробовать выставлять не случайные числа
        Random r=new Random();
        for (ArrayList<Dot> dotM:student.dotsArr) {
            for(Dot dot:dotM) {
                for(Node node:dot.nodesFromMe){
                	node.setWeight(r.nextFloat());// node.setWeight( r.nextFloat());
                }
            }
        }
        /*число точек на которые подается информация(не включая биос точки)
        //TODO проблема возникает в неправильно забракованых(false) нодах
        бракуются не те ноды ,которые необходимо менять ,если запретить происходит магия(работает дольше ,результат неверный
        и веса нод становятся огромными*/

        //всю ето парашуу еще и удалить нельзя ,выдает результат неверный
        if(SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE) {

            for (int i = student.dotsArr.get(0).size() - 1 - BIAS; i > 0; i--) {
                //TODO  -BIAS решает ошибку(IndexOutOfBoundsExeption(вызов елемента на 1 больше масива))
                for (int j = 0; j < student.dotsArr.get(1).size() - BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                    student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(FIRST_LAYER_NODES_VALUE);
                    student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;//TODO delete it
                }
            }
        }	  if(BLOCK_USELESS_INPUTS){

                for (int i = student.dotsArr.get(0).size()-BIAS-1; i >HOW_MUCH_INPUTS_MUST_BE_USED; i--) {//Запрет на ввод данных из лишних точек
                    for (int j = 0; j < student.dotsArr.get(1).size()-BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(0f);
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;
                    }
                }
                	int it =9;
    }
    }
   
    
    
    /*ERROR_FROM errorFrom(float weightBuffer) {
    	if(weightBuffer<1f && weightBuffer>0.9f){
    		return ERROR_FROM.EAT_ORGANIC;
         }       
         if(weightBuffer<0.15 && weightBuffer>0.125f){
            return ERROR_FROM.MOVE_DOWN;
         }         
         if(weightBuffer<0.3f && weightBuffer>0.2f){
        	 return ERROR_FROM.EAT_ORGANIC;
         }         
         if(weightBuffer>0.15f && weightBuffer<0.175f){
        	 return ERROR_FROM.MOVE_LEFT_ON_UP_WALL;
         }         
         if(weightBuffer>0.63 && weightBuffer<0.64){
        	 return ERROR_FROM.MOVE_DOWN_ON_RIGHT_WALL;
         }
         return null;
    }*/
    
    //TODO Розделять ошибки в зависимости от ожидаемого результата
    
    
    float teach(Network student,Data_Set data_set,boolean secondLRate){//Returns final dot error
        Dot crutch=new Dot(Dot_Type.OUTPUT);//TODO УБРАТЬ КОСТЫЛЬ
        for(ArrayList<Dot> dotArr: student.dotsArr){//clear dots value (DON`T DELETE)
            for(Dot dot:dotArr){
                dot.clear();//сброс данных точки(значение,ошибка,дельта весов)
            }
        }
        Random r=new Random();
        int i=r.nextInt(data_set.inOuts.size());//Choose random data from data_set
        /*System.out.println("ee:"+ student.evaluteFitness( data_set.inputs.get(i),true));
        /*Random r=new Random();
        int i=r.nextInt(data_set.inOuts.size());//Choose random data from data_set
        //System.out.println("ee:"+ student.evaluteFitness( data_set.inputs.get(i),true));
        Iterator itr = data_set.inOuts.entrySet().iterator();
        long count=-1;
        Map.Entry<Float[],Float[]> entry=null ;
        while(itr.hasNext()) {//выбор случайного елемента
        	count++;
        	if(count!=i)
        		continue;
        	else {
        		entry =  (Entry<Float[], Float[]>) itr.next();
        		break;
        		}
        }
       
        if(entry==null)
        	throw new Exception("ENTRY WITH DATASET VALUES IS NULL");
        */
        ArrayList<Float[]> keys=new ArrayList(data_set.inOuts.keySet());//TODO Сохранить массив вне метода для улучшения производительности
        student.evaluteFitness( keys.get(i),true);//TODO  ПРИДУМАТЬ КАК УСКОРИТЬ, ПРИ БОЛЬШОМ ДАТАСЕТЕ СИЛЬЕО ЗАМЕДЛЯЕТСЯ
        //outputs correction
        int yy=0;
        for (Dot dot : student.dotsArr.get(student.dotsArr.size()-1)) {
            ArrayList<Float> expected = new ArrayList<>(Arrays.asList(data_set.inOuts.get( keys.get(i))));
            //down :for more than one output, dont delete
            dot.error=dot.value -expected.get(yy);//TODO ФУНКЦИЯ ПОТЕРЬ 
            crutch=dot;
            dot.weightsDelta=dot.error* Dot.activationFunctionDX(dot.value);
            yy++;
           // System.out.println("error:"+dot.error);
           // System.out.println("output:"+dot.getOutpup());
        }
/*TODO  При более чем 2х слоях большая ошибка(3+) в первых слоях дот,изза чего данные превращаются в единицу,выяснить почему.
  		В слоях ближе к концу такой проблемы нет.
  		Нужно оптимизировать обучение в многопоточность.*/
        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer calculation(error,weightsDelta),just calculation, not changing
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-(j+1))) {//dotsArr contains Arraylist that contains dots
                int counter=0;//счетчик для подсчета процента готовности
                for (Node node:dot.nodesFromMe) {
                  dot.error+=node.getWeight()*node.to.weightsDelta;
                  counter++;
                }
                // dot.error=dot.error/counter;
                dot.weightsDelta= dot.error* Dot.activationFunctionDX(dot.value);
            }

        }

        for (int j = 1; j < student.dotsArr.size()+1; j++) {//hidden layer correction ,счетчик обхода массива масивов с точками пропуская входной слой 
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-j)) {//обход масива масивов с точками с конца,используя счетчик из внешнего цыкла,выбор массива с точками с конца и перебор всех точек
                for (Node node: dot.nodesToMe) {//обход всех связей точки
                    float weight = 0;
                    if(!secondLRate){
                        weight=node.getWeight()-node.from.value*dot.weightsDelta*LEARNING_RATE;//использовать первый коеф. обучения
                    }else{
                        weight=node.getWeight()-node.from.value*dot.weightsDelta*SECOND_LEARNING_RATE;//использовать второй коеф. обучения
                    }
                    if(weight>THRESHOLD_NODE_VALUE ||weight<-THRESHOLD_NODE_VALUE){//fixes gradient boom(very big node values that turns value into 1)//TODO ПЕРЕДЕЛАТЬ
                        weight=THRESHOLD_WEIGHT_RESET_VALUE;//установить число по умолчанию если число выходит за рамки
                        if(NODES_BECOMES_UNCHANGEBLE_IF_WEIGHT_BIGGER_THAN_THRESHOLD) {
                        node.setWeight(weight);
                        node.changeble=false;
                        continue;}
                    }
                    //nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate
                    node.setWeight(weight);
                }
            }

        }
        
    return crutch.error;
    }
    //TODO Тест не работает правильно 
    public static void suitabilityTest(Network student) {//tests network,ввыводит что на входе и на выходе и подходит ли ответ 
    	Float[] f =new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,100),rnd(3,100),0f,0f,0f,0f};
        float weightBuffer=student.evaluteFitness(f,false);
        String answer;
        Random r = new Random();
        if(weightBuffer<1f && weightBuffer>0.9f){
           answer=" passed";
        }else{
           answer=" failed";
        }
        System.out.println("Multiply:"+ weightBuffer+answer+"____________________"+Arrays.toString(f));//print evaluteFitness parametres
        
        f =new Float[]{0f,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3),0f,0f,0f,0f};
        weightBuffer=student.evaluteFitness(f,false);
        if(weightBuffer<0.125 && weightBuffer>0.1f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("Move up:"+weightBuffer+answer+"____________________"+Arrays.toString(f));
        
        f=new Float[]{0f,rnd(1,4),rnd(7,100),(float) r.nextInt(2),(float) r.nextInt(2),(float) r.nextInt(2),(float) r.nextInt(2)};
        weightBuffer=student.evaluteFitness(new Float[]{0f,rnd(1,4),rnd(7,100),(float) r.nextInt(2),(float) r.nextInt(2),(float) r.nextInt(2),(float) r.nextInt(2)},false);
        if(weightBuffer<0.3f && weightBuffer>0.2f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("Eat organic:"+weightBuffer+answer+"________________"+Arrays.toString(f));
        
        f=new Float[]{0f,rnd(4,30),rnd(1,4),1f,0f,0f,0f};
        weightBuffer=student.evaluteFitness(new Float[]{0f,rnd(4,30),rnd(1,4),1f,0f,0f,0f},false);
        if(weightBuffer>0.15f && weightBuffer<0.175f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("move left on up wall:"+weightBuffer+answer+"________"+Arrays.toString(f));
        
        f=new Float[]{0f,rnd(4,30),rnd(1,4),0f,0f,0f,1f};
        weightBuffer=student.evaluteFitness(new Float[]{0f,rnd(4,30),rnd(1,4),0f,0f,0f,1f},false);
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
