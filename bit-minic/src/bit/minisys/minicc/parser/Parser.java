package bit.minisys.minicc.parser;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.*;
import org.xml.sax.*;

import bit.minisys.minicc.scanner.Word;
import bit.minisys.minicc.scanner.Error;
/**
 * �﷨������
 * @author Wu Yifan
 * ʹ��DOM,SAX����XML
 *
 */
public class Parser {

	/**
	 * @param args
	 */
	
	ArrayList<Word>wordList = new ArrayList<Word>();//���ʱ�
	Stack<AnalyseNode>analyseStack = new Stack<AnalyseNode>();//����ջ
	Stack<String>semanticStack = new Stack<String>();//����ջ
	ArrayList<Error>errorList = new ArrayList<Error>();//������Ϣ�б�
	StringBuffer bf;//����ջ������
	int errorCount = 0;//ͳ�ƴ������
	boolean graErrorFlag = false;//�﷨���������־
	int tempCount = 0;//����������ʱ����
	int fourElemCount = 0;//ͳ����Ԫʽ����
	AnalyseNode top;//��ǰջ��Ԫ��
	AnalyseNode PROGRAM, FUNCTIONS, FUNLIST, FUNCTION, ARGS, ALIST, FARGS, FUNC_BODY, STMTS, STMT, 
				EXPR_STMT, RET_STMT, FOR_STMT, IF_STMT, EXPR, FACTOR, FLIST, EARGS, EALIST, 
				ELSEIF, ILIST, TYPE;//���ս��
	AnalyseNode TKN_ID, TKN_CONST_I, TKN_LP, TKN_RP, TKN_COMMA, TKN_LB, TKN_RB, TKN_SEMICOLON, TKN_KW_RET, 
				TKN_PLUS, TKN_MINUS, TKN_LESS, TKN_DIV, TKN_ASN, TKN_INT, TKN_FLOAT, 
				TKN_FOR, TKN_IF, TKN_ELSE;//�ս��
	Word firstWord;//����������
	String OP = null;
	String ARG1, ARG2, RES;
	Error error;
	Stack<Integer>if_fj,if_rj,while_fj,while_rj,for_fj,for_rj;//if while for ��ת��ַջ
	Stack<String>for_op=new Stack<String>();
	
public Parser(){
		
}
public Parser(String filePath) throws ParserConfigurationException, SAXException, IOException{//��xml�ļ��л�ȡwordlist
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	org.w3c.dom.Document doc = db.parse(filePath);
//	System.out.println("filePath: " + filePath);
	NodeList wordList = doc.getElementsByTagName("token");
//	System.out.println("There are " + wordList.getLength() + " token");
	for (int i = 0; i < wordList.getLength(); i++){
		Node token = wordList.item(i);
//		System.out.println(tokens);
		Word word = new Word();
		/*
		 * ��5��ѭ������ȡtoken�е�5����Ԫ��
		 * ֮������ѭ����Ϊ����ȥ#text�����
		 * node instanceof Text�ĳ��ϣ�����ǣ�#text
		 * node instanceof Element�ĳ��ϣ�����ǣ���ǩ��
		 * #text��������Ϊdom����xml�Ĺ����аѻس�Ҳ�����ڵ�
		 * */
		Node node = token.getFirstChild();
		for (; node != null; node = node.getNextSibling()){
			if (node.getNodeType() == Node.ELEMENT_NODE){//����[#text:]�ĳ���
				word.id = Integer.parseInt(node.getFirstChild().getNodeValue());
				break;
			}
		}
		for (node = node.getNextSibling(); node != null; node = node.getNextSibling()){
			if (node.getNodeType() == Node.ELEMENT_NODE){//����[#text:]�ĳ���
				word.value = node.getFirstChild().getNodeValue();
				break;
			}
		}
		for (node = node.getNextSibling(); node != null; node = node.getNextSibling()){
			if (node.getNodeType() == Node.ELEMENT_NODE){//����[#text:]�ĳ���
				word.type = node.getFirstChild().getNodeValue();
				break;
			}
		}
		for (node = node.getNextSibling(); node != null; node = node.getNextSibling()){
			if (node.getNodeType() == Node.ELEMENT_NODE){//����[#text:]�ĳ���
				word.line = Integer.parseInt(node.getFirstChild().getNodeValue());
				break;
			}
		}
		for (node = node.getNextSibling(); node != null; node = node.getNextSibling()){
			if (node.getNodeType() == Node.ELEMENT_NODE){//����[#text:]�ĳ���
				word.flag = Boolean.getBoolean(node.getFirstChild().getNodeValue());
				break;
			}
		}
		this.wordList.add(word);
//		System.out.println("word.no: " + word.id + "  word.value: " + word.value);

	}
	init();
	
}

public void init(){
	PROGRAM = new AnalyseNode(AnalyseNode.NONTERMINAL, "PROGRAM", null, null);
	FUNCTIONS = new AnalyseNode(AnalyseNode.NONTERMINAL, "FUNCTIONS", null, null);
	FUNLIST = new AnalyseNode(AnalyseNode.NONTERMINAL, "FUNLIST", null, null);
	FUNCTION = new AnalyseNode(AnalyseNode.NONTERMINAL, "FUNCTION", null, null);
	ARGS = new AnalyseNode(AnalyseNode.NONTERMINAL, "ARGS", null, null);
	ALIST = new AnalyseNode(AnalyseNode.NONTERMINAL, "ALIST", null, null);
	FARGS = new AnalyseNode(AnalyseNode.NONTERMINAL, "FARGS", null, null);
	FUNC_BODY = new AnalyseNode(AnalyseNode.NONTERMINAL, "FUNC_BODY", null, null);
	STMTS = new AnalyseNode(AnalyseNode.NONTERMINAL, "STMTS", null, null);
	STMT = new AnalyseNode(AnalyseNode.NONTERMINAL, "STMT", null, null);
	EXPR_STMT = new AnalyseNode(AnalyseNode.NONTERMINAL, "EXPR_STMT", null, null);
	EXPR = new AnalyseNode(AnalyseNode.NONTERMINAL, "EXPR", null, null);
	FACTOR = new AnalyseNode(AnalyseNode.NONTERMINAL, "FACTOR", null, null);
	FLIST = new AnalyseNode(AnalyseNode.NONTERMINAL, "FLIST", null, null);
	EARGS = new AnalyseNode(AnalyseNode.NONTERMINAL, "EARGS", null, null);
	EALIST = new AnalyseNode(AnalyseNode.NONTERMINAL, "EALIST", null, null);
	RET_STMT = new AnalyseNode(AnalyseNode.NONTERMINAL, "RET_STMT", null, null);
	FOR_STMT = new AnalyseNode(AnalyseNode.NONTERMINAL, "FOR_STMT", null, null);
	IF_STMT = new AnalyseNode(AnalyseNode.NONTERMINAL, "IF_STMT", null, null);
	ELSEIF = new AnalyseNode(AnalyseNode.NONTERMINAL, "ELSEIF", null, null);
	ILIST = new AnalyseNode(AnalyseNode.NONTERMINAL, "ILIST", null, null);
	TYPE = new AnalyseNode(AnalyseNode.NONTERMINAL, "TYPE", null, null);
	
	TKN_ID = new AnalyseNode(AnalyseNode.TERMINAL, "id", null, Word.IDENTIFIER);
	TKN_CONST_I = new AnalyseNode(AnalyseNode.TERMINAL, "const_i", null, Word.INT_CONST);
	TKN_LP = new AnalyseNode(AnalyseNode.TERMINAL, "(", null, Word.BOUNDARYSIGN);
	TKN_RP = new AnalyseNode(AnalyseNode.TERMINAL, ")", null, Word.BOUNDARYSIGN);
	TKN_COMMA = new AnalyseNode(AnalyseNode.TERMINAL, ",", null, Word.BOUNDARYSIGN);
	TKN_LB = new AnalyseNode(AnalyseNode.TERMINAL, "{", null, Word.BOUNDARYSIGN);
	TKN_RB = new AnalyseNode(AnalyseNode.TERMINAL, "}", null, Word.BOUNDARYSIGN);
	TKN_SEMICOLON = new AnalyseNode(AnalyseNode.TERMINAL, ";", null, Word.BOUNDARYSIGN);
	TKN_KW_RET = new AnalyseNode(AnalyseNode.TERMINAL, "return", null, Word.KEY);
	TKN_PLUS = new AnalyseNode(AnalyseNode.TERMINAL, "+", null, Word.OPERATOR);
	TKN_MINUS = new AnalyseNode(AnalyseNode.TERMINAL, "-", null, Word.OPERATOR);
	TKN_LESS = new AnalyseNode(AnalyseNode.TERMINAL, "<", null, Word.OPERATOR);
	TKN_DIV = new AnalyseNode(AnalyseNode.TERMINAL, "/", null, Word.OPERATOR);
	TKN_ASN = new AnalyseNode(AnalyseNode.TERMINAL, "=", null, Word.OPERATOR);
	TKN_INT = new AnalyseNode(AnalyseNode.TERMINAL, "int", null, Word.KEY);
	TKN_FLOAT = new AnalyseNode(AnalyseNode.TERMINAL, "float", null, Word.KEY);
	TKN_FOR = new AnalyseNode(AnalyseNode.TERMINAL, "for", null, Word.KEY);
	TKN_IF = new AnalyseNode(AnalyseNode.TERMINAL, "if", null, Word.KEY);
	TKN_ELSE = new AnalyseNode(AnalyseNode.TERMINAL, "else", null, Word.KEY);
	
	if_fj=new Stack<Integer>();
	if_rj=new Stack<Integer>();
	while_fj=new Stack<Integer>();
	while_rj=new Stack<Integer>();
	for_fj=new Stack<Integer>();
	for_rj=new Stack<Integer>();
	
}
public void grammerAnalyse(){//LL1�������������﷨����
	bf = new StringBuffer();
	int gcount = 0;
	error = null;
	analyseStack.add(0,PROGRAM);
	analyseStack.add(1,new AnalyseNode(AnalyseNode.END, "#", null, null));
	semanticStack.add("#");
	while(!analyseStack.empty()&&!wordList.isEmpty()){
		bf.append("����"+gcount+"\t");
		if(gcount++>10000){
			graErrorFlag=true;
			break;
		}
		top=analyseStack.get(0);//��ǰջ��Ԫ��
		firstWord=wordList.get(0);//����������
		
//		System.out.println("top.name = " + top.name);
//		System.out.println("firstWord.value = " + firstWord.value);
		
		if(firstWord.value.equals("#")
				&&top.name.equals("#")){// ��������
			bf.append("\n");
			analyseStack.remove(0);
			wordList.remove(0);
			
		}
		else if(top.name.equals("#")){
			analyseStack.remove(0);
			graErrorFlag=true;
			break;
			
		}
		else if(AnalyseNode.isTerm(top)){//�ս��ʱ�Ĵ���
			 termOP(top.name);
		}else if(AnalyseNode.isNonterm(top)){//���ս��ʱ�Ĵ���
			nonTermOP(top.name);	
		}
		
		bf.append("��ǰ����ջ:");
		for(int i = 0; i < analyseStack.size(); i++){
			bf.append(analyseStack.get(i).name);
		}
		bf.append("\t").append("�������Ŵ���");
		for(int j = 0; j < wordList.size(); j++){
			bf.append(wordList.get(j).value);
		}
		bf.append("\t").append("����ջ:");
		for(int k = semanticStack.size() - 1; k >= 0;k--){
			bf.append(semanticStack.get(k));
		}
		bf.append("\r\n");
		bf.append("\r\n");
	}
}
private void termOP(String term){
	if(firstWord.type.equals(Word.INT_CONST)||firstWord.type.equals(Word.CHAR_CONST)||
			term.equals(firstWord.value)||
			(term.equals("id")&&firstWord.type.equals(Word.IDENTIFIER)
					)){
		top.value = firstWord.value;
		analyseStack.remove(0);
		wordList.remove(0);
	}else{
		errorCount++;
		analyseStack.remove(0);
		wordList.remove(0);
		error=new Error(errorCount,"error",firstWord.line,firstWord);
		errorList.add(error);
		graErrorFlag=true;
	}	
	
}
private void nonTermOP(String nonTerm){
//	System.out.println("nonTerm = " + nonTerm);
	switch(nonTerm){//ջ��Ϊ���ս������
	case "PROGRAM":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(FUNCTIONS);
			top.firstChild = a;
			analyseStack.add(0,a);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FUNCTIONS":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a1 = new AnalyseNode(FUNCTION);
			top.firstChild = a1;
			analyseStack.add(0,a1);
			AnalyseNode b1 = new AnalyseNode(FUNLIST);
			a1.nextSubling = b1;
			analyseStack.add(1,b1);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FUNLIST":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a1 = new AnalyseNode(FUNCTIONS);
			top.firstChild = a1;
			analyseStack.add(0,a1);
		}else if(firstWord.value.equals("#")){//����ʽΪ��
			analyseStack.remove(0);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FUNCTION":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a1 = new AnalyseNode(TYPE);
			top.firstChild = a1;
			analyseStack.add(0,a1);
			AnalyseNode b = new AnalyseNode(TKN_ID);
			a1.nextSubling = b;
			analyseStack.add(1,b);
			AnalyseNode c = new AnalyseNode(TKN_LP);
			b.nextSubling = c;
			analyseStack.add(2,c);
			AnalyseNode d = new AnalyseNode(ARGS);
			c.nextSubling = d;
			analyseStack.add(3,d);
			AnalyseNode e = new AnalyseNode(TKN_RP);
			d.nextSubling = e;
			analyseStack.add(4,e);
			AnalyseNode f = new AnalyseNode(FUNC_BODY);
			e.nextSubling = f;
			analyseStack.add(5,f);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		
		break;
	case "ARGS":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a0 = new AnalyseNode(FARGS);
			top.firstChild = a0;
			analyseStack.add(0,a0);
			AnalyseNode b0 = new AnalyseNode(ALIST);
			a0.nextSubling = b0;
			analyseStack.add(1,b0);
		}else if(firstWord.value.equals(")")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "ALIST":
		if(firstWord.value.equals(",")){
			analyseStack.remove(0);
			AnalyseNode a0 = new AnalyseNode(TKN_COMMA);
			top.firstChild = a0;
			analyseStack.add(0,a0);
			AnalyseNode b0 = new AnalyseNode(FARGS);
			a0.nextSubling = b0;
			analyseStack.add(1,b0);
			AnalyseNode c0 = new AnalyseNode(ALIST);
			b0.nextSubling = c0;
			analyseStack.add(2,c0);
		}else if(firstWord.value.equals(")")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FARGS":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a0 = new AnalyseNode(TYPE);
			top.firstChild = a0;
			analyseStack.add(0,a0);
			AnalyseNode b0 = new AnalyseNode(TKN_ID);
			a0.nextSubling = b0;
			analyseStack.add(1,b0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FUNC_BODY":
		if(firstWord.value.equals("{")){
			analyseStack.remove(0);
			AnalyseNode a0 = new AnalyseNode(TKN_LB);
			top.firstChild = a0;
			analyseStack.add(0,a0);
			AnalyseNode b0 = new AnalyseNode(STMTS);
			a0.nextSubling = b0;
			analyseStack.add(1,b0);
			AnalyseNode c0 = new AnalyseNode(TKN_RB);
			b0.nextSubling = c0;
			analyseStack.add(2,c0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "STMTS":
		if(firstWord.value.equals("(") || firstWord.type.equals(Word.IDENTIFIER) || 
				firstWord.value.equals("int") || firstWord.value.equals("float") ||
				firstWord.value.equals("return") || firstWord.value.equals("for") || 
				firstWord.value.equals("if") || firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			AnalyseNode a01 = new AnalyseNode(STMT);
			top.firstChild = a01;
			analyseStack.add(0,a01);
			AnalyseNode b01 = new AnalyseNode(STMTS);
			a01.nextSubling = b01;
			analyseStack.add(1,b01);
		}else if(firstWord.value.equals("}")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "STMT":
		if(firstWord.value.equals("(") || firstWord.type.equals(Word.IDENTIFIER) || 
				firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			AnalyseNode a01 = new AnalyseNode(EXPR_STMT);
			top.firstChild = a01;
			analyseStack.add(0,a01);
		}else if(firstWord.value.equals("return")){
			analyseStack.remove(0);
			AnalyseNode a01 = new AnalyseNode(RET_STMT);
			top.firstChild = a01;
			analyseStack.add(0,a01);
		}else if(firstWord.value.equals("for")){
			analyseStack.remove(0);
			AnalyseNode a01 = new AnalyseNode(FOR_STMT);
			top.firstChild = a01;
			analyseStack.add(0,a01);
		}else if(firstWord.value.equals("if")){
			analyseStack.remove(0);
			AnalyseNode a01 = new AnalyseNode(IF_STMT);
			top.firstChild = a01;
			analyseStack.add(0,a01);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "EXPR_STMT":
		if(firstWord.value.equals("(") || firstWord.type.equals(Word.IDENTIFIER) || 
				firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			AnalyseNode a01 = new AnalyseNode(EXPR);
			top.firstChild = a01;
			analyseStack.add(0,a01);
			AnalyseNode b01 = new AnalyseNode(TKN_SEMICOLON);
			a01.nextSubling = b01;
			analyseStack.add(1,b01);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "EXPR":
		if(firstWord.value.equals("(") || firstWord.type.equals(Word.IDENTIFIER) || 
				firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(FACTOR);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(FLIST);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
		}else if(firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.value.equals(";") || firstWord.value.equals(")")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(EARGS);
			top.firstChild = a;
			analyseStack.add(0,a);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FACTOR":
		if(firstWord.type.equals(Word.IDENTIFIER)){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_ID);
			top.firstChild = a2;
			analyseStack.add(0,a2);
		}else if(firstWord.value.equals("(")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_LP);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(EXPR);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
			AnalyseNode c01 = new AnalyseNode(TKN_RP);
			b2.nextSubling = c01;
			analyseStack.add(2,c01);
		}else if(firstWord.type.equals(Word.INT_CONST)){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_CONST_I);
			top.firstChild = a2;
			analyseStack.add(0,a2);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FLIST":
		if(firstWord.value.equals("=")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_ASN);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(FACTOR);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
			AnalyseNode c2 = new AnalyseNode(FLIST);
			b2.nextSubling = c2;
			analyseStack.add(2,c2);
		}else if(firstWord.value.equals("+")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_PLUS);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(FACTOR);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
			AnalyseNode c2 = new AnalyseNode(FLIST);
			b2.nextSubling = c2;
			analyseStack.add(2,c2);
		}else if(firstWord.value.equals("<")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_LESS);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(FACTOR);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
			AnalyseNode c2 = new AnalyseNode(FLIST);
			b2.nextSubling = c2;
			analyseStack.add(2,c2);
		}else if(firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.value.equals(")") || firstWord.value.equals(";")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "EARGS":
		if(firstWord.value.equals("int") || firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(FARGS);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(EALIST);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
		}else if(firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.value.equals(")") || firstWord.value.equals(";")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "EALIST":
		if(firstWord.value.equals(",")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_COMMA);
			top.firstChild = a2;
			analyseStack.add(0,a2);
			AnalyseNode b2 = new AnalyseNode(TKN_ID);
			a2.nextSubling = b2;
			analyseStack.add(1,b2);
			AnalyseNode c2 = new AnalyseNode(EALIST);
			b2.nextSubling = c2;
			analyseStack.add(2,c2);
		}else if(firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.value.equals(")") || firstWord.value.equals(";")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "RET_STMT":
		if(firstWord.value.equals("return")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(TKN_KW_RET);
			top.firstChild = a;
			analyseStack.add(0,a);
			AnalyseNode b = new AnalyseNode(EXPR_STMT);
			a.nextSubling = b;
			analyseStack.add(1,b);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "FOR_STMT":
		if(firstWord.value.equals("for")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(TKN_FOR);
			top.firstChild = a;
			analyseStack.add(0,a);
			AnalyseNode b = new AnalyseNode(TKN_LP);
			a.nextSubling = b;
			analyseStack.add(1,b);
			AnalyseNode c = new AnalyseNode(EXPR_STMT);
			b.nextSubling = c;
			analyseStack.add(2,c);
			AnalyseNode d = new AnalyseNode(EXPR_STMT);
			c.nextSubling = d;
			analyseStack.add(3,d);
			AnalyseNode e = new AnalyseNode(EXPR);
			d.nextSubling = e;
			analyseStack.add(4,e);
			AnalyseNode f = new AnalyseNode(TKN_RP);
			e.nextSubling = f;
			analyseStack.add(5,f);
			AnalyseNode g = new AnalyseNode(TKN_LB);
			f.nextSubling = g;
			analyseStack.add(6,g);
			AnalyseNode h = new AnalyseNode(STMTS);
			g.nextSubling = h;
			analyseStack.add(7,h);
			AnalyseNode i = new AnalyseNode(TKN_RB);
			h.nextSubling = i;
			analyseStack.add(8,i);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "IF_STMT":
		if(firstWord.value.equals("if")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(TKN_IF);
			top.firstChild = a;
			analyseStack.add(0,a);
			AnalyseNode b = new AnalyseNode(TKN_LP);
			a.nextSubling = b;
			analyseStack.add(1,b);
			AnalyseNode c = new AnalyseNode(EXPR);
			b.nextSubling = c;
			analyseStack.add(2,c);
			AnalyseNode d = new AnalyseNode(TKN_RP);
			c.nextSubling = d;
			analyseStack.add(3,d);
			AnalyseNode e = new AnalyseNode(TKN_LB);
			d.nextSubling = e;
			analyseStack.add(4,e);
			AnalyseNode f = new AnalyseNode(STMTS);
			e.nextSubling = f;
			analyseStack.add(5,f);
			AnalyseNode g = new AnalyseNode(TKN_RB);
			f.nextSubling = g;
			analyseStack.add(6,g);
			AnalyseNode h = new AnalyseNode(ELSEIF);
			g.nextSubling = h;
			analyseStack.add(7,h);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "ELSEIF":
		if(firstWord.value.equals("else")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(TKN_ELSE);
			top.firstChild = a;
			analyseStack.add(0,a);
			AnalyseNode b = new AnalyseNode(ILIST);
			a.nextSubling = b;
			analyseStack.add(1,b);
		}else if(firstWord.value.equals("(") || firstWord.type.equals(Word.IDENTIFIER) || 
				firstWord.value.equals("int") || firstWord.value.equals("float") || 
				firstWord.value.equals("ret") || firstWord.value.equals("for") || 
				firstWord.value.equals("if")){
			analyseStack.remove(0);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "ILIST":
		if(firstWord.value.equals("if")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(IF_STMT);
			top.firstChild = a;
			analyseStack.add(0,a);
		}else if(firstWord.value.equals("{")){
			analyseStack.remove(0);
			AnalyseNode a = new AnalyseNode(TKN_LB);
			top.firstChild = a;
			analyseStack.add(0,a);
			AnalyseNode b = new AnalyseNode(STMTS);
			a.nextSubling = b;
			analyseStack.add(1,b);
			AnalyseNode c = new AnalyseNode(TKN_RB);
			b.nextSubling = c;
			analyseStack.add(2,c);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	case "TYPE":
		if(firstWord.value.equals("int")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_INT);
			top.firstChild = a2;
			analyseStack.add(0,a2);
		}else if(firstWord.value.equals("float")){
			analyseStack.remove(0);
			AnalyseNode a2 = new AnalyseNode(TKN_FLOAT);
			top.firstChild = a2;
			analyseStack.add(0,a2);
		}else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"error",firstWord.line,firstWord);
			errorList.add(error);	
			graErrorFlag=true;
		}
		break;
	default:
		break;
	}
}
public String outputLL1() throws IOException{
//	File file=new File(output);
//	if(!file.exists()){
//		file.mkdirs();
//		file.createNewFile();//�������ļ������ھʹ�����
//	}
	String path = ".\\input\\LL1.log";
	FileOutputStream fos = new FileOutputStream(path);
	
	BufferedOutputStream bos = new BufferedOutputStream(fos); 
	OutputStreamWriter osw1 = new OutputStreamWriter(bos,"utf-8");
	PrintWriter pw1 = new PrintWriter(osw1);
	pw1.println(bf.toString());
	bf.delete(0, bf.length());
	if(graErrorFlag){
		Error error;
		pw1.println("������Ϣ���£�");
		pw1.println("�������\t������Ϣ\t���������� \t���󵥴�");
		for(int i=0;i<errorList.size();i++){
			error=errorList.get(i);
			pw1.println(error.id+"\t"+error.info+"\t\t"+error.line+"\t"+error.word.value);
		}
	}else {
		pw1.println("�﷨����ͨ��");
	}
	pw1.close();
	return path;
}
public String outputTree(String output) throws IOException{
	String path = output;
	/*
	 * ��S(��ʼԪ��)��ʼ�����﷨�������������������->��һ������->�������ӵ�˳��
	 * �����趨�����ݽṹ������˳��Ϊnode, node.nextsubling, node.firstChild
	 * ÿ����һ���ڵ㣬������븸�ڵ��element�У��Ӷ����xml��ʽ�����
	 * */
	
	// �������ڵ� �������������� ;   
    Element root = new Element("ParserTree").setAttribute("name", "test.tree.xml");   
    // �����ڵ���ӵ��ĵ��У�   
    Document Doc = new Document(root);
    // ����start�ڵ㲢��ӵ�root
    Element start = new Element("PROGRAM"); 
    root.addContent(start);
	
    AnalyseNode node = PROGRAM.firstChild;
    deal(start, node);
    
    // ParserTree.xml �ļ���  
    // ʹxml�ļ�����Ч��
    Format format = Format.getPrettyFormat();
    XMLOutputter XMLOut = new XMLOutputter(format);
    XMLOut.output(Doc, new FileOutputStream(path));
	
	return path;
}
private void deal (Element e, AnalyseNode node){//��node����sublings����e��
	if((node==null) || node.hasDealt){
		return;
	}else{
		Element element;
		if(node.wordType == null){
			switch(node.name){
			case "+":
				node.value = "plus";
				break;
			case "-":
				node.value = "minus";
				break;
			case "*":
				node.value = "mul";
				break;
			case "/":
				node.value = "div";
				break;
			case ",":
				node.value = "comma";
				break;
			case ";":
				node.value = "semicolon";
				break;
			case "(":
				node.value = "lp";
				break;
			case ")":
				node.value = "rp";
				break;
			case "{":
				node.value = "lb";
				break;
			case "}":
				node.value = "rb";
				break;
			default :
				break;
			}
			if(node.value == null){
//				System.out.println(node.name + "->" + node.value);
				element = new Element(node.name);//����node�ڵ�
			}else{
//				System.out.println(node.name + "->" + node.value);
				element = new Element(node.value).setText(node.name + "->" + node.value);//����node�ڵ�
			}
		}else{
//			System.out.println(node.name + "->" + node.value);
			element = new Element(node.wordType).setText(node.value);//����node�ڵ�
		}
		e.addContent(element);
		node.hasDealt = true;
		AnalyseNode temp = node.nextSubling;
		if(temp != null){//����sublings
			deal(e, temp);
		}
		deal(element, node.firstChild);//�����ӽڵ�
	}
}

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		// TODO Auto-generated method stub
		Parser parser = new Parser(".\\input\\test.token.xml");
		parser.grammerAnalyse();
		parser.outputLL1();
		parser.outputTree(".\\input\\test.tree.xml");
		System.out.println("Parsing Finished!");
	}

}
