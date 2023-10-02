import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class HackAssembler{

	public int line;
	public File toTranslate;
	public ArrayList<String> toTranslateText;
	public String currentInstruction;
	public HashMap<String, Integer> table;
	public ArrayList<String> toReturnText;

	public static void main(String[] args) {
		File file = new File("/Users/Action Builder/Desktop/nand2tetris/projects/06/add/add.asm");
		HackAssembler h = new HackAssembler(file);
	}

	public HackAssembler(File file){
		toTranslate = file;
		line=-1;
		toReturnText = new ArrayList<String>();
		table = new HashMap<String, Integer>();
		addEntry("R0", 0);
		addEntry("R1", 1);
		addEntry("R2", 2);
		addEntry("R3", 3);
		addEntry("R4", 4);
		addEntry("R5", 5);
		addEntry("R6", 6);
		addEntry("R7", 7);
		addEntry("R8", 8);
		addEntry("R9", 9);
		addEntry("R10", 10);
		addEntry("R11", 11);
		addEntry("R12", 12);
		addEntry("R13", 13);
		addEntry("R14", 14);
		addEntry("R15", 15);
		addEntry("SCREEN", 16384);
		addEntry("KBD", 24576);
		addEntry("SP", 0);
		addEntry("LCL", 1);
		addEntry("Arg", 2);
		addEntry("THIS", 3);
		addEntry("THAT", 4);
		toTranslateText = getFileText(toTranslate);
		String type = "";
		while(hasMoreLines()){
			advance();
			type = instructionType();
			if(type.equals("C_INSTRUCTION")){
				toReturnText.add("111" + dest(dest()) + comp(comp()) + jump(jump()));
			} else if (type.equals("A_INSTRUCTION") && !hasSymbol()){
				toReturnText.add("0" + decimalToBinary(currentInstruction.substring(1)));
			}  else if (type.equals("A_INSTRUCTION") && hasSymbol()){
				toReturnText.add("0" + decimalToBinary(String.valueOf(getAddress(symbol()))));
			} else if (type.equals("L_INSTRUCTION")){
				addEntry(symbol(), line + 1);
			}
		}

		try{
		File output = new File("/Users/Action Builder/Desktop/nand2tetris/projects/06/Prog.hack");
		FileWriter writer = new FileWriter("Prog.hack");
		for(int i = 0; i < toReturnText.size(); i++){
			System.out.println(toReturnText.get(i));
			writer.write(toReturnText.get(i));
		}
		writer.close();
		} catch (Exception e){

		}
	}

	//COPIED FROM MY SHOPPING CART PROJECT
	public ArrayList<String> getFileText(File myFile){
    	ArrayList<String> keep = new ArrayList<String>();
    	try{
			BufferedReader loadFile = new BufferedReader(new FileReader(myFile));
			String currentString = loadFile.readLine();
			while(currentString != null) {
				keep.add(currentString);
				currentString = loadFile.readLine();
			}
			return keep;
		}catch(Exception e) {
			return null;
		}
    }

    //Checks if there are more lines
	public boolean hasMoreLines(){
		return (line + 1) < toTranslateText.size();
	}

	//Advances to the next line and returns that line
	public String advance(){
		line++;
		currentInstruction = toTranslateText.get(line);
		while(currentInstruction == null || currentInstruction.charAt(0) == '/'){
			line++;
			currentInstruction = toTranslateText.get(line);
		}
		return currentInstruction;
	}

	//Returns the instruction type
	public String instructionType(){
		if(currentInstruction.charAt(0) == '@'){
			return "A_INSTRUCTION";
		} else if (currentInstruction.charAt(0) == '('){
			return "L_INSTRUCTION";
		} else {
			return "C_INSTRUCTION";
		}
	}

	//Returns if the instruction uses a symbol
	public boolean hasSymbol(){
		String type = instructionType();
		if(type.equals("C_INSTRUCTION")){
			return false;
		} else if (type.equals("L_INSTRUCTION")){
			return true;
		} else {
			for(int i=1; i<currentInstruction.length(); i++){
				if(!Character.isDigit(currentInstruction.charAt(i))){
					return true;
				}
			}
			return false;
		}
	}

	public String symbol(){
		String type = instructionType();
		if(type.equals("A_INSTRUCTION")){
			return currentInstruction.substring(1);
		} else if (type.equals("L_INSTRUCTION")){
			return currentInstruction.substring(1, currentInstruction.length()-1);
		}
		return null;
	}

	//returns the dest String
	public String dest(){
		return currentInstruction.substring(0, currentInstruction.indexOf('='));
	}

	//returns the comp String
	public String comp(){
		if(currentInstruction.indexOf(';') != -1){
			return currentInstruction.substring(currentInstruction.indexOf('=') + 1, currentInstruction.indexOf(';'));
		} else {
			return currentInstruction.substring(currentInstruction.indexOf('=') + 1);
		}
	}
	//returns the jump String
	public String jump(){
		if(currentInstruction.indexOf(';') != -1){
			return currentInstruction.substring(currentInstruction.indexOf(';') + 1);
		} else {
			return null;
		}
	}

	public String dest(String str){
		String toReturn = "";
		if(str == null){
			return "000";
		}
		if(str.indexOf('A') != -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		if(str.indexOf('D') != -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		if(str.indexOf('M') != -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}

		return toReturn;
	}

	public String comp(String str){
		String toReturn = "";
		String aBit = "";

		if(str.indexOf('M') != -1){
			toReturn += "1";
			aBit = "M";
		} else {
			toReturn += "0";
			aBit = "A";
		}

		//You can see my failed attempt at not hardcoding it below. It's just too complex for my brain...
		if(str.equals("0")){
			return toReturn + "101010";
		} else if(str.equals("1")){
			return toReturn + "111111";
		} else if(str.equals("-1")){
			return toReturn + "111010";
		} else if(str.equals("D")){
			return toReturn + "001100";
		} else if(str.equals(aBit)){
			return toReturn + "110000";
		} else if(str.equals("!D")){
			return toReturn + "001101";
		} else if(str.equals("!" + aBit)){
			return toReturn + "110001";
		} else if(str.equals("-D")){
			return toReturn + "001111";
		} else if(str.equals("-" + aBit)){
			return toReturn + "110011";
		} else if(str.equals("D+1")){
			return toReturn + "011111";
		} else if(str.equals(aBit + "+1")){
			return toReturn + "110111";
		} else if(str.equals("D-1")){
			return toReturn + "001110";
		} else if(str.equals(aBit + "-1")){
			return toReturn + "110010";
		} else if(str.equals("D+" + aBit)){
			return toReturn + "000010";
		} else if(str.equals("D-" + aBit)){
			return toReturn + "010011";
		} else if(str.equals(aBit + "-D")){
			return toReturn + "000111";
		} else if(str.equals("D&" + aBit)){
			return toReturn + "000000";
		} else {
			return toReturn + "010101";
		}

		/*if(str.indexOf('M') != -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		if(str.indexOf('D') == -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		//BIT 3
		if(str.indexOf('A') == -1 && str.indexOf('M') == -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		//BIT 5
		if(str.indexOf('-') != -1 || str.indexOf('+') != -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		if(str.indexOf('|') != -1 || (str.indexOf('-') != -1 && str.indexOf('1') == -1) || (str.indexOf('+') != -1 && str.indexOf('1') != -1) || str.indexOf("!") != -1){
			toReturn += "1";
		} else {
			toReturn += "0";
		}*/
	}

	public String jump(String str){
		String toReturn = "";
		if(str == null){
			return "000";
		} else if(str.equals("JGT")){
			return "001";
		} else if(str.equals("JEQ")){
			return "010";
		} else if(str.equals("JGE")){
			return "011";
		} else if(str.equals("JLT")){
			return "100";
		} else if(str.equals("JNE")){
			return "101";
		} else if(str.equals("JLE")){
			return "110";
		} else {
			return "111";
		}


		/*if(str.charAt(1) == 'L' || str.charAt(1) == 'N' || str.charAt(1) == 'M'){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		if(str.charAt(1) == 'E' || str.charAt(1) == 'P' || (str.charAt(2) == 'E' && str.charAt(1) != 'N')){
			toReturn += "1";
		} else {
			toReturn += "0";
		}
		if(str.charAt(1) == 'G' || str.charAt(1) == 'N' || str.charAt(1) == 'M'){
			toReturn += "1";
		} else {
			toReturn += "0";
		}

		return toReturn;*/
	}

	public void addEntry(String symbol, int address){
		table.put(symbol, address);
	}

	public boolean contains(String symbol){
		return table.containsKey(symbol);
	}

	public int getAddress(String symbol){
		return table.get(symbol);
	}

	public String decimalToBinary(String str){
		int decimal = Integer.parseInt(str);
		int decimalTemp = decimal;
		int power = 0;
		String toReturn = "";
		/*while ((Math.pow(2, power) <=  decimalTemp)){
			power++;
		}
		power--;*/
		while(power >= 0){
			if(Math.pow(2,power) > decimalTemp){
			    toReturn += "0";
			} else {
			    toReturn += "1";
			    decimalTemp -= Math.pow(2,power);
			}
			power--;
		}
		return toReturn;
	}

}