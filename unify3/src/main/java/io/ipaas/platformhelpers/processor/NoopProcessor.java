package io.ipaas.platformhelpers.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class NoopProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("** noop processor invoked **");
	}
}
