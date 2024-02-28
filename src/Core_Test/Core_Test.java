package Core_Test;


// Simple JscrollPane 
import javax.swing.*; 
import java.awt.*; 

// Driver Class 
public class Core_Test { 
	// main function 
	public static void main(String[] args) { 
		
		JFrame frame = new JFrame("Simple JScrollPane Example"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(300, 200); 
		
		// Create a JPanel to hold a list of labels. 
		JPanel panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
		
		// Add a large number of labels to the panel. 
		for (int i = 1; i <= 90; i++) { 
			
			JLabel label = new JLabel("Label " + i); 
			panel.add(label); 
			
		} 
		
		// Create a JScrollPane and set the panel as its viewport. 
		JScrollPane scrollPane = new JScrollPane(panel); 
		
		// Add the JScrollPane to the frame. 
		frame.add(scrollPane); 
		frame.setVisible(true); 
	} 
} 
