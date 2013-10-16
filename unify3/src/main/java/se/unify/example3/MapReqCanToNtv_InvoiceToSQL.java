package se.unify.example3;

import java.sql.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MapReqCanToNtv_InvoiceToSQL implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		CanonicalInvoice invoice = exchange.getIn().getBody(CanonicalInvoice.class);
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO INVOICE (id, time_occurred, status_code, sum) VALUES (");
		sb.append("'").append(invoice.id).append("', ");
		sb.append("'").append(new Date(invoice.date.getTime())).append("', ");
		sb.append("'").append("1").append("', ");
		sb.append("'").append(invoice.sum).append("') ");

		String sql = sb.toString();
		System.out.println(" **** Mapped into SQL: " + sql);
		
		exchange.getIn().setBody(sql);
	}

}
