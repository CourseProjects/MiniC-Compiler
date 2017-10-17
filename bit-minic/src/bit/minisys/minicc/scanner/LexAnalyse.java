package bit.minisys.minicc.scanner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * �ʷ�������
 * 
 * @author WYF
 * �õ���Error.java��Word.java
 * 
 */
public class LexAnalyse {

	ArrayList<Word> wordList = new ArrayList<Word>();// ���ʱ�
	ArrayList<Error> errorList = new ArrayList<Error>();// ������Ϣ�б�
	int wordCount = 0;// ͳ�Ƶ��ʸ���
	int errorCount = 0;// ͳ�ƴ������
	boolean noteFlag = false;// ����ע�ͱ�־
	boolean lexErrorFlag = false;// �ʷ����������־
	public LexAnalyse() {

	}

	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	/**
	 * �����ַ��ж�
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isDigit(char ch) {
		boolean flag = false;
		if ('0' <= ch && ch <= '9')
			flag = true;
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊint����
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isInteger(String word) {
		int i;
		boolean flag = false;
		for (i = 0; i < word.length(); i++) {
			if (Character.isDigit(word.charAt(i))) {
				continue;
			} else {
				break;
			}
		}
		if (i == word.length()) {
			flag = true;
		}
		return flag;
	}

//	/**
//	 * �жϵ����Ƿ�Ϊchar����
//	 * 
//	 * @param word
//	 * @return
//	 */
//	private static boolean isChar(String word) {
//		boolean flag = false;
//		int i = 0;
//		char temp = word.charAt(i);
//		if (temp == '\'') {
//			for (i = 1; i < word.length(); i++) {
//				temp = word.charAt(i);
//				if (0 <= temp && temp <= 255)
//					continue;
//				else
//					break;
//			}
//			if (i + 1 == word.length() && word.charAt(i) == '\'')
//				flag = true;
//		} else
//			return flag;
//
//		return flag;
//	}

	/**
	 * �ж��ַ��Ƿ�Ϊ��ĸ
	 * 
	 * @param ch
	 * @return
	 */
	private static boolean isLetter(char ch) {
		boolean flag = false;
		if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))
			flag = true;
		return flag;
	}

	/**
	 * �жϵ����Ƿ�Ϊ�Ϸ���ʶ��
	 * 
	 * @param word
	 * @return
	 */
	private static boolean isID(String word) {
		boolean flag = false;
		int i = 0;
		if (Word.isKey(word))
			return flag;
		char temp = word.charAt(i);
		if (isLetter(temp) || temp == '_') {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (isLetter(temp) || temp == '_' || isDigit(temp))
					continue;
				else
					break;
			}
			if (i >= word.length())
				flag = true;
		} else
			return flag;

		return flag;
	}

	/**
	 * �жϴʷ������Ƿ�ͨ��
	 * 
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	public void analyse(String str, int line) { // ��δ�����ַ�������,<=,>=,<<,>>
		int beginIndex;
		int endIndex;
		int index = 0;
		int length = str.length();
		Word word = null;
		Error error;
		// boolean flag=false;
		char temp;
		while (index < length) {
			temp = str.charAt(index);
			if (!noteFlag) {
				if (isLetter(temp) || temp == '_') {// �ж��ǲ��Ǳ�־��
					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,
									index + 1)))
							&& (!Word.isOperator(str
									.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
						// temp=str.charAt(index);
					}
					endIndex = index;
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.value = str.substring(beginIndex, endIndex);
					if (Word.isKey(word.value)) {
						word.type = Word.KEY;
					} else if (isID(word.value)) {
						word.type = Word.IDENTIFIER;
					} else {
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;//Ϊ�����if֮���index++���

				} else if (isDigit(temp)) {// �ж��ǲ���int����

					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,
									index + 1)))
							&& (!Word.isOperator(str
									.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
						// temp=str.charAt(index);
					}
					endIndex = index;
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.value = str.substring(beginIndex, endIndex);
					if (isInteger(word.value)) {
						word.type = Word.INT_CONST;
					} else {
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} else if (String.valueOf(str.charAt(index)).equals("'")) {// �ַ�����,temp == '\''?
					// flag=true;
					beginIndex = index;
					index++;
					temp = str.charAt(index);
					while (index < length && (0 <= temp && temp <= 255)) {
						if (String.valueOf(str.charAt(index)).equals("'"))
							break;
						index++;
						// temp=str.charAt(index);
					}
					if (index < length) {
						endIndex = ++index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.CHAR_CONST;
						// flag=true;
						// word.flag=flag;
						index--;
					} else {
						endIndex = index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
						index--;
					}
				} else if (temp == '=') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {// "=="
						endIndex = index + 1;//endIndexȡindex+1,����Ҫ����index--
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {// "="
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						//word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '!') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {// "!="
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
						//index++;
					} else {// "!"
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '&') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '&') {// "&&"
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {// "&"
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '|') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '|') {// "||"
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {// "|"
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '+') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '+') {// "++"
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;

					} else if (index < length && str.charAt(index) == '=') {// "+="
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;

					}else {// "+"
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '-') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '-') {// "--"
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					}else if (index < length && str.charAt(index) == '=') {// "-="
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					}else {// "-"
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (temp == '/') {
					index++;
					if (index < length && str.charAt(index) == '/')// "//"
						break;//�������ע������������ʾ��������һ�С�������ע��Ӧ����Ԥ����ʱ��ɾ��������
					/*
					 * { index++; while(str.charAt(index)!='\n'){ index++; } }
					 */
					else if (index < length && str.charAt(index) == '*') {
						noteFlag = true;//����ǿ�ע����ע�ͱ����λ�棬���ǿ�ע��Ӧ����Ԥ����ʱ��ɾ��������
					} else {// "/"
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
//					index--;
				} else {// ���Ǳ�ʶ�������ֳ������ַ�������

					switch (temp) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;// ���˿հ��ַ�
					case '[':
					case ']':
					case '(':
					case ')':
					case '{':
					case '}':
					case ',':
					case '"':
					case '.':
					case ';':
						// case '+':
						// case '-':
					case '*':
						// case '/':
					case '%':
					case '>':
					case '<':
					case '?':
					case '#':
						word = new Word();
						wordCount++;
						word.id = wordCount;
//						word.id = ++wordCount;
						word.line = line;
						word.value = String.valueOf(temp);
						if (Word.isOperator(word.value))
							word.type = Word.OPERATOR;
						else if (Word.isBoundarySign(word.value))
							word.type = Word.BOUNDARYSIGN;
						else
							word.type = Word.END;
						break;
					default:
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.value = String.valueOf(temp);
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "�Ƿ���ʶ��", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
				}
			} else {
				int i = str.indexOf("*/");
				if (i != -1) {// ������ע�Ͳ��֣����ǿ�ע��Ӧ����Ԥ����ʱ��ɾ��������
					noteFlag = false;
					index = i + 2;
					continue;
				} else// ���ж��ǿ�ע�͵����ݣ�����ѭ�������ٷ���
					break;
			}
			if (word == null) {// �����ǿհ��ַ����˹�
				index++;
				continue;
			}

			wordList.add(word);
			index++;
		}
	}

	public ArrayList<Word> lexAnalyse(String str) {
		String buffer[];
		buffer = str.split("\n");
		int line = 1;
		for (int i = 0; i < buffer.length; i++) {
			analyse(buffer[i].trim(), line);
			line++;
		}
		if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	public ArrayList<Word> lexAnalyse1(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "utf-8");
		BufferedReader inbr = new BufferedReader(isr);
		String str = "";
		int line = 1;
		while ((str = inbr.readLine()) != null) {
			// System.out.println(str);
			analyse(str.trim(), line);
			line++;
		}
		inbr.close();
		if (!wordList.get(wordList.size() - 1).type.equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	public String outputWordList(String output) throws IOException {
		
		// �������ڵ� �������������� ;   
        Element root = new Element("project").setAttribute("name", "test.l");   
        // �����ڵ���ӵ��ĵ��У�   
        Document Doc = new Document(root);
        // ����tokens�ڵ㲢��ӵ�root
        Element tokens = new Element("tokens"); 
        root.addContent(tokens);
        
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			
			// �����ڵ� token;   
	        Element elements = new Element("token");     
	        // ��tokens�ڵ�����ӽڵ㲢��ֵ;
	        elements.addContent(new Element("number").setText(new Integer(word.id).toString()));
	        elements.addContent(new Element("value").setText(word.value));
	        elements.addContent(new Element("type").setText(word.type));
	        elements.addContent(new Element("line").setText(new Integer(word.line).toString()));
	        elements.addContent(new Element("valid").setText(new Boolean(word.flag).toString()));
	        // �ѽڵ���ӵ�root��
	        tokens.addContent(elements);
		}
		if (lexErrorFlag) {
			Error error;
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				// �����ڵ������Ϣ;   
		        Element elements = new Element("������Ϣ");     
		        // ��tokens�ڵ�����ӽڵ㲢��ֵ;
		        elements.addContent(new Element("�������").setText(new Integer(error.id).toString()));
		        elements.addContent(new Element("������Ϣ").setText(error.info));
		        elements.addContent(new Element("����������").setText(new Integer(error.line).toString()));
		        elements.addContent(new Element("���󵥴�").setText(error.word.value));
		        // �ѽڵ���ӵ�root��
		        root.addContent(elements);
		        
			}
		}
		
		// ��� test.token.xml �ļ���  
        // ʹxml�ļ�����Ч��
        Format format = Format.getPrettyFormat();
        XMLOutputter XMLOut = new XMLOutputter(format);
        XMLOut.output(Doc, new FileOutputStream(output));
        
		return output;
	}


	public static void main(String[] args) throws IOException {
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse1(".\\input\\test.pp.c");
		lex.outputWordList(".\\input\\test.token.xml");
		System.out.println("Lexanalyse finished!");
	}
}
