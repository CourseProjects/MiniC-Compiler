package bit.minisys.minicc.scanner;

import java.util.ArrayList;


/**
 * ������
 * 
 * @author Administrator 1��������� 2�����ʵ�ֵ 3���������� 4������������ 5�������Ƿ�Ϸ�
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
	public static ArrayList<String> key = new ArrayList<String>();// �ؼ��ּ���
	public static ArrayList<String> boundarySign = new ArrayList<String>();// �������
	public static ArrayList<String> operator = new ArrayList<String>();// ���������
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
	public int id;// �������
	public String value;// ���ʵ�ֵ
	public String type;// ��������
	public int line;// ����������
	public boolean flag = true;//�����Ƿ�Ϸ�

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

	public static boolean isArOP(String word) {// �жϵ����Ƿ�Ϊ���������
		if ((word.equals("+") || word.equals("-") || word.equals("*") || word
				.equals("/")))
			return true;
		else
			return false;
	}

	public static boolean isBoolOP(String word) {// �жϵ����Ƿ�Ϊ���������
		if ((word.equals(">") || word.equals("<") || word.equals("==")
				|| word.equals("!=") || word.equals("!") || word.equals("&&") || word
				.equals("||")))
			return true;
		else
			return false;
	}
}
