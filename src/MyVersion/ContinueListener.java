package MyVersion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContinueListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        System.out.println("It works2");
        World.pause = true;

    }
}
