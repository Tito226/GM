package MyVersion.Frame;

public class Action_Boundaries {
	public static final double[] moveUpBoundaries= {0.1f,0.125f};//(output>moveUpBoundaries[0] && output<moveUpBoundaries[1])
	public static final double[] moveDownBoundaries= {0.125f,0.15f};
	public static final double[] moveLeftBoundaries= {0.15f,0.175f};
	public static final double[] moveRightBoundaries= {0.175f,0.2f};
	public static final double[] multiplyBoundaries= {0.9f,1f};
	public static final double[] eatOrganicBoundaries= {0.2,0.3};
	public static final double[] eatRightCellBoundaries= {0.6,0.61};
	public static final double[] eatLeftCellBoundaries= {0.61,0.62};
	public static final double[] eatUpCellBoundaries= {0.62,0.63};
	public static final double[] eatDownCellBoundaries= {0.63,0.64};
	
	//public static final double[] moveUpBoundaries= {0.1f,0.125f};
}
