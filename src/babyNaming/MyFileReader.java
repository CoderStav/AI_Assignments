package babyNaming;

import java.io.*;

public class MyFileReader extends FileReader{

	public MyFileReader(String fileName) throws FileNotFoundException {
		super(fileName);
	}
	
	public int readLine(StringBuilder buf) throws IOException{
		int c;
		while((c = this.read()) != '\n' && c != '\r' && c != '\t' && c != -1)
			buf.append((char) c);
		return c; 
	}

}
