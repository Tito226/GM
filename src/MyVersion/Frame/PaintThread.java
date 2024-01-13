package MyVersion.Frame;

import static MyVersion.Frame.GM2_CONFIG.CELL_START_ORGANIC;
import static MyVersion.Frame.GM2_CONFIG.PAINT_MODE;
import static MyVersion.Frame.World.cellSize;
import static MyVersion.Frame.World.cells;
import static MyVersion.Frame.World.height;
import static MyVersion.Frame.World.pause;
import static MyVersion.Frame.World.width;
import static MyVersion.Frame.World.world;

import java.awt.Graphics;

class PaintThread extends Thread{
    inPaintThread paint1;
    inPaintThread paint2;
    inPaintThread paint3;
    static byte paintMode=PAINT_MODE;
    public   void rept(){/** full repaint, used in multi thread paint mode*/
      if(!pause){
        pause=true;
        for (int i = 0; i < width ; i++) {
            for (int j = 0; j < World.height; j++) {
                if(cells[i][j]!=null)
                    cells[i][j].setChange(true);;//set,true
            }
        }
        pause=false;
  }else {
        for (int i = 0; i < width ; i++) {
            for (int j = 0; j < World.height; j++) {
                if(cells[i][j]!=null)
                    cells[i][j].setChange(true);
            }
        }
    }
    }
    	public PaintThread(){

    	}
    	
    	
    	class inPaintThread extends Thread {
    		int type;
    		Graphics g;

        public inPaintThread(Graphics g, int type) {
            this.g = g;
            this.type = type;
        }

        public void oneThreadPaint(Graphics g) {
        	Painter.statPaint(g);
    		for (int i = 0; i < width; i++) {
    			for (int j = 0; j < height; j++) {
        	
    				if (cells[i][j] != null) {
    					if (cells[i][j].secCell == null && cells[i][j].partCell == null) {
    						g.setColor(cells[i][j].getColor());
    						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
    					} else if (cells[i][j].secCell != null && cells[i][j].partCell == null) {
    						g.setColor(cells[i][j].secCell.getColor());
    						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
    					}
    					if (cells[i][j] != null && cells[i][j].partCell != null) {
    						g.setColor(cells[i][j].partCell.getColor());
    						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
    					}
    				}
    			}
    		}  
    		Painter.statPaint(g);
        }
        
        public void two1ThreadPaint(Graphics g) {
        	Painter.statPaint(g);
        	for (int i = 0; i < width / 2; i++) {
                for (int j = 0; j < height; j++) {
                    if (cells[i][j] != null) {
                        if (cells[i][j].secCell == null && cells[i][j].partCell == null && cells[i][j].organic!=CELL_START_ORGANIC) {
                            if(cells[i][j].isChange()){//get change 
                            g.setColor(cells[i][j].getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                            cells[i][j].setChange(false);
                            }
                        } else if (cells[i][j].secCell != null && cells[i][j].partCell == null) {
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
        	Painter.statPaint(g);
    	}
        
        public void two2ThreadPaint(Graphics g) {
        	for (int i = width / 2; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (cells[i][j] != null) {
                        if (cells[i][j].secCell == null && cells[i][j].partCell == null
                        		&& cells[i][j].organic!=CELL_START_ORGANIC) {
                            if(cells[i][j].isChange()){
                            g.setColor(cells[i][j].getColor());
                            g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                            cells[i][j].setChange(false);
                            }
                        } else if (cells[i][j].secCell != null && cells[i][j].partCell == null) {
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
    	}
        
        @Override
        public void run() {
            while (true) {
                if (0 == 0){
                    switch (type) {
                        case 1 -> {
                        	two1ThreadPaint(g);
                        	
                        }
                        case 2 -> {
                        	two2ThreadPaint(g);
                        
                        }
                        case 3->{
                        
                        	oneThreadPaint(world.getGraphics());
                        
                        	
                        }
                        
                    }
            }
                try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Автоматически созданный блок catch
					e.printStackTrace();
				}
         }
            
        }

    	}

    	@Override
    	public void run() {
    		paint(world.getGraphics());
    	}

    	public void paint(Graphics g) {
    		long startTime = System.currentTimeMillis();
    		if(paintMode==2) {
    			if (paint1 == null) {

    				paint1 = new inPaintThread(g, 1);
                	paint2 = new inPaintThread(g, 2);
                	paint1.start();
                	paint2.start();
            }
        }else if (paintMode==1){
            if (paint1 == null) {
                paint1 = new inPaintThread(g, 3);
                paint1.start();
            }
        }
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 100) {
            System.out.println("Paint took " + (endTime - startTime) + " milliseconds");
        }

    }
}