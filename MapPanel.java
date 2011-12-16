import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MapPanel extends JPanel implements ActionListener{
	
	JLabel label;
	JButton back;
	JPanel labelPanel;
	JPanel buttonPanel;
	JScrollPane scrollPanel;
	String country;
    
    public MapPanel() {
        super(new BorderLayout());
        
        label = new JLabel();
        back = new JButton();
        country = Application.getCountry();
        
        label.setText("The Map of " + country + ": ");
        
        back.setText("GO BACK");
        back.addActionListener(this);
        
        labelPanel = new JPanel();
        labelPanel.add(label);
        
    	scrollPanel = new JScrollPane();
    	
    	// HAS TO BE ADDED THE ACTUAL MAP TO THE scrollPanel!!!
   
    	
    	buttonPanel = new JPanel();
    	buttonPanel.add(back);
    	
    	
        
        add(labelPanel, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
 
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    		Application.getOptionPanel(this, country);
    	
    }
    
}