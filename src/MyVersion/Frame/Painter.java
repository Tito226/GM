package MyVersion.Frame;

import static MyVersion.Frame.GM2_CONFIG.CELL_START_ORGANIC;
import static MyVersion.Frame.World.Restarts;
import static MyVersion.Frame.World.bestLifeTime;
import static MyVersion.Frame.World.cellSize;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.width;

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
	
	 public static void ecoPaint(Graphics g) {
		statPaint(g);
     	for (int i = 0; i < width; i++) {
             for (int j = 0; j < height; j++) {
                 if (cells[i][j] != null) {
                     if (cells[i][j].secCell == null && cells[i][j].partCell == null
                     		&& cells[i][j].organic!=CELL_START_ORGANIC) {
                         if(cells[i][j].isChange()){
                        	 g.setColor(cells[i][j].getColor());
                        	 g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        	 cells[i][j].setChange(false);
                         }
                     } else if (cells[i][j].secCell != null ) {
                         if(cells[i][j].isChange()){
                        	 g.setColor(cells[i][j].secCell.getColor());
                        	 g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        	 cells[i][j].setChange(false);
                         }
                     }
                     if (cells[i][j] != null && cells[i][j].partCell != null) {
                         if(cells[i][j].isChange()){
                        	 g.setColor(cells[i][j].partCell.getColor());
                        	 g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                        	 cells[i][j].setChange(false);
                         }
                     }
                 }
             }
         }
     	statPaint(g);
 	}

}
