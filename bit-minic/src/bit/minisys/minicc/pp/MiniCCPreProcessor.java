package bit.minisys.minicc.pp;

/**
 * 
 * @author WYF
 * @version 0.1
 * This is a simple and minimum preprocessor 
 */
public class MiniCCPreProcessor implements IMiniCCPreProcessor {
	/**
	 * this is a demo preprocessor that removes \r\n and single line comments
	 * @param iFile : input file
	 * @param oFile : output file
	 */
	public void run(String iFile, String oFile){
		PreProcessor pp = new PreProcessor(iFile);
		pp.preProcess(oFile);
		System.out.println("1. PreProcess finished!");
	}
	
}
