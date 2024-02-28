package MyVersion.Frame;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

public class FrameMover extends MouseInputAdapter{
	private JComponent view= null;
	private Point holdPointOnView = null;
	
	public FrameMover(JComponent view) {
	    this.view = view;
	    this.view.addMouseListener(this);
	    this.view.addMouseMotionListener(this);
	}
	 @Override
	  public void mousePressed(MouseEvent e) {
	    view.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	    holdPointOnView = e.getPoint();
	  }
	
	 @Override
	  public void mouseReleased(MouseEvent e) {
	    view.setCursor(null);
	  }
	 
	 @Override
	  public void mouseDragged(MouseEvent e) {
	    Point dragEventPoint = e.getPoint();//Получаем положение курсора в начале движения
	    JViewport viewport = (JViewport) view.getParent();
	    Point viewPos = viewport.getViewPosition();
	    int maxViewPosX = view.getWidth() - viewport.getWidth();
	    int maxViewPosY = view.getHeight() - viewport.getHeight();

	    if(view.getWidth() > viewport.getWidth()) {//
	      viewPos.x -= dragEventPoint.x - holdPointOnView.x;

	      if(viewPos.x < 0) {
	        viewPos.x = 0;
	        holdPointOnView.x = dragEventPoint.x;
	      }

	      if(viewPos.x > maxViewPosX) {
	        viewPos.x = maxViewPosX;
	        holdPointOnView.x = dragEventPoint.x;
	      }
	    }

	    if(view.getHeight() > viewport.getHeight()) {//
	      viewPos.y -= dragEventPoint.y - holdPointOnView.y;

	      if(viewPos.y < 0) {
	        viewPos.y = 0;
	        holdPointOnView.y = dragEventPoint.y;
	      }

	      if(viewPos.y > maxViewPosY) {
	        viewPos.y = maxViewPosY;
	        holdPointOnView.y = dragEventPoint.y;
	      }
	    }

	    viewport.setViewPosition(viewPos);
	  }
}
