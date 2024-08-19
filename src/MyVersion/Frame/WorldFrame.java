package MyVersion.Frame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WorldFrame extends JPanel {
	World world;
	JFrame frame=new JFrame("GM");
	private static boolean windowClosed=false;
	InfoPanel inf;
	Painter painter;
	Cell_InfoPanel cell_inf;

	private WorldFrame(World world) {
		this.world=world;
		inf=new InfoPanel(world);
		painter=new Painter(this);
		cell_inf=new Cell_InfoPanel(world);
	}

	public static void createWorldFrame(World world) {
		WorldFrame result =new WorldFrame(world);
		world.worldFrame=result;
		FrameCreator.createFrame(world,world.realWidth,world.realHeight);
	}

	
	void addWinListnerToFrame() {
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {

				windowClosed=false;
			}

			@Override
			public void windowIconified(WindowEvent e) {
				windowClosed=true;
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				windowClosed=false;
			}

			@Override
			public void windowClosed(WindowEvent e) {
				windowClosed=true;
			}

			@Override
			public void windowClosing(WindowEvent e) {
				windowClosed=true;
			}

			@Override
			public void windowActivated(WindowEvent e) {
				windowClosed=false;
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				windowClosed=true;
			}

		});
	}

	public static boolean isWindowClosed() {
		return windowClosed;
	}
}
