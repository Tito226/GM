package MyVersion.NEAT.com.evo.NEAT.config;

import java.io.Serializable;


public class NEAT_Config implements Serializable {

    public static final int INPUTS = 33;
    public static final int OUTPUTS = 1;
    public static final int HIDDEN_NODES = 15;//20
    public static final int POPULATION =58; //3

    public static final float COMPATIBILITY_THRESHOLD = 3;//5
    public static final float EXCESS_COEFFICENT = 2;//2
    public static final float DISJOINT_COEFFICENT = 2; //2
    public static final float WEIGHT_COEFFICENT = 0.1f; //0.4 //0.1 base

    public static final float STALE_SPECIES = POPULATION-POPULATION/4-2;


    public static final float STEPS = 0.16f;  //base 0.1f
    public static final float PERTURB_CHANCE = 0.9f;//0.9
    public static final float WEIGHT_CHANCE = 0.3f ;//0.3
    public static final float WEIGHT_MUTATION_CHANCE = 0.75f;
    public static final float NODE_MUTATION_CHANCE = 0.05f;//0.03
    public static final float CONNECTION_MUTATION_CHANCE = 0.09f; //0.05
    public static final float BIAS_CONNECTION_MUTATION_CHANCE = 0.2f;//0.15
    public static final float DISABLE_MUTATION_CHANCE = 0.4f;//0.4
    public static final float ENABLE_MUTATION_CHANCE = 0.2f ;//0.15
    public static final float CROSSOVER_CHANCE = 0.55f;//0.75

    public static final int STALE_POOL = POPULATION-POPULATION/4;
}
