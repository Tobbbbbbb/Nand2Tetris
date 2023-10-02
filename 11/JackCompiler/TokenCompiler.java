package Backend;

import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class TokenCompiler{
	
	public static ArrayList<String> toTranslateText;
	public int numIndents = 0;
	public static ArrayList<String> toReturnText;
	public int line = 0;
	
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
		toTranslateText = j.getToReturnText();
		CompilationEngine e = new CompilationEngine(file, prog);
	}*/
	
	public static ArrayList<String> getToReturnText() {
		return toReturnText;
	}
	
	public TokenCompiler(File f, String prog) {
		JackTokenizer j = new JackTokenizer(f, prog);
		toTranslateText = j.getToReturnText();
		System.out.println("translated");
		toReturnText=new ArrayList<String>();
		compileClass();
		
		
		
		/*try{
			System.out.println("start");
			//System.out.println("1");
			File output = new File(f.getParent() + "/" + prog + "2.xml");
			//output.delete();
			//output = new File(f.getParent() + "/" + prog + "1.xml");
			PrintWriter writer = new PrintWriter(output);
			for(int i = 0; i < toReturnText.size(); i++){
				writer.println(toReturnText.get(i));
			}
			writer.close();
			System.out.println("finish");
		} catch (Exception e){
			System.out.println("You are a failure");
		}*/
		
	}
	
	public void compileClass() {
		System.out.println("class");
		openCompile("class");
		compileLine();
		compileLine();
		compileLine();
			while(toTranslateText.get(line).equals("<keyword> static </keyword>") || toTranslateText.get(line).equals("<keyword> field </keyword>")) {
				System.out.println("hey");
				compileClassVarDec();
			}
			while(toTranslateText.get(line).equals("<keyword> constructor </keyword>") || toTranslateText.get(line).equals("<keyword> function </keyword>") || toTranslateText.get(line).equals("<keyword> method </keyword>")){
				System.out.println("ho");
				compileSubroutineDec();
			}
		compileLine();
		closeCompile("class");
	}
	
	public void compileClassVarDec() {
		System.out.println("classVarDec");
		openCompile("classVarDec");
		compileLine();
		System.out.println(toTranslateText.get(line));
		compileLine();
		System.out.println(toTranslateText.get(line));
		compileLine();
		System.out.println(toTranslateText.get(line));
		
		while(!toTranslateText.get(line).equals("<symbol> ; </symbol>")) {
			compileLine();
			compileLine();
		}
		
		System.out.println(toTranslateText.get(line));
		compileLine();
		System.out.println(toTranslateText.get(line));
		closeCompile("classVarDec");
	}
	
	public void compileSubroutineDec() {
		System.out.println("subroutineDec");
		openCompile("subroutineDec");
		compileLine();
		compileLine();
		compileLine();
		compileLine();
		
		compileParameterList();
		
		compileSubroutineBody();
		
		closeCompile("subroutineDec");
	}
	
	public void compileParameterList() {
		System.out.println("parameterList");
		openCompile("parameterList");
		
		if(!toTranslateText.get(line).equals("<symbol> ) </symbol>")) {
			compileLine();
			compileLine();
		}
		
		while(!toTranslateText.get(line).equals("<symbol> ) </symbol>")) {
			compileLine();
			compileLine();
			compileLine();
		}
		
		closeCompile("parameterList");
		
		compileLine();
	}
	
	public void compileSubroutineBody() {
		System.out.println("subroutineBody");
		openCompile("subroutineBody");
		
		compileLine();
		
		System.out.println("fynite");
		
		while(toTranslateText.get(line).equals("<keyword> var </keyword>")) {
			compileVarDec();
		}
		compileStatements();
		
		compileLine();
		
		closeCompile("subroutineBody");
	}
	
	public void compileVarDec() {
		System.out.println("varDec");
		openCompile("varDec");
		
		compileLine();
		compileLine();
		compileLine();
		
		System.out.println("finite");
		
		while(toTranslateText.get(line).equals("<symbol> , </symbol>")) {
			compileLine();
			compileLine();
		}
		
		compileLine();
		
		closeCompile("varDec");
	}
	
	public void compileStatements() {
		System.out.println("statement");
		openCompile("statements");
		
		while(toTranslateText.get(line).equals("<keyword> let </keyword>") || toTranslateText.get(line).equals("<keyword> if </keyword>") || toTranslateText.get(line).equals("<keyword> while </keyword>") || toTranslateText.get(line).equals("<keyword> do </keyword>") || toTranslateText.get(line).equals("<keyword> return </keyword>")) {
			if(toTranslateText.get(line).equals("<keyword> let </keyword>")) {
				compileLet();
			} else if(toTranslateText.get(line).equals("<keyword> if </keyword>")) {
				compileIf();
			} else if(toTranslateText.get(line).equals("<keyword> while </keyword>")) {
				compileWhile();
			} else if(toTranslateText.get(line).equals("<keyword> do </keyword>")) {
				compileDo();
			} else if(toTranslateText.get(line).equals("<keyword> return </keyword>")) {
				compileReturn();
			}
		}
		
		closeCompile("statements");
	}
	
	public void compileLet() {
		System.out.println("letStatement");
		openCompile("letStatement");

		compileLine();
		compileLine();
		
		System.out.println(toTranslateText.get(line));
		if(toTranslateText.get(line).equals("<symbol> [ </symbol>")) {
			compileLine();
			compileExpression();
			compileLine();
		}
		compileLine();
		compileExpression();
		compileLine();
		
		closeCompile("letStatement");
	}
	
	public void compileIf() {
		System.out.println("ifStatement");
		openCompile("ifStatement");
		
		compileLine();
		compileLine();
		compileExpression();
		compileLine();
		compileLine();
		compileStatements();
		compileLine();
		if(toTranslateText.get(line).equals("<keyword> else </keyword>")) {
			compileLine();
			compileLine();
			compileStatements();
			compileLine();
		}
		
		closeCompile("ifStatement");
	}
	
	public void compileWhile() {
		System.out.println("whileStatement");
		openCompile("whileStatement");

		compileLine();
		compileLine();
		compileExpression();
		compileLine();
		compileLine();
		compileStatements();
		compileLine();
		
		closeCompile("whileStatement");
	}
	
	public void compileDo() {
		System.out.println("doStatement");
		openCompile("doStatement");

		compileLine();
		compileLine();
		if(toTranslateText.get(line).equals("<symbol> . </symbol>")) {
			compileLine();
			compileLine();
		}
		compileLine();
		compileExpressionList();
		compileLine();
		
		closeCompile("doStatement");
	}
	
	public void compileReturn() {
		System.out.println("returnStatement");
		openCompile("returnStatement");

		compileLine();
		if(!toTranslateText.get(line).equals("<symbol> ; </symbol>")) {
			compileExpression();
		}
		compileLine();
		
		closeCompile("returnStatement");
	}
	
	public void compileExpression() {
		System.out.println("expression");
		openCompile("expression");

		compileTerm();
		boolean opp = false;
		for(int i = 0; i<op.length; i++) {
			if(toTranslateText.get(line).equals("<symbol> " + op[i] + " </symbol>")) {
				opp=true;
				break;
			}
		}
		while(opp) {
			compileLine();
			
			compileTerm();
			
			for(int i = 0; i<op.length; i++) {
				if(toTranslateText.get(line).equals("<symbol> " + op[i] + " </symbol>")) {
					opp=true;
					break;
				}
				if(i==op.length-1) {
					opp=false;
				}
			}
		}
		
		closeCompile("expression");
	}
	
	public void compileTerm() {
		System.out.println("term");
		openCompile("term");
		
		if(toTranslateText.get(line).equals("<symbol> - </symbol>") || toTranslateText.get(line).equals("<symbol> ~ </symbol>")) {
			compileLine();
			compileTerm();
		} else if (toTranslateText.get(line).equals("<symbol> ( </symbol>")){
			compileLine();
			compileExpression();
			compileLine();
		} else {
		compileLine();
			if(toTranslateText.get(line).equals("<symbol> [ </symbol>")) {
				compileLine();
				compileExpression();
				compileLine();
			} else if (toTranslateText.get(line).equals("<symbol> ( </symbol>")) {
				compileLine();
				compileExpressionList();
				compileLine();
			} else if(toTranslateText.get(line).equals("<symbol> . </symbol>")) {
				compileLine();
				compileLine();
				compileLine();
				compileExpressionList();
			}
		}
		
		closeCompile("term");
	}
	
	public void compileExpressionList() {
		System.out.println("expressionList");
		openCompile("expressionList");

		if(!toTranslateText.get(line).equals("<symbol> ) </symbol>")) {
			compileExpression();
		}
		while(!toTranslateText.get(line).equals("<symbol> ) </symbol>")) {
			compileLine();
			compileExpression();
		}
		
		closeCompile("expressionList");
		compileLine();
	}
	
	public void compileLine() {
		String cur="";
		for(int i=0; i<numIndents;i++) {
			cur += "\s\s";
		}
		//System.out.println(toTranslateText.get(line));
		toReturnText.add(cur + toTranslateText.get(line));
		line++;
	}
	
	public void openCompile(String str) {
		String cur = "";
		for(int i=0;i<numIndents;i++) {
			cur += "\s\s";
		}
		cur += "<" + str + ">";
		toReturnText.add(cur);
		numIndents++;
	}
	
	public void closeCompile(String str) {
		String cur = "";
		numIndents--;
		for(int i=0;i<numIndents;i++) {
			cur += "\s\s";
		}
		cur += "</" + str + ">";
		toReturnText.add(cur);
	}
}