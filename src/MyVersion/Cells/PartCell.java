package MyVersion.Cells;

import java.awt.Color;

interface PartCell{

void step(double output);
void test();
int getEnergy();
Color getColor();
void setY(int y);
void setX(int x);
}