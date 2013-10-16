package io.ipaas.platformhelpers.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "status")
public class Status extends CanonicalModel implements Serializable {

	private static final long serialVersionUID = 3245848455552871775L;

	// NoArgsConstructor is mandatory for JAXB
	public Status() {

	}

	public Status(String msg, int code) {
		this.msg = msg;
		this.code = code;
	}

	@XmlElement(name = "msg")
	public String msg;

	@XmlElement(name = "code")
	public int code;

}
