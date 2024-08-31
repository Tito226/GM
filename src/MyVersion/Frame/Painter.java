package MyVersion.Frame;

import static MyVersion.Frame.FRAME_CONFIG.CELL_START_ORGANIC;
import static MyVersion.Frame.FRAME_CONFIG.PAINT_MODE;
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

/** Paints whole window */
public class Painter implements ImageObserver {
	WorldFrame worldFrame;
	World world;
	volatile boolean stopPainting=false;
	byte paintCount=0;
	public Painter(WorldFrame worldFrame) {
		this.worldFrame=worldFrame;
		this.world=worldFrame.world;
	}

	public static void stringPaint(String str) {
	}

	void painterInitial() {
		if (PAINT_MODE==1 || PAINT_MODE==3 || PAINT_MODE==4) {
			Runnable task=() -> {
				Graphics worldGraphics=worldFrame.getGraphics();
				while (true) {
					while (!worldFrame.isWindowClosed()) {
						if (!world.getPause() && !stopPainting) {
							if (PAINT_MODE==1) {
								this.fastPaint();
							} else if (PAINT_MODE==3) {
								this.fullPaint();
							} else if (PAINT_MODE==4) {
								this.combinedPaint();
							}
							try {
								Thread.sleep(10);// 20
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							try {
								Thread.sleep(200);// 20
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Автоматически созданный блок catch
						e.printStackTrace();
					}
				}
			};
			new Thread(task,"Painter").start();
		}
	}
	
	public static void stringPaint(String[] strings) {
	}

	/** Paints only changed cells, faster then full paint, but can cause bugs */
	public void fastPaint() {
		try {
			Graphics g=worldFrame.getGraphics();
			worldFrame.cell_inf.paint(worldFrame.cell_inf.getGraphics());
			worldFrame.inf.paint(worldFrame.inf.getGraphics());
			for (int i=0; i<width; i++) {
				for (int j=0; j<height; j++) {
					Cell curCell=cells[i][j];
					if (curCell!=null&&curCell.isChange()) {
						paint(g,i,j,curCell);
						curCell.setChange(false);
					}
				}
			}

		} catch (Exception e) {
			System.err.println("paint eror");
		}

	}

	BufferedImage buffer;

	/** Paints every cell ,slow */
	public void fullPaint() {
		Graphics g=worldFrame.getGraphics();
		worldFrame.cell_inf.paint(worldFrame.cell_inf.getGraphics());
		worldFrame.inf.paint(worldFrame.inf.getGraphics());
		if (buffer==null||buffer.getWidth()!=worldFrame.getWidth()||buffer.getHeight()!=worldFrame.getHeight()) {
			try {
				// Пересоздайте буфер, если он не существует или его размер изменился
				buffer=new BufferedImage(worldFrame.getWidth(),worldFrame.getHeight(),BufferedImage.TYPE_INT_RGB);
			} catch (Exception e) {

			}
		}
		Graphics bufferGraphics=buffer.getGraphics();
		// Отрисовка в буфер
		try {
			for (int i=0; i<width; i++) {
				for (int j=0; j<height; j++) {
					if (cells[i][j]!=null) {
						paint(bufferGraphics,i,j,cells[i][j]);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("fullpaint error");
		}
		g.drawImage(buffer,0,0,this);
		bufferGraphics.dispose();
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	/** Paint every cell without using BufferedImage, very slow */
	void paint(Graphics g, int i, int j, Cell curCell) {
		g.setColor(curCell.getColor());
		g.fillRect(i*cellSize,j*cellSize,cellSize,cellSize);
		if (cellSize>=3) {
			if (curCell.liveCell==null) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(i*cellSize,j*cellSize,cellSize,cellSize);
			} else {
				g.setColor(Color.black);
				g.drawRect(i*cellSize,j*cellSize,cellSize,cellSize);
			}
			if (curCell.selected||(curCell.liveCell!=null&&curCell.liveCell instanceof NormCell
					&&((NormCell) curCell.liveCell).selected)) {
				g.setColor(Color.red);
				g.drawOval(i*cellSize,j*cellSize,cellSize,cellSize);
				// g.drawOval(j, j, i, i);
			}
		}
	}

	/** Calls fullPaint() and fastPaint() in turn */
	public void combinedPaint() {
		if (paintCount%2==0) {
			fullPaint();
		} else {
			fastPaint();
		}
		paintCount++;
	}

}
