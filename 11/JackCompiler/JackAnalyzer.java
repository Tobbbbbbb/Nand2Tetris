package Backend;

import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class JackAnalyzer{
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Project number:");
		String proj = in.nextLine();
		System.out.println("Project folder:");
		String progg = in.nextLine();
		System.out.println("Project file:");
		String prog = in.nextLine();
		File file = new File("/Users/actionbuilder/Desktop/nand2tetris/projects/" + proj + "/" + progg + "/" + prog + ".jack");
		//System.out.println(file.getPath());
		TokenCompiler c = new TokenCompiler(file, prog);
		ArrayList<String> temp = c.getToReturnText();
		try{
			System.out.println("start");
			//System.out.println("1");
			File output = new File(file.getParent() + "/" + prog + "2.xml");
			//output.delete();
			//output = new File(f.getParent() + "/" + prog + "1.xml");
			PrintWriter writer = new PrintWriter(output);
			for(int i = 0; i < temp.size(); i++){
				writer.println(temp.get(i));
			}
			writer.close();
			System.out.println("finish");
		} catch (Exception e){
			System.out.println("You are a failure");
		}
		CompilationEngine comp = new CompilationEngine(temp);
		ArrayList<String> toReturnText = comp.getToReturnText();
		try{
			System.out.println("start");
			//System.out.println("1");
			File output = new File(file.getParent() + "/" + prog + ".vm");
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
		}
	}
}