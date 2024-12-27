package MyVersion.Frame;

import java.util.ArrayList;
import static MyVersion.Frame.FRAME_CONFIG.HOW_MANY_LAST_CELLS_TO_SAVE;

/** переопределяет метод remove(Object o), чтобы сохранять последние n клеток */
public class ArrayListWrapper<E> extends ArrayList<E> {
	World world;

	public ArrayListWrapper(World world) {
		super();
		this.world=world;
	}

	@Override
	public synchronized boolean remove(Object o) {
		boolean result=super.remove((E) o);
		if (this.size()<=HOW_MANY_LAST_CELLS_TO_SAVE) {
			world.saveLastCells();
			// System.out.println(world.lastLeftBrains.size());
		}
		return result;

	}

}
