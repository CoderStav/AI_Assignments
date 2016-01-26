package eightPuzzleSolver;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;


public class GUIOutputStream extends OutputStream{
	private JTextArea outputTA;
	
	/*
	 * GUIOutputStream
	 * Redirects console output to the specified
	 * Swing JTextArea
	 */
	public GUIOutputStream(JTextArea outputTA){
		this.outputTA = outputTA;
	}
	
	@Override
	public void write(int out) throws IOException{
		this.outputTA.append(String.valueOf((char)out));
		this.outputTA.setCaretPosition(this.outputTA.getDocument().getLength());
	}
	
}