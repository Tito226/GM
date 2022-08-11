package MyVersion.Frame;

import MyVersion.NEAT.Pool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileOpenListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
World.pause=false;
try{
    FileInputStream fileInputStream = new FileInputStream("D:\\savevvv.txt");
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
World.pooll=((Pool) objectInputStream.readObject());
    fileInputStream.close();
    objectInputStream.close();
    World.pause=true;
} catch (IOException | ClassNotFoundException ex) {
    ex.printStackTrace();
}
    }
}
