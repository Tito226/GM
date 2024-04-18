package MyVersion.Cells;

import java.awt.Color;


public interface LiveCell{
	
	void idleEnergyDecrese();
	void step();
	void test();
	public default int getEnergy() {
		return getHead().getEnergy();
	}
	Color getColor();
	void setY(int y);
	void setX(int x);
	void kill(boolean spreadOrganic);
	default Integer getGeneralEnergy() {
		return getHead().getGeneralEnergy();
	}
	
	void apoptosis();
	public Double[] getInputData();
	int getY();
	int getX();
	NormCell getHead();
	int getEnergyToMultiplyMe();
	boolean getTested();
	void setTested(boolean value);
}