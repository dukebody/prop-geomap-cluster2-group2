import java.util.*;
import java.io.*;

public class BordersFileWriter implements IFileWriter{
	File file;
	
	public BordersFileWriter(File f) {
		file = f;
	}
	
	public void write(Iterator<HashMap<String,String>> map){
		try{
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Id_Zona Latitud Longitud Codi_Pais Nom_Pais");
			out.newLine();
			while(map.hasNext()){
				HashMap<String,String> newmap = map.next();
				String line = newmap.get("id_zone") + " " + newmap.get("latitude") + " " + newmap.get("longitude") + 
									" " + newmap.get("id_country") + " " + newmap.get("name_country");
				out.write(line);
				out.newLine();
			}
			out.close();
			
		}catch(IOException e){
			System.out.println("I/O Error: " + e.getMessage());
		}
	}
	
}

