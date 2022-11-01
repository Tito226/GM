package MyVersion.Core;

public class Core_Config {
    //Network
    public static final int INPUTS =35;
    public static final int HIDDEN_DOTS =50;// MUST DIVIDE ON HIDDEN_DOTS_PER_MASSIVE
    public static final int OUTPUTS=1;
    public static final int BIAS=0;//MUST BE 0 OR 1
    public static final float BIAS_VALUE=0f;
    public static final float LEARNING_RATE =0.08F;//0.08
    //
    public static final int HIDDEN_DOTS_PER_ARRAY =25;

    //Realization parameters                                DON'T CHANGE IF YOU DON'T KNOW WHAT THEY DO
    public static final boolean BLOCK_USELESS_INPUTS=true;//TRUE
    public static final int HOW_MUCH_INPUTS_MUST_BE_USED=7;//7
}

