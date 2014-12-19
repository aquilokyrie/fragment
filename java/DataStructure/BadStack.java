public class BadStack{

	private Object[] elements;
	private int size = 0;

	public BadStack(int initCapacity){
		this.elements = new Object[initCapacity];
	}

	private void ensureCapacity(){

		if(elements.length == size){
			Object[] oldElements = this.elements;
			elements = new Object[oldElements.length * 2];
		}
	}
}