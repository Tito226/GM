package MyVersion.Frame;

import static MyVersion.Frame.World.bestLifeTime;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import javax.swing.JPanel;

import MyVersion.Cells.Cell;
import MyVersion.Cells.LiveCell;
import MyVersion.Cells.NormCell;

public class Cell_InfoPanel extends JPanel {
	World world;
	LiveCell selectedLiveCell;
	Cell selectedCell;
	int width=300;
	int heigth=100;
	public Cell_InfoPanel(World world) {
		
		this.world=world;
		this.setMaximumSize(new Dimension(width,heigth));
	}
	int xGap=10,yGap=10;
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
   	 	g.fillRect(xGap,yGap, width, heigth);
		g.setColor(Color.BLACK);
		if(selectedLiveCell!=null && selectedLiveCell instanceof NormCell) {
			NormCell selectedNormCell =(NormCell) selectedLiveCell;
			LiveCell curLiveCellInCells=world.getCells()[selectedNormCell.getX()][selectedNormCell.getY()].liveCell;
			g.drawString("Selected: " + selectedNormCell+" |", xGap+3, yGap+10);
			g.drawString("energy: " + selectedNormCell.getEnergy()+" |", xGap+3, yGap+20);
			g.drawString("lifeTime: " + selectedNormCell.lifeTime+" |", xGap+3, yGap+30);
			g.drawString("cause of death: " + selectedNormCell.causeOfDeath+" |", xGap+3, yGap+40);
			g.drawString("brain: " + selectedNormCell.brain+" |", xGap+3, yGap+50);
			g.drawString("containst(this): " + world.normCells.contains(selectedNormCell) +" |", xGap+3, yGap+60);
			g.drawString("in cells: " +(curLiveCellInCells!=null ? curLiveCellInCells.equals(selectedNormCell) : false )+" |", xGap+3, yGap+70);
		}else if(selectedLiveCell!=null) {
			NormCell selectedNormCell =selectedLiveCell.getHead();
			g.drawString("energy: " + selectedLiveCell.getEnergy()+" |", xGap+3, yGap+20);
			g.drawString("head lifeTime: " + selectedLiveCell.getHead().lifeTime+" |", xGap+3, yGap+30);
			g.drawString("brain: " + selectedLiveCell.getHead().brain+" |", xGap+3, yGap+40);
			world.getCells()[selectedNormCell.getX()][selectedNormCell.getY()].selected=true;
		}else if(selectedCell!=null) {
			g.drawString("organic: " + selectedCell.organic+" |", xGap+3, yGap+10);
		}
		
	}
}
