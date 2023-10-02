package Backend;

import java.util.*;
public class SymbolTable{
	
	//name and type
	public HashMap<String, String> typeMap;
	//name and kind
	public HashMap<String, String> kindMap;
	//name and number
	public HashMap<String, Integer> indexMap;
	//kind & number
	public HashMap<String, Integer> numberTracker;
	
	public SymbolTable(){
		typeMap = new HashMap<>();
		kindMap = new HashMap<>();
		indexMap = new HashMap<>();
		numberTracker = new HashMap<>();
		numberTracker.put("STATIC", 0);
		numberTracker.put("FIELD", 0);
		numberTracker.put("ARG", 0);
		numberTracker.put("VAR", 0);
		
	}
	
	public void startSubroutine(String className) {
		ArrayList<String> keys = new ArrayList<String>(typeMap.keySet());
		for(int i=0; i<keys.size();i++) {
			if(kindMap.get(keys.get(i)).equals("ARG") || kindMap.get(keys.get(i)).equals("VAR")) {
				typeMap.remove(keys.get(i));
				kindMap.remove(keys.get(i));
				indexMap.remove(keys.get(i));
			}
		}
		numberTracker.put("ARG", 0);
		numberTracker.put("VAR", 0);
		//second one should be className
	}
	
	//kind is either STATIC FIELD ARG OR VAR
	public void define(String name, String type, String kind) {
		typeMap.put(name, type);
		kindMap.put(name, kind);
		//numberTracker.put(kind, 0);
		indexMap.put(name, numberTracker.get(kind));
		numberTracker.put(kind, numberTracker.get(kind) + 1);
		System.out.println("DEFINE " + name + " " + type + " " + kind + numberTracker.get(kind));
	}
	
	public int varCount(String kind) {
		return numberTracker.get(kind);
	}
	
	public String kindOf(String name) {
		if(!kindMap.containsKey(name)) {
			System.out.println("KINDOF SUSSY");
			return name;
		}
		if(kindMap.get(name).equalsIgnoreCase("var")) {
			System.out.println("VAR DETECTED");
			return "local";
		} else if(kindMap.get(name).equalsIgnoreCase("arg")) {
			return "argument";
		} else if(kindMap.get(name).equalsIgnoreCase("field")) {
			return "this";
		} 
		System.out.println("KINDOF" + kindMap.get(name));
		return kindMap.get(name);
	}
	
	public String typeOf(String name) {
		if(!typeMap.containsKey(name)) {
			System.out.println("TYPEOF SUSSY");
			return name;
		}
		return typeMap.get(name);
	}

	public int indexOf(String name) {
		if(!typeMap.containsKey(name)) {
			System.out.println("INDEXOF SUSSY");
			return 0;
		}
		return indexMap.get(name);
	}
}