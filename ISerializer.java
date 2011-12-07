import java.util.*;

interface ISerializer {
	// Converts from Objects to HashMaps to be written to a stream

	// Serializer(Iterator<Country> itr)

	Iterator<HashMap<String,String>> getIterator();
	// return an iterator over the maps containing the data to be written
}