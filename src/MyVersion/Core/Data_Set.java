package MyVersion.Core;

import MyVersion.Cells.Directions;
import MyVersion.Cells.DataMethods;
import MyVersion.Frame.Action_Boundaries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static MyVersion.Frame.GM2_CONFIG.*;

public class Data_Set {
    int inputsNum=90000;//200
    //комплект {inputs.get(i);outputs.get(i)}
    //          входы--------выходы
    static Random r = new Random();
    public HashMap<Double[],Double[]> dataSetInputsOutputs=new HashMap();

    public Data_Set(){

        for (int i = 0; i < inputsNum; i++) {
            //INPUTS:(isReadyToMultiply,energy,organic,upCell,downCell,leftCell,rightCell)
        	//TODO Сеть не различает 1 и 3 ,можно попробовать сменить входные данные
        		/*TODO ДОБАВИТЬ 7 ПАРАМЕТР*/
        	
        		dataSetInputsOutputs.put(getMultiplyTrainData()[0], getMultiplyTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveUpTrainData()[0],getMoveUpTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getEatOrganicTrainData0()[0],getEatOrganicTrainData0()[1]);
        		dataSetInputsOutputs.put(getEatOrganicTrainData()[0],getEatOrganicTrainData()[1]);
        		dataSetInputsOutputs.put(getEatOrganicTrainData2()[0],getEatOrganicTrainData2()[1]);
        		
        		//move down if right wall
        		/*dataSetInputsOutputs.put(getMoveDownIfRightWallTrainData()[0],getMoveDownIfRightWallTrainData()[1]);*/
                //move right on up wall
        		//dataSetInputsOutputs.put(getMoveRightOnUpWallTrainData()[0],getMoveRightOnUpWallTrainData()[1]);
       
        		dataSetInputsOutputs.put(getMoveDownOnRightWallTrainData()[0],getMoveDownOnRightWallTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveRightIfUpWallTrainData()[0],getMoveRightIfUpWallTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveLeftIfDownWallTrainData()[0],getMoveLeftIfDownWallTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveLeftIfDownWallTrainData2()[0],getMoveLeftIfDownWallTrainData2()[1]);
        		//----------------------------------------------------
        		dataSetInputsOutputs.put(getMoveLeftIfFoodTrainData()[0],getMoveLeftIfFoodTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveRightIfFoodTrainData()[0],getMoveRightIfFoodTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveUpIfFoodTrainData()[0],getMoveUpIfFoodTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveDownIfFoodTrainData()[0],getMoveDownIfFoodTrainData()[1]);
        		
        		dataSetInputsOutputs.put(getMoveUpIfFoodEverywhereTrainData()[0],getMoveUpIfFoodEverywhereTrainData()[1]);
        }

    }
    public Data_Set(HashMap<Float,Float> map) {
    	for (int i = 0; i < map.size(); i++) {
    		//inOuts.putAll(map);

        }

    }
    //INPUTS:(isReadyToMultiply,energy,organic,upCell,downCell,leftCell,rightCell)
    public static Double[][] getEatOrganicTrainData0() {
    	return new Double[][]{{0d,rnd(HOW_MUCH_ORGANIC_EATS_PER_STEP,ENERGY_NEEDED_TO_MULTIPLY),rnd(7,50)/DataMethods.organicDil,rndF(0.01,DataMethods.nextOrganicValue),rndF(0.01,DataMethods.nextOrganicValue),rndF(0.01,DataMethods.nextOrganicValue),rndF(0.01,DataMethods.nextOrganicValue),r.nextDouble()},
    	{(Action_Boundaries.eatOrganicBoundaries[1]+Action_Boundaries.eatOrganicBoundaries[0])/2}};
    } 
    
    public static Double[][] getEatOrganicTrainData() {
    	return new Double[][]{{0d,rnd(HOW_MUCH_ORGANIC_EATS_PER_STEP,ENERGY_NEEDED_TO_MULTIPLY),rnd(7,50)/DataMethods.organicDil,rndF(0.01,DataMethods.nextOrganicValue),rndF(0.01,DataMethods.nextOrganicValue),rndF(0.01,DataMethods.nextOrganicValue),rndF(0.01,DataMethods.nextOrganicValue),r.nextDouble()},
    	{(Action_Boundaries.eatOrganicBoundaries[1]+Action_Boundaries.eatOrganicBoundaries[0])/2}};
    } 
    public static Double[][] getEatOrganicTrainData2() {
    	return new Double[][]{{0d,rnd(1,ENERGY_NEEDED_TO_MULTIPLY),rnd(7,50)/DataMethods.organicDil,(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),r.nextDouble()},
    	{(Action_Boundaries.eatOrganicBoundaries[1]+Action_Boundaries.eatOrganicBoundaries[0])/2}};
    }

    public static Double[][] getMultiplyTrainData() {
    	return new Double[][]{{1d,rnd(ENERGY_NEEDED_TO_MULTIPLY+10,100),rnd(3,100)/DataMethods.organicDil,(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),(double) r.nextInt(2),r.nextDouble()},
    	{(Action_Boundaries.multiplyBoundaries[1]+Action_Boundaries.multiplyBoundaries[0])/2}};
    }
    
    public static Double[][] getMoveUpTrainData() {
    	return new Double[][]{{0d,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3)/DataMethods.organicDil,0d,0d,0d,0d,(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2d},
    	{(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2}};
    } 
    
    public static Double[][] getMoveDownOnRightWallTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,(double) r.nextInt(2),0d,0d,1d,(Action_Boundaries.moveRightBoundaries[1]+Action_Boundaries.moveRightBoundaries[0])/2},
    	{(Action_Boundaries.moveDownBoundaries[1]+Action_Boundaries.moveDownBoundaries[0])/2}};
    }
    public static Double[][] getMoveRightIfUpWallTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,1d,0d,0d,0d,(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2},
    	{(Action_Boundaries.moveRightBoundaries[1]+Action_Boundaries.moveRightBoundaries[0])/2}};
    }
    public static Double[][] getMoveLeftIfDownWallTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,0d,1d,0d,0d,(Action_Boundaries.moveLeftBoundaries[1]+Action_Boundaries.moveLeftBoundaries[0])/2},
    	{(Action_Boundaries.moveLeftBoundaries[1]+Action_Boundaries.moveLeftBoundaries[0])/2}};
    }
    
    public static Double[][] getMoveLeftIfDownWallTrainData2() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,0d,1d,0d,0d,(Action_Boundaries.moveDownBoundaries[1]+Action_Boundaries.moveDownBoundaries[0])/2},
    	{(Action_Boundaries.moveLeftBoundaries[1]+Action_Boundaries.moveLeftBoundaries[0])/2}};
    }
    //--------------------------------------------------------------------------------------------
    static double foodValue=CELL_START_ORGANIC/DataMethods.organicDil;
    //INPUTS:(isReadyToMultiply,energy,organic,upCell,downCell,leftCell,rightCell)
    public static Double[][] getMoveLeftIfFoodTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,0d,0d,foodValue,0d,(r.nextDouble())},
    	{(Action_Boundaries.moveLeftBoundaries[1]+Action_Boundaries.moveLeftBoundaries[0])/2}};
    }
    public static Double[][] getMoveRightIfFoodTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,0d,0d,0d,foodValue,(r.nextDouble())},
    	{(Action_Boundaries.moveRightBoundaries[1]+Action_Boundaries.moveRightBoundaries[0])/2}};
    }
    public static Double[][] getMoveUpIfFoodTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,foodValue,0d,0d,0d,(r.nextDouble())},
    	{(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2}};
    }
    public static Double[][] getMoveDownIfFoodTrainData() {
    	return new Double[][]{{0d,rnd(4,ENERGY_NEEDED_TO_MULTIPLY),rnd(1,4)/DataMethods.organicDil,0d,foodValue,0d,0d,0d,(r.nextDouble())},
    	{(Action_Boundaries.moveDownBoundaries[1]+Action_Boundaries.moveDownBoundaries[0])/2}};
    }
    public static Double[][] getMoveUpIfFoodEverywhereTrainData() {
    	return new Double[][]{{0d,rnd(3,ENERGY_NEEDED_TO_MULTIPLY),rnd(0,3)/DataMethods.organicDil,foodValue,foodValue,foodValue,foodValue,(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2d},
    	{(Action_Boundaries.moveUpBoundaries[1]+Action_Boundaries.moveUpBoundaries[0])/2}};
    } 
    public static double rnd(int min, int max){
        max -= min;
        return (int) (Math.random() * max) + min;
    }
    public static double rndF(double min, double max){
        max -= min;
        return (double) ((Math.random() * max) + min);
    }
    
    public void remove() {
    	dataSetInputsOutputs=null;
    }
    
    
}
