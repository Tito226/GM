package MyVersion.Frame;

import static MyVersion.Frame.FRAME_CONFIG.AUTO_SIZE;
import static MyVersion.Frame.FRAME_CONFIG.CELL_SIZE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import MyVersion.Cells.Cell;
import MyVersion.Cells.LiveCell;
import MyVersion.Cells.NormCell;

public class FrameCreator {
	/***/
	static JFrame createFrame(World world, int width, int height) {
		
		WorldFrame worldFrame=world.worldFrame;
		JFrame frame=world.worldFrame.frame;
		JPanel rightMenu=new JPanel();// Right menu
		JPanel upMenu=new JPanel();// Upper menu
		JButton pauseButton=new JButton("Pause");
		JButton countinueButton=new JButton("Continue");
		//world.scroller=new JScrollPane(world);
		JScrollPane scroller=new JScrollPane(worldFrame);
		if (!AUTO_SIZE) {
			world.cellSize=1;
		}
		/*************** BUTTONS *******************/
		JButton semaPlusButton=new JButton("+Thread");
		JButton semaMinusButton=new JButton("-Thread");
		rightMenu.add(semaPlusButton);
		rightMenu.add(semaMinusButton);
		rightMenu.add(pauseButton);
		rightMenu.add(countinueButton);
		rightMenu.setLayout(new BoxLayout(rightMenu,BoxLayout.Y_AXIS));
		// rightMenu.setAlignmentX(1f);
		rightMenu.add(Box.createVerticalStrut(10));

		JButton saveButton=new JButton("save");
		JButton loadButton=new JButton("load");
		loadButton.addActionListener(new FileOpenListener());
		upMenu.add(saveButton);
		upMenu.add(loadButton);

		JButton plusButton=new JButton("+Zoom");
		JButton minusButton=new JButton("-Zoom");
		upMenu.add(plusButton);
		upMenu.add(minusButton);

		saveButton.addActionListener((java.awt.event.ActionEvent e) -> {
			BrainSaver.saveSelectedBrain(world);
		});
		
		pauseButton.addActionListener((java.awt.event.ActionEvent e) -> {
			World.setPause(true);
		});
		
		
		
		countinueButton.addActionListener((java.awt.event.ActionEvent e) -> {
			World.setPause(false);
		});
		
		semaPlusButton.addActionListener((java.awt.event.ActionEvent e) -> {
			boolean buff=world.getPause();
			world.setPause(true);
			world.maxThreads+=1;
			// world.sem=new Semaphore(world.maxThreads);
			System.out.println(world.maxThreads);
			if (!buff) {
				world.setPause(false);
			}
		});

		semaMinusButton.addActionListener((java.awt.event.ActionEvent e) -> {
			boolean buff=world.getPause();
			world.setPause(true);
			if (world.maxThreads>1) {
				world.maxThreads-=1;
			}
			// world.sem=new Semaphore(world.maxThreads);
			System.out.println(world.maxThreads);
			if (!buff) {
				world.setPause(false);
			}
		});

		plusButton.addActionListener((java.awt.event.ActionEvent e) -> {
			frame.paintAll(frame.getGraphics());
			world.cellSize++;
			System.out.println(world.cellSize);
		});
		minusButton.addActionListener((java.awt.event.ActionEvent e) -> {
			if (world.cellSize>1) {
				world.cellSize--;
			}
			frame.paintAll(frame.getGraphics());
			System.out.println(world.cellSize);
		});
		/*****************************************/
		rightMenu.add(worldFrame.cell_inf);
		upMenu.add(worldFrame.inf);
		worldFrame.inf.setPreferredSize(new Dimension(300,40));// метод для задания размера JPanel
		worldFrame.setSize(new Dimension(width,height));// метод для задания размера JPanel

		/****************** Selector ******************/

		worldFrame.addMouseListener(new MouseAdapter() {
			Cell previousElement;
			NormCell previousNormCell;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON3) {
					for (Cell[] curCellArray : world.cells) {
						for (Cell curCell : curCellArray) {
							curCell.selected=false;
							if (curCell.liveCell!=null && curCell.liveCell instanceof NormCell) {
								NormCell curNormCell=(NormCell) curCell.liveCell;
								curNormCell.selected=false;
							}
						}
					}
					// Получаем координаты курсора мыши
					if (previousElement!=null) {
						previousElement.selected=false;
					}
					if (previousNormCell!=null) {
						previousNormCell.selected=false;
					}
					int x=e.getX();
					int y=e.getY();
					// System.out.println(x+" "+y);
					int columnIndex=x/world.cellSize;
					int rowIndex=y/world.cellSize;

					if (columnIndex>=0 && rowIndex<world.cells[0].length && rowIndex>=0 && columnIndex<world.cells.length) {
						Cell selectedElement=world.cells[columnIndex][rowIndex];

						if (selectedElement.liveCell!=null && selectedElement.liveCell instanceof NormCell) {
							LiveCell selectedLiveCell=selectedElement.liveCell;
							NormCell selectedNormCell=(NormCell) selectedLiveCell;
							previousNormCell=selectedNormCell;
							worldFrame.cell_inf.selectedLiveCell=selectedNormCell;
							worldFrame.cell_inf.selectedCell=null;
							selectedNormCell.selected=true;
						} else if (selectedElement.liveCell!=null) {
							LiveCell selectedLiveCell=selectedElement.liveCell;
							worldFrame.cell_inf.selectedLiveCell=selectedLiveCell;
						} else if (selectedElement==previousElement) {
							selectedElement.selected=false;
						} else {
							previousElement=selectedElement;
							worldFrame.cell_inf.selectedLiveCell=null;
							worldFrame.cell_inf.selectedCell=selectedElement;
							selectedElement.selected=true;
						}
					}
				}
			}
		});
		/********************************************/
		/*************** SCROLLER ******************/

		scroller.setPreferredSize(new Dimension(width,height));
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		Runnable task=() -> {
			while (true) {
				if (AUTO_SIZE) {
					worldFrame.setPreferredSize(new Dimension(width*world.cellSize/CELL_SIZE+10,height*world.cellSize/CELL_SIZE+10));
					scroller.setPreferredSize(new Dimension(1200,700));
				} else {
					worldFrame.setPreferredSize(new Dimension(width*world.cellSize+10,height*world.cellSize+10));
					scroller.setPreferredSize(new Dimension(1200,700));
				}
				scroller.revalidate();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		};
		Thread thread=new Thread(task);
		thread.start();
		new FrameMover(worldFrame);
		/****************************************/

		/************** JSlider *******************/

		JSlider slider=new JSlider(JSlider.HORIZONTAL,0,150,0);
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(50);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		rightMenu.add(Box.createVerticalStrut(20));
		// Добавляем слушатель изменений
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source=(JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					world.slowdown=source.getValue();
				}
			}
		});
		slider.setMaximumSize(new Dimension(200,50));
		rightMenu.add(slider);
		/****************************************/
		worldFrame.addWinListnerToFrame();
		frame.add(upMenu,BorderLayout.NORTH);
		frame.add(rightMenu,BorderLayout.EAST); // справа будет панель с управлением
		frame.add(scroller,BorderLayout.CENTER);
		worldFrame.setSize(width,height);
		frame.setSize(width,height);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
		return frame;
	}
}
