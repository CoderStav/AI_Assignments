package simacogo;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class GUIOutputStream extends OutputStream{

	private JTextArea outputTA;
	
	/**
	 * GUIOutputStream
	 * Redirects console output to the specified swing JTextArea
	 * @param outTA JTextArea to redirect console output to
	 */
	public GUIOutputStream(JTextArea outTA){
		this.outputTA = outTA;
	}
	
	@Override
	public void write(int out) throws IOException{
		this.outputTA.append(String.valueOf((char)out));
		this.outputTA.setCaretPosition(this.outputTA.getDocument().getLength());
	}
	
}
