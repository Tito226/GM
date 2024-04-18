package MyVersion.Frame;

import static MyVersion.Frame.GM2_CONFIG.CREATE_SAVE_ON_LIFETIME;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;

import MyVersion.Cells.NormCell;



public class FileSaveListener implements ActionListener {
	World world;
	public FileSaveListener(World world) {
		this.world=world;
	}
	
    //TODO СДЕЛАТЬ СОХРАНЕНИЕ
    @Override
    public void actionPerformed(ActionEvent e) {
    	Scanner scanner=new Scanner(System.in);
    	NormCell cellForSave=world.cell_inf.selectedLiveCell.getHead();
    	Random r=new Random();
    	if(world.cell_inf.selectedLiveCell!=null && cellForSave!=null) {//TODO STUB
	 		try {
	 			world.fileOutputStream = new FileOutputStream("C:\\Users\\Timurs1\\Desktop\\brains\\BrainSave"+scanner.nextLine()+"---"+r.nextLong()+".network");
	 			world.objectOutputStream = new ObjectOutputStream(world.fileOutputStream);
	 			world.objectOutputStream.writeObject(new NetworkWrapper[] {cellForSave.brain,cellForSave.multiCellBrain});
	 			world.objectOutputStream.close();
	 			world.fileOutputStream.close();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
	        
	 	}
    }
}