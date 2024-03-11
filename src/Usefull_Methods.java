
public final class Usefull_Methods {

	public static double rnd(double min, double max){
		max -= min;
		return (Math.random() * ++max) + min;
	}
	
}
