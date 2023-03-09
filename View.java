package Lab3;


import java.awt.*;
import javax.swing.*; 
import java.util.*;
/** The View class that contains all GUI elements
 * @author Arscene Rubayita
 * @version fall 2022, cosc 20403
 * All the objects are defined as protected so they are accesible only to classes in the same package
 */


public class View extends JFrame 

{
	JButton read = new JButton("Read");
    JButton save = new JButton("Save");
    JButton execute = new JButton("Run");
    JButton reset = new JButton("Reset");
	JTextArea display = new JTextArea(30,40);
	JScrollPane jsp = new JScrollPane(display);
    JPanel  displayButtons = new JPanel(new GridLayout(1,4));
    protected Color tcuC = new Color(77,25,121);

	/**JVM Main method
	 * @param args
	 */
    public static void main(String[] args) {
		new View();
	  }

	/** Constructor with no parameters
	 */

	public View()
	{
		setLayout(new BorderLayout());
	    displayButtons.setBackground(tcuC);
	    display.setBackground(new Color(250,238,205));
	    jsp.setBackground(tcuC);
		setBounds(200,200,600,300);

		displayButtons.add(read);
		displayButtons.add(execute);
		displayButtons.add(save);
		displayButtons.add(reset);

		add(displayButtons,BorderLayout.NORTH);
		add(jsp,BorderLayout.CENTER);
		setTitle("Basic Interpreter");
		setForeground(tcuC);
    	setVisible(true);
		pack();
	}


	/** Fills GUI text area with text file content
	 * @param fileContent Vector containing file that has been read in
	 */
	public void changeDisplay(Vector<String> fileContent){

		StringBuilder content = new StringBuilder();
		for(String line: fileContent){
			content.append(line);
		}

		display.setText(content.toString());

		}


	/** Displays unique error message
	 * @param s String that contains error message
	 */
	public void showError(String s){

		JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
	}

}



