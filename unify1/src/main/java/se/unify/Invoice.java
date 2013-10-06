package se.unify;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "invoice")
public class Invoice implements Serializable {
    
    /**
	 * Enabled a static unique ID for serialization
	 */
	private static final long serialVersionUID = -7780878522595583787L;

	@XmlAttribute(name = "id")
    protected int id;
 
    @XmlElement(name = "date", required = true)
    protected Date date;
 
    @XmlElement(name = "sum", required = true)
    protected Integer sum;
 
}
