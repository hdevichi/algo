/*
 * Created on May 14, 2004
 */
package test;

import java.io.File;

import reproducing.AbstractSelfReproducing;
// Imports added by MakeReproducing
import java.io.FileWriter;
import java.io.IOException;

public class TestNEW extends AbstractSelfReproducing {
    private static final String[] SOURCE = {
        "/*",
        " * Created on May 14, 2004",
        " */",
        "package org.hadriendevichi.test;",
        "",
        "import java.io.File;",
        "",
        "import reproducingCode.AbstractSelfReproducing;",
        "// Imports added by MakeReproducing",
        "import java.io.FileWriter;",
        "import java.io.IOException;",
        "",
        "public class Test_sgc extends AbstractSelfReproducing {",
        null,
        "	",
        "	private static final String PATH = \"\"; //\"\\\\src\\\\org\\\\hadriendevichi\\\\test\\\\\";",
        "	",
        "	public static String getChildName(String[] args) {",
        "		",
        "		String path = PATH;",
        "		if (args.length > 0)",
        "			path += \"Test\"+args[0]+\".java\";",
        "		else ",
        "			path += \"TestNEW.java\";",
        "		",
        "		File f = new File(path);",
        "		return f.getAbsolutePath();",
        "	}",
        "	",
        "	public static void doMain(String[] args) {",
        "		",
        "		System.out.println(\"Je me suis reproduit en \"+getChildName(args)+\"!!!\");",
        "	}",
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
	public static void main(String[] args) throws IOException {

		// Open a file for output
		File f = new File(getChildName(args));
		f.createNewFile();
		FileWriter fw = new FileWriter(f);

		// Print the source array content into the file
		for (int i = 0; i < SOURCE.length; i++) {

			if (SOURCE[i] == null) {

				// When null line is encountered, insert the array
				// in the output

				// Print array declaration
				fw.write("    private static final String[] SOURCE = {");
				fw.write((char)13);

				// Print the array (but last line)
				for (int j = 0; j < SOURCE.length-1; j++) {

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
						if (SOURCE[j].indexOf(34) == -1 && SOURCE[j].indexOf(92) == -1  ) {
							fw.write(SOURCE[j]);
						} else {
							for (int k = 0 ; k < SOURCE[j].length(); k++) {
								if (SOURCE[j].charAt(k) == 34) {
									fw.write((char)92);
									fw.write((char)34);
								} else {
									if (SOURCE[j].charAt(k) == 92) {
										fw.write((char)92);
										fw.write((char)92);
									} else {
										fw.write(SOURCE[j].charAt(k));
									}
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
				fw.write(SOURCE[SOURCE.length-1]);
				fw.write((char)34);
				fw.write("};");
				fw.write((char)13);

			} else {

				// Non null line: just print it
				fw.write(SOURCE[i]);
				if ( i != (SOURCE.length - 1) )
					fw.write((char)13);
			}
		}

		fw.close();
		doMain(args);
	}
}