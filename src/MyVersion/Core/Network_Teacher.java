package MyVersion.Core;

import java.util.ArrayList;
import java.util.Random;

public class Network_Teacher {
float error;

    public static void main(String[] args) {
        Network student=new Network();
        Data_Set data_set=new Data_Set();
        Network_Teacher teacher=new Network_Teacher();
       teacher.teach(student,data_set);
    }


    void teach(Network student,Data_Set data_set){
        Random r=new Random();

        for (ArrayList<Dot> dotM:student.dots) {
           for(Dot dot:dotM) {
               for(Node node:dot.nodesFromMe){
                   node.value1= r.nextFloat();
               }
           }
        }
        int i=r.nextInt(data_set.inputs.size());
        student.evaluteFitness( data_set.inputs.get(i));


        Float[] inputs=new Float[]{1F,1f,100f};
        System.out.println(student.evaluteFitness(inputs));
    }

}
