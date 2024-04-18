package MyVersion.Frame;

import static MyVersion.Frame.GM2_CONFIG.CELL_START_ORGANIC;
import static MyVersion.Frame.World.bestLifeTime;
import static MyVersion.Frame.World.cellSize;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.width;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import MyVersion.Cells.Cell;
import MyVersion.Cells.NormCell;

public class Painter implements ImageObserver {
	World world;
	byte paintCount=0;
	public  Painter(World world) {
		this.world=world;
	}
	
	public static void stringPaint(String str) {}
	
	public static void stringPaint(String[] strings) {}
	
	 public void ecoPaint() {
		try {
			 Graphics g=world.getGraphics();
			 world.cell_inf.paint(world.cell_inf.getGraphics());
			 world.inf.paint(world.inf.getGraphics());
			 for (int i = 0; i < width; i++) {
				 for (int j = 0; j < height; j++) {
					 Cell curCell=cells[i][j];
					 if (curCell != null && curCell.isChange()) { 
						 paint(g,i,j,curCell);
						 curCell.setChange(false);
					 }
				 }
        	}
     	
     	}catch(Exception e) {
     		System.err.println("paint eror");
     	}
		
 	}
	 BufferedImage buffer;
	 
	 public void fullPaint() {
		 Graphics g=world.getGraphics();
		 world.cell_inf.paint(world.cell_inf.getGraphics());
		 world.inf.paint(world.inf.getGraphics());
	        if (buffer == null || buffer.getWidth() != world.getWidth() || buffer.getHeight() != world.getHeight()) {
	        	try {
	        		// Пересоздайте буфер, если он не существует или его размер изменился
	        		buffer = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
	            }catch(Exception e) {
	            	
	            }
	        }
	        Graphics bufferGraphics = buffer.getGraphics();
	        // Отрисовка в буфер
	        try {
	        	for (int i = 0; i < width; i++) {
	            	for (int j = 0; j < height; j++) {
	                	if (cells[i][j] != null) {
	                		paint(bufferGraphics,i,j,cells[i][j]);
	                	}
	            	}
	        	}
	        }catch(Exception e ) {
	        	System.err.println("fullpaint error");
	        }
	        g.drawImage(buffer, 0, 0, this);
	        bufferGraphics.dispose();
	    }

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}
	
	void paint(Graphics g,int i,int j,Cell curCell) {
		 g.setColor(curCell.getColor());
		 g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
		 if(cellSize>=3) {
			 if(curCell.liveCell==null) {
			 	g.setColor(Color.LIGHT_GRAY);
			 	g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
			}else {
			 	g.setColor(Color.black);
			 	g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
		 	}
			if(curCell.selected || (curCell.liveCell!=null && curCell.liveCell instanceof NormCell &&  ((NormCell)curCell.liveCell).selected) ) {
				g.setColor(Color.red);
			 	g.drawOval(i * cellSize, j * cellSize, cellSize, cellSize);
			 	//g.drawOval(j, j, i, i);
			}
		 }
	}
	public void combinedPaint() {
		if(paintCount%2==0) {
			fullPaint();
		}else {
			ecoPaint();
		}
		paintCount++;
	}

}
