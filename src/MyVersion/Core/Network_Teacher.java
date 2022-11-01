package MyVersion.Core;

import MyVersion.Frame.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static MyVersion.Core.Core_Config.*;
import static MyVersion.Core.Data_Set.rnd;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Network_Teacher {
    public static void fullTeach(Network student){
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
        teacher.randomize(student);
        for (int i = 0; i < 990000; i++) {
            teacher.teach(student,data_set);
        }
    }

    public static void main(String[] args) throws IOException {
        Network student=new Network(1);
        fullTeach(student);
        System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggg:"+ student.evaluteFitness(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(1,6),rnd(3,100),0f,0f,0f,0f},false));

    }

    public  Network mainy() throws IOException {
        Network student=new Network(1);
        fullTeach(student);
        System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggg:"+ student.evaluteFitness(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(1,6),rnd(3,100),0f,0f,0f,0f},false));
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
        if(BLOCK_USELESS_INPUTS){

        for (int i = student.dotsArr.get(0).size()-BIAS-1; i >HOW_MUCH_INPUTS_MUST_BE_USED-BIAS-1; i--) {
            for (int j = 0; j < student.dotsArr.get(1).size()-BIAS; j++) {//"< student.dotsArr.get(1).size()-1" may be changed to "student.dotsArr.get(0).get(i).nodesFromMe.size()"
                student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(0);
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
    void teach(Network student,Data_Set data_set){

        for(ArrayList<Dot> dotArr: student.dotsArr){//clear dots value
            for(Dot dot:dotArr){
                dot.clear();
            }
        }

        Random r=new Random();
        int i=r.nextInt(data_set.inputs.size());
        //System.out.println("ee:"+ student.evaluteFitness( data_set.inputs.get(i),true));
        student.evaluteFitness( data_set.inputs.get(i),true);
        //outputs correction
        int yy=0;
        for (Dot dot : student.dotsArr.get(student.dotsArr.size()-1)) {
            ArrayList<Float> expected = new ArrayList<>(Arrays.asList(data_set.outputs.get(i)));
            //down :for more than one output, dont delete
            dot.error=dot.value -expected.get(yy);
            dot.weightsDelta=dot.error* Dot.activationFunctionDX(dot.value);
            yy++;
           // System.out.println("error:"+dot.error);
           // System.out.println("output:"+dot.getOutpup());
        }
//TODO  При более чем 2х слоях большая ошибка(3+) в первых слоях дот,изза чего данные превращаются в единицу,выяснить почему.
//TODO  В слоях ближе к концу такой проблемы нет,можно попробовать увеличить количество итераций обучения.
//TODO В нодах из первых дот огромные веса(10+),данные превращаются в еденицу
//TODO  Нужно оптимизировать обучение в многопоточность.
        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer calculation(error,weightsDelta)
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-(j+1))) {
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
                    if(node.changeble){
                    //nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate
                    float weight=

                            //скорее всего дельта имеет отрицательное большое значение ,значит проблема с ней и ее расчетом
                            // (насколько я заметил проблемы возникают в первом слое)
                            node.getWeight()-node.from.value*dot.weightsDelta*LEARNING_RATE;

                    node.setWeight(weight);
                    }
                }
            }

        }

    }

}
