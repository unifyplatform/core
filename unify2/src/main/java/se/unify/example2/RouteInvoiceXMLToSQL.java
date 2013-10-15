package se.unify.example2;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class RouteInvoiceXMLToSQL extends RouteBuilder {
  
	// Verify this service by calling:
	// curl -X PUT -H "Content-Type:application/xml" -d @invoice.xml 192.168.33.10:9090/api/invoices
	// @invoice.xml is the xml-file in test/resources
	
	String cxfrsUrl = "http://0.0.0.0:9090/api?"
			+ "resourceClasses=" + IInvoiceService.class.getName();
	
	@Override
    public void configure() {
		
		// Inbound endpoint
        from("cxfrs://" + cxfrsUrl)
	        .to("log:se.unify2?level=INFO") // Log before to canonical
	        //TODO: Add input validation of native format
	        .process(new MapToInvoice()) // Map to canonical format
	        .to("log:se.unify2?level=INFO") // Log after to canonical
	        .to("activemq://canonical_invoice"); // Publish canonical messages, in this case the invoice object is the canonical format...
	    
        // outbound endpoint
        from("activemq://canonical_invoice")
	        .to("log:se.unify2?level=INFO")
	        //TODO: Add input validation of canonical format
	        .process(new MapToSQL())
	        .to("jdbc:myDataSource"); // myDataSource is configured in spring, could not find how to do it in java
    }
}