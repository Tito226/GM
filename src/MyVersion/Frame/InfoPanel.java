package MyVersion.Frame;

import static MyVersion.Frame.World.bestLifeTime;
import static MyVersion.Frame.World.cellSize;
import static MyVersion.Frame.World.cells;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import javax.swing.JPanel;

public class InfoPanel extends JPanel {
	World world;
	RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
	//System.out.println(mxBean.getUptime()/1000/60);
	public InfoPanel(World world) {
		this.world=world;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
   	 	g.fillRect(0,0, 250, 100);
		g.setColor(Color.BLACK);
		g.drawString("Restarts: " + world.Restarts+" |", 7, 9);
		g.drawString("Best Life Time: " + bestLifeTime+" |", 7, 19);
		g.drawString("Step: " +world.stepsAtAll+" |" , 7, 29);
		g.drawString("| sps: " +world.fps+" |", 7, 39);
		g.drawString("| Live Cells: " +world.liveCells+" |" , 65, 39);
		g.drawString("| Uptime: " +mxBean.getUptime()/1000/60+":"+mxBean.getUptime()/1000%60+" |" , 107, 9);
	}
	
}
