package bit.minisys.minicc.codegen;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class MiniCCCodeGen implements IMiniCCCodeGen{
	public void run(String iFile, String oFile) throws IOException, ParserConfigurationException, SAXException{
		CodeGen codeGen = new CodeGen(iFile);
		codeGen.generate(oFile);
		System.out.println("7. Code generate finished!");
	}
}
