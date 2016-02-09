package simacogo;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class GUIOutputStream extends OutputStream{

	private JTextArea outputTA;
	
	public GUIOutputStream(JTextArea outTA){
		this.outputTA = outTA;
	}
	
	@Override
	public void write(int out) throws IOException{
		this.outputTA.append(String.valueOf((char)out));
		this.outputTA.setCaretPosition(this.outputTA.getDocument().getLength());
	}
	
}
