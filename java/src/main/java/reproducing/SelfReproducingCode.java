/*
 * Created on 4 mai 04
 * Author Hadrien Devichi
 */
package reproducing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is a self-reproductive program.
 * It outputs its full source to a file
 * whose name is given as parameter.
 */
public class SelfReproducingCode {
	
	private static final String[] source = {
		"/*",
		" * Created on 13 mai 04",
		" * Author Hadrien Devichi",
		" */",
		"package org.hadriendevichi.test;",
		"",
		"import java.io.File;",
		"import java.io.FileWriter;",
		"import java.io.IOException;",
		"",
		"/**",
		" * This is a self-reproductive program.",
		" * It outputs its full source to a file",
		" * whose name is given as parameter.",
		" */",
		"public class SelfReproducingCode {",
		"",
		null,
		"",
		"	public static void main(String[] args) throws IOException {",
		"",
		"		// Open a file for output",
		"		File f = new File(args[0]);",
		"		f.createNewFile();",
		"boolean status = f.createNewFile();",	
		"if (!status) {",
		"		throw new IOException(\"File creation failed\");",
		"}",
		"		try (FileWriter fw = new FileWriter(f)) {",
		"",
		"		// Print the source array content into the file",
		"		for (int i = 0; i < source.length; i++) {",
		"",
		"			if (source[i] == null) {",
		"",
		"				// When null line is encountered, insert the array",
		"				// in the output",
		"",
		"				// Print array declaration",
		"				fw.write(\"    private static final String[] source = {\");",
		"				fw.write((char)13);",
		"",
		"				// Print the array (but last line)",
		"				for (int j = 0; j < source.length-1; j++) {",
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
		"						if (source[j].indexOf(34) == -1) {",
		"							fw.write(source[j]);",
		"						} else {",
		"							for (int k = 0 ; k < source[j].length(); k++) {",
		"								if (source[j].charAt(k) == 34) {",
		"									fw.write((char)92);",
		"									fw.write((char)34);",
		"								} else",
		"									fw.write(source[j].charAt(k));",
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
		"				fw.write(source[source.length-1]);",
		"				fw.write((char)34);",
		"				fw.write(\"};\");",
		"				fw.write((char)13);",
		"",	
		"			} else {",
		"",
		"				// Non null line: just print it",
		"				fw.write(source[i]);",
		"				if ( i != (source.length - 1) )",
		"					fw.write((char)13);",
		"			}",
		"		}",
		"",
		"	}",		
		"	}",
		"}"};
		
	public static void main(String[] args) throws IOException {		
		
		// Open a file for output
		File f = new File(args[0]);

		boolean status = f.createNewFile();	
		if (!status) {
			throw new IOException("File creation failed");
		}

		try (FileWriter fw = new FileWriter(f)) {
		
			// Print the source array content into the file
			for (int i = 0; i < source.length; i++) {
				
				if (source[i] == null) {	
					
					// When null line is encountered, insert the array
					// in the output
						
					// Print array declaration
					fw.write("    private static final String[] source = {");
					fw.write((char)13);
					
					// Print the array (but last line)
					for (int j = 0; j < source.length-1; j++) {
						
						if (j == i) {	
							// special case of the null line
							fw.write("        null,");
							fw.write((char)13);	
						} else {
							// case of non null lines: print constant string
							fw.write("        ");
							fw.write((char)34);
							// If the constant string contains string delimitors characters,
							// treat them before printing them
							if (source[j].indexOf(34) == -1) {
								fw.write(source[j]);
							} else {
								for (int k = 0 ; k < source[j].length(); k++) {
									if (source[j].charAt(k) == 34) {
										fw.write((char)92);
										fw.write((char)34);
									} else {
										fw.write(source[j].charAt(k));
									}
								}
							}
							fw.write((char)34);
							fw.write(",");
							fw.write((char)13);
						}
					}
					
					// Print last line of array
					// Note: no check for string delimitor is made here,
					// but the last line should be }
					fw.write("        ");
					fw.write((char)34);
					fw.write(source[source.length-1]);
					fw.write((char)34);
					fw.write("};");
					fw.write((char)13);
					
				} else {
					
					// Non null line: just print it
					fw.write(source[i]);
					if ( i != (source.length - 1) )
						fw.write((char)13);
				}
			}
		}			
	}
}