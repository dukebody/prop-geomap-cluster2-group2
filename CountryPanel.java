import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

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
	String country;
    
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
        
        label.setText("Select a country: ");
        
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
    	
			if(country != null){
				
		        Object[] options = {"Yes", "No"};
		        int n = JOptionPane.showOptionDialog(null, "Are you sure you want to choose " + country + "?", "Check",
		                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		        if (n == JOptionPane.YES_OPTION) {
		            Application.getOptionPanel(this, country);
		        }
				
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
        	country = (String)listModel.get(index);
       // 	JOptionPane.showMessageDialog(null, "The country you've selected is "+country);
            
        }
    }
    
    
 
//    public static void main(String s[]) {
//    	
//    	ArrayList<String> list = new ArrayList<String>();
//		list.add("Joro");
//		list.add("Tony");
//		list.add("Samy");
//		list.add("Tsveta");
//		list.add("Bobi");
//		list.add("Borka");
//		list.add("Gosho");
//		list.add("Antoaneta");
//		
//        JFrame frame = new JFrame("List Model Example");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setContentPane(new CountryPanel(list));
//        frame.setSize(520, 400);
//        frame.setVisible(true);
//      }
    
}