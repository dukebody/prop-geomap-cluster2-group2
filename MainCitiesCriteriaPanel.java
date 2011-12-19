/*
Author: Israel Saeta Pérez
*/


import java.util.*;
import java.util.List;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;


/**
Lets the user select criteria to find the main cities of the country.
*/
public class MainCitiesCriteriaPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	JLabel label;
	JPanel labelPanel;

    JRadioButton rb1, rb2, rb3;

    DefaultListModel listModel;
    JList list;
    JScrollPane listPanel;

    JFormattedTextField nCitiesField;
    JFormattedTextField distField;

    JButton submit;
    JButton back;
	JPanel buttonPanel;
	
    String countryName;
    List<String> typeCodes;
    int nCities = 0;
    
    public MainCitiesCriteriaPanel() {
        super(new BorderLayout());
        
        CountryController countryc = Application.getCountryController();
        CitiesController citiesc = Application.getCitiesController();
        countryName = Application.getCountryName();
        
        
        label = new JLabel();
        labelPanel = new JPanel();
        labelPanel.add(label);
        
        label.setText("Criteria to select main cities for " + countryName);


        listModel = new DefaultListModel();

        Iterator<HashMap<String,String>> iter = citiesc.getToponymTypesIterator();
        while(iter.hasNext()) {
            String topTypeName = iter.next().get("code");
            listModel.addElement(topTypeName);
        }
        list = new JList(listModel);
        list.addListSelectionListener(this);
        listPanel = new JScrollPane(list);


        NumberFormat nCitiesFormat = NumberFormat.getInstance();
        nCitiesFormat.setMaximumFractionDigits(0);
        nCitiesFormat.setMaximumIntegerDigits(4);

        NumberFormat distFormat = NumberFormat.getInstance();
        distFormat.setMaximumFractionDigits(0);
        distFormat.setMaximumIntegerDigits(3);

        nCitiesField = new JFormattedTextField(nCitiesFormat);
        distField = new JFormattedTextField(distFormat);

        nCitiesField.setMaximumSize(new Dimension(50,1));
        nCitiesField.setColumns(4);

        rb1 = new JRadioButton("Filter by type", true);
        rb2 = new JRadioButton("Filter by number", false);
        rb3 = new JRadioButton("Filter by distance to the coast (km)", false);
        
        ButtonGroup bgroup = new ButtonGroup();
        
        bgroup.add(rb1);
        bgroup.add(rb2);
        bgroup.add(rb3);


        JPanel radioPanel = new JPanel();
        
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.add(rb1);
        radioPanel.add(listPanel);
        radioPanel.add(rb2);
        radioPanel.add(nCitiesField);
        radioPanel.add(rb3);
        radioPanel.add(distField);


        submit = new JButton();
        back = new JButton();

        submit.setText("OK");
        back.setText("GO BACK");
        submit.addActionListener(this);
        back.addActionListener(this);

        buttonPanel = new JPanel();
        buttonPanel.add(back);
    	buttonPanel.add(submit);

        add(labelPanel, BorderLayout.NORTH);
        add(radioPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == submit){
            if (rb1.isSelected()) {
    		    Application.getMainCitiesPanel(this, typeCodes, 0, 0.0);
            }
            else if (rb2.isSelected()) {
                JFrame frame = Application.getFrame();
                try {
                    nCities = new Integer(nCitiesField.getText());
                    if (nCities <= 0)
                        throw new NumberFormatException();
                    typeCodes = new ArrayList<String>();
                    Application.getMainCitiesPanel(this, typeCodes, nCities, 0.0);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive integer number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JFrame frame = Application.getFrame();
                try {
                    Double dist = new Double(distField.getText());
                    if (dist <= 0)
                        throw new NumberFormatException();
                    typeCodes = new ArrayList<String>();
                    Application.getMainCitiesPanel(this, typeCodes, 0, dist);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else {
            Application.getOptionPanel(this, countryName);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            Object[] tcs = list.getSelectedValues();
            typeCodes = new ArrayList<String>();
            for (int i = 0; i < tcs.length; i++) {
                String typeCode = (String) tcs[i];
                typeCodes.add(typeCode);
            }

        }
    }
    
}
