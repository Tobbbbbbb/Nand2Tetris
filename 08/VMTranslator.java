package Backend;

import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class VMTranslator{
	
	public static File toTranslate;
	public static ArrayList<String> toTranslateText;
	public ArrayList<String> toReturnText;
	public int calls = 0;
	public int line;
	public String currentInstruction;
	public ArrayList<String> numCalls;
	public HashMap<String, Integer> functionNums;
	public boolean isMain = false;
	public int numRet = 0;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Project folder (capitalize):");
		String progg = in.nextLine();
		System.out.println("Project name (capitalize):");
		String prog = in.nextLine();
		System.out.println("Project number:");
		String proj = in.nextLine();
		ArrayList<String> p = new ArrayList<String>();
		System.out.println("Project file (put sys first!):");
		String proggg = in.nextLine();
		while(!proggg.equals("")) {
		p.add(proggg);
		System.out.println("Project folder (Sus):");
		proggg = in.nextLine();
		}
		File file = new File("/Users/actionbuilder/Desktop/nand2tetris/projects/" + proj + "/" + progg + "/" + prog + "/" + p.get(0) + ".vm");
		//System.out.println(file.getPath());
		toTranslate = file;
		toTranslateText = getFileText(toTranslate);
		for(int i=1; i<p.size(); i++) {
			file = new File("/Users/actionbuilder/Desktop/nand2tetris/projects/" + proj + "/" + progg + "/" + prog + "/" + p.get(i) + ".vm");
			toTranslateText.addAll(getFileText(file));
		}
		VMTranslator h = new VMTranslator(file, prog);
	}
	
	public VMTranslator(File file, String prog) {
		toReturnText = new ArrayList<String>();
		numCalls = new ArrayList<String>();
		functionNums = new HashMap<String, Integer>();
		line=-1;
		/*toReturnText.add("@256");
		toReturnText.add("D=A");
		toReturnText.add("@SP");
		toReturnText.add("M=D");*/
		HashMap<String, Integer> staticNum = new HashMap<String, Integer>();
		String curClass = "";
		while(hasMoreLines()) {
			//System.out.println(currentInstruction);
			advance();
			if(currentInstruction.indexOf("function") != -1) {
				int space = currentInstruction.indexOf(" ");
				int dot = currentInstruction.indexOf(".", space + 1);
				curClass = currentInstruction.substring(space + 1, dot);
			} else if(currentInstruction.indexOf("static") != -1) {
				int space1 = currentInstruction.indexOf(" ");
				int space2 = currentInstruction.indexOf(" ", space1 + 1);
				int num = Integer.valueOf(currentInstruction.substring(space2 + 1));
				if(!staticNum.containsKey(curClass) || staticNum.get(curClass)<num) {
					System.out.println("racism " + curClass);
					staticNum.put(curClass, num);
				}
			}
		}
		int temp = 0;
		Set<String> set = staticNum.keySet();
		ArrayList<String> keys = new ArrayList<String>(set);
		HashMap<String, Integer> staticMin = new HashMap<String, Integer>();
		for(int i = 0; i<staticNum.size(); i++) {
			staticMin.put(keys.get(i), temp);
			temp+=staticNum.get(keys.get(i)) + 1;
		}
		
		line=-1;
		while(hasMoreLines()) {
			//System.out.println(currentInstruction);
			advance();
			if(currentInstruction.indexOf("function") != -1) {
				int space = currentInstruction.indexOf(" ");
				int dot = currentInstruction.indexOf(".", space + 1);
				curClass = currentInstruction.substring(space + 1, dot);
			} else if(currentInstruction.indexOf("static") != -1) {
				String newLine = currentInstruction;
				int space1 = currentInstruction.indexOf(" ");
				int space2 = currentInstruction.indexOf(" ", space1 + 1);
				int num = Integer.valueOf(currentInstruction.substring(space2 + 1));
				newLine = currentInstruction.substring(0, space2 + 1);
				newLine += staticMin.get(curClass) + num;
				toTranslateText.set(line, newLine);
			}
		}
		
		line=-1;
		while(hasMoreLines()) {
			advance();
			System.out.println(currentInstruction);
		}
		
		line=-1;
		while(hasMoreLines()) {
			//System.out.println(currentInstruction);
			advance();
			//System.out.println(currentInstruction);
			if(currentInstruction.indexOf("push") != -1) {
				int space1 = currentInstruction.indexOf(" ");
				int space2 = currentInstruction.indexOf(" ", space1 + 1);
				String segment = currentInstruction.substring(space1 + 1, space2);
				int num = Integer.valueOf(currentInstruction.substring(space2 + 1));
				toReturnText.addAll(push(segment, num));
			} else if(currentInstruction.indexOf("pop") != -1) {
				int space1 = currentInstruction.indexOf(" ");
				int space2 = currentInstruction.indexOf(" ", space1 + 1);
				String segment = currentInstruction.substring(space1 + 1, space2);
				int num = Integer.valueOf(currentInstruction.substring(space2 + 1));
				toReturnText.addAll(pop(segment, num));
			} else if(currentInstruction.indexOf("label") != -1){
				toReturnText.addAll(label(currentInstruction.substring(currentInstruction.indexOf(" ") + 1)));
			} else if(currentInstruction.indexOf("if-goto") != -1){
				toReturnText.addAll(ifGoTo(currentInstruction.substring(currentInstruction.indexOf(" ") + 1)));
			} else if(currentInstruction.indexOf("goto") != -1){
				toReturnText.addAll(goTo(currentInstruction.substring(currentInstruction.indexOf(" ") + 1)));
			} else if (currentInstruction.indexOf("function") != -1) {
				int space1 = currentInstruction.indexOf(" ");
				int space2 = currentInstruction.indexOf(" ", space1 + 1);
				String segment = currentInstruction.substring(space1 + 1, space2);
				int num = Integer.valueOf(currentInstruction.substring(space2 + 1));
				toReturnText.addAll(function(segment, num));
			} else if (currentInstruction.indexOf("call") != -1) {
				int space1 = currentInstruction.indexOf(" ");
				int space2 = currentInstruction.indexOf(" ", space1 + 1);
				String segment = currentInstruction.substring(space1 + 1, space2);
				int num = Integer.valueOf(currentInstruction.substring(space2 + 1));
				toReturnText.addAll(call(segment, num));
			} else if (currentInstruction.indexOf("return") != -1) {
				toReturnText.addAll(returnCommand());
			} else {
				if(currentInstruction.equals("add")) {
					toReturnText.addAll(add());
				} else if(currentInstruction.equals("neg")) {
					toReturnText.addAll(neg());
				} else if(currentInstruction.equals("sub")) {
					toReturnText.addAll(sub());
				} else if(currentInstruction.equals("eq")) {
					toReturnText.addAll(eq());
				} else if(currentInstruction.equals("gt")) {
					toReturnText.addAll(gt());
				} else if(currentInstruction.equals("lt")) {
					toReturnText.addAll(lt());
				} else if(currentInstruction.equals("and")) {
					toReturnText.addAll(and());
				} else if(currentInstruction.equals("or")) {
					toReturnText.addAll(or());
				} else if(currentInstruction.equals("not")) {
					toReturnText.addAll(not());
				}
			}
		}
		
		try{
			File output = new File(toTranslate.getParent() + "/" + prog + ".asm");
			PrintWriter writer = new PrintWriter(output);
			for(int i = 0; i < toReturnText.size(); i++){
				//System.out.println(toReturnText.get(i));
				writer.println(toReturnText.get(i));
			}
			writer.close();
		} catch (Exception e){

		}
	}
	
	public ArrayList<String> getXY(){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("@SP");
		toReturn.add("M=M-1");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("A=A-1");
		//Now D=Y and M=X
		return toReturn;
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
		public String advance(){
			line++;
			currentInstruction = toTranslateText.get(line);
			int sub = currentInstruction.indexOf("/");
			int lastNonSpace = 0;
			//System.out.println(currentInstruction);
			if(sub != -1) {
				if(sub==0) {
					currentInstruction = "";
				} else {
					for(int i=0; i<sub; i++) {
						if(!Character.isWhitespace(currentInstruction.charAt(i))) {
							lastNonSpace = i;
						}
					}
					currentInstruction = currentInstruction.substring(0,lastNonSpace+1);
				}
			} else if(currentInstruction.length() != 0) {
				for(int i=0; i<currentInstruction.length(); i++) {
					if(!Character.isWhitespace(currentInstruction.charAt(i))) {
						lastNonSpace = i;
					}
				}
				currentInstruction = currentInstruction.substring(0,lastNonSpace+1);
			}
			lastNonSpace=0;
			while(hasMoreLines() && (currentInstruction.equals("") || currentInstruction == null || currentInstruction.charAt(0) == '/')){
				line++;
				currentInstruction = toTranslateText.get(line);
				sub = currentInstruction.indexOf("/");
				if(sub != -1) {
					if(sub==0) {
						currentInstruction = "";
					} else {
						for(int i=0; i<sub; i++) {
							if(!Character.isWhitespace(currentInstruction.charAt(i))) {
								lastNonSpace = i;
							}
						}
						currentInstruction = currentInstruction.substring(0,lastNonSpace+1);
					}
				} else if(currentInstruction.length() != 0) {
					for(int i=0; i<currentInstruction.length(); i++) {
						if(!Character.isWhitespace(currentInstruction.charAt(i))) {
							lastNonSpace = i;
							System.out.println(i);
						}
					}
					currentInstruction = currentInstruction.substring(0,lastNonSpace+1);
				}
				lastNonSpace=0;
				//System.out.println("skip" + currentInstruction);
			}
			//System.out.println("line " + symbolLine);
			//System.out.println(currentInstruction);
			return currentInstruction;
		}
	
	public ArrayList<String> add(){
		ArrayList<String> toReturn = getXY();
		toReturn.add("M=D+M");
		return toReturn;
	}
	
	public ArrayList<String> neg(){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=-M");
		return toReturn;
	}
	
	public ArrayList<String> sub(){
		ArrayList<String> toReturn = getXY();
		toReturn.add("M=M-D");
		return toReturn;
	}
	
	//0 for false, anything else for true
	public ArrayList<String> eq(){
		ArrayList<String> toReturn = getXY();
		//If they are equal, return non-zero
		toReturn.add("D=M-D");
		toReturn.add("@EQ" + calls);
		toReturn.add("D;JEQ");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=0");
		toReturn.add("@EQEND" + calls);
		toReturn.add("0;JMP");
		toReturn.add("(EQ" + calls + ")");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=-1");
		toReturn.add("(EQEND" + calls + ")");
		calls++;
		return toReturn;
	}
	
	//
	public ArrayList<String> gt(){
		ArrayList<String> toReturn = getXY();
		toReturn.add("D=M-D");
		toReturn.add("@GT" + calls);
		toReturn.add("D;JGT");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=0");
		toReturn.add("@GTEND" + calls);
		toReturn.add("0;JMP");
		toReturn.add("(GT" + calls + ")");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=-1");
		toReturn.add("(GTEND" + calls + ")");
		calls++;
		return toReturn;
	}
	
	public ArrayList<String> lt(){
		ArrayList<String> toReturn = getXY();
		toReturn.add("D=M-D");
		toReturn.add("@LT" + calls);
		toReturn.add("D;JLT");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=0");
		toReturn.add("@LTEND" + calls);
		toReturn.add("0;JMP");
		toReturn.add("(LT" + calls + ")");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=-1");
		toReturn.add("(LTEND" + calls + ")");
		calls++;
		return toReturn;
	}
	
	public ArrayList<String> and(){
		ArrayList<String> toReturn = getXY();
		toReturn.add("M=M&D");
		return toReturn;
	}
	
	public ArrayList<String> or(){
		ArrayList<String> toReturn = getXY();
		toReturn.add("M=M|D");
		return toReturn;
	}
	
	//IDK
	public ArrayList<String> not(){
		/*ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("D=M-D");
		toReturn.add("@NT" + calls);
		toReturn.add("D;JEQ");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=0");
		toReturn.add("@LTEND" + calls);
		toReturn.add("0;JMP");
		toReturn.add("(LT" + calls + ")");
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=-1");
		toReturn.add("(LTEND" + calls + ")");
		calls++;
		return toReturn;*/
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("@SP");
		toReturn.add("A=M-1");
		toReturn.add("M=-M");
		toReturn.add("M=M-1");
		return toReturn;
	}
	
	public ArrayList<String> segmentGet(String segment, int i){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("@" + i);
		toReturn.add("D=A");
		if(segment.equals("local")) {
			toReturn.add("@LCL");
			toReturn.add("A=M+D");
		} else if(segment.equals("argument")) {
			toReturn.add("@ARG");
			toReturn.add("A=M+D");
		} else if(segment.equals("this")) {
			toReturn.add("@THIS");
			toReturn.add("A=M+D");
		} else if(segment.equals("that")) {
			toReturn.add("@THAT");
			toReturn.add("A=M+D");
		} else if(segment.equals("static")) {
			toReturn.add("@16");
			toReturn.add("A=A+D");
		} else if(segment.equals("pointer")) {
			toReturn.add("@3");
			toReturn.add("A=A+D");
		} else if(segment.equals("temp")) {
			toReturn.add("@5");
			toReturn.add("A=A+D");
		}
		if(!segment.equals("constant")) {
			toReturn.add("D=M");
		}
		return toReturn;
	}
	
	public ArrayList<String> push(String segment, int i){
		ArrayList<String> toReturn = segmentGet(segment, i);
		toReturn.add("@SP");
		toReturn.add("M=M+1");
		toReturn.add("A=M-1");
		toReturn.add("M=D");
		return toReturn;
	}
	
	public ArrayList<String> segmentSet(String segment, int i){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("@R13");
		toReturn.add("M=D");
		toReturn.addAll(segmentGet(segment, i));
		toReturn.add("D=A");
		toReturn.add("@R14");
		toReturn.add("M=D");
		toReturn.add("@R13");
		toReturn.add("D=M");
		toReturn.add("@R14");
		toReturn.add("A=M");
		toReturn.add("M=D");
		return toReturn;
	}
	
	public ArrayList<String> pop(String segment, int i){
		ArrayList<String> toReturn = new ArrayList<String>();
		/*toReturn.add("@SP");
		toReturn.add("M=M-1");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.addAll(segmentSet(segment, i));
		return toReturn;*/
		
		toReturn.addAll(segmentGet(segment, i));
		toReturn.add("D=A");
		toReturn.add("@R13");
		toReturn.add("M=D");
		toReturn.add("@SP");
		toReturn.add("M=M-1");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("@R13");
		toReturn.add("A=M");
		toReturn.add("M=D");
		return toReturn;
	}
	
	public ArrayList<String> label(String lbl){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("(" + lbl + ")");
		return toReturn;
	}
	
	public ArrayList<String> goTo(String lbl){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("@" + lbl);
		toReturn.add("0;JMP");
		return toReturn;
	}
	
	public ArrayList<String> ifGoTo(String lbl){
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.addAll(pop("temp", 0));
		toReturn.add("@5");
		toReturn.add("D=M");
		toReturn.add("@" + lbl);
		toReturn.add("D;JNE");
		return toReturn;
	}
	
	public ArrayList<String> call(String functionName, int nArgs){
		ArrayList<String> toReturn = new ArrayList<String>();
		//pushes retAddr
		toReturn.add("@retAddr" + numRet);
		toReturn.add("D=A");
		toReturn.add("@SP");
		toReturn.add("M=M+1");
		toReturn.add("A=M-1");
		toReturn.add("M=D");
		//pushes lcl
		toReturn.add("@LCL");
		toReturn.add("D=M");
		toReturn.add("@SP");
		toReturn.add("M=M+1");
		toReturn.add("A=M-1");
		toReturn.add("M=D");
		//pushes arg
		toReturn.add("@ARG");
		toReturn.add("D=M");
		toReturn.add("@SP");
		toReturn.add("M=M+1");
		toReturn.add("A=M-1");
		toReturn.add("M=D");
		//pushes this
		toReturn.add("@THIS");
		toReturn.add("D=M");
		toReturn.add("@SP");
		toReturn.add("M=M+1");
		toReturn.add("A=M-1");
		toReturn.add("M=D");
		//pushes that
		toReturn.add("@THAT");
		toReturn.add("D=M");
		toReturn.add("@SP");
		toReturn.add("M=M+1");
		toReturn.add("A=M-1");
		toReturn.add("M=D");
		//REPOSITIONS ARG
		toReturn.add("@SP");
		toReturn.add("D=M");
		toReturn.add("@ARG");
		toReturn.add("M=D");
		toReturn.add("@" + nArgs);
		toReturn.add("D=A");
		toReturn.add("@ARG");
		toReturn.add("M=M-D");
		toReturn.add("@5");
		toReturn.add("D=A");
		toReturn.add("@ARG");
		toReturn.add("M=M-D");
		//REPOSITIONS LCL
		toReturn.add("@SP");
		toReturn.add("D=M");
		toReturn.add("@LCL");
		toReturn.add("M=D");
		//Goes to function name
		toReturn.add("@" + functionName);
		toReturn.add("0;JMP");
		//toReturn.add("(" + functionName + "$ret" + numCalls.get(functionNums.get(functionName)) + ")");
		toReturn.add("(retAddr" + numRet + ")");
		numRet++;
		return toReturn;
	}
	
	public ArrayList<String> function(String functionName, int nVars){
		System.out.println(functionName + " BRUH");
		ArrayList<String> toReturn = new ArrayList<String>();
		toReturn.add("(" + functionName + ")");
		/*toReturn.addAll(push("constant", nVars, false));
		//R8
		toReturn.addAll(pop("temp", 3));
		toReturn.add("(" + functionName + "loop)");
		toReturn.addAll(push("constant", 0, false));
		toReturn.addAll(push("temp", 3, false));
		toReturn.addAll(push("constant", 1, false));
		toReturn.addAll(sub());
		toReturn.addAll(pop("temp", 3));
		toReturn.addAll(push("temp", 3, false));
		toReturn.addAll(ifGoTo(functionName + "loop"));*/
		for(int i = 0; i<nVars; i++) {
			toReturn.addAll(push("constant", 0));
		}
		if(functionName.indexOf("main")!=-1) {
			isMain=true;
		} else {
			isMain=false;
		}
		System.out.println(isMain);
		return toReturn;
	}
	
	public ArrayList<String> returnCommand(){
		ArrayList<String> toReturn = new ArrayList<String>();
		//R6 is endFrame
		//R7 is retAddr
		//endFrame=LCL
		toReturn.add("@LCL");
		toReturn.add("D=M");
		toReturn.add("@R14");
		toReturn.add("M=D");
		//retAddr = *(endFrame - 5)
		toReturn.add("@R15");
		toReturn.add("M=D");
		toReturn.add("@5");
		toReturn.add("D=A");
		toReturn.add("@R15");
		toReturn.add("M=M-D");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("@R15");
		toReturn.add("M=D");
		//Pops to ARG
		toReturn.addAll(pop("argument", 0));
		//Restoring SP
		toReturn.add("@ARG");
		toReturn.add("D=M");
		toReturn.add("@SP");
		toReturn.add("M=D+1");
		//Restoring that
		toReturn.add("@R14");
		toReturn.add("D=M");
		toReturn.add("@THAT");
		toReturn.add("M=D");
		toReturn.add("@1");
		toReturn.add("D=A");
		toReturn.add("@THAT");
		toReturn.add("M=M-D");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("@THAT");
		toReturn.add("M=D");
		//Restoring this
		toReturn.add("@R14");
		toReturn.add("D=M");
		toReturn.add("@THIS");
		toReturn.add("M=D");
		toReturn.add("@2");
		toReturn.add("D=A");
		toReturn.add("@THIS");
		toReturn.add("M=M-D");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("@THIS");
		toReturn.add("M=D");
		//Restoring arg
		toReturn.add("@R14");
		toReturn.add("D=M");
		toReturn.add("@ARG");
		toReturn.add("M=D");
		toReturn.add("@3");
		toReturn.add("D=A");
		toReturn.add("@ARG");
		toReturn.add("M=M-D");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("@ARG");
		toReturn.add("M=D");
		//Restoring LCL
		toReturn.add("@R14");
		toReturn.add("D=M");
		toReturn.add("@LCL");
		toReturn.add("M=D");
		toReturn.add("@4");
		toReturn.add("D=A");
		toReturn.add("@LCL");
		toReturn.add("M=M-D");
		toReturn.add("A=M");
		toReturn.add("D=M");
		toReturn.add("@LCL");
		toReturn.add("M=D");
		//Going to retAddr.
		toReturn.add("@R15");
		toReturn.add("A=M");
		System.out.println(isMain);
		//if(!isMain) {
			toReturn.add("0;JMP");
		/*} else {
			toReturnText.add("(LOOP)");
			toReturnText.add("@LOOP");
			toReturnText.add("0;JMP");
		}*/
		return toReturn;
	}
}