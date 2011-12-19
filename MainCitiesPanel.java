/*
Authors: Georgi Ivanov and Israel Saeta Pérez
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;


/**
Shows the main cities of a country according to specified criteria.
*/
public class MainCitiesPanel extends JPanel implements ActionListener{
	
	ArrayList<String> cities;
	JLabel label;
	JList list;
	DefaultListModel listModel;
	JButton back;
	JPanel labelPanel;
	JPanel buttonPanel;
	JScrollPane listPanel;
	String countryName;
    
    public MainCitiesPanel(List<String> typeCodes, int nCities, Double dist) {
        super(new BorderLayout());

        countryName = Application.getCountryName();

        CountryController cc = Application.getCountryController();

        List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();
        if (!typeCodes.isEmpty()) {
            mainCities = cc.getMainCitiesByType(countryName, typeCodes);
        }
        else if (nCities != 0) {
            mainCities = cc.getMainCitiesByPopulation(countryName, nCities);
        } else {
            mainCities = cc.getAllCoastalBorderCities(countryName, dist);
            nCities = mainCities.size();
        }
        
        label = new JLabel();
        back = new JButton();
      
        listModel = new DefaultListModel();
        
        Iterator<HashMap<String,String>> iter = mainCities.iterator();
        while(iter.hasNext()){
        	String name = iter.next().get("nameUTF").replaceAll("_", " ");
        	listModel.addElement(name);
        }

        list = new JList(listModel);
        
        if (!typeCodes.isEmpty())
            label.setText("The main cities of " + countryName + " with the selected codes are: ");
        else 
            label.setText("The top " + nCities + " main cities of " + countryName + " are: ");
        
        back.setText("GO BACK");
        back.addActionListener(this);
        
        labelPanel = new JPanel();
    	listPanel = new JScrollPane(list);
    	
    	buttonPanel = new JPanel();
    	
    	labelPanel.add(label);
    	buttonPanel.add(back);
        
        add(labelPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
 
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    		Application.getOptionPanel(this, countryName);
    	
    }
    
}
