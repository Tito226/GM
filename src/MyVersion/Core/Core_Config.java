package MyVersion.Core;

public class Core_Config {
    //Network
    public static final int INPUTS =35;
    public static final int HIDDEN_DOTS=150;// MUST DIVIDE ON HIDDEN_DOTS_PER_MASSIVE
    public static final int OUTPUTS=1;
    public static final int BIAS=1;//MUST BE 0 OR 1
    public static final double BIAS_VALUE=-6.5d;//Value of bias dots//-7 -6
    public static final double LEARNING_RATE =0.001001d;//0.0009
    public static final double SECOND_LEARNING_RATE=0.000000007d;//0.000007
    public static final int HIDDEN_DOTS_PER_ARRAY =25;//25
    public static final boolean USE_R_WHILE_LEARNING=false;
    
    public static final int TEACH_ITERATIONS=800000;//800000

    //Realization parameters                                DON'T CHANGE IF YOU DON'T KNOW WHAT THEY DO
    public static final boolean BLOCK_USELESS_INPUTS=true;//TRUE
    public static final int HOW_MUCH_INPUTS_MUST_BE_USED=7;//7
    public static final double THRESHOLD_NODE_VALUE=1.91d;//2.01 Nodes weight can't become bigger than this,1.41
    public static final boolean SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE=false;//Has more priority than BLOCK_USELESS_INPUTS, rules it out
    public static final double FIRST_LAYER_NODES_VALUE=-0.15d;//-0.15f
    public static final double THRESHOLD_WEIGHT_RESET_VALUE=0.07d;
    public static final boolean NODES_BECOMES_UNCHANGEBLE_IF_WEIGHT_BIGGER_THAN_THRESHOLD=false;
    public static final boolean FIRST_INPUT_MULTIPLIER=false;//true
}

/* //Network
    public static final int INPUTS =35;
    public static final int HIDDEN_DOTS=150;// MUST DIVIDE ON HIDDEN_DOTS_PER_MASSIVE
    public static final int OUTPUTS=1;
    public static final int BIAS=1;//MUST BE 0 OR 1
    public static final float BIAS_VALUE=-8f;//Value of bias dots//-7
    public static final float LEARNING_RATE =0.0006F;//0.0009
    public static final float SECOND_LEARNING_RATE=0.000007f;//0.000007
    //
    public static final int HIDDEN_DOTS_PER_ARRAY =25;

    public static final int TEACH_ITERATIONS=1500000;//800000

    //Realization parameters                                DON'T CHANGE IF YOU DON'T KNOW WHAT THEY DO
    public static final boolean BLOCK_USELESS_INPUTS=true;//TRUE
    public static final int HOW_MUCH_INPUTS_MUST_BE_USED=7;//7
    public static final float THRESHOLD_NODE_VALUE=2.09F;//Nodes weight can't become bigger than this
    public static final boolean SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE=false;//Has more priority than BLOCK_USELESS_INPUTS, rules it out
    public static final float FIRST_LAYER_NODES_VALUE=-0.15f;//-0.15f
    public static final float THRESHOLD_WEIGHT_RESET_VALUE=0.07f;*/

/*//Network
    public static final int INPUTS =35;
    public static final int HIDDEN_DOTS =100;// MUST DIVIDE ON HIDDEN_DOTS_PER_MASSIVE
    public static final int OUTPUTS=1;
    public static final int BIAS=1;//MUST BE 0 OR 1
    public static final float BIAS_VALUE=-1f;//Value of bias dots
    public static final float LEARNING_RATE =0.2F;//0.08
    public static final float SECOND_LEARNING_RATE=0.002f;//0.005
    //
    public static final int HIDDEN_DOTS_PER_ARRAY =50;

    public static final int TEACH_ITERATIONS=900000;

    //Realization parameters                                DON'T CHANGE IF YOU DON'T KNOW WHAT THEY DO
    public static final boolean BLOCK_USELESS_INPUTS=true;//TRUE
    public static final int HOW_MUCH_INPUTS_MUST_BE_USED=7;//7
    public static final float THRESHOLD_NODE_VALUE=2.3F;//Nodes weight can't become bigger than this
    public static final boolean SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE=false;//Has more priority than BLOCK_USELESS_INPUTS, rules it out
    public static final float FIRST_LAYER_NODES_VALUE=-0.15f;//-0.15f*/



/*public static final int INPUTS =35;
    public static final int HIDDEN_DOTS=150;// MUST DIVIDE ON HIDDEN_DOTS_PER_MASSIVE
    public static final int OUTPUTS=1;
    public static final int BIAS=1;//MUST BE 0 OR 1
    public static final float BIAS_VALUE=-8f;//Value of bias dots//-7
    public static final float LEARNING_RATE =0.001F;//0.0009
    public static final float SECOND_LEARNING_RATE=0.0007f;//0.005
    //
    public static final int HIDDEN_DOTS_PER_ARRAY =25;

    public static final int TEACH_ITERATIONS=800000;//800000

    //Realization parameters                                DON'T CHANGE IF YOU DON'T KNOW WHAT THEY DO
    public static final boolean BLOCK_USELESS_INPUTS=true;//TRUE
    public static final int HOW_MUCH_INPUTS_MUST_BE_USED=7;//7
    public static final float THRESHOLD_NODE_VALUE=2.01F;//Nodes weight can't become bigger than this
    public static final boolean SET_FIRST_LAYER_NODES_NON_RANDOM_VALUE=false;//Has more priority than BLOCK_USELESS_INPUTS, rules it out
    public static final float FIRST_LAYER_NODES_VALUE=-0.15f;//-0.15f
}*/