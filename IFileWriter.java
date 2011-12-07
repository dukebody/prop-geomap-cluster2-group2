import java.util.*;

interface IFileWriter {
	// Writes a stream of HashMaps to a file

	// FileWriter(File f)

	public void write(Iterator<HashMap<String,String>> mapItr);
	// iterates over the maps and writes them in the proper format line by line
}