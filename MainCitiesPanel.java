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
    
    public MainCitiesPanel(List<String> typeCodes, int nCities) {
        super(new BorderLayout());

        countryName = Application.getCountryName();

        CountryController cc = Application.getCountryController();

        List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();
        if (!typeCodes.isEmpty()) {
            mainCities = cc.getMainCitiesByType(countryName, typeCodes);
        }
        else {
            mainCities = cc.getMainCitiesByPopulation(countryName, nCities);
        }
        
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
        
        
        if (!typeCodes.isEmpty())
            label.setText("The main cities of " + countryName + " with the selected codes are: ");
        else 
            label.setText("The top " + nCities +" + main cities of " + countryName + " are: ");
        
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
