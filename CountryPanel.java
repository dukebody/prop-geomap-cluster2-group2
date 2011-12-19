/*
Author: Georgi Ivanov
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


/**
Shows the list of all loaded countries for the user to select one.
*/
public class CountryPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	ArrayList<String> countries;
	JLabel label;
	JList list;
	DefaultListModel listModel;
	JButton submit;
	JButton back;
	JPanel labelPanel;
	JPanel buttonPanel;
	JScrollPane listPanel;
	String countryName;
    
    public CountryPanel() {
        super(new BorderLayout());
        
        label = new JLabel();
        submit = new JButton();
        back = new JButton();
      
        listModel = new DefaultListModel();
        
        CountryController cc = Application.getCountryController();
        Iterator<HashMap<String,String>> iter = cc.getAllCountriesIterator();
        while(iter.hasNext()) {
        	String countryName = iter.next().get("name");
        	listModel.addElement(countryName.replaceAll("_", " "));
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(5);
        list.addListSelectionListener(this);
        
        label.setText("Select a country");
        
        submit.setText("SUBMIT");
        submit.addActionListener(this);
        
        back.setText("GO BACK");
        back.addActionListener(this);
        
        labelPanel = new JPanel();
    	listPanel = new JScrollPane(list);
    	
    	buttonPanel = new JPanel();
    	
    	labelPanel.add(label);
    	buttonPanel.add(submit);
    	buttonPanel.add(back);
        
        add(labelPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
 
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    	if (e.getSource() == submit) {
    	
			if(countryName != null) {
	            Application.getOptionPanel(this, countryName.replaceAll(" ", "_"));
	        }

			else
				JOptionPane.showMessageDialog(null, "No country selected!");
    	
    	}
    	else
    		Application.getFileFinderPanel(this);
    	
    }
    
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
 
            int index = list.getSelectedIndex();
        	countryName = (String)listModel.get(index);
       // 	JOptionPane.showMessageDialog(null, "The country you've selected is "+country);
            
        }
    }
}