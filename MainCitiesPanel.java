import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MainCitiesPanel extends JPanel implements ActionListener{
	
	ArrayList<String> cities;
	JLabel label;
	JList list;
	DefaultListModel listModel;
	JButton back;
	JPanel labelPanel;
	JPanel buttonPanel;
	JScrollPane listPanel;
	String country;
    
    public MainCitiesPanel(ArrayList<String> cList) {
        super(new BorderLayout());
        cities = cList;
        
        label = new JLabel();
        back = new JButton();
      
        listModel = new DefaultListModel();
        
        Iterator<String> iter = cities.iterator();
        while(iter.hasNext()){
        	String country = iter.next();
        	listModel.addElement(country);
        }

        list = new JList(listModel);
        
//        list.setLayoutOrientation(JList.VERTICAL);
//        list.setVisibleRowCount(5);
        
        country = Application.getCountry();
        
        label.setText("The Main Cities of " + country + " are: ");
        
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
    	
    		Application.getOptionPanel(this, country);
    	
    }
    
}
