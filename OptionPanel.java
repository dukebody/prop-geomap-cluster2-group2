import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
    	           BorderFactory.createEtchedBorder(), "Options for " + Application.getCountry() + ": "));
    	
    	buttonPanel = new JPanel();
    	
    	submit = new JButton();
    	submit.setText("SUBMIT");
    	submit.addActionListener(this);
    	
    	back = new JButton();
    	back.setText("GO BACK");
    	back.addActionListener(this);
    	
    	buttonPanel.add(submit);
    	buttonPanel.add(back);
    	
    	add(radioPanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);
    
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    	if (e.getSource() == submit) {
    		
    		CountryController cc = Application.getCC();
    		String country = Application.getCountry();
    		
			if(rb1.isSelected()){
				double result = cc.getTotalSharedBorderLength(country.replaceAll(" ", "_"));
				JOptionPane.showMessageDialog(null, "The borders length of " + country + " is: " + result + " km!");
			}
			else if(rb2.isSelected()){
				double result = cc.getTotalCoastlineLength(country.replaceAll(" ", "_"));
				JOptionPane.showMessageDialog(null, "The coastal length of " + country + " is: " + result + " km!");
			}
			else if(rb3.isSelected()){
				double result = cc.getTotalCoastlineLength(country.replaceAll(" ", "_"))+cc.getTotalSharedBorderLength(country.replaceAll(" ", "_"));
				JOptionPane.showMessageDialog(null, "The total length (boarders + coasts) of " + country + " is: " + result + " km!");
			}
			else if(rb4.isSelected()){
				Application.getMainCitiesPanel(this);
			}
			else{
				Application.getMapPanel(this);
			}
    	
    	}
    	else
    		Application.getCountryPanel(this, Application.getBordersFile(), Application.getToponymsFile());
    	
    }
    
}