package bit.minisys.minicc.parser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class MiniCCParser implements IMiniCCParser{
	public void run(String iFile, String oFile) throws ParserConfigurationException, SAXException, IOException{
		Parser parser = new Parser(iFile);
		parser.grammerAnalyse();
		parser.outputLL1();
		parser.outputTree(oFile);
		System.out.println("3. Parse finished!");
	}
}
