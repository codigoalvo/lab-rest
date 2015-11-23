package codigoalvo.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
	private String msg;

	public Message() {}

	public Message(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
