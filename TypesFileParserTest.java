import java.util.*;
import java.io.*;

public class TypesFileParserTest {
	public static void main(String[] args) {
		File f = new File("FeatureCodes.txt");
	 	TypesFileParser parser = new TypesFileParser(f);
	 	try{
		 	Iterator<HashMap<String,String>> itr = parser.getIterator();
		 	while (itr.hasNext()) {
		 		HashMap<String,String> map = itr.next();
			 	System.out.println("-------new line-------");
		 		System.out.println(map);
		//	 	if(map != null)
		// 	 		System.out.println(map.get("type_code") + " called " + map.get("name") + " Description: " + map.get("description") + "\n");
		//	 		System.out.println(map.get("name"));
		 	}
	 	}catch (IOException e) {
            System.out.println(e.toString());
        } catch (RuntimeException e) {
            System.out.println("The file format is wrong!");
        }
	}
}
