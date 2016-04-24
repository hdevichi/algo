/*
 * Created on May 14, 2004
 * Author: Hadrien Devichi
 */
package reproducingCode;

/**
 * Classes to be made self reproducing with MakeReproducing
 * should subclass this class
 */
public abstract class AbstractSelfReproducing {
	
	// Internal use - do not modify
	protected static final int _GENERATION = 0;
	
	/**
	 * That method will be called by the main method of the
	 * generated class to get the filename of the clone
	 * @param args
	 */
	protected static String getChildName(String[] args) {
		if (args.length > 0)
			return args[0];
		
		return null;
		
	}
	
	/**
	 * That method will be called by the main method of the
	 * generated class to perform any logic besides cloning
	 */
	public static void doMain(String[] args) {
	}
	
	protected boolean shouldReproduce(String[] args) {
		return true;
	}
}
