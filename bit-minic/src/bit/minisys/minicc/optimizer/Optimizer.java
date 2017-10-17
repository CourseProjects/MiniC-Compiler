package bit.minisys.minicc.optimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import bit.minisys.minicc.util.MiniCCUtil;

public class Optimizer {
	String path;
	public Optimizer(String filePath){
		this.path = filePath;
	}
	public void outputOptimized(String output) throws IOException{
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String checked = "";
		while((line = br.readLine()) != null){
			line += "\r\n";
			checked += line;
		}
		MiniCCUtil.createAndWriteFile(output, checked);
		br.close();
	}
	
	public static void main(String[] args) throws IOException{
		Optimizer opt = new Optimizer(".\\input\\test.ic.xml");
		opt.outputOptimized(".\\input\\test.ic2.xml");
		System.out.println("Opt finished!");
	}
}
