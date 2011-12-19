import java.io.*;
import javax.swing.*;
import java.util.*;

public class Application{

	private static DataStorage ds;
	private static JFrame frame;
	private static File file1;
	private static File file2;
	private static File file3 = new File("FeatureCodes_Cities.txt");
	private static String countryName;
	private static CountryController countryc;
	private static ZonesController zonesc;
	private static CitiesController citiesc;
	
	private static boolean check = false;
	
	public static void main(String[] args) throws InterruptedException{
		
		frame = new JFrame("COUNTRY GEOGRAPHICAL INFO SYSTEM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new HomePanel());
        frame.setLocation(500, 300);
        frame.setSize(450, 300);
        frame.setVisible(true);
        
        ds = new DataStorage();
		countryc = new CountryController(ds);
		zonesc = new ZonesController(ds);
		citiesc = new CitiesController(ds);

		try {
			loadToponymTypes();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "The toponym types file couldn't be loaded. Make sure that a valid file named FeatureCodes_Cities.txt is present in the same folder of the application.",
    	        "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static JFrame getFrame() {
		return frame;
	}

	public static void getHomePanel(JPanel p){
			
		frame.remove(p);
    	frame.add(new HomePanel());
        frame.setSize(450, 300);
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
				loadBorderPoints();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "The specified border file couldn't be loaded (wrong format)",
    	        "Error", JOptionPane.ERROR_MESSAGE);
			}
			try {
				loadCities();
		        check = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "The specified toponyms file couldn't be loaded (wrong format)",
    	        "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (check) {
			frame.remove(p);
	    	frame.add(new CountryPanel());
	        frame.setSize(450, 300);
	        frame.setVisible(true);
		}
		

        
	}
	
	private static void loadToponymTypes() throws Exception {

		File fTypes = new File("FeatureCodes_Cities.txt");
        TypesFileParser typesParser = new TypesFileParser(fTypes);
        Iterator<HashMap<String,String>> itrTypes = typesParser.getIterator();
        ToponymTypesDeserializer ttd = new ToponymTypesDeserializer(itrTypes, citiesc);
    	ttd.generate();
	}

	private static void loadBorderPoints() throws Exception {

		BordersFileParser parserFronteres = new BordersFileParser(file1);
        Iterator<HashMap<String,String>> itrFronteres = parserFronteres.getIterator();
		BorderPointsDeserializer bpd = new BorderPointsDeserializer(itrFronteres, countryc, zonesc);
		bpd.generate();
	}

	private static void loadCities() throws Exception {
		System.out.println("Loading cities...");
		try {
			ToponymsFileParser parserToponyms = new ToponymsFileParser(file2);
	        Iterator<HashMap<String,String>> itrToponyms = parserToponyms.getIterator();
			CitiesDeserializer cd = new CitiesDeserializer(itrToponyms, citiesc);
			cd.generate();
		} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception();
		}
	}
	
	public static void getOptionPanel(JPanel p, String c){
		
		countryName = c;
		frame.remove(p);
    	frame.add(new OptionPanel());
        frame.setSize(450, 300);
    	frame.setVisible(true);
    
	}
	
	public static void getMainCitiesPanel(JPanel p, List<String> typeCodes, int nCities){
		
		frame.remove(p);
    	frame.add(new MainCitiesPanel(typeCodes, nCities));
        frame.setSize(450, 300);
        frame.setVisible(true);
		
	}

	public static void getMainCitiesCriteriaPanel(JPanel p){
		
		frame.remove(p);
    	frame.add(new MainCitiesCriteriaPanel());
        frame.setSize(350, 500);
        frame.setVisible(true);
	}
	
	public static void getMapPanel(JPanel p){
		
		frame.remove(p);
    	frame.add(new MapPanel());
        frame.setSize(1200, 900);
        frame.setVisible(true);
		
	}
	
	public static String getCountryName(){
		return countryName;
	}
	
	public static File getBordersFile(){
		return file1;
	}
	
	public static File getToponymsFile(){
		return file2;
	}
	
	public static CountryController getCountryController(){
		return countryc;
	}

	public static CitiesController getCitiesController(){
		return citiesc;
	}
	
}

