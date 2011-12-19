/*
Authors: Georgi Ivanov and Israel Saeta Pérez
*/


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
Panel for showing a map of the selected country and its main cities.
*/
public class MapPanel extends JPanel implements ActionListener{
	
	JLabel label;
	JButton back;
	JPanel labelPanel;
	JPanel buttonPanel;
    MapCanvas mapCanvas;
	JScrollPane scrollPanel;
	String countryName;
    
    public MapPanel() {
        super(new BorderLayout());
        
        label = new JLabel();
        back = new JButton();
        countryName = Application.getCountryName();
        CountryController cc = Application.getCountryController();
        
        label.setText("Map of " + countryName.replaceAll("_", " "));
        
        back.setText("GO BACK");
        back.addActionListener(this);
        
        labelPanel = new JPanel();
        labelPanel.add(label);
        
    	mapCanvas = new MapCanvas();

        mapCanvas.setCountry(countryName);

    	buttonPanel = new JPanel();
    	buttonPanel.add(back);
        
        add(labelPanel, BorderLayout.NORTH);
        add(mapCanvas, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
 
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    		Application.getOptionPanel(this, countryName);
    	
    }
    
}