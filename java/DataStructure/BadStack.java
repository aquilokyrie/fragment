import java.util.EmptyStackException;

/**
* 这是一个有内存泄漏问题的堆栈
* 当堆栈执行了先入栈后出栈的过程后，堆栈内的非活动区域仍然保存了无效的对象句柄
* 这些句柄所对应的对象虽然无用，但是不会被GC回收
*/
public class BadStack{

	private Object[] elements;
	private int size = 0;

	public BadStack(int initCapacity){
		this.elements = new Object[initCapacity];
	}

	/**
	* 检查堆栈的空间是否不足，如果不足则将容量扩充为2倍
	*/
	private void ensureCapacity(){

		if(elements.length == size){
			Object[] oldElements = this.elements;
			elements = new Object[oldElements.length * 2 + 1];
			System.arraycopy(oldElements,0,elements,0,size);
		}
	}

	public void push(Object o){
		this.ensureCapacity();

		elements[size++] = e;
	}

	public Object pop(){
		if(this.size == 0)
			throw new EmptyStackException();

		Object result = this.elements[--size];
		// elements[size] = null;	//手动消除过期对象的引用
		return result;
	}
}