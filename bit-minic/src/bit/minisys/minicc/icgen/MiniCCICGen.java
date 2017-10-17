package bit.minisys.minicc.icgen;

import java.io.IOException;

public class MiniCCICGen implements IMiniCCICGen {
	public void run(String iFile, String oFile) throws IOException {
		ICGen icGen = new ICGen(iFile);
		icGen.outputICGenerated(oFile);
		System.out.println("5. Intermediate code generate finished!");
	}
}
