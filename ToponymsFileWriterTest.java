import java.io.*;

public class ToponymsFileWriterTest {

	public static void main(String[] args) {
		File file = new File("tops.txt");
		File file2 = new File("ToponymsTest.txt");
		ToponymsFileWriter writer= new ToponymsFileWriter(file);
		try{
			ToponymsFileParser parser = new ToponymsFileParser(file2);
			writer.write(parser.getIterator());
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}

}
