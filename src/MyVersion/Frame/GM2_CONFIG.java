package MyVersion.Frame;

public class GM2_CONFIG {
     //GENERAL
    public final static int CELL_SIZE=2;
    public final static int PAINT_MODE=3; //1-one Thread 2-two Threads 0-one tread for all
    public static final boolean DEBUG=false;
    public static final boolean TEST_RUN=false;
    public static final boolean CREATE_SAVES=true;
    public final static int CREATE_SAVE_ON_LIFETIME=6000;
     //NORMCELL SETTINGS
    public static final boolean MULTI_THREAD_EVALUTE_FITNESS_METHOD=false;
    public static final int NUMBER_OF_MUTATIONS=7;/*Сколько нод мутируют при делении клетки(в конструкторе)*/
    public static final int MUTATION_CHANCE=90;//rand.nextInt(MUTATION_CHANCE)==1
    public static final int ENERGY_NEEDED_TO_MULTIPLY=30;//30
    public final static int ENERGY_NEEDED_TO_MULTIPLY_PROTOPLAST=10;//10
    public static final int NORM_CELL_START_ENERGY=15;//15
    public static final int PROTOPLAST_START_ENERGY=4;//4
    public static final int HOW_MUCH_ORGANIC_EATS_PER_STEP=3;//3
    //CELL SETTINGS
    public static final int ORGANIC_PER_CELL_ON_NORMCELL_DEATH=5;
    public static final int CELL_START_ORGANIC=6;//6
	public static final int CELLS_ON_START=250;
}
/*TODO создать тестовый режим*/