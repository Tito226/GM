package MyVersion.Frame;

public class Action_Boundaries {
	//Move
	public static final double[] moveUpBoundaries= {0.1f,0.125f};//(output>moveUpBoundaries[0] && output<moveUpBoundaries[1])
	public static final double[] moveDownBoundaries= {0.125f,0.15f};
	public static final double[] moveLeftBoundaries= {0.15f,0.175f};
	public static final double[] moveRightBoundaries= {0.175f,0.2f};
	//
	public static final double[] apoptosisBoundaries= {0.4f,0.45f};
	public static final double[] multiplyBoundaries= {0.9f,1f};
	public static final double[] eatOrganicBoundaries= {0.2,0.3};
	//Cell eat
	public static final double[] eatRightCellBoundaries= {0.6,0.61};
	public static final double[] eatLeftCellBoundaries= {0.61,0.62};
	public static final double[] eatUpCellBoundaries= {0.62,0.63};
	public static final double[] eatDownCellBoundaries= {0.63,0.64};
	//Multiply Protoplast
	public static final double[] multiplyProtoplastUpBoundaries= {0.64,0.65};
	public static final double[] multiplyProtoplastDownBoundaries= {0.65,0.66};
	public static final double[] multiplyProtoplastRightBoundaries= {0.66,0.67};
	public static final double[] multiplyProtoplastLeftBoundaries= {0.67,0.68};
	public static final double[] multiplyProtoplastBoundaries= {multiplyProtoplastUpBoundaries[0],multiplyProtoplastLeftBoundaries[1]};
	//Multiply Protoplast
	public static final double[] multiplyRootUpBoundaries= {0.68,0.69};
	public static final double[] multiplyRootDownBoundaries= {0.69,0.7};
	public static final double[] multiplyRootRightBoundaries= {0.7,0.71};
	public static final double[] multiplyRootLeftBoundaries= {0.71,0.72};
	public static final double[] multiplyRootBoundaries= {multiplyRootUpBoundaries[0],multiplyRootLeftBoundaries[1]};
	
	//public static final double[] moveUpBoundaries= {0.1f,0.125f};
}
