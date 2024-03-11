package MyVersion.Frame;

public class GM2_CONFIG {
	//CELL SETTINGS
    public static final int CELL_START_ORGANIC=6;//6
	public static final int CELLS_ON_START=1000;
     //GENERAL
	public final static int WIDTH=1250;
    public final static int HEIGHT=700;
	public static final boolean AUTO_SIZE=true;
    public final static int CELL_SIZE=2;
    public final static int PAINT_MODE=4; //1-one Thread 2-two Threads 0-one tread for all 3-one thread fullPaint 4-one Thread combinedPaint
    public static final boolean DEBUG=false;//TODO DEBUG
    public static final boolean LOAD_SAVE=true;
    public static final boolean CREATE_SAVES=true;
    public final static int CREATE_SAVE_ON_LIFETIME=3000;
    public final static int LIMITED_ARRAY_SIZE=(int) (CELLS_ON_START/100*0.5d);
     //NORMCELL SETTINGS
    public static final boolean SIMPLE_DISTANCE=false;
    public static final boolean MULTI_THREAD_EVALUTE_FITNESS_METHOD=false;
    public static final int NUMBER_OF_MUTATIONS=5;/*Сколько нод мутируют при делении клетки(в конструкторе)*/
    public static final int MUTATION_CHANCE=10;//rand.nextInt(MUTATION_CHANCE)==0
    public static final int ENERGY_NEEDED_TO_MULTIPLY=30;//30
    public final static int ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST=10;//10
    public static final int NORM_CELL_START_ENERGY=15;//15
    public static final int PROTOPLAST_START_ENERGY=4;//4
    public static final int HOW_MUCH_ORGANIC_EATS_PER_STEP=3;//3
    //CELL SETTINGS
    public static final int ORGANIC_PER_CELL_ON_NORMCELL_DEATH=ENERGY_NEEDED_TO_MULTIPLY/9;
}
