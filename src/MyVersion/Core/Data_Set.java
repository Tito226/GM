package MyVersion.Core;

import java.util.ArrayList;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Data_Set {
    int inputsNum=5;
    public Data_Set(){
        for (int i = 0; i < 5; i++) {
            inputs.add(new Float[inputsNum]);
            outputs.add(new Float[inputsNum]);
        }
        inputs.set(0,new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f});
        outputs.set(0,new Float[]{rndF(0.1001f,0.2f)});
        for (int i = 0; i < 100; i++) {
            inputs.set(0,new Float[]{0f,rnd(6,ENERGY_NEEDED_TO_MULTIPLY),rnd(3,100),0f,0f,0f,0f});
            outputs.set(0,new Float[]{rndF(0.1001f,0.2f)});
        }
    }
   public ArrayList<Float[]> inputs=new ArrayList<>();
   public ArrayList<Float[]> outputs=new ArrayList<>();


    public static float rnd(int min, int max){
        max -= min;
        return (int) (Math.random() * max) + min;
    }
    public static float rndF(float min, float max){
        max -= min;
        return (float) ((Math.random() * max) + min);
    }
}
