package MyVersion.Frame.Wrappers;

import java.util.Random;

import MyVersion.Core.ActivationFunctions;
import MyVersion.Core.FunctionChooseInterface;
import MyVersion.Core.Network_Like;

public interface NetworkWrapperLike extends Network_Like{
	public void mutate(int numberOfMutations);
	public double[] calculateOutput(Double[] inputData, boolean b);
	public void kill();
	public boolean getDontDelete();
	public void setDontDelete(boolean b);
	public boolean getIsDead();
	public void setIsDead(boolean b);
}
