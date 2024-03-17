package MyVersion.Cells;

import java.awt.Color;


public interface LiveCell{
	void step();
	void test();
	int getEnergy();
	Color getColor();
	void setY(int y);
	void setX(int x);
	void kill();
	Integer getGeneralEnergy();
	public Double[] getInputData();
	int getY();
	int getX();
	NormCell getHead();
	int getEnergyToMultiplyMe();
}