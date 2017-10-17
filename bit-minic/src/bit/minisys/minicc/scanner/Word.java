package bit.minisys.minicc.scanner;

import java.util.ArrayList;


/**
 * 单词类
 * 
 * @author Administrator 1、单词序号 2、单词的值 3、单词类型 4、单词所在行 5、单词是否合法
 */
public class Word {
	public final static String KEY = "keyword";
	public final static String OPERATOR = "operator";
	public final static String INT_CONST = "const_i";
	public final static String CHAR_CONST = "const_c";
	public final static String BOOL_CONST = "const_b";
	public final static String IDENTIFIER = "identifier";
	public final static String BOUNDARYSIGN = "separator";
	public final static String END = "#";
	public final static String UNIDEF = "undefined";
	public static ArrayList<String> key = new ArrayList<String>();// 关键字集合
	public static ArrayList<String> boundarySign = new ArrayList<String>();// 界符集合
	public static ArrayList<String> operator = new ArrayList<String>();// 运算符集合
	static {
		Word.operator.add("+");//+
		Word.operator.add("-");//-
		Word.operator.add("++");
		Word.operator.add("--");
		Word.operator.add("*");//*
		Word.operator.add("/");///
		Word.operator.add(">");
		Word.operator.add("<");
		Word.operator.add(">=");
		Word.operator.add("<=");
		Word.operator.add("==");
		Word.operator.add("!=");
		Word.operator.add("=");
		Word.operator.add("&&");
		Word.operator.add("||");
		Word.operator.add("!");
		Word.operator.add(".");
		Word.operator.add("?");
		Word.operator.add("|");
		Word.operator.add("&");
		Word.boundarySign.add("(");//(
		Word.boundarySign.add(")");//)
		Word.boundarySign.add("{");//{
		Word.boundarySign.add("}");//}
		Word.boundarySign.add(";");//;
		Word.boundarySign.add(",");//,
		Word.key.add("void");
		Word.key.add("int");
		Word.key.add("if");
		Word.key.add("else");
		Word.key.add("for");
		Word.key.add("return");
		Word.key.add("float");
		Word.key.add("char");
		Word.key.add("while");
		Word.key.add("printf");
		Word.key.add("scanf");
	}
	public int id;// 单词序号
	public String value;// 单词的值
	public String type;// 单词类型
	public int line;// 单词所在行
	public boolean flag = true;//单词是否合法

	public Word() {

	}

	public Word(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	public static boolean isKey(String word) {
		return key.contains(word);
	}

	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	public static boolean isBoundarySign(String word) {
		return boundarySign.contains(word);
	}

	public static boolean isArOP(String word) {// 判断单词是否为算术运算符
		if ((word.equals("+") || word.equals("-") || word.equals("*") || word
				.equals("/")))
			return true;
		else
			return false;
	}

	public static boolean isBoolOP(String word) {// 判断单词是否为布尔运算符
		if ((word.equals(">") || word.equals("<") || word.equals("==")
				|| word.equals("!=") || word.equals("!") || word.equals("&&") || word
				.equals("||")))
			return true;
		else
			return false;
	}
}
