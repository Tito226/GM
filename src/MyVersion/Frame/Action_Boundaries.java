package MyVersion.Frame;

public class Action_Boundaries {
	//Move
	
	/*public static final double[] moveUpBoundaries = {-0.9, -0.875};   // Передвинуты влево
	public static final double[] moveDownBoundaries = {-0.875, -0.85};
	public static final double[] moveLeftBoundaries = {-0.85, -0.825};
	public static final double[] moveRightBoundaries = {-0.825, -0.8};*/
	public static final double[] moveUpBoundaries = {-0.2, -0.15};  //-0.2;0
	public static final double[] moveDownBoundaries = {-0.15, -0.1};
	public static final double[] moveLeftBoundaries = {-0.1, -0.05};
	public static final double[] moveRightBoundaries = {-0.05, 0};
	//
	public static final double[] eatOrganicBoundaries = {0.2, 0.3};//{-0.3, -0.2}
	public static final double[] apoptosisBoundaries = {0.0, 0.05};   // Сдвинуто ближе к центру
	public static final double[] multiplyBoundaries = {0.1, 0.2};    // Размножение в более высоких значениях
	//Cell eat
	public static final double[] eatRightCellBoundaries = {0.4, 0.41}; // Все диапазоны смещены ближе к началу
	public static final double[] eatLeftCellBoundaries = {0.41, 0.42};
	public static final double[] eatUpCellBoundaries = {0.42, 0.43};
	public static final double[] eatDownCellBoundaries = {0.43, 0.44};
	
	//public static final double[] moveUpBoundaries= {0.1f,0.125f};
}
