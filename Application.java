import java.io.*;
import javax.swing.*;
import java.util.*;

public class Application{

	private static DataStorage ds;
	private static JFrame frame;
	private static File file1;
	private static File file2;
	private static File file3 = new File("FeatureCodes_Cities.txt");
	private static ArrayList<String> countries;
	private static ArrayList<String> cities;
	private static String country;
	private static CountryController countryc;
	private static ZonesController zonesc;
	private static CitiesController citiesc;
	private static BorderPointsDeserializer bpd;
	private static boolean check = false;
	
	public static void main(String[] args) throws InterruptedException{
		
		frame = new JFrame("COUNTRY GEOGRAPHICAL INFO SYSTEM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new HomePanel());
        frame.setLocation(500, 300);
        frame.setSize(350, 200);
        frame.setVisible(true);
        
        ds = new DataStorage();
        countries = new ArrayList<String>();
	}
	
	public static void getHomePanel(JPanel p){
			
		frame.remove(p);
    	frame.add(new HomePanel());
        frame.setSize(350, 200);
        frame.setVisible(true);
	    
	}
	
	public static void getFileFinderPanel(JPanel p){
		
		check = false;
		frame.remove(p);
    	frame.add(new FileFinderPanel());
        frame.setSize(400, 150);
        frame.setVisible(true);
        
	}
	
	public static void getCountryPanel(JPanel p, File f1, File f2){
	
		file1 = f1;
		file2 = f2;
		
		if(!check) {
			try {
				setListCountries();
				frame.remove(p);
		    	frame.add(new CountryPanel(countries));
		        frame.setSize(350, 200);
		        frame.setVisible(true);
		        check = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "The specified border file couldn't be loaded (wrong format)",
    	        "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			frame.remove(p);
	    	frame.add(new CountryPanel(countries));
	        frame.setSize(350, 200);
	        frame.setVisible(true);
	    	check = true;
		}
		

        
	}
	
	private static void setListCountries() throws Exception {
		
		countries = new ArrayList<String>();
		countryc = new CountryController(ds);
		zonesc = new ZonesController(ds);
		citiesc = new CitiesController(ds);
		Iterator<HashMap<String,String>> itrFronteres = null;
		

		BordersFileParser parserFronteres = new BordersFileParser(file1);
        itrFronteres = parserFronteres.getIterator();
		bpd = new BorderPointsDeserializer(itrFronteres, countryc, zonesc);
		bpd.generate();
		Iterator<HashMap<String,String>> iter = countryc.getAllCountriesIterator();
		while(iter.hasNext()){
			countries.add(iter.next().get("name"));
		}

		
		
	}
	
	public static void getOptionPanel(JPanel p, String c){
		
		country = c;
		frame.remove(p);
    	frame.add(new OptionPanel());
        frame.setSize(350, 200);
    	frame.setVisible(true);
    
	}
	
	public static void getMainCitiesPanel(JPanel p){
		
		setListCities();
		
		frame.remove(p);
    	frame.add(new MainCitiesPanel(cities));
        frame.setSize(350, 200);
        frame.setVisible(true);
		
	}
	
	private static void setListCities(){
		
		cities = new ArrayList<String>();
			
		// TO BE IMPLEMENTED!!!
		// cities is the ArrayList of the main cities of the chosen country!
		
		
	}
	
	public static void getMapPanel(JPanel p){
		
		frame.remove(p);
    	frame.add(new MapPanel());
        frame.setSize(350, 200);
        frame.setVisible(true);
		
	}
	
	public static String getCountry(){
		return country;
	}
	
	public static File getBordersFile(){
		return file1;
	}
	
	public static File getToponymsFile(){
		return file2;
	}
	
	public static CountryController getCC(){
		return countryc;
	}
	
	
}

