package MyVersion.Frame;

import static MyVersion.Frame.World.Restarts;
import static MyVersion.Frame.World.bestLifeTime;

import java.awt.Color;
import java.awt.Graphics;

public class Painter {
	public static void statPaint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Restarts: " + Restarts, 7, 9);
		g.drawString("Best Life Time: " + bestLifeTime, 7, 19);
	}
	
	public static void stringPaint(String str) {}
	
	public static void stringPaint(String[] strings) {}

}
