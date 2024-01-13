package MyVersion.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class PauseListener implements ActionListener {
  //  int y=0;
//boolean b=false;
    public PauseListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("It works1");
        World.setPause(true);
      //  if(y%2==0) {
      //      System.out.println("It works1");
      //      World.pause = false;
      //  }else{
      //      System.out.println("It works2");
     //       World.pause = true;
     //   }
       // y++;


    }
}
