package bit.minisys.minicc.scanner;

import java.io.IOException;

public class MiniCCScanner implements IMiniCCScanner{

	public void run(String iFile, String oFile) throws IOException{
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse1(iFile);
		lex.outputWordList(oFile);
		System.out.println("2. LexAnalyse finished!");
	}
}
