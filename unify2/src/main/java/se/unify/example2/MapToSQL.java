package se.unify.example2;

import java.sql.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MapToSQL implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String sql = beanToSql(exchange.getIn().getBody(Invoice.class));
		exchange.getIn().setBody(sql);
	}

	public String beanToSql(Invoice invoice) {

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO INVOICE (id, time_occurred, status_code, sum) VALUES (");
		sb.append("'").append(invoice.id).append("', ");
		sb.append("'").append(new Date(invoice.date.getTime())).append("', ");
		sb.append("'").append("1").append("', ");
		sb.append("'").append(invoice.sum).append("') ");

		System.out.println(" **** toSql returning: " + sb.toString());

		return sb.toString();
	}

}
