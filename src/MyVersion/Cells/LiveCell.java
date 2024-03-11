package MyVersion.Cells;

import java.awt.Color;

import MyVersion.Cells.NormCell.DataMethods;

public interface LiveCell{
	DataMethods myMethods = null;
	int energy = 0;
	void step();
	void test();
	int getEnergy();
	Color getColor();
	void setY(int y);
	void setX(int x);
	void kill();
}