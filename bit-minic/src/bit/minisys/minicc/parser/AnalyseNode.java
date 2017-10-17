package bit.minisys.minicc.parser;

import java.util.ArrayList;

import bit.minisys.minicc.scanner.Word;

/**
 * ����ջ�ڵ���
 * @author WYF
 *	
 */
public class AnalyseNode {
	String type;//�ڵ�����
	String name;//�ڵ���
	String value;//�ڵ�ֵ
	AnalyseNode firstChild, nextSubling;//�����﷨��
	boolean hasDealt;//��־�Ѿ���xml�ļ��д����
	String wordType;//�ڵ㵥�ʵ�����

	public AnalyseNode(){
		
	}
	public AnalyseNode(String type,String name,String value, String wordType){
		this.type=type;
		this.name=name;
		this.value=value;
		this.hasDealt = false;
		this.wordType = wordType;
	}
	public AnalyseNode(AnalyseNode node){
		this.type=node.type;
		this.name=node.name;
		this.value=node.value;
		this.hasDealt = node.hasDealt;
		this.wordType = node.wordType;
	}
	
	
	public final static String NONTERMINAL="nonterminal";
	public final static String TERMINAL="terminal";
	public final static String END="#";
	static ArrayList<String>nonterminal=new ArrayList<String>();//���ս������
	static{
		nonterminal.add("PROGRAM");
		nonterminal.add("FUNCTIONS");
		nonterminal.add("FUNLIST");
		nonterminal.add("FUNCTION");
		nonterminal.add("ARGS");
		nonterminal.add("ALIST");
		nonterminal.add("FARGS");
		nonterminal.add("FUNC_BODY");
		nonterminal.add("STMTS");
		nonterminal.add("STMT");
		nonterminal.add("EXPR_STMT");
		nonterminal.add("EXPR");
		nonterminal.add("FACTOR");
		nonterminal.add("FLIST");
		nonterminal.add("EARGS");
		nonterminal.add("EALIST");
		nonterminal.add("RET_STMT");
		nonterminal.add("FOR_STMT");
		nonterminal.add("IF_STMT");
		nonterminal.add("ELSEIF");
		nonterminal.add("ILIST");
		nonterminal.add("TYPE");
	}
	
	
	public static boolean isNonterm(AnalyseNode node){
		return nonterminal.contains(node.name);
	}
	public static boolean isTerm(AnalyseNode node){
		return Word.isKey(node.name)||Word.isOperator(node.name)||Word.isBoundarySign(node.name)
		||node.name.equals("id")||node.name.equals("num")||node.name.equals("ch") || 
		node.name.equals("const_i");
	}

}
