package bit.minisys.minicc.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bit.minisys.minicc.icgen.FourElement;


public class CodeGen {
	
	ArrayList<FourElement>fourElementList = new ArrayList<FourElement>();
	int fourEleCount = 0;
	StringBuffer data = null;//定义变量
	StringBuffer text = null;//生成代码
	ArrayList<String> varList = new ArrayList<String>();//定义过的变量列表
	
	public CodeGen(String path) throws IOException, ParserConfigurationException, SAXException {//从所给path指向的四元组文件中读取四元组，存入fourElements
		// TODO Auto-generated constructor stub
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(path);
//		System.out.println("path: " + path);
		NodeList nodeList = doc.getElementsByTagName("quaternion");
		for (int i = 0; i < nodeList.getLength(); i++){
			Element temp = (Element) nodeList.item(i);
//			System.out.println(temp);
			FourElement fourElement = new FourElement(++fourEleCount, temp.getAttribute("op"), 
					temp.getAttribute("arg1"), temp.getAttribute("arg2"), 
					temp.getAttribute("result"));
			fourElementList.add(fourElement);
		}
		
//		//控制台输出四元组
//		int i = 0;
//		FourElement temp = null;
//		for(; i < fourElementList.size(); i++){
//			temp = fourElementList.get(i);
//			System.out.println("fourElements: " + temp.getId() + "(" + temp.getOp() +
//					"," + temp.getArg1() + "," + temp.getArg2() +  "," + temp.getResult() + 
//					")");
//		}
	}
	
	public boolean isDefined(String s){//判断变量是否定义过，定义过返回true
		if(varList.contains(s)){
			return true;
		}else{
			return false;
		}
	}
	public void generate(String output)throws IOException{
		File file = new File(output);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		data = new StringBuffer();
		text = new StringBuffer();
		data.append(".data\r\n");
		text.append(".text\r\n.globl main\r\nmain:\r\n");
		int i = 0;
		String res = null;
		String arg1 = null;
		String arg2 = null;
		
		//遍历四元组list，对每个四元组生成代码
		for(i = 0; i < fourElementList.size(); i++){
			FourElement fourElement = fourElementList.get(i);
			switch(fourElement.getOp()){
			case "=":
				res = fourElement.getResult().toString();
				checkAndDefine(res);
				arg2 = fourElement.getArg2();
				checkAndDefine(arg2);
				
				text.append("\tla $a0, " + res + "\r\n");
				text.append("\tla $v1, " + arg2 + "\r\n");
				text.append("\tsw $v1, 0($v0)\r\n");
				text.append("\tsw $v0, 0($a0)\r\n");
				break;
			case "+":
			case "-":
			case "*":
			case "/":
				res = fourElement.getResult().toString();
				checkAndDefine(res);
				arg1 = fourElement.getArg1();
				checkAndDefine(arg1);
				arg2 = fourElement.getArg2();
				checkAndDefine(arg2);
				
				text.append("\tla $a0, " + res + "\r\n");
				text.append("\tla $t1, " + arg1 + "\r\n");
				text.append("\tla $t2, " + arg2 + "\r\n");
				text.append("\tadd $t3, $t1, $t2\r\n");
				text.append("\tsw $t3, 0($a0)\r\n");
				break;
				
			default:
//					System.out.println("OP: " + fourElement.getOp());
					break;
			}
		}
		
		//输出代码
		bw.write(data.toString());
		bw.write(text.toString());
		bw.close();
	}
	private void checkAndDefine(String var){
		if(!this.isDefined(var)){//如果没定义过，则定义
			data.append(var + ": .word\r\n");//假定为int型变量
			varList.add(var);
		}
	}
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
		CodeGen codeGen = new CodeGen(".\\input\\test.ic2.xml");
		codeGen.generate(".\\input\\test.code.s");
		System.out.println("Code Generating Finished!");
	}
}
