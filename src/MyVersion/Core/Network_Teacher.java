package MyVersion.Core;

import MyVersion.Frame.World;
import MyVersion.Graphic_Builder.Graphic_Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        for (int i = 0; i < TEACH_ITERATIONS; i++) {
            errors[i]=teacher.teach(student,data_set);//teaches network and save error
        }
        return errors;
    }

    public static void main(String[] args) throws IOException {//run this to test network teacher
        Network student=new Network(1);
        float[] errors= fullTeach(student);
        suitabilityTest(student);
        Graphic_Builder.createGraphic(errors);
    }

    public  Network mainy() throws IOException {//must create and tune network
        Network student=new Network(1);
        fullTeach(student);
        suitabilityTest(student);
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
        //число точек на которые подается информация(не включая биос точки)
        //TODO проблема возникает в неправильно забракованых(false) нодах
        //бракуются не те ноды ,которые необходимо менять ,если запретить происходит магия(работает дольше ,результат неверный
        //и веса нод становятся огромными

        //всю ето парашуу еще и удалить нельзя ,выдает результат неверный
        if(SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE) {

            for (int i = student.dotsArr.get(0).size() - 1 - BIAS; i > 0; i--) {
                //TODO  -BIAS решает ошибку(IndexOutOfBoundsExeption(вызов елемента на 1 больше масива))
                for (int j = 0; j < student.dotsArr.get(1).size() - BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                    student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(FIRST_LAYER_NODES_VALUE);
                }
            }
        }else if(BLOCK_USELESS_INPUTS){

                for (int i = student.dotsArr.get(0).size()-BIAS-1; i >HOW_MUCH_INPUTS_MUST_BE_USED-BIAS-1; i--) {
                    for (int j = 0; j < student.dotsArr.get(1).size()-BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(0f);
                        student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;
                    }
                }

    }
    }
    // КОМЕНТАТОРЫ{
//Если я правильно помню, тот же Ng постоянно говорит, что корректировка весов должна производится одновременно.
// То есть скорректировать веса мы можем только после того, как посчитаем все ошибки.
// Иначе у нас на коррекцию предыдущего скрытого слоя появлияет коррекция текущего, хотя ошибка была получена при нескорректированном весе.


    //Оказалось всё просто, нужно складывать умножение дельты на вес по всем нейронам следующего слоя, с которыми есть связь у нейрона, для которого считается ошибка.
    //Но у меня пока что обучение не идёт, пытаюсь понять, что я упустил. Тут ещё вроде бы ничего не сказали про смещение. Оно обязательно?
    // Может быть, из-за того, что у меня оно не учтено, сеть не хочет учиться?


//Предположим, ты скорректировал веса, получил 2 (или более) разных ошибок (0.12, 0,15, 0,21), которые передаёшь на output.
// Далее мы просто вычисляем среднее арифметическое всех ошибок (0.12+0.15+0.21)/3 = 0,16 и работаем уже с данным значением.
//Может быть есть способ точнее, но так 100% будет работать.}

    // НА https://robocraft.ru/algorithm/560 НАПИСАНО ЧТО ОШИБИ НУЖНО СУММИРОВАТЬ
    float teach(Network student,Data_Set data_set){//Ruturns final dot error
        Dot crutch=new Dot(Dot_Type.OUTPUT);//TODO make it not crutch
        for(ArrayList<Dot> dotArr: student.dotsArr){//clear dots value (DON`T DELETE)
            for(Dot dot:dotArr){
                dot.clear();
            }
        }

        Random r=new Random();
        int i=r.nextInt(data_set.inputs.size());//Choose random data from data_set
        //System.out.println("ee:"+ student.evaluteFitness( data_set.inputs.get(i),true));
        student.evaluteFitness( data_set.inputs.get(i),true);
        //outputs correction
        int yy=0;
        for (Dot dot : student.dotsArr.get(student.dotsArr.size()-1)) {
            ArrayList<Float> expected = new ArrayList<>(Arrays.asList(data_set.outputs.get(i)));
            //down :for more than one output, dont delete
            dot.error=dot.value -expected.get(yy);
            crutch=dot;
            dot.weightsDelta=dot.error* Dot.activationFunctionDX(dot.value);
            yy++;
           // System.out.println("error:"+dot.error);
           // System.out.println("output:"+dot.getOutpup());
        }
//TODO  При более чем 2х слоях большая ошибка(3+) в первых слоях дот,изза чего данные превращаются в единицу,выяснить почему.
//TODO  В слоях ближе к концу такой проблемы нет,можно попробовать увеличить количество итераций обучения.
//TODO В нодах из первых дот огромные веса(10+),данные превращаются в еденицу
//TODO  Нужно оптимизировать обучение в многопоточность.
        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer calculation(error,weightsDelta),just calculation, not changing
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-(j+1))) {//dotsArr contains Arraylist that contains dots
                int counter=0;
                for (Node node:dot.nodesFromMe) {
                  dot.error+=node.getWeight()*node.to.weightsDelta;
                  counter++;
                }
                // dot.error=dot.error/counter;
                dot.weightsDelta= dot.error* Dot.activationFunctionDX(dot.value);
            }

        }
//TODO  на входном слое ноды имеют бешеные веса
        for (int j = 1; j < student.dotsArr.size()+1; j++) {//hidden layer correction
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-j)) {
                for (Node node: dot.nodesToMe) {
                    float weight=node.getWeight()-node.from.value*dot.weightsDelta*LEARNING_RATE;
                    if(weight>THRESHOLD_NODE_VALUE){//fixes gradient boom(very big node values that turns value into 1)
                        weight=THRESHOLD_NODE_VALUE;
                    }
                    //nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate
                    node.setWeight(weight);
                }
            }

        }
    return crutch.error;
    }

    static void suitabilityTest(Network student) {//testі network
        float weightBuffer=student.evaluteFitness(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f},false);
        String answer;
        if(weightBuffer<1f && weightBuffer>0.9f){
           answer=" passed";
        }else{
           answer=" failed";
        }
        System.out.println("Multiply:"+ weightBuffer+answer);
        weightBuffer=student.evaluteFitness(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f},false);
        if(weightBuffer<0.15 && weightBuffer>0.125f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("Move down:"+weightBuffer+answer);
        weightBuffer=student.evaluteFitness(new Float[]{0f,rnd(1,6),rnd(3,100),0f,0f,0f,0f},false);
        if(weightBuffer<0.3f && weightBuffer>0.2f){
            answer=" passed";
        }else{
            answer=" failed";
        }
        System.out.println("Eat organic:"+weightBuffer+answer);

    }

        void showResult(float[] errors){
            Graphic_Builder.createGraphic(errors);
        }

}
