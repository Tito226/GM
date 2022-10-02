package MyVersion.Core;

import MyVersion.Frame.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static MyVersion.Core.Core_Config.LEARNING_RATE;
import static MyVersion.Core.Data_Set.rnd;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Network_Teacher {
    public static void fullTeach(Network student){
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
        teacher.randomize(student);
        for (int i = 0; i < 500000; i++) {
            teacher.teach(student,data_set);
        }
    }

    public static void main(String[] args) throws IOException {
        Network student=new Network(1);
        fullTeach(student);
        //TODO исправить ноды от ненужных точек(на которых не обучались ,они влияют на выход)
        System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggg:"+ student.evaluteFitness(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(1,6),rnd(3,100),0f,0f,0f,0f},false));

    }

    public  Network mainy() throws IOException {
        Network student=new Network(1);
        fullTeach(student);
        //TODO исправить ноды от ненужных точек(на которых не обучались ,они влияют на выход)
        System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggg:"+ student.evaluteFitness(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f},false));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(1,6),rnd(3,100),0f,0f,0f,0f},false));
        return student;
    }

    void randomize(Network student){
        Random r=new Random();
        for (ArrayList<Dot> dotM:student.dotsArr) {
            for(Dot dot:dotM) {
                for(Node node:dot.nodesFromMe){
                    node.setWeight( r.nextFloat());
                }
            }
        }
        int numberOfTeachDots=7;
        for (int i = student.dotsArr.get(0).size()-numberOfTeachDots; i >0; i--) {
            for (int j = 0; j < student.dotsArr.get(1).size(); j++) {
                student.dotsArr.get(0).get(i).nodesFromMe.get(j).setWeight(0);
                student.dotsArr.get(0).get(i).nodesFromMe.get(j).changeble=false;
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

        for(ArrayList<Dot> dotArr: student.dotsArr){
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

        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer correction
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

        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer correction
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-j)) {
                for (Node node: dot.nodesToMe) {
                    //nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate
                    node.setWeight(node.getWeight()-node.from.value*dot.weightsDelta*LEARNING_RATE);
                }
            }

        }

    }

}
