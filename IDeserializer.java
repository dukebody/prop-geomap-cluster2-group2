/*
Author: Israel Saeta Pérez
*/

/**
A component providing IDeserializer generates the actual objects in the workspace
(countries, zones, cities, etc.) from a HashMap iterator coming from a FileParser.
*/
public interface IDeserializer {
	// Deserializer(Iterator<HashMap<String,String> itr, CountryController/CitiesController cc);

	/**
	Generates the Objects to be created in the system.
	*/
	public void generate();
}