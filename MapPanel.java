import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
        System.out.println(countryName);
        CountryController cc = Application.getCountryController();
        
        label.setText("The Map of " + countryName + ": ");
        
        back.setText("GO BACK");
        back.addActionListener(this);
        
        labelPanel = new JPanel();
        labelPanel.add(label);
        
    	mapCanvas = new MapCanvas();

        ArrayList<ArrayList<Double[]>> allPoints = new ArrayList<ArrayList<Double[]>>();
        Iterator<HashMap<String,String>> itr =  cc.getAllCountriesIterator();
        while (itr.hasNext()) {
            String name  = itr.next().get("name");
            ArrayList<ArrayList<Double[]>> countryPoints = cc.getCountryBorderPointsForDrawing(name);
            for (ArrayList<Double[]> zonePoints: countryPoints) {
                allPoints.add(zonePoints);
            }
        }
        //ArrayList<ArrayList<Double[]>> allPoints = cc.getCountryBorderPointsForDrawing(countryName);
        mapCanvas.setPoints(allPoints);

    	// HAS TO BE ADDED THE ACTUAL MAP TO THE scrollPanel!!!
   
    	
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