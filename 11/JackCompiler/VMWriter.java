package Backend;

import java.util.*;
public class VMWriter{
	
	public ArrayList<String> toReturnText;
	
	public VMWriter() {
		toReturnText = new ArrayList<String>();
		System.out.println("hey");
	}

	//segment is either CONST ARG LOCAL STATIC THIS THAT or POINTER
	public void writePush(String segment, int index) {
		toReturnText.add("push " + segment.toLowerCase() + " " + index);
	}
	
	//segment is either CONST ARG LOCAL STATIC THIS THAT or POINTER
	public void writePop(String segment, int index) {
		toReturnText.add("pop " + segment.toLowerCase() + " " + index);
	}
	
	//command is either ADD SUB NEG EQ GT LT AND OR or NOT
	public void writeArithmetic(String command) {
		toReturnText.add(command.toLowerCase());
	}
	
	public void writeLabel(String label) {
		toReturnText.add("label " + label.toLowerCase());
	}
	
	public void writeGoto(String label) {
		toReturnText.add("goto " + label.toLowerCase());
	}
	
	public void writeIf(String label) {
		toReturnText.add("if-goto " + label.toLowerCase());
	}
	
	public void writeCall(String name, int nArgs) {
		toReturnText.add("call " + name + " " + nArgs);
	}
	
	public void writeFunction(String name, int nLocals) {
		toReturnText.add("function " + name + " " + nLocals);
	}
	
	public void writeReturn() {
		toReturnText.add("return");
	}
	
	//closes output file
	public void close() {
		
	}
	
	public ArrayList<String> getToReturnText(){
		System.out.println("EE");
		for(int i=0; i<toReturnText.size();i++) {
			System.out.println(toReturnText.get(i));
		}
		return toReturnText;
	}
}