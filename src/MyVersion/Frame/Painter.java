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

public class Painter implements ImageObserver {
	World world;
	byte paintCount=0;
	public  Painter(World world) {
		this.world=world;
	}
	
	public static void stringPaint(String str) {}
	
	public static void stringPaint(String[] strings) {}
	
	 public void ecoPaint(InfoPanel inf) {
		try {/*TODO FIX MULTI THREAD PAINT EROR(SEC CELL==NULL),иногда остается "тень" зеленой клетки */
			 Graphics g=world.getGraphics();
			 inf.paint(inf.getGraphics());
			 for (int i = 0; i < width; i++) {
				 for (int j = 0; j < height; j++) {
					 Cell curCell=cells[i][j];
					 if (curCell != null && curCell.isChange()) { 
						 g.setColor(curCell.getColor());
						 g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
						 curCell.setChange(false);
					 }
				 }
        	}
     	
     	}catch(Exception e) {
     		System.err.println("paint eror");
     	}
		
 	}
	 BufferedImage buffer;
	 
	 public void fullPaint( InfoPanel inf) {
		 Graphics g=world.getGraphics();
		 inf.paint(inf.getGraphics());
	        if (buffer == null || buffer.getWidth() != world.getWidth() || buffer.getHeight() != world.getHeight()) {
	            // Пересоздайте буфер, если он не существует или его размер изменился
	            buffer = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
	        }
	        Graphics bufferGraphics = buffer.getGraphics();
	        // Отрисовка в буфер
	        try {
	        	for (int i = 0; i < width; i++) {
	            	for (int j = 0; j < height; j++) {
	                	if (cells[i][j] != null) {
	                    	bufferGraphics.setColor(cells[i][j].getColor());
	                    	bufferGraphics.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
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
	
	public void combinedPaint( InfoPanel inf) {
		if(paintCount%2==0) {
			fullPaint(inf);
		}else {
			ecoPaint(inf);
		}
		paintCount++;
	}

}
