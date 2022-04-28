package MyVersion;

import MyVersion.NEAT.Pool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static MyVersion.World.pooll;

public class FileSaveListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Pool pool=pooll;
        FileOutputStream outputStream ;
        ObjectOutputStream objectOutputStream;
        try {
            World.pause=false;
            outputStream = new FileOutputStream("D:\\savevvv.txt");
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(pool);
            objectOutputStream.close();
            outputStream.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            World.pause=true;
        }
    }
}