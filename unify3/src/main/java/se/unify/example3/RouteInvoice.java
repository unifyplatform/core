package se.unify.example3;

import io.ipaas.platformhelpers.UnifyRouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class RouteInvoice extends UnifyRouteBuilder {

	// Verify this service by calling:
	// curl -X POST -H "Content-Type:application/xml" -d @invoice.xml 192.168.33.10:9091/se-unify-example3/restapi/invoice/create
	// @invoice.xml is the xml-file in test/resources

	@Override
	public void configure() throws Exception {
		super.configure();
		Class<CanonicalInvoice> canModel = CanonicalInvoice.class;
		
		// Inbound endpoint is syncronous and RESTful endpoint, 
		// response is not mapped (null response Processor)
		inSyncRestCan(IRestInvoice.class, canModel);
		
		// outbound endpoint is syncronous defined by custom URL 
		// default response is generated (null response Processor).
		outSyncCustom(canModel, new MapReqCanToNtv_InvoiceToSQL(), 
				"jdbc:myDataSource", null);
		
	}
}