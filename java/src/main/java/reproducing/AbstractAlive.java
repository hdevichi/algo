/*
 * Created on 15 mai 2004
 *
 */
package reproducing;

/**
 * @author Hadri
 *
 */
public class AbstractAlive extends AbstractSelfReproducing {

	public void cloneSelf() {
		// to implement
		throw new UnsupportedOperationException();
	}

	public void addCode() {
		// to implement
		throw new UnsupportedOperationException();
	}
		
	public void morph(String code, int line ) {
		// to implement
		throw new UnsupportedOperationException();
	}
	
	public void morphMethod(String code, String name) {
		// to implement
		throw new UnsupportedOperationException();
	}
	
	public void morphLifeCode(String code, int line) {
		// to implement
		throw new UnsupportedOperationException();
	}
		
	public void introspect() {
		// to implement
		throw new UnsupportedOperationException();
	}
}
