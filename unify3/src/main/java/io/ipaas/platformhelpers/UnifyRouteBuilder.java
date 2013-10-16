package io.ipaas.platformhelpers;

import io.ipaas.platformhelpers.processor.CommonResponseProcessor;
import io.ipaas.platformhelpers.processor.NoopProcessor;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

public class UnifyRouteBuilder extends RouteBuilder {

	String logSettings = "?level=INFO&showAll=true";
	
	public RouteDefinition inSyncRestCan(Class ntvRestInterface, Class canModel) {
		return inSyncRest(ntvRestInterface, canModel, null, null);
	}
	
	private RouteDefinition inSyncRest(Class ntvRestInterface, Class canModel, Processor ntvToCan, Processor respToNtv) {

		String routeSpecificPackage = ntvRestInterface.getPackage().getName();
		String cxfrsUrl = "http://0.0.0.0:9091" 
				+ "/" + routeSpecificPackage.replaceAll("\\.", "-") 
				+ "/restapi"  
				+ "?resourceClasses=" + ntvRestInterface.getName();

		RouteDefinition rdef = null;
		rdef = from("cxfrs://" + cxfrsUrl);									// Get message from cxf REST endpoint
		
		if (ntvToCan == null)
			rdef.to("log:inReqCan_" + routeSpecificPackage + logSettings);	// No mapping, logging as canonical
		else
			rdef.to("log:inReqNtv_" + routeSpecificPackage + logSettings)	// Log native message format
				.process(ntvToCan)											// Map to canonical format using custom processor
				.to("log:inReqCan" + routeSpecificPackage + logSettings); 	// Log after mapping
		
		rdef.to("activemq://canonical_" + canModel.getName())				// Send canonical to broker
			.to("log:inRespCan" + routeSpecificPackage + logSettings); 		// Log after mapping
			
		return rdef;
	}

	public RouteDefinition outSyncCustom(Class canonicalFormat, Processor canToNtv, String toUrl, Processor respToCan ) {
		
		String routeSpecificPackage = canToNtv.getClass().getPackage().getName();
		
		RouteDefinition rdef = null;
		rdef = from("activemq://canonical_" + canonicalFormat.getName()); 	// Pickup canonical
		rdef.to("log:outReqCan" + routeSpecificPackage + logSettings)		// Log incoming canonical
			.process(canToNtv) 												// Map to outbound native
			.to("log:outReqNtv" + routeSpecificPackage + logSettings)		// Log native format
			.to(toUrl)														// Send to native
			.to("log:outRespNtv_" + routeSpecificPackage + logSettings);	// Log native Response format
		if (respToCan == null) {
			rdef.process(new CommonResponseProcessor());					// Do platform default response mapping 
		}
		else
			rdef.process(respToCan);										// Do custom response mapping
		
		rdef.to("log:outRespCan_" + routeSpecificPackage + logSettings);	// Log canonical Response format
		return rdef;														// Returns route definition, so called can extend it if needed
	}

	@Override
	public void configure() throws Exception {
		// pattern independant routes for managing the service
		// i.e. config cache reload, testing info and lifecycle status
		
		
	}
}