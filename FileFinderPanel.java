/*
Author: Georgi Ivanov
*/

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
Lets the user select files to load the borders and the toponyms from.
*/
public class FileFinderPanel extends JPanel implements ActionListener{
	
	JButton find1, find2, submit, submitWithValidation, home;
    JLabel label1, label2;
	JFrame frame;
    JFileChooser fc;
    File file1, file2;
    
    public FileFinderPanel() {
    	
        super(new BorderLayout());
		
		label1 = new JLabel();
		label2 = new JLabel();
		find1 = new JButton();
		find2 = new JButton();
		submit = new JButton();
		submitWithValidation = new JButton();
		home = new JButton();
		fc = new JFileChooser(System.getProperty("user.dir"));
		
		label1.setText("Choose Borders File:        ");
		
		label2.setText("Choose Toponyms File:    ");
		
		find1.setText("FIND");
		find1.addActionListener(this);
		
		find2.setText("FIND");
		find2.addActionListener(this);
		
		submit.setText("SUBMIT");
		submit.addActionListener(this);

		submitWithValidation.setText("SUBMIT & VALIDATE");
		submitWithValidation.addActionListener(this);
		
		home.setText("HOME");
		home.addActionListener(this);
		
		JPanel line1 = new JPanel();
		line1.add(label1);
		line1.add(find1);
		
		JPanel line2 = new JPanel();
		line2.add(label2);
		line2.add(find2);
		
		JPanel line3 = new JPanel();
		line3.add(home);
		line3.add(submitWithValidation);
		line3.add(submit);
		  
		add(line1, BorderLayout.NORTH);
		add(line2, BorderLayout.CENTER);
		add(line3, BorderLayout.SOUTH);

    }
    
    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == find1) {
        	
        	int val = fc.showOpenDialog(FileFinderPanel.this);
        	
            if (val == JFileChooser.APPROVE_OPTION) {
                file1 = fc.getSelectedFile();
                label1.setText(file1.getName());
            }
   
        } else if (e.getSource() == find2) {

        	int val = fc.showOpenDialog(FileFinderPanel.this);
        	
            if (val == JFileChooser.APPROVE_OPTION) {
                file2 = fc.getSelectedFile();
                label2.setText(file2.getName());
            }
            
        } else if (e.getSource() == submit) {
        	
        	if(file1 != null && file2 != null){
                   Application.setValidateZones(false);
                   Application.getCountryPanel(this, file1, file2);
        	}
        	else{
        		JOptionPane.showMessageDialog(null, "You have to choose both files!");
        	}
        	
        } else if (e.getSource() == submitWithValidation) {
        	
	    	if(file1 != null && file2 != null){
	               Application.setValidateZones(true);
	               Application.getCountryPanel(this, file1, file2);
	    	}
	    	else{
	    		JOptionPane.showMessageDialog(null, "You have to choose both files!");
	    	}
    	} else if (e.getSource() == home) {
        	
        	Application.getHomePanel(this);
        	
        }
        
    }
    
}