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
 * 词法分析器
 * 
 * @author WYF
 * 用到了Error.java和Word.java
 * 
 */
public class LexAnalyse {

	ArrayList<Word> wordList = new ArrayList<Word>();// 单词表
	ArrayList<Error> errorList = new ArrayList<Error>();// 错误信息列表
	int wordCount = 0;// 统计单词个数
	int errorCount = 0;// 统计错误个数
	boolean noteFlag = false;// 多行注释标志
	boolean lexErrorFlag = false;// 词法分析出错标志
	public LexAnalyse() {

	}

	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	/**
	 * 数字字符判断
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
	 * 判断单词是否为int常量
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
//	 * 判断单词是否为char常量
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
	 * 判断字符是否为字母
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
	 * 判断单词是否为合法标识符
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
	 * 判断词法分析是否通过
	 * 
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	public void analyse(String str, int line) { // 暂未处理字符串常量,<=,>=,<<,>>
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
				if (isLetter(temp) || temp == '_') {// 判断是不是标志符
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
						error = new Error(errorCount, "非法标识符", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;//为了配合if之后的index++语句

				} else if (isDigit(temp)) {// 判断是不是int常数

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
						error = new Error(errorCount, "非法标识符", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} else if (String.valueOf(str.charAt(index)).equals("'")) {// 字符常量,temp == '\''?
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
						error = new Error(errorCount, "非法标识符", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
						index--;
					}
				} else if (temp == '=') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {// "=="
						endIndex = index + 1;//endIndex取index+1,则不需要最后的index--
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
						break;//如果是行注释则跳出，表示不分析这一行。可是行注释应该是预处理时候删掉的内容
					/*
					 * { index++; while(str.charAt(index)!='\n'){ index++; } }
					 */
					else if (index < length && str.charAt(index) == '*') {
						noteFlag = true;//如果是块注释则将注释标记置位真，可是块注释应该是预处理时候删掉的内容
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
				} else {// 不是标识符、数字常量、字符串常量

					switch (temp) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;// 过滤空白字符
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
						error = new Error(errorCount, "非法标识符", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
				}
			} else {
				int i = str.indexOf("*/");
				if (i != -1) {// 跳过块注释部分，但是块注释应该是预处理时候删掉的内容
					noteFlag = false;
					index = i + 2;
					continue;
				} else// 此行都是块注释的内容，跳出循环，不再分析
					break;
			}
			if (word == null) {// 表明是空白字符，滤过
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
		
		// 创建根节点 并设置它的属性 ;   
        Element root = new Element("project").setAttribute("name", "test.l");   
        // 将根节点添加到文档中；   
        Document Doc = new Document(root);
        // 创建tokens节点并添加到root
        Element tokens = new Element("tokens"); 
        root.addContent(tokens);
        
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			
			// 创建节点 token;   
	        Element elements = new Element("token");     
	        // 给tokens节点添加子节点并赋值;
	        elements.addContent(new Element("number").setText(new Integer(word.id).toString()));
	        elements.addContent(new Element("value").setText(word.value));
	        elements.addContent(new Element("type").setText(word.type));
	        elements.addContent(new Element("line").setText(new Integer(word.line).toString()));
	        elements.addContent(new Element("valid").setText(new Boolean(word.flag).toString()));
	        // 把节点添加到root中
	        tokens.addContent(elements);
		}
		if (lexErrorFlag) {
			Error error;
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				// 创建节点错误信息;   
		        Element elements = new Element("错误信息");     
		        // 给tokens节点添加子节点并赋值;
		        elements.addContent(new Element("错误序号").setText(new Integer(error.id).toString()));
		        elements.addContent(new Element("错误信息").setText(error.info));
		        elements.addContent(new Element("错误所在行").setText(new Integer(error.line).toString()));
		        elements.addContent(new Element("错误单词").setText(error.word.value));
		        // 把节点添加到root中
		        root.addContent(elements);
		        
			}
		}
		
		// 输出 test.token.xml 文件；  
        // 使xml文件缩进效果
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
