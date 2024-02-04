package MyVersion.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;



public class FileSaveListener implements ActionListener {
    //TODO СДЕЛАТЬ СОХРАНЕНИЕ
    @Override
    public void actionPerformed(ActionEvent e) {

        FileOutputStream outputStream ;
        ObjectOutputStream objectOutputStream;
        try {
            World.setPause(false);
            outputStream = new FileOutputStream("D:\\savevvv.txt");
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject("");
            objectOutputStream.close();
            outputStream.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            World.setPause(true);
        }
    }
}