package MyVersion.Core;

import MyVersion.Frame.Action_Boundaries;
import MyVersion.Frame.Directions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static MyVersion.Frame.GM2_CONFIG.ENERGY_NEEDED_TO_MULTIPLY;

public class Data_Set {
    int inputsNum=900;//200
    //комплект {inputs.get(i);outputs.get(i)}
    //          входы--------выходы
    static Random r = new Random();
    public HashMap<Double[],Double[]> dataSetInputsOutputs=new HashMap();

    public Data_Set(){

        for (int i = 0; i < inputsNum; i++) {
            //INPUTS:(isReadyToMultiply,energy,organic,upCell,downCell,leftCell,rightCell)
        	//TODO Сеть не различает 1 и 3 ,можно попробовать сменить входные данные
        		
        	
        		dataSetInputsOutputs.put(getMultiplyTrainData()[0], getMultiplyTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveUpTrainData()[0],getMoveUpTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getEatOrganicTrainData()[0],getEatOrganicTrainData()[1]);
        		//move down if right wall
        		dataSetInputsOutputs.put(getMoveDownIfRightWallTrainData()[0],getMoveDownIfRightWallTrainData()[1]);
                //move right on up wall
        		dataSetInputsOutputs.put(getMoveRightOnUpWallTrainData()[0],getMoveRightOnUpWallTrainData()[1]);
                //inOuts.put(new Float[]{0f,rnd(4,30),rnd(1,4),0f,0f,0f,1f}, new Float[]{0.635f});

        }

    }
    public Data_Set(HashMap<Float,Float> map) {
    	for (int i = 0; i < map.size(); i++) {
    		//inOuts.putAll(map);

        }

    }

    public static Double[][] getEatOrganicTrainData() {
    	return new Double[][]{{0d,rnd(1,ENERGY_NEEDED_TO_MULTIPLY),rnd(7,50),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)},
    	{(Action_Boundaries.eatOrganicBoundaries[1]+Action_Boundaries.eatOrganicBoundaries[0])/2}};
    } 

    public static Double[][] getMultiplyTrainData() {
    	return new Double[][]{{1d,rnd(ENERGY_NEEDED_TO_MULTIPLY+10,100),rnd(3,100),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2)},
    	{(Action_Boundaries.multiplyBoundaries[1]+Action_Boundaries.multiplyBoundaries[0])/2}};
    }
    
    public static Double[][] getMoveUpTrainData() {
    	return new Double[][]{{0d,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3),0d,0d,0d,0d},
    	{(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2}};
    } 
    
    public static Double[][] getMoveDownIfRightWallTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4),(double) r.nextInt(2),0d,0d,1d},
    	{(Action_Boundaries.moveDownBoundaries[1]+Action_Boundaries.moveDownBoundaries[0])/2}};
    } 
    
    public static Double[][] getMoveRightOnUpWallTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4),1d,0d,0d,0d},
    	{(Action_Boundaries.moveRightBoundaries[1]+Action_Boundaries.moveRightBoundaries[0])/2}};
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
