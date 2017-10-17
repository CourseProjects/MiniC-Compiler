package bit.minisys.minicc.parser;

import java.util.ArrayList;

import bit.minisys.minicc.scanner.Word;

/**
 * 分析栈节点类
 * @author WYF
 *	
 */
public class AnalyseNode {
	String type;//节点类型
	String name;//节点名
	String value;//节点值
	AnalyseNode firstChild, nextSubling;//构造语法树
	boolean hasDealt;//标志已经在xml文件中处理过
	String wordType;//节点单词的类型

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
	static ArrayList<String>nonterminal=new ArrayList<String>();//非终结符集合
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
