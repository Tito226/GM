package MyVersion.Core;

import java.util.ArrayList;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Data_Set {
    int inputsNum=50;
    //комплект {inputs.get(i);outputs.get(i)}
    //          входы--------выходы
    public ArrayList<Float[]> inputs=new ArrayList<>();
    public ArrayList<Float[]> outputs=new ArrayList<>();

    public Data_Set(){

        for (int i = 0; i < inputsNum; i++) {
                inputs.add(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,200),rnd(3,100),0f,0f,0f,0f});
                outputs.add(new Float[]{rndF(0.9001f,1f)});


                inputs.add(new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f});
                outputs.add(new Float[]{rndF(0.1001f,0.2f)});

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
