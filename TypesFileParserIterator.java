import java.util.*;
import java.io.*;


public class TypesFileParserIterator implements Iterator<HashMap<String,String>>
{
    private BufferedReader _reader;
    private String _currentLine;
    private HashMap<String,String> _currentType = new HashMap<String,String>();
    private String type = "";
    private boolean isType = false;
    private boolean newline = false;
 
    public TypesFileParserIterator(BufferedReader reader) throws IOException, RuntimeException {
        _reader = reader;
        _currentLine = _reader.readLine();
        if (!rightFormat())
            throw new RuntimeException();
    }
    
    private boolean rightFormat() {
    	if(_currentLine.equals("") || _currentLine.equals("---") || _currentLine.split("\t").length > 1 && _currentLine.split("\t").length < 4)
    		return true;
        return false;
    }
    
    public boolean hasNext(){
        return _currentLine != null;
    }
 
    public HashMap<String,String> next(){
        String[] fields = _currentLine.split("\t");
        
        if(_currentLine.equals("")){
        	newline = true;
        	try{
        		_currentLine = _reader.readLine();
        	}catch(IOException e){
        		e.printStackTrace();
        	}
//        	System.out.println("in here");
        	return this.next();
        }
        
        else if(_currentLine.equals("---")){
        	if(newline){
        		isType = true;
        		newline = false;
        	}
//        	System.out.println("---");
        	try{
        		_currentLine = _reader.readLine();
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	return this.next();
        }
        
        else if(isType){
        	type = _currentLine.split(" ")[0];
//        	System.out.println("type is: " + type);
        	isType = false;
        	try{
        		_currentLine = _reader.readLine();
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	return this.next();
        }
        
        else{
		    _currentType.put("category", type);
		    _currentType.put("type_code", fields[0]);
		    _currentType.put("name", fields[1]);
		    
		    if(fields.length < 3)
		    	_currentType.put("description", "");
		    else
		    	_currentType.put("description", fields[2]);
		    
		    HashMap<String,String> result = _currentType;
		    try{
        		_currentLine = _reader.readLine();
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	
		    return result;
        }
        
    }
 
    public void remove(){
    	
    }

}