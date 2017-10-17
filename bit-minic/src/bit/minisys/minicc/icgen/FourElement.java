package bit.minisys.minicc.icgen;
/**
 * 四元式节点
 * @author Administrator
 *
 */
public class FourElement {
	int id;//四元式序号，为编程方便
	String op;//操作符
	String arg1;//第一个操作数
	String arg2;//第二个操作数
	Object result;//结果


public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getOp() {
	return op;
}
public void setOp(String op) {
	this.op = op;
}
public String getArg1() {
	return arg1;
}
public void setArg1(String arg1) {
	this.arg1 = arg1;
}
public String getArg2() {
	return arg2;
}
public void setArg2(String arg2) {
	this.arg2 = arg2;
}
public Object getResult() {
	return result;
}
public void setResult(Object result) {
	this.result = result;
}

public FourElement(){
	
}
public FourElement(int id,String op,String arg1,String arg2,String result){
	this.id=id;
	this.op=op;
	this.arg1=arg1;
	this.arg2=arg2;
	this.result=result;	
}
}
