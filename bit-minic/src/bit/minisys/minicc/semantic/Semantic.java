package bit.minisys.minicc.semantic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import bit.minisys.minicc.util.MiniCCUtil;

public class Semantic {
	String path;
	public Semantic(String filePath){
		this.path = filePath;
	}
	public void outputCheckRes(String output) throws IOException{
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String checked = "";
		while((line = br.readLine()) != null){
			line += "\r\n";
//			System.out.println("line ->" + line);
			checked += line;
		}
		MiniCCUtil.createAndWriteFile(output, checked);
		br.close();
	}
	
	public static void main(String[] args) throws IOException{
		Semantic semantic = new Semantic(".\\input\\test.tree.xml");
		semantic.outputCheckRes(".\\input\\test.tree2.xml");
		System.out.println("Semantic finished!");
	}
}
