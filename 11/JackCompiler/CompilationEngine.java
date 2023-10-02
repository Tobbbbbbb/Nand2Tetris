package Backend;

import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class CompilationEngine{
	
	public TokenCompiler t;
	public static VMWriter vmWriter;
	public SymbolTable symbolTable;
	public ArrayList<String> toTranslateText;
	public int numIndents;
	public String currentInstruction;
	public int line;
	public int numIfLabels;
	public int numElseLabels;
	public int numWhileLabels;
	public int numWhelseLabels;
	public String className;
	
	public String[] op = {
			"+",
			"-",
			"*",
			"/",
			"&amp;",
			"|",
			"&lt;",
			"&gt;",
			"="
	};
	
	public CompilationEngine(ArrayList<String> f) {
		vmWriter = new VMWriter();
		symbolTable = new SymbolTable();
		toTranslateText = f;
		line=0;
		numIfLabels=0;
		numElseLabels=0;
		numWhileLabels=0;
		numWhelseLabels=0;
		compileClass();
		System.out.println("HH");
	}
	
	public static ArrayList<String> getToReturnText() {
		return vmWriter.getToReturnText();
	}
	
	//assume it starts at <FUNCTIONNAME - compile>
	//assume it ends at 1 + </FUNCTIONNAME - compile>
	
	public void compileClass() {
		advance();
		advance();
		className = stripLine();
		System.out.println(currentInstruction);
		System.out.println(stripLine());
		advance();
		advance();
		while(currentInstruction.equals("<classVarDec>")) {
			compileClassVarDec();
		}
		while(currentInstruction.equals("<subroutineDec>")) {
			compileSubroutineDec();
		}
	}
	
	public void compileClassVarDec() {
		advance();
		String kind = stripLine().toUpperCase();
		advance();
		String type = stripLine();
		advance();
		String name = stripLine();
		advance();
		symbolTable.define(name, type, kind);
		while(!stripLine().equals(";")) {
			advance();
			name = stripLine();
			advance();
			symbolTable.define(name, type, kind);
		}
		advance();
		advance();
	}

	public void compileSubroutineDec() {
		symbolTable.startSubroutine(className);
		System.out.println("subrotuineDecCall");
		advance();
		String kind = stripLine();
		advance();
		advance();
		String name = stripLine();
		advance();
		advance();
		if(kind.equals("method")) {
			symbolTable.define("this", className, "ARG");
		}
		int nArgs = compileParameterList();
		advance();
		advance();
		advance();
		while(currentInstruction.equals("<varDec>")) {
			compileVarDec();
		}
		if(kind.equals("function")) {
			vmWriter.writeFunction(className + "." + name, symbolTable.varCount("VAR") + nArgs);
		} else if(kind.equals("method")){
			vmWriter.writeFunction(className + "." + name, symbolTable.varCount("VAR") + nArgs);
		} else {
			vmWriter.writeFunction(className + "." + name, 0);
		}
		System.out.println("SUBROUTINE" + className + "." + name + "," + symbolTable.varCount("VAR"));
		if(kind.equals("constructor")) {
			//push #field vars used?
			//calls "Memory.alloc numArgs-1" not sure about the number. Maybe #static?
			vmWriter.writePush("constant", symbolTable.varCount("FIELD"));
			vmWriter.writeCall("Memory.alloc", 1);
			vmWriter.writePop("pointer", 0);
		} else if (kind.equals("method")) {
			//push arg 0, pop pointer 0
			vmWriter.writePush("argument", 0);
			vmWriter.writePop("pointer", 0);
		} else {
			//push arg 0, pop pointer 0
			//vmWriter.writePush("argument", 0);
			//vmWriter.writePop("pointer", 0);
		}
		compileSubroutineBody(kind);
		System.out.println("SUBROUTINE END" + currentInstruction);
		advance();
	}
	
	public int compileParameterList() {
		advance();
		if(!currentInstruction.equals("</parameterList>")) {
			//Parameter var dec
			String kind = "ARG";
			String type = stripLine();
			advance();
			String name = stripLine();
			advance();
			symbolTable.define(name, type, kind);
			int numArgs=1;
			while(!currentInstruction.equals("</parameterList>")) {
				advance();
				type = stripLine();
				advance();
				name = stripLine();
				advance();
				symbolTable.define(name, type, kind);
				numArgs++;
			}
			advance();
			return numArgs;
		} else {
			advance();
			return 0;
		}
	}
	
	public void compileSubroutineBody(String kind) {
		//vmWriter.writeReturn();
		if(currentInstruction.equals("<statements>")) {
			compileStatements();
		}
		//advance();
		advance();
		advance();
	}
	
	public void compileVarDec() {
		advance();
		String kind = "VAR";
		advance();
		String type = stripLine();
		advance();
		String name = stripLine();
		advance();
		symbolTable.define(name, type, kind);
		while(!stripLine().equals(";")) {
			advance();
			name = stripLine();
			advance();
			symbolTable.define(name, type, kind);
		}
		advance();
		advance();
	}
	
	public void compileStatements() {
		advance();
		while(!currentInstruction.equals("</statements>")) {
			if(currentInstruction.equals("<letStatement>")) {
				compileLet();
			} else if(currentInstruction.equals("<ifStatement>")) {
				compileIf();
			} else if(currentInstruction.equals("<whileStatement>")) {
				compileWhile();
			} else if(currentInstruction.equals("<doStatement>")) {
				compileDo();
			} else if(currentInstruction.equals("<returnStatement>")) {
				compileReturn();
			}
		}
		System.out.println("ENDNEBRASKA" + currentInstruction);
		advance();
	}
	
	public void compileLet() {
		System.out.println("LET" + currentInstruction);
		advance();
		advance();
		String name = stripLine();
		//vmWriter.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
		advance();
		boolean isArray = false;
		if(stripLine().equals("[")) {
			System.out.println("LETARRAY");
			isArray=true;
			advance();
			compileExpression();
			vmWriter.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
			vmWriter.writeArithmetic("ADD");
			advance();
		}
		advance();
		compileExpression();
		if(!isArray) {
			vmWriter.writePop(symbolTable.kindOf(name), symbolTable.indexOf(name));
		} else {
			vmWriter.writePop("temp", 0);
			vmWriter.writePop("pointer", 1);
			vmWriter.writePush("temp", 0);
			vmWriter.writePop("that", 0);
		}
		advance();
		System.out.println("LETEND" + currentInstruction);
		advance();
	}
	
	public void compileIf() {
		System.out.println("IF" + currentInstruction);
		int tempIfLabels = numIfLabels;
		int tempElseLabels = numElseLabels;
		numIfLabels++;
		numElseLabels++;
		advance();
		advance();
		advance();
		compileExpression();
		//vmWriter.writeArithmetic("NOT");
		//int tempIfLabels = numIfLabels;
		//vmWriter.writeIf("ifLabel" + numIfLabels);
		vmWriter.writeIf("IF_TRUE" + tempIfLabels);
		vmWriter.writeGoto("IF_FALSE" + tempElseLabels);
		vmWriter.writeLabel("IF_TRUE" + tempIfLabels);
		//numIfLabels++;
		advance();
		advance();
		compileStatements();
		//int tempElseLabels = numElseLabels;
		//vmWriter.writeGoto("elseLabel" + numElseLabels);
		//vmWriter.writeLabel("ifLabel" + tempIfLabels);
		//numElseLabels++;
		advance();
		if(stripLine().equals("else")) {
			vmWriter.writeGoto("IF_END" + tempIfLabels);
			vmWriter.writeLabel("IF_FALSE" + tempIfLabels);
			advance();
			advance();
			compileStatements();
			advance();
			System.out.println("PRELSEEND" + currentInstruction);
			vmWriter.writeLabel("IF_END" + tempIfLabels);
			
		} else {
			vmWriter.writeLabel("IF_FALSE" + tempIfLabels);
		}
		//vmWriter.writeLabel("elseLabel" + tempElseLabels);
		System.out.println("IFEND" + currentInstruction);
		advance();
	}
	
	public void compileWhile() {
		int tempWhileLabels = numWhileLabels;
		vmWriter.writeLabel("WHILE_EXP" + numWhileLabels);
		numWhileLabels++;
		advance();
		advance();
		advance();
		compileExpression();
		vmWriter.writeArithmetic("NOT");
		int tempWhelseLabels = numWhelseLabels;
		vmWriter.writeIf("WHILE_END" + numWhelseLabels);
		numWhelseLabels++;
		advance();
		advance();
		compileStatements();
		vmWriter.writeGoto("WHILE_EXP" + tempWhileLabels);
		vmWriter.writeLabel("WHILE_END" + tempWhelseLabels);
		advance();
		advance();
	}
	
	public void compileDo() {
		System.out.println("DOSTART" + currentInstruction);
		advance();
		advance();
		//now on subroutinecall
		String name = stripLine();
		boolean call = false;
		boolean dot = false;
		String obj = "";
		advance();
		System.out.println("DODOT" + stripLine());
		if(stripLine().equals(".")) {
			dot = true;
			obj = name;
			advance();
			name = stripLine();
			if(!name.equals("new")) {
				call = true;
			}
			advance();
		}
		//push obj now
		/*if(call) {
			vmWriter.writePush(symbolTable.kindOf(obj), symbolTable.indexOf(obj));
		}*/
		int numArgs = 0;
		advance();
		System.out.println("B4" + currentInstruction);
		//if(!stripLine().equals(")")) {
		if(!dot) {
			vmWriter.writePush("pointer", 0);
		} else {
			if(!symbolTable.kindOf(obj).equals(obj)) {
			vmWriter.writePush(symbolTable.kindOf(obj), symbolTable.indexOf(obj));
			}
		}
			numArgs = compileExpressionList();
		//}
		System.out.println("AFTER");
		advance();
		advance();
		System.out.println("ENDDO" + currentInstruction);
		advance();
		/*if(dot && !call) {
			vmWriter.writeCall(symbolTable.typeOf(obj) + "." + name, numArgs);
		} else {
			vmWriter.writeCall(className + "." + name, numArgs);
		}*/
		if(dot) {
			System.out.println("DOT ACTIVATED");
			if(symbolTable.kindOf(obj).equals(obj)) {
				vmWriter.writeCall(symbolTable.typeOf(obj) + "." + name, numArgs);
			} else {
				vmWriter.writeCall(symbolTable.typeOf(obj) + "." + name, numArgs+1);
			}
		} else {
			vmWriter.writeCall(className + "." + name, numArgs+1);
		}
		vmWriter.writePop("temp", 0);
	}
	
	public void compileReturn() {
		advance();
		advance();
		if(!stripLine().equals(";")) {
			compileExpression();
		} else {
			vmWriter.writePush("constant", 0);
		}
		vmWriter.writeReturn();
		advance();
		System.out.println("ENDRET" + currentInstruction);
		advance();
	}
	
	public void compileExpression() {
		System.out.println("EXPSTART" + currentInstruction);
		advance();
		compileTerm();
		while(!getElement().equals("</expression>")) {
			System.out.println("OHIO" + getElement());
			String op = stripLine();
			System.out.println("OPOPOP" + op);
			advance();
			compileTerm();
			if(op.equals("+")) {
				vmWriter.writeArithmetic("ADD");
			} else if(op.equals("-")) {
				vmWriter.writeArithmetic("SUB");
			} else if(op.equals("*")) {
				vmWriter.writeCall("Math.multiply", 2);
			} else if(op.equals("/")) {
				vmWriter.writeCall("Math.divide", 2);
			} else if(op.equals("&amp;")) {
				vmWriter.writeArithmetic("AND");
			} else if(op.equals("|")) {
				vmWriter.writeArithmetic("OR");
			} else if(op.equals("&lt;")) {
				vmWriter.writeArithmetic("LT");
			} else if(op.equals("&gt;")) {
				vmWriter.writeArithmetic("GT");
			} else if(op.equals("=")) {
				vmWriter.writeArithmetic("EQ");
			} else {
				System.out.println("OPERATOR FAILED: " + op);
			}
			if(!getElement().equals("</expression>"))
			advance();
		}
		System.out.println("EXPEND" + currentInstruction);
		advance();
			
	}
	
	public void compileTerm() {
		System.out.println("START" + currentInstruction);
		advance();
		System.out.println("TERMIS" + getElement());
		boolean isList = false;
		if(stripLine().equals("~")) {
			advance();
			compileTerm();
			isList = true;
			vmWriter.writeArithmetic("NOT");
		} else if (stripLine().equals("-")) {
			advance();
			compileTerm();
			isList = true;
			vmWriter.writeArithmetic("NEG");
		} else if (getElement().equals("<integerConstant>")) {
			vmWriter.writePush("constant", Integer.parseInt(stripLine()));
		} else if (getElement().equals("<StringConstant>")) {
			System.out.println("ISSTRING");
			//FINISH LATER
			//symbolTable.define("temp", "String", "VAR");
			vmWriter.writePush("constant", stripLine().length());
			vmWriter.writeCall("String.new", 1);
			//vmWriter.writePop("local", symbolTable.indexOf("temp"));
			for(int i=0; i<stripLine().length(); i++) {
				//vmWriter.writePush("local", symbolTable.indexOf("temp"));
				vmWriter.writePush("constant", (int) stripLine().charAt(i));
				vmWriter.writeCall("String.appendChar", 2);
				//vmWriter.writePop("local", symbolTable.indexOf("temp"));
			}
		} else if (getElement().equals("<keyword>")) {
			if(stripLine().equals("false")) {
				vmWriter.writePush("constant", 0);
			} else if (stripLine().equals("true")) {
				vmWriter.writePush("constant", 0);
				vmWriter.writeArithmetic("NOT");
			} else if(stripLine().equals("this")) {
				vmWriter.writePush("pointer", 0);
			} else if(stripLine().equals("null")) {
				vmWriter.writePush("constant", 0);
			}
		} else if (stripLine().equals("(")) {
			//isList = true;
			advance();
			compileExpression();
			System.out.println("AFTER" + currentInstruction);
		} else {
			String prev = stripLine();
			advance();
			if(stripLine().equals("[")) {
				vmWriter.writePush(symbolTable.kindOf(prev), symbolTable.indexOf(prev));
				advance();
				compileExpression();
				vmWriter.writeArithmetic("ADD");
				vmWriter.writePop("pointer", 1);
				vmWriter.writePush("that", 0);
			} else if (stripLine().equals("(")) {
				//subroutine call
			} else if (stripLine().equals(".")) {
				System.out.println("SUBCALL" + prev);
				advance();
				String name = symbolTable.typeOf(prev) + "." + stripLine();
				advance();
				advance();
				int nArgs = compileExpressionList();
				if(!symbolTable.typeOf(prev).equals(prev)) {
					vmWriter.writePush(symbolTable.kindOf(prev), symbolTable.indexOf(prev));
					vmWriter.writeCall(name, nArgs+1);
				} else {
					vmWriter.writeCall(name, nArgs);
				}
			} else {
				System.out.println("HUHHHH" + prev);
				vmWriter.writePush(symbolTable.kindOf(prev), symbolTable.indexOf(prev));
				isList=true;
			}
		}
		
		if(!isList) {
		advance();
		}
		System.out.println("TERMEND" + currentInstruction);
		advance();
	}
	
	public boolean isInt(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public int compileExpressionList() {
		System.out.println("EXPLSTART" + currentInstruction);
		advance();
		System.out.println("EXPLEQUALS:" + currentInstruction + " " + currentInstruction.equals("</expressionList>"));
		if(!currentInstruction.equals("</expressionList>")) {
			compileExpression();
			int numArgs = 1;
			while(!getElement().equals("</expressionList>")) {
				advance();
				compileExpression();
				System.out.println("EXPRESS" + currentInstruction);
				numArgs++;
			}
			System.out.println("EXPLEND" + currentInstruction);
			advance();
			return numArgs;
		} else {
			advance();
			return 0;
		}
	}
	
	public void advance() {
		line++;
		currentInstruction = toTranslateText.get(line);
		String tempInstruction = currentInstruction;
		while(tempInstruction.charAt(0)!='<') {
			tempInstruction = tempInstruction.substring(1);
		}
		currentInstruction = tempInstruction;
		System.out.println(line);
		System.out.println(currentInstruction);
	}
	
	public String stripLine() {
		try{
		int temp = currentInstruction.indexOf(">");
		int temp2 = currentInstruction.indexOf("<", temp);
		return currentInstruction.substring(temp+2, temp2-1);
		} catch(Exception e) {
			return "";
		}
	}
	
	public String getElement() {
		try{
			int temp = currentInstruction.indexOf("<");
			int temp2 = currentInstruction.indexOf(">");
			return currentInstruction.substring(temp, temp2+1);
		} catch(Exception e) {
			return "";
		}
	}
	
}