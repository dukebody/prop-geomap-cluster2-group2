/*
Author: Georgi Ivanov
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
Welcome window.
*/
public class HomePanel extends JPanel implements ActionListener{
	
	JButton jButton1, jButton2;
    JLabel label, jLabel1, jLabel2, jLabel3;
	JFrame frame;
    
    public HomePanel() {
        super(new BorderLayout());
        
        label = new JLabel();
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jButton1 = new JButton();
		jButton2 = new JButton();
		
		label.setText(" ");
		
		jLabel1.setText("WELCOME TO");
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		
		jLabel2.setText("COUNTRY GEOGRAPHICAL");
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		
		jLabel3.setText("INFORMATION SYSTEM");
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		
		jButton1.setText("Use existing data");
		jButton1.addActionListener(this);
		
		jButton2.setText("Create data");
		jButton2.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));
		panel.add(label);
		panel.add(jLabel1);
		panel.add(jLabel2);
		panel.add(jLabel3);
		
		JPanel panelButton = new JPanel();
		panelButton.add(jButton1);
		panelButton.add(jButton2);    
		
		add(panel, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
    
    }
    
    public void actionPerformed(ActionEvent e) {
    	
        //Handle open button action.
        if (e.getSource() == jButton1) {
        	
        	Application.getFileFinderPanel(this);

        } else if (e.getSource() == jButton2) {
        	
        	JOptionPane.showMessageDialog(null, "UNDER CONSTRUCTION!");
        	
        }
    }
    
}