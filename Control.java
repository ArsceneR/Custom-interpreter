package Lab3;

import java.io.IOException;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;

/** The Control class that handles the communication  between the model and the view.
 * 
 */
public class Control extends View  implements ActionListener,WindowListener

{
	Model m;
	InterpreterFrame myInterpreter;

	/**JVM Main method
	 * @param args
	 */
	public static void main(String[] args) { new Control(); }
	/** Constructor with no parameters.
	 */
   	public Control()
	{

		m = new Model();
		read.addActionListener(this);
		save.addActionListener(this);
		execute.addActionListener(this);
		reset.addActionListener(this);
		this.addWindowListener(this);

	}

	/**
	 * {@inheritDoc}
	 */
public void actionPerformed(ActionEvent e) {   String whichButton = e.getActionCommand();  // determines which button generated the event event
		if (whichButton.equals("Read")) {
			processRead();
		}
	    if (whichButton.equals("Save"))
			try {
				processSave();
			}
			catch(FileNotFoundException f){
				showError("File not found");

			}
		if (whichButton.equals("Run"))
			    processExecute(); 
		if (whichButton.equals("Reset"))
			    processReset();        
    }
private void processExecute()
	{ System.out.println ("Running program" );
		try {
			m.run();

			myInterpreter = new InterpreterFrame();
			myInterpreter.runProgram(m.getOutput());
		}
		catch(NullPointerException Exception){
			showError("No output to display");
		}
		catch(IllegalArgumentException e){
			String issue = e.getMessage();
			showError(issue);
		}
		catch(Exception e){
			String issue = e.getMessage();
			showError(issue);
		}

	}
	
private void processRead()
	{
		System.out.println ("Read Method called" );
		try {
			m = new Model(); //reset database.
			Vector<String> content = m.readFile(getFileName(true) + "");
			changeDisplay(content); //changes display to text file content

		} catch (IOException e) {
			System.out.println("File not found");
			showError("File not found");
		}

	}
   	
private void processSave() throws FileNotFoundException {

	PrintWriter out = new PrintWriter(getFileName(false));
	for (String line : display.getText().split("\\n"))
		out.println(line);

	out.close();
}

private void processReset()
	{
		System.out.println ("Reset Method called " );
		m= new Model(); //reset database.
	   display.setText("");

		if (myInterpreter!=null)
		   myInterpreter.dispose();
	}
	
private File getFileName(boolean opt) {
		// Put up a file dialog to allow the user to select an input file
		File file = null;FileDialog d ;
		if (opt) d = new FileDialog(this, "Input Data", FileDialog.LOAD);
		   else d = new FileDialog(this, "Save Data", FileDialog.SAVE);
		d.setDirectory(".");
		d.setVisible(true);
		if (d.getFile() != null)
			file = new File(d.getDirectory(), d.getFile());
		return file;
	}




	/**
	 * {@inheritDoc}
	 */
	public void windowClosing(WindowEvent e) {
		dispose();
		System.exit(0);
	}
	/**
	 * {@inheritDoc}
	 */
	public void windowOpened(WindowEvent e)
	{  }
	/**
	 * {@inheritDoc}
	 */
	public void windowIconified(WindowEvent e)
	{  }
	/**
	 * {@inheritDoc}
	 */
	public void windowClosed(WindowEvent e)
	{  }
	/**
	 * {@inheritDoc}
	 */
	public void windowDeiconified(WindowEvent e)
	{  }
	/**
	 * {@inheritDoc}
	 */
	public void windowActivated(WindowEvent e)
	{  }
	/**
	 * {@inheritDoc}
	 */
	public void windowDeactivated(WindowEvent e)
	{  }

}
