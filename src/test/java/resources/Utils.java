package resources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Utils {
	
	public PrintStream Log(String path) throws FileNotFoundException {
		
		PrintStream ps = new PrintStream(new FileOutputStream(path));
			return ps;
	}

}
