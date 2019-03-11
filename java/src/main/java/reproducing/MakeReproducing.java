/*
 * Created on 13 mai 2004
 * Author: Hadrien Devichi
 */
package reproducing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

// TODO optimize. reduce number of loops; use something faster than vect?

// TODO adapt to changes in abstractSelfRepr
/**
 * This class takes a class name (in the same package) and outputs
 * a file with the same class, but with self generating capability
 * 
 * The class must not have a main method.
 * The class must not have a SOURCE static String[]
 * 
 * The class should extend the SelfReproducing abstract class
 */
public class MakeReproducing {
	
	private static final Logger LOGGER = Logger.getLogger( MakeReproducing.class.getName() );
	
	// do we need to add import to the class
	private static boolean needImportFile = true;
	private static boolean needImportFileWriter = true;
	private static boolean needImportIOException = true;
	
	private static final String[] reproductionCode = {
			"	public static void main(String[] args) throws IOException {",
			"",
			"		// Open a file for output",
			"		File f = new File(getChildName(args));",
			"		f.createNewFile();",
			"		FileWriter fw = new FileWriter(f);",
			"",
			"		// Print the source array content into the file",
			"		for (int i = 0; i < SOURCE.length; i++) {",
			"",
			"			if (SOURCE[i] == null) {",
			"",
			"				// When null line is encountered, insert the array",
			"				// in the output",
			"",
			"				// Print array declaration",
			"				fw.write(\"    private static final String[] SOURCE = {\");",
			"				fw.write((char)13);",
			"",
			"				// Print the array (but last line)",
			"				for (int j = 0; j < SOURCE.length-1; j++) {",
			"",
			"					if (j == i) {",
			"						// special case of the null line",
			"						fw.write(\"        null,\");",
			"						fw.write((char)13);",
			"					} else {",
			"						// case of non null lines: print constant string",
			"						fw.write(\"        \");",
			"						fw.write((char)34);",
			"						// If the constant string contains string delimitors characters,",
			"						// treat them before printing them",
			"						if (SOURCE[j].indexOf(34) == -1 && SOURCE[j].indexOf(92) == -1  ) {",
			"							fw.write(SOURCE[j]);",
			"						} else {",
			"							for (int k = 0 ; k < SOURCE[j].length(); k++) {",
			"								if (SOURCE[j].charAt(k) == 34) {",
			"									fw.write((char)92);",
			"									fw.write((char)34);",
			"								} else {",
			"									if (SOURCE[j].charAt(k) == 92) {",
			"										fw.write((char)92);",
			"										fw.write((char)92);",
			"									} else {",
			"										fw.write(SOURCE[j].charAt(k));",
			"									}",
			"								}",
			"							}",
			"						}",
			"						fw.write((char)34);",
			"						fw.write(\",\");",
			"						fw.write((char)13);",
			"					}",
			"				}",
			"",
			"				// Print last line of array",
			"				// Note: no check for string delimitor is made here,",
			"				// but the last line should be }",
			"				fw.write(\"        \");",
			"				fw.write((char)34);",
			"				fw.write(SOURCE[SOURCE.length-1]);",
			"				fw.write((char)34);",
			"				fw.write(\"};\");",
			"				fw.write((char)13);",
			"",	
			"			} else {",
			"",
			"				// Non null line: just print it",
			"				fw.write(SOURCE[i]);",
			"				if ( i != (SOURCE.length - 1) )",
			"					fw.write((char)13);",
			"			}",
			"		}",
			"",
			"		fw.close();",
			"		doMain(args);",	
			"	}",
			"}"};
	
	public static void main(String[] args) {
		
		String inputClass = "";
		String className ="";
		
		// Retrieve name of file to open
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.open ();
		shell.setVisible(false);
		FileDialog dialog = new FileDialog (shell, SWT.OPEN);
		dialog.setFilterNames (new String [] {"Java files", "All Files (*.*)"});
		dialog.setFilterExtensions (new String [] {"*.java", "*.*"});
		if (args.length > 0)
			dialog.setFilterPath(args[0]);
		dialog.open();
		if (dialog.getFileName().length() == 0) {
			LOGGER.info("Aborting.");
			return;
		}
		className = dialog.getFileName();
		className = className.substring(0,className.lastIndexOf('.'));
		inputClass = dialog.getFilterPath()+"\\"+dialog.getFileName();
		shell.dispose();
		display.dispose ();

		LOGGER.debug("Treating file: "+inputClass);
		
		// Check file existence
		File f = new File(inputClass);
		if (!f.exists()) {
			LOGGER.error("File: "+inputClass+" not found!");
			return;
		}
		
		// Read file into a vector of lines. 
		Vector source = new Vector();
		try (BufferedReader in = new BufferedReader(new FileReader(f)) ) {
		    String s;
		    while((s = in.readLine()) != null) {
		      source.addElement(s);
		    }
		} catch (IOException e) {
			LOGGER.error("Error reading file: "+inputClass);
			return;
		} 
		
		// Open output file
		String outputName = inputClass.substring(0, inputClass.lastIndexOf('.')) + "_sgc.java";
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputName);
		} catch (IOException e) {
			LOGGER.error("Error opening output file.");
			return;
		} finally {
			try {
				if (fw != null)
					fw.close();
			}	catch (IOException e) {
				LOGGER.error(e);
			}
		}
		
		// Do the magic
		LOGGER.debug("Output to: "+outputName);
		
		// Assuming the class start on the first { that is not commented,
		// find the start of the class
		int classBeginIndex = 0;
		for (int i = 0 ; i < source.size() ; i ++ ) {
			String line = (String)source.elementAt(i);
			
			if (line.trim().startsWith("import java.io.*")) {
				needImportFile = false;
				needImportFileWriter = false;
				needImportIOException = false;
			} else {
				if (line.trim().startsWith("import java.io.File")) {
					needImportFile = false;
				}
				if (line.trim().startsWith("import java.io.FileWriter")) {
					needImportFileWriter = false;
				}
				if (line.trim().startsWith("import java.io.IOException")) {
					needImportIOException = false;
				}
			}
			
			if (line.indexOf('{') == -1 )
				continue;
			boolean commented = false;
			int pos = line.indexOf('{');
			if (pos < 2)
				continue;
			pos = pos - 2;
			while (pos >= 0) {
				if (line.substring(pos,pos+1).equals("//") || line.substring(pos, pos+1).equals("/*")) {
					commented = true;
					pos= 0;
				} else {
					pos--;
				}
			}
			if (!commented) {
				classBeginIndex = i;
				break;
			}
		}
		
		if (classBeginIndex == (source.size() - 1)) {
			LOGGER.warn("No class definition found in file.");
			return;
		} else {
			// modify class name in the source
			String classDef = (String)source.elementAt(classBeginIndex);
			int indexOfName = classDef.indexOf(className);
			classDef = classDef.substring(0,indexOfName)+className+"_sgc"+classDef.substring(indexOfName+className.length());
			source.setElementAt(classDef, classBeginIndex);
		}
		
		try {
			writeSelfGeneratingClass(fw,source,classBeginIndex);
			fw.close();
		} catch (IOException e) {
			LOGGER.error("Error writing output.");
		}
		
		LOGGER.debug("Done.");
		
	}
	
	// Replace in the string s all the delimitors by escaped ones
	// also escape the slashes
	private static String escapeDelimitors(String s) {
		if (s.indexOf('\"') == -1 && s.indexOf('\\') == -1 )
			return s;
		StringBuilder sb = new StringBuilder();
		for (int i=0 ; i < s.length() ; i++) {
			if (s.charAt(i) == '\"') {
				sb.append('\\');
				sb.append('\"');
			} else {
				if (s.charAt(i) == '\\') {
					sb.append('\\');
					sb.append('\\');
				} else {
					sb.append(s.charAt(i));
				}
			}
		}
		return sb.toString();
	}
	
	private static void writeSelfGeneratingClass(FileWriter fw, Vector source, int classBeginIndex) throws IOException {
		
		int lastImportIndex = 0;
		for (int i = 0 ; i < source.size() ; i++ ) {
			if ( ((String)source.elementAt(i)).startsWith("package") )
				lastImportIndex = i;
			if ( ((String)source.elementAt(i)).startsWith("import") )
				lastImportIndex = i;
		}
		// find index of last } (it won't be printed)
		int classEndIndex = source.size()-1;
		while ( ((String)source.elementAt(classEndIndex)).indexOf('}') == -1) {
			classEndIndex--;
		}
		
		for (int currentLine = 0 ; currentLine <= classBeginIndex ; currentLine++ ) {
			
			String line = (String)source.elementAt(currentLine);
			fw.write(line);
			fw.write((char)13);
			if (currentLine == lastImportIndex) {
				if (needImportFile || needImportFileWriter || needImportIOException ) {
					fw.write("// Imports added by MakeReproducing");
					fw.write((char)13);
				}
				if (needImportFile) {
					fw.write("import java.io.File;");
					fw.write((char)13);
				}
				if (needImportFileWriter) {
					fw.write("import java.io.FileWriter;");
					fw.write((char)13);
				}
				if (needImportIOException) {
					fw.write("import java.io.IOException;");
					fw.write((char)13);
				}
			}
		}
		
		fw.write("    private static final String[] SOURCE = {");
		fw.write((char)13);
		for (int currentLine = 0 ; currentLine < classEndIndex ; currentLine++ ) {
			
			// add additionnal imports
			if (currentLine == lastImportIndex+1) {
				if (needImportFile || needImportFileWriter || needImportIOException ) {
					fw.write("        ");
					fw.write("\"");
					fw.write("// Imports added by MakeReproducing");
					fw.write("\",");
					fw.write((char)13);
				}
				if (needImportFile) {
					fw.write("        ");
					fw.write("\"");
					fw.write("import java.io.File;");
					fw.write("\",");
					fw.write((char)13);
				}
				if (needImportFileWriter) {
					fw.write("        ");
					fw.write("\"");
					fw.write("import java.io.FileWriter;");
					fw.write("\",");
					fw.write((char)13);
				}
				if (needImportIOException) {
					fw.write("        ");
					fw.write("\"");
					fw.write("import java.io.IOException;");
					fw.write("\",");
					fw.write((char)13);
				}
			}
			// add null line
			if (currentLine == (classBeginIndex+1) ){
				fw.write("        null,");
				fw.write((char)13);
			}
			fw.write("        ");
			fw.write("\"");
			fw.write(escapeDelimitors((String)source.elementAt(currentLine)));
			fw.write("\"");

			fw.write(",");

			fw.write((char)13);
		}
		
		// write the source for the main method now
		for (int currentLine = 0 ; currentLine < reproductionCode.length; currentLine++ ) {
			fw.write("        ");
			fw.write("\"");
			fw.write(escapeDelimitors((String)reproductionCode[currentLine]));
			fw.write("\"");
			if (currentLine != (reproductionCode.length -1))
				fw.write(",");
			else
				fw.write("};");
			
			fw.write((char)13);
			
		}
		// write the source
		for (int currentLine = classBeginIndex +1; currentLine < classEndIndex ; currentLine ++) {
			fw.write((String)source.elementAt(currentLine));
			fw.write((char)13);
		}
		
		// write the main method
		for (int currentLine = 0 ; currentLine < reproductionCode.length ; currentLine++ ) {
			fw.write(reproductionCode[currentLine]);
			if (currentLine != (reproductionCode.length - 1))
				fw.write((char)13);
		}
	}
}
