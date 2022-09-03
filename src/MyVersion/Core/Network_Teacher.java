package MyVersion.Core;

import java.util.ArrayList;
import java.util.Random;

public class Network_Teacher {


    public static void main(String[] args) {
        Network student=new Network();
        Network_Teacher teacher=new Network_Teacher(student);
        teacher.teach();
    }


    Network student;
    public Network_Teacher(Network student){
        this.student=student;
    }
    void teach(){
        Random r=new Random();

        for (ArrayList<Dot> dotM:student.dots) {
           for(Dot dot:dotM) {
               for(Node node:dot.nodesFromMe){
                   node.value1= r.nextFloat();
               }
           }
        }

        float[] inputs=new float[]{1,1,100};
        System.out.println(student.evaluteFitness(inputs));
    }

}
