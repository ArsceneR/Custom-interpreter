package Lab3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** The Interpreter class that displays program output
 * @author Arscene Rubayita
 * @version fall 2022, cosc 20403
 */

public class InterpreterFrame extends JFrame implements WindowListener
{
   JPanel consoleDisplay = new JPanel();
   TextArea consoleDisplay2 = new TextArea(20,40);

	/** Constructor with no parameters
	 */
public  InterpreterFrame() {
		
	    setTitle("CMD Console Output");
	    setSize(200,200); this.addWindowListener(this);
		setBounds(300,300,500,500);
		this.add(consoleDisplay);
		consoleDisplay.add(consoleDisplay2);
		consoleDisplay2.setFont(new Font("Courier", Font.PLAIN,20));
		consoleDisplay2.setBackground(Color.BLACK);
		consoleDisplay2.setForeground(new Color(236,240,205));	
		setVisible(true);
		pack();
     }

	/** Displays intepreter frame with output from running minibasic program
	 * @param output contains print output to display in interpreter frame
	 */
public void runProgram(String output)
	{

		consoleDisplay2.setText(output.toString());
	}




	/**
	 * {@inheritDoc}
	 */
	public void windowClosing(WindowEvent e) {
		dispose();
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