/*
 * Created on May 14, 2004
 */
package test;

import java.io.File;

import reproducing.AbstractSelfReproducing;

public class Test extends AbstractSelfReproducing {
	
	private static final String PATH = ""; //"\\src\\org\\hadriendevichi\\test\\";
	
	public static String getChildName(String[] args) {
		
		String path = PATH;
		if (args.length > 0)
			path += "Test"+args[0]+".java";
		else 
			path += "TestNEW.java";
		
		File f = new File(path);
		return f.getAbsolutePath();
	}
	
	public static void doMain(String[] args) {
		
		System.out.println("Je me suis reproduit en "+getChildName(args)+"!!!");
	}
}
