package se.unify.example2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MapToInvoice implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Invoice invoice = xmlToBean(exchange.getIn().getBody(String.class));
		exchange.getIn().setBody(invoice);
	}

	public Invoice xmlToBean(String xml) throws JAXBException {
		Unmarshaller um = JAXBContext.newInstance(Invoice.class).createUnmarshaller();
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		return (Invoice) um.unmarshal(is);
	}

}
