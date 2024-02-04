package MyVersion.Frame;

import java.util.ArrayList;

public class LimitedArrayList<T> {
	private ArrayList<T> arr=new ArrayList<>();
	int size;
	public LimitedArrayList(int size){
		this.size=size;
	}
	public T get(int i) {
		return arr.get(i);
	}
	
	@SuppressWarnings("unchecked")
	public void add(Object o) {
		if(arr.size()<=size) {
			arr.add((T) o);
		}else {
			arr.remove(0);
			arr.add((T) o);
		}
	}
	
	public int size() {
		return arr.size();
	}
}
