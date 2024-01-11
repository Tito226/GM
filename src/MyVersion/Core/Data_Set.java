package MyVersion.Core;

import MyVersion.Frame.Directions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Data_Set {
    int inputsNum=100;//200
    //комплект {inputs.get(i);outputs.get(i)}
    //          входы--------выходы
    
    public HashMap<Double[],Double[]> inOuts=new HashMap();

    public Data_Set(){

        for (int i = 0; i < inputsNum; i++) {
            //INPUTS:(isReadyToMultiply,energy,organic,upCell,downCell,leftCell,rightCell)
        	//TODO Сеть не различает 1 и 3 ,можно попробовать сменить входные данные
        		Random r = new Random();
        		inOuts.put(new Double[]{1d,rnd(ENERGY_NEEDED_TO_MULTIPLY,100),rnd(3,100),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)}, new Double[]{0.95d});//Multiply
                inOuts.put(new Double[]{0d,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3),0d,0d,0d,0d},new Double[]{0.11d});//(output>0.1f && output<0.125f){move(Directions.Up);}
                inOuts.put(new Double[]{0d,rnd(1,4),rnd(7,100),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)}, new Double[]{0.25d});//(output>0.2 && output<0.3){eatOrganic();}
                inOuts.put(new Double[]{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4),(double) r.nextInt(2),0d,0d,1d}, new Double[]{0.13d});//move down if right wall
                ///inOuts.put(new Float[]{0f,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4),1f,0f,0f,0f}, new Float[]{0.16f});//move left on up wall
                //inOuts.put(new Float[]{0f,rnd(4,30),rnd(1,4),0f,0f,0f,1f}, new Float[]{0.635f});
                
                /*inOuts.put(new Float[]{0f,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3),0f,0f,0f,0f},new Float[]{0.11f});//(output>0.1f && output<0.125f){move(Directions.Up);}
                inOuts.put(new Float[]{1f,rnd(ENERGY_NEEDED_TO_MULTIPLY,100),rnd(3,100),0f,0f,0f,0f}, new Float[]{0.95f});//Multiply
                inOuts.put(new Float[]{0f,rnd(1,4),rnd(7,100),0f,0f,0f,0f}, new Float[]{0.25f});//(output>0.2 && output<0.3){eatOrganic();}
                inOuts.put(new Float[]{0f,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4),(float) r.nextInt(2),0f,0f,1f}, new Float[]{0.13f});//move down if right wall
                inOuts.put(new Float[]{0f,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4),1f,0f,0f,0f}, new Float[]{0.16f});//move left on up wall*/
        }

    }
    public Data_Set(HashMap<Float,Float> map) {
    	for (int i = 0; i < map.size(); i++) {
    		//inOuts.putAll(map);

        }

    }


    public static double rnd(int min, int max){
        max -= min;
        return (int) (Math.random() * max) + min;
    }
    public static float rndF(float min, float max){
        max -= min;
        return (float) ((Math.random() * max) + min);
    }
}
