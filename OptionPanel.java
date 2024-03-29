/*
Author: Georgi Ivanov
*/

import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
Lets the user select the information to calculate and show. Displays basic dialogs with simple info.
*/
public class OptionPanel extends JPanel implements ActionListener{
	
	
	JRadioButton rb1;
	JRadioButton rb2;
	JRadioButton rb3;
	JRadioButton rb4;
	JRadioButton rb5;
	ButtonGroup bgroup;
	JPanel radioPanel;
	JPanel buttonPanel;
	JButton submit;
	JButton back;
    
    public OptionPanel() {
        super(new BorderLayout());
        
        rb1 = new JRadioButton("Calculate Borders Length", true);
    	rb2 = new JRadioButton("Calculate Coastal Length", false);
    	rb3 = new JRadioButton("Calculate Total Length", false);
    	rb4 = new JRadioButton("Show Main Cities", false);
    	rb5 = new JRadioButton("Display Map", false);
    	
    	bgroup = new ButtonGroup();
    	
    	bgroup.add(rb1);
    	bgroup.add(rb2);
    	bgroup.add(rb3);
    	bgroup.add(rb4);
    	bgroup.add(rb5);
    	
    	radioPanel = new JPanel();
    	
    	radioPanel.setLayout(new GridLayout(5, 1));
    	radioPanel.add(rb1);
    	radioPanel.add(rb2);
    	radioPanel.add(rb3);
    	radioPanel.add(rb4);
    	radioPanel.add(rb5);

    	radioPanel.setBorder(BorderFactory.createTitledBorder(
    	           BorderFactory.createEtchedBorder(), "Options for " + Application.getCountryName()));
    	
    	buttonPanel = new JPanel();
    	
    	submit = new JButton();
    	submit.setText("SUBMIT");
    	submit.addActionListener(this);
    	
    	back = new JButton();
    	back.setText("GO BACK");
    	back.addActionListener(this);
    	
    	buttonPanel.add(back);
        buttonPanel.add(submit);
    	
    	add(radioPanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);
    
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    	if (e.getSource() == submit) {
    		DecimalFormat df = new DecimalFormat("#");
    		CountryController cc = Application.getCountryController();
    		String countryName = Application.getCountryName();
    		
			if(rb1.isSelected()){
				double result = cc.getTotalSharedBorderLength(countryName.replaceAll(" ", "_"));
				JOptionPane.showMessageDialog(null, "The borders length of " + countryName + " is: " + df.format(result) + " km!");
			}
			else if(rb2.isSelected()){
				double result = cc.getTotalCoastlineLength(countryName.replaceAll(" ", "_"));
				JOptionPane.showMessageDialog(null, "The coastal length of " + countryName + " is: " + df.format(result) + " km!");
			}
			else if(rb3.isSelected()){
				double result = cc.getTotalCoastlineLength(countryName.replaceAll(" ", "_"))+cc.getTotalSharedBorderLength(countryName.replaceAll(" ", "_"));
				JOptionPane.showMessageDialog(null, "The total length (boarders + coasts) of " + countryName + " is: " + df.format(result) + " km!");
			}
			else if(rb4.isSelected()){
				Application.getMainCitiesCriteriaPanel(this);
			}
			else{
				Application.getMapPanel(this);
			}
    	
    	}
    	else
    		Application.getCountryPanel(this, Application.getBordersFile(), Application.getToponymsFile());
    	
    }
    
}