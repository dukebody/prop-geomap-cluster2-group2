import java.util.*;
import java.io.*;


public class ToponymsFileParserIterator implements Iterator<HashMap<String,String>> {
    private BufferedReader _reader;
    private String _currentLine;
    private HashMap<String,String> _currentToponymPoint = new HashMap<String,String>();
 
    public ToponymsFileParserIterator(BufferedReader reader) throws IOException {
        _reader = reader;
		//Reading the header line of the file
        _currentLine = _reader.readLine();
        if (!headerFormat()) throw new RuntimeException();
		//First real data line (2nd one)
        _currentLine = _reader.readLine();
		if (_currentLine != null) {
            _currentToponymPoint = getCurrentToponymPoint();
        } else {
            _currentToponymPoint = null;
        }
    }
    
    public boolean hasNext() {
		return _currentLine != null;
    }
 
    public HashMap<String,String> next() throws NoSuchElementException, RuntimeException {
        if (_currentToponymPoint == null) throw new NoSuchElementException();

        HashMap<String,String> temp = (HashMap<String,String>) _currentToponymPoint.clone();

        try {
            // prepare new point
            _currentLine = _reader.readLine();  // advance one line for the next iteration

            if (_currentLine != null) {
            _currentToponymPoint = getCurrentToponymPoint();
            } else { // EOF
                _currentToponymPoint = null;
            }
        } catch (IOException e) {
            _currentLine = null;
            _currentToponymPoint = null;
        }
		
		return temp;
	}
	
	private HashMap<String,String> getCurrentToponymPoint() throws RuntimeException {
		String[] fields = _currentLine.split(" ");
		int aux=-1;
        
        if (fields.length != 7) throw new RuntimeException();
		
		try {aux=0;
			String nom_utf8 = fields[0];
            _currentToponymPoint.put("Nom_UTF", fields[0]);
			aux=1;
			String nom_ascii = fields[1];
            _currentToponymPoint.put("Nom_ASCII", fields[1]);
			aux=2;
			String noms_alternatius = fields[2];
            _currentToponymPoint.put("Noms_Alternatius", fields[2]);
			aux=3;
			Double latitud = new Double(fields[3]);
            _currentToponymPoint.put("Latitud", fields[3]);
			aux=4;
			Double longitud = new Double(fields[4]);
            _currentToponymPoint.put("Longitud", fields[4]);
			aux=5;
			String codi_toponim = fields[5];
            _currentToponymPoint.put("CodiToponim", fields[5]);
			aux=6;
			Double poblacio = new Double(fields[6]);
            _currentToponymPoint.put("Poblacio", fields[6]);
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }

        return _currentToponymPoint;
    }
	
    private boolean headerFormat() {
        String[] fields = _currentLine.split(" ");
		
		String zero = fields[0];
		String one = fields[1];
		String two = fields[2];
		String three = fields[3];
		String four = fields[4];
		String five = fields[5];
		String six = fields[6];
		
        if (fields.length!=7) return false;
		else if (!zero.equals("Nom_UTF")) return false;
		else if (!one.equals("Nom_ASCII")) return false;
		else if (!two.equals("Noms_Alternatius")) return false;
		else if (!three.equals("Latitud")) return false;
		else if (!four.equals("Longitud")) return false;
		else if (!five.equals("CodiToponim")) return false;
		else if (!six.equals("Poblacio")) return false;
        return true;
    }
 
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This is a readonly iterator.");
    }

}