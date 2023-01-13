package MyVersion.Core;

import MyVersion.Frame.Directions;

import java.util.ArrayList;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Data_Set {
    int inputsNum=10000;
    //комплект {inputs.get(i);outputs.get(i)}
    //          входы--------выходы
    public ArrayList<Float[]> inputs=new ArrayList<>();
    public ArrayList<Float[]> outputs=new ArrayList<>();

    public Data_Set(){

        for (int i = 0; i < inputsNum; i++) {
            //INPUTS:(isReadyToMultiply,energy,organic,upCell,downCell,leftCell,rightCell)
//TODO Попробовать уменьшить числа ,например поделить один на ето число,чтобы при суммировании на нейронах не получалась единица
                inputs.add(new Float[]{0f,rnd(10,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3),0f,0f,0f,0f});

                outputs.add(new Float[]{0.1f});//(output>0.125f && output<0.15f){move(Directions.DOWN);}

                inputs.add(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,100),rnd(3,100),0f,0f,0f,0f});
                outputs.add(new Float[]{0.95f});

                inputs.add(new Float[]{0f,rnd(1,6),rnd(7,100),0f,0f,0f,0f});
                outputs.add(new Float[]{0.26f});//(output>0.2 && output<0.3){eatOrganic();}

              //  inputs.add(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,500),rnd(3,100),0f,0f,0f,0f});
               // outputs.add(new Float[]{rndF(0.9001f,1f)});

        }

    }


    public static float rnd(int min, int max){
        max -= min;
        return (int) (Math.random() * max) + min;
    }
    public static float rndF(float min, float max){
        max -= min;
        return (float) ((Math.random() * max) + min);
    }
}
