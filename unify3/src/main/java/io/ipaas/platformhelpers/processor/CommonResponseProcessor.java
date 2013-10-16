package io.ipaas.platformhelpers.processor;

import io.ipaas.platformhelpers.model.Status;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CommonResponseProcessor implements Processor  {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		exchange.getOut().setBody(new Status("SUCCESS", 200));
		
	}

}
