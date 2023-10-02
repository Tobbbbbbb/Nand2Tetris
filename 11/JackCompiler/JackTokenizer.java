package Backend;

import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class JackTokenizer{
	
	public int line;
	public File toTranslate;
	public ArrayList<String> toTranslateText;
	public String currentInstruction;
	public HashMap<String, Integer> table;
	public ArrayList<String> toReturnText;
	public ArrayList<String> tempTokens;
	public HashMap<String, String> tokens;
	public ArrayList<String> tokenList;
	public ArrayList<String> tokenType;
	public String currentToken;
	public String[] keyword = {
		"class",
		"constructor",
		"function",
		"method",
		"field",
		"static",
		"var",
		"int",
		"char",
		"boolean",
		"void",
		"true",
		"false",
		"null",
		"this",
		"let",
		"do",
		"if",
		"else",
		"while",
		"return"
	};
	
	public String[] symbol = {
		"{",
		"}",
		"(",
		")",
		"[",
		"]",
		".",
		",",
		";",
		"+",
		"-",
		"*",
		"/",
		"&",
		"|",
		"<",
		">",
		"=",
		"~"
	};
	
	public String[] openSymbol = {
		"{",
		"(",
		"["
	};
	
	public String[] closedSymbol = {
			"}",
			")",
			"]"
		};
	
	/*public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Project number:");
		String proj = in.nextLine();
		System.out.println("Project folder:");
		String progg = in.nextLine();
		System.out.println("Project file:");
		String prog = in.nextLine();
		File file = new File("/Users/actionbuilder/Desktop/nand2tetris/projects/" + proj + "/" + progg + "/" + prog + ".jack");
		System.out.println(file.getPath());
		JackTokenizer j = new JackTokenizer(file, prog);
	}*/
	
	public JackTokenizer(File f, String prog) {
		toTranslateText = getFileText(f);
		table = new HashMap<>();
		toReturnText = new ArrayList<String>();
		tempTokens = new ArrayList<String>();
		tokens = new HashMap<>();
		tokenList = new ArrayList<String>();
		tokenType = new ArrayList<String>();
		line=-1;
		tokenize();
		line=-1;
		int numIndents = 0;
		String curString = "";
		while(hasMoreTokens()) {
			advance();
			for(int i=0; i<numIndents; i++) {
				curString += "\t";
			}
			curString += "<";
			curString += tokenType();
			curString += "> ";
			curString += currentToken;
			curString += " </";
			curString += tokenType();
			curString += ">";
			toReturnText.add(curString);
			curString = "";
			if(tokenType().equals("SYMBOL")) {
				for(int j=0; j<openSymbol.length; j++) {
					if(currentToken.equals(openSymbol[j])) {
						numIndents++;
					} else if (currentToken.equals(closedSymbol[j])) {
						numIndents--;
					}
				}
			}
		int end = toReturnText.size();
		for(int i=0; i<end;i++) {
			if(toReturnText.get(i).equals("<identifier>  </identifier>")) {
				toReturnText.remove(i);
				i--;
				end--;
			}
		}
		}
		
		try{
			//System.out.println("1");
			File output = new File(f.getParent() + "/" + prog + "1.xml");
			//output.delete();
			//output = new File(f.getParent() + "/" + prog + "1.xml");
			PrintWriter writer = new PrintWriter(output);
			for(int i = 0; i < toReturnText.size(); i++){
				writer.println(toReturnText.get(i));
			}
			writer.close();
		} catch (Exception e){
			System.out.println("You are a failure");
		}
		
	}
	
	//COPIED FROM MY SHOPPING CART PROJECT
			public static ArrayList<String> getFileText(File myFile){
		    	ArrayList<String> keep = new ArrayList<String>();
		    	//System.out.println(myFile.getPath());
		    	try{
					BufferedReader loadFile = new BufferedReader(new FileReader(myFile));
					String currentString = loadFile.readLine();
					while(currentString != null) {
						//System.out.println(currentString);
						keep.add(currentString);
						currentString = loadFile.readLine();
					}
					return keep;
				}catch(Exception e) {
					System.out.println("oof");
					return null;
				}
		    }
			
			
			//Checks if there are more lines
			public boolean hasMoreLines(){
				return (line + 1) < toTranslateText.size();
			}

			//Advances to the next line and returns that line
			//Fix the commenting thing later
			public String advanceLine(){
				line++;
				currentInstruction = toTranslateText.get(line);
				int firstChar = 0;
				while(firstChar < currentInstruction.length() && Character.isWhitespace(currentInstruction.charAt(firstChar))) {
					firstChar++;
				}
				currentInstruction = currentInstruction.substring(firstChar);
				boolean commented = currentInstruction.contains("/**");
				//System.out.println("hey");
				//System.out.println(currentInstruction.contains("//"));
				//System.out.println(commented);
				//System.out.println(currentInstruction);
				if(currentInstruction.contains("//")) {
					while(currentInstruction.indexOf("//")==0) {
						line++;
						currentInstruction = toTranslateText.get(line);
					}
					if(currentInstruction.contains("//")) {
						currentInstruction = currentInstruction.substring(0, currentInstruction.indexOf("//"));
					}
					
				}
				while(commented) {
					System.out.println(line);
					commented = !currentInstruction.contains("*/");
					if(commented) {
						line++;
						currentInstruction = toTranslateText.get(line);
					} else {
						currentInstruction = currentInstruction.substring(currentInstruction.indexOf("*/") + 2);
					}
				}
				return currentInstruction;
			}
			
			public void tokenize() {
				//divides by spaces, but not symbols...
				while(hasMoreLines()) {
					advanceLine();
					/*int curSpace = currentInstruction.indexOf("\\s");
					int curSpace2 = currentInstruction.indexOf("\\s", curSpace);
					tokens.add(currentInstruction.substring(currentInstruction.charAt(0), currentInstruction.charAt(curSpace)));
					while(curSpace != currentInstruction.length()) {
						tokens.add(currentInstruction.substring(curSpace, curSpace2));
						curSpace = curSpace2;
						curSpace2 = currentInstruction.indexOf("\\s", curSpace);
					}*/
					for(int i=0; i<currentInstruction.length();i++) {
						if(currentInstruction.substring(i,i+1).equals("\"")) {
							tempTokens.add("&quot;");
						} else {
							tempTokens.add(currentInstruction.substring(i,i+1));
						}
					}
					tempTokens.add("\s");
				}
				
				boolean inQuote = false;
				String curString = "";
				boolean isSymb = false;
				for(int i=0; i<tempTokens.size(); i++) {
					while(i<tempTokens.size() && curString.length()==0 && tempTokens.get(i).equals("\s")) {
						i++;
					}
					if(i==tempTokens.size()) {
						break;
					}
					if(tempTokens.get(i).equals("&quot;") && !inQuote) {
						inQuote=true;
						i++;
					} else if (tempTokens.get(i).equals("&quot;") && inQuote) {
						inQuote=false;
						tokenList.add(curString);
						tokenType.add("StringConstant");
						curString="";
						i++;
					}
					if(!tempTokens.get(i).equals("\s") && !inQuote) {
						for(int j=0; j<symbol.length; j++) {
							if(tempTokens.get(i).equals(symbol[j])) {
								isSymb=true;
								for(int k=0; k<keyword.length; k++) {
									if(curString.equals(keyword[k])) {
										tokenList.add(curString);
										tokenType.add("keyword");
										break;
									} else if(checkInt(curString)) {
										tokenList.add(curString);
										tokenType.add("integerConstant");
										break;
									}
									if(k==keyword.length-1){
										tokenList.add(curString);
										tokenType.add("identifier");
									}
								}
								curString="";
								if(!(tempTokens.get(i).equals("<") || tempTokens.get(i).equals(">") || tempTokens.get(i).equals("\"") || tempTokens.get(i).equals("&"))) {
									tokenList.add(tempTokens.get(i));
								} else {
									if(tempTokens.get(i).equals("<")) {
										tokenList.add("&lt;");
									} else if (tempTokens.get(i).equals(">")) {
										tokenList.add("&gt;");
									} else if (tempTokens.get(i).equals("\"")) {
										tokenList.add("&quot;");
									} else if (tempTokens.get(i).equals("&")) {
										tokenList.add("&amp;");
									}
								}
								tokenType.add("symbol");
							}
						}
						if(!isSymb) {
							curString += tempTokens.get(i);
						}
						isSymb=false;
					} else if(tempTokens.get(i).equals("\s") && !inQuote) {
						for(int k=0; k<keyword.length; k++) {
							if(curString.equals(keyword[k])) {
								tokenList.add(curString);
								tokenType.add("keyword");
								break;
							} else if(checkInt(curString)) {
								tokenList.add(curString);
								tokenType.add("integerConstant");
								break;
							}
							if(k==keyword.length-1){
								tokenList.add(curString);
								tokenType.add("identifier");
							}
						}
						curString = "";
					} else if (inQuote) {
						curString += tempTokens.get(i);
					}
				}
				
			}
			
	public boolean checkInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
			
	public boolean hasMoreTokens() {
		return (line + 1) < tokenList.size();
	}
	
	public void advance() {
		line++;
		currentToken=tokenList.get(line);
	}
	
	public String tokenType() {
		String toRet = tokenType.get(line);
		if(toRet.equals("integerConstant")) {
			toRet = "INT_CONST";
		} else if (toRet.equals("StringConstant")) {
			toRet = "STRING_CONST";
		} else {
			toRet = toRet.toUpperCase();
		}
		return tokenType.get(line);
	}
	
	public String keyWord() {
		return currentToken.toUpperCase();
	}
	
	public char symbol() {
		return currentToken.charAt(0);
	}
	
	public String identifier() {
		return currentToken;
	}
	
	public int intVal() {
		return Integer.parseInt(currentToken);
	}
	
	public String stringVal() {
		return currentToken;
	}
	
	public ArrayList<String> getToReturnText(){
		return toReturnText;
	}
}