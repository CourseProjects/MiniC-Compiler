package bit.minisys.minicc.pp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import bit.minisys.minicc.util.MiniCCUtil;

public class PreProcessor {
	String path;
	public PreProcessor(String filePath) {
		// TODO Auto-generated constructor stub
		path = filePath;
	}
	public void preProcess(String output){
		if(!MiniCCUtil.checkFile(path)){
			return;
		}
		String processed = "";
		File file = new File(path);
		// read the file
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {  
				// remove /r/n
//				line = line.replace("\r\n", "");
				// remove comment annotated with //
				int start = line.indexOf("//");
				if(start == 0){
					continue;
				}
				else if(start > 0){
					line = line.substring(0, start);
				}
				// remove comment annotated with /**/
				// for /**/ in one line
				start = line.indexOf("/*");
				int end = line.indexOf("*/");
				if(start >= 0 && end >= 0 && end - start >= 2){
					line = line.substring(0, start);
				}
				// append line to the output
				processed += line;
				processed += "\r\n";
			}
			reader.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MiniCCUtil.createAndWriteFile(output, processed);
	}
	
	public static void main(String[] args){
		PreProcessor pp = new PreProcessor(".\\input\\test.c");
		pp.preProcess(".\\input\\test.pp.c");
		System.out.println("PreProcess finished!");
	}
}
