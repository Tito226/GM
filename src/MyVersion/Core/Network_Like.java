package MyVersion.Core;
import java.io.Serializable;
import java.util.Random;

public interface Network_Like extends Serializable {
	public void kill();
	public double[] calculateOutput(Double[] inputs,boolean forTeaching);
	public double[][][] returnStandanrtRepresentation();
	public ActivationFunctions getMyFunc();
	boolean getUsingBiasDots();
}
