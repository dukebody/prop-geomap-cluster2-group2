import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

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
    
    public MainCitiesPanel() {
        super(new BorderLayout());

        countryName = Application.getCountryName();

        CountryController cc = Application.getCountryController();
        List<HashMap<String,String>> mainCities = cc.getMainCitiesByPopulation(countryName, 10);  // XXX: Let the user input it
        
        label = new JLabel();
        back = new JButton();
      
        listModel = new DefaultListModel();
        
        Iterator<HashMap<String,String>> iter = mainCities.iterator();
        while(iter.hasNext()){
        	String name = iter.next().get("nameUTF").replaceAll("_", " ");
            System.out.println(name);
        	listModel.addElement(name);
        }

        list = new JList(listModel);
        
//        list.setLayoutOrientation(JList.VERTICAL);
//        list.setVisibleRowCount(5);
        
        
        
        label.setText("The Main Cities of " + countryName + " are: ");
        
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
