import java.util.*;
import java.io.*;

public class ToponymsFileWriter implements IFileWriter{
	File file;
	
	public ToponymsFileWriter(File f) {
		file = f;
	}

	public void write(Iterator<HashMap<String,String>> map){
		try{
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Nom_UTF Nom_ASCII Noms_Alternatius Latitud Longitud CodiToponim Poblacio");
			while(map.hasNext()){
				HashMap<String,String> newmap = map.next();
				String line = newmap.get("Nom_UTF") + " " + newmap.get("Nom_ASCII") + " " + newmap.get("Noms_Alternatius") + " " + 
						newmap.get("Latitud") + " " + newmap.get("Longitud") + " " + newmap.get("CodiToponim") + " " + newmap.get("Poblacio");
				out.newLine();
				out.write(line);
			}
			out.close();
			
		}catch(IOException e){
			System.out.println("I/O Error: " + e.getMessage());
		}
	}
	
}
