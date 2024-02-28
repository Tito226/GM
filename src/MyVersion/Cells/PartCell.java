package MyVersion.Cells;

import java.awt.Color;

interface PartCell{
Integer[] myCoords = new Integer[2];
void step(float output);
void test();
int getEnergy();
Color getColor();
void setY(int y);
void setX(int x);
    int getXx();
    int getYy();
}