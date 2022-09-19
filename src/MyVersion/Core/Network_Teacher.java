package MyVersion.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static MyVersion.Core.Core_Config.LEARNING_RATE;
import static MyVersion.Core.Data_Set.rnd;
import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Network_Teacher {


    public static void main(String[] args) {
        Network student=new Network(1);
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
        teacher.randomize(student);
        for (int i = 0; i < 20000; i++) {
            teacher.teach(student,data_set);
        }
        //TODO вывод непраавильный ,проверить акриваионную функцию в обучении
        System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggg:"+ student.evaluteFitness(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f}));
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy:"+student.evaluteFitness(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f}));
    }


    void randomize(Network student){
        Random r=new Random();
        for (ArrayList<Dot> dotM:student.dotsArr) {
            for(Dot dot:dotM) {
                for(Node node:dot.nodesFromMe){
                    node.weight = r.nextFloat();
                }
            }
        }
    }
    void teach(Network student,Data_Set data_set){

        for(ArrayList<Dot> dotArr: student.dotsArr){
            for(Dot dot:dotArr){
                dot.clear();
            }
        }

        Random r=new Random();
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

        int i=r.nextInt(data_set.inputs.size());
        student.evaluteFitness( data_set.inputs.get(i));

        //outputs correction
        int yy=0;
        for (Dot dot : student.dotsArr.get(student.dotsArr.size()-1)) {
            ArrayList<Float> expected = new ArrayList<>(Arrays.asList(data_set.outputs.get(i)));
            //down :for more than one output, dont delete
            dot.error=dot.value -expected.get(yy);
            dot.weightsDelta=dot.error*dot.activationFunctionDX(dot.value);
            yy++;
            System.out.println("error:"+dot.error);
            System.out.println("output:"+dot.getOutpup());
        }

        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer correction
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-(j+1))) {
                for (Node node:dot.nodesFromMe) {
                  dot.error+=node.weight*node.to.weightsDelta;
                }
                dot.weightsDelta= dot.error*dot.activationFunctionDX(dot.value);
            }

        }

        for (int j = 1; j < student.dotsArr.size(); j++) {//hidden layer correction
            for (Dot dot : student.dotsArr.get(student.dotsArr.size()-j)) {
                for (Node node: dot.nodesToMe) {
                    //nodes to dot weight correction  :  weight=weight-node_fromDot_value*weightsDelta*learning_rate
                    node.weight =node.weight-node.from.value*dot.weightsDelta*LEARNING_RATE;
                }
            }

        }



        Float[] inputs=new Float[]{1F,1f,100f};
        System.out.println(student.evaluteFitness(inputs));
    }

}
