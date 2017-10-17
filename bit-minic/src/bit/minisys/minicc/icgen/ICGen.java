package bit.minisys.minicc.icgen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * �Ȱ�xml�б��ʽ�����ݶ�������Ȼ��������Ԫʽ�ŵ�һ��list��
 * Ȼ�����ɶ�Ӧ��xml�ļ�
 * */
public class ICGen {
	String path;//�����xml·��
	ArrayList<FourElement>fourElemList = new ArrayList<FourElement>();//��Ԫʽ�б�
	Stack<String>stack = new Stack<String>();//������������
	int tempCount = 0;//����������ʱ����
	int fourElemCount = 0;//ͳ����Ԫʽ����
	
	public ICGen(String filePath){
		this.path = filePath;
	}
	public void outputICGenerated(String output) throws IOException{
		try {
			generate();
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outputXML(output);
	}
	private void generate() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.parse(path);
//		System.out.println("path: " + path);
		NodeList nodeList = doc.getElementsByTagName("STMTS");
//		System.out.println("There are " + NODEList.getLength() + " STMTS");
		for (int i = 0; i < nodeList.getLength(); i++){
			Node stmt = nodeList.item(i);
//			System.out.println(stmt);
			deal(stmt.getFirstChild(), 0);
			deal(stmt.getNextSibling(), 0);
			
			String arg1, arg2, res, op;
			while(!stack.isEmpty()){
				arg2 = stack.pop();
				if(arg2.equals(";")){
					continue;
				}
				if(stack.isEmpty()){
					break;
				}
				op = stack.pop();
				if(op.equals(";")){
					continue;
				}
				if(stack.isEmpty()){
					FourElement fourElement = new FourElement(++fourElemCount, op, null, arg2, null);
					fourElemList.add(fourElement);
					continue;
				}
				if(op.equals("return")){
					FourElement fourElement = new FourElement(++fourElemCount, op, null, arg2, null);
					fourElemList.add(fourElement);
					continue;
				}
				if(op.equals("=")){
					FourElement fourElement = new FourElement(++fourElemCount, op, null, arg2, stack.pop());
					fourElemList.add(fourElement);
					continue;
				}
				res = newTemp();
				arg1 = stack.pop();
				if(arg1.equals(";")){
					continue;
				}
				FourElement fourElement = new FourElement(++fourElemCount, op, arg1, arg2, res);
				fourElemList.add(fourElement);
				stack.push(res);
			}
			
		}
	}
	private String newTemp(){
		tempCount++;
		return "T" + tempCount;
	}
	void deal(Node node, int depth){
		if(node == null){
			return;
		}
		if(node.getNodeName().equals("STMTS")){
			return;
		}
		switch (node.getNodeName()) {
		case "keyword":
			
		case "identifier":
			
		case "operator":
//			System.out.println("node.getNodeName ->" + node.getNodeName());
//			System.out.println("node.getFirstChild().getNodeValue() ->" + node.getFirstChild().getNodeValue());
//			stack.push(node.getFirstChild().getNodeValue());
//			break;
			
		case "separator":
			stack.push(node.getFirstChild().getNodeValue());
			break;

		default:
			break;
		}
		
		
		deal(node.getFirstChild(), depth + 1);
		deal(node.getNextSibling(), depth + 1);
	}
	private void outputXML(String output) throws IOException{
		// �������ڵ� �������������� ;   
        Element root = new Element("IC").setAttribute("name", "test.ic.xml");   
        // �����ڵ���ӵ��ĵ��У�   
        Document Doc = new Document(root);
        // ����tokens�ڵ㲢��ӵ�root
        Element functions = new Element("functions"); 
        root.addContent(functions);

		// �����ڵ� function;   
        Element elements = new Element("function");  
        
        FourElement fourElement;
		for (int i = 0; i < fourElemList.size(); i++) {
			fourElement = fourElemList.get(i);

	        Element ele = new Element("quaternion");
	        if(fourElement.getResult() == null){
	        	ele.setAttribute("result", "");
	        }else{
		        ele.setAttribute("result", fourElement.getResult().toString());
	        }
	        if(fourElement.getArg2() == null){
	        	ele.setAttribute("arg2", "");
	        }else{
		        ele.setAttribute("arg2", fourElement.getArg2());
	        }
	        if(fourElement.getArg1() == null){
	        	ele.setAttribute("arg1", "");
	        }else{
		        ele.setAttribute("arg1", fourElement.getArg1());
	        }
	        if(fourElement.getOp() == null){
	        	ele.setAttribute("op", "");
	        }else{
		        ele.setAttribute("op", fourElement.getOp());
	        }
	        ele.setAttribute("addr", new Integer(fourElement.getId()).toString());
			elements.addContent(ele);
		}
        // �ѽڵ���ӵ�root��
        functions.addContent(elements);
        
        // ��� 
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);
        XMLOut.output(Doc, new FileOutputStream(output));
	}
	
	public static void main(String[] args) throws IOException{
		ICGen icGen = new ICGen(".\\input\\test.tree2.xml");
		icGen.outputICGenerated(".\\input\\test.ic.xml");
		System.out.println("ICGen finished!");
	}
}
