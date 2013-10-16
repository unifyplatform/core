package se.unify.example3;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "invoice")
public class CanonicalInvoice implements Serializable {
	
	private static final long serialVersionUID = -9060674721768261982L;

	@XmlAttribute(name = "id")
    public int id;
 
    @XmlElement(name = "date", required = true)
    public Date date;
 
    @XmlElement(name = "sum", required = true)
    public Integer sum;
 
}
