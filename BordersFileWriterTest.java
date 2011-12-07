import java.io.*;

public class BordersFileWriterTest {

	public static void main(String[] args) {
		File file = new File("bords.txt");
		File file2 = new File("BordersTest.txt");
		BordersFileWriter writer= new BordersFileWriter(file);
		try{
			BordersFileParser parser = new BordersFileParser(file2);
			writer.write(parser.getIterator());
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}

}
