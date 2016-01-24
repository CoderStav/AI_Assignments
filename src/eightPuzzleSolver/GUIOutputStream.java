package eightPuzzleSolver;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;


public class GUIOutputStream extends OutputStream{
	private JTextArea outputTA;
	
	public GUIOutputStream(JTextArea outputTA){
		this.outputTA = outputTA;
	}
	
	@Override
	public void write(int o) throws IOException{
		this.outputTA.append(String.valueOf((char)o));
		this.outputTA.setCaretPosition(this.outputTA.getDocument().getLength());
	}
	
}