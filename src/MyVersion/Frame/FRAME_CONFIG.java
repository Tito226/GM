package MyVersion.Frame;

public class FRAME_CONFIG {
	//CELL SETTINGS
    public static final int CELL_START_ORGANIC=6;//6
	public static final int CELLS_ON_START=7000;//900
	public static final int ORGANIC_ON_DAETH_RANGE=3;
    //GENERAL
	public final static int WIDTH=1250;
    public final static int HEIGHT=700;
	public static final boolean AUTO_SIZE=true;
    public final static int CELL_SIZE=6;//2 
    public final static int PAINT_MODE=3; //1-one Thread 2-two Threads 0-one tread for all 3-one thread fullPaint 4-one Thread combinedPaint
    public static final boolean DEBUG=false;//TODO DEBUG
    public final static int DEBUG_TIME_AFTER_STEP=500;
    public static final boolean LOAD_SAVE=false;
    public static final boolean CREATE_SAVES=false;
    public final static int CREATE_SAVE_ON_LIFETIME=4000;
    public final static int MIN_ARRAY_SIZE=1;
    public final static int RESTART_DELAY=500;
    public final static int SIZE_FORMULA=(CELLS_ON_START/100*7);
    public final static int LIMITED_ARRAY_SIZE=SIZE_FORMULA>MIN_ARRAY_SIZE ? SIZE_FORMULA : MIN_ARRAY_SIZE;
     //NORMCELL SETTINGS
    public final static int CRITICAL_ORGANIC_VALUE=600;//If Cell energy>CRITICAL_ORGANIC_VALUE live cell will die
    public static final boolean SIMPLE_DISTANCE=false;
    public static final boolean MULTI_THREAD_EVALUTE_FITNESS_METHOD=false;
    public static final int NORM_CELL_START_ENERGY=15;//15
    public static final int PROTOPLAST_START_ENERGY=4;//4
    public static final int HOW_MUCH_ORGANIC_EATS_PER_STEP=3;//3
    //Multiply energy
    public static final int ENERGY_NEEDED_TO_MULTIPLY=30;//30
    public final static int ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST=10;//10 
    public final static int ENERGY_NEEDED_TO_MULTIPLY_ROOT=10;//10 
    //CELL SETTINGS
    public static final int ORGANIC_PER_CELL_ON_NORMCELL_DEATH=ENERGY_NEEDED_TO_MULTIPLY/9;
    //Mutations 
    public static final int NUMBER_OF_MUTATIONS=1;/*Сколько нод мутируют при делении клетки(в конструкторе)*///5
    public static final int CHANCE_OF_NODE_DEACTIVATE=3;
    public static final int CHANCE_OF_NODE_ACTIVATE=3;
    public static final int MUTATION_CHANCE=4;//rand.nextInt(MUTATION_CHANCE)==0 10
    
    //Cell life time
    public static final int NORMCELL_MAX_LIFETIME=10000;
	public static final int PROTOPLAST_MAX_LIFETIME=1000;
	public static final int ROOT_MAX_LIFETIME=1000;
}
