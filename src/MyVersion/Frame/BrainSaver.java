package MyVersion.Frame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;
import MyVersion.Cells.NormCell;
import MyVersion.Core.Network_Like;
import MyVersion.Frame.Wrappers.NetworkWrapperLike;

public class BrainSaver {
	static void saveBestBrain(Network_Like[] brain,int thisBestLifeTime) {
		FileOutputStream fileOutputStream;
		ObjectOutputStream objectOutputStream;
		Random r=new Random();
		try {
			fileOutputStream=new FileOutputStream("C:\\Users\\Timurs1\\Desktop\\brains\\BrainSave"+thisBestLifeTime
					+"---"+r.nextLong()+".network");
			objectOutputStream=new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(brain);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	static void saveSelectedBrain(World world) {
		System.out.println("please enter save name: ");
    	Scanner scanner=new Scanner(System.in);
    	NormCell cellForSave=world.worldFrame.cell_inf.selectedLiveCell.getHead();
    	Random r=new Random();
    	if(world.worldFrame.cell_inf.selectedLiveCell!=null && cellForSave!=null) {//TODO STUB
	 		try {
	 			world.fileOutputStream = new FileOutputStream("C:\\Users\\Timurs1\\Desktop\\brains\\BrainSave"+scanner.nextLine()+"---"+r.nextLong()+".network");
	 			world.objectOutputStream = new ObjectOutputStream(world.fileOutputStream);
	 			world.objectOutputStream.writeObject(new NetworkWrapperLike[] {cellForSave.brain,cellForSave.multiCellBrain});
	 			world.objectOutputStream.close();
	 			world.fileOutputStream.close();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
	        
	 	}
	}
	
}
