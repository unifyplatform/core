package se.unify;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class ControllerService {

    @PUT
    @Path("/invoices")
    @Produces("application/xml")
    public Response newInvoice(Invoice invoice) throws Exception {
        return Response.ok().type("application/xml").entity(invoice).build();
    }

}
