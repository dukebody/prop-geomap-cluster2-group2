import java.util.*;
import java.io.*;


public class ToponymsFileParserIterator implements Iterator<HashMap<String,String>> {
    private BufferedReader _reader;
    private String _currentLine;
    private HashMap<String,String> _currentToponymPoint = new HashMap<String,String>();
	//private Toponym _toponymBuffer;
 
    public ToponymsFileParserIterator(BufferedReader reader) throws IOException {
        _reader = reader;
		//Reading the header line of the file
        _currentLine = _reader.readLine();
        if (!headerFormat()) throw new RuntimeException();
		//First real data line (2nd one)
        _currentLine = _reader.readLine();
    }
    
    public boolean hasNext() {
        return _currentLine != null;
    }
 
    public HashMap<String,String> next() throws NoSuchElementException, RuntimeException {
		HashMap<String,String> temp = _currentToponymPoint;
		
		if (_currentToponymPoint==null) throw new NoSuchElementException();
	
		try {
			_currentLine = _reader.readLine();
			_currentToponymPoint = getCurrentToponymPoint();
		} catch (IOException e) {
            _currentLine = null;
            _currentToponymPoint = null;
		}
		
		return temp;
	}
	
	private HashMap<String,String> getCurrentToponymPoint() throws RuntimeException {
        String[] fields = _currentLine.split(" ");
        
        if (fields.length != 7) throw new RuntimeException();

        /*_currentMap.put("Nom_UTF", fields[0]);
        _currentMap.put("Nom_ASCII", fields[1]);
        _currentMap.put("Noms_Alternatius", fields[2]);
        _currentMap.put("Latitud", fields[3]);
        _currentMap.put("Longitud", fields[4]);
		_currentMap.put("CodiToponim", fields[5]);
		_currentMap.put("Poblacio", fields[6]);*/
		
		try {
			//_toponymBuffer = new Toponym();
		
            //_toponymBuffer.setNom_utf8(fields[0]);
			String nom_utf8 = fields[0];
            _currentToponymPoint.put("Nom_UTF", nom_utf8);
			
			//_toponymBuffer.setNom_ascii(fields[1]);
			String nom_ascii = fields[1];
            _currentToponymPoint.put("Nom_ASCII", nom_ascii);
			
			//_toponymBuffer.setNoms_alternatius(fields[2]);
			String noms_alternatius = fields[2];
            _currentToponymPoint.put("Noms_Alternatius", noms_alternatius);

            //_toponymBuffer.setLatitud(fields[3]);
			Float latitud = new Float(fields[3]);
            _currentToponymPoint.put("Latitud", latitud.toString());

            //_toponymBuffer.setLongitud(fields[4]);
			Float longitud = new Float(fields[4]);
            _currentToponymPoint.put("Longitud", longitud.toString());

            //_toponymBuffer.setCodiToponim(fields[5]);
			String codi_toponim = fields[5];
            _currentToponymPoint.put("CodiToponim", codi_toponim);

            //_toponymBuffer.setPoblacio(fields[6]);
			Long poblacio = new Long(fields[6]);
            _currentToponymPoint.put("Poblacio", poblacio.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }

        return _currentToponymPoint;
    }
	
    private boolean headerFormat() {
        String[] fields = _currentLine.split(" ");
        if (fields.length!=7) return false;
        return true;
    }
 
    public void remove() {}

}